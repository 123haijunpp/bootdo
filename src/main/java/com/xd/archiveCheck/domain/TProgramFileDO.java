package com.xd.archiveCheck.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-12-24 10:20:20
 */
public class TProgramFileDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//程序文件表：主键ID，自动增长
	private Long id;
	//文件名字
	private String fileName;
	//文件状态 1 开源文件 2 已归档
	private String state;
	//版本说明：每一次归档要求说明本次修改的内容
	private String imprint;
	//备注
	private String remark;

	/**
	 * 设置：程序文件表：主键ID，自动增长
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：程序文件表：主键ID，自动增长
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：文件名字
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * 获取：文件名字
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 设置：文件状态 1 开源文件 2 已归档
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * 获取：文件状态 1 开源文件 2 已归档
	 */
	public String getState() {
		return state;
	}
	/**
	 * 设置：版本说明：每一次归档要求说明本次修改的内容
	 */
	public void setImprint(String imprint) {
		this.imprint = imprint;
	}
	/**
	 * 获取：版本说明：每一次归档要求说明本次修改的内容
	 */
	public String getImprint() {
		return imprint;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
}
