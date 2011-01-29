package org.jallinone.system.importdata.server;

public class FieldEntry {

    public String languageCode;
    public String fieldName;


    public FieldEntry(String languageCode,String fieldName) {
      this.languageCode = languageCode;
      this.fieldName = fieldName;
    }


    public boolean equals(Object o) {
      if (!(o instanceof FieldEntry))
        return false;
      FieldEntry oo = (FieldEntry)o;
      return
          languageCode.equals(oo.languageCode) &&
          fieldName.equals(oo.fieldName);
    }

    public int hashCode() {
      return
          languageCode.hashCode()+
          fieldName.hashCode();
    }

  }
