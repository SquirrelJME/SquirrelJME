// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.ui;

/**
 * Holds the background color.
 *
 * @since 2022/02/14
 */
public final class BGColor
{
	/** The background color. */
	public volatile int bgColor;
	
	/**
	 * Initializes the background color with an initial color.
	 *
	 * @param __bgColor The background color used.
	 * @since 2022/02/14
	 */
	public BGColor(int __bgColor)
	{
		this.bgColor = __bgColor;
	}
}
