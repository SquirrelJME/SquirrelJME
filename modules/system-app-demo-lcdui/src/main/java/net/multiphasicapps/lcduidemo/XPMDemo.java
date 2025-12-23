// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This is the XPM demo.
 *
 * @since 2019/04/15
 */
@SquirrelJMEVendorApi
public final class XPMDemo
	extends AbstractImageDemo
{
	/**
	 * Initializes the demo.
	 *
	 * @since 2019/04/15
	 */
	public XPMDemo()
	{
		super(XPMDemo.class.getResourceAsStream("image.xpm"));
	}
}

