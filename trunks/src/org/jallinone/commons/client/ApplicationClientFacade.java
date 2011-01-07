package org.jallinone.commons.client;

import java.math.BigDecimal;

import javax.swing.JOptionPane;

import org.jallinone.accounting.accountingmotives.client.AccountingMotivesController;
import org.jallinone.accounting.accounts.client.AccountsController;
import org.jallinone.accounting.ledger.client.LedgerController;
import org.jallinone.accounting.movements.client.AccountingItemNoVatController;
import org.jallinone.accounting.movements.client.AccountingItemVatController;
import org.jallinone.accounting.movements.client.AccountsFilterFrame;
import org.jallinone.accounting.movements.client.BudgetFilterFrame;
import org.jallinone.accounting.movements.client.CloseAccountsFrame;
import org.jallinone.accounting.movements.client.CostsPreceedingsFilterFrame;
import org.jallinone.accounting.movements.client.DebitCreditFilterFrame;
import org.jallinone.accounting.movements.client.JournalFilterFrame;
import org.jallinone.accounting.movements.client.OpenAccountsFrame;
import org.jallinone.accounting.movements.client.StatementOfAccountFilterFrame;
import org.jallinone.accounting.vatregisters.client.VatEndorseFilterFrame;
import org.jallinone.accounting.vatregisters.client.VatRegistersController;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.contacts.client.ContactsController;
import org.jallinone.documents.client.DocumentTypesController;
import org.jallinone.documents.client.DocumentsController;
import org.jallinone.employees.client.EmployeesController;
import org.jallinone.expirations.client.ExpirationsController;
import org.jallinone.items.client.ItemTypesController;
import org.jallinone.items.client.ItemsController;
import org.jallinone.items.client.PrintBarcodeLabelsFrame;
import org.jallinone.ordertracking.client.OrderTrackingFrame;
import org.jallinone.production.machineries.client.MachineriesController;
import org.jallinone.production.manufactures.client.ManufacturesController;
import org.jallinone.production.manufactures.client.OperationsController;
import org.jallinone.production.orders.client.ProdOrdersController;
import org.jallinone.purchases.documents.client.PurchaseDocsController;
import org.jallinone.purchases.documents.client.ReorderFromMinStocksFrame;
import org.jallinone.purchases.documents.invoices.client.PurchaseDebitingDocsController;
import org.jallinone.purchases.documents.invoices.client.PurchaseInvoiceDocController;
import org.jallinone.purchases.documents.invoices.client.PurchaseInvoiceDocFromDelivNotesController;
import org.jallinone.purchases.documents.invoices.client.PurchaseInvoiceDocFromPurchaseDocController;
import org.jallinone.purchases.documents.invoices.client.PurchaseInvoiceDocsController;
import org.jallinone.purchases.items.client.ABCFrame;
import org.jallinone.purchases.suppliers.client.SuppliersController;
import org.jallinone.registers.bank.client.BanksController;
import org.jallinone.registers.carrier.client.CarriersController;
import org.jallinone.registers.currency.client.CurrenciesController;
import org.jallinone.registers.measure.client.MeasuresController;
import org.jallinone.registers.payments.client.PaymentTypesController;
import org.jallinone.registers.payments.client.PaymentsController;
import org.jallinone.registers.task.client.TasksController;
import org.jallinone.registers.transportmotives.client.TransportMotivesController;
import org.jallinone.registers.vat.client.VatsController;
import org.jallinone.sales.activities.client.SaleActivitiesController;
import org.jallinone.sales.agents.client.AgentTypesController;
import org.jallinone.sales.agents.client.AgentsController;
import org.jallinone.sales.charges.client.ChargesController;
import org.jallinone.sales.customers.client.CustomersController;
import org.jallinone.sales.discounts.client.HierarCustomerDiscountsController;
import org.jallinone.sales.discounts.client.HierarItemDiscountsController;
import org.jallinone.sales.documents.client.DeliveryRequestsController;
import org.jallinone.sales.documents.client.SaleContractDocsController;
import org.jallinone.sales.documents.client.SaleDeskDocsController;
import org.jallinone.sales.documents.client.SaleEstimateDocsController;
import org.jallinone.sales.documents.client.SaleOrderDocsController;
import org.jallinone.sales.documents.invoices.client.SaleCreditDocsController;
import org.jallinone.sales.documents.invoices.client.SaleInvoiceDocController;
import org.jallinone.sales.documents.invoices.client.SaleInvoiceDocFromDelivNotesController;
import org.jallinone.sales.documents.invoices.client.SaleInvoiceDocFromSaleDocController;
import org.jallinone.sales.documents.invoices.client.SaleInvoiceDocsController;
import org.jallinone.sales.pos.client.SaleDeskCompanySelDialog;
import org.jallinone.sales.pricelist.client.PricelistController;
import org.jallinone.sales.reports.client.SalesPivotFrame;
import org.jallinone.scheduler.activities.client.ScheduledActivitiesController;
import org.jallinone.scheduler.appointments.client.EmployeeAppointmentsFrame;
import org.jallinone.scheduler.callouts.client.CallOutRequestController;
import org.jallinone.scheduler.callouts.client.CallOutRequestsController;
import org.jallinone.scheduler.callouts.client.CallOutTypesController;
import org.jallinone.scheduler.callouts.client.CallOutsController;
import org.jallinone.scheduler.gantt.client.EmployeeGanttController;
import org.jallinone.sqltool.client.SqlToolFrame;
import org.jallinone.subjects.client.SubjectHierarchiesController;
import org.jallinone.system.client.ApplicationParametersController;
import org.jallinone.system.client.CompanyParametersController;
import org.jallinone.system.client.UserParametersController;
import org.jallinone.system.client.UsersListFrame;
import org.jallinone.system.companies.client.CompaniesController;
import org.jallinone.system.customizations.client.CustomFunctionsController;
import org.jallinone.system.customizations.client.ReportsController;
import org.jallinone.system.customizations.client.ShowCustomFunctionFrame;
import org.jallinone.system.customizations.client.WindowsController;
import org.jallinone.system.importdata.client.ETLProcessesGridController;
import org.jallinone.system.languages.client.LanguagesController;
import org.jallinone.system.permissions.client.FunctionController;
import org.jallinone.system.permissions.client.RolesController;
import org.jallinone.system.permissions.client.UsersController;
import org.jallinone.system.translations.client.TranslationsGridFrame;
import org.jallinone.variants.client.VariantTypesController;
import org.jallinone.variants.client.VariantsController;
import org.jallinone.warehouse.availability.client.ItemAvailabilityFrame;
import org.jallinone.warehouse.client.InventoryFrame;
import org.jallinone.warehouse.client.WarehousesController;
import org.jallinone.warehouse.documents.client.InDeliveryNotesController;
import org.jallinone.warehouse.documents.client.OutDeliveryNotesController;
import org.jallinone.warehouse.movements.client.ManualMovementController;
import org.jallinone.warehouse.tables.motives.client.MotivesController;
import org.jallinone.warehouse.tables.movements.client.MovementsController;
import org.openswing.swing.mdi.client.ClientFacade;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.warehouse.client.InventoryListFrame;
import org.jallinone.sales.reports.client.SalesReportFrame;
import org.jallinone.items.spareparts.client.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Application client facade: contains all application functions,
 * callable by the tree/menubar menu.</p>
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
public class ApplicationClientFacade implements ClientFacade {

