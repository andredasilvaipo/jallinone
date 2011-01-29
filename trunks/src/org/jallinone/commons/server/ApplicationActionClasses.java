package org.jallinone.commons.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jallinone.accounting.accountingmotives.server.DeleteAccountingMotivesAction;
import org.jallinone.accounting.accountingmotives.server.InsertAccountingMotivesAction;
import org.jallinone.accounting.accountingmotives.server.LoadAccountingMotivesAction;
import org.jallinone.accounting.accountingmotives.server.UpdateAccountingMotivesAction;
import org.jallinone.accounting.accountingmotives.server.ValidateAccountingMotiveCodeAction;
import org.jallinone.accounting.accounts.server.DeleteAccountsAction;
import org.jallinone.accounting.accounts.server.InsertAccountsAction;
import org.jallinone.accounting.accounts.server.LoadAccountsAction;
import org.jallinone.accounting.accounts.server.UpdateAccountsAction;
import org.jallinone.accounting.accounts.server.ValidateAccountCodeAction;
import org.jallinone.accounting.ledger.server.DeleteLedgerAction;
import org.jallinone.accounting.ledger.server.InsertLedgerAction;
import org.jallinone.accounting.ledger.server.LoadLedgerAction;
import org.jallinone.accounting.ledger.server.UpdateLedgerAction;
import org.jallinone.accounting.ledger.server.ValidateLedgerCodeAction;
import org.jallinone.accounting.movements.server.ClosePAccountsAction;
import org.jallinone.accounting.movements.server.EndorseEAccountsAction;
import org.jallinone.accounting.movements.server.InsertJournalItemAction;
import org.jallinone.accounting.movements.server.OpenPAccountsAction;
import org.jallinone.accounting.vatregisters.server.DeleteVatRegistersAction;
import org.jallinone.accounting.vatregisters.server.InsertVatRegistersAction;
import org.jallinone.accounting.vatregisters.server.LoadVatRegistersAction;
import org.jallinone.accounting.vatregisters.server.UpdateVatRegistersAction;
import org.jallinone.accounting.vatregisters.server.ValidateVatRegisterCodeAction;
import org.jallinone.accounting.vatregisters.server.VatEndorseAction;
import org.jallinone.contacts.server.DeleteContactAction;
import org.jallinone.contacts.server.InsertContactAction;
import org.jallinone.contacts.server.LoadContactAction;
import org.jallinone.contacts.server.LoadContactsAction;
import org.jallinone.contacts.server.UpdateContactAction;
import org.jallinone.documents.server.DeleteDocumentLinksAction;
import org.jallinone.documents.server.DeleteDocumentTypeAction;
import org.jallinone.documents.server.DeleteDocumentVersionsAction;
import org.jallinone.documents.server.DeleteDocumentsAction;
import org.jallinone.documents.server.DeleteLevelPropertiesAction;
import org.jallinone.documents.server.InsertDocumentAction;
import org.jallinone.documents.server.InsertDocumentLinksAction;
import org.jallinone.documents.server.InsertDocumentTypeAction;
import org.jallinone.documents.server.InsertLevelPropertiesAction;
import org.jallinone.documents.server.LoadDocPropertiesAction;
import org.jallinone.documents.server.LoadDocumentAction;
import org.jallinone.documents.server.LoadDocumentLinksAction;
import org.jallinone.documents.server.LoadDocumentTypesAction;
import org.jallinone.documents.server.LoadDocumentVersionAction;
import org.jallinone.documents.server.LoadDocumentVersionsAction;
import org.jallinone.documents.server.LoadDocumentsAction;
import org.jallinone.documents.server.LoadLevelPropertiesAction;
import org.jallinone.documents.server.UpdateDocPropertiesAction;
import org.jallinone.documents.server.UpdateDocumentAction;
import org.jallinone.documents.server.UpdateDocumentTypesAction;
import org.jallinone.documents.server.UpdateLevelPropertiesAction;
import org.jallinone.employees.server.DeleteEmployeeAction;
import org.jallinone.employees.server.DeleteEmployeeCalendarsAction;
import org.jallinone.employees.server.InsertEmployeeAction;
import org.jallinone.employees.server.InsertEmployeeCalendarsAction;
import org.jallinone.employees.server.LoadEmployeeAction;
import org.jallinone.employees.server.LoadEmployeeCalendarAction;
import org.jallinone.employees.server.LoadEmployeesAction;
import org.jallinone.employees.server.UpdateEmployeeAction;
import org.jallinone.employees.server.UpdateEmployeeCalendarsAction;
import org.jallinone.employees.server.ValidateEmployeeCodeAction;
import org.jallinone.expirations.server.*;
import org.jallinone.hierarchies.server.DeleteLevelAction;
import org.jallinone.hierarchies.server.InsertLevelAction;
import org.jallinone.hierarchies.server.LoadHierarchyAction;
import org.jallinone.hierarchies.server.LoadLeavesAction;
import org.jallinone.hierarchies.server.RootLevelAction;
import org.jallinone.hierarchies.server.UpdateLevelAction;
import org.jallinone.items.server.CreateBarcodeLabelsDataAction;
import org.jallinone.items.server.DeleteBarcodeLabelsDataAction;
import org.jallinone.items.server.DeleteItemAttachedDocsAction;
import org.jallinone.items.server.DeleteItemTypeAction;
import org.jallinone.items.server.DeleteItemsAction;
import org.jallinone.items.server.InsertItemAction;
import org.jallinone.items.server.InsertItemAttachedDocsAction;
import org.jallinone.items.server.InsertItemTypeAction;
import org.jallinone.items.server.LoadItemAction;
import org.jallinone.items.server.LoadItemAttachedDocsAction;
import org.jallinone.items.server.LoadItemTypesAction;
import org.jallinone.items.server.LoadItemVariantsAction;
import org.jallinone.items.server.LoadItemsAction;
import org.jallinone.items.server.LoadVariantBarcodesAction;
import org.jallinone.items.server.LoadVariantMinStocksAction;
import org.jallinone.items.server.UpdateItemAction;
import org.jallinone.items.server.UpdateItemTypesAction;
import org.jallinone.items.server.UpdateItemVariantsAction;
import org.jallinone.items.server.UpdateMinStocksAction;
import org.jallinone.items.server.UpdateVariantBarcodesAction;
import org.jallinone.items.server.UpdateVariantMinStocksAction;
import org.jallinone.items.server.ValidateItemCodeAction;
import org.jallinone.items.server.ValidateVariantBarcodeAction;
import org.jallinone.ordertracking.server.LoadOrderTrackingAction;
import org.jallinone.production.billsofmaterial.server.CreateBillOfMaterialsDataAction;
import org.jallinone.production.billsofmaterial.server.DeleteAltComponentsAction;
import org.jallinone.production.billsofmaterial.server.DeleteBillOfMaterialsDataAction;
import org.jallinone.production.billsofmaterial.server.DeleteComponentsAction;
import org.jallinone.production.billsofmaterial.server.InsertAltComponentsAction;
import org.jallinone.production.billsofmaterial.server.InsertComponentsAction;
import org.jallinone.production.billsofmaterial.server.LoadAltComponentsAction;
import org.jallinone.production.billsofmaterial.server.LoadBillsOfMaterialAction;
import org.jallinone.production.billsofmaterial.server.LoadComponentsAction;
import org.jallinone.production.billsofmaterial.server.LoadItemImplosionAction;
import org.jallinone.production.billsofmaterial.server.UpdateComponentsAction;
import org.jallinone.production.machineries.server.DeleteMachineriesAction;
import org.jallinone.production.machineries.server.InsertMachineriesAction;
import org.jallinone.production.machineries.server.LoadMachineriesAction;
import org.jallinone.production.machineries.server.UpdateMachineriesAction;
import org.jallinone.production.machineries.server.ValidateMachineryCodeAction;
import org.jallinone.production.manufactures.server.DeleteManufacturePhasesAction;
import org.jallinone.production.manufactures.server.DeleteManufacturesAction;
import org.jallinone.production.manufactures.server.DeleteOperationsAction;
import org.jallinone.production.manufactures.server.InsertManufactureAction;
import org.jallinone.production.manufactures.server.InsertManufacturePhasesAction;
import org.jallinone.production.manufactures.server.InsertOperationsAction;
import org.jallinone.production.manufactures.server.LoadManufacturePhasesAction;
import org.jallinone.production.manufactures.server.LoadManufacturesAction;
import org.jallinone.production.manufactures.server.LoadOperationsAction;
import org.jallinone.production.manufactures.server.UpdateManufactureAction;
import org.jallinone.production.manufactures.server.UpdateManufacturePhasesAction;
import org.jallinone.production.manufactures.server.UpdateOperationsAction;
import org.jallinone.production.manufactures.server.ValidateManufactureCodeAction;
import org.jallinone.production.manufactures.server.ValidateOperationCodeAction;
import org.jallinone.production.orders.server.CheckComponentsAvailabilityAction;
import org.jallinone.production.orders.server.CloseProdOrderAction;
import org.jallinone.production.orders.server.ConfirmProdOrderAction;
import org.jallinone.production.orders.server.DeleteProdOrderProductsAction;
import org.jallinone.production.orders.server.DeleteProdOrdersAction;
import org.jallinone.production.orders.server.InsertProdOrderAction;
import org.jallinone.production.orders.server.InsertProdOrderProductsAction;
import org.jallinone.production.orders.server.LoadProdOrderAction;
import org.jallinone.production.orders.server.LoadProdOrderComponentsAction;
import org.jallinone.production.orders.server.LoadProdOrderProductsAction;
import org.jallinone.production.orders.server.LoadProdOrdersAction;
import org.jallinone.production.orders.server.UpdateProdOrderAction;
import org.jallinone.production.orders.server.UpdateProdOrderProductsAction;
import org.jallinone.purchases.documents.server.ClosePurchaseDocAction;
import org.jallinone.purchases.documents.server.ConfirmPurchaseOrderAction;
import org.jallinone.purchases.documents.server.CreateBarcodeLabelsDataFromPurchaseDocAction;
import org.jallinone.purchases.documents.server.CreateInvoiceFromInDelivNotesAction;
import org.jallinone.purchases.documents.server.CreateInvoiceFromPurchaseDocAction;
import org.jallinone.purchases.documents.server.CreatePurchaseOrdersAction;
import org.jallinone.purchases.documents.server.DeletePurchaseDocRowsAction;
import org.jallinone.purchases.documents.server.DeletePurchaseDocsAction;
import org.jallinone.purchases.documents.server.InsertPurchaseDocAction;
import org.jallinone.purchases.documents.server.InsertPurchaseDocRowAction;
import org.jallinone.purchases.documents.server.InsertPurchaseDocRowsAction;
import org.jallinone.purchases.documents.server.LoadInDeliveryNotesForPurchaseDocAction;
import org.jallinone.purchases.documents.server.LoadPurchaseDocAction;
import org.jallinone.purchases.documents.server.LoadPurchaseDocAndDelivNoteRowsAction;
import org.jallinone.purchases.documents.server.LoadPurchaseDocRowAction;
import org.jallinone.purchases.documents.server.LoadPurchaseDocRowsAction;
import org.jallinone.purchases.documents.server.LoadPurchaseDocsAction;
import org.jallinone.purchases.documents.server.LoadSupplierPriceItemsAction;
import org.jallinone.purchases.documents.server.PurchaseDocTotalsAction;
import org.jallinone.purchases.documents.server.ReorderFromMinStocksAction;
import org.jallinone.purchases.documents.server.UpdatePurchaseDocAction;
import org.jallinone.purchases.documents.server.UpdatePurchaseDocRowAction;
import org.jallinone.purchases.documents.server.ValidatePurchaseDocNumberAction;
import org.jallinone.purchases.documents.server.ValidateSupplierPriceItemCodeAction;
import org.jallinone.purchases.items.server.CreateABCAction;
import org.jallinone.purchases.items.server.DeleteABCAction;
import org.jallinone.purchases.items.server.DeleteSupplierItemsAction;
import org.jallinone.purchases.items.server.ImportAllItemsToSupplierAction;
import org.jallinone.purchases.items.server.InsertSupplierItemsAction;
import org.jallinone.purchases.items.server.LoadABCAction;
import org.jallinone.purchases.items.server.LoadSupplierItemsAction;
import org.jallinone.purchases.items.server.UpdateSupplierItemsAction;
import org.jallinone.purchases.items.server.ValidateSupplierItemCodeAction;
import org.jallinone.purchases.pricelist.server.ChangeSupplierPricelistAction;
import org.jallinone.purchases.pricelist.server.DeleteSupplierPricelistAction;
import org.jallinone.purchases.pricelist.server.DeleteSupplierPricesAction;
import org.jallinone.purchases.pricelist.server.ImportAllSupplierItemsAction;
import org.jallinone.purchases.pricelist.server.InsertSupplierPricelistsAction;
import org.jallinone.purchases.pricelist.server.InsertSupplierPricesAction;
import org.jallinone.purchases.pricelist.server.LoadSupplierPricelistAction;
import org.jallinone.purchases.pricelist.server.LoadSupplierPricesAction;
import org.jallinone.purchases.pricelist.server.LoadSupplierVariantsPricesAction;
import org.jallinone.purchases.pricelist.server.UpdateSupplierPricelistsAction;
import org.jallinone.purchases.pricelist.server.UpdateSupplierPricesAction;
import org.jallinone.purchases.pricelist.server.UpdateSupplierVariantsPricesAction;
import org.jallinone.purchases.pricelist.server.ValidateSupplierPricelistCodeAction;
import org.jallinone.purchases.suppliers.server.DeleteSuppliersAction;
import org.jallinone.purchases.suppliers.server.InsertSupplierAction;
import org.jallinone.purchases.suppliers.server.LoadSupplierAction;
import org.jallinone.purchases.suppliers.server.LoadSuppliersAction;
import org.jallinone.purchases.suppliers.server.UpdateSupplierAction;
import org.jallinone.purchases.suppliers.server.ValidateSupplierCodeAction;
import org.jallinone.registers.bank.server.DeleteBanksAction;
import org.jallinone.registers.bank.server.InsertBankAction;
import org.jallinone.registers.bank.server.LoadBanksAction;
import org.jallinone.registers.bank.server.UpdateBankAction;
import org.jallinone.registers.bank.server.ValidateBankCodeAction;
import org.jallinone.registers.carrier.server.DeleteCarriersAction;
import org.jallinone.registers.carrier.server.InsertCarriersAction;
import org.jallinone.registers.carrier.server.LoadCarriersAction;
import org.jallinone.registers.carrier.server.UpdateCarriersAction;
import org.jallinone.registers.carrier.server.ValidateCarrierCodeAction;
import org.jallinone.registers.currency.server.DeleteCurrenciesAction;
import org.jallinone.registers.currency.server.InsertCurrencyAction;
import org.jallinone.registers.currency.server.LoadCompanyCurrencyAction;
import org.jallinone.registers.currency.server.LoadCurrenciesAction;
import org.jallinone.registers.currency.server.LoadCurrencyConvsAction;
import org.jallinone.registers.currency.server.UpdateCurrenciesAction;
import org.jallinone.registers.currency.server.UpdateCurrencyConvsAction;
import org.jallinone.registers.currency.server.ValidateCurrencyCodeAction;
import org.jallinone.registers.measure.server.DeleteMeasuresAction;
import org.jallinone.registers.measure.server.InsertMeasuresAction;
import org.jallinone.registers.measure.server.LoadMeasureConvsAction;
import org.jallinone.registers.measure.server.LoadMeasuresAction;
import org.jallinone.registers.measure.server.UpdateMeasureConvsAction;
import org.jallinone.registers.measure.server.UpdateMeasuresAction;
import org.jallinone.registers.measure.server.ValidateMeasureCodeAction;
import org.jallinone.registers.payments.server.DeletePaymentTypesAction;
import org.jallinone.registers.payments.server.DeletePaymentsAction;
import org.jallinone.registers.payments.server.InsertPaymentTypesAction;
import org.jallinone.registers.payments.server.InsertPaymentsAction;
import org.jallinone.registers.payments.server.LoadPaymentInstalmentsAction;
import org.jallinone.registers.payments.server.LoadPaymentTypesAction;
import org.jallinone.registers.payments.server.LoadPaymentsAction;
import org.jallinone.registers.payments.server.UpdatePaymentInstalmentsAction;
import org.jallinone.registers.payments.server.UpdatePaymentTypesAction;
import org.jallinone.registers.payments.server.UpdatePaymentsAction;
import org.jallinone.registers.payments.server.ValidatePaymentCodeAction;
import org.jallinone.registers.payments.server.ValidatePaymentTypeCodeAction;
import org.jallinone.registers.task.server.DeleteTasksAction;
import org.jallinone.registers.task.server.InsertTasksAction;
import org.jallinone.registers.task.server.LoadTasksAction;
import org.jallinone.registers.task.server.UpdateTasksAction;
import org.jallinone.registers.task.server.ValidateTaskCodeAction;
import org.jallinone.registers.transportmotives.server.DeleteTransportMotivesAction;
import org.jallinone.registers.transportmotives.server.InsertTransportMotivesAction;
import org.jallinone.registers.transportmotives.server.LoadTransportMotivesAction;
import org.jallinone.registers.transportmotives.server.UpdateTransportMotivesAction;
import org.jallinone.registers.transportmotives.server.ValidateTransportMotiveCodeAction;
import org.jallinone.registers.vat.server.DeleteVatsAction;
import org.jallinone.registers.vat.server.InsertVatsAction;
import org.jallinone.registers.vat.server.LoadVatsAction;
import org.jallinone.registers.vat.server.UpdateVatsAction;
import org.jallinone.registers.vat.server.ValidateVatCodeAction;
import org.jallinone.reports.server.JasperReportAction;
import org.jallinone.sales.activities.server.DeleteSaleActivitiesAction;
import org.jallinone.sales.activities.server.InsertSaleActivitiesAction;
import org.jallinone.sales.activities.server.LoadSaleActivitiesAction;
import org.jallinone.sales.activities.server.UpdateSaleActivitiesAction;
import org.jallinone.sales.activities.server.ValidateSaleActivityCodeAction;
import org.jallinone.sales.agents.server.DeleteAgentTypesAction;
import org.jallinone.sales.agents.server.DeleteAgentsAction;
import org.jallinone.sales.agents.server.InsertAgentTypesAction;
import org.jallinone.sales.agents.server.InsertAgentsAction;
import org.jallinone.sales.agents.server.LoadAgentTypesAction;
import org.jallinone.sales.agents.server.LoadAgentsAction;
import org.jallinone.sales.agents.server.UpdateAgentTypesAction;
import org.jallinone.sales.agents.server.UpdateAgentsAction;
import org.jallinone.sales.agents.server.ValidateAgentCodeAction;
import org.jallinone.sales.charges.server.DeleteChargesAction;
import org.jallinone.sales.charges.server.InsertChargesAction;
import org.jallinone.sales.charges.server.LoadChargesAction;
import org.jallinone.sales.charges.server.UpdateChargesAction;
import org.jallinone.sales.charges.server.ValidateChargeCodeAction;
import org.jallinone.sales.customers.server.DeleteCustomersAction;
import org.jallinone.sales.customers.server.InsertCustomerAction;
import org.jallinone.sales.customers.server.LoadCustomerAction;
import org.jallinone.sales.customers.server.LoadCustomersAction;
import org.jallinone.sales.customers.server.UpdateCustomerAction;
import org.jallinone.sales.customers.server.ValidateCustomerCodeAction;
import org.jallinone.sales.destinations.server.DeleteDestinationsAction;
import org.jallinone.sales.destinations.server.InsertDestinationsAction;
import org.jallinone.sales.destinations.server.LoadDestinationsAction;
import org.jallinone.sales.destinations.server.UpdateDestinationsAction;
import org.jallinone.sales.destinations.server.ValidateDestinationCodeAction;
import org.jallinone.sales.discounts.server.DeleteCustomerDiscountsAction;
import org.jallinone.sales.discounts.server.DeleteHierarCustomerDiscountsAction;
import org.jallinone.sales.discounts.server.DeleteHierarItemDiscountsAction;
import org.jallinone.sales.discounts.server.DeleteItemDiscountsAction;
import org.jallinone.sales.discounts.server.InsertCustomerDiscountsAction;
import org.jallinone.sales.discounts.server.InsertHierarCustomerDiscountsAction;
import org.jallinone.sales.discounts.server.InsertHierarItemDiscountsAction;
import org.jallinone.sales.discounts.server.InsertItemDiscountsAction;
import org.jallinone.sales.discounts.server.LoadCustomerDiscountsAction;
import org.jallinone.sales.discounts.server.LoadHierarCustomerDiscountsAction;
import org.jallinone.sales.discounts.server.LoadHierarItemDiscountsAction;
import org.jallinone.sales.discounts.server.LoadItemDiscountsAction;
import org.jallinone.sales.discounts.server.UpdateCustomerDiscountsAction;
import org.jallinone.sales.discounts.server.UpdateHierarCustomerDiscountsAction;
import org.jallinone.sales.discounts.server.UpdateHierarItemDiscountsAction;
import org.jallinone.sales.discounts.server.UpdateItemDiscountsAction;
import org.jallinone.sales.documents.activities.server.DeleteSaleDocActivitiesAction;
import org.jallinone.sales.documents.activities.server.InsertSaleDocActivitiesAction;
import org.jallinone.sales.documents.activities.server.LoadSaleDocActivitiesAction;
import org.jallinone.sales.documents.activities.server.UpdateSaleDocActivitiesAction;
import org.jallinone.sales.documents.headercharges.server.DeleteSaleDocChargesAction;
import org.jallinone.sales.documents.headercharges.server.InsertSaleDocChargesAction;
import org.jallinone.sales.documents.headercharges.server.LoadSaleDocChargesAction;
import org.jallinone.sales.documents.headercharges.server.UpdateSaleDocChargesAction;
import org.jallinone.sales.documents.headerdiscounts.server.DeleteSaleDocDiscountsAction;
import org.jallinone.sales.documents.headerdiscounts.server.InsertSaleDocDiscountsAction;
import org.jallinone.sales.documents.headerdiscounts.server.LoadSaleDocDiscountsAction;
import org.jallinone.sales.documents.headerdiscounts.server.LoadSaleHeaderDiscountsAction;
import org.jallinone.sales.documents.headerdiscounts.server.UpdateSaleDocDiscountsAction;
import org.jallinone.sales.documents.headerdiscounts.server.ValidateSaleHeaderDiscountCodeAction;
import org.jallinone.sales.documents.itemdiscounts.server.DeleteSaleDocRowDiscountsAction;
import org.jallinone.sales.documents.itemdiscounts.server.InsertSaleDocRowDiscountsAction;
import org.jallinone.sales.documents.itemdiscounts.server.LoadSaleDocRowDiscountsAction;
import org.jallinone.sales.documents.itemdiscounts.server.LoadSaleItemDiscountsAction;
import org.jallinone.sales.documents.itemdiscounts.server.UpdateSaleDocRowDiscountsAction;
import org.jallinone.sales.documents.itemdiscounts.server.ValidateSaleItemDiscountCodeAction;
import org.jallinone.sales.documents.server.CloseSaleDocAction;
import org.jallinone.sales.documents.server.ConfirmSaleDocAction;
import org.jallinone.sales.documents.server.CreateInvoiceFromOutDelivNotesAction;
import org.jallinone.sales.documents.server.CreateInvoiceFromSaleDocAction;
import org.jallinone.sales.documents.server.CreateSaleDocFromEstimateAction;
import org.jallinone.sales.documents.server.DeleteSaleDocRowsAction;
import org.jallinone.sales.documents.server.DeleteSaleDocsAction;
import org.jallinone.sales.documents.server.InsertSaleDocAction;
import org.jallinone.sales.documents.server.InsertSaleDocRowAction;
import org.jallinone.sales.documents.server.InsertSaleDocRowsAction;
import org.jallinone.sales.documents.server.LoadItemsSoldToOtherCustomersAction;
import org.jallinone.sales.documents.server.LoadOutDeliveryNotesForSaleDocAction;
import org.jallinone.sales.documents.server.LoadPriceItemsAction;
import org.jallinone.sales.documents.server.LoadSaleDocAction;
import org.jallinone.sales.documents.server.LoadSaleDocAndDelivNoteRowsAction;
import org.jallinone.sales.documents.server.LoadSaleDocRowAction;
import org.jallinone.sales.documents.server.LoadSaleDocRowsAction;
import org.jallinone.sales.documents.server.LoadSaleDocsAction;
import org.jallinone.sales.documents.server.SaleDocTotalsAction;
import org.jallinone.sales.documents.server.SaleItemTotalDiscountAction;
import org.jallinone.sales.documents.server.UpdateSaleDocAction;
import org.jallinone.sales.documents.server.UpdateSaleDocRowAction;
import org.jallinone.sales.documents.server.ValidatePriceItemCodeAction;
import org.jallinone.sales.documents.server.ValidateSaleDocNumberAction;
import org.jallinone.sales.pricelist.server.ChangePricelistAction;
import org.jallinone.sales.pricelist.server.DeletePricelistAction;
import org.jallinone.sales.pricelist.server.DeletePricesAction;
import org.jallinone.sales.pricelist.server.ImportAllItemsAction;
import org.jallinone.sales.pricelist.server.InsertPricelistsAction;
import org.jallinone.sales.pricelist.server.InsertPricesAction;
import org.jallinone.sales.pricelist.server.LoadPricelistAction;
import org.jallinone.sales.pricelist.server.LoadPricesAction;
import org.jallinone.sales.pricelist.server.LoadVariantsPriceAction;
import org.jallinone.sales.pricelist.server.LoadVariantsPricesAction;
import org.jallinone.sales.pricelist.server.UpdatePricelistsAction;
import org.jallinone.sales.pricelist.server.UpdatePricesAction;
import org.jallinone.sales.pricelist.server.UpdateVariantsPricesAction;
import org.jallinone.sales.pricelist.server.ValidatePricelistCodeAction;
import org.jallinone.sales.reports.server.SalesPivotAction;
import org.jallinone.sales.reports.server.SalesReportAction;
import org.jallinone.scheduler.activities.server.CloseScheduledActivityAction;
import org.jallinone.scheduler.activities.server.DeleteAttachedDocsAction;
import org.jallinone.scheduler.activities.server.DeleteScheduledActivitiesAction;
import org.jallinone.scheduler.activities.server.DeleteScheduledEmployeesAction;
import org.jallinone.scheduler.activities.server.DeleteScheduledItemsAction;
import org.jallinone.scheduler.activities.server.DeleteScheduledMachineriesAction;
import org.jallinone.scheduler.activities.server.InsertAttachedDocsAction;
import org.jallinone.scheduler.activities.server.InsertScheduledActivityAction;
import org.jallinone.scheduler.activities.server.InsertScheduledEmployeesAction;
import org.jallinone.scheduler.activities.server.InsertScheduledItemsAction;
import org.jallinone.scheduler.activities.server.InsertScheduledMachineriesAction;
import org.jallinone.scheduler.activities.server.LoadAttachedDocsAction;
import org.jallinone.scheduler.activities.server.LoadEmployeeActivitiesAction;
import org.jallinone.scheduler.activities.server.LoadScheduledActivitiesAction;
import org.jallinone.scheduler.activities.server.LoadScheduledActivityAction;
import org.jallinone.scheduler.activities.server.LoadScheduledEmployeesAction;
import org.jallinone.scheduler.activities.server.LoadScheduledItemsAction;
import org.jallinone.scheduler.activities.server.LoadScheduledMachineriesAction;
import org.jallinone.scheduler.activities.server.UpdateScheduledActivityAction;
import org.jallinone.scheduler.activities.server.UpdateScheduledEmployeesAction;
import org.jallinone.scheduler.activities.server.UpdateScheduledItemsAction;
import org.jallinone.scheduler.activities.server.UpdateScheduledMachineriesAction;
import org.jallinone.scheduler.callouts.server.CalloutRequestsReportAction;
import org.jallinone.scheduler.callouts.server.CreateInvoiceFromScheduledActivityAction;
import org.jallinone.scheduler.callouts.server.DeleteCallOutItemsAction;
import org.jallinone.scheduler.callouts.server.DeleteCallOutMachineriesAction;
import org.jallinone.scheduler.callouts.server.DeleteCallOutRequestsAction;
import org.jallinone.scheduler.callouts.server.DeleteCallOutTasksAction;
import org.jallinone.scheduler.callouts.server.DeleteCallOutTypeAction;
import org.jallinone.scheduler.callouts.server.DeleteCallOutsAction;
import org.jallinone.scheduler.callouts.server.InsertCallOutAction;
import org.jallinone.scheduler.callouts.server.InsertCallOutItemsAction;
import org.jallinone.scheduler.callouts.server.InsertCallOutMachineriesAction;
import org.jallinone.scheduler.callouts.server.InsertCallOutRequestAction;
import org.jallinone.scheduler.callouts.server.InsertCallOutTasksAction;
import org.jallinone.scheduler.callouts.server.InsertCallOutTypeAction;
import org.jallinone.scheduler.callouts.server.LinkScheduledActivityToCallOutRequestAction;
import org.jallinone.scheduler.callouts.server.LoadCallOutAction;
import org.jallinone.scheduler.callouts.server.LoadCallOutItemsAction;
import org.jallinone.scheduler.callouts.server.LoadCallOutMachineriesAction;
import org.jallinone.scheduler.callouts.server.LoadCallOutRequestAction;
import org.jallinone.scheduler.callouts.server.LoadCallOutRequestsAction;
import org.jallinone.scheduler.callouts.server.LoadCallOutTasksAction;
import org.jallinone.scheduler.callouts.server.LoadCallOutTypesAction;
import org.jallinone.scheduler.callouts.server.LoadCallOutsAction;
import org.jallinone.scheduler.callouts.server.UpdateCallOutAction;
import org.jallinone.scheduler.callouts.server.UpdateCallOutRequestAction;
import org.jallinone.scheduler.callouts.server.UpdateCallOutTypesAction;
import org.jallinone.scheduler.callouts.server.ValidateCallOutCodeAction;
import org.jallinone.scheduler.gantt.server.LoadEmployeeActivitiesOnGanttAction;
import org.jallinone.scheduler.gantt.server.LoadScheduledEmployeesOnGanttAction;
import org.jallinone.sqltool.server.DeleteTablesAction;
import org.jallinone.sqltool.server.ExecuteQueryAction;
import org.jallinone.sqltool.server.ExecuteStatementAction;
import org.jallinone.sqltool.server.ExecuteValidateQueryAction;
import org.jallinone.sqltool.server.GetQueryInfoAction;
import org.jallinone.sqltool.server.InsertTablesAction;
import org.jallinone.sqltool.server.LoadEntitiesAction;
import org.jallinone.sqltool.server.UpdateTablesAction;
import org.jallinone.startup.server.*;
import org.jallinone.subjects.server.DeleteReferencesAction;
import org.jallinone.subjects.server.DeleteSubjectHierarchyAction;
import org.jallinone.subjects.server.DeleteSubjectsLinksAction;
import org.jallinone.subjects.server.InsertReferencesAction;
import org.jallinone.subjects.server.InsertSubjectAction;
import org.jallinone.subjects.server.InsertSubjectHierarchyAction;
import org.jallinone.subjects.server.InsertSubjectsLinksAction;
import org.jallinone.subjects.server.LoadHierarSubjectsAction;
import org.jallinone.subjects.server.LoadReferencesAction;
import org.jallinone.subjects.server.LoadSubjectHierarchiesAction;
import org.jallinone.subjects.server.LoadSubjectHierarchyLevelsAction;
import org.jallinone.subjects.server.LoadSubjectPerNameAction;
import org.jallinone.subjects.server.UpdateReferencesAction;
import org.jallinone.subjects.server.UpdateSubjectAction;
import org.jallinone.subjects.server.UpdateSubjectHierarchiesAction;
import org.jallinone.subjects.server.UpdateSubjectHierarchyLevelsAction;
import org.jallinone.system.companies.server.DeleteCompanyAction;
import org.jallinone.system.companies.server.InsertCompanyAction;
import org.jallinone.system.companies.server.LoadCompaniesAction;
import org.jallinone.system.companies.server.LoadCompanyAction;
import org.jallinone.system.companies.server.UpdateCompanyAction;
import org.jallinone.system.customizations.server.CheckReportFilesAction;
import org.jallinone.system.customizations.server.DeleteCustomFunctionsAction;
import org.jallinone.system.customizations.server.DeleteWindowCustomizationsAction;
import org.jallinone.system.customizations.server.InsertCustomFunctionAction;
import org.jallinone.system.customizations.server.InsertWindowCustomizationsAction;
import org.jallinone.system.customizations.server.LoadCustomColumnsAction;
import org.jallinone.system.customizations.server.LoadCustomFunctionAction;
import org.jallinone.system.customizations.server.LoadCustomFunctionsAction;
import org.jallinone.system.customizations.server.LoadCustomizedReportsAction;
import org.jallinone.system.customizations.server.LoadReportFileNamesAction;
import org.jallinone.system.customizations.server.LoadWindowCustomizationsAction;
import org.jallinone.system.customizations.server.LoadWindowsAction;
import org.jallinone.system.customizations.server.UpdateCustomColumnsAction;
import org.jallinone.system.customizations.server.UpdateCustomFunctionAction;
import org.jallinone.system.customizations.server.UpdateCustomReportsAction;
import org.jallinone.system.customizations.server.UpdateWindowCustomizationsAction;
import org.jallinone.system.customizations.server.UploadReportFilesAction;
import org.jallinone.system.gridmanager.server.DbGridPermissionsAction;
import org.jallinone.system.importdata.server.DeleteETLProcessesAction;
import org.jallinone.system.importdata.server.GetFolderContentAction;
import org.jallinone.system.importdata.server.InsertETLProcessAction;
import org.jallinone.system.importdata.server.LoadETLProcessAction;
import org.jallinone.system.importdata.server.LoadETLProcessesAction;
import org.jallinone.system.importdata.server.LoadSelectableFieldsAction;
import org.jallinone.system.importdata.server.StartETLProcessAction;
import org.jallinone.system.languages.server.DeleteLanguageAction;
import org.jallinone.system.languages.server.InsertLanguagesAction;
import org.jallinone.system.languages.server.UpdateLanguagesAction;
import org.jallinone.system.languages.server.ValidateLanguageCodeAction;
import org.jallinone.system.permissions.server.DeleteRoleAction;
import org.jallinone.system.permissions.server.DeleteUsersAction;
import org.jallinone.system.permissions.server.InsertRolesAction;
import org.jallinone.system.permissions.server.InsertUserAction;
import org.jallinone.system.permissions.server.LoadFunctionsAction;
import org.jallinone.system.permissions.server.LoadGridPermissionsPerRoleAction;
import org.jallinone.system.permissions.server.LoadMenuFoldersAction;
import org.jallinone.system.permissions.server.LoadRoleFunctionCompaniesAction;
import org.jallinone.system.permissions.server.LoadRoleFunctionsAction;
import org.jallinone.system.permissions.server.LoadRolesAction;
import org.jallinone.system.permissions.server.LoadUserRolesAction;
import org.jallinone.system.permissions.server.LoadUsersAction;
import org.jallinone.system.permissions.server.UpdateFunctionAction;
import org.jallinone.system.permissions.server.UpdateGridPermissionsPerRoleAction;
import org.jallinone.system.permissions.server.UpdateRoleFunctionCompaniesAction;
import org.jallinone.system.permissions.server.UpdateRoleFunctionsAction;
import org.jallinone.system.permissions.server.UpdateRolesAction;
import org.jallinone.system.permissions.server.UpdateUserRolesAction;
import org.jallinone.system.permissions.server.UpdateUsersAction;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.server.LoadCompanyParamsAction;
import org.jallinone.system.server.LoadUserParamAction;
import org.jallinone.system.server.LoadUserParamsAction;
import org.jallinone.system.server.SaveUserParamAction;
import org.jallinone.system.server.UpdateApplicationParamsAction;
import org.jallinone.system.server.UpdateCompanyParamsAction;
import org.jallinone.system.server.UpdateUserParamsAction;
import org.jallinone.system.server.UserAuthorizationsAction;
import org.jallinone.system.server.UserLoginAction;
import org.jallinone.system.server.UsersListAction;
import org.jallinone.system.translations.server.LoadTranslationsAction;
import org.jallinone.system.translations.server.UpdateTranslationsAction;
import org.jallinone.variants.server.DeleteVariantTypesAction;
import org.jallinone.variants.server.DeleteVariantsAction;
import org.jallinone.variants.server.InsertVariantTypesAction;
import org.jallinone.variants.server.InsertVariantsAction;
import org.jallinone.variants.server.LoadProductVariantsMatrixAction;
import org.jallinone.variants.server.LoadVariantTypesAction;
import org.jallinone.variants.server.LoadVariantsAction;
import org.jallinone.variants.server.LoadVariantsNamesAction;
import org.jallinone.variants.server.UpdateVariantTypesAction;
import org.jallinone.variants.server.UpdateVariantsAction;
import org.jallinone.warehouse.availability.server.LoadBookedItemsAction;
import org.jallinone.warehouse.availability.server.LoadItemAvailabilitiesAction;
import org.jallinone.warehouse.availability.server.LoadOrderedItemsAction;
import org.jallinone.warehouse.documents.server.CloseDeliveryNoteAction;
import org.jallinone.warehouse.documents.server.DeleteDeliveryNotesAction;
import org.jallinone.warehouse.documents.server.DeleteInDeliveryNoteRowsAction;
import org.jallinone.warehouse.documents.server.DeleteOutDeliveryNoteRowsAction;
import org.jallinone.warehouse.documents.server.InsertInDeliveryNoteAction;
import org.jallinone.warehouse.documents.server.InsertInDeliveryNoteRowAction;
import org.jallinone.warehouse.documents.server.InsertOutDeliveryNoteAction;
import org.jallinone.warehouse.documents.server.InsertOutDeliveryNoteRowAction;
import org.jallinone.warehouse.documents.server.LoadInDeliveryNoteAction;
import org.jallinone.warehouse.documents.server.LoadInDeliveryNoteRowsAction;
import org.jallinone.warehouse.documents.server.LoadInDeliveryNotesAction;
import org.jallinone.warehouse.documents.server.LoadOutDeliveryNoteAction;
import org.jallinone.warehouse.documents.server.LoadOutDeliveryNoteRowsAction;
import org.jallinone.warehouse.documents.server.LoadOutDeliveryNotesAction;
import org.jallinone.warehouse.documents.server.UpdateInDeliveryNoteAction;
import org.jallinone.warehouse.documents.server.UpdateInDeliveryNoteRowsAction;
import org.jallinone.warehouse.documents.server.UpdateOutDeliveryNoteAction;
import org.jallinone.warehouse.documents.server.UpdateOutDeliveryNoteRowsAction;
import org.jallinone.warehouse.movements.server.InsertManualMovementAction;
import org.jallinone.warehouse.movements.server.InsertManualMovementsAction;
import org.jallinone.warehouse.server.CloseInventoryAction;
import org.jallinone.warehouse.server.ConfirmInventoryAction;
import org.jallinone.warehouse.server.DeleteInventoriesAction;
import org.jallinone.warehouse.server.DeleteInventoryItemsAction;
import org.jallinone.warehouse.server.DeleteWarehouseAction;
import org.jallinone.warehouse.server.ImportInventoryItemsAction;
import org.jallinone.warehouse.server.InsertInventoryAction;
import org.jallinone.warehouse.server.InsertInventoryItemAction;
import org.jallinone.warehouse.server.InsertWarehouseAction;
import org.jallinone.warehouse.server.LoadInventoriesAction;
import org.jallinone.warehouse.server.LoadInventoryItemsAction;
import org.jallinone.warehouse.server.LoadStoredSerialNumbersAction;
import org.jallinone.warehouse.server.LoadWarehouseAction;
import org.jallinone.warehouse.server.LoadWarehousesAction;
import org.jallinone.warehouse.server.UpdateInventoriesAction;
import org.jallinone.warehouse.server.UpdateInventoryItemsAction;
import org.jallinone.warehouse.server.UpdateWarehouseAction;
import org.jallinone.warehouse.server.ValidateStoredSerialNumberAction;
import org.jallinone.warehouse.server.ValidateWarehouseCodeAction;
import org.jallinone.warehouse.tables.motives.server.DeleteMotivesAction;
import org.jallinone.warehouse.tables.motives.server.InsertMotiveAction;
import org.jallinone.warehouse.tables.motives.server.InsertWarehouseMotivesAction;
import org.jallinone.warehouse.tables.motives.server.LoadMotivesAction;
import org.jallinone.warehouse.tables.motives.server.UpdateMotivesAction;
import org.jallinone.warehouse.tables.motives.server.ValidateMotiveCodeAction;
import org.jallinone.warehouse.tables.movements.server.LoadMovementsAction;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.server.Action;
import org.openswing.swing.server.ActionsCollection;
import org.openswing.swing.server.ConnectionManager;
import org.openswing.swing.server.UserSessionParameters;
import org.jallinone.items.spareparts.server.*;
import org.jallinone.expirations.server.PayImmediatelyAction;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Collection of application class actions.</p>
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
public class ApplicationActionClasses extends ActionsCollection {

