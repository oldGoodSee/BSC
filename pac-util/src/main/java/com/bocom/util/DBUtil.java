package com.bocom.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by richardwong on 2017/6/1.
 */
public class DBUtil {

    public static Connection getMySQLConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(
        		"jdbc:mysql://localhost:3306/databaseName","root", "root");
        return conn;
    }

    /**
     * 获取当前数据库下的所有表名称
     * @return
     * @throws Exception
     */
    public static List getAllTableName() throws Exception {
        List tables = new ArrayList();
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SHOW TABLES ");
        while (rs.next()) {
            String tableName = rs.getString(1);
            tables.add(tableName);
        }
        rs.close();
        stmt.close();
        conn.close();
        return tables;
    }


    /**
     * 获得某表的建表语句
     * @param tableName
     * @return
     * @throws Exception
     */
    public static Map getCommentByTableName(List tableName) throws Exception {
        Map map = new HashMap();
        Connection conn = getMySQLConnection();
        Statement stmt = conn.createStatement();
        for (int i = 0; i < tableName.size(); i++) {
            String table = (String) tableName.get(i);
            ResultSet rs = stmt.executeQuery("SHOW CREATE TABLE " + table);
            if (rs != null && rs.next()) {
                String createDDL = rs.getString(2);
                String comment = parse(createDDL);
                map.put(table, comment);
            }
            rs.close();
        }
        stmt.close();
        conn.close();
        return map;
    }
    /**
     * 获得某表中所有字段的注释
     * @param tableName
     * @return
     * @throws Exception
     */
    public static List<Map<String,String>> getColumnCommentByTableName(Connection connection,
                                                                       String table)
            throws Exception {
        List<Map<String,String>> resultList=new ArrayList<Map<String,String>>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("show full columns from " + table);
        System.out.println("【"+table+"】");
        while (rs.next()) {
//              System.out.println("字段名称：" + rs.getString("Field") + "\t"+ "字段注释：" + rs.getString("Comment") );
//            System.out.println(rs.getString("Field") + "\t:\t"+  rs.getString("Comment") );
            Map<String,String> map = new HashMap<String,String>();
            map.put("field", rs.getString("Field"));
            map.put("comment", rs.getString("Comment"));
            resultList.add(map);
        }
//          }
        rs.close();
        stmt.close();
//        connection.close();
        return resultList;
    }



    /**
     * 返回注释信息
     * @param all
     * @return
     */

    public static String parse(String all) {
        String comment = null;
        int index = all.indexOf("COMMENT='");
        if (index < 0) {
            return "";
        }
        comment = all.substring(index + 9);
        comment = comment.substring(0, comment.length() - 1);
        return comment;
    }

    public static void main(String[] args) throws Exception {
//        List tables = getAllTableName();
//        Map tablesComment = getCommentByTableName(tables);
//        Set names = tablesComment.keySet();
//        Iterator iter = names.iterator();
//        while (iter.hasNext()) {
//            String name = (String) iter.next();
//            System.out.println("Table Name: " + name + ", Comment: " + tablesComment.get(name));
//        }
//    	int b=4;
//    	System.out.println("b:"+b);
//        getColumnCommentByTableName(tables);
    	
    }
    
   
    
    
}
