package com.vendetech.project.dingtalk.entity;
import java.io.Serializable;
import java.util.Date;

/*
* 
* gen by beetlsql 2019-04-24
*/
public class EmpDduser   implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id ;
	//是否退出；0-有效，1-离职
	private Integer deleteFlag ;
	private Long createUserId ;
	//钉钉姓名
	private String ddName ;
	//员工UserID
	private String ddUserid ;
	//员工号
	private String empId ;
	//关联钉钉的手机号
	private String mobile ;
	private Long modifyUserId ;
	private Date createTime ;
	//进入钉钉企业日期
	private Date enterStartDate ;
	private Date modifyTime ;
	//退出钉钉企业日期
	private Date outEndDate ;
	
	public EmpDduser() {
	}
	
	public Long getId(){
		return  id;
	}
	public void setId(Long id ){
		this.id = id;
	}
	
	public Integer getDeleteFlag(){
		return  deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag ){
		this.deleteFlag = deleteFlag;
	}
	
	public Long getCreateUserId(){
		return  createUserId;
	}
	public void setCreateUserId(Long createUserId ){
		this.createUserId = createUserId;
	}
	
	public String getDdName(){
		return  ddName;
	}
	public void setDdName(String ddName ){
		this.ddName = ddName;
	}
	
	public String getDdUserid(){
		return  ddUserid;
	}
	public void setDdUserid(String ddUserid ){
		this.ddUserid = ddUserid;
	}
	
	public String getEmpId(){
		return  empId;
	}
	public void setEmpId(String empId ){
		this.empId = empId;
	}
	
	public String getMobile(){
		return  mobile;
	}
	public void setMobile(String mobile ){
		this.mobile = mobile;
	}
	
	public Long getModifyUserId(){
		return  modifyUserId;
	}
	public void setModifyUserId(Long modifyUserId ){
		this.modifyUserId = modifyUserId;
	}
	
	public Date getCreateTime(){
		return  createTime;
	}
	public void setCreateTime(Date createTime ){
		this.createTime = createTime;
	}
	
	public Date getEnterStartDate(){
		return  enterStartDate;
	}
	public void setEnterStartDate(Date enterStartDate ){
		this.enterStartDate = enterStartDate;
	}
	
	public Date getModifyTime(){
		return  modifyTime;
	}
	public void setModifyTime(Date modifyTime ){
		this.modifyTime = modifyTime;
	}
	
	public Date getOutEndDate(){
		return  outEndDate;
	}
	public void setOutEndDate(Date outEndDate ){
		this.outEndDate = outEndDate;
	}
	
	
	

}
