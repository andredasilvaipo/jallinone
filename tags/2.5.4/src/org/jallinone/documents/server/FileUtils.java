package org.jallinone.documents.server;

import java.io.*;
import java.util.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Singleton class that provides a set of utility methods related to file system management.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of JAllInOne ERP/CRM application.
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class FileUtils {

	/**
	 * @param basedir base dir for all documents
	 * @param topic topic used to create distinct subfolders, one for each topic (ITM01/DOC14)
	 * @return absolute path where saving the specified file
	 */
	public static String getFilePath(String basedir,String topic) {
		Calendar cal = Calendar.getInstance();
		basedir = basedir.replace('\\','/');
		if (!basedir.endsWith("/"))
			basedir += "/";
		String relativePath = topic+"/"+cal.get(cal.YEAR)+"/"+cal.get(cal.MONTH)+"/"+cal.get(cal.DAY_OF_MONTH)+"/";
		return relativePath;
	}


	/**
	 * @param basedir base dir for all documents
	 * @param topic topic used to create distinct subfolders, one for each topic (DOC14)
	 * @return absolute path where reading the specified file
	 */
	public static String getFilePath(String basedir,String topic,java.util.Date fileDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fileDate);
		basedir = basedir.replace('\\','/');
		if (!basedir.endsWith("/"))
			basedir += "/";
		String relativePath = topic+"/"+cal.get(cal.YEAR)+"/"+cal.get(cal.MONTH)+"/"+cal.get(cal.DAY_OF_MONTH)+"/";
		return relativePath;
	}


}