  public ApplicationActionClasses() {

    Action a = null;
    a = new UserAuthorizationsAction(); put(a.getRequestName(),a);
    a = new UserLoginAction(); put(a.getRequestName(),a);
    a = new org.jallinone.system.server.LoadLanguagesAction(); put(a.getRequestName(),a);
    a = new CreateConfigFileAction(); put(a.getRequestName(),a);

    a = new LoadWindowsAction(); put(a.getRequestName(),a);
    a = new LoadWindowCustomizationsAction(); put(a.getRequestName(),a);
    a = new InsertWindowCustomizationsAction(); put(a.getRequestName(),a);
    a = new UpdateWindowCustomizationsAction(); put(a.getRequestName(),a);
    a = new DeleteWindowCustomizationsAction(); put(a.getRequestName(),a);

    a = new LoadCompaniesAction(); put(a.getRequestName(),a);
    a = new LoadCompanyAction(); put(a.getRequestName(),a);
    a = new InsertCompanyAction(); put(a.getRequestName(),a);
    a = new UpdateCompanyAction(); put(a.getRequestName(),a);
    a = new DeleteCompanyAction(); put(a.getRequestName(),a);

    a = new org.jallinone.system.languages.server.LoadLanguagesAction(); put(a.getRequestName(),a);
    a = new InsertLanguagesAction(); put(a.getRequestName(),a);
    a = new UpdateLanguagesAction(); put(a.getRequestName(),a);
    a = new DeleteLanguageAction(); put(a.getRequestName(),a);
    a = new ValidateLanguageCodeAction(); put(a.getRequestName(),a);

    a = new LoadWarehousesAction(); put(a.getRequestName(),a);
    a = new LoadWarehouseAction(); put(a.getRequestName(),a);
    a = new InsertWarehouseAction(); put(a.getRequestName(),a);
    a = new UpdateWarehouseAction(); put(a.getRequestName(),a);
    a = new DeleteWarehouseAction(); put(a.getRequestName(),a);
    a = new ValidateWarehouseCodeAction(); put(a.getRequestName(),a);

    a = new LoadHierarchyAction(); put(a.getRequestName(),a);
    a = new InsertLevelAction(); put(a.getRequestName(),a);
    a = new UpdateLevelAction(); put(a.getRequestName(),a);
    a = new DeleteLevelAction(); put(a.getRequestName(),a);
    a = new RootLevelAction(); put(a.getRequestName(),a);

    a = new LoadRolesAction(); put(a.getRequestName(),a);
    a = new InsertRolesAction(); put(a.getRequestName(),a);
    a = new UpdateRolesAction(); put(a.getRequestName(),a);
    a = new DeleteRoleAction(); put(a.getRequestName(),a);

    a = new LoadMenuFoldersAction(); put(a.getRequestName(),a);
    a = new LoadRoleFunctionsAction(); put(a.getRequestName(),a);
    a = new UpdateRoleFunctionsAction(); put(a.getRequestName(),a);
    a = new LoadRoleFunctionCompaniesAction(); put(a.getRequestName(),a);
    a = new UpdateRoleFunctionCompaniesAction(); put(a.getRequestName(),a);

    a = new LoadUsersAction(); put(a.getRequestName(),a);
    a = new InsertUserAction(); put(a.getRequestName(),a);
    a = new UpdateUsersAction(); put(a.getRequestName(),a);
    a = new DeleteUsersAction(); put(a.getRequestName(),a);
    a = new LoadUserRolesAction(); put(a.getRequestName(),a);
    a = new UpdateUserRolesAction(); put(a.getRequestName(),a);

    a = new LoadVatsAction(); put(a.getRequestName(),a);
    a = new InsertVatsAction(); put(a.getRequestName(),a);
    a = new UpdateVatsAction(); put(a.getRequestName(),a);
    a = new DeleteVatsAction(); put(a.getRequestName(),a);
    a = new ValidateVatCodeAction(); put(a.getRequestName(),a);

    a = new LoadTasksAction(); put(a.getRequestName(),a);
    a = new InsertTasksAction(); put(a.getRequestName(),a);
    a = new UpdateTasksAction(); put(a.getRequestName(),a);
    a = new DeleteTasksAction(); put(a.getRequestName(),a);
    a = new ValidateTaskCodeAction(); put(a.getRequestName(),a);

    a = new LoadCarriersAction(); put(a.getRequestName(),a);
    a = new InsertCarriersAction(); put(a.getRequestName(),a);
    a = new UpdateCarriersAction(); put(a.getRequestName(),a);
    a = new DeleteCarriersAction(); put(a.getRequestName(),a);
    a = new ValidateCarrierCodeAction(); put(a.getRequestName(),a);

    a = new LoadTransportMotivesAction(); put(a.getRequestName(),a);
    a = new InsertTransportMotivesAction(); put(a.getRequestName(),a);
    a = new UpdateTransportMotivesAction(); put(a.getRequestName(),a);
    a = new DeleteTransportMotivesAction(); put(a.getRequestName(),a);
    a = new ValidateTransportMotiveCodeAction(); put(a.getRequestName(),a);

    a = new LoadMeasuresAction(); put(a.getRequestName(),a);
    a = new InsertMeasuresAction(); put(a.getRequestName(),a);
    a = new UpdateMeasuresAction(); put(a.getRequestName(),a);
    a = new DeleteMeasuresAction(); put(a.getRequestName(),a);
    a = new ValidateMeasureCodeAction(); put(a.getRequestName(),a);
    a = new LoadMeasureConvsAction(); put(a.getRequestName(),a);
    a = new UpdateMeasureConvsAction(); put(a.getRequestName(),a);

    a = new LoadCurrenciesAction(); put(a.getRequestName(),a);
    a = new InsertCurrencyAction(); put(a.getRequestName(),a);
    a = new UpdateCurrenciesAction(); put(a.getRequestName(),a);
    a = new DeleteCurrenciesAction(); put(a.getRequestName(),a);
    a = new ValidateCurrencyCodeAction(); put(a.getRequestName(),a);
    a = new LoadCurrencyConvsAction(); put(a.getRequestName(),a);
    a = new UpdateCurrencyConvsAction(); put(a.getRequestName(),a);
    a = new LoadCompanyCurrencyAction(); put(a.getRequestName(),a);

    a = new LoadBanksAction(); put(a.getRequestName(),a);
    a = new InsertBankAction(); put(a.getRequestName(),a);
    a = new UpdateBankAction(); put(a.getRequestName(),a);
    a = new DeleteBanksAction(); put(a.getRequestName(),a);
    a = new ValidateBankCodeAction(); put(a.getRequestName(),a);

    a = new LoadItemTypesAction(); put(a.getRequestName(),a);
    a = new InsertItemTypeAction(); put(a.getRequestName(),a);
    a = new UpdateItemTypesAction(); put(a.getRequestName(),a);
    a = new DeleteItemTypeAction(); put(a.getRequestName(),a);
    a = new LoadItemsAction(); put(a.getRequestName(),a);
    a = new ValidateItemCodeAction(); put(a.getRequestName(),a);

    a = new LoadItemAction(); put(a.getRequestName(),a);
    a = new InsertItemAction(); put(a.getRequestName(),a);
    a = new UpdateItemAction(); put(a.getRequestName(),a);
    a = new DeleteItemsAction(); put(a.getRequestName(),a);

    a = new LoadCustomerAction(); put(a.getRequestName(),a);
    a = new InsertCustomerAction(); put(a.getRequestName(),a);
    a = new UpdateCustomerAction(); put(a.getRequestName(),a);
    a = new DeleteCustomersAction(); put(a.getRequestName(),a);
    a = new LoadCustomersAction(); put(a.getRequestName(),a);
    a = new ValidateCustomerCodeAction(); put(a.getRequestName(),a);

    a = new InsertPaymentTypesAction(); put(a.getRequestName(),a);
    a = new UpdatePaymentTypesAction(); put(a.getRequestName(),a);
    a = new DeletePaymentTypesAction(); put(a.getRequestName(),a);
    a = new LoadPaymentTypesAction(); put(a.getRequestName(),a);
    a = new ValidatePaymentTypeCodeAction(); put(a.getRequestName(),a);

    a = new InsertPaymentsAction(); put(a.getRequestName(),a);
    a = new DeletePaymentsAction(); put(a.getRequestName(),a);
    a = new LoadPaymentsAction(); put(a.getRequestName(),a);
    a = new ValidatePaymentCodeAction(); put(a.getRequestName(),a);
    a = new LoadPaymentInstalmentsAction(); put(a.getRequestName(),a);
    a = new UpdatePaymentInstalmentsAction(); put(a.getRequestName(),a);
    a = new UpdatePaymentsAction(); put(a.getRequestName(),a);

    a = new LoadReferencesAction(); put(a.getRequestName(),a);
    a = new InsertReferencesAction(); put(a.getRequestName(),a);
    a = new UpdateReferencesAction(); put(a.getRequestName(),a);
    a = new DeleteReferencesAction(); put(a.getRequestName(),a);

    a = new LoadSubjectHierarchiesAction(); put(a.getRequestName(),a);
    a = new InsertSubjectHierarchyAction(); put(a.getRequestName(),a);
    a = new UpdateSubjectHierarchiesAction(); put(a.getRequestName(),a);
    a = new DeleteSubjectHierarchyAction(); put(a.getRequestName(),a);
    a = new LoadHierarSubjectsAction(); put(a.getRequestName(),a);
    a = new InsertSubjectsLinksAction(); put(a.getRequestName(),a);
    a = new DeleteSubjectsLinksAction(); put(a.getRequestName(),a);
    a = new LoadSubjectHierarchyLevelsAction(); put(a.getRequestName(),a);
    a = new UpdateSubjectHierarchyLevelsAction(); put(a.getRequestName(),a);

    a = new LoadCustomerDiscountsAction(); put(a.getRequestName(),a);
    a = new InsertCustomerDiscountsAction(); put(a.getRequestName(),a);
    a = new UpdateCustomerDiscountsAction(); put(a.getRequestName(),a);
    a = new DeleteCustomerDiscountsAction(); put(a.getRequestName(),a);

    a = new LoadItemDiscountsAction(); put(a.getRequestName(),a);
    a = new InsertItemDiscountsAction(); put(a.getRequestName(),a);
    a = new UpdateItemDiscountsAction(); put(a.getRequestName(),a);
    a = new DeleteItemDiscountsAction(); put(a.getRequestName(),a);

    a = new LoadHierarItemDiscountsAction(); put(a.getRequestName(),a);
    a = new InsertHierarItemDiscountsAction(); put(a.getRequestName(),a);
    a = new UpdateHierarItemDiscountsAction(); put(a.getRequestName(),a);
    a = new DeleteHierarItemDiscountsAction(); put(a.getRequestName(),a);

    a = new LoadHierarCustomerDiscountsAction(); put(a.getRequestName(),a);
    a = new InsertHierarCustomerDiscountsAction(); put(a.getRequestName(),a);
    a = new UpdateHierarCustomerDiscountsAction(); put(a.getRequestName(),a);
    a = new DeleteHierarCustomerDiscountsAction(); put(a.getRequestName(),a);

    a = new LoadChargesAction(); put(a.getRequestName(),a);
    a = new InsertChargesAction(); put(a.getRequestName(),a);
    a = new UpdateChargesAction(); put(a.getRequestName(),a);
    a = new DeleteChargesAction(); put(a.getRequestName(),a);
    a = new ValidateChargeCodeAction(); put(a.getRequestName(),a);

    a = new LoadSaleActivitiesAction(); put(a.getRequestName(),a);
    a = new InsertSaleActivitiesAction(); put(a.getRequestName(),a);
    a = new UpdateSaleActivitiesAction(); put(a.getRequestName(),a);
    a = new DeleteSaleActivitiesAction(); put(a.getRequestName(),a);
    a = new ValidateSaleActivityCodeAction(); put(a.getRequestName(),a);

    a = new LoadPricelistAction(); put(a.getRequestName(),a);
    a = new InsertPricelistsAction(); put(a.getRequestName(),a);
    a = new UpdatePricelistsAction(); put(a.getRequestName(),a);
    a = new DeletePricelistAction(); put(a.getRequestName(),a);
    a = new ValidatePricelistCodeAction(); put(a.getRequestName(),a);

    a = new LoadPricesAction(); put(a.getRequestName(),a);
    a = new InsertPricesAction(); put(a.getRequestName(),a);
    a = new UpdatePricesAction(); put(a.getRequestName(),a);
    a = new DeletePricesAction(); put(a.getRequestName(),a);
    a = new ChangePricelistAction(); put(a.getRequestName(),a);
    a = new ImportAllItemsAction(); put(a.getRequestName(),a);

    a = new LoadDestinationsAction(); put(a.getRequestName(),a);
    a = new InsertDestinationsAction(); put(a.getRequestName(),a);
    a = new UpdateDestinationsAction(); put(a.getRequestName(),a);
    a = new DeleteDestinationsAction(); put(a.getRequestName(),a);
    a = new ValidateDestinationCodeAction(); put(a.getRequestName(),a);

    a = new LoadAgentTypesAction(); put(a.getRequestName(),a);
    a = new InsertAgentTypesAction(); put(a.getRequestName(),a);
    a = new UpdateAgentTypesAction(); put(a.getRequestName(),a);
    a = new DeleteAgentTypesAction(); put(a.getRequestName(),a);

    a = new LoadAgentsAction(); put(a.getRequestName(),a);
    a = new InsertAgentsAction(); put(a.getRequestName(),a);
    a = new UpdateAgentsAction(); put(a.getRequestName(),a);
    a = new DeleteAgentsAction(); put(a.getRequestName(),a);
    a = new ValidateAgentCodeAction(); put(a.getRequestName(),a);

    a = new LoadEmployeesAction(); put(a.getRequestName(),a);
    a = new InsertEmployeeAction(); put(a.getRequestName(),a);
    a = new UpdateEmployeeAction(); put(a.getRequestName(),a);
    a = new DeleteEmployeeAction(); put(a.getRequestName(),a);
    a = new ValidateEmployeeCodeAction(); put(a.getRequestName(),a);
    a = new LoadEmployeeAction(); put(a.getRequestName(),a);

    a = new LoadEmployeeCalendarAction(); put(a.getRequestName(),a);
    a = new InsertEmployeeCalendarsAction(); put(a.getRequestName(),a);
    a = new UpdateEmployeeCalendarsAction(); put(a.getRequestName(),a);
    a = new DeleteEmployeeCalendarsAction(); put(a.getRequestName(),a);

    a = new LoadSupplierAction(); put(a.getRequestName(),a);
    a = new InsertSupplierAction(); put(a.getRequestName(),a);
    a = new UpdateSupplierAction(); put(a.getRequestName(),a);
    a = new DeleteSuppliersAction(); put(a.getRequestName(),a);
    a = new LoadSuppliersAction(); put(a.getRequestName(),a);
    a = new ValidateSupplierCodeAction(); put(a.getRequestName(),a);

    a = new InsertSupplierItemsAction(); put(a.getRequestName(),a);
    a = new UpdateSupplierItemsAction(); put(a.getRequestName(),a);
    a = new DeleteSupplierItemsAction(); put(a.getRequestName(),a);
    a = new LoadSupplierItemsAction(); put(a.getRequestName(),a);
    a = new ValidateSupplierItemCodeAction(); put(a.getRequestName(),a);
    a = new ImportAllItemsToSupplierAction(); put(a.getRequestName(),a);

    a = new LoadSupplierPricelistAction(); put(a.getRequestName(),a);
    a = new InsertSupplierPricelistsAction(); put(a.getRequestName(),a);
    a = new UpdateSupplierPricelistsAction(); put(a.getRequestName(),a);
    a = new DeleteSupplierPricelistAction(); put(a.getRequestName(),a);
    a = new ValidateSupplierPricelistCodeAction(); put(a.getRequestName(),a);

    a = new LoadSupplierPricesAction(); put(a.getRequestName(),a);
    a = new InsertSupplierPricesAction(); put(a.getRequestName(),a);
    a = new UpdateSupplierPricesAction(); put(a.getRequestName(),a);
    a = new DeleteSupplierPricesAction(); put(a.getRequestName(),a);
    a = new ChangeSupplierPricelistAction(); put(a.getRequestName(),a);
    a = new ImportAllSupplierItemsAction(); put(a.getRequestName(),a);

    a = new LoadPurchaseDocAction(); put(a.getRequestName(),a);
    a = new InsertPurchaseDocAction(); put(a.getRequestName(),a);
    a = new UpdatePurchaseDocAction(); put(a.getRequestName(),a);
    a = new DeletePurchaseDocsAction(); put(a.getRequestName(),a);
    a = new LoadPurchaseDocsAction(); put(a.getRequestName(),a);
    a = new PurchaseDocTotalsAction(); put(a.getRequestName(),a);
    a = new ValidatePurchaseDocNumberAction(); put(a.getRequestName(),a);

    a = new LoadPurchaseDocRowAction(); put(a.getRequestName(),a);
    a = new InsertPurchaseDocRowAction(); put(a.getRequestName(),a);
    a = new UpdatePurchaseDocRowAction(); put(a.getRequestName(),a);
    a = new DeletePurchaseDocRowsAction(); put(a.getRequestName(),a);
    a = new LoadPurchaseDocRowsAction(); put(a.getRequestName(),a);
    a = new LoadSupplierPriceItemsAction(); put(a.getRequestName(),a);
    a = new ValidateSupplierPriceItemCodeAction(); put(a.getRequestName(),a);
    a = new ConfirmPurchaseOrderAction(); put(a.getRequestName(),a);
    a = new LoadPurchaseDocAndDelivNoteRowsAction(); put(a.getRequestName(),a);

    a = new LoadInDeliveryNotesAction(); put(a.getRequestName(),a);
    a = new InsertInDeliveryNoteAction(); put(a.getRequestName(),a);
    a = new UpdateInDeliveryNoteAction(); put(a.getRequestName(),a);
    a = new DeleteDeliveryNotesAction(); put(a.getRequestName(),a);
    a = new LoadInDeliveryNoteAction(); put(a.getRequestName(),a);
    a = new CloseDeliveryNoteAction(); put(a.getRequestName(),a);

    a = new InsertInDeliveryNoteRowAction(); put(a.getRequestName(),a);
    a = new UpdateInDeliveryNoteRowsAction(); put(a.getRequestName(),a);
    a = new DeleteInDeliveryNoteRowsAction(); put(a.getRequestName(),a);
    a = new LoadInDeliveryNoteRowsAction(); put(a.getRequestName(),a);

    a = new LoadItemAvailabilitiesAction(); put(a.getRequestName(),a);

    a = new LoadMotivesAction(); put(a.getRequestName(),a);
    a = new InsertMotiveAction(); put(a.getRequestName(),a);
    a = new UpdateMotivesAction(); put(a.getRequestName(),a);
    a = new DeleteMotivesAction(); put(a.getRequestName(),a);
    a = new ValidateMotiveCodeAction(); put(a.getRequestName(),a);

    a = new LoadMovementsAction(); put(a.getRequestName(),a);
    a = new LoadBookedItemsAction(); put(a.getRequestName(),a);
    a = new LoadOrderedItemsAction(); put(a.getRequestName(),a);

    a = new InsertManualMovementAction(); put(a.getRequestName(),a);

    a = new LoadSaleDocAction(); put(a.getRequestName(),a);
    a = new InsertSaleDocAction(); put(a.getRequestName(),a);
    a = new UpdateSaleDocAction(); put(a.getRequestName(),a);
    a = new DeleteSaleDocsAction(); put(a.getRequestName(),a);
    a = new LoadSaleDocsAction(); put(a.getRequestName(),a);
    a = new ValidateSaleDocNumberAction(); put(a.getRequestName(),a);

    a = new LoadSaleDocRowAction(); put(a.getRequestName(),a);
    a = new InsertSaleDocRowAction(); put(a.getRequestName(),a);
    a = new UpdateSaleDocRowAction(); put(a.getRequestName(),a);
    a = new DeleteSaleDocRowsAction(); put(a.getRequestName(),a);
    a = new LoadSaleDocRowsAction(); put(a.getRequestName(),a);
    a = new LoadPriceItemsAction(); put(a.getRequestName(),a);
    a = new ValidatePriceItemCodeAction(); put(a.getRequestName(),a);
    a = new ConfirmSaleDocAction(); put(a.getRequestName(),a);
    a = new LoadSaleDocAndDelivNoteRowsAction(); put(a.getRequestName(),a);
    a = new SaleItemTotalDiscountAction(); put(a.getRequestName(),a);
    a = new CloseSaleDocAction(); put(a.getRequestName(),a);

    a = new LoadSaleDocRowDiscountsAction(); put(a.getRequestName(),a);
    a = new InsertSaleDocRowDiscountsAction(); put(a.getRequestName(),a);
    a = new UpdateSaleDocRowDiscountsAction(); put(a.getRequestName(),a);
    a = new DeleteSaleDocRowDiscountsAction(); put(a.getRequestName(),a);
    a = new LoadSaleItemDiscountsAction(); put(a.getRequestName(),a);
    a = new ValidateSaleItemDiscountCodeAction(); put(a.getRequestName(),a);

    a = new LoadSaleDocDiscountsAction(); put(a.getRequestName(),a);
    a = new InsertSaleDocDiscountsAction(); put(a.getRequestName(),a);
    a = new UpdateSaleDocDiscountsAction(); put(a.getRequestName(),a);
    a = new DeleteSaleDocDiscountsAction(); put(a.getRequestName(),a);
    a = new LoadSaleHeaderDiscountsAction(); put(a.getRequestName(),a);
    a = new ValidateSaleHeaderDiscountCodeAction(); put(a.getRequestName(),a);

    a = new LoadSaleDocChargesAction(); put(a.getRequestName(),a);
    a = new InsertSaleDocChargesAction(); put(a.getRequestName(),a);
    a = new UpdateSaleDocChargesAction(); put(a.getRequestName(),a);
    a = new DeleteSaleDocChargesAction(); put(a.getRequestName(),a);

    a = new LoadSaleDocActivitiesAction(); put(a.getRequestName(),a);
    a = new InsertSaleDocActivitiesAction(); put(a.getRequestName(),a);
    a = new UpdateSaleDocActivitiesAction(); put(a.getRequestName(),a);
    a = new DeleteSaleDocActivitiesAction(); put(a.getRequestName(),a);

    a = new LoadOutDeliveryNotesAction(); put(a.getRequestName(),a);
    a = new InsertOutDeliveryNoteAction(); put(a.getRequestName(),a);
    a = new UpdateOutDeliveryNoteAction(); put(a.getRequestName(),a);
    a = new LoadOutDeliveryNoteAction(); put(a.getRequestName(),a);

    a = new InsertOutDeliveryNoteRowAction(); put(a.getRequestName(),a);
    a = new UpdateOutDeliveryNoteRowsAction(); put(a.getRequestName(),a);
    a = new DeleteOutDeliveryNoteRowsAction(); put(a.getRequestName(),a);
    a = new LoadOutDeliveryNoteRowsAction(); put(a.getRequestName(),a);

    a = new LoadUserParamAction(); put(a.getRequestName(),a);
    a = new SaveUserParamAction(); put(a.getRequestName(),a);
    a = new LoadUserParamsAction(); put(a.getRequestName(),a);
    a = new UpdateUserParamsAction(); put(a.getRequestName(),a);
    a = new CreateSaleDocFromEstimateAction(); put(a.getRequestName(),a);
    a = new CreateInvoiceFromSaleDocAction(); put(a.getRequestName(),a);

    a = new UpdateApplicationParamsAction(); put(a.getRequestName(),a);

    a = new LoadCompanyParamsAction(); put(a.getRequestName(),a);
    a = new UpdateCompanyParamsAction(); put(a.getRequestName(),a);

    a = new LoadExpirationsAction(); put(a.getRequestName(),a);
    a = new UpdateExpirationsAction(); put(a.getRequestName(),a);
    a = new LoadOutDeliveryNotesForSaleDocAction(); put(a.getRequestName(),a);
    a = new CreateInvoiceFromOutDelivNotesAction(); put(a.getRequestName(),a);

    a = new LoadContactAction(); put(a.getRequestName(),a);
    a = new LoadContactsAction(); put(a.getRequestName(),a);
    a = new InsertContactAction(); put(a.getRequestName(),a);
    a = new UpdateContactAction(); put(a.getRequestName(),a);
    a = new DeleteContactAction(); put(a.getRequestName(),a);

    a = new CreateInvoiceFromInDelivNotesAction(); put(a.getRequestName(),a);
    a = new CreateInvoiceFromPurchaseDocAction(); put(a.getRequestName(),a);
    a = new ClosePurchaseDocAction(); put(a.getRequestName(),a);
    a = new LoadInDeliveryNotesForPurchaseDocAction(); put(a.getRequestName(),a);

    a = new JasperReportAction(); put(a.getRequestName(),a);

    a = new LoadLedgerAction(); put(a.getRequestName(),a);
    a = new InsertLedgerAction(); put(a.getRequestName(),a);
    a = new UpdateLedgerAction(); put(a.getRequestName(),a);
    a = new DeleteLedgerAction(); put(a.getRequestName(),a);
    a = new ValidateLedgerCodeAction(); put(a.getRequestName(),a);

    a = new LoadAccountsAction(); put(a.getRequestName(),a);
    a = new InsertAccountsAction(); put(a.getRequestName(),a);
    a = new UpdateAccountsAction(); put(a.getRequestName(),a);
    a = new DeleteAccountsAction(); put(a.getRequestName(),a);
    a = new ValidateAccountCodeAction(); put(a.getRequestName(),a);

    a = new LoadAccountingMotivesAction(); put(a.getRequestName(),a);
    a = new InsertAccountingMotivesAction(); put(a.getRequestName(),a);
    a = new UpdateAccountingMotivesAction(); put(a.getRequestName(),a);
    a = new DeleteAccountingMotivesAction(); put(a.getRequestName(),a);
    a = new ValidateAccountingMotiveCodeAction(); put(a.getRequestName(),a);

    a = new LoadVatRegistersAction(); put(a.getRequestName(),a);
    a = new InsertVatRegistersAction(); put(a.getRequestName(),a);
    a = new UpdateVatRegistersAction(); put(a.getRequestName(),a);
    a = new DeleteVatRegistersAction(); put(a.getRequestName(),a);
    a = new ValidateVatRegisterCodeAction(); put(a.getRequestName(),a);

    a = new VatEndorseAction(); put(a.getRequestName(),a);
    a = new InsertJournalItemAction(); put(a.getRequestName(),a);
    a = new EndorseEAccountsAction(); put(a.getRequestName(),a);
    a = new ClosePAccountsAction(); put(a.getRequestName(),a);
    a = new OpenPAccountsAction(); put(a.getRequestName(),a);

    a = new LoadCustomizedReportsAction(); put(a.getRequestName(),a);
    a = new LoadReportFileNamesAction(); put(a.getRequestName(),a);
    a = new UpdateCustomReportsAction(); put(a.getRequestName(),a);
    a = new CheckReportFilesAction(); put(a.getRequestName(),a);
    a = new UploadReportFilesAction(); put(a.getRequestName(),a);

    a = new LoadScheduledActivitiesAction(); put(a.getRequestName(),a);
    a = new LoadScheduledActivityAction(); put(a.getRequestName(),a);
    a = new InsertScheduledActivityAction(); put(a.getRequestName(),a);
    a = new UpdateScheduledActivityAction(); put(a.getRequestName(),a);
    a = new DeleteScheduledActivitiesAction(); put(a.getRequestName(),a);

    a = new LoadCallOutTypesAction(); put(a.getRequestName(),a);
    a = new InsertCallOutTypeAction(); put(a.getRequestName(),a);
    a = new UpdateCallOutTypesAction(); put(a.getRequestName(),a);
    a = new DeleteCallOutTypeAction(); put(a.getRequestName(),a);
    a = new LoadCallOutsAction(); put(a.getRequestName(),a);
    a = new ValidateCallOutCodeAction(); put(a.getRequestName(),a);
		a = new CalloutRequestsReportAction(); put(a.getRequestName(),a);

    a = new LoadCallOutAction(); put(a.getRequestName(),a);
    a = new InsertCallOutAction(); put(a.getRequestName(),a);
    a = new UpdateCallOutAction(); put(a.getRequestName(),a);
    a = new DeleteCallOutsAction(); put(a.getRequestName(),a);

    a = new LoadCallOutTasksAction(); put(a.getRequestName(),a);
    a = new InsertCallOutTasksAction(); put(a.getRequestName(),a);
    a = new DeleteCallOutTasksAction(); put(a.getRequestName(),a);

    a = new LoadCallOutMachineriesAction(); put(a.getRequestName(),a);
    a = new InsertCallOutMachineriesAction(); put(a.getRequestName(),a);
    a = new DeleteCallOutMachineriesAction(); put(a.getRequestName(),a);

    a = new LoadCallOutItemsAction(); put(a.getRequestName(),a);
    a = new InsertCallOutItemsAction(); put(a.getRequestName(),a);
    a = new DeleteCallOutItemsAction(); put(a.getRequestName(),a);

    a = new LoadCallOutRequestsAction(); put(a.getRequestName(),a);
    a = new InsertCallOutRequestAction(); put(a.getRequestName(),a);
    a = new UpdateCallOutRequestAction(); put(a.getRequestName(),a);
    a = new DeleteCallOutRequestsAction(); put(a.getRequestName(),a);
    a = new LoadCallOutRequestAction(); put(a.getRequestName(),a);

    a = new LoadSubjectPerNameAction(); put(a.getRequestName(),a);
    a = new InsertSubjectAction(); put(a.getRequestName(),a);
    a = new UpdateSubjectAction(); put(a.getRequestName(),a);

    a = new LoadScheduledEmployeesAction(); put(a.getRequestName(),a);
    a = new InsertScheduledEmployeesAction(); put(a.getRequestName(),a);
    a = new DeleteScheduledEmployeesAction(); put(a.getRequestName(),a);
    a = new UpdateScheduledEmployeesAction(); put(a.getRequestName(),a);

    a = new LoadScheduledMachineriesAction(); put(a.getRequestName(),a);
    a = new InsertScheduledMachineriesAction(); put(a.getRequestName(),a);
    a = new DeleteScheduledMachineriesAction(); put(a.getRequestName(),a);
    a = new UpdateScheduledMachineriesAction(); put(a.getRequestName(),a);

    a = new LoadScheduledItemsAction(); put(a.getRequestName(),a);
    a = new InsertScheduledItemsAction(); put(a.getRequestName(),a);
    a = new DeleteScheduledItemsAction(); put(a.getRequestName(),a);
    a = new UpdateScheduledItemsAction(); put(a.getRequestName(),a);

    a = new LoadAttachedDocsAction(); put(a.getRequestName(),a);
    a = new InsertAttachedDocsAction(); put(a.getRequestName(),a);
    a = new DeleteAttachedDocsAction(); put(a.getRequestName(),a);

    a = new LinkScheduledActivityToCallOutRequestAction(); put(a.getRequestName(),a);

    a = new LoadMachineriesAction(); put(a.getRequestName(),a);
    a = new InsertMachineriesAction(); put(a.getRequestName(),a);
    a = new UpdateMachineriesAction(); put(a.getRequestName(),a);
    a = new DeleteMachineriesAction(); put(a.getRequestName(),a);
    a = new ValidateMachineryCodeAction(); put(a.getRequestName(),a);

    a = new CloseScheduledActivityAction(); put(a.getRequestName(),a);
    a = new CreateInvoiceFromScheduledActivityAction(); put(a.getRequestName(),a);
    a = new LoadEmployeeActivitiesAction(); put(a.getRequestName(),a);

    a = new LoadScheduledEmployeesOnGanttAction(); put(a.getRequestName(),a);
    a = new LoadEmployeeActivitiesOnGanttAction(); put(a.getRequestName(),a);

    a = new LoadDocumentTypesAction(); put(a.getRequestName(),a);
    a = new InsertDocumentTypeAction(); put(a.getRequestName(),a);
    a = new UpdateDocumentTypesAction(); put(a.getRequestName(),a);
    a = new DeleteDocumentTypeAction(); put(a.getRequestName(),a);
    a = new LoadDocumentsAction(); put(a.getRequestName(),a);
    a = new LoadDocumentAction(); put(a.getRequestName(),a);
    a = new InsertDocumentAction(); put(a.getRequestName(),a);
    a = new UpdateDocumentAction(); put(a.getRequestName(),a);
    a = new DeleteDocumentsAction(); put(a.getRequestName(),a);

    a = new LoadDocumentLinksAction(); put(a.getRequestName(),a);
    a = new LoadDocumentVersionsAction(); put(a.getRequestName(),a);
    a = new LoadDocumentVersionAction(); put(a.getRequestName(),a);
    a = new InsertDocumentLinksAction(); put(a.getRequestName(),a);
    a = new DeleteDocumentLinksAction(); put(a.getRequestName(),a);
    a = new DeleteDocumentVersionsAction(); put(a.getRequestName(),a);

    a = new LoadLevelPropertiesAction(); put(a.getRequestName(),a);
    a = new InsertLevelPropertiesAction(); put(a.getRequestName(),a);
    a = new UpdateLevelPropertiesAction(); put(a.getRequestName(),a);
    a = new DeleteLevelPropertiesAction(); put(a.getRequestName(),a);

    a = new LoadDocPropertiesAction(); put(a.getRequestName(),a);
    a = new UpdateDocPropertiesAction(); put(a.getRequestName(),a);

    a = new InsertWarehouseMotivesAction(); put(a.getRequestName(),a);

    a = new LoadItemAttachedDocsAction(); put(a.getRequestName(),a);
    a = new InsertItemAttachedDocsAction(); put(a.getRequestName(),a);
    a = new DeleteItemAttachedDocsAction(); put(a.getRequestName(),a);

    a = new DeleteComponentsAction(); put(a.getRequestName(),a);
    a = new InsertComponentsAction(); put(a.getRequestName(),a);
    a = new LoadBillsOfMaterialAction(); put(a.getRequestName(),a);
    a = new LoadComponentsAction(); put(a.getRequestName(),a);
    a = new UpdateComponentsAction(); put(a.getRequestName(),a);
    a = new LoadItemImplosionAction(); put(a.getRequestName(),a);

    a = new DeleteManufacturesAction(); put(a.getRequestName(),a);
    a = new DeleteManufacturePhasesAction(); put(a.getRequestName(),a);
    a = new InsertManufactureAction(); put(a.getRequestName(),a);
    a = new InsertManufacturePhasesAction(); put(a.getRequestName(),a);
    a = new LoadManufacturePhasesAction(); put(a.getRequestName(),a);
    a = new LoadManufacturesAction(); put(a.getRequestName(),a);
    a = new ValidateManufactureCodeAction(); put(a.getRequestName(),a);
    a = new UpdateManufactureAction(); put(a.getRequestName(),a);
    a = new UpdateManufacturePhasesAction(); put(a.getRequestName(),a);

    a = new DeleteOperationsAction(); put(a.getRequestName(),a);
    a = new InsertOperationsAction(); put(a.getRequestName(),a);
    a = new UpdateOperationsAction(); put(a.getRequestName(),a);
    a = new LoadOperationsAction(); put(a.getRequestName(),a);
    a = new ValidateOperationCodeAction(); put(a.getRequestName(),a);

    a = new CloseProdOrderAction(); put(a.getRequestName(),a);
    a = new ConfirmProdOrderAction(); put(a.getRequestName(),a);
    a = new CheckComponentsAvailabilityAction(); put(a.getRequestName(),a);
    a = new DeleteProdOrderProductsAction(); put(a.getRequestName(),a);
    a = new DeleteProdOrdersAction(); put(a.getRequestName(),a);
    a = new InsertProdOrderAction(); put(a.getRequestName(),a);
    a = new InsertProdOrderProductsAction(); put(a.getRequestName(),a);
    a = new LoadProdOrderAction(); put(a.getRequestName(),a);
    a = new LoadProdOrderProductsAction(); put(a.getRequestName(),a);
    a = new LoadProdOrdersAction(); put(a.getRequestName(),a);
    a = new UpdateProdOrderAction(); put(a.getRequestName(),a);
    a = new UpdateProdOrderProductsAction(); put(a.getRequestName(),a);
    a = new LoadProdOrderComponentsAction(); put(a.getRequestName(),a);
    a = new CreateBillOfMaterialsDataAction(); put(a.getRequestName(),a);
    a = new DeleteBillOfMaterialsDataAction(); put(a.getRequestName(),a);

    a = new DeleteAltComponentsAction(); put(a.getRequestName(),a);
    a = new InsertAltComponentsAction(); put(a.getRequestName(),a);
    a = new LoadAltComponentsAction(); put(a.getRequestName(),a);

    a = new LoadFunctionsAction(); put(a.getRequestName(),a);
    a = new UpdateFunctionAction(); put(a.getRequestName(),a);

    a = new DeleteTablesAction(); put(a.getRequestName(),a);
    a = new ExecuteQueryAction(); put(a.getRequestName(),a);
    a = new ExecuteStatementAction(); put(a.getRequestName(),a);
    a = new GetQueryInfoAction(); put(a.getRequestName(),a);
    a = new InsertTablesAction(); put(a.getRequestName(),a);
    a = new LoadEntitiesAction(); put(a.getRequestName(),a);
    a = new UpdateTablesAction(); put(a.getRequestName(),a);

    a = new LoadCustomColumnsAction(); put(a.getRequestName(),a);
    a = new UpdateCustomColumnsAction(); put(a.getRequestName(),a);
    a = new DeleteCustomFunctionsAction(); put(a.getRequestName(),a);
    a = new LoadCustomFunctionsAction(); put(a.getRequestName(),a);
    a = new LoadCustomFunctionAction(); put(a.getRequestName(),a);
    a = new InsertCustomFunctionAction(); put(a.getRequestName(),a);
    a = new UpdateCustomFunctionAction(); put(a.getRequestName(),a);
    a = new ExecuteValidateQueryAction(); put(a.getRequestName(),a);

    a = new LoadVariantsNamesAction(); put(a.getRequestName(),a);
    a = new LoadItemVariantsAction(); put(a.getRequestName(),a);
    a = new UpdateItemVariantsAction(); put(a.getRequestName(),a);

    a = new LoadVariantTypesAction(); put(a.getRequestName(),a);
    a = new InsertVariantTypesAction(); put(a.getRequestName(),a);
    a = new UpdateVariantTypesAction(); put(a.getRequestName(),a);
    a = new DeleteVariantTypesAction(); put(a.getRequestName(),a);

    a = new LoadVariantsAction(); put(a.getRequestName(),a);
    a = new InsertVariantsAction(); put(a.getRequestName(),a);
    a = new UpdateVariantsAction(); put(a.getRequestName(),a);
    a = new DeleteVariantsAction(); put(a.getRequestName(),a);

    a = new LoadProductVariantsMatrixAction(); put(a.getRequestName(),a);
    a = new InsertPurchaseDocRowsAction(); put(a.getRequestName(),a);
    a = new InsertSaleDocRowsAction(); put(a.getRequestName(),a);

    a = new InsertManualMovementsAction(); put(a.getRequestName(),a);

    a = new LoadStoredSerialNumbersAction(); put(a.getRequestName(),a);
    a = new ValidateStoredSerialNumberAction(); put(a.getRequestName(),a);
    a = new LoadVariantBarcodesAction(); put(a.getRequestName(),a);
    a = new UpdateVariantBarcodesAction(); put(a.getRequestName(),a);
    a = new ValidateVariantBarcodeAction(); put(a.getRequestName(),a);
    a = new LoadItemsSoldToOtherCustomersAction(); put(a.getRequestName(),a);
    a = new LoadOrderTrackingAction(); put(a.getRequestName(),a);

    a = new DeleteBarcodeLabelsDataAction(); put(a.getRequestName(),a);
    a = new CreateBarcodeLabelsDataAction(); put(a.getRequestName(),a);
    a = new CreateBarcodeLabelsDataFromPurchaseDocAction(); put(a.getRequestName(),a);

    a = new LoadVariantsPricesAction(); put(a.getRequestName(),a);
    a = new UpdateVariantsPricesAction(); put(a.getRequestName(),a);
    a = new LoadSupplierVariantsPricesAction(); put(a.getRequestName(),a);
    a = new UpdateSupplierVariantsPricesAction(); put(a.getRequestName(),a);
    a = new LoadVariantsPriceAction(); put(a.getRequestName(),a);

    a = new SalesPivotAction(); put(a.getRequestName(),a);

    a = new LoadTranslationsAction(); put(a.getRequestName(),a);
    a = new UpdateTranslationsAction(); put(a.getRequestName(),a);

    a = new UsersListAction(); put(a.getRequestName(),a);

    a = new LoadVariantMinStocksAction(); put(a.getRequestName(),a);
    a = new UpdateVariantMinStocksAction(); put(a.getRequestName(),a);

    a = new ReorderFromMinStocksAction(); put(a.getRequestName(),a);
    a = new CreatePurchaseOrdersAction(); put(a.getRequestName(),a);
    a = new CreateABCAction(); put(a.getRequestName(),a);
    a = new LoadABCAction(); put(a.getRequestName(),a);
    a = new DeleteABCAction(); put(a.getRequestName(),a);
    a = new UpdateMinStocksAction(); put(a.getRequestName(),a);

    a = new DeleteETLProcessesAction(); put(a.getRequestName(),a);
    a = new InsertETLProcessAction(); put(a.getRequestName(),a);
    a = new LoadETLProcessAction(); put(a.getRequestName(),a);
    a = new LoadETLProcessesAction(); put(a.getRequestName(),a);
    a = new LoadSelectableFieldsAction(); put(a.getRequestName(),a);
    a = new GetFolderContentAction(); put(a.getRequestName(),a);
    a = new StartETLProcessAction(); put(a.getRequestName(),a);

    a = new DbGridPermissionsAction(); put(a.getRequestName(),a);
    a = new LoadGridPermissionsPerRoleAction(); put(a.getRequestName(),a);
    a = new UpdateGridPermissionsPerRoleAction(); put(a.getRequestName(),a);

    a = new SaleDocTotalsAction(); put(a.getRequestName(),a);

		a = new ImportInventoryItemsAction(); put(a.getRequestName(),a);
		a = new DeleteInventoriesAction(); put(a.getRequestName(),a);
		a = new InsertInventoryAction(); put(a.getRequestName(),a);
		a = new LoadInventoriesAction(); put(a.getRequestName(),a);
		a = new LoadInventoryItemsAction(); put(a.getRequestName(),a);
		a = new UpdateInventoriesAction(); put(a.getRequestName(),a);
		a = new LoadLeavesAction(); put(a.getRequestName(),a);

		a = new DeleteInventoryItemsAction(); put(a.getRequestName(),a);
		a = new InsertInventoryItemAction(); put(a.getRequestName(),a);
		a = new UpdateInventoryItemsAction(); put(a.getRequestName(),a);
		a = new CloseInventoryAction(); put(a.getRequestName(),a);
		a = new ConfirmInventoryAction(); put(a.getRequestName(),a);

		a = new SalesReportAction(); put(a.getRequestName(),a);

		a = new GetBeansFactoryNameAction(); put(a.getRequestName(),a);

		a = new LoadItemVariantsAction(); put(a.getRequestName(),a);

		a = new ValidateSheetCodeAction(); put(a.getRequestName(),a);
		a = new InsertItemSheetAction(); put(a.getRequestName(),a);
		a = new UpdateItemSheetsAction(); put(a.getRequestName(),a);
		a = new DeleteItemSheetsAction(); put(a.getRequestName(),a);
		a = new InsertItemSheetLevelsAction(); put(a.getRequestName(),a);
		a = new InsertSheetSparePartsAction(); put(a.getRequestName(),a);
		a = new DeleteSheetSparePartsAction	(); put(a.getRequestName(),a);
		a = new DeleteSheetAttachedDocsAction(); put(a.getRequestName(),a);
		a = new InsertSheetAttachedDocsAction(); put(a.getRequestName(),a);
		a = new InsertChildrenSheetsAction(); put(a.getRequestName(),a);
		a = new DeleteChildrenSheetsAction(); put(a.getRequestName(),a);
		a = new LoadItemSheetsAction(); put(a.getRequestName(),a);
		a = new LoadItemSparePartsAction(); put(a.getRequestName(),a);
		a = new LoadItemSheetImageAction(); put(a.getRequestName(),a);
		a = new LoadItemSheetLevelsAction(); put(a.getRequestName(),a);
		a = new LoadSheetAttachedDocsAction(); put(a.getRequestName(),a);
		a = new LoadSheetSparePartsAction(); put(a.getRequestName(),a);
		a = new UpdateSubsheetAction(); put(a.getRequestName(),a);
		a = new DeleteItemSheetLevelsAction(); put(a.getRequestName(),a);
		a = new UpdateItemSheetLevelsAction(); put(a.getRequestName(),a);

		a = new PayImmediatelyAction(); put(a.getRequestName(),a);

		a = new LoadExpirationPaymentsAction(); put(a.getRequestName(),a);
		a = new LoadPaymentDistributionsAction(); put(a.getRequestName(),a);
		a = new InsertPaymentAction(); put(a.getRequestName(),a);

    put("changeLanguage",new Action() {

      public String getRequestName() {
        return "changeLanguage";
      }

      public Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
        PreparedStatement pstmt = null;
        Connection conn = null;
        try {

          conn = ConnectionManager.getConnection(context);
          pstmt = conn.prepareStatement(
              "select LANGUAGE_CODE from SYS09_LANGUAGES where "+
              "CLIENT_LANGUAGE_CODE=? "
          );
          pstmt.setString(1,inputPar.toString());
          ResultSet rset = pstmt.executeQuery();
          if (rset.next()) {
            ((JAIOUserSessionParameters)userSessionPars).setServerLanguageId(rset.getString(1));
          }
          rset.close();

          userSessionPars.setLanguageId(inputPar.toString());
          return new VOResponse(Boolean.TRUE);

        } catch (Exception ex1) {
          ex1.printStackTrace();
          return new ErrorResponse(ex1.getMessage());
        } finally {
          try {
            pstmt.close();
          }
          catch (Exception ex) {
          }
          try {
            ConnectionManager.releaseConnection(conn,context);
          }
          catch (Exception ex2) {
          }
        }
      }

    });

  }





}
