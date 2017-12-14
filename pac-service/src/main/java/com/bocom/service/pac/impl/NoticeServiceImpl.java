package com.bocom.service.pac.impl;

import com.alibaba.fastjson.JSONObject;
import com.bocom.dao.NoticeDao;
import com.bocom.domain.pac.NoticeInfo;
import com.bocom.enums.DictionaryEnums;
import com.bocom.service.pac.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class NoticeServiceImpl extends BaseServiceImpl implements NoticeService {

    private static Logger logger = LoggerFactory
            .getLogger(NoticeServiceImpl.class);

    @Resource
    private NoticeDao noticeDao;

    @Override
    public List<NoticeInfo> getNoticeInfos(Map<String, Object> param) {
        return noticeDao.getNoticeInfos(param);
    }

    @Override
    public Integer getRepeatSequence(Map<String, Object> param) {
        return noticeDao.getRepeatSequence(param);
    }

    @Override
    public String addNotice(JSONObject jsonObject, String userId) {
        NoticeInfo noticeInfo = new NoticeInfo();
        try {
            String noticeType = jsonObject.getString("noticeType");
            String content = URLDecoder.decode(jsonObject.getString("content"), "UTF-8");
            if ("private".equals(noticeType)) {
                Integer appId = jsonObject.getInteger("appId");
                if (appId > 0) {
                    String appName = URLDecoder.decode(jsonObject.getString("appName"), "utf-8");
                    noticeInfo.setAppId(appId);
                    noticeInfo.setAppName(appName);
                    noticeInfo.setNoticeType("private");
                    super.saveLog(DictionaryEnums.ACTION_ADD.getDictionaryCode(), DictionaryEnums.BUSINESS_NOTICE.getDictionaryCode(),
                            "为应用 ： " + appName + " 创建 私有型 通知 : " + content);
                } else {
                    return "error";
                }
            } else {
                noticeInfo.setNoticeType("public");
                super.saveLog(DictionaryEnums.ACTION_ADD.getDictionaryCode(), DictionaryEnums.BUSINESS_NOTICE.getDictionaryCode(),
                        "创建 全局型 通知 : " + content);
            }
            noticeInfo.setContent(content.trim());
            noticeInfo.setCreateTime(new Date());
            noticeInfo.setFrequency(jsonObject.getInteger("frequency"));
            noticeInfo.setDeleteFlag(0);
            noticeInfo.setSort(jsonObject.getInteger("sort"));
            noticeInfo.setCreateUserId(userId);

            noticeDao.addNotice(noticeInfo);
        } catch (UnsupportedEncodingException e) {
            logger.error("addNotice error >>>  " + e);
            return "error";
        }
        return "success";
    }

    @Override
    public void updateNotice(Map<String, Object> param) {
        NoticeInfo noticeInfo = noticeDao.getNoticeInfoById(Integer.parseInt(param.get("id") + ""));
        super.saveLog(DictionaryEnums.ACTION_UPDATE.getDictionaryCode(), DictionaryEnums.BUSINESS_NOTICE.getDictionaryCode(),
                "更新序号为：" + noticeInfo.getSort() + "的通知，内容更改为：" + param.get("content"));
        noticeDao.updateNotice(param);
    }

    @Override
    public void deleteNotice(Map<String, Object> param) {
        NoticeInfo noticeInfo = noticeDao.getNoticeInfoById(Integer.parseInt(param.get("id") + ""));
        param.put("deleteFlag", 1);
        noticeDao.deleteNotice(param);
        super.saveLog(DictionaryEnums.ACTION_DELETE.getDictionaryCode(), DictionaryEnums.BUSINESS_NOTICE.getDictionaryCode(),
                "删除通知 ：" + noticeInfo.getContent());
    }

    @Override
    public NoticeInfo getNoticeInfoById(Integer id) {
        return noticeDao.getNoticeInfoById(id);
    }

}
