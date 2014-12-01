package com.car.manager.config;

import com.car.manager.controller.*;
import com.car.manager.interceptor.GlobalInterceptor;
import com.car.manager.model.*;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class CarManagerJfinalConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		// 加载少量必要配置，随后可用getProperty(...)获取值
		loadPropertyFile("a_little_config.txt");
		me.setDevMode(getPropertyToBoolean("devMode"));
		me.setBaseViewPath("/WEB-INF/pages/");
		me.setEncoding("UTF-8");
	}

	@Override
	public void configHandler(Handlers me) {
	}

	@Override
	public void configInterceptor(Interceptors me) {
		me.add(new SessionInViewInterceptor());
		me.add(new GlobalInterceptor());
	}

	@Override
	public void configPlugin(Plugins me) {
		C3p0Plugin cp = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password").trim());
		me.add(cp);
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		me.add(arp);
		
		arp.addMapping("carinfo", "cid", CarInfo.class);
		arp.addMapping("dept", "did", Dept.class);
		arp.addMapping("maintain", "mid", Maintain.class);
		arp.addMapping("outinfo", "oid", OutInfo.class);
		arp.addMapping("refuel", "rid", Refuel.class);
		arp.addMapping("userinfo", "uid", UserInfo.class);
		arp.setShowSql(getPropertyToBoolean("devMode"));
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/", MainController.class);
		me.add("/userInfo", UserInfoController.class);
		me.add("/carInfo", CarInfoController.class);
		me.add("/outInfo", OutInfoController.class);
		me.add("/maintain", MaintainInfoController.class);
	}

}