  /** main class */
  private ClientApplet mainClass = null;


  /**
   * Costructor called by ClientApplet class.
   * @param mainClass main class
   */
  public ApplicationClientFacade(ClientApplet mainClass) {
    this.mainClass = mainClass;
  }


  /**
   * Customizations tree+grid frame.
   */
  public void getWindowsList() {
    new WindowsController();
  }


  /**
   * Warehouse grid frame.
   */
  public void getWarehousesList() {
    new WarehousesController(mainClass);
  }


  /**
   * @return main class
   */
  public ClientApplet getMainClass() {
    return mainClass;
  }


  /**
   * Companies grid frame.
   */
  public void getCompaniesList() {
    new CompaniesController();
  }


  /**
   * Languages grid frame.
   */
  public void getLanguagesList() {
    new LanguagesController();
  }


  /**
   * Roles frame.
   */
  public void getRolesList() {
    new RolesController();
  }


  /**
   * Users frame.
   */
  public void getUsersList() {
    new UsersController();
  }


  /**
   * Vats grid frame.
   */
  public void getVatsList() {
    new VatsController();
  }


  /**
   * Banks frame.
   */
  public void getBanksList() {
    new BanksController();
  }


  /**
   * Tasks frame.
   */
  public void getTasksList() {
    new TasksController();
  }


  /**
   * Measure Units grid frame.
   */
  public void getMeasureUnitsList() {
    new MeasuresController();
  }


  /**
   * Currencies grid frame.
   */
  public void getCurrenciesList() {
    new CurrenciesController();
  }


