package com.gaoqs.intell.biz.taobaoke.model.site;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 站点内容
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name = "tbs_details")
public class SiteDetailsModel {

	private String id;// 商品id
	private String name;// 名称
	private String url;// 推广链接地址
	private String bdUrl;// 百度爬虫链接地址
	private Integer curPercent;// 折扣
	private Double price;// 价格
	private String notes;// 产品说明
	private String types;// 类型，展示在不同的区域
	private String isFromTmall;// 是否是tmall
	private String shopId;// 商店id
	private String shopName;// 商店名称
	private String shopUrl;// 商店链接
	private String picUrl;// 图片链接
	private String status;// 状态 I:正常 U:停用
	private Date gmtCreate;// 创建日期
	private Date gmtModify;// 修改日期
	private Double typePw;// 权重
	private String folderId;// 目录id

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBdUrl() {
		return bdUrl;
	}

	public void setBdUrl(String bdUrl) {
		this.bdUrl = bdUrl;
	}

	public Integer getCurPercent() {
		return curPercent;
	}

	public void setCurPercent(Integer curPercent) {
		this.curPercent = curPercent;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getIsFromTmall() {
		return isFromTmall;
	}

	public void setIsFromTmall(String isFromTmall) {
		this.isFromTmall = isFromTmall;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopUrl() {
		return shopUrl;
	}

	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
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

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
