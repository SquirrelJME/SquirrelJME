// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.lcduidemo;

/**
 * Demos a GIF.
 *
 * @since 2021/12/05
 */
public class GIFDemo
	extends AbstractImageDemo
{
	/**
	 * Initializes the image demo.
	 *
	 * @since 2021/12/05
	 */
	public GIFDemo()
		throws NullPointerException
	{
		super(GIFDemo.class.getResourceAsStream("image.gif"));
	}
}
