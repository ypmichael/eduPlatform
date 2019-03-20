package com.third.IntelPlat.common;

import java.lang.reflect.Method;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.service.OperationLogService;

@Component
@Aspect
public class OperationLogAspect {

	private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

	@Resource
	private OperationLogService operationLogService;

	@Pointcut("@annotation(com.third.IntelPlat.common.OperationLog)")
	public void operationLog() {
	}

	@AfterReturning("operationLog()")
	public void doAfterReturning(JoinPoint jp) {
		Method proxyMethod = ((MethodSignature) jp.getSignature()).getMethod();
		Method soruceMethod;
		try {
			soruceMethod = jp.getTarget().getClass().getMethod(proxyMethod.getName(), proxyMethod.getParameterTypes());
			OperationLog operation = soruceMethod.getAnnotation(OperationLog.class);
			Object[] args = jp.getArgs();
			String operationReal = operation.operation();
			for (int i = 0; i < args.length; i++) {
				Object argsStr = args[i];
				if (null == args[i]) {
					argsStr = "";
				} else if (!(args[i] instanceof String)) {
					argsStr = args[i].toString();
				}
				operationReal = operationReal.replace("$" + (i + 1), (CharSequence) argsStr);
			}
			if (operation != null) {

				Subject subject = SecurityUtils.getSubject();
				String operator = (String) subject.getPrincipal();

				operationLogService.insertOperationLog(operator, new Date(), operation.operation(), operation.type(),
						operation.model());
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
