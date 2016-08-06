package com.mrsoftware.udb.json;

//package com.wellsfargo.docsys.util.JSON;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import com.wellsfargo.docsys.edp.AutoEntity.Entity;
//import com.wellsfargo.docsys.edp.AutoEntity.EntityCollection;
//
//public class JSONToCollectionParser extends JSONParser {
//	
//	@Override
//	public void addChildObject(String name, Object object) {
//		HashMap<String, Object> e = getCurrentContext();
//		
//		e.put(name, object);
//	}
//
//	@Override
//	public void addNamedValue(String name, JSONValue value) {
//
//		HashMap<String, Object> e = getCurrentContext();
//		
//		e.put(name,  value.getStringValue());
//	}
//
//	public void onName(String name)
//	{
//	}
//	
////	@Override
////	public void addChildObject(String name, Object object) {
////		
////		
////	}
//
//	@Override
//	public void addArrayElement(Object object) {
//	
//		ArrayList<Object> a = getCurrentContext();
//		
//		a.add(object);
//	}
//
//	@Override
//	public boolean isObject(Object o) {
//		return o instanceof Entity;
//	}
//
//	@Override
//	public boolean isArray(Object o) {
//
//		return o instanceof EntityCollection;
//	}
//
//	@Override
//	public Object onBeginObject(String name) {
//		
//		return new HashMap<String, Object>();
//	}
//
//
//	public Object onBeginArray(String name) {
//		
//		return new ArrayList<Object>(); 
//	}
//
////	static public void main(String[] args)
////	{
////		try 
////		{
////			JSONParser parser = new JSONToCollectionParser();
////		   
////			View a = View.getViewInstance("edp.ApplicationView");
////			
////			a.setFilter("app_obj_id > 280");
////			a.load();
////			
////			String s = a.toJSON();
////			
////			parser.parse(s);
////			
////			Object o = parser.getResult();
////			
////			int i = 5;
////			i = 34;
////		} 
////		catch (Exception e) {
////		    e.printStackTrace();
////		}
////		
////	}
//
//}
