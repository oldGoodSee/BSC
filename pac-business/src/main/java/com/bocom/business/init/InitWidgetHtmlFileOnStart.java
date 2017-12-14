package com.bocom.business.init;

import com.bocom.business.WidgetBusiness;
import com.bocom.domain.pac.AppWidgetInfo;
import com.bocom.service.pac.WidgetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 在系统启动时候  去把所有的微应用下载到项目内来解决组装微应用时候的404
 *
 * @author QY
 */
@Service
public class InitWidgetHtmlFileOnStart implements ApplicationListener {

    private static Logger logger = LoggerFactory.getLogger(InitWidgetHtmlFileOnStart.class);


    @Resource
    private WidgetBusiness widgetBusiness;

    @Resource
    private WidgetService widgetService;

    @Value("${web.appRoot.key}")
    private String appRootKey;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        //只在初始化“根上下文”的时候执行
        if (applicationEvent.getSource() instanceof XmlWebApplicationContext) {
            String name = ((XmlWebApplicationContext) applicationEvent.getSource()).getDisplayName();
            if (name.equals("Root WebApplicationContext")) {
                Map<String, Object> param = new HashMap<>();
                try {
                    param.put("status", "1");
                    String webAppPath = System.getProperty(appRootKey);
                    logger.info("微应用  初始化开始 ！");
                    List<AppWidgetInfo> appWidgetInfos = widgetService.getAppWidgetList(param);
                    for (AppWidgetInfo widgetInfo : appWidgetInfos) {
                        widgetBusiness.unzipWidgetFile(widgetInfo.getWidgetId().toString(), webAppPath);
                    }
                    logger.info("微应用  初始化完成 ！共初始化了  " + appWidgetInfos.size() + " 个微应用！");
                } catch (Exception e) {
                    logger.error("WebAppInitializer   init caseAlarm  data error ", e);
                }
            }
        }


    }
}
