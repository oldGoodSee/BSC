package com.bocom.service.upload.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bocom.dao.UploadInfoDao;
import com.bocom.domain.upload.UploadInfo;
import com.bocom.service.upload.UploadService;

@Service
public class UploadInfoServiceImpl implements UploadService {
	@Autowired
	private UploadInfoDao uploadInfoDao;

	@Override
	public int insert(UploadInfo uploadInfo) {
		return uploadInfoDao.insert(uploadInfo);
	}

	@Override
	public List<UploadInfo> getByRefId(Integer refId, Integer type) {
		return uploadInfoDao.getByRefId(refId,type);
	}

	@Override
	public int deleteByRefId(Integer refId, Integer type) {
		return uploadInfoDao.deleteByRefId(refId,type);
	}

	@Override
	public int updateUploadInfo(Integer refId, Integer type, String uploadInfo) {
		this.deleteByRefId(refId, type);
		String[] split = uploadInfo.split(";");
        if (null != split && split.length > 0)
        {
        	Date date = new Date();
            for (String str : split)
            {
                String[] split2 = str.split(",");
                if (null != split2 && split2.length == 2)
                {
                    String fileName = split2[0];
                    String url = split2[1];
                    UploadInfo data = new UploadInfo();
                    data.setName(fileName);
                    data.setUrl(url);
                    data.setCreateTime(date);
                    data.setRefId(refId);
                    data.setType(type);
                    this.insert(data);
                }
            }
        }
		return 0;
	}

}