  /**
   * Carriers grid frame.
   */
  public void getCarriersList() {
    new CarriersController();
  }


 /**
  * Transport motives grid frame.
  */
 public void getTransportMotivesList() {
   new TransportMotivesController();
 }


  /**
   * Item types frame.
   */
  public void getItemTypesList() {
    new ItemTypesController();
  }


  /**
   * Item tree+grid frame.
   */
  public void getItemsList() {
    new ItemsController(false);
  }


  /**
   * Customers grid frame.
   */
  public void getCustomersList() {
    new CustomersController();
  }


  /**
   * Payment types grid frame.
   */
  public void getPaymentTypesList() {
    new PaymentTypesController();
  }


  /**
   * Payments grid frame.
   */
  public void getPaymentsList() {
    new PaymentsController();
  }


  /**
   * Customer hierachies list.
   */
  public void getCustomerHiearchiesList() {
    new SubjectHierarchiesController(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER,"REG08_CUSTOMERS","customer hierarchies",ApplicationConsts.ID_CUSTOMER_GRID,false);
  }


  /**
   * Contact hierachies list.
   */
  public void getContactHiearchiesList() {
    new SubjectHierarchiesController(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT,"REG08_CONTACTS","contacts hierarchies",ApplicationConsts.ID_CUSTOMER_GRID,false);
  }


  /**
   * Hierarchy item discounts tree + grid frame.
   */
  public void getSalesItemsHierarchyDiscountsList() {
    new HierarItemDiscountsController();
  }


  /**
   * Hierarchy customer discounts tree + grid frame.
   */
  public void getCustomersHierarchyDiscountsList() {
    new HierarCustomerDiscountsController();
  }


  /**
   * Charges grid frame.
   */
  public void getSaleChargesList() {
    new ChargesController();
  }


  /**
   * Pricelist + item prices frame.
   */
  public void getSalePricesList() {
    new PricelistController();
  }


  /**
   * Agent types grid frame.
   */
  public void getAgentTypesList() {
    new AgentTypesController();
  }


  /**
   * Agents grid frame.
   */
  public void getAgentsList() {
    new AgentsController();
  }



  /**
   * Employees grid frame.
   */
  public void getEmployeesList() {
    new EmployeesController();
  }


  /**
   * Suppliers grid frame.
   */
  public void getSuppliersList() {
    new SuppliersController();
  }


  /**
   * Supplier hierachies list.
   */
  public void getSupplierHiearchiesList() {
    new SubjectHierarchiesController(ApplicationConsts.SUBJECT_SUPPLIER,"REG08_SUPPLIERS","supplier hierarchies",ApplicationConsts.ID_SUPPLIER_GRID,false);
  }

  /**
   * Purchase orders grid frame.
   */
  public void getPurchaseOrdersList() {
    new PurchaseDocsController();
  }


  /**
   * In delivery notes grid frame.
   */
  public void getInDeliveryNotesList() {
    new InDeliveryNotesController();
  }


  /**
   * Get item availability, per warehouse.
   */
  public void getItemsAvailability() {
    new ItemAvailabilityFrame();
  }


  /**
   * Get warehouse motives.
   */
  public void getWarehouseMotivesList() {
    new MotivesController();
  }


  /**
   * Get warehouse movements list.
   */
  public void getWarehouseMovementsList() {
    new MovementsController();
  }


  /**
   * Create a new warehouse movement.
   */
  public void warehouseMovement() {
    new ManualMovementController();
  }


  /**
   * Sale orders grid frame.
   */
  public void getSaleOrdersList() {
    new SaleOrderDocsController();
  }


  /**
   * Sale activities grid frame.
   */
  public void getSalesActivitiesList() {
    new SaleActivitiesController();
  }


  /**
   * Out delivery notes grid frame.
   */
  public void getOutDeliveryNotesList() {
    new OutDeliveryNotesController();
  }


  /**
   * Sale contracts grid frame.
   */
  public void getSaleContractsList() {
    new SaleContractDocsController();
  }


  /**
   * Retail selling selling grid frame.
   */
  public void getDeskSalesList() {
    new SaleDeskDocsController();
  }


  /**
   * Sale estimate grid frame.
   */
  public void getSaleEstimatesList() {
    new SaleEstimateDocsController();
  }

  /**
   * User parameters detail frame.
   */
  public void getUserParsList() {
    new UserParametersController();
  }


  /**
   * Create a sale invoice from delivery notes.
   */
  public void createSaleInvoiceFromDN() {
    new SaleInvoiceDocFromDelivNotesController(null,null);
  }


