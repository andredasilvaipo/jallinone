package org.jallinone.commons.java;

import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamResult;

import org.openswing.swing.table.permissions.java.GridPermissions;


public class HashMapAdapter extends XmlAdapter<String, HashMap> {

    @Override
    public HashMap unmarshal(String v) throws Exception {
    	v = v.substring(5,v.length()-6);
    	HashMap myMap = new HashMap();
        String[] vv = v.split("\t");
        Object key = null;
        Object value = null;
        for(int i=0;i<vv.length;i=i+4) {
        	key = vv[i+1];
        	if (!vv[i].equals("java.lang.String"))
        	  key = Class.forName(vv[i]).getConstructor(new Class[]{String.class}).newInstance(new Object[]{vv[i+1]});
        	value = vv[i+3];
        	if (!vv[i+2].equals("java.lang.String"))
          	  value = Class.forName(vv[i+2]).getConstructor(new Class[]{String.class}).newInstance(new Object[]{vv[i+3]});
        	myMap.put(key, value);
        }
        return myMap;
    }


    @Override
    public String marshal(HashMap v) throws Exception {
    	String map = "<map>";
    	Iterator it = v.keySet().iterator();
    	Object key = null;
    	Object value = null;
    	while(it.hasNext()) {
    		key = it.next();
    		value = v.get(key);
//    		if (value instanceof GridPermissions) {
//    			GridPermissions gp = (GridPermissions)value;
//    			StreamResult sr = new StreamResult();
//    			JAXBContext.newInstance(GridPermissions.class).createMarshaller().marshal(gp,sr);
//    			value = sr.toString();
//    		}
    		
    		map += key.getClass().getName()+"\t"+key+"\t";
    		map += value.getClass().getName()+"\t"+value+"\t";
    	}
    	if (v.size()>0)
    		map = map.substring(0,map.length()-1);
        return map+"</map>";
    }
}