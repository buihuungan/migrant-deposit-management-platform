package org.jeecg.modules.system.util;

import com.alibaba.fastjson.JSON;

import java.util.*;

/**
 * 
 * 功能描述: 对象工具类
 */
public final class ObjectUtils {
	/**
	 * 
	 * 把对象转成XML格式字符串
	 */
	public static String toXml(Object o) {
//		JSON jsonObject = JSONSerializer.toJSON(o);
//		XMLSerializer xmlSerializer = new XMLSerializer();
//		return xmlSerializer.write(jsonObject);
//		JSONObject jsonObject = JSON.parseObject(o.toString());
//		XMLSerializer xmlSerializer = new XMLSerializer();
//		return xmlSerializer.write(jsonObject);
		return o.toString();
	}

	/**
	 * 
	 * 把对象转成json数据
	 */
	public static String toJSON(Object o) {
//		JSONObject jsonObject = JSON.parseObject(o.toString());

		return  JSON.toJSONString(o);
	}

	/**
	 * 将 1，2，3，4 类型的对象转成list
	 * 
	 * @param o
	 * @return
	 */
	public static List<String> toList(Object o) {
		if (o != null) {
			List<String> list = new ArrayList<String>();
			String str = o.toString();
			String str2[] = str.split(",");
			for (int i = 0; i < str2.length; i++) {
				list.add(str2[i]);
			}
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 根据特殊要求筛选任务
	 * @param tasks 原始任务
	 * @param request 用户具有特殊的条件
	 * @return
	 */
	public static List<Map<String,Object>> taskFilterByRequest(List<Map<String,Object>> tasks,List<String> request){
		List<Map<String,Object>> result = new ArrayList<Map<String, Object>>();

		for (Map<String,Object> map : tasks) {
			int flag= 0 ;
			if (map.get("specialRequest")==null||map.get("specialRequest").toString()==""){
				result.add(map);
			}else {
				List<String> contidtions = (List<String>)map.get("specialRequest");
				for (String contidtion:contidtions) {
					if (request.contains(contidtion)){
						continue;
					}else {
						flag = 1;
						break;
					}
				}
				if (flag == 0){
					result.add(map);
				}
			}
		}

		return tasks;
	}

	/**
	 * zhangpan
	 * 申请任务专用
	 * @param text
	 * @return
	 */
	public static List<Map<String,Object>> stringToList(String text)throws Exception{
		if (text == null){
			throw new Exception();
		}
		text =text.replaceAll("=",":");
		text = text.substring(1,text.length()-1);
		List<Map<String,Object>> res = new ArrayList<Map<String, Object>>();
		text = text.replaceAll("\\{","");
		text = text.replaceAll("\\}","");
		text = text.replaceAll(" ","");
		text = text.replaceAll("\"","");
		List<String> list = Arrays.asList(text.split(","));
		for (String str:list) {
			String[] ms = str.split(":");
			if("taskConfigId".equals(ms[0])){
				Map<String,Object> m = new HashMap<String, Object>();
				m.put(ms[0], ms[1]);
				res.add(m);
			}
		}
		return res;
}
}
