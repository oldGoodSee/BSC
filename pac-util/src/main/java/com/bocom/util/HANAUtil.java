package com.bocom.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;

/**
 * 用于和SAP HANA数据库交互的工具类
 * RICHARD.W
 */
public class HANAUtil {

    private static Logger logger = LoggerFactory
            .getLogger(HANAUtil.class);
//	private String DRIVER = "com.sap.db.jdbc.Driver"; // jdbc 4.0
//	private String URL = "jdbc:sap://sap1.vicp.cc:38815/HDB?reconnect=true";
//	private int NUMEVERYPAGE = 10;
	private String DRIVER=""; // jdbc 4.0
	private String URL="";
	private int NUMEVERYPAGE=0;
	private String userNameBase;
	private String pwdBase;

	public HANAUtil() {
	}

	public static void main(String[] args) {
		HANAUtil demo = new HANAUtil();
		try {
			demo.select();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		String sql="select * from 303803498.USERINFO;";
//		String sql2=" "+sql.substring(sql.lastIndexOf("from"),sql.length());
//		sql2="select count(*) "+sql2;
//		logger.debug(sql2);
		
	}

	/**
	 * 主程序入口
	 * 
	 * @param url
	 * @param userName
	 * @param pwd
	 * @param sql
	 * @param tableName1
	 * @param targetPage
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryHANA(String url, String userName, String pwd, 
			String sql, String tableName1, int targetPage)
			throws Exception {
		logger.debug("in the beginning of queryHANA, url is:"+url+", userName is:"+
			userName+", pwd is:"+pwd+", sql is:"+sql +",targetPage is:"+targetPage);
		JSONObject result = new JSONObject();
		logger.debug("准备创建数据库连接");
		this.initDbInfo();
		//救火方法，如果数据库连接的用户和密码没拿到，就赶紧从配置文件获得
		if(userName==null || userName.trim().length()==0){
			userName=userNameBase;
		}
		if(pwd==null || pwd.trim().length()==0){
			pwd=pwdBase;
		}
		Connection con = this.getConnection(DRIVER, url, userName, pwd);
		logger.debug("创建数据库连接成功");
		result.put("commentList", new JSONArray());
		// PreparedStatement pstmt = con.prepareStatement("select
		// USER_ID,USER_NAME,USER_MODE from SYS.USERS");
		result=this.processResult(userName, result, sql, con, targetPage);
		logger.debug("after run processresult, result is:"+result);
		this.closeConnection(con);
		return result;
	}

	/**
	 * 测试方法，项目运行的时候不会调用到这个函数
	 * @throws Exception
	 */
	public void select() throws Exception {
		logger.debug("准备创建数据库连接");
//		this.initDbInfo();
        if(this.NUMEVERYPAGE==0){
            this.NUMEVERYPAGE=10;
        }
		Connection con = this.getConnection("com.sap.db.jdbc.Driver",
                "jdbc:sap://sap1.vicp.cc:38815/HDB?reconnect=true",
                "303803498", "Hsm57221");
		logger.debug("创建数据库连接成功");
		// getCommentList(con);
		// PreparedStatement pstmt = con.prepareStatement("select
		// USER_ID,USER_NAME,USER_MODE from SYS.USERS");
		// PreparedStatement pstmt = con.prepareStatement("select * from
		// 303803498.USERINFO");
		String sql = "select * from USERINFO";
		// ResultSet rs = pstmt.executeQuery();
		this.processResult("303803498", new JSONObject(), sql, con, 1);
		this.closeConnection(con);
	}
	
	/**
	 * 处理结果集
	 * 
	 * @param result
	 * @param sql
	 * @param con
	 * @param targetPage
	 * @throws Exception
	 */
	private JSONObject processResult(String userName, JSONObject result, String sql,
			Connection con, int targetPage)
			throws Exception 
	{
		this.processPageInfo(con, sql, result);
		String tableName = this.getTableName(sql);
		result.put("commentList", this.getCommentList(con, userName, tableName));
		// 带入分页元素进行查询，上面这个processPageInfo查询是为了总条数，hana不能用resultset的游标
		PreparedStatement pstmt2 = con.prepareStatement(sql + 
				" limit "+ NUMEVERYPAGE +" offset " + (targetPage - 1));
		String sql_=sql +
                " limit "+ NUMEVERYPAGE +" offset " + (targetPage - 1);
		ResultSet rs2 = pstmt2.executeQuery();
		JSONArray array = new JSONArray();
		if (rs2.next()) {
			ResultSetMetaData rsmd = rs2.getMetaData();
			int colNum = rsmd.getColumnCount();
			logger.debug("===================");
			// 获取列数
			do {
				// 遍历每一列
				JSONObject jsonObj = new JSONObject();
				for (int i = 1; i <= colNum; i++) {
					String columnName = rsmd.getColumnLabel(i);
					String value = rs2.getString(columnName);
					jsonObj.put(columnName, value);
				}
				array.add(jsonObj);
				System.out.print("\n");
			} while (rs2.next());
		} else {
			logger.debug("there is no data.");
		}
		logger.debug("array: " + array.toJSONString());
		result.put("data", array);
		result.put("success", true);
		result.put("message", "success");
		logger.debug(result.toJSONString());
		this.closeStatement(pstmt2);
		logger.debug("in processResult,result is:"+result.toJSONString());
		return result;
	}

	private Connection getConnection(String driver, String url,
			String user, String password) throws Exception {

		Class.forName(driver);
		return DriverManager.getConnection(url, user, password);
	}

	private void closeConnection(Connection con) throws Exception {
		if (con != null) {
			con.close();
		}
	}

	private void closeStatement(Statement stmt) throws Exception {
		if (stmt != null) {
			stmt.close();
		}
	}
	
	/**
	 * 获得字段注释
	 * 
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private JSONArray getCommentList(Connection con, String userName,
			String tableName) 
			throws SQLException {
		DatabaseMetaData dMetaData = con.getMetaData();
		ResultSet commentResult = dMetaData
				.getColumns(null, userName, tableName.toUpperCase(), "%");
		JSONArray commentLists = new JSONArray();
		while (commentResult.next()) {
			logger.debug(
					"解析表字段注释：" + commentResult.getString("COLUMN_NAME") + " = "
			+ commentResult.getString("REMARKS"));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("field", commentResult.getString("COLUMN_NAME"));
			jsonObject.put("comment", commentResult.getString("REMARKS"));
			commentLists.add(jsonObject);
		}
		return commentLists;
	}
	
	/**
	 * 处理分页相关信息
	 * @param con
	 * @param sql
	 * @param result
	 * @throws Exception 
	 */
	private void processPageInfo(Connection con,String sql,JSONObject result)
			throws Exception
	{
		String sql2=" "+sql.substring(sql.lastIndexOf("from"),sql.length());
		sql2="select count(*) C "+sql2;
		PreparedStatement pstmt = con.prepareStatement(sql2);
		ResultSet rs = pstmt.executeQuery();
		int totalNum = 0;
		while (rs.next()) {// get totalNum
			logger.debug("获得了总记录数!!!");
			totalNum=rs.getInt("C");
		}
		logger.debug("一共有" + totalNum + "条记录");
		// 查询数据
		result.put("totalNum", totalNum);// 总行数
		double tatalPageNum = Math.ceil(Double.parseDouble(totalNum + "")
				/ Double.parseDouble(NUMEVERYPAGE + ""));// 总页数
		result.put("totalPageNum", tatalPageNum);
		this.closeStatement(pstmt);
	}

	/**
	 * get table name from sql sentence
	 * @param sql
	 * @return
	 */
	private String getTableName(String sql) {
		String tableName = null;
		if (sql != null) {
			String[] sqlArray = sql.split(" ");
			for (int i = 0; i < sqlArray.length; i++) {
				String s = sqlArray[i];
				if (s.equalsIgnoreCase("from")) {
					tableName = sqlArray[i + 1];
					break;
				}
			}
		}
		return tableName;
	}
	
	/**
	 * 初始化数据库信息
	 * @throws IOException
	 */
	private void initDbInfo() throws IOException{
		DRIVER=ConfigGetPropertyUtilHANA.get("hana.driver");
    	URL=ConfigGetPropertyUtilHANA.get("hana.url");
    	userNameBase=ConfigGetPropertyUtilHANA.get("hana.user");
    	pwdBase=ConfigGetPropertyUtilHANA.get("hana.pwd");
    	NUMEVERYPAGE=Integer.parseInt(ConfigGetPropertyUtilHANA.get("hana.numeverypage"));
    	logger.debug("driver:"+DRIVER);
    	logger.debug("url:"+URL);
    	logger.debug("NUMEVERYPAGE:"+NUMEVERYPAGE);
    	logger.debug("userNameBase:"+userNameBase);
    	logger.debug("pwdBase:"+pwdBase);
     }
}
