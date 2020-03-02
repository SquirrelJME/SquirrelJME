// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

/**
 * This is the XPM demo.
 *
 * @since 2019/04/15
 */
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

