package org.jallinone.system.importdata.server;

import java.io.*;
import java.util.*;


public class GenFiles {


  public GenFiles() {
		try {
			int lev = 1;
			int sublev = 1;
			PrintWriter pw = new PrintWriter(new FileOutputStream("D:\\Java\\Projects\\jAllInOne\\items.txt"));
			for(int i=0;i<100000;i++) {
				pw.println(
				  "ITEM"+getPad(""+i,'0',6)+"Item "+getPad(""+i,'0',6)+"    PZ   20   NEAN13     NNNNNLevel"+getPad(""+lev,'0',6)+"         |"+getPad(""+sublev,'0',6)+"              1     "
				);
				if (i%10000==0)
					lev++;
				if (i%1000==0)
					sublev++;
			}
			pw.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
  }


	private String getPad(String s,char pad,int n) {
		int len = s.length();
		for(int i=len;i<n;i++)
			s = pad+s;
		return s;
	}


  public static void main(String[] args) {
    GenFiles genFiles1 = new GenFiles();
  }

}
