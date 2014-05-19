package com.gaoqs.intell.biz.taobaoke.model.site;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 站点目录
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name = "tbs_index_list")
public class SiteIndexModel {

	private String id;// 目录编号

	private String name;// 名称

	private String status;// 状态 I:正常 U:停用

	private Date gmtCreate;// 创建日期

	private Date gmtModify;// 修改日期

	private Double typePw;// 权重

	private String siteId;// 站点id

	@Id
	@Column(length = 50)
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModify() {
		return gmtModify;
	}

	public void setGmtModify(Date gmtModify) {
		this.gmtModify = gmtModify;
	}

	public Double getTypePw() {
		return typePw;
	}

	public void setTypePw(Double typePw) {
		this.typePw = typePw;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
