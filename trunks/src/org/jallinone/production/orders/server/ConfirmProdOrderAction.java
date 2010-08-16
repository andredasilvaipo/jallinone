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
import org.jallinone.items.server.LoadItemAction;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.production.manufactures.server.LoadManufacturePhasesAction;
import org.jallinone.production.manufactures.java.ManufacturePhaseVO;
import org.jallinone.production.manufactures.java.ManufactureVO;
import org.jallinone.registers.measure.server.MeasureConvBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to confirm a production order.
 * Confirmation activity involves the following actions:
 * - fill in DOC24 table
 * - check if all components are available in the specified warehouse
 * - remove all required components from the specified warehouse
 * - calculate doc sequence
 * </p>
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
public class ConfirmProdOrderAction implements Action {

  private LoadProdOrderProductsAction rowsAction = new LoadProdOrderProductsAction();
  private AddMovementBean mov = new AddMovementBean();
  private CheckComponentsAvailabilityBean check = new CheckComponentsAvailabilityBean();
  private LoadBillOfMaterialBean bill = new LoadBillOfMaterialBean();
  private LoadItemAction item = new LoadItemAction();
  private LoadManufacturePhasesAction ops = new LoadManufacturePhasesAction();
  private MeasureConvBean conv = new MeasureConvBean();


