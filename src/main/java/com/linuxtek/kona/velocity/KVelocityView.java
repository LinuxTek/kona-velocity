/*
 * Copyright (C) 2011 LINUXTEK, Inc.  All Rights Reserved.
 */
package com.linuxtek.kona.velocity;

import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.servlet.view.velocity.VelocityView;
import org.springframework.web.util.NestedServletException;
 
public class KVelocityView extends VelocityView {
 
    private static Logger logger = Logger.getLogger(KVelocityView.class);

    @Override
	protected void mergeTemplate(Template template, Context context, 
			HttpServletResponse response) throws Exception {
		try {
            Object raw = context.get("_raw_output");
            if (raw != null && Boolean.valueOf(raw.toString())) {
                super.mergeTemplate(template, context, response);
                return;
            }

			StringWriter sw = new StringWriter();
			template.merge(context, sw);
	    	Document doc = Jsoup.parse(sw.toString());
	    	doc.outputSettings().prettyPrint(true);
            String html = doc.toString();
            response.getWriter().print(html);
            logger.debug("template merged: " + template.getName());
            
			//template.merge(context, response.getWriter());
		}
		catch (MethodInvocationException ex) {
			Throwable cause = ex.getWrappedThrowable();
			throw new NestedServletException(
					"Method invocation failed during rendering of Velocity view with name '" +
					getBeanName() + "': " + ex.getMessage() + "; reference [" + ex.getReferenceName() +
					"], method '" + ex.getMethodName() + "'",
					cause==null ? ex : cause);
		}
	}
}
