import java.io.*;
import java.util.*;


public class Util {
  public Util() {
    try {
      PrintWriter pw = new PrintWriter(new FileOutputStream("F:\\Java\\Projects\\jAllInOne\\db2.xml"));
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("F:\\Java\\Projects\\jAllInOne\\db.xml")));

      String line = null;
      String a = null;
      String b = null;
      int i = 0;
      HashSet set = new HashSet();
      while((line=br.readLine())!=null) {
        //<INDEX ID="1625" IndexName="SYS02_COMPANIES_ACCESS_FKIndex1" IndexKind="1" FKRefDef_Obj_id="1295">
        i = line.indexOf("IndexName=\"");
        if (line.indexOf("<INDEX ID=\"")!=-1 && i!=-1) {
          a = line.substring(i+11,line.indexOf("\"",i+12));
          if (a.length()>13 &&
              a.charAt(5)=='_' &&
              a.indexOf("_FKIndex")!=-1) {
            b = a.substring(0,5)+a.substring(a.lastIndexOf("_FKIndex"));
            if (!set.contains(b)) {
              line = line.substring(0,i+11)+b+line.substring(line.indexOf("\"",i+12));
              set.add(b);
            }
            else {
              b = a.substring(0, 9) + a.substring(a.lastIndexOf("_FKIndex"));
              if (!set.contains(b)) {
                line = line.substring(0, i + 11) + b + line.substring(line.indexOf("\"", i + 12));
                set.add(b);
              }
              else
                throw new Exception(line);
            }
          }
        }
        pw.println(line);
      }

      pw.close();
      br.close();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  public static void main(String[] args) {
    Util util1 = new Util();
  }

}
