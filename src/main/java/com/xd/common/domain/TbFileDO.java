package com.xd.common.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 配置文件
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-08-08 11:24:53
 */
public class TbFileDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//文件名
	private String filename;
	//文件路径
	private String path;
	//用户编号
	private Integer userId;
	//录入人
	private String uploadUser;
	//录入时间
	private Date uploadDate;
	//备注信息
	private String remarks;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：文件名
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * 获取：文件名
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * 设置：文件路径
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * 获取：文件路径
	 */
	public String getPath() {
		return path;
	}
	/**
	 * 设置：用户编号
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户编号
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * 设置：录入人
	 */
	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}
	/**
	 * 获取：录入人
	 */
	public String getUploadUser() {
		return uploadUser;
	}
	/**
	 * 设置：录入时间
	 */
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
	/**
	 * 获取：录入时间
	 */
	public Date getUploadDate() {
		return uploadDate;
	}
	/**
	 * 设置：备注信息
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * 获取：备注信息
	 */
	public String getRemarks() {
		return remarks;
	}
}
