package org.jallinone.ordertracking.client;



import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.client.GridController;
import java.math.BigDecimal;
import javax.swing.border.*;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.client.CustomizedControls;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.client.CustomizedColumns;
import org.jallinone.hierarchies.client.*;
import org.jallinone.commons.client.CompaniesComboColumn;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.Response;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.domains.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.documents.java.GridSaleDocVO;
import org.jallinone.commons.client.CompaniesComboControl;
import org.openswing.swing.form.client.FormController;
import org.jallinone.ordertracking.java.OrderTrackingFilterVO;
import java.util.Calendar;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import java.awt.event.*;
import org.openswing.swing.table.model.client.VOListTableModel;
import org.jallinone.ordertracking.java.DocumentTrackingVO;
import org.openswing.swing.internationalization.java.Resources;
import org.openswing.swing.message.receive.java.ValueObject;
import org.jallinone.purchases.documents.invoices.client.*;
import org.jallinone.purchases.documents.java.PurchaseDocPK;
import org.jallinone.purchases.documents.client.PurchaseDocController;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.invoices.client.*;
import org.jallinone.sales.documents.client.*;
import org.jallinone.warehouse.documents.client.*;
import org.jallinone.warehouse.documents.java.DeliveryNotePK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame that shows the orders tracking.</p>
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
public class OrderTrackingGridPanel extends JPanel {

  GridControl grid = new GridControl();
  TextColumn colCompany = new TextColumn();
  TextColumn colCode = new TextColumn();
  TextColumn colName1 = new TextColumn();
  TextColumn colType = new TextColumn();
  IntegerColumn colYear = new IntegerColumn();
  IntegerColumn colSeq = new IntegerColumn();
  TextColumn colName2 = new TextColumn();
  ComboColumn colState = new ComboColumn();
  DateColumn colDate = new DateColumn();
  ServerGridDataLocator locator = new ServerGridDataLocator();


