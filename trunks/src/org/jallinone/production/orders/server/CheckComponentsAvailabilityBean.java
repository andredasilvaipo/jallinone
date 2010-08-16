package org.jallinone.production.orders.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.production.orders.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.production.billsofmaterial.server.LoadBillOfMaterialBean;
import org.jallinone.items.java.ItemPK;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import org.jallinone.production.billsofmaterial.java.MaterialVO;
import org.jallinone.warehouse.availability.server.LoadItemAvailabilitiesAction;
import org.jallinone.warehouse.availability.java.ItemAvailabilityVO;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.warehouse.movements.server.AddMovementBean;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.jallinone.production.billsofmaterial.server.LoadAltComponentsAction;
import org.jallinone.production.billsofmaterial.java.AltComponentVO;
import org.jallinone.registers.measure.server.MeasureConvBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to check if all components are available in the specified warehouse.</p>
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
public class CheckComponentsAvailabilityBean {

  private LoadBillOfMaterialBean bean = new LoadBillOfMaterialBean();
  private LoadItemAvailabilitiesAction avail = new LoadItemAvailabilitiesAction();
  private LoadAltComponentsAction altComps = new LoadAltComponentsAction();
  private MeasureConvBean conv = new MeasureConvBean();


  public CheckComponentsAvailabilityBean() {
  }


  /**
   * Check if components required by specified products are available.
   * @param products list of ProdOrderProductVO objects
   * @params compAltComps collection of <component item code,HashSet of alternative component item codes>; filled by this method (and given back by reference)
   * @return VOListResponse of ProdOrderComponentVO objects
   */
  public final Response checkComponentsAvailability(Connection conn,Hashtable compAltComps,ArrayList products,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    try {

      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      if (products.size()==0) {
        return new VOListResponse(new ArrayList(),false,0);
      }


      // fill in comps hashtable with the collection of required components...
      ItemPK pk = null;
      ProdOrderProductVO prodVO = null;
      ArrayList components = null;
      MaterialVO compVO = null;
      Response res = null;
      ProdOrderComponentVO componentVO = null;
      Hashtable comps = new Hashtable(); // collection of <component item code,ProdOrderComponentVO object>
      for(int i=0;i<products.size();i++) {
        // retrieve bill of materials for each product...
        prodVO = (ProdOrderProductVO)products.get(i);
        pk = new ItemPK(prodVO.getCompanyCodeSys01DOC23(),prodVO.getItemCodeItm01DOC23());
        res = bean.getBillOfMaterials(conn,pk,userSessionPars,request,response,userSession,context);
        if (res.isError()) {
          return res;
        }

        // extract components only (leaf nodes)...
        components = getComponents((DefaultMutableTreeNode) ((TreeModel)((VOResponse)res).getVo()).getRoot() );
        for(int j=0;j<components.size();j++) {
          compVO = (MaterialVO)components.get(j);
          componentVO = (ProdOrderComponentVO)comps.get(compVO.getItemCodeItm01ITM03());
          if (componentVO==null) {
            componentVO = new ProdOrderComponentVO();
            comps.put(compVO.getItemCodeItm01ITM03(),componentVO);
            componentVO.setAvailableQty(new BigDecimal(0));
            componentVO.setCompanyCodeSys01DOC24(compVO.getCompanyCodeSys01ITM03());
            componentVO.setDescriptionSYS10(compVO.getDescriptionSYS10());
            componentVO.setDocNumberDOC24(prodVO.getDocNumberDOC23());
            componentVO.setDocYearDOC24(prodVO.getDocYearDOC23());
            componentVO.setItemCodeItm01DOC24(compVO.getItemCodeItm01ITM03());
            componentVO.setMinSellingQtyUmCodeReg02ITM01(compVO.getMinSellingQtyUmCodeReg02ITM01());
            componentVO.setQtyDOC24(new BigDecimal(0));
          }
          componentVO.setQtyDOC24(componentVO.getQtyDOC24().add(compVO.getQtyITM03().multiply(prodVO.getQtyDOC23())));
        }
      }


      // check components availability in the specified warehouse...
      Enumeration en = comps.keys();
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,prodVO.getCompanyCodeSys01DOC23());
      gridParams.getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,prodVO.getWarehouseCodeWar01DOC22());
      gridParams.getOtherGridParams().put(ApplicationConsts.LOAD_ALL,Boolean.TRUE);
      ItemAvailabilityVO availVO = null;
      BigDecimal availability,altAvailability,delta;
      String itemCode = null;
      ArrayList list,availList;
      AltComponentVO altVO = null;
      ArrayList alternativeComps = new ArrayList();
      ArrayList compsToRemove = new ArrayList();
      ProdOrderComponentVO altComponentVO = null;
      HashSet altCodes = null; // list of alternative component item codes...
      BigDecimal altQty = null;
      while(en.hasMoreElements()) {
        itemCode = en.nextElement().toString();
        componentVO = (ProdOrderComponentVO)comps.get(itemCode);

        gridParams.getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(prodVO.getCompanyCodeSys01DOC23(),itemCode));
        res = avail.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
        if (res.isError())
          return res;

