package org.jallinone.commons.java;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class JavaAwtColorAdapter extends XmlAdapter<String,java.awt.Color> {
    
	public java.awt.Color unmarshal(String val) throws Exception {
        return new java.awt.Color(Integer.valueOf(val));
    }
    
    public String marshal(java.awt.Color val) throws Exception {
        return String.valueOf(val.getRGB());
    }
}