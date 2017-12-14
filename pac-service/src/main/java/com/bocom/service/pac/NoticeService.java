package com.bocom.service.pac;

import com.alibaba.fastjson.JSONObject;
import com.bocom.domain.pac.NoticeInfo;

import java.util.List;
import java.util.Map;


public interface NoticeService {

    List<NoticeInfo> getNoticeInfos(Map<String, Object> param);

    //增加通知
    String addNotice(JSONObject jsonObject, String userId);

    //编辑通知
    void updateNotice(Map<String, Object> param);

    //删除通知
    void deleteNotice(Map<String, Object> param);

    //获取重复的序号
    Integer getRepeatSequence(Map<String, Object> param);

    NoticeInfo getNoticeInfoById(Integer id);
}
