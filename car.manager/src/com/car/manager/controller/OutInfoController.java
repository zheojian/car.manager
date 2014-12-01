package com.car.manager.controller;

import java.util.List;

import com.car.manager.model.CarInfo;
import com.car.manager.model.Dept;
import com.car.manager.model.OutInfo;
import com.car.manager.model.UserInfo;
import com.car.manager.util.DateUtils;
import com.car.manager.util.RtKit;

public class OutInfoController extends BaseController {

	/** outInfo begin ************************************************************/
	public void index(){
		String sql = "select o.* , c.license , u.user_name apply_name , a.user_name approve_name from outinfo o join userinfo u on o.apply_uid = u.uid join carinfo c on o.cid = c.cid left join userinfo a on o.approve_uid = a.uid";
		List<OutInfo> list = OutInfo.dao.find(sql);
		setAttr("outs", list);
		render("outInfo.html");
	}
	public void toAdd(){
		//车辆
		List<CarInfo> cars = CarInfo.dao.find("select cid,license,mileage from carinfo where status = 0");
		setAttr("cars", cars);
		//司机
		List<UserInfo> drivers = UserInfo.dao.find("select uid,user_name from userinfo where status = 0 and driving_license is not null");
		setAttr("drivers", drivers);
		//申请人
		List<UserInfo> users = UserInfo.dao.find("select uid,user_name from userinfo where status = 0");
		setAttr("users", users);
		//科室
		List<Dept> depts = Dept.dao.find("select * from dept");
		setAttr("depts", depts);
				
		setAttr("outInfo", new OutInfo());
		render("outInfoAdd.html");
	}
	public void add(){
		OutInfo outInfo = getModel(OutInfo.class);
		outInfo.set("apply_date", DateUtils.getStringDateShort());
		if(outInfo.save()){
			//将车辆设置为 派出 状态
			CarInfo carInfo = CarInfo.dao.findById(outInfo.getInt("cid"));
			carInfo.set("status", 3).update();
			renderJson(RtKit.rt("y", "添加派车信息成功"));
		}else{
			renderJson(RtKit.rt("n", "添加派车信息失败"));
		}
	}
	public void toUpdate(){
		OutInfo outInfo = getModel(OutInfo.class);
		outInfo = OutInfo.dao.findById(outInfo.getInt("cid"));
		setAttr("outInfo", outInfo);
		//有驾照的人员
		List<UserInfo> users = UserInfo.dao.find("select uid,user_name from userinfo where status = 0 and driving_license is not null");
		setAttr("users", users);
		render("outInfoUpdate.html");
	}
	public void update(){
		OutInfo outInfo = getModel(OutInfo.class);
		if(outInfo.update()){
			renderJson(RtKit.rt("y", "修改成功"));
		}else{
			renderJson(RtKit.rt("n", "修改失败"));
		}
	}
	public void approve(){
		OutInfo outInfo = getModel(OutInfo.class);
		UserInfo user = (UserInfo) getSession().getAttribute("user");
		outInfo.set("approve_uid", user.getInt("uid"));
		if(outInfo.update()){
			renderJson(RtKit.rt("y", "修改成功"));
		}else{
			renderJson(RtKit.rt("n", "修改失败"));
		}
	}
	
	public void finish(){
		OutInfo outInfo = getModel(OutInfo.class);
		if(outInfo.update()){
			outInfo = OutInfo.dao.findById(outInfo.get("oid"));
			//将车辆设置为 正常  状态
			CarInfo carInfo = CarInfo.dao.findById(outInfo.getInt("cid"));
			carInfo.set("status", 0).update();
			renderJson(RtKit.rt("y", "修改成功"));
		}else{
			renderJson(RtKit.rt("n", "修改失败"));
		}
	}
	
	public void del(){
		try {
			if(OutInfo.dao.deleteById(getParaToInt("oid"))){
				renderJson(RtKit.rt("y", "删除成功"));
				return ;
			}
		} catch (Exception e) {}
		renderJson(RtKit.rt("n", "删除失败，该记录可能存在相关联的信息。"));
	}
	/** outInfo end ************************************************************/
	
}
