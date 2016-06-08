/*
 * Copyright (C) 2011 LINUXTEK, Inc.  All Rights Reserved.
 */
package com.linuxtek.kona.velocity;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class KVelocityViewResolver extends AbstractTemplateViewResolver {

	private String dateToolAttribute;
	private String numberToolAttribute;
	private String toolboxConfigLocation;


	public KVelocityViewResolver() {
		setViewClass(requiredViewClass());
	}

	/**
	 * Requires {@link KVelocityView}.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Class requiredViewClass() {
		return KVelocityView.class;
	}


	/**
	 * Set the name of the DateTool helper object to expose in the Velocity context
	 * of this view, or <code>null</code> if not needed. DateTool is part of Velocity Tools 1.0.
	 * @see org.apache.velocity.tools.generic.DateTool
	 * @see KVelocityView#setDateToolAttribute
	 */
	public void setDateToolAttribute(String dateToolAttribute) {
		this.dateToolAttribute = dateToolAttribute;
	}

	/**
	 * Set the name of the NumberTool helper object to expose in the Velocity context
	 * of this view, or <code>null</code> if not needed. NumberTool is part of Velocity Tools 1.1.
	 * @see org.apache.velocity.tools.generic.NumberTool
	 * @see KVelocityView#setNumberToolAttribute
	 */
	public void setNumberToolAttribute(String numberToolAttribute) {
		this.numberToolAttribute = numberToolAttribute;
	}

	/**
	 * Set a Velocity Toolbox config location, for example "/WEB-INF/toolbox.xml",
	 * to automatically load a Velocity Tools toolbox definition file and expose
	 * all defined tools in the specified scopes. If no config location is
	 * specified, no toolbox will be loaded and exposed.
	 * <p>The specified location string needs to refer to a ServletContext
	 * resource, as expected by ServletToolboxManager which is part of
	 * the view package of Velocity Tools.
	 * <p><b>Note:</b> Specifying a toolbox config location will lead to
	 * KVelocityToolboxView instances being created.
	 * @see org.apache.velocity.tools.view.servlet.ServletToolboxManager#getInstance
	 * @see KVelocityToolboxView#setToolboxConfigLocation
	 */
	public void setToolboxConfigLocation(String toolboxConfigLocation) {
		this.toolboxConfigLocation = toolboxConfigLocation;
	}


	@Override
	protected void initApplicationContext() {
		super.initApplicationContext();

		if (this.toolboxConfigLocation != null) {
			if (KVelocityView.class.equals(getViewClass())) {
				logger.info("Using KVelocityToolboxView instead of default KVelocityView " +
						"due to specified toolboxConfigLocation");
				setViewClass(KVelocityToolboxView.class);
			}
			else if (!KVelocityToolboxView.class.isAssignableFrom(getViewClass())) {
				throw new IllegalArgumentException(
						"Given view class [" + getViewClass().getName() +
						"] is not of type [" + KVelocityToolboxView.class.getName() +
						"], which it needs to be in case of a specified toolboxConfigLocation");
			}
		}
	}


	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		KVelocityView view = (KVelocityView) super.buildView(viewName);
		view.setDateToolAttribute(this.dateToolAttribute);
		view.setNumberToolAttribute(this.numberToolAttribute);
		if (this.toolboxConfigLocation != null) {
			((KVelocityToolboxView) view).setToolboxConfigLocation(this.toolboxConfigLocation);
		}
		return view;
	}

}