        availList = new ArrayList(((VOListResponse)res).getRows());
        componentVO.setAvailabilities(availList);
        availability = new BigDecimal(0);
        for(int i=0;i<availList.size();i++) {
          availVO = (ItemAvailabilityVO)availList.get(i);
          availability = availability.add(availVO.getAvailableQtyWAR03());
        }
        componentVO.setAvailableQty(availability);

        if (componentVO.getQtyDOC24().doubleValue()>componentVO.getAvailableQty().doubleValue()) {
          // check if there exist some alternative component...
          res = altComps.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
          if (res.isError())
            return res;
          list = new ArrayList(((VOListResponse)res).getRows());
          for(int i=0;i<list.size();i++) {
            altVO = (AltComponentVO)list.get(i);
            gridParams.getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(prodVO.getCompanyCodeSys01DOC23(),altVO.getItemCodeItm01ITM04()));
            res = avail.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
            if (res.isError())
              return res;
            availList = new ArrayList(((VOListResponse)res).getRows());
            altAvailability = new BigDecimal(0);
            for(int j=0;j<availList.size();j++) {
              availVO = (ItemAvailabilityVO)availList.get(j);
              altAvailability = altAvailability.add(availVO.getAvailableQtyWAR03());
            }
            if (altAvailability.doubleValue()>0) {
              altComponentVO = new ProdOrderComponentVO();
              altComponentVO.setAvailabilities(availList);
              altComponentVO.setAvailableQty(altAvailability);
              altComponentVO.setCompanyCodeSys01DOC24(altVO.getCompanyCodeSys01ITM04());
              altComponentVO.setDescriptionSYS10(altVO.getDescriptionSYS10());
              altComponentVO.setDocNumberDOC24(prodVO.getDocNumberDOC23());
              altComponentVO.setDocYearDOC24(prodVO.getDocYearDOC23());
              altComponentVO.setItemCodeItm01DOC24(altVO.getItemCodeItm01ITM04());
              altComponentVO.setMinSellingQtyUmCodeReg02ITM01(altVO.getMinSellingQtyUmCodeReg02ITM01());
              altQty = conv.convertQty(
                altVO.getMinSellingQtyUmCodeReg02ITM01(),
                componentVO.getMinSellingQtyUmCodeReg02ITM01(),
                altAvailability,
                userSessionPars,
                request,
                response,
                userSession,
                context
              );
              if (componentVO.getQtyDOC24().subtract(availability).doubleValue()>altQty.doubleValue()) {
                delta = altQty;
                altComponentVO.setQtyDOC24(altAvailability);
              }
              else {
                delta = componentVO.getQtyDOC24();
                altComponentVO.setQtyDOC24(
                  conv.convertQty(
                   componentVO.getMinSellingQtyUmCodeReg02ITM01(),
                   altVO.getMinSellingQtyUmCodeReg02ITM01(),
                   delta,
                   userSessionPars,
                   request,
                   response,
                   userSession,
                   context
                  )
                );
              }
              componentVO.setQtyDOC24(componentVO.getQtyDOC24().subtract(delta));
              alternativeComps.add(altComponentVO);

              altCodes = (HashSet)compAltComps.get(itemCode);
              if (altCodes==null) {
                altCodes = new HashSet();
                compAltComps.put(itemCode,altCodes);
              }
              altCodes.add(altVO.getItemCodeItm01ITM04());

              if (componentVO.getQtyDOC24().doubleValue()==0) {
                compsToRemove.add(componentVO);
                break;
              }
              if (componentVO.getQtyDOC24().subtract(availability).doubleValue()==0)
                break;
            }
          }
        }
      }

      list = new ArrayList(comps.values());
      list.addAll(alternativeComps);
      list.removeAll(compsToRemove);
      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"checkComponentsAvailability","Error while retrieving components availability for the specified production order",ex);
      return new ErrorResponse(ex.getMessage());
    }
  }


  /**
   * Expand the current node and return the list of leaves (MaterialVO objects).
   */
  private ArrayList getComponents(DefaultMutableTreeNode node) {
    ArrayList list = new ArrayList();
    for(int i=0;i<node.getChildCount();i++)
      list.addAll(getComponents((DefaultMutableTreeNode)node.getChildAt(i)));
    if (node.isLeaf())
      list.add(node.getUserObject());
    return list;
  }



}
