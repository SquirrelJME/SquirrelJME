// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.scritchui.NativeScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * For returning the default ScritchUI interface.
 *
 * @since 2024/03/07
 */
@SquirrelJMEVendorApi
public final class DefaultScritchInterface
{
	/**
	 * {@squirreljme.property cc.squirreljme.scritchui.force=(false|true|panel)
	 * Should the framebuffer be forced to be used for ScritchUI? If panel
	 * is specified then if supported, only native panels will be used.}
	 */
	@SquirrelJMEVendorApi
	public static final String FORCE_PROPERTY =
		"cc.squirreljme.scritchui.force";
	
	/** The native instance. */
	private static volatile ScritchInterface _instance;
	
	/**
	 * Not used.
	 *
	 * @since 2024/03/07
	 */
	private DefaultScritchInterface()
	{
	}
	
	/**
	 * Returns the interface for ScritchUI.
	 *
	 * @return The interface for ScritchUI.
	 * @since 2024/03/07
	 */
	@SquirrelJMEVendorApi
	public static ScritchInterface instance()
	{
		// Was this already obtained?
		ScritchInterface instance = DefaultScritchInterface._instance;
		if (instance != null)
			return instance;
		
		// Use native interface, if enabled, otherwise framebuffer
		instance = NativeScritchInterface.nativeInterface();
		if (instance == null || Boolean.getBoolean(
			DefaultScritchInterface.FORCE_PROPERTY))
		{
			// Use a default screen provider
			FramebufferScreensProvider provider =
				new DefaultFramebufferScreensProvider();
			
			// Setup framebuffer instance
			instance = new FramebufferScritchInterface(provider);
		}
		
		// Are panels only supported? We then need to provide components
		// and drawing for everything else
		else if (NativeScritchInterface.panelOnly() ||
			"panel".equals(System.getProperty(
				DefaultScritchInterface.FORCE_PROPERTY)))
			instance = new FramebufferScritchInterface(instance);
		
		// Cache and return
		DefaultScritchInterface._instance = instance;
		return instance;
	}
}
