package com.third.IntelPlat.security;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.third.IntelPlat.common.MD5Utils;
import com.third.IntelPlat.rest.Result;

public class BearerTokenAuthenticatingFilter extends AuthenticatingFilter {
	
	private static Logger logger = LoggerFactory.getLogger(BearerTokenAuthenticatingFilter.class);
	
	private static final String TOKEN_HEADER = "Token";
	private static final String TOKEN_SCHEME_Bearer = "Bearer";
	
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response)
	{
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		
		if (isLoginSubmission(request, response)) {		
			
			BearerToken token = parseJson(request);
			
			if(token == null)
			{
				logger.error("token为空,登录失败");
				return BearerToken.emptyToken();
			}
			
			// get from json
			String username = (String) token.getPrincipal();
			String password = MD5Utils.encrypt((String) token.getCredentials());
			
			long createTime = System.currentTimeMillis();
			
			if(username == null || password == null )
			{
				logger.error("token为空,登录失败");
				return BearerToken.emptyToken();
			}
				
			
			return new BearerToken(username, password, createTime, false);	
		}else{
			String authorizeHeader = httpRequest.getHeader(TOKEN_HEADER);
			
			if (!isValidHeader(authorizeHeader)) {
				
				logger.error("token无效,登录失败");
				return BearerToken.emptyToken();
			}
			
			return BearerToken.parseToken(authorizeHeader);
		}
	}
	
	protected boolean isLoginSubmission(ServletRequest request, ServletResponse response) {
		
		return (isLoginRequest(request, response))&&(request instanceof HttpServletRequest) && WebUtils.toHttp(request).getMethod().equalsIgnoreCase(POST_METHOD);
	}	
	
	boolean isValidHeader(String authorizeHeader) {
		
		if (authorizeHeader == null) 
			return false;
		
		// whether bearer auth
		String authorizeScheme = TOKEN_SCHEME_Bearer.toLowerCase(Locale.ENGLISH);
		String test = authorizeHeader.toLowerCase(Locale.ENGLISH);
		if(test.startsWith(authorizeScheme)){
			return true;
		}
		else
			return false;		
	}

	@Override
	protected boolean preHandle(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse res = (HttpServletResponse) servletResponse;
		
		if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
			res.setStatus(HttpStatus.OK.value());
			res.addHeader("Access-Control-Allow-Origin", "*");
			res.addHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Accept,Token");
			res.addHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,HEAD,PUT,DELETE");
			res.addHeader("Access-Control-Expose-Headers", "Token");
			return super.preHandle(request, res);
		}
		return super.preHandle(request, res);
	}
	
	@Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) {
        return super.isAccessAllowed(servletRequest, servletResponse, mappedValue) ||
                (!isLoginRequest(servletRequest, servletResponse) && isPermissive(mappedValue));
    }
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

		HttpServletRequest httpRequest = WebUtils.toHttp(request);		
		String authorizeHeader = httpRequest.getHeader(TOKEN_HEADER);

		// login request or token involved request
		if (isLoginSubmission(request, response) || isValidHeader(authorizeHeader)) {

			 return executeLogin(request, response);
		}		
		else {		
			
//			HttpServletResponse res =  WebUtils.toHttp(response);
//			JSONObject json = new JSONObject();
//			json.put("status", false);
//			json.put("err", ErrDesc.NEED_LOGIN);
//			String error = json.toString();
//			logger.info(ErrDesc.NEED_LOGIN);
//			res.getWriter().write(error);
			
			// don't go another filter again
			return false;
		}
	}	
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
		
		if (isLoginRequest(request, response)) {
			
			// create token and return to client here
			BearerToken authToken = (BearerToken) token;
			
			HttpServletResponse res =  WebUtils.toHttp(response);
			
			HttpServletRequest req = (HttpServletRequest) request;
		
			if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
				res.setStatus(HttpStatus.OK.value());
				res.addHeader("Access-Control-Allow-Origin", "*");
				res.addHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Accept,Token");
				res.addHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,HEAD,PUT,DELETE");
				res.addHeader("Access-Control-Expose-Headers", "Token");
			}else
			{
				res.setStatus(HttpStatus.OK.value());
				res.addHeader("Access-Control-Allow-Origin", "*");
				res.addHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,HEAD");
				res.addHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Accept,Token");
				res.addHeader("Access-Control-Expose-Headers", "Token");
			}
			
			res.addHeader(TOKEN_HEADER, TOKEN_SCHEME_Bearer + " " + authToken.genToken());
			res.addHeader("Content-Type", "application/json");
			
			Gson gson = new Gson();
			Result result = new Result();
			result.setStatus(1);
			result.setCode("1002");
			result.setInfo("登录成功");
			
			res.getWriter().write(gson.toJson(result));
			logger.info(gson.toJson(result));
			return false;			
		}
		return true;
	}
	
	@Override
	 protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
		
		HttpServletResponse res =  WebUtils.toHttp(response);
		
		res.setStatus(HttpStatus.UNAUTHORIZED.value());
		res.addHeader("Access-Control-Allow-Origin", "*");
		res.addHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,HEAD");
		res.addHeader("Access-Control-Allow-Headers", "X-Requested-With,Content-Type,Accept,Token");
		res.addHeader("Content-Type", "application/json");
		
		String error = e.getMessage();
		try{
			res.getWriter().write(error);
			res.flushBuffer();
		}
		catch(Exception e1){
			e1.printStackTrace();
		}
		
		// don't go another filter again
		return false;
    }	
	
	private BearerToken parseJson(ServletRequest request){
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
			Gson gson = new Gson();
			BearerToken token = gson.fromJson(buffer.toString(), BearerToken.class);
			
			return token;
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
