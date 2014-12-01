package com.car.manager.controller;

import java.util.List;

import com.car.manager.model.CarInfo;
import com.car.manager.model.UserInfo;
import com.car.manager.util.RtKit;

public class CarInfoController extends BaseController {

	/** carInfo begin ************************************************************/
	public void index(){
		List<CarInfo> list = CarInfo.dao.find("select c.*,u.user_name from carinfo c join userinfo u on c.uid = u.uid");
		setAttr("cars", list);
		render("carInfo.html");
	}
	public void toAdd(){
		//有驾照的人员
		List<UserInfo> users = UserInfo.dao.find("select uid,user_name from userinfo where status = 0 and driving_license is not null");
		setAttr("users", users);
		setAttr("carInfo", new CarInfo());
		render("carInfoAdd.html");
	}
	public void add(){
		CarInfo carInfo = getModel(CarInfo.class);
		if(carInfo.save()){
			renderJson(RtKit.rt("y", "添加车辆信息成功"));
		}else{
			renderJson(RtKit.rt("n", "添加车辆信息失败"));
		}
	}
	public void toUpdate(){
		CarInfo carInfo = getModel(CarInfo.class);
		carInfo = CarInfo.dao.findById(carInfo.getInt("cid"));
		setAttr("carInfo", carInfo);
		//有驾照的人员
		List<UserInfo> users = UserInfo.dao.find("select uid,user_name from userinfo where status = 0 and driving_license is not null");
		setAttr("users", users);
		render("carInfoUpdate.html");
	}
	public void update(){
		CarInfo carInfo = getModel(CarInfo.class);
		if(carInfo.update()){
			renderJson(RtKit.rt("y", "修改成功"));
		}else{
			renderJson(RtKit.rt("n", "修改失败"));
		}
	}
	public void del(){
		try {
			if(CarInfo.dao.deleteById(getParaToInt("cid"))){
				renderJson(RtKit.rt("y", "删除成功"));
				return ;
			}
		} catch (Exception e) {}
		renderJson(RtKit.rt("n", "删除失败，该车辆可能存在相关联的信息。"));
	}
	/** carInfo end ************************************************************/
	
}
