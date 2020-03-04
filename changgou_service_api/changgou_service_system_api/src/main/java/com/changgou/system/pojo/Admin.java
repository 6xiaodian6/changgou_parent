package com.changgou.system.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * admin实体类
 * @author 黑马架构师2.5
 *
 */
@Table(name="tb_admin")
public class Admin implements Serializable {

	@Id
	private Integer id;//id
	private String loginName;//用户名
	private String password;//密码
	private String status;//状态

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}



}
