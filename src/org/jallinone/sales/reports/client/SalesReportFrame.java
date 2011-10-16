package org.jallinone.sales.reports.client;

import java.math.BigDecimal;
import java.text.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.pivottable.aggregators.java.*;
import org.openswing.swing.pivottable.client.*;
import org.openswing.swing.pivottable.functions.java.*;
import org.openswing.swing.pivottable.java.*;
import org.openswing.swing.util.client.*;
import org.jallinone.commons.client.*;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.reports.java.SalesPivotVO;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.jallinone.items.java.GridItemVO;
import org.jallinone.sales.customers.java.GridCustomerVO;
import org.openswing.swing.pivottable.java.InputFilter;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.items.java.ItemTypeVO;
import org.jallinone.commons.client.ImagePanel;
import java.awt.event.*;
import org.jallinone.system.java.ButtonCompanyAuthorizations;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Internal frame used to show sales graph.</p>
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
public class SalesReportFrame extends InternalFrame {


	JPanel buttonsPanel = new JPanel();
	FlowLayout flowLayout1 = new FlowLayout();
	FilterButton filterButton1 = new FilterButton();
	HashMap filters = new HashMap();
	JPanel northPanel = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	Form filterPanel = new Form();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	LabelControl labelDocType = new LabelControl();
	LabelControl labelYear = new LabelControl();
	LabelControl labelCompany = new LabelControl();
	CompaniesComboControl controlComp = new CompaniesComboControl();
	ComboBoxControl controlDocTypes = new ComboBoxControl();
  LabelControl labelMonth = new LabelControl();
  ComboBoxControl controlMonth = new ComboBoxControl();
  ComboBoxControl controlYear = new ComboBoxControl();
  ImagePanel sp = new ImagePanel();


	public SalesReportFrame() {
		try {
			jbInit();
			setSize(770,440);
			this.setTitle(ClientSettings.getInstance().getResources().getResource("sales graph"));
			init();


			Calendar cal = Calendar.getInstance();
			int year = cal.get(cal.YEAR);
			Domain d = new Domain("YEARS");
			for(int i=year-10;i<=year;i++)
				d.addDomainPair(new Integer(i),String.valueOf(i));
			controlYear.setDomain(d);
			controlYear.setValue(new Integer(year));

			d = new Domain("MONTHS");
			for(int i=0;i<=11;i++)
				d.addDomainPair(new Integer(i),String.valueOf(i+1));
			controlMonth.setDomain(d);

			MDIFrame.add(this);
			filterPanel.setMode(Consts.INSERT);

			controlMonth.setValue(null);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	private void init() {




	}


	private void jbInit() throws Exception {
		setAskBeforeClose(false);

		filterPanel.setFunctionId("SALES_GRAPH");
		filterPanel.setVOClassName("org.jallinone.sales.reports.java.SalesPivotVO");
		filterPanel.setFormController(new FormController());

		controlDocTypes.setAttributeName("docType");
		controlComp.setAttributeName("companyCode");
		controlDocTypes.setDomainId("SALE_DOC_TYPE");

		controlComp.setLinkLabel(labelCompany);
		controlComp.setFunctionCode("SALES_GRAPH");



		buttonsPanel.setLayout(flowLayout1);
		flowLayout1.setAlignment(FlowLayout.LEFT);
		northPanel.setLayout(gridBagLayout1);
		filterPanel.setLayout(gridBagLayout2);
		labelDocType.setLabel("docTypeDOC01");
		labelYear.setLabel("docYearDOC01");
		labelCompany.setLabel("companyCodeSYS01");
		labelMonth.setLabel("month");
		filterButton1.addActionListener(new SalesReportFrame_filterButton1_actionAdapter(this));
    buttonsPanel.add(filterButton1, null);
		this.getContentPane().add(northPanel, BorderLayout.NORTH);
		northPanel.add(filterPanel,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		filterPanel.add(labelCompany,            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		northPanel.add(buttonsPanel,  new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

		filterPanel.add(controlComp,             new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 100, 0));
		filterPanel.add(controlDocTypes,             new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelMonth,         new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlMonth,   new GridBagConstraints(3, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlYear,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelDocType,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelYear, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(sp, BorderLayout.CENTER);



	}

  void filterButton1_actionPerformed(ActionEvent e) {
		Object companyCode = controlComp.getValue();
		if (companyCode==null) {
			ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
			ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
			ArrayList companiesList = bca.getCompaniesList("SALES_GRAPH");
			if (companiesList.size()>0)
				companyCode = companiesList.get(0);
		}

		Response res = ClientUtils.getData("salesReport",new Object[]{
		  controlYear.getValue(),
			controlMonth.getValue(),
		  companyCode,
			controlDocTypes.getValue()
		});
		if (!res.isError()) {
			byte[] bytes = (byte[])((VOResponse)res).getVo();
			sp.setImage(bytes);
		}
  }




}

class SalesReportFrame_filterButton1_actionAdapter implements java.awt.event.ActionListener {
  SalesReportFrame adaptee;

  SalesReportFrame_filterButton1_actionAdapter(SalesReportFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.filterButton1_actionPerformed(e);
  }
}
