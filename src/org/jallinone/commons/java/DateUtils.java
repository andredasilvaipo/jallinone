package org.jallinone.commons.java;

import java.util.*;
import java.sql.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Singleton class used to manage date and time.</p>
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
public class DateUtils {

	/**
	 * Remove date info from date+time object.
	 * @param time date+time object
	 * @return time object with date set to 0/0/0
	 */
	public static synchronized Timestamp removeDate(Timestamp time) {
		if (time==null)
			return time;
		Calendar today = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
//		cal.set(cal.YEAR,0);
//		cal.set(cal.MONTH,0);
//		cal.set(cal.DAY_OF_MONTH,0);
		cal.set(cal.YEAR,today.get(cal.YEAR));
		cal.set(cal.MONTH,today.get(cal.MONTH));
		cal.set(cal.DAY_OF_MONTH,today.get(cal.DAY_OF_MONTH));
		return new Timestamp(cal.getTimeInMillis());
	}


	/**
	 * Set date info to date+time object.
	 * @param time date+time object
	 * @return date+time object having the same time info and as date the current date
	 */
	public static synchronized Timestamp setTime(Timestamp time) {
		if (time==null)
			return time;
		Calendar today = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.set(cal.YEAR,today.get(cal.YEAR));
		cal.set(cal.MONTH,today.get(cal.MONTH));
		cal.set(cal.DAY_OF_MONTH,today.get(cal.DAY_OF_MONTH));
		return new Timestamp(cal.getTimeInMillis());
	}


}
