package org.sqlToDoc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import freemarker.template.Configuration;
import freemarker.template.Template;
  
/** 
 * @Description:freemarker工具类
 * 
 * @author:-jiujiya
 * @version:1.0 
 */
public class FreeMarkerUtils {  
      
    /** 是否已初始化 */  
    private static boolean isInit = false;  
      
    /** 应用所在路径 */  
    private static String appPath = null;  
      
    /** 编码格式 UTF-8 */  
    private static final String ENCODING = "UTF-8";  
  
    /** FreeMarker配置 */  
    private static Configuration config = new Configuration();  
      
    /** 路径分割符 */  
    public static final String PATH_SEPARATOR = "/";  
    
    /** 
     * 初始化FreeMarker配置 
     * @param applicationPath 应用所在路径 
     * @throws IOException 
     */  
    public static void initFreeMarker(String applicationPath) throws IOException {
        if (!(isInit)) {
            appPath = applicationPath;  
            // 加载模版
            File file = new File(new StringBuffer(appPath).append(File.separator).toString());  
            // 设置要解析的模板所在的目录，并加载模板文件  
            config.setDirectoryForTemplateLoading(file);
            // 设置文件编码为UTF-8
            config.setEncoding(Locale.getDefault(), ENCODING);  
            isInit = true;
        }  
    }  
  
    /** 
     * 据数据及模板生成文件 
     *  
     * @param data 
     *            一个Map的数据结果集 
     * @param templateFileName 
     *            ftl模版路径(已默认为WEB-INF/templates,文件名相对此路径)  
     * @param outFileName 
     *            生成文件名称(可带路径) 
     * @param isAbsPath 
     *            是否绝对路径 
     * @throws Exception 
     */  
    public static void crateFile(Map<String, Object> data, String templateFileName, String outFileName, boolean isAbsPath) throws Exception {  
        if(!isInit){  
        	throw new RuntimeException("FreeMarker模板引擎未初始化,请确认已经调用initFreeMarker()方法对其进行了初始化");
        }  
        Writer out = null;  
        try {  
            // 获取模板,并设置编码方式，这个编码必须要与页面中的编码格式一致  
            Template template = config.getTemplate(templateFileName, ENCODING);  
            template.setEncoding(ENCODING);  
              
            // 生成文件路径  
            // 如果是绝对路径则直接使用  
            if(isAbsPath){  
                outFileName = new StringBuffer(outFileName).toString();  
            } else{  
                // 相对路径则使用默认的appPath加上输入的文件路径  
                outFileName = new StringBuffer(appPath).append(File.separator).append(outFileName).toString();  
            }  
            File outFile = new File(outFileName);  
            out = new OutputStreamWriter(new FileOutputStream(outFile), ENCODING);  
              
            // 处理模版  
            template.process(data, out);  
              
            out.flush();  
        } finally{  
            try {  
                if(out != null){  
                    out.close();  
                }  
            } catch (IOException e) {  
            	e.printStackTrace();
            }  
        }  
    }  
  
}
