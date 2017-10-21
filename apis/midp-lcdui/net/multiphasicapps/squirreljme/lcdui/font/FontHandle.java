// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.font;

/**
 * This class is used as the base handle for fonts which are visible to
 * LCDUI.
 *
 * Handles are identified by a single ID.
 *
 * @since 2017/10/20
 */
public final class FontHandle
{
	/** The identifier of the handle. */
	protected final int id;
	
	/**
	 * Initializes the font handle.
	 *
	 * @param __id The ID of the font.
	 * @since 2017/10/20
	 */
	public FontHandle(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (!(__o instanceof FontHandle))
			return false;
		
		return this.id == ((FontHandle)__o).id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public final int hashCode()
	{
		return this.id;
	}
}

