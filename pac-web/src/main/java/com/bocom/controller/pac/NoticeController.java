package com.bocom.controller.pac;

import com.alibaba.fastjson.JSONObject;
import com.bocom.service.pac.NoticeService;
import com.bocom.support.servlet.ResultStringKey;
import com.bocom.util.StringUtils;
import com.bocom.util.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/manager/notice")
public class NoticeController {

    private static Logger logger = LoggerFactory
            .getLogger(NoticeController.class);

    @Resource
    private NoticeService noticeService;

    private static final String MSG = "数据有问题，请检查！";
    @ResponseBody
    @RequestMapping(value = "/configNotice", method = RequestMethod.POST)
    public Map<String, Object> configureNotice(HttpServletRequest request, @RequestBody JSONObject jsonObject, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        String type = jsonObject.getString("type");
        try {
            JSONObject noticeInfoJson = jsonObject.getJSONObject("noticeInfo");
            String userId = UserUtils.getUserId(session);
            if (!StringUtils.isNullOrEmpty(userId, type) && !noticeInfoJson.isEmpty()) {
                result = configureNoticeByType(type, noticeInfoJson, userId);
                if (result.isEmpty()) {
                    result.put(ResultStringKey.RESULT, ResultStringKey.SUCCESS);
                    return result;
                }else {
                    return result;
                }
            } else {
                result.put(ResultStringKey.RESULT, ResultStringKey.ERROR);
                result.put(ResultStringKey.REASON, MSG);
                return result;
            }
        } catch (Exception e) {
            logger.error("configNotice  error  >>>>>   " + e);
            if (e.getMessage().contains("too long")) {
                result.put(ResultStringKey.REASON, "通知内容字数过多，请重新输入!");
            } else {
                result.put(ResultStringKey.REASON, "配置通知失败，请重试！");
            }
            result.put(ResultStringKey.RESULT, ResultStringKey.ERROR);
            return result;
        }
    }

    private Map<String, Object> configureNoticeByType(String type, JSONObject noticeInfoJson, String userId) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> param = new HashMap<>();
        if ("update".equals(type)) {
            String content = noticeInfoJson.getString("content");
            if (content.isEmpty()) {
                result.put(ResultStringKey.RESULT, ResultStringKey.ERROR);
                result.put(ResultStringKey.REASON, MSG);
                return result;
            }
            param.put("content", URLDecoder.decode(content.trim(), "UTF-8"));
            param.put("lastModifyTime", new Date());
            param.put("id", noticeInfoJson.getString("id"));
            noticeService.updateNotice(param);
        } else if ("delete".equals(type)) {
            String id = noticeInfoJson.getString("id");
            param.put("id", id.trim());
            noticeService.deleteNotice(param);
        } else if ("add".equals(type)) {
            String sort = noticeInfoJson.getString("sort");
            String noticeType = noticeInfoJson.getString("noticeType");
            Integer appId = noticeInfoJson.getInteger("appId");
            if ("public".equals(noticeType)) {
                param.put("sort", sort);
                param.put("noticeType", noticeType);
                param.put("createUserId", userId);
            } else {
                param.put("sort", sort);
                param.put("appId", appId);
                param.put("createUserId", userId);
            }
            Integer repeatCount = noticeService.getRepeatSequence(param);
            if (repeatCount > 0) {
                result.put(ResultStringKey.RESULT, ResultStringKey.ERROR);
                result.put(ResultStringKey.REASON, "序号重复 请重新设置！");
                return result;
            }
            String resultStr = noticeService.addNotice(noticeInfoJson, userId);
            if (!ResultStringKey.SUCCESS.equals(resultStr)) {
                result.put(ResultStringKey.RESULT, ResultStringKey.ERROR);
                result.put(ResultStringKey.REASON, MSG);
                return result;
            }
        }
        return result;
    }

}