  public OrderTrackingGridPanel(Color color) {
    try {
      jbInit();
      init(color);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void init(final Color color) {
    grid.setExpandableColumn(0);
    grid.setOverwriteRowWhenExpanding(false);
    grid.setSingleExpandableRow(true);
    grid.setExpandableRowController(new ExpandableRowController() {

      /**
       * @param model grid model
       * @param rowNum the current row number
       * @return <code>true</code> if the current row must be expanded, <code>false</code> otherwise
       */
      public boolean isRowExpandable(VOListTableModel model,int rowNum) {
        DocumentTrackingVO vo = (DocumentTrackingVO)model.getObjectForRow(rowNum);
        String docTypeDescr = vo.getDocTypeDescr();
        Resources res = ClientSettings.getInstance().getResources();

        if (docTypeDescr.equals(res.getResource("purchase invoice")) ||
            docTypeDescr.equals(res.getResource("purchase invoice from delivery notes")) ||
            docTypeDescr.equals(res.getResource("purchase invoice from purchase document")) ||
            docTypeDescr.equals(res.getResource("debiting note")) ||
            docTypeDescr.equals(res.getResource("purchase generic document")))
          return false;
        if (docTypeDescr.equals(res.getResource("purchase order")))
          return true;
        else if (docTypeDescr.equals(res.getResource("sale invoice")) ||
                 docTypeDescr.equals(res.getResource("sale invoice from delivery notes")) ||
                 docTypeDescr.equals(res.getResource("sale invoice from sale document")) ||
                 docTypeDescr.equals(res.getResource("credit note")) ||
                 docTypeDescr.equals(res.getResource("sale generic document")))
          return false;
        if (docTypeDescr.equals(res.getResource("sale order")) ||
            docTypeDescr.equals(res.getResource("sale contract")) ||
            docTypeDescr.equals(res.getResource("desk selling")) ||
            docTypeDescr.equals(res.getResource("sale estimate")) ||
            docTypeDescr.equals(res.getResource("delivery request")))
          return true;
        return false;
      }


      /**
       * @param model grid model
       * @param rowNum the current row number
       * @return JComponent to show when expanding row; null to do not show anything
       */
      public JComponent getComponentToShow(VOListTableModel model,int rowNum) {
        DocumentTrackingVO vo = (DocumentTrackingVO)model.getObjectForRow(rowNum); // this is the v.o. associared to grid
        OrderTrackingGridPanel p = new OrderTrackingGridPanel(
          new Color(
            color.getRed()+15,
            color.getGreen()+15,
            color.getBlue()+15
          )
        );
        p.getGrid().getOtherGridParams().put(ApplicationConsts.PROPERTIES_FILTER,vo);
        p.getGrid().setAutoLoadData(true);
        p.setPreferredSize(new Dimension(OrderTrackingGridPanel.this.getWidth()-50,250));
        return p;
      }

    });

    grid.setController(new GridController() {

      /**
       * Method used to define the background color for each cell of the grid.
       * @param rowNumber selected row index
       * @param attributeName attribute name related to the column currently selected
       * @param value object contained in the selected cell
       * @return background color of the selected cell
       */
      public Color getBackgroundColor(int row,String attributeName,Object value) {
        return color;
      }


      /**
       * Callback method invoked when the user has double clicked on the selected row of the grid.
       * @param rowNumber selected row index
       * @param persistentObject v.o. related to the selected row
       */
      public void doubleClick(int rowNumber,ValueObject persistentObject) {
        DocumentTrackingVO vo = (DocumentTrackingVO)persistentObject;
        String docTypeDescr = vo.getDocTypeDescr();
        Resources res = ClientSettings.getInstance().getResources();

        if (docTypeDescr.equals(res.getResource("debiting note"))) {
          new PurchaseDebitingDocController(
            null,
            new PurchaseDocPK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
        else if (docTypeDescr.equals(res.getResource("purchase invoice")) ||
                 docTypeDescr.equals(res.getResource("purchase generic document"))) {
           new PurchaseInvoiceDocController(
             null,
             new PurchaseDocPK(
               vo.getCompanyCodeSYS01(),
               vo.getDocType(),
               vo.getDocYear(),
               vo.getDocNumber()
             )
           );
        }
        else if (docTypeDescr.equals(res.getResource("purchase invoice from delivery notes"))) {
           new PurchaseInvoiceDocFromDelivNotesController(
             null,
             new PurchaseDocPK(
               vo.getCompanyCodeSYS01(),
               vo.getDocType(),
               vo.getDocYear(),
               vo.getDocNumber()
             )
           );
        }
        else if (docTypeDescr.equals(res.getResource("purchase invoice from purchase document"))) {
           new PurchaseInvoiceDocFromPurchaseDocController(
             null,
             new PurchaseDocPK(
               vo.getCompanyCodeSYS01(),
               vo.getDocType(),
               vo.getDocYear(),
               vo.getDocNumber()
             )
           );
        }
        else if (docTypeDescr.equals(res.getResource("purchase order"))) {
          new PurchaseDocController(
            null,
            new PurchaseDocPK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
        else if (docTypeDescr.equals(res.getResource("sale invoice")) ||
                 docTypeDescr.equals(res.getResource("sale generic document"))) {
          new SaleInvoiceDocController(
            null,
            new SaleDocPK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
        else if (docTypeDescr.equals(res.getResource("sale invoice from delivery notes"))) {
          new SaleInvoiceDocFromDelivNotesController(
            null,
            new SaleDocPK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
        else if (docTypeDescr.equals(res.getResource("sale invoice from sale document"))) {
          new SaleInvoiceDocFromSaleDocController(
            null,
            new SaleDocPK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
        else if (docTypeDescr.equals(res.getResource("credit note"))) {
          new SaleCreditDocController(
            null,
            new SaleDocPK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
        else if (docTypeDescr.equals(res.getResource("sale order"))) {
          new SaleOrderDocController(
            null,
            new SaleDocPK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
        else if (docTypeDescr.equals(res.getResource("sale contract"))) {
          new SaleContractDocController(
            null,
            new SaleDocPK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
        else if (docTypeDescr.equals(res.getResource("desk selling"))) {
          new SaleDeskDocController(
            null,
            new SaleDocPK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
        else if (docTypeDescr.equals(res.getResource("sale estimate"))) {
          new SaleEstimateDocController(
            null,
            new SaleDocPK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
        else if (docTypeDescr.equals(res.getResource("in delivery note"))) {
          new InDeliveryNoteController(
            null,
            new DeliveryNotePK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
        else if (docTypeDescr.equals(res.getResource("out delivery note"))) {
          new OutDeliveryNoteController(
            null,
            new DeliveryNotePK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
        else if (docTypeDescr.equals(res.getResource("delivery request"))) {
          new DeliveryRequestController(
            null,
            new SaleDocPK(
              vo.getCompanyCodeSYS01(),
              vo.getDocType(),
              vo.getDocYear(),
              vo.getDocNumber()
            )
          );
        }
      }

    });

  }


  public final GridControl getGrid() {
    return grid;
  }


  /**
   * Define input controls editable settings according to the document state.
   */
  private void jbInit() {
    colState.setDomainId("DOC06_STATES");
    locator.setServerMethodName("loadOrderTracking");
    grid.setGridDataLocator(locator);
    grid.setAutoLoadData(false);
    grid.setValueObjectClassName("org.jallinone.ordertracking.java.DocumentTrackingVO");
    colCompany.setColumnName("companyCodeSYS01");
    colType.setColumnName("docTypeDescr");
    colType.setPreferredWidth(110);
    this.setLayout(new BorderLayout());
    colYear.setColumnName("docYear");
    colYear.setPreferredWidth(50);
    colSeq.setColumnName("docSequence");
    colSeq.setPreferredWidth(65);
    colDate.setColumnName("docDate");
    colDate.setPreferredWidth(80);
    colCode.setColumnName("customerSupplierCode");
    colCode.setPreferredWidth(80);
    colName1.setColumnName("name_1");
    colName1.setHeaderColumnName("name_1REG04");
    colName1.setPreferredWidth(150);
    colName2.setColumnName("name_2");
    colName2.setHeaderColumnName("name_2REG04");
    colName2.setPreferredWidth(120);
    colState.setColumnName("docState");
    colState.setPreferredWidth(80);
    this.add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colType, null);
    grid.getColumnContainer().add(colYear, null);
    grid.getColumnContainer().add(colSeq, null);
    grid.getColumnContainer().add(colDate, null);
    grid.getColumnContainer().add(colCode, null);
    grid.getColumnContainer().add(colName1, null);
    //grid.getColumnContainer().add(colName2, null);
    grid.getColumnContainer().add(colState, null);
  }



}

