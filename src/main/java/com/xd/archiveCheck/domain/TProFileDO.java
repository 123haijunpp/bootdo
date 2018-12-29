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
public class TProFileDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//外键:引用项目表t_project(id)
	private Long proCode;
	//外键:引用程序文件表t_program_file(id)
	private Long fId;

	/**
	 * 设置：外键:引用项目表t_project(id)
	 */
	public void setProCode(Long proCode) {
		this.proCode = proCode;
	}
	/**
	 * 获取：外键:引用项目表t_project(id)
	 */
	public Long getProCode() {
		return proCode;
	}
	/**
	 * 设置：外键:引用程序文件表t_program_file(id)
	 */
	public void setFId(Long fId) {
		this.fId = fId;
	}
	/**
	 * 获取：外键:引用程序文件表t_program_file(id)
	 */
	public Long getFId() {
		return fId;
	}
}
