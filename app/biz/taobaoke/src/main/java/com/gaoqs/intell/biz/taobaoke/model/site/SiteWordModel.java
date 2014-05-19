package com.gaoqs.intell.biz.taobaoke.model.site;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 站点词库
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name = "mil_word_site")
public class SiteWordModel {

	private String id;// 目录编号

	private String name;// 名称

	private String alias;// 别名，10长度

	private Long create_at;// 创建日期

	private int category_id;// 目录

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}


	public String getName() {
		return name;
	}
	
	@Id
	@Column(length = 50)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}



	public Long getCreate_at() {
		return create_at;
	}


	public void setCreate_at(Long create_at) {
		this.create_at = create_at;
	}


	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
}