  /**
   * Create a sale invoice from a sale document (contract or order)
   */
  public void createSaleInvoiceFromSD() {
    new SaleInvoiceDocFromSaleDocController(null,null);
  }


  /**
   * Manually create a sale invoice.
   */
  public void createSaleInvoice() {
    new SaleInvoiceDocController(null,null);
  }


  /**
   * Get all sale invoices.
   */
  public void getSalesInvoicesList() {
    new SaleInvoiceDocsController();
  }


  /**
   * Get all expirations.
   */
  public void getExpirationsList() {
    new ExpirationsController();
  }


  /**
   * Get contacts list.
   */
  public void getContactsList() {
    new ContactsController();
  }







  /**
   * Create a purchase invoice from delivery notes.
   */
  public void createPurchaseInvoiceFromDN() {
    new PurchaseInvoiceDocFromDelivNotesController(null,null);
  }


  /**
   * Create a purchase invoice from a purchase document (order)
   */
  public void createPurchaseInvoiceFromPD() {
    new PurchaseInvoiceDocFromPurchaseDocController(null,null);
  }


  /**
   * Manually create a purchase invoice.
   */
  public void createPurchaseInvoice() {
    new PurchaseInvoiceDocController(null,null);
  }


  /**
   * Get all purchase invoices.
   */
  public void getPurchaseInvoicesList() {
    new PurchaseInvoiceDocsController();
  }



  /**
   * Print warehouse inventory.
   */
  public void getWarehouseInventory() {
    new InventoryFrame();
  }


  /**
   * Get accounting ledger.
   */
  public void getLedger() {
    new LedgerController();
  }


  /**
   * Get accounts.
   */
  public void getAccounts() {
    new AccountsController();
  }


  /**
   * Get accounting motives.
   */
  public void getAccountMotives() {
    new AccountingMotivesController();
  }


  /**
   * Get vat registers.
   */
  public void getVatRegisters() {
    new VatRegistersController();
  }


  /**
   * View accounting journal.
   */
  public void getJournal() {
    new JournalFilterFrame();
  }


  /**
   * View costs and preceedings report.
   */
  public void getCostsPreceedings() {
    new CostsPreceedingsFilterFrame();
  }


  /**
   * View debit and credit report.
   */
  public void getDebitCredit() {
    new DebitCreditFilterFrame();
  }


  /**
   * View accounting budget.
   */
  public void getBudget() {
    new BudgetFilterFrame();
  }


  /**
   * View statement of account for a specified customer.
   */
  public void getCustomerAccount() {
    new StatementOfAccountFilterFrame();
  }


  /**
   * View a specified account report.
   */
  public void getAccountReport() {
    new AccountsFilterFrame();
  }




  /**
   * Open all accounts.
   */
  public void openAccounts() {
    new OpenAccountsFrame();
  }


  /**
   * Close all account for the specified year.
   */
  public void closeAccounts() {
    new CloseAccountsFrame();
  }


  /**
   * Create new accounting item that does not use vat registers/vat accounts.
   */
  public void newAccountingItemNoVat() {
    new AccountingItemNoVatController();
  }


  /**
   * Create new accounting item that uses vat registers/vat accounts.
   */
  public void newAccountingItemWithVat() {
    new AccountingItemVatController();
  }


  /**
   * Endorse vat to tresury.
   */
  public void vatEndorse() {
    new VatEndorseFilterFrame();
  }



  /**
   * Create new debiting notes list frame.
   */
  public void getPurchaseDebitingNotesList() {
    new PurchaseDebitingDocsController();
  }


  /**
   * Create new credit notes list frame.
   */
  public void getSalesCreditNotesList() {
    new SaleCreditDocsController();
  }


  /**
   * Get application parameters.
   */
  public void getApplicationParsList() {
    new ApplicationParametersController();
  }


  /**
   * Get company parameters.
   */
  public void getCompanyParsList() {
    new CompanyParametersController();
  }


  /**
   * Get customized reports.
   */
  public void getReportsCustomizationList() {
    new ReportsController();
  }


  /**
   * Get scheduled activities.
   */
  public void getScheduledActivitiesList() {
    new ScheduledActivitiesController(false);
  }


  /**
   * Get call-out types (hierarchies).
   */
  public void getCallOutTypes() {
    new CallOutTypesController();
  }


  /**
   * Get call-out definitions.
   */
  public void getCallOuts() {
    new CallOutsController();
  }


  /**
   * Get call-out requests.
   */
  public void getCallOutRequests() {
    new CallOutRequestsController();
  }


