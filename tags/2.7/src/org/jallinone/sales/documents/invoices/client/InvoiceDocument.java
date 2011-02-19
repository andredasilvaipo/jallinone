package org.jallinone.sales.documents.invoices.client;

import org.openswing.swing.client.GenericButtonController;
import org.openswing.swing.form.client.Form;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.openswing.swing.mdi.client.InternalFrame;
import org.jallinone.sales.documents.client.SaleDocument;
import org.openswing.swing.client.GridControl;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Interface implemented by all sale invoices.</p>
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
public interface InvoiceDocument extends SaleDocument {


  public GridControl getInvoices();


  /**
   * Create a link between this window and the parent window.
   * @param parent parent window
   */
  public void setParentFrame(InternalFrame parentFrame);


  /**
   * Add a child window to this.
   * @param frame child window to add
   */
  public void pushFrame(InternalFrame frame);


  public String getFunctionId();


  public void enabledConfirmButton();

}
