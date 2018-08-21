package org.sqlToDoc;

/** 
 * Description: 生成db文档的bean
 * 
 * @author jiujiya
 * @version 1.0 
 */
public class DbDocBean {
	
	// 文档模板
	public String[] styleList = new String[] {};
	// 文档名称
	public String projectText = "数据库设计文档";
	// 文档路径
	public String pathText = "";
	// 数据库IP
	public String dbIpText = "";
	// 端口号
	public String dbPortText = "3306";
	// 用户名
	public String dbUserText = "root";
	// 密码
	public String dbPsText = "";
	// 数据库名称
	public String dbNameText = "";
	// 数据库连接池
	public String driverClassName = "com.mysql.jdbc.Driver";
	// ftl模板基础路径
	public String ftlBasePath = "";
	
	/**
	 * @param url 数据库url
	 */
	public void setDbUrl(String url) {
		url = url.substring(13);
		dbIpText = url.split(":")[0];
		url = url.substring(dbIpText.length() + 1);
		dbPortText = url.split("/")[0];
		url = url.substring(dbPortText.length() + 1);
		dbNameText = url.split("\\?")[0];
	}
}
