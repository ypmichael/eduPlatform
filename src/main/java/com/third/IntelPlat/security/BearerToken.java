package com.third.IntelPlat.security;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationToken;

public class BearerToken implements AuthenticationToken {


	private static final long serialVersionUID = -8959620526701217063L;

	private final String userName;
	private final String password;
	
	private final long createTime;
	private final boolean isAuthed;
	
	
	// time out set to 30 minutes
	private final long timeout = 1000 * 60 * 20;
	
	private Set<String> permissons;
	
	public BearerToken(String userName, String password, long createTime, boolean isAuthed) {
		this.userName = userName;
		this.password = password;
		this.createTime = createTime;
		this.isAuthed = isAuthed;
	}
	
	@Override
	public Object getCredentials() {
		return password;
	}

	@Override
	public Object getPrincipal() {
		return userName;
	}
	
	public long getCreateTime() {
		return createTime;
	}

	public boolean isAuthed() {
		return isAuthed;
	}	
	
	public boolean isTimeout(){
		
		// compare create time with current time
		long current = System.currentTimeMillis();
		if((current - this.createTime) > timeout)
			return true;
		else
			return false;
	}
	
	public String genToken(){
		
		StringBuffer sb = new StringBuffer();
		sb.append(userName).append(":")
			.append(password).append(":")
			.append(createTime);
		String tokenStr = AESUtils.encrypt(sb.toString());
		return tokenStr;
	}
	
	public static BearerToken parseToken(String header){
		
		String[] authTokens = header.split(" ");		
		if (authTokens == null || authTokens.length < 2) {
			return emptyToken();
		}
		
		String tokenStr = AESUtils.decrypt(authTokens[1]);
		
		String[] principlesAndCredentials = tokenStr.split(":", -1);		
		if (principlesAndCredentials == null || principlesAndCredentials.length != 3) {
			return emptyToken();
		}
		
		String username = principlesAndCredentials[0];
		String password = principlesAndCredentials[1];
		String createTime = principlesAndCredentials[2];
		
		return new BearerToken(username, password, Long.valueOf(createTime), true);
	}
	
	public static BearerToken emptyToken(){
		return new BearerToken(null, null, -1, false);
	}

	public boolean isEmptyToken(){
		return createTime==-1?true:false;
	}

	public Set<String> getPermissons() {
		return permissons;
	}

	public void setPermissons(Set<String> permissons) {
		this.permissons = permissons;
	}

}