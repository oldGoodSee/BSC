package com.bocom.dao;

import com.bocom.domain.pac.NoticeInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by bocom-qy on 2017-1-16.
 */
public interface NoticeDao {

    List<NoticeInfo> getNoticeInfos(Map<String, Object> param);

    void addNotice(NoticeInfo noticeInfo);

    void updateNotice(Map<String, Object> param);

    void deleteNotice(Map<String, Object> param);

    Integer getRepeatSequence(Map<String, Object> param);

    NoticeInfo getNoticeInfoById(Integer id);
}
