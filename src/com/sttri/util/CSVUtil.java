package com.sttri.util;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.*;


public class CSVUtil {

	@SuppressWarnings("rawtypes")
	public static void createCSVFile(List list, Map<String, String> mapFields,OutputStream out) {
    	String sep = ",";
    	Object objClass = null;
    	Map<String,Method> getMap = null;
    	List<String> methodNameList = new ArrayList<String>();
    	Method method = null;
        try { 
        	//加上UTF-8文件的标识字符 ,用来解决office打开CSV文件中文乱码问题
        	out.write(new byte []{( byte ) 0xEF ,( byte ) 0xBB ,( byte ) 0xBF });  
        	 if(mapFields!=null){
                 String key  = "";
                 //开始导出表格头部
                 for (Iterator<String> i = mapFields.keySet().iterator();i.hasNext();) {
                     key = i.next();
                     methodNameList.add(key);
                     out.write(mapFields.get(key).getBytes()); 
                     out.write(sep.getBytes()); 
                 }
                 out.write(System.getProperty("line.separator").getBytes()); 
        	 }
            if (list!=null && list.size()>0) {
            	for (int i = 0; i < list.size(); i++) {
            		objClass = list.get(i);
                    getMap = getAllMethod(objClass);//获得对象所有的get方法
                    //按保存的字段顺序导出内容
                    for (int j = 0; j < methodNameList.size(); j++) {
                        //根据key获取对应方法
                        method = getMap.get("GET"+methodNameList.get(j).toString().toUpperCase());
                        if(method!=null){
                            //从对应的get方法得到返回值
//                            String value = method.invoke(objClass, null).toString();
                            String value = method.invoke(objClass)==null?"":method.invoke(objClass).toString();
                            out.write(value.getBytes()); 
                            out.write((sep).getBytes()); 
                          }
                    }
                    out.write(sep.getBytes()); 
                    out.write(System.getProperty("line.separator").getBytes()); 
                } 
			}
            out.flush(); 
            out.close();
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    }
	 /**
     * 获取类的所有get方法
     * @param cls
     * @return
     */
    public static HashMap<String,Method> getAllMethod(Object obj) throws Exception{
        HashMap<String,Method> map = new HashMap<String,Method>();
        Method[] methods = obj.getClass().getMethods();//得到所有方法
        String methodName = "";
        for (int i = 0; i < methods.length; i++) {
            methodName = methods[i].getName().toUpperCase();//方法名
            if(methodName.startsWith("GET")){
                map.put(methodName, methods[i]);//添加get方法至map中
            }
        }
        return map;
    }
}