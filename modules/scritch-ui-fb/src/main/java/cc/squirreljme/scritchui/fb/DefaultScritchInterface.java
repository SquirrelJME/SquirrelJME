// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

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
	
}
