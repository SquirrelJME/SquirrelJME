// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.ui;

/**
 * Colors, styles, and otherwise.
 *
 * @since 2026/06/12
 */
public class DrawStyle
{
	/**
	 * The color used for local views.
	 *
	 * @param __id The view ID.
	 * @return The color used for the ID.
	 * @since 2026/06/12
	 */
	public static int localViewColor(int __id)
	{
		switch (Math.abs(__id) & 3)
		{
			case 0:		return 0xFF0000;
			case 1:		return 0x00FF00;
			case 2:		return 0xFFFF00;
			case 3:
			default:	return 0x0000FF;
		}
	}
}
