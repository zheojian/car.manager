package com.car.manager.controller;

import java.util.List;



import com.car.manager.model.UserInfo;
import com.car.manager.util.MD5;
import com.car.manager.util.RtKit;

public class UserInfoController extends BaseController {

	/** userInfo begin ************************************************************/
	public void index(){
		List<UserInfo> list = UserInfo.dao.find("select * from userinfo where status = 0 and pms <> 2");
		setAttr("users", list);
		render("userInfo.html");
	}
	public void toAdd(){
		setAttr("userInfo", new UserInfo());
		render("userInfoAdd.html");
	}
	public void add(){
		UserInfo userInfo = getModel(UserInfo.class);
		userInfo.set("password", MD5.MD5Encode(userInfo.getStr("password")));
		if(userInfo.save()){
			renderJson(RtKit.rt("y", "添加用户成功"));
		}else{
			renderJson(RtKit.rt("n", "添加用户失败"));
		}
	}
	public void toUpdate(){
		UserInfo userInfo = getModel(UserInfo.class);
		userInfo = UserInfo.dao.findById(userInfo.getInt("uid"));
		setAttr("userInfo", userInfo);
		render("userInfoUpdate.html");
	}
	public void update(){
		UserInfo userInfo = getModel(UserInfo.class);
		String pwd = userInfo.getStr("password");
		if(pwd == null || pwd.equals(""))userInfo.remove("password");
		else userInfo.set("password", MD5.MD5Encode(pwd));
		if(userInfo.update()){
			renderJson(RtKit.rt("y", "修改成功"));
		}else{
			renderJson(RtKit.rt("n", "修改失败"));
		}
	}
	public void del(){
		try {
			if(UserInfo.dao.deleteById(getParaToInt("uid"))){
				renderJson(RtKit.rt("y", "删除成功"));
				return ;
			}
		} catch (Exception e) {}
		renderJson(RtKit.rt("n", "删除失败，该用户可能存在相关联的信息。"));
	}
	public void toChangePwd(){
		UserInfo userInfo = (UserInfo) getSession().getAttribute("user");
		setAttr("userInfo", userInfo);
		render("userInfoChangePwd.html");
	}
	public void changePwd(){
		UserInfo userInfo = getModel(UserInfo.class);
		String npwd = userInfo.getStr("password");
		String opwd = getPara("opassword");
		UserInfo ouser = (UserInfo) getSession().getAttribute("user");
		if(ouser.getStr("password").equals(MD5.MD5Encode(opwd))){
			userInfo.set("password", MD5.MD5Encode(npwd));
			if(userInfo.update()){
				getSession().setAttribute("user", UserInfo.dao.findById(userInfo.getInt("uid")));
				renderJson(RtKit.rt("y", "修改成功"));
			}else{
				renderJson(RtKit.rt("n", "修改失败"));
			}
		}else{
			renderJson(RtKit.rt("n", "修改失败,密码不正确"));
		}
	}
	/** userInfo end ************************************************************/
	
}
