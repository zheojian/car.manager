package com.car.manager.interceptor;

import javax.servlet.http.HttpSession;

import com.car.manager.model.UserInfo;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;

public class GlobalInterceptor implements Interceptor {

	public void intercept(ActionInvocation ai) {
		HttpSession s = ai.getController().getSession();
		UserInfo user = (UserInfo) s.getAttribute("user");
		if(user == null){
			ai.getController().redirect("/toLogin");
			return;
		}
		ai.invoke();
	}

}
