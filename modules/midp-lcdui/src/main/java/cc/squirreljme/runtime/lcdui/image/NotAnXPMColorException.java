// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

/**
 * This is thrown if this is not an XPM color.
 *
 * @since 2021/01/01
 */
public class NotAnXPMColorException
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 * 
	 * @param __s The string used.
	 * @since 2021/01/01
	 */
	public NotAnXPMColorException(String __s)
	{
		super(__s);
	}
}
