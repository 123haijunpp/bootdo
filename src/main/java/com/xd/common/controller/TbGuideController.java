package com.xd.common.controller;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bootdo.common.config.BootdoConfig;
import com.bootdo.common.domain.FileDO;
import com.bootdo.common.utils.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xd.common.domain.TbGuideDO;
import com.xd.common.service.TbGuideService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 办证指引
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-08-02 14:50:55
 */
 
@Controller
@RequestMapping("/common/tbguide")
public class TbGuideController {
	@Autowired
	private TbGuideService tbGuideService;
	@Autowired
	private BootdoConfig bootdoConfig;
	
	@GetMapping()
	String TbGuide(Model model) {
		Map<String, Object> params = new HashMap<>(16);
	    return "common/file/guide";
	}
	
	@ResponseBody
	@GetMapping("/list")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<TbGuideDO> tbGuideList = tbGuideService.list(query);
		int total = tbGuideService.count(query);
		for (TbGuideDO t: tbGuideList) {
			t.setKey("/files/"+t.getFilename());
		}
		PageUtils pageUtils = new PageUtils(tbGuideList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	String add(){
	    return "common/tbGuide/add";
	}

	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") Integer id,Model model){
		TbGuideDO tbGuide = tbGuideService.get(id);
		model.addAttribute("tbGuide", tbGuide);
	    return "common/tbGuide/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	public R save( TbGuideDO tbGuide){
		if(tbGuideService.save(tbGuide)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	public R update( TbGuideDO tbGuide){
		tbGuideService.update(tbGuide);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	/*@PostMapping( "/remove")
	@ResponseBody
	public R remove( Integer id){
		if(tbGuideService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}*/

	@ResponseBody
	@PostMapping("/upload")
	R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		String fileName = file.getOriginalFilename();
//		fileName = FileUtil.renameToUUID(fileName);
		TbGuideDO sysFile = new TbGuideDO();
		sysFile.setFilename(fileName);
		sysFile.setRemarks(String.valueOf(FileType.fileType(fileName)));
		sysFile.setCreateDate(new Date());
		try {
			FileUtil.uploadFile(file.getBytes(), bootdoConfig.getUploadPath(), fileName);
		} catch (Exception e) {
			return R.error();
		}
		if (tbGuideService.save(sysFile) > 0) {
			return R.ok().put("fileName",sysFile.getFilename());
		}
		return R.error();
	}

	@RequestMapping("/download")
	public void down(HttpServletResponse resp, String name){
		download(resp,name,name);
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	public R remove(@RequestParam("ids[]") Integer[] ids){
		tbGuideService.batchRemove(ids);
		return R.ok();
	}
	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	public R remove(Integer id, HttpServletRequest request) {
		String fileName = bootdoConfig.getUploadPath() + tbGuideService.get(id).getFilename().replace("/files/", "");
		if (tbGuideService.remove(id) > 0) {
			boolean b = FileUtil.deleteFile(fileName);
			if (!b) {
				return R.error("数据库记录删除成功，文件删除失败");
			}
			return R.ok();
		} else {
			return R.error();
		}
	}


	/**
	 * @param resp
	 * @param name         文件真实名字
	 * @param downloadName 文件下载时名字
	 */
	private void download(HttpServletResponse resp, String name, String downloadName) {
		String fileName = null;
		try {
			fileName = new String(downloadName.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String path = bootdoConfig.getUploadPath() + name;
		File file = new File(path);
		resp.reset();
		resp.setContentType("application/octet-stream");
		resp.setCharacterEncoding("utf-8");
		resp.setContentLength((int) file.length());
		resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			os = resp.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(file));
			int i = 0;
			while ((i = bis.read(buff)) != -1) {
				os.write(buff, 0, i);
				os.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