  public ConfirmProdOrderAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "confirmProdOrder";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    ResultSet rset = null;
    try {
      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      conn = ConnectionManager.getConnection(context);

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        null
      ));



      // retrieve products defined in the specified production order...
      DetailProdOrderVO vo = (DetailProdOrderVO)inputPar;
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.PROD_ORDER_PK,new ProdOrderPK(vo.getCompanyCodeSys01DOC22(),vo.getDocYearDOC22(),vo.getDocNumberDOC22()));
      Response res = rowsAction.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
      if (res.isError())
        return res;
      ArrayList products = new ArrayList(((VOListResponse)res).getRows());
      Hashtable compAltCodes = new Hashtable(); // collection of <component item code,HashSet of alternative component item codes>

      // retrieve components availabilities required in the specified production order...
      res = check.checkComponentsAvailability(conn,compAltCodes,products,userSessionPars,request,response,userSession,context);
      if (res.isError())
        return res;
      ArrayList components = new ArrayList(((VOListResponse)res).getRows());


      // remove all components previously saved in DOC24 for the specified production order...
      pstmt = conn.prepareStatement(
        "delete from DOC24_PRODUCTION_COMPONENTS where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?"
      );
      pstmt.setString(1, vo.getCompanyCodeSys01DOC22());
      pstmt.setBigDecimal(2, vo.getDocYearDOC22());
      pstmt.setBigDecimal(3, vo.getDocNumberDOC22());
      pstmt.execute();
      pstmt.close();


      // generate progressive for doc. sequence...
      pstmt = conn.prepareStatement(
        "select max(DOC_SEQUENCE) from DOC22_PRODUCTION_ORDER where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_SEQUENCE is not null"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01DOC22());
      pstmt.setBigDecimal(2,vo.getDocYearDOC22());
      rset = pstmt.executeQuery();
      int docSequenceDOC22 = 1;
      if (rset.next())
        docSequenceDOC22 = rset.getInt(1)+1;
      vo.setDocSequenceDOC22(new BigDecimal(docSequenceDOC22));
      rset.close();


      // update order state...
      pstmt = conn.prepareStatement("update DOC22_PRODUCTION_ORDER set DOC_STATE=?,DOC_SEQUENCE=? where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.CONFIRMED);
      pstmt.setInt(2,docSequenceDOC22);
      pstmt.setString(3,vo.getCompanyCodeSys01DOC22());
      pstmt.setBigDecimal(4,vo.getDocYearDOC22());
      pstmt.setBigDecimal(5,vo.getDocNumberDOC22());
      pstmt.execute();
      pstmt.close();


      // insert components in DOC24 for the specified production order...
      pstmt2 = conn.prepareStatement(
        "insert into DOC24_PRODUCTION_COMPONENTS(COMPANY_CODE_SYS01,DOC_YEAR,DOC_NUMBER,ITEM_CODE_ITM01,QTY,PROGRESSIVE_HIE01) values(?,?,?,?,?,?)"
      );
      ProdOrderComponentVO compVO = null;
      ProdOrderComponentVO auxCompVO = null;


      // check components availability in the specified warehouse and remove components from it...
      ItemAvailabilityVO availVO = null;
      java.util.List list = null;
      BigDecimal availability = new BigDecimal(0);
      BigDecimal qty,delta;
      WarehouseMovementVO movVO = null;
      int i;
      ArrayList serialNumbers = new ArrayList();
      for(int j=0;j<components.size();j++) {
        compVO = (ProdOrderComponentVO)components.get(j);
        qty = compVO.getQtyDOC24();
        list = compVO.getAvailabilities();
        availability = new BigDecimal(0);
        for(i=0;i<list.size();i++) {
          availVO = (ItemAvailabilityVO)list.get(i);
          availability = availability.add(availVO.getAvailableQtyWAR03());
        }
        if (availability.doubleValue()<qty.doubleValue()) {
          conn.rollback();
          return new ErrorResponse(
            compVO.getItemCodeItm01DOC24()+" "+
            resources.getResource("component is not available in the specified warehouse")+".\n"+
            resources.getResource("found")+": "+availability.doubleValue()+" "+
            resources.getResource("required")+": "+qty.doubleValue()
          );
        }
        // the current component is available: it will be removed...
        i=0;
        while(qty.doubleValue()>0) {
          availVO = (ItemAvailabilityVO)list.get(i);
          if (qty.doubleValue()>availVO.getAvailableQtyWAR03().doubleValue()) {
            delta = availVO.getAvailableQtyWAR03();
            qty = qty.subtract(delta);
          }
          else {
            delta = qty;
            qty = new BigDecimal(0);
          }

          // insert record in DOC24...
          compVO.setProgressiveHie01DOC24(availVO.getProgressiveHie01WAR03());
          compVO.setLocationDescriptionSYS10(availVO.getLocationDescriptionSYS10());
          pstmt2.setString(1, vo.getCompanyCodeSys01DOC22());
          pstmt2.setBigDecimal(2, vo.getDocYearDOC22());
          pstmt2.setBigDecimal(3, vo.getDocNumberDOC22());
          pstmt2.setString(4, compVO.getItemCodeItm01DOC24());
          pstmt2.setBigDecimal(5, delta);
          pstmt2.setBigDecimal(6, compVO.getProgressiveHie01DOC24());
          pstmt2.execute();

          // create a warehouse movement...
          movVO = new WarehouseMovementVO(
            availVO.getProgressiveHie01WAR03(),
            delta,
            vo.getCompanyCodeSys01DOC22(),
            vo.getWarehouseCodeWar01DOC22(),
            compVO.getItemCodeItm01DOC24(),
            ApplicationConsts.WAREHOUSE_MOTIVE_UNLOAD_BY_PRODUCTION,
            ApplicationConsts.ITEM_GOOD,
            resources.getResource("unload items from production order")+" "+vo.getDocSequenceDOC22()+"/"+vo.getDocYearDOC22(),
            serialNumbers,

            compVO.getVariantCodeItm11DOC24(),
            compVO.getVariantCodeItm12DOC24(),
            compVO.getVariantCodeItm13DOC24(),
            compVO.getVariantCodeItm14DOC24(),
            compVO.getVariantCodeItm15DOC24(),
            compVO.getVariantTypeItm06DOC24(),
            compVO.getVariantTypeItm07DOC24(),
            compVO.getVariantTypeItm08DOC24(),
            compVO.getVariantTypeItm09DOC24(),
            compVO.getVariantTypeItm10DOC24()

          );
          res = mov.addWarehouseMovement(conn,movVO,userSessionPars,request,response,userSession,context);
          if (res.isError()) {
            conn.rollback();
            return res;
          }

          i++;
        }

      }
      rset.close();
      pstmt2.close();

      Hashtable usedComponents = new Hashtable();
      Hashtable usedComponentsVO = new Hashtable();
      for(int j=0;j<components.size();j++) {
        compVO = (ProdOrderComponentVO)components.get(j);
        usedComponents.put(compVO.getItemCodeItm01DOC24(),compVO.getQtyDOC24());
        usedComponentsVO.put(compVO.getItemCodeItm01DOC24(),compVO);
      }
      res = insertProductComponents(conn,vo,products,usedComponents,usedComponentsVO,compAltCodes,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }

      Response answer = new VOResponse(new BigDecimal(docSequenceDOC22));

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while confirming a production order",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        if (rset != null) {
          rset.close();
        }
      }
      catch (Exception ex4) {
      }
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex2) {
      }

      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * Insert records into DOC25/DOC26 tables.
   */
  private Response insertProductComponents(Connection conn,DetailProdOrderVO prodOrderVO,ArrayList products,Hashtable usedComponents,Hashtable usedComponentsVO,Hashtable compAltCodes,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) throws Exception {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    try {
      pstmt = conn.prepareStatement(
      "insert into DOC25_PRODUCTION_PROD_COMPS(COMPANY_CODE_SYS01,DOC_YEAR,DOC_NUMBER,PRODUCT_ITEM_CODE_ITM01,COMPONENT_ITEM_CODE_ITM01,QTY,SEQUENCE) values(?,?,?,?,?,?,?)"
      );
      pstmt2 = conn.prepareStatement(
        "insert into DOC26_PRODUCTION_OPERATIONS(COMPANY_CODE_SYS01,DOC_YEAR,DOC_NUMBER,ITEM_CODE_ITM01,PHASE_NUMBER,"+
        "OPERATION_CODE,OPERATION_DESCRIPTION,VALUE,DURATION,MANUFACTURE_TYPE,COMPLETION_PERC,QTY,TASK_CODE,TASK_DESCRIPTION,"+
        "MACHINERY_CODE,MACHINERY_DESCRIPTION,SUBST_OPERATION_CODE,SUBST_OPERATION_DESCRIPTION,NOTE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
      );

      ProdOrderProductVO prodVO = null;
      Response res = null;
      TreeModel model = null;
      DefaultMutableTreeNode root;
      int sequence = 0;
      HashSet subProductsAlreadyAdded = new HashSet();
      for(int i=0;i<products.size();i++) {
        prodVO = (ProdOrderProductVO)products.get(i);
        // retrieve bill of materials for each product...
        res = bill.getBillOfMaterials(conn,new ItemPK(prodVO.getCompanyCodeSys01DOC23(),prodVO.getItemCodeItm01DOC23()),userSessionPars,request,response,userSession,context);
        if (res.isError())
          return res;
        model = (TreeModel)((VOResponse)res).getVo();
        root = (DefaultMutableTreeNode)model.getRoot();

        // expand nodes to retrieve sun-products and fill in DOC25/DOC26...
        sequence = expandNode(sequence,prodOrderVO,root,usedComponents,usedComponentsVO,compAltCodes,subProductsAlreadyAdded,pstmt,pstmt2,userSessionPars,request,response,userSession,context);
      }
      return new VOResponse(Boolean.TRUE);
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex1) {
      }
    }
  }


  /**
   * Expand the current (sub)product node.
   */
  private int expandNode(int sequence,DetailProdOrderVO prodOrderVO,DefaultMutableTreeNode node,Hashtable usedComponents,Hashtable usedComponentsVO,Hashtable compAltCodes,HashSet subProductsAlreadyAdded,PreparedStatement pstmt,PreparedStatement pstmt2,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) throws Exception {
    DefaultMutableTreeNode childNode = null;
    MaterialVO vo = null;
    BigDecimal qty = null;
    Iterator it = null;
    BigDecimal delta = null;
    String itemCode = null;
    MaterialVO prodVO = (MaterialVO)node.getUserObject();

    for(int i=0;i<node.getChildCount();i++) {
      childNode = (DefaultMutableTreeNode)node.getChildAt(i);
      if (!childNode.isLeaf()) {
        // the component is a sub-product...
        sequence = expandNode(sequence,prodOrderVO,childNode,usedComponents,usedComponentsVO,compAltCodes,subProductsAlreadyAdded,pstmt,pstmt2,userSessionPars,request,response,userSession,context);
      }
    }

    BigDecimal altQty = null;
    for(int i=0;i<node.getChildCount();i++) {
      childNode = (DefaultMutableTreeNode)node.getChildAt(i);
      vo = (MaterialVO)childNode.getUserObject();
      if (childNode.isLeaf()) {
        // component found: check if there must be used an alternative component instead of the current one...
        qty = (BigDecimal)usedComponents.get(vo.getItemCodeItm01ITM03());
        if (qty!=null && qty.doubleValue()>0) {
          if (vo.getQtyITM03().doubleValue()<=qty.doubleValue())
            delta = vo.getQtyITM03();
          else {
            delta = qty;
          }
          vo.setQtyITM03(vo.getQtyITM03().subtract(delta));
          usedComponents.put(vo.getItemCodeItm01ITM03(),qty.subtract(delta));

          pstmt.setString(1,prodOrderVO.getCompanyCodeSys01DOC22());
          pstmt.setBigDecimal(2,prodOrderVO.getDocYearDOC22());
          pstmt.setBigDecimal(3,prodOrderVO.getDocNumberDOC22());
          pstmt.setString(4,prodVO.getItemCodeItm01ITM03());
          pstmt.setString(5,vo.getItemCodeItm01ITM03());
          pstmt.setBigDecimal(6,delta);
          pstmt.setInt(7,sequence);
          pstmt.execute();
          sequence++;
        }
        if (vo.getQtyITM03().doubleValue()>0) {
          it = ((HashSet)compAltCodes.get(vo.getItemCodeItm01ITM03())).iterator();
          while(vo.getQtyITM03().doubleValue()>0 && it.hasNext()) {
            itemCode = it.next().toString();
            qty = (BigDecimal)usedComponents.get(itemCode);

            altQty = conv.convertQty(
              ((ProdOrderComponentVO)usedComponentsVO.get(itemCode)).getMinSellingQtyUmCodeReg02ITM01(),
              vo.getMinSellingQtyUmCodeReg02ITM01(),
              qty,
              userSessionPars,
              request,
              response,
              userSession,
              context
            );

            if (qty!=null && qty.doubleValue()>0) {
              if (vo.getQtyITM03().doubleValue()<=altQty.doubleValue()) {
                delta = conv.convertQty(
                    vo.getMinSellingQtyUmCodeReg02ITM01(),
                    ((ProdOrderComponentVO)usedComponentsVO.get(itemCode)).getMinSellingQtyUmCodeReg02ITM01(),
                    vo.getQtyITM03(),
                    userSessionPars,
                    request,
                    response,
                    userSession,
                    context
                );
                usedComponents.put(itemCode,qty.subtract(delta));
                vo.setQtyITM03(new BigDecimal(0));

              }
              else {
                delta = qty;
                usedComponents.put(itemCode,new BigDecimal(0));
                vo.setQtyITM03(vo.getQtyITM03().subtract(altQty));
              }

              pstmt.setString(1,prodOrderVO.getCompanyCodeSys01DOC22());
              pstmt.setBigDecimal(2,prodOrderVO.getDocYearDOC22());
              pstmt.setBigDecimal(3,prodOrderVO.getDocNumberDOC22());
              pstmt.setString(4,prodVO.getItemCodeItm01ITM03());
              pstmt.setString(5,itemCode);
              pstmt.setBigDecimal(6,delta);
              pstmt.setInt(7,sequence);
              pstmt.execute();
              sequence++;
            }
          }
        }
      }
      else {
        // the component is a sub-product...
        pstmt.setString(1,prodOrderVO.getCompanyCodeSys01DOC22());
        pstmt.setBigDecimal(2,prodOrderVO.getDocYearDOC22());
        pstmt.setBigDecimal(3,prodOrderVO.getDocNumberDOC22());
        pstmt.setString(4,prodVO.getItemCodeItm01ITM03());
        pstmt.setString(5,vo.getItemCodeItm01ITM03());
        pstmt.setBigDecimal(6,vo.getQtyITM03());
        pstmt.setInt(7,sequence);
        pstmt.execute();
        sequence++;
      }
    }


    // retrieve manufacture code...
    Response res = item.executeCommand(new ItemPK(prodVO.getCompanyCodeSys01ITM03(),prodVO.getItemCodeItm01ITM03()),userSessionPars,request,response,userSession,context);
    if (res.isError())
      throw new Exception(res.getErrorMessage());
    DetailItemVO itemVO = (DetailItemVO)((VOResponse)res).getVo();

    // retrieve manufacture operations and insert them to DOC26...
    ManufactureVO manVO = new ManufactureVO();
    manVO.setCompanyCodeSys01PRO01(itemVO.getCompanyCodeSys01ITM01());
    manVO.setManufactureCodePRO01(itemVO.getManufactureCodePro01ITM01());
    GridParams gridParams = new GridParams();
    gridParams.getOtherGridParams().put(ApplicationConsts.MANUFACTURE_VO,manVO);
    res = ops.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
    if (res.isError())
      throw new Exception(res.getErrorMessage());
    java.util.List list = ((VOListResponse)res).getRows();
    ManufacturePhaseVO phaseVO = null;

    if (subProductsAlreadyAdded.contains(prodVO.getItemCodeItm01ITM03()))
      return sequence;
    subProductsAlreadyAdded.add(prodVO.getItemCodeItm01ITM03());

    for(int i=0;i<list.size();i++) {
      phaseVO = (ManufacturePhaseVO)list.get(i);
/*
  "insert into DOC26_PRODUCTION_OPERATIONS(COMPANY_CODE_SYS01,DOC_YEAR,DOC_NUMBER,ITEM_CODE_ITM01,PHASE_NUMBER,"+
  "OPERATION_CODE,OPERATION_DESCRIPTION,VALUE,DURATION,MANUFACTURE_TYPE,COMPLETION_PERC,QTY,TASK_CODE,TASK_DESCRIPTION,"+
  "MACHINERY_CODE,MACHINERY_DESCRIPTION,SUBST_OPERATION_CODE,SUBST_OPERATION_DESCRIPTION,NOTE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"

*/
      pstmt2.setString(1,prodOrderVO.getCompanyCodeSys01DOC22());
      pstmt2.setBigDecimal(2,prodOrderVO.getDocYearDOC22());
      pstmt2.setBigDecimal(3,prodOrderVO.getDocNumberDOC22());
      pstmt2.setString(4,prodVO.getItemCodeItm01ITM03());
      pstmt2.setBigDecimal(5,phaseVO.getPhaseNumberPRO02());
      pstmt2.setString(6,phaseVO.getOperationCodePro04PRO02());
      pstmt2.setString(7,phaseVO.getDescriptionSYS10());
      pstmt2.setBigDecimal(8,phaseVO.getValuePRO02());
      pstmt2.setBigDecimal(9,phaseVO.getDurationPRO02());
      pstmt2.setString(10,phaseVO.getManufactureTypePRO02());
      pstmt2.setBigDecimal(11,phaseVO.getCompletionPercPRO02());
      pstmt2.setBigDecimal(12,phaseVO.getQtyPRO02());
      pstmt2.setString(13,phaseVO.getTaskCodeReg07PRO02());
      pstmt2.setString(14,phaseVO.getTaskDescriptionSYS10());
      pstmt2.setString(15,phaseVO.getMachineryCodePro03PRO02());
      pstmt2.setString(16,phaseVO.getMachineryDescriptionSYS10());
      pstmt2.setString(17,phaseVO.getSubstOperationCodePro04PRO02());
      pstmt2.setString(18,phaseVO.getDescription2());
      pstmt2.setString(19,phaseVO.getNotePRO02());
      pstmt2.execute();
    }

    return sequence;
  }


}
