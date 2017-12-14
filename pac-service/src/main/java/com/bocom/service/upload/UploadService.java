package com.bocom.service.upload;

import java.util.List;

import com.bocom.domain.upload.UploadInfo;

public interface UploadService
{
    public int insert(UploadInfo uploadInfo);
    
    public List<UploadInfo> getByRefId(Integer refId,Integer type);
    
    public int deleteByRefId(Integer refId,Integer type);
    
    public int updateUploadInfo(Integer refId,Integer type,String uploadInfos);
}
