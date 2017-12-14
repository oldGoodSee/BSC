package com.bocom.controller.view;

import com.bocom.business.OperationLogBusiness;
import com.bocom.util.PageUtil;
import com.bocom.util.StringUtils;
import com.bocom.util.UserUtils;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manager/menuAction")
public class PageViewController {

    // log
    private static Logger logger = LoggerFactory
            .getLogger(PageViewController.class);

    @Resource
    private OperationLogBusiness operationLogBusiness;

    @RequestMapping("queryLog")
    public String queryLog(HttpServletRequest request, HttpSession session, Model model) {

        try {
            String operationType = request.getParameter("operationType");
            String businessType = request.getParameter("businessType");
            String content = request.getParameter("content");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            Map<String, Object> param = new HashMap<>();
            List<String> userRoleCode = UserUtils.getUserRoleCode(session);
            if (!userRoleCode.isEmpty() && !userRoleCode.contains("1")) {
                param.put("userName", UserUtils.getUserName(session));
            }
            param.put("operationType", StringUtils.isNullOrEmpty(operationType) ? null : operationType);
            param.put("businessType", StringUtils.isNullOrEmpty(businessType) ? null : businessType);
            param.put("content", StringUtils.isNullOrEmpty(content) ? null : content);
            if (!StringUtils.isNullOrEmpty(startTime, endTime)) {
                param.put("startTime", startTime);
                param.put("endTime", endTime);
            } else if (!StringUtils.isNullOrEmpty(startTime)) {
                param.put("startTime", startTime);
            } else if (!StringUtils.isNullOrEmpty(endTime)) {
                param.put("endTime", endTime);
            }
            PageUtil.setParams(request, param);
            List<Map<String, Object>> logInfoList = operationLogBusiness.queryLogByLimit(param);
            List<Map<String, Object>> dicInfoListMap = operationLogBusiness.queryDictionaryMap(null);
            PageInfo pageInfo = new PageInfo(logInfoList);
            model.addAttribute("logPage", logInfoList);
            model.addAttribute("pageInfo", pageInfo);
            request.getServletContext().setAttribute("dicInfoListMap", dicInfoListMap);
            if (!StringUtils.isNullOrEmpty(operationType)) {
                model.addAttribute("operationType", operationType);
            }
            if (!StringUtils.isNullOrEmpty(content)) {
                model.addAttribute("content", content);
            }
            if (!StringUtils.isNullOrEmpty(businessType)) {
                model.addAttribute("businessType", businessType);
            }
            if (!StringUtils.isNullOrEmpty(startTime)) {
                model.addAttribute("startTime", startTime);
            }
            if (!StringUtils.isNullOrEmpty(endTime)) {
                model.addAttribute("endTime", endTime);
            }
        } catch (Exception e) {
            logger.error("queryLog error " + e);
        }

        return "pac_gl/logManage";
    }
}
