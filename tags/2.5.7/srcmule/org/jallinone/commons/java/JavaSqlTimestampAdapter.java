package org.jallinone.commons.java;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JavaSqlTimestampAdapter extends XmlAdapter<String,java.sql.Timestamp> {
    
	public java.sql.Timestamp unmarshal(String val) throws Exception {
        return new java.sql.Timestamp(Long.valueOf(val));
    }
    
    public String marshal(java.sql.Timestamp val) throws Exception {
        return String.valueOf(val.getTime());
    }
}