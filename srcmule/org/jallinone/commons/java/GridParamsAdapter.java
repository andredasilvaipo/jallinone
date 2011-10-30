package org.jallinone.commons.java;



import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamResult;

import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.table.permissions.java.GridPermissions;


public class GridParamsAdapter extends XmlAdapter<String, GridParams> {

    @Override
    public GridParams unmarshal(String v) throws Exception {
    	v = v.substring(12,v.length()-13);
    	GridParams gp = new GridParams();
        String[] vv = v.split("\t");
        gp.setAction(Integer.parseInt(vv[0]));
        gp.setStartPos(Integer.parseInt(vv[1]));
        
//        String[] sortedCols = vv[2].split(",");
//        String[] sortedVersus = vv[3].split(",");
//        
//        for(int i=0;i<sortedCols.length;i++) {
//        	gp.getCurrentSortedColumns().add(sortedCols[i]);
//        	gp.getCurrentSortedVersusColumns().add(sortedVersus[i]);
//        }
        return gp;
    }


    @Override
    public String marshal(GridParams v) throws Exception {
    	String map = "<GridParams>";
    	map += v.getAction()+"\t";
    	map += v.getStartPos()+"\t";
//    	map += v.getCurrentSortedColumns()+"\t";
//    	map += v.getCurrentSortedVersusColumns()+"\t";
        return map+"</GridParams>";
    }
}