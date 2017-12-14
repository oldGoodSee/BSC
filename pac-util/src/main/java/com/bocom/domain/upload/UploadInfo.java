package com.bocom.domain.upload;

import java.util.Date;

public class UploadInfo
{
    private Integer id;

    private String name;
    
    private String url;

    private Date createTime;
    
    private Integer refId;
    
    private Integer type;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Integer getRefId()
    {
        return refId;
    }

    public void setRefId(Integer refId)
    {
        this.refId = refId;
    }

    public UploadInfo()
    {
        super();
    }

    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public UploadInfo(Integer id, String name, String url, Date createTime,
			Integer refId, Integer type) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.createTime = createTime;
		this.refId = refId;
		this.type = type;
	}
	
    
}
