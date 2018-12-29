package com.xd.common.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 办证指引
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-08-02 14:50:55
 */
public class TbGuideDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//名称
	private String key;
	//配置文件名
	private String filename;
	//创建者
	private Integer createBy;
	//创建时间
	private Date createDate;
	//更新者
	private Integer updateBy;
	//更新时间
	private Date updateDate;
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
	 * 设置：名称
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * 获取：名称
	 */
	public String getKey() {
		return key;
	}
	/**
	 * 设置：配置文件名
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * 获取：配置文件名
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * 设置：创建者
	 */
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建者
	 */
	public Integer getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 设置：更新者
	 */
	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：更新者
	 */
	public Integer getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateDate() {
		return updateDate;
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
