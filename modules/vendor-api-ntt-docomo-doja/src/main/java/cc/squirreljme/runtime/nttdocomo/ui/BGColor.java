// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Holds the background color.
 *
 * @since 2022/02/14
 */
@SquirrelJMEVendorApi
public final class BGColor
{
	/** The background color. */
	@SquirrelJMEVendorApi
	public volatile int bgColor;
	
	/**
	 * Initializes the background color with an initial color.
	 *
	 * @param __bgColor The background color used.
	 * @since 2022/02/14
	 */
	@SquirrelJMEVendorApi
	public BGColor(int __bgColor)
	{
		this.bgColor = __bgColor;
	}
}
