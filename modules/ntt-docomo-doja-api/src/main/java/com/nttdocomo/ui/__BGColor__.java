// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

/**
 * Holds the background color.
 *
 * @since 2022/02/14
 */
final class __BGColor__
{
	/** The background color. */
	volatile int _bgColor;
	
	/**
	 * Initializes the background color with an initial color.
	 *
	 * @param __bgColor The background color used.
	 * @since 2022/02/14
	 */
	public __BGColor__(int __bgColor)
	{
		this._bgColor = __bgColor;
	}
}
