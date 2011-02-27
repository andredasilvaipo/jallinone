package org.jallinone.commons.java;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JavaSqlDateAdapter extends XmlAdapter<String,java.sql.Date> {
    
	public java.sql.Date unmarshal(String val) throws Exception {
        return new java.sql.Date(Long.valueOf(val));
    }
    
    public String marshal(java.sql.Date val) throws Exception {
        return String.valueOf(val.getTime());
    }
}