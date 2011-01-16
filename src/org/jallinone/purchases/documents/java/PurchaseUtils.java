package org.jallinone.purchases.documents.java;

import java.math.BigDecimal;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Utility methods, used both on client and server side.</p>
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
public class PurchaseUtils {


  public static boolean updateTotals(DetailPurchaseDocRowVO vo,int currencyDecimals) {

    if (vo.getQtyDOC07()!=null && vo.getValueReg01DOC07()!=null && vo.getValuePur04DOC07()!=null) {
      vo.setTaxableIncomeDOC07(vo.getQtyDOC07().multiply(vo.getValuePur04DOC07()).setScale(currencyDecimals,BigDecimal.ROUND_HALF_UP));

      // apply percentage discount...
      if (vo.getDiscountPercDOC07()!=null) {
        double taxtable = vo.getTaxableIncomeDOC07().doubleValue()-vo.getTaxableIncomeDOC07().doubleValue()*vo.getDiscountPercDOC07().doubleValue()/100d;
        vo.setTaxableIncomeDOC07(new BigDecimal(taxtable).setScale(currencyDecimals,BigDecimal.ROUND_HALF_UP));
      }

      // apply value discount...
      if (vo.getDiscountValueDOC07()!=null) {
        vo.setTaxableIncomeDOC07(vo.getTaxableIncomeDOC07().subtract(vo.getDiscountValueDOC07()).setScale(currencyDecimals,BigDecimal.ROUND_HALF_UP));
      }

      // calculate row vat...
      double vatPerc = vo.getValueReg01DOC07().doubleValue()*(1d-vo.getDeductibleReg01DOC07().doubleValue()/100d)/100;
      vo.setVatValueDOC07(vo.getTaxableIncomeDOC07().multiply(new BigDecimal(vatPerc)).setScale(currencyDecimals,BigDecimal.ROUND_HALF_UP));

      // calculate row total...
      vo.setValueDOC07(vo.getTaxableIncomeDOC07().add(vo.getVatValueDOC07()));

      return true;
    }
    else {
      vo.setTaxableIncomeDOC07(null);
      vo.setVatValueDOC07(null);
      vo.setValueDOC07(null);

      return false;
    }
  }

}
