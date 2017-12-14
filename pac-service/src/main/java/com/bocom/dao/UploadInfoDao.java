package com.bocom.dao;

import com.bocom.domain.upload.UploadInfo;

import java.util.List;

public interface UploadInfoDao {
    int insert(UploadInfo uploadInfo);

    List<UploadInfo> getByRefId(Integer refId, Integer type);

    int deleteByRefId(Integer refId, Integer type);
}
