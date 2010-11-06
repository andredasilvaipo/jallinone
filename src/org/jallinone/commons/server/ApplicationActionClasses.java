package org.jallinone.commons.server;

import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.accounting.accountingmotives.server.*;
import org.jallinone.accounting.accounts.server.*;
import org.jallinone.accounting.ledger.server.*;
import org.jallinone.accounting.movements.server.*;
import org.jallinone.accounting.vatregisters.server.*;
import org.jallinone.contacts.server.*;
import org.jallinone.documents.server.*;
import org.jallinone.employees.server.*;
import org.jallinone.expirations.server.*;
import org.jallinone.hierarchies.server.*;
import org.jallinone.items.server.*;
import org.jallinone.ordertracking.server.*;
import org.jallinone.production.billsofmaterial.server.*;
import org.jallinone.production.machineries.server.*;
import org.jallinone.production.manufactures.server.*;
import org.jallinone.production.orders.server.*;
import org.jallinone.purchases.documents.server.*;
import org.jallinone.purchases.items.server.*;
import org.jallinone.purchases.pricelist.server.*;
import org.jallinone.purchases.suppliers.server.*;
import org.jallinone.registers.bank.server.*;
import org.jallinone.registers.carrier.server.*;
import org.jallinone.registers.color.server.*;
import org.jallinone.registers.currency.server.*;
import org.jallinone.registers.measure.server.*;
import org.jallinone.registers.payments.server.*;
import org.jallinone.registers.size.server.*;
import org.jallinone.registers.task.server.*;
import org.jallinone.registers.transportmotives.server.*;
import org.jallinone.registers.vat.server.*;
import org.jallinone.reports.server.*;
import org.jallinone.sales.activities.server.*;
import org.jallinone.sales.agents.server.*;
import org.jallinone.sales.charges.server.*;
import org.jallinone.sales.customers.server.*;
import org.jallinone.sales.destinations.server.*;
import org.jallinone.sales.discounts.server.*;
import org.jallinone.sales.documents.activities.server.*;
import org.jallinone.sales.documents.headercharges.server.*;
import org.jallinone.sales.documents.headerdiscounts.server.*;
import org.jallinone.sales.documents.itemdiscounts.server.*;
import org.jallinone.sales.documents.server.*;
import org.jallinone.sales.pricelist.server.*;
import org.jallinone.sales.reports.server.*;
import org.jallinone.scheduler.activities.server.*;
import org.jallinone.scheduler.callouts.server.*;
import org.jallinone.scheduler.gantt.server.*;
import org.jallinone.sqltool.server.*;
import org.jallinone.startup.server.*;
import org.jallinone.subjects.server.*;
import org.jallinone.system.companies.server.*;
import org.jallinone.system.customizations.server.*;
import org.jallinone.system.languages.server.*;
import org.jallinone.system.permissions.server.*;
import org.jallinone.system.server.*;
import org.jallinone.variants.server.*;
import org.jallinone.warehouse.availability.server.*;
import org.jallinone.warehouse.documents.server.*;
import org.jallinone.warehouse.movements.server.*;
import org.jallinone.warehouse.server.*;
import org.jallinone.warehouse.tables.motives.server.*;
import org.jallinone.warehouse.tables.movements.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.purchases.items.server.*;
import org.jallinone.system.importdata.server.*;
import org.jallinone.system.gridmanager.server.*;
import org.jallinone.system.permissions.server.LoadGridPermissionsPerRoleAction;


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

    a = new LoadColorsAction(); put(a.getRequestName(),a);
    a = new InsertColorsAction(); put(a.getRequestName(),a);
    a = new UpdateColorsAction(); put(a.getRequestName(),a);
    a = new DeleteColorsAction(); put(a.getRequestName(),a);
    a = new ValidateColorCodeAction(); put(a.getRequestName(),a);

    a = new LoadSizesAction(); put(a.getRequestName(),a);
    a = new InsertSizesAction(); put(a.getRequestName(),a);
    a = new UpdateSizesAction(); put(a.getRequestName(),a);
    a = new DeleteSizesAction(); put(a.getRequestName(),a);
    a = new ValidateSizeCodeAction(); put(a.getRequestName(),a);

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