  /**
   * Create new call-out request.
   */
  public void newCallOutRequest() {
    new CallOutRequestController(null,null);
  }


  /**
   * Get machineries list.
   */
  public void getMachineriesList() {
    new MachineriesController();
  }


  /**
   * Get employee appointments.
   */
  public void getEmployeeActivities() {
    BigDecimal progressiveReg04SYS03 = mainClass.getAuthorizations().getProgressiveReg04SYS03();
    if (progressiveReg04SYS03==null) {
      JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource("no employee code linked to the current user"),
          ClientSettings.getInstance().getResources().getResource("appointments cannot be viewed"),
          JOptionPane.WARNING_MESSAGE
      );
    }
    else
      new EmployeeAppointmentsFrame(
        progressiveReg04SYS03,
        mainClass.getAuthorizations().getCompanyCodeSys01SYS03(),
        mainClass.getAuthorizations().getEmployeeCode(),
        mainClass.getAuthorizations().getName_1(),
        mainClass.getAuthorizations().getName_2()
      );
  }


  /**
   * View CRM activities list.
   */
  public void getCRMActivitiesList() {
    new ScheduledActivitiesController(true);
  }


  /**
   * List of employee activities
   */
  public void getEmployeeActivitiesList() {
    new EmployeeGanttController();
  }


  /**
   * Document Hierarchy Type definition + Document Hierarchy definition + Document Level Properties definition
   */
  public void getDocumentsHiearchiesList() {
    new DocumentTypesController();
  }


  /**
   * Get Document List
   */
  public void getDocumentsList() {
    new DocumentsController();
  }


  /**
   * Get products List
   */
  public void getBillsOfMaterialList() {
    new ItemsController(true);
  }


  /**
   * Get manufactures List
   */
  public void getManufacturesList() {
    new ManufacturesController();
  }


  /**
   * Get operations list (used when defining manufacture cycles)
   */
  public void getOperationsList() {
    new OperationsController();
  }


  /**
   * Get production orders list.
   */
  public void getProdOrdersList() {
    new ProdOrdersController();
  }


  /**
   * Get functions list.
   */
  public void getFunctionsList() {
    new FunctionController();
  }


  /**
   * Get employees hierarchy.
   */
  public void getEmployeeHiearchiesList() {
    new SubjectHierarchiesController(ApplicationConsts.SUBJECT_EMPLOYEE,"REG08_EMPLOYEES","employees hierarchies",ApplicationConsts.ID_HIERAR_EMPLOYEES_GRID,true);
  }


  /**
   * Get custom functions list.
   */
  public void getCustomFunctionsList() {
    new CustomFunctionsController();
  }


  /**
   * Show SQL Tool window.
   */
  public void sqlTool() {
    new SqlToolFrame();
  }


  /**
   * Show a custom grid frame.
   * @param functionId related functionId
   */
  public void executeCustomFunction(String functionId) {
    new ShowCustomFunctionFrame(functionId);
  }


  public void getVariantTypesList() {
    new VariantTypesController();
  }

  public void getVariantsList() {
    new VariantsController();
  }


  public void getPOS() {
    new SaleDeskCompanySelDialog();
  }


  public void getOrderTracking() {
    new OrderTrackingFrame();
  }


  public void printBarcodeLabels() {
    new PrintBarcodeLabelsFrame();
  }


  public void getSalesPivotFrame() {
    new SalesPivotFrame();
  }


  public void getDeliveryRequestsList() {
    new DeliveryRequestsController();
  }


  public void getTranslations() {
    new TranslationsGridFrame(null,null);
  }


  public void getConnectedUsers() {
    new UsersListFrame();
  }


  public void getReorderFromMinStocks() {
    new ReorderFromMinStocksFrame();
  }


  public void getABC() {
    new ABCFrame();
  }


  public void importData() {
    new ETLProcessesGridController();
  }


	public void getInventoryList() {
		new InventoryListFrame();
	}


	public void getSalesGraph() {
		new SalesReportFrame();
	}


	/**
	 * Show an editable grid used to define sheets levels and names and sheets definition, for spare parts catalogue
	 */
	public void getSheetLevelsFrame() {
		new SheetLevelsFrame();
	}

	/**
	 * Show an editable spare parts catalogue, used to define links among sheets, documents and spare parts.
	 */
	public void getSparePartsDefFrame() {
		new SparePartsCatalogueFrame(false);
	}

	/**
	 * Show a read only spare parts catalogue.
	 */
	public void getSparePartsCatFrame() {
		new SparePartsCatalogueFrame(true);
	}


}



