package com.bootdo.common.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ReportController extends BaseController {
	@Value("${python.apiPath}")
	String service_url; 
	
	@Autowired
	private RestTemplate restTemplate;
	
	/** 
	 * 报表列表
	 * @return
	 */
	@GetMapping("/reports")
	String report_list() {
		
		return "common/report/reports";
	}
	
	/**
	 * 报表详情（编辑）页
	 * @param id
	 * @return
	 */
	@GetMapping("/report/{id}")
	String report_detail(@PathVariable String id, Model model, HttpServletRequest request) {
		//1.URL地址
		String url = String.format("%s/api/report/%s", service_url, id);
		
		//2.调用PYTHON服务，返回JSON字符串
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		//3.解析JSON字符串
		JSONObject report = JSONObject.parseObject(response.getBody());
			
		ArrayList<String> params = new ArrayList<String>(); 
		//从地址栏解析参数值
		JSONArray filters = report.getJSONArray("filters");
		for(Object obj:filters){
			JSONObject filter = (JSONObject )obj;
			
			if("DATE_RANGE".equals(filter.getString("ui_type"))){
				String val1 = request.getParameter(filter.getString("name") + '1');
				String val2 = request.getParameter(filter.getString("name") + '2');
				
				// 查询参数覆盖默认值
				if( !StringUtils.isEmpty(val1) && !StringUtils.isEmpty(val2)){
					filter.put("default_value", String.format("%s - %s", val1, val2));
					filter.put("val1", val1);
					filter.put("val2", val2);
				}else{
					String default_value = filter.getString("default_value");
					if( !StringUtils.isEmpty(default_value)){
						int length = default_value.length();
						val1 = default_value.substring(0, length / 2).trim();
						val2 = default_value.substring(length / 2 + 1).trim();
						
						filter.put("val1", val1);
						filter.put("val2", val2);
					}
				}
				
				if( !StringUtils.isEmpty(filter.getString("val1")) &&
					!StringUtils.isEmpty(filter.getString("val2"))){
					params.add(String.format("%s=%s", filter.getString("name") + '1', filter.getString("val1")));
					params.add(String.format("%s=%s", filter.getString("name") + '2', filter.getString("val2")));
				}
			}else{
				String val = request.getParameter(filter.getString("name"));
				
				if(!StringUtils.isEmpty(val)){
					filter.put("default_value", val);
				}
				
				if( !StringUtils.isEmpty(filter.getString("default_value"))){
					params.add(String.format("%s=%s", filter.getString("name"), filter.getString("default_value")));
				}
			}
			
			if("SELECT".equals(filter.getString("ui_type"))){
				JSONArray options = filter.getJSONArray("options");
				if(options != null){
					for(Object obj2:options){
						JSONObject option = (JSONObject )obj2;
						if( option.getString("value").equals(filter.getString("default_value"))){
							option.put("selected", true);
						}else{
							option.put("selected", false);
						}
					}
				}
			}
			
			filter.put("sql", "");
		}
				
		//4.生成datatables列定义
		JSONArray headers = report.getJSONArray("headers");
		
		JSONArray columns = new JSONArray();
		
		for(Object obj:headers){
			JSONObject header = (JSONObject )obj;
			JSONObject col = new JSONObject ();
			
			if( !"1".equals(header.getString("hide"))){
				col.put("data", header.getString("name"));
				columns.add(col);
			}
		}
		
		//附件下载时的文件名
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String attachment = String.format("%s_%s", report.getString("title"), sdf.format(System.currentTimeMillis()));
		
		String query_string = request.getQueryString();
		if( StringUtils.isEmpty(query_string)){
			query_string = String.join("&", params);
		}
		
		String query_url = String.format("/report/%s/data?%s", id, query_string);
		String export_url = String.format("/report/%s/export?%s&attachment=%s", id, query_string, attachment);
		
		//4.设置界面数据模型
		model.addAttribute("action", String.format("/report/%s", id));
		model.addAttribute("report", report);
		model.addAttribute("columns", columns);
		model.addAttribute("url", query_url);
		model.addAttribute("export_url", export_url);

		return "common/report/report";
	}
	
	/**
	 * 报表数据展现页
	 * @param id
	 * @return
	 */
	@GetMapping("/report/{id}/data")
	@ResponseBody
	Map<String,Object> report_data(@PathVariable String id, HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		try{
			int draw = 1;
			int page = 1;
			int per_page = 20;
			String username = getUsername();
			
			try{
				int start = Integer.parseInt(request.getParameter("start"));
				int length = Integer.parseInt(request.getParameter("length"));
				
				page = start / length + 1;
				per_page = length;
				
				draw = Integer.parseInt(request.getParameter("draw"));
				
			}catch(Exception e){
				
			}
			
			//1.URL地址
			String url = String.format("%s/api/report/%s/data?page=%d&per_page=%d&username=%s", 
					service_url, 
					id, 
					page, 
					per_page, 
					username);
			
			//附加过滤条件
			Map<String,String[]> params = request.getParameterMap();
			for(String key : params.keySet()){
				if(key.equals("start") || key.equals("length") || key.equals("draw") || key.equals("_")){
					continue;
				}
				
				if( key.startsWith("columns[") || key.startsWith("search[")){
					continue;
				}
				
				String val = request.getParameter(key);
				
				if( val == null || val.isEmpty()){
					continue;
				}
				
				url += String.format("&%s=%s", key, val);
			}
			
			//2.调用PYTHON服务，返回JSON字符串
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			
			//3.解析JSON字符串
			JSONObject json = JSONObject.parseObject(response.getBody());
			
			map.put("draw", draw);
			map.put("recordsTotal", json.getString("total"));
			map.put("recordsFiltered", json.getString("total"));
			map.put("data", json.getJSONArray("items"));
			
			return map;
		}catch(Exception e){
			
			map.put("error", e.getMessage());
			return map;
		}		
		
	}
	
	/**
	 * 报表数据导出
	 * @param id
	 * @return
	 */
	@GetMapping("/report/{id}/export")
	@ResponseBody
	void report_export(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) {
		String attachment_format = "xls";
		String attachment_filename = request.getParameter("attachment");
		String username = getUsername();
		
		//1.URL地址
		String url = String.format("%s/api/report/%s/data?export=%s&username=%s", service_url, id, attachment_format, username);
		
		//附加过滤条件
		Map<String,String[]> params = request.getParameterMap();
		for(String key : params.keySet()){
			if(key.equals("attachment")){
				continue;
			}
			
			String val = request.getParameter(key);
			
			if( val == null || val.isEmpty()){
				continue;
			}
			
			url += String.format("&%s=%s", key, val);
		}
		
		//2.调用PYTHON服务，返回xls文件内容
		ResponseEntity<byte[]> result = restTemplate.getForEntity(url, byte[].class);
		byte[] result_body = result.getBody();
	
		// 文件名
		String filename = String.format("%s.%s", attachment_filename, attachment_format);
		
		//转码
		try{
			filename = java.net.URLEncoder.encode(filename, "UTF-8");
		}catch(Exception e){
			
		}
		
		//3.下载文件
		response.setContentType("multipart/form-data"); 		
		response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        response.addHeader("Content-Length", "" + result_body.length);
		
        
		try{
			try(OutputStream out = new BufferedOutputStream(response.getOutputStream())){
				out.write(result_body);
				out.flush();
			}
		}catch(Exception e){
			
		}
		
	}	

	/**
	 * 查询过滤条件列表
	 * @param report_id
	 * @return
	 */
	@GetMapping("/report/{report_id}/filters")
	String filter_list(@PathVariable String report_id) {
		
		return "common/report/filters";
	}

	/**
	 * 报表数据展现页
	 * @param id
	 * @return
	 */
	@GetMapping("/report/{report_id}/filter/{id}/options")
	@ResponseBody
	JSONArray filter_options(@PathVariable String report_id, @PathVariable String id, HttpServletRequest request) {
		//1.URL地址
		String url = String.format("%s/api/report/%s/filter/%s/options?", service_url, report_id, id);
		
		//附加过滤条件
		Map<String,String[]> params = request.getParameterMap();
		for(String key : params.keySet()){
			if(key.equals("_")){
				continue;
			}
			
			String val = request.getParameter(key);
			
			if( val == null || val.isEmpty()){
				continue;
			}
			
			if( url.endsWith("?")){
				url += String.format("%s=%s", key, val);
			}else{
				url += String.format("&%s=%s", key, val);
			}
		}

		//2.调用PYTHON服务，返回JSON字符串
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		
		JSONArray arr = JSONArray.parseArray(response.getBody());
		return arr;
	}
	
	/**
	 * 过滤条件选项
	 * @param report_id
	 * @param id
	 * @return
	 */
	@GetMapping("/report/{report_id}/filter/{id}")
	String filter_detail(@PathVariable String report_id, @PathVariable String id) {
		
		return "common/report/filter";
	}
	/**
	 * 表头列标题列表
	 * @param report_id
	 * @return
	 */
	@GetMapping("/report/{report_id}/headers")
	String header_list(@PathVariable String report_id) {
		
		return "common/report/headers";
	}

	/**
	 * 表头列标题详情（编辑）页
	 * @param report_id
	 * @param id
	 * @return
	 */
	@GetMapping("/report/{report_id}/header/{id}")
	String header_detail(@PathVariable String report_id, @PathVariable String id) {
		
		return "common/report/header";
	}
	
}
