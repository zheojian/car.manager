package com.car.manager.controller;

import org.apache.log4j.Logger;

import com.car.manager.model.UserInfo;
import com.jfinal.core.Controller;

public class BaseController extends Controller {
	protected static Logger log = Logger.getLogger(BaseController.class);

	@Override
	public void render(String view) {
		setAttr("ctx", getRequest().getContextPath());
		UserInfo user = (UserInfo) getSession().getAttribute("user");
		if(user != null)setAttr("pms", user.getInt("pms"));
		super.render(view);
	}
}
