package org.jallinone.commons.server;

import java.util.Properties;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.config.DefaultMuleConfiguration;
import org.mule.config.builders.AutoConfigurationBuilder;
import org.mule.context.DefaultMuleContextBuilder;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.module.client.MuleClient;
import org.openswing.swing.logger.server.Logger;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Beans factory based on Mule ESB.</p>
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
public class MuleBeansFactory implements BeansFactory{

	/** Mule context */
	private MuleContext muleContext = null;
	
	/** Mule base address */
	private String loggingInterceptorLevel = null;

	/** Mule base address */
	private String muleURL = null;

	
	public MuleBeansFactory() {
	}

	
	/**
	 * Init this factory.
	 */
	public void initBeansFactory(Properties p) throws Throwable {
		try {
			muleURL = p.getProperty("muleURL");
			if (!muleURL.endsWith("/"))
				muleURL += "/";
			
			loggingInterceptorLevel = p.getProperty("loggingInterceptorLevel");
			
			AutoConfigurationBuilder configbuilder = new AutoConfigurationBuilder("conf/jallinone.xml");
			DefaultMuleConfiguration muleConfig = new DefaultMuleConfiguration();
			muleConfig.setClientMode(true);
			DefaultMuleContextBuilder contextBuilder = new DefaultMuleContextBuilder();
			contextBuilder.setMuleConfiguration(muleConfig);
			muleContext = new DefaultMuleContextFactory().createMuleContext(configbuilder, contextBuilder);
			
			
			
/*
			if (createMuleContext) {
				// in case Mule server not embedded with the JAllInOne web app
				
				AutoConfigurationBuilder configbuilder = new AutoConfigurationBuilder("conf/jallinone.xml");
				DefaultMuleConfiguration muleConfig = new DefaultMuleConfiguration();
				//muleConfig.setDefaultResponseTimeout(10000);
				
				MuleContextBuilder contextBuilder = new DefaultMuleContextBuilder();
				contextBuilder.setMuleConfiguration(muleConfig);
				muleContext = new DefaultMuleContextFactory().createMuleContext(configbuilder, contextBuilder);
				
				//muleContext = new DefaultMuleContextFactory().createMuleContext("conf/jallinone.xml");
				muleContext.start();
			}
*/				
		} catch (Throwable e) {
			Logger.error("NONAME",this.getClass().getName(),"MuleBeansFactory",e.getMessage(),e);
		}		
	}

	
	
	/**
	 * @return mule client
	 */
	public MuleClient getMuleClient() throws MuleException {
		if (muleContext!=null)
			// in case Mule server not embedded with the JAllInOne web app
			return new MuleClient(muleContext);
		else
			return new MuleClient();
	}
		
	
	/**
	 * Create a bean of the specified class.
	 * @param clazz bean's class type
	 * @return an instance of the bean
	 * @throws Throwable in case of errors 
	 */
	public Object getBean(Class clazz) throws Throwable {
		try {
			LoggingInInterceptor loggingIn = new LoggingInInterceptor();
			if (loggingInterceptorLevel!=null)
				loggingIn.setLimit(Integer.parseInt(loggingInterceptorLevel));
			LoggingOutInterceptor loggingOut = new LoggingOutInterceptor();
			if (loggingInterceptorLevel!=null)
				loggingOut.setLimit(Integer.parseInt(loggingInterceptorLevel));
			//loggingIn.setPrintWriter(null);
			
			JaxWsProxyFactoryBean pf = new JaxWsProxyFactoryBean();
			pf.getInInterceptors().add(loggingIn); 
			pf.getOutInterceptors().add(loggingOut);
	        pf.setServiceClass(clazz);
	        String serviceName = clazz.getName();
	        if (serviceName.indexOf(".")!=-1)
	        	serviceName = serviceName.substring(serviceName.lastIndexOf(".")+1);
	       	pf.setAddress(muleURL+serviceName);
	        Object bean = pf.create();
	        return bean;
		}
		catch (Throwable t) {
			Logger.error("", this.getClass().getName(), "getBean", t.getMessage(), t);
			throw t;
		}
    }
	
	
	/**
	 * Destroy this factory.
	 */
	public void destroyBeansFactory() throws Throwable {
		if (muleContext!=null) 
			muleContext.stop();
	}
	
}
