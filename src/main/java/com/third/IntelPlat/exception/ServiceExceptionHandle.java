package com.third.IntelPlat.exception;


import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.third.IntelPlat.rest.Result;

@ControllerAdvice
public class ServiceExceptionHandle {
	
	@Autowired
    private MessageSource messageSource;	
	
	@ExceptionHandler(ServiceException.class)
	@ResponseBody
    public String handServiceExcption(Exception ex){

		Result result = new Result();
		
		result.setStatus(Result.STATUS_FAILED);
		result.setInfo(genInfoById(ex.getMessage()));
		
		
		Gson json = new Gson();
		
        return json.toJson(result);
    }
	
	@ExceptionHandler(RestException.class)
	@ResponseBody
    public String handRestExcption(Exception ex){

		Result result = new Result();
		
		result.setStatus(Result.STATUS_FAILED);
		result.setInfo(genInfoById(ex.getMessage()));
		
		
		Gson json = new Gson();
		
        return json.toJson(result);
    }	
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
    public String handRuntimeExcption(Exception ex){

		Result result = new Result();
		
		result.setStatus(Result.STATUS_FAILED);
		result.setInfo(ex.getMessage());
		
		
		Gson json = new Gson();
		
        return json.toJson(result);
    }		
	
	/** 
	 * return exception info by id;
	 * if no message define found, return id itself;
	**/
	public String genInfoById(String id){
		Locale locale = LocaleContextHolder.getLocale();
		String content = messageSource.getMessage(id, null, id, locale);
		
		return content;
	}

}
