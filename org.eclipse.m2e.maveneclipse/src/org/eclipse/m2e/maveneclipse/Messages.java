/*
 * Copyright 2000-2011 the original author or authors.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.m2e.maveneclipse;

import org.eclipse.osgi.util.NLS;

/**
 * General messages.
 * 
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.m2e.maveneclipse.internal.messages"; //$NON-NLS-1$

	public static String mavenEclipseProjectConfiguratorError;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
