package com.bocom.util;

import com.dragonsoft.node.adapter.comm.RbspCall;
import com.dragonsoft.node.adapter.comm.RbspConsts;
import com.dragonsoft.node.adapter.comm.RbspService;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by richard on 2017/6/21.
 * 测试云南服务总线调用
 */
public class ConnectUtil {

    // log
    private static Logger logger = LoggerFactory
            .getLogger(ConnectUtil.class);

    private String senderId="C53-00000203";//调用方id
    private String serviceId="S53-00000198";//服务id
    private String userCardId="asdfasd";//用户卡id
    private String userDept="0100";//用户公司部门
    private String userName="ptjian";//用户名
//    private String url="http://10.166.112.97:8080/bus";
    private String url="http://10.166.112.142:8585/node";

    public ConnectUtil() {
    }
//
//    public ConnectUtil(String senderId, String serviceId, String userCardId,
//                       String userDept, String userName, String url) {
//        this.senderId = senderId;
//        this.serviceId = serviceId;
//        this.userCardId = userCardId;
//        this.userDept = userDept;
//        this.userName = userName;
//        this.url = url;
//    }

    public String connectServer(String key, String value, String[] resultCol,
                                int pageSize, int pageNum)
    {
        logger.debug("in connectServer method line:1");
        logger.debug(key+"--"+value+"--"+resultCol.length);
        RbspService service = new RbspService(this.senderId,this.serviceId);
        service.setUserCardId(this.userCardId);
        service.setUserDept(this.userDept);
        service.setUserName(this.userName);
        RbspCall call = service.createCall();
        call.setUrl(this.url);
        call.setMethod(RbspConsts.METHOD_QUERY);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("DataObjectCode", "5300_XCKSJ");
        params.put("InfoCodeMode", "1");
//        params.put("Condition", "GMSFHM like '%6%' ");
        logger.debug("condition is: "+(key+" like '%"+value+"%' "));
        params.put("Condition", key+" like '%"+value+"%' ");
//        params.put("RequiredItems", new String[]{"XM","GMSFHM"});
        params.put("RequiredItems", resultCol);
        //分页start
//        params.put("PageNum", pageNum);
//        params.put("RowsPerPage", pageSize);
        //分页end
        logger.debug("params is: "+params);
        String xmll = call.invoke(params);
        logger.debug("result is: "+xmll);

//        String a=xmll.substring(xmll.lastIndexOf("<Value"), xmll.lastIndexOf("</Value>"))+"</Value>";
//        System.out.println("after: "+a);
//        JSONObject jsonObject=new JSONObject();
//
//        try {
//            Document doc= DocumentHelper.parseText(xmll);
//            System.out.println(doc.getRootElement());
//            Element root=doc.getRootElement();
//            List<Element> list=root.selectNodes("//Value/Row");
////            List<Element> rowEle=root.elements("RBSPMessage");
//            int i=1;
//            for(Element ele:list){
//                System.out.println(i);
//////                System.out.println(ele.getText());
////                Element data1=ele.element("//Data[1]");
////                Element data2=ele.element("//Data[2]");
////                System.out.println(data1.getText());
////                System.out.println(data2.getText());
//
//                System.out.println(ele.node(0).getText()+"--"+ele.node(1).getText());
//                i++;
//            }
//            List<String[]> dataList=new ArrayList<String[]>();
//            int i=1;
//            for(Element ele:list){
//                System.out.print(i+"===>");
////                       System.out.println(ele.getText());
//                       Element data1=ele.element("//Data[1]");
//                       Element data2=ele.element("//Data[2]");
//                       System.out.println(data1.getText());
//                       System.out.println(dßßßata2.getText());
//                String resultStr=ele.node(1).getText()+","+ele.node(3).getText()+
//                        ","+ele.node(5).getText()+ ","+ele.node(7).getText();
//                System.out.println("resultStr: "+resultStr);
//                dataList.add(com.dragonsoft.pci.util.StringUtils.splitString(
//                        resultStr, ",", false));
//
//                System.out.println(ele.node(1).getText()+","+ele.node(3).getText()+
//                        ","+ele.node(5).getText()+ ","+ele.node(7).getText());
//                i++;
//            }
//            System.out.println("datalist size:"+dataList.size());
//            System.out.println("resultCol length:"+resultCol.length);
//
//            JSONArray data=new JSONArray();
//            for(String[] sarray:dataList){
//                System.out.println("sarray length: "+sarray.length);
//                JSONObject obj=new JSONObject();
//                for(int j=0; j<resultCol.length; j++){
//                    System.out.println("j:"+resultCol[j]);
//                    obj.put(resultCol[j], sarray[j]);
//                }
//                data.add(obj);
//            }
//            System.out.println("list: "+data);
//            jsonObject.put("list", data);
//            jsonObject.put("endRow", 10);
//            jsonObject.put("firstPage", 1);
//            jsonObject.put("hasNextPage", false);
//            jsonObject.put("hasPreviousPage", false);
//            jsonObject.put("isFirstPage", true);
//            jsonObject.put("isLastPage", true);
//            jsonObject.put("lastPage", 1);
//            JSONArray navigatepageNums=new JSONArray();
//            navigatepageNums.add(new JSONObject().put("0",	1));
//            navigatepageNums.add(new JSONObject().put("1",	2));
//            navigatepageNums.add(new JSONObject().put("2",	3));
//            jsonObject.put("navigatepageNums", navigatepageNums);
//            jsonObject.put("nextPage", 1);
//            jsonObject.put("pageNum", 1);
//            jsonObject.put("pageSize", 10);
//            jsonObject.put("pages", 10);
//            jsonObject.put("prePage", 1);
//            jsonObject.put("size", 10);
//            jsonObject.put("startRow", 1);
//            jsonObject.put("total", data.size());
//            jsonObject.put("endRow", 10);
//            JSONObject jsonObject2=new JSONObject();
//            JSONObject j=new JSONObject();
//            j.put("page", jsonObject);
//            j.put("success",true);
//            System.out.println("j: "+j);
//        }catch (Exception e){
//            logger.error(String.valueOf(e));
//        }
        return xmll;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUserCardId() {
        return userCardId;
    }

    public void setUserCardId(String userCardId) {
        this.userCardId = userCardId;
    }

    public String getUserDept() {
        return userDept;
    }

    public void setUserDept(String userDept) {
        this.userDept = userDept;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static void main(String[] args)
    {
        String xmll="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<RBSPMessage>" +
                "<Version/>" +
                "<ServiceID>S53-00000198</ServiceID>" +
                "<TimeStamp/>" +
                "<Validity/>" +
                "<Security>" +
                "<Signature Algorithm=\"\"/>" +
                "<CheckCode Algorithm=\"\"/>" +
                "<Encrypt/>" +
                "</Security>" +
                "<Method>" +
                "<Name>Query</Name>" +
                "<Items>" +
                "<Item>" +
                "<Value Type=\"arrayOfArrayOf_string\">" +
                "<Row>" +
                "<Data>000</Data>" +
                "<Data/>" +
                "</Row>" +
                "<Row>" +
                "<Data>XM</Data>" +
                "<Data>GMSFHM</Data>" +
                "</Row>" +
                "<Row>" +
                "<Data>王瑞</Data>" +
                "<Data>100303196010100045</Data>" +
                "</Row>" +
                "<Row>" +
                "<Data>房勇</Data><Data>101000190000000006</Data></Row>" +
                "<Row><Data>赖晓明</Data><Data>104012196212111811</Data>" +
                "</Row><Row><Data>白马</Data><Data>106000195322245804</Data>" +
                "</Row><Row><Data>郭培阳</Data><Data>110100196310235736</Data>" +
                "</Row>" +
                "</Value>" +
                "</Item>" +
                "</Items>" +
                "</Method>" +
                "</RBSPMessage>";

        System.out.println("original: "+xmll);
        String a=xmll.substring(xmll.lastIndexOf("<Value"), xmll.lastIndexOf("</Value>"))+"</Value>";
        System.out.println("after: "+a);

        try {
            Document doc= DocumentHelper.parseText(xmll);
            System.out.println(doc.getRootElement());
            Element root=doc.getRootElement();
            List<Element> list=root.selectNodes("//Value/Row");
//            List<Element> rowEle=root.elements("RBSPMessage");
            int i=1;
            for(Element ele:list){
                System.out.println(i);
////                System.out.println(ele.getText());
//                Element data1=ele.element("//Data[1]");
//                Element data2=ele.element("//Data[2]");
//                System.out.println(data1.getText());
//                System.out.println(data2.getText());
                System.out.println(ele.node(0).getText()+"--"+ele.node(1).getText());
                i++;
            }
        }catch (Exception e){

        }
    }

    public String connectServer2()
    {
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
