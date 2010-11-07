package org.jallinone.system.importdata.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;

import java.math.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;
import org.jallinone.system.importdata.java.*;
import org.jallinone.system.importdata.java.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.importdata.java.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to read a file from the file system.</p>
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
public class GetFileContentBean  implements GetFileContent {


	private DataSource dataSource; 

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/** external connection */
  private Connection conn = null;

	/**
   * Set external connection. 
   */
  public void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
   * Create local connection
   */
  public Connection getConn() throws Exception {
		Connection c = dataSource.getConnection(); c.setAutoCommit(false); return c;
	}


	public GetFileContentBean() {}



	/**
	 * Business logic to execute.
	 */
	public byte[] getFileContent(final String fileFormat,File file,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			if (!file.exists())
				throw new Exception("File not exists");

			BufferedInputStream in = null;
			byte[] bytes = new byte[0];
			try {
				in = new BufferedInputStream(new FileInputStream(file));
				byte[] aux = null;
				byte[] bb = new byte[10000];
				int len = 0;
				while((len=in.read(bb))>0) {
					aux = bytes;
					bytes = new byte[aux.length+len];
					System.arraycopy(aux,0,bytes,0,aux.length);
					System.arraycopy(bb,0,bytes,aux.length,len);
				}
				in.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			finally {
				try {
					in.close();
				}
				catch (Exception ex1) {
				}
			}
			return bytes;

		}
		catch (Throwable ex) {
			Logger.error(username, this.getClass().getName(), "executeCommand", "Error while reading a folder", ex);
			throw new Exception(ex.getMessage()); 
		}
		finally {
	        try {
	            if (this.conn==null && conn!=null) {
	                // close only local connection
	                conn.commit();
	                conn.close();
	            }

	        }
	        catch (Exception exx) {}			
		}
	}



}


