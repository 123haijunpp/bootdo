package com.xd.common.controller;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bootdo.common.config.BootdoConfig;
import com.bootdo.common.utils.*;
import com.xd.common.domain.TbGuideDO;
import com.xd.common.util.UserUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xd.common.domain.TbFileDO;
import com.xd.common.service.TbFileService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 配置文件
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-08-08 11:24:53
 */
 
@Controller
@RequestMapping("/common/tbFile")
public class TbFileController {
	@Autowired
	private TbFileService tbFileService;
	@Autowired
	private BootdoConfig bootdoConfig;
	@Value("${uploadFilePath}")
	private String uploadFilePath;

	@GetMapping()
	String TbFile(){
		return "common/file/guide";
	}
	
	@ResponseBody
	@GetMapping("/list")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<TbFileDO> tbFileList = tbFileService.list(query);
		int total = tbFileService.count(query);
		PageUtils pageUtils = new PageUtils(tbFileList, total);
		return pageUtils;
	}
	
	@GetMapping("/add")
	String add(){
	    return "common/tbFile/add";
	}

	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") Integer id,Model model){
		TbFileDO tbFile = tbFileService.get(id);
		model.addAttribute("tbFile", tbFile);
	    return "common/tbFile/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("common:tbFile:add")
	public R save( TbFileDO tbFile){
		if(tbFileService.save(tbFile)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	public R update( TbFileDO tbFile){
		tbFileService.update(tbFile);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	public R remove(Integer id, HttpServletRequest request) {
		String fileName = bootdoConfig.getUploadPath() + tbFileService.get(id).getFilename().replace("/files/", "");
		if (tbFileService.remove(id) > 0) {
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
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("common:tbFile:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids){
		tbFileService.batchRemove(ids);
		return R.ok();
	}
	@ResponseBody
	@PostMapping("/upload")
	R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		UserUtil userUtil = new UserUtil();
		String fileName = file.getOriginalFilename();
//		fileName = FileUtil.renameToUUID(fileName);
		TbFileDO sysFile = new TbFileDO();
		sysFile.setFilename(fileName);
		sysFile.setPath(bootdoConfig.getUploadPath()+"/"+fileName);
		sysFile.setRemarks(String.valueOf(FileType.fileType(fileName)));
		sysFile.setUploadDate(new Date());
		sysFile.setUploadUser(userUtil.getUser().getUsername());
		try {
			FileUtil.uploadFile(file.getBytes(), bootdoConfig.getUploadPath(), fileName);
		} catch (Exception e) {
			return R.error();
		}
		if (tbFileService.save(sysFile) > 0) {
			return R.ok().put("fileName",sysFile.getFilename());
		}
		return R.error();
	}
	@RequestMapping("/download")
	public void down(HttpServletResponse resp, String name){
		download(resp,name,name);
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
