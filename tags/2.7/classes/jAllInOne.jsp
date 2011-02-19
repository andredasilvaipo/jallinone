<html>
<head>
<title>AppServlet</title>
<SCRIPT LANGUAGE="JavaScript">

  function closeFrame() {
    window.close();
  }

</SCRIPT>
</head>
<body>
<OBJECT classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93"
    width="500" height="400" align="baseline"
    codebase="http://java.sun.com/products/plugin/autodl/jinstall-1_4_2-windows-i586.cab#Version=1,4,2">
    <PARAM NAME="code" VALUE="org.jallinone.commons.client.ClientApplet">
    <PARAM NAME=archive VALUE="clientjaio.jar,clientos.jar,commonos.jar,jcalendar.jar,jnlp.jar,looks-2.0.1.jar,OfficeLnFs_2.7.jar,jasperreports-1.2.7.jar,itext-1.3.1.jar,hessian-3.1.5.jar">
    <PARAM NAME="codebase" VALUE=".">
    <PARAM NAME="type" VALUE="application/x-java-applet;jpi-version=1.4.2">
    <PARAM NAME=MAYSCRIPT value="true">
    <PARAM NAME="scriptable" VALUE="true">
    <param name="SERVERURL" value=
        "<%=request.getScheme()%>://<%= request.getServerName()+
        ":"+request.getServerPort()+
        request.getContextPath()  %>/controller" >

<comment>
<EMBED type="application/x-java-applet;jpi-version=1.4.2" width="500"
   height="400" align="baseline" code="org.jallinone.commons.client.ClientApplet"
   codebase="."
   pluginspage="http://java.sun.com/products/plugin/autodl/jinstall-1_4_2-windows-i586.cab#Version=1,4,2"
   archive="clientjaio.jar,clientos.jar,commonos.jar,jcalendar.jar,jnlp.jar,substance.jar,jasperreports-1.2.7.jar,itext-1.3.1.jar,hessian-3.1.5.jar" MAYSCRIPT="true"
   SERVERURL=
        "<%=request.getScheme()%>://<%= request.getServerName()+
        ":"+request.getServerPort()+
        request.getContextPath()  %>/controller" 
>
<NOEMBED>
</comment>
<br>
<CENTER>No Java 2 SDK, Standard Edition v 1.4 or next has been installed!</CENTER>
</NOEMBED>
</EMBED>

</OBJECT>
</body></html>
