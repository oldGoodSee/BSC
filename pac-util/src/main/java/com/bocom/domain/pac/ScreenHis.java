package com.bocom.domain.pac;

import java.util.Date;

public class ScreenHis {
    private Integer id;

	private String name;

	private Integer pid;

	private String image;

	private String wcode;

	private String sort;

	private Date dealtime;

	private String content;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getWcode() {
		return wcode;
	}

	public void setWcode(String wcode) {
		this.wcode = wcode;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Date getDealtime() {
		return dealtime;
	}

	public void setDealtime(Date dealtime) {
		this.dealtime = dealtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}