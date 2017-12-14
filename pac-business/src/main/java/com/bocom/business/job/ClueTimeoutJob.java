package com.bocom.business.job;


import com.bocom.domain.pac.AppBaseInfo;
import com.bocom.enums.DictionaryEnums;
import com.bocom.service.pac.AppBaseInfoService;
import com.bocom.service.pac.BehaviorService;
import com.bocom.service.pac.FunctionService;
import com.bocom.support.servlet.ResultStringKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@EnableScheduling
public class ClueTimeoutJob {

    @Resource
    private FunctionService functionService;

    @Resource
    private AppBaseInfoService appBaseInfoService;

    @Resource
    private BehaviorService behaviorService;

    private static Logger logger = LoggerFactory
            .getLogger(ClueTimeoutJob.class);

    @Scheduled(cron = "${scheduled.job.autoUninstallApp}")
    public void autoUninstallApp() {
        try {
            Date createTime = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(createTime);
            String str = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
            Map<String, Object> param = new HashMap<>();
            param.put("time", str);
            List<AppBaseInfo> canUnistallAppList = appBaseInfoService.getInfoByUninstallTime(param);
            if (logger.isDebugEnabled()) {
                logger.debug("canUnistallAppList  is == " + canUnistallAppList);
            }
            for (AppBaseInfo appBaseInfo : canUnistallAppList) {
                String result = appBaseInfoService.deleteApp(String.valueOf(appBaseInfo.getAppId()), String.valueOf(appBaseInfo.getStatus()), "自动");
                logger.debug("deleteApp " + appBaseInfo.getAppName() + " result ===" + result);
                if (result.equals(ResultStringKey.ERROR)) {
                    behaviorService.saveLog(DictionaryEnums.ERROR_MSG.getDictionaryCode(), DictionaryEnums.BUSINESS_PAC.getDictionaryCode(),
                            "自动卸载  " + appBaseInfo.getAppName() + "  失败");
                }
            }
        } catch (Exception e) {
            logger.error("自动卸载 autoUninstallApp error" + e);
        }
    }

    @Scheduled(cron = "${scheduled.job.autoExportLog}")
    public void autoExportLog() {
        String tabeleName = "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -3);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String str = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());

            calendar.add(Calendar.MONTH, -1);
            tabeleName = new SimpleDateFormat("yyyyMM").format(calendar.getTime());
            Map<String, Object> param = new HashMap<>();
            param.put("startTime", str);
            param.put("tableName", tabeleName);
            behaviorService.addHistoryTable(param);
            behaviorService.saveLog(DictionaryEnums.ACTION_ADD.getDictionaryCode(), DictionaryEnums.BUSINESS_LOG.getDictionaryCode(),
                    "自动归档 和 删除  " + tabeleName + " 月份日志");
            behaviorService.deleteHistoryRecord(param);
        } catch (Exception e) {
            logger.error("自动归档日志 autoExportLog error" + e);
            behaviorService.saveLog(DictionaryEnums.ERROR_MSG.getDictionaryCode(), DictionaryEnums.ERROR_MSG.getDictionaryCode(),
                    "自动归档  " + tabeleName + " 月份日志  失败");
        }
    }

    @Scheduled(cron = "${scheduled.job.cronExpression}")
    public void addTest() {
        try {
            //保持数据库连接
            functionService.addTest();
        } catch (Exception e) {
            logger.error("ClueTimeoutJob.addTest error :" + e);
        }
    }

}
