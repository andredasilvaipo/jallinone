package org.jallinone.events.server;

import javax.rules.*;
import javax.rules.admin.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.logger.server.Logger;
import java.rmi.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Events manager used to collect all events fired by business objects:
 * it provides a method invoked by business objects.
 * This method may throw an exception used by the business object to interrupt the transaction with error.</p>
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
public class EventsManager {

  /** unique instance of this class */
  private static EventsManager instance;

  /** stateless rule session */
  private StatelessRuleSession session;


  public static EventsManager getInstance() {
    if (instance!=null)
      return instance;
    instance = new EventsManager();
    return instance;
  }


  /**
   * Constructor invoked by the static initializer.
   */
  private EventsManager() {
    try {
       // Load the rule service provider of the reference implementation.
       // Loading this class will automatically register this provider with the provider manager.
       Class.forName( "org.jruleengine.RuleServiceProviderImpl" );

       // get the rule service provider from the provider manager...
       RuleServiceProvider serviceProvider = RuleServiceProviderManager.getRuleServiceProvider( "org.jruleengine" );

       // get the RuleAdministrator...
       RuleAdministrator ruleAdministrator = serviceProvider.getRuleAdministrator();

       // get an input stream to a fetch XML ruleset...
       String urlString = "Rules.xml";

       InputStream inStream = this.getClass().getResourceAsStream("/"+urlString);

       // parse the ruleset from the XML document...
       RuleExecutionSet res1 = ruleAdministrator.getLocalRuleExecutionSetProvider( new HashMap() ).createRuleExecutionSet( inStream, new HashMap() );
       inStream.close();

       // register the RuleExecutionSet...
       String uri = res1.getName();
       ruleAdministrator.registerRuleExecutionSet(uri, res1, null );

       RuleRuntime ruleRuntime = serviceProvider.getRuleRuntime();

       // create a stateless session...
       session = (StatelessRuleSession) ruleRuntime.createRuleSession(uri,new HashMap(),RuleRuntime.STATELESS_SESSION_TYPE);

     }
     catch (Throwable ex) {
       Logger.error("",this.getClass().getName(),"EventsManager","Error while loading rules",ex);
     }
  }


  /**
   * Method called by a business object when starting the b.o. method, before committing, after commiting.
   * @param event event fired by the business object
   * @throws EventsManagerException exception throwed by the events manager to force transation aborting inside the business object
   */
  public void processEvent(GenericEvent event) throws EventsManagerException {
    try {
      List list = new ArrayList();
      list.add(event);
      list.add(this);
      list = session.executeRules(list);
      if (event.getErrorMessage()!=null)
        throw new EventsManagerException(event.getErrorMessage());
    }
    catch (Throwable ex) {
      throw new EventsManagerException(ex.getMessage());
    }
  }

}
