package com.xd.entryAndExitManagement.domain;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;



/**
 * 填表数据
 * 
 * @author andrew
 * @email 1992lcg@163.com
 * @date 2018-09-13 10:18:02
 */
public class TbFormDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//用户编号
	private Integer userId;
	//填表用户姓名
	private String xm;
	//证件类型
	private Integer idType;
	//证件号码
	private String idNo;
	//表单类型
	private Integer formType;
	//联系电话
	private String telephone;
	//填表时间
	private Date formDate;
	//填表时间的数字，例如20180802
	private Integer formDateNum;
	//预约业务办理时间
	private Date meetingDate;
	//预约服务大厅编号
	private Integer hallId;
	//服务大厅名称
	private String hallName;
	//JSON格式的填表数据
	private String data;
	//录入人
	private String uploadUser;
	//录入时间
	private Date uploadDate;
	//同步标志
	private Integer sync;
	//最后一次同步的时间
	private Date syncTime;
	//打印标志
	private Integer print;
	//打印时间
	private Date printTime;
	//审核标志，1 - 成功，2 - 失败
	private Integer exam;
	//审核时间
	private Date examTime;
	//受理标志，1 - 成功，2 - 失败
	private Integer deal;
	//受理时间
	private Date dealTime;
	//问题描述
	private String reason;
	//1-正常 0-取消
	private Integer status;
	//取消时间
	private Date cancelTime;
	//所在地区
	private String district;
	private JSONObject jsondata;

	public JSONObject getJsondata() {
		return jsondata;
	}

	public void setJsondata(JSONObject jsondata) {
		this.jsondata = jsondata;
	}

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
	 * 设置：填表用户姓名
	 */
	public void setXm(String xm) {
		this.xm = xm;
	}
	/**
	 * 获取：填表用户姓名
	 */
	public String getXm() {
		return xm;
	}
	/**
	 * 设置：证件类型
	 */
	public void setIdType(Integer idType) {
		this.idType = idType;
	}
	/**
	 * 获取：证件类型
	 */
	public Integer getIdType() {
		return idType;
	}
	/**
	 * 设置：证件号码
	 */
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	/**
	 * 获取：证件号码
	 */
	public String getIdNo() {
		return idNo;
	}
	/**
	 * 设置：表单类型
	 */
	public void setFormType(Integer formType) {
		this.formType = formType;
	}
	/**
	 * 获取：表单类型
	 */
	public Integer getFormType() {
		return formType;
	}
	/**
	 * 设置：联系电话
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * 获取：联系电话
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * 设置：填表时间
	 */
	public void setFormDate(Date formDate) {
		this.formDate = formDate;
	}
	/**
	 * 获取：填表时间
	 */
	public Date getFormDate() {
		return formDate;
	}
	/**
	 * 设置：填表时间的数字，例如20180802
	 */
	public void setFormDateNum(Integer formDateNum) {
		this.formDateNum = formDateNum;
	}
	/**
	 * 获取：填表时间的数字，例如20180802
	 */
	public Integer getFormDateNum() {
		return formDateNum;
	}
	/**
	 * 设置：预约业务办理时间
	 */
	public void setMeetingDate(Date meetingDate) {
		this.meetingDate = meetingDate;
	}
	/**
	 * 获取：预约业务办理时间
	 */
	public Date getMeetingDate() {
		return meetingDate;
	}
	/**
	 * 设置：预约服务大厅编号
	 */
	public void setHallId(Integer hallId) {
		this.hallId = hallId;
	}
	/**
	 * 获取：预约服务大厅编号
	 */
	public Integer getHallId() {
		return hallId;
	}
	/**
	 * 设置：服务大厅名称
	 */
	public void setHallName(String hallName) {
		this.hallName = hallName;
	}
	/**
	 * 获取：服务大厅名称
	 */
	public String getHallName() {
		return hallName;
	}
	/**
	 * 设置：JSON格式的填表数据
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * 获取：JSON格式的填表数据
	 */
	public String getData() {
		return data;
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
	 * 设置：同步标志
	 */
	public void setSync(Integer sync) {
		this.sync = sync;
	}
	/**
	 * 获取：同步标志
	 */
	public Integer getSync() {
		return sync;
	}
	/**
	 * 设置：最后一次同步的时间
	 */
	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}
	/**
	 * 获取：最后一次同步的时间
	 */
	public Date getSyncTime() {
		return syncTime;
	}
	/**
	 * 设置：打印标志
	 */
	public void setPrint(Integer print) {
		this.print = print;
	}
	/**
	 * 获取：打印标志
	 */
	public Integer getPrint() {
		return print;
	}
	/**
	 * 设置：打印时间
	 */
	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}
	/**
	 * 获取：打印时间
	 */
	public Date getPrintTime() {
		return printTime;
	}
	/**
	 * 设置：审核标志，1 - 成功，2 - 失败
	 */
	public void setExam(Integer exam) {
		this.exam = exam;
	}
	/**
	 * 获取：审核标志，1 - 成功，2 - 失败
	 */
	public Integer getExam() {
		return exam;
	}
	/**
	 * 设置：审核时间
	 */
	public void setExamTime(Date examTime) {
		this.examTime = examTime;
	}
	/**
	 * 获取：审核时间
	 */
	public Date getExamTime() {
		return examTime;
	}
	/**
	 * 设置：受理标志，1 - 成功，2 - 失败
	 */
	public void setDeal(Integer deal) {
		this.deal = deal;
	}
	/**
	 * 获取：受理标志，1 - 成功，2 - 失败
	 */
	public Integer getDeal() {
		return deal;
	}
	/**
	 * 设置：受理时间
	 */
	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}
	/**
	 * 获取：受理时间
	 */
	public Date getDealTime() {
		return dealTime;
	}
	/**
	 * 设置：问题描述
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * 获取：问题描述
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * 设置：1-正常 0-取消
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：1-正常 0-取消
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：取消时间
	 */
	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}
	/**
	 * 获取：取消时间
	 */
	public Date getCancelTime() {
		return cancelTime;
	}
	/**
	 * 设置：所在地区
	 */
	public void setDistrict(String district) {
		this.district = district;
	}
	/**
	 * 获取：所在地区
	 */
	public String getDistrict() {
		return district;
	}
}
