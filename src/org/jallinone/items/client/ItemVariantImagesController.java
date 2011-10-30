package org.jallinone.items.client;

import org.openswing.swing.table.client.GridController;
import org.openswing.swing.message.receive.java.ValueObject;
import org.jallinone.items.java.ItemVariantImageVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.client.ComboBoxControl;
import java.lang.reflect.Method;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used to manage images related to combinations of variants.</p>
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
public class ItemVariantImagesController extends GridController {

	private ItemVariantImagesFrame frame = null;


	public ItemVariantImagesController(ItemVariantImagesFrame frame) {
		this.frame = frame;
	}


	/**
	 * Callback method invoked when the user has selected another row.
	 * @param rowNumber selected row index
	 */
	public void rowChanged(int rowNumber) {
		frame.getForm().reload();
	}


}
