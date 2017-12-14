package com.bocom.controller.pac;

import com.bocom.service.pac.WidgetServiceService;
import com.bocom.util.ConnectUtil;
import com.bocom.util.PageUtil;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;



@Controller
@RequestMapping("/server/widget2")
public class WidgetServiceController2 {

    // log
    private static Logger logger = LoggerFactory
            .getLogger(WidgetServiceController2.class);


    @Resource
    private WidgetServiceService widgetServiceService;

    @ResponseBody
    @RequestMapping(value = "/czrkQueryRBSP", method = RequestMethod.GET
            ,produces = "text/html;charset=UTF-8")
    public String czrkQueryRBSP(HttpServletRequest request) {
        logger.debug("a request coming, in czrkQuery.");
        PageInfo pageInfo = null;
        /*
        try {
            String param = request.getParameter("param");
            String name = request.getParameter("name");
            Map<String, Object> paramSelect = new HashMap<>();
            paramSelect.put("param", param);
            if (null != name) {
                paramSelect.put("name", URLDecoder.decode(name, ResultStringKey.CHAR_UTF8));
            }
            PageUtil.setParams(request, paramSelect);
            List<Map<String, Object>> map = widgetServiceService.test(paramSelect);
            pageInfo = new PageInfo(map);
           */
        String result=null;
        try{
            String key1=null;
            String value1=null;
            Enumeration em = request.getParameterNames();
            while (em.hasMoreElements()) {
                String keyTemp=(String)em.nextElement();
                if("resultCol".equals(keyTemp)||"pageNum".equals(keyTemp)||
                        "pageSize".equals(keyTemp)||"serverType".equals(keyTemp)||
                        "_".equals(keyTemp))
                {
                    System.out.println("key1 is: "+ (keyTemp)+", and value is: "
                            +(request.getParameter(keyTemp)));
//                    continue;
                }
                else
                {
                    key1=keyTemp;
                    value1=URLDecoder.decode(request.getParameter(key1),
                            "utf-8");
                    System.out.println("key2 is: "+ key1+", and value is: "+value1);
                }
            }
            String pageSize=request.getParameter("pageSize");
            String pageNum=request.getParameter("pageNum");
            String resultColStr=request.getParameter("resultCol");
            String[] resultCol=null;
            if(resultColStr!=null && resultColStr.trim().length()>0){
                logger.debug("we find resultColStr, it's not null and not empty.");
                if(resultColStr.contains(",")){
                    logger.debug("resultColStr contains ','");
                    resultCol=resultColStr.split(",");
                }else{
                    logger.debug("resultColStr not contains ','");
                    resultCol=new String[1];
                    resultCol[0]=resultColStr;
                }
            }

            if(resultCol!=null && resultCol.length>0){
                logger.debug("we find a resultCol array, length is: "
                        +resultCol.length+"let's have a look!!");
                for(String col:resultCol){
                    logger.debug("in foreach, col is: "+col);
                }
            }else{
                logger.debug("resultCol is null or resultCol's length is 0.");
            }
            logger.debug("begin to connect czrk service!!!");
            ConnectUtil connectUtil=new ConnectUtil();
//            String result=connectUtil
//                    .connectServer(key1, value1,resultCol
//                            ,Integer.parseInt(pageSize)
//                            , Integer.parseInt(pageNum));
            result=connectUtil.connectServer2();//发布到现场的时候，需要使用上方的connectServer()
            logger.debug("connect service successfully, result is: "+result);

        } catch (Exception e) {
            logger.error("testSelect error  msg >>>  " + e);
        }
//        return PageUtil.covertMap(new Object[]{"page"},
//                new Object[]{pageInfo});
        return result;
    }

    /**
     * 模拟巨龙服务总线
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/czrkServer", method = RequestMethod.GET)
    public String czrkServer(HttpServletRequest request,
                             HttpServletResponse response) {
        response.setContentType("application/xml;utf-8");
        response.setCharacterEncoding("utf-8");
        String result="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<RBSPMessage>\n" +
                "    <Version/>\n" +
                "    <ServiceID>S53-00000198</ServiceID>\n" +
                "    <TimeStamp/>\n" +
                "    <Validity/>\n" +
                "    <Security>\n" +
                "        <Signature Algorithm=\"\"/>\n" +
                "        <CheckCode Algorithm=\"\"/>\n" +
                "        <Encrypt/>\n" +
                "    </Security>\n" +
                "    <Method>\n" +
                "        <Name>Query</Name>\n" +
                "        <Items>\n" +
                "            <Item>\n" +
                "                <Value Type=\"arrayOfArrayOf_string\">\n" +
                "                    <Row>\n" +
                "                        <Data>000</Data>\n" +
                "                        <Data/>\n" +
                "                    </Row>\n" +
                "                    <Row>\n" +
                "                        <Data>XM</Data>\n" +
                "                        <Data>GMSFHM</Data>\n" +
                "                    </Row>\n" +
                "                    <Row>\n" +
                "                        <Data>王瑞</Data>\n" +
                "                        <Data>100303196010100045</Data>\n" +
                "                    </Row>\n" +
                "                    <Row>\n" +
                "                        <Data>房勇</Data>\n" +
                "                        <Data>101000190000000006</Data>\n" +
                "                    </Row>\n" +
                "                    <Row>\n" +
                "                        <Data>赖晓明</Data>\n" +
                "                        <Data>104012196212111811</Data>\n" +
                "                    </Row>\n" +
                "                    <Row>\n" +
                "                        <Data>白马\uE787</Data>\n" +
                "                        <Data>106000195322245804</Data>\n" +
                "                    </Row>\n" +
                "                    <Row>\n" +
                "                        <Data>郭培阳</Data>\n" +
                "                        <Data>110100196310235736</Data>\n" +
                "                    </Row>\n" +
                "                </Value>\n" +
                "            </Item>\n" +
                "        </Items>\n" +
                "    </Method>\n" +
                "</RBSPMessage>";

        return result;
    }

}
