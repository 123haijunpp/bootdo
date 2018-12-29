package com.xd.entryAndExitManagement.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bootdo.common.service.DictService;
import com.bootdo.common.utils.DateUtils;
import com.bootdo.system.domain.UserDO;
import com.xd.common.util.SendApiconfig;
import com.xd.common.util.UserUtil;
import com.xd.entryAndExitManagement.domain.ReservationDO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xd.entryAndExitManagement.domain.TbFormDO;
import com.xd.entryAndExitManagement.service.TbFormService;
import com.bootdo.common.utils.PageUtils;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * 填表数据
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-07-31 09:59:34
 */
 
@Controller
@RequestMapping("/EntryAndExitManagement/tbForm")
public class TbFormController {
	@Autowired
	private TbFormService tbFormService;
	@Autowired
	private SendApiconfig sendApiconfig;

	
	@GetMapping()
	@RequiresPermissions("entryAndExitManagement:tbForm:tbForm")
	String TbForm(){
	    return "entryAndExitManagement/tbForm/tbForm";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("entryAndExitManagement:tbForm:tbForm")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//增加数据权限参数,数据所属地区
		UserUtil userUtil = new UserUtil();
		params=userUtil.addParams(params);
		//查询列表数据
        Query query = new Query(params);
		List<TbFormDO> tbFormList = tbFormService.list(query);
		/*for (TbFormDO t:tbFormList) {
		    if(t.getHallId()!=null){
                t.setHallName(tbHallService.get(t.getHallId()).getName());
            }
		}*/
		int total = tbFormService.count(query);
		PageUtils pageUtils = new PageUtils(tbFormList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	@RequiresPermissions("entryAndExitManagement:tbForm:add")
	String add(){
	    return "entryAndExitManagement/tbForm/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("entryAndExitManagement:tbForm:edit")
	String edit(@PathVariable("id") Integer id,Model model){
		TbFormDO tbForm = tbFormService.get(id);
		model.addAttribute("tbForm", tbForm);
	    return "entryAndExitManagement/tbForm/edit";
	}

	@GetMapping("/formdetail/{id}")
	String formdetail(@PathVariable("id") Integer id, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		TbFormDO tbForm = new TbFormDO();
		JSONArray data = new JSONArray();
		UserDO userDO = null;
		UserUtil userUtil = new UserUtil();
		userDO = userUtil.getUser();
		JSONObject postData= new JSONObject();

		try{
			 tbForm = tbFormService.get(id);
			tbForm.setHallName(URLDecoder.decode(request.getParameter("hallName"),"UTF-8"));
			tbForm.setJsondata(JSON.parseObject(tbForm.getData()));
			JSONObject jsonObject= new JSONObject();
			jsonObject.put("form_id",tbForm.getId());
			postData.put("code","2010");
			postData.put("params",jsonObject);
			JSONObject jsonObject1= JSONObject.parseObject(sendApiconfig.getPython(postData));
			if( Boolean.valueOf(jsonObject1.get("status").toString())){
				//3.解析JSON字符串
				data = JSON.parseArray(jsonObject1.get("data").toString());
				for(int i=0;i<data.size();i++) {
					JSONObject job = data.getJSONObject(i); // 遍历 jsonarray 数组，把每一个对象转成 json 对象
					if (job.get("field_name").toString().equals("express")) {
						if (job.get("field_value").toString().equals("1")){
							job.put("field_value", "是");
						}else {
							job.put("field_value", "否");
						}
					} else {

					}
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		model.addAttribute("data", data);
		model.addAttribute("tbForm", tbForm);
		model.addAttribute("shuiyin", "@"+ DateUtils.format(new Date(),DateUtils.DATE_PATTERN)+userDO.getUsername());
		return "entryAndExitManagement/tbForm/formDetail";
	}


	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("entryAndExitManagement:tbForm:add")
	public R save( TbFormDO tbForm){
		if(tbFormService.save(tbForm)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("entryAndExitManagement:tbForm:edit")
	public R update( TbFormDO tbForm){
		tbFormService.update(tbForm);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("entryAndExitManagement:tbForm:remove")
	public R remove( Integer id){
		if(tbFormService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("entryAndExitManagement:tbForm:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		tbFormService.batchRemove(ids);
		return R.ok();
	}

	/**
	 * 是否是json格式
	 * @param content
	 * @return
	 */
	public boolean isJson(String content){
		try {
			JSONObject jsonStr= JSONObject.parseObject(content);
			return  true;
		} catch (Exception e) {
			return false;
		}
	}

}

