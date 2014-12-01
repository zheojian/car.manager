package com.car.manager.controller;

import java.util.ArrayList;
import java.util.List;



import com.car.manager.model.CarInfo;
import com.car.manager.model.UserInfo;
import com.car.manager.util.MD5;
import com.car.manager.util.RtKit;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;

public class MainController extends BaseController {

	public void index(){
		render("index.html");
	}
	public void menu(){
		render("menu.html");
	}
	
	/** login begin ************************************************************/
	@ClearInterceptor(ClearLayer.ALL)
	public void toLogin()throws Exception{
		setAttr("account", new CarInfo());
		render("login.html");
	}
	
	@ClearInterceptor(ClearLayer.ALL)
	public void login()throws Exception{
		String userName = getPara("username");
		String password = getPara("password");
		if(userName == null || "".equals(userName.trim())
				|| password == null || "".equals(password.trim())){
			renderJson(RtKit.rt("n", "用户名或密码不能为空!"));
			return;
		}
		
		userName = userName.trim();
		password = password.trim();
		if(userName.length()>20 || userName.length()<3 
				|| password.length()>16 || password.length()<3){
			setAttr("msg", "用户名长度5-20,密码长度5-16.");
			renderJson(RtKit.rt("n", "用户名长度3-20,密码长度3-16."));
			return;
		}
		
		UserInfo account = UserInfo.dao.findFirst("select * from userinfo where user_name = ? and password = ? and status = 0", userName,MD5.MD5Encode(password));
		if(account != null){
			getSession().setAttribute("user", account);
			renderJson(RtKit.rt("y"));
		}else{
			renderJson(RtKit.rt("n", "用户名或密码错误!"));
		}
	}
	public void logout(){
		getSession().invalidate();
		redirect("/toLogin");
	}
	
	/** login end ************************************************************/
	
	
	/** carInfo begin ************************************************************/
	public void s(){
		List<UserInfo> list = null;
		String schoolName = getPara("school_name");
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		if(schoolName == null || "".equals(schoolName.trim())){
			sql.append("select * from school order by sort asc");
		}else{
			schoolName = schoolName.trim();
			sql.append("select a.* from school a "); 
			StringBuffer inner = new StringBuffer();
			StringBuffer where = new StringBuffer();
			
			where.append(" where a.school_name like ?");
			StringBuffer like = new StringBuffer();
			params.add(like.append('%').append(schoolName.charAt(0)).append('%').toString());
			
			if(schoolName.length()>1){
				for (int i = 1; i < schoolName.length(); i++) {
					inner.append(" inner join school ")
					.append('s').append(i).append(" on a.school_id = ")
					.append('s').append(i).append(".school_id ");
					where.append(" and ")
					.append('s').append(i).append(".school_name like ?");
					like.setLength(0);
					params.add(like.append('%').append(schoolName.charAt(i)).append('%').toString());
				}
			}
			sql.append(inner).append(where).append(" order by sort asc");
		}
		log.info(sql.toString());
		log.info(params);
		list = UserInfo.dao.find(sql.toString(), params.toArray());
		if(list != null && ! list.isEmpty()) {
			setAttr("schools", list);
			render("result.html");
		}else{
			renderError(404);
		}
	}
}
