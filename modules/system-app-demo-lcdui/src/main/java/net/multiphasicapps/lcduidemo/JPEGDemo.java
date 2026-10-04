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
 * This is the JPEG demo.
 *
 * @since 2019/05/05
 */
public final class JPEGDemo
	extends AbstractImageDemo
{
	/**
	 * Initializes the demo.
	 *
	 * @since 2019/05/05
	 */
	public JPEGDemo()
	{
		super(JPEGDemo.class.getResourceAsStream("image.jpg"));
	}
}
