package com.bocom.controller.pac;

import com.alibaba.fastjson.JSONObject;
import com.bocom.service.app.AppRestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/manager/arcm")
public class ArcmAppController {

    private static Logger logger = LoggerFactory
            .getLogger(ArcmAppController.class);

    @Resource
    private AppRestService appRestService;

    @ResponseBody
    @RequestMapping("/getAppInfoData")
    public Map<String, Object> getAppInfoDataList(HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String appName = request.getParameter("appName");
            String funcName = request.getParameter("funcName");
            Map<String, Object> param = new HashMap<>();
            if (null != appName && !"".equals(appName)) {
//                查询的两个参数：
//                1、searchType 有两个值
//                 |--app   根据app信息模糊查询
//                        |--page 根据page信息模糊查询
//                2、searchParam 查询具体参数值
                param.put("searchParam", appName);
                param.put("searchType", "app");
            } else {
                param.put("searchParam", funcName);
                param.put("searchType", "page");
            }
            JSONObject appInfoJson = appRestService
                    .queryAppByAppId(param);
            if (logger.isDebugEnabled()) {
                logger.debug("queryAppByAppId param  is " + param);
                logger.debug("appInfoJson is : " + appInfoJson);
            }
            resultMap.put("result", "success");
            resultMap.put("data", appInfoJson);
        } catch (Exception e) {
            logger.debug("getAppInfoDataList failed : " + e);
            resultMap.put("result", "error");
            resultMap.put("reason", "get data failed!");
        }
        return resultMap;
    }
}
