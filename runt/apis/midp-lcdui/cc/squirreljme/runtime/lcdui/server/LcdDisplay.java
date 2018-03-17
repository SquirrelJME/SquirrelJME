// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

/**
 * This represents a single display on the definition which allows displayable
 * widgets to be shown on them as requested.
 *
 * @since 2018/03/17
 */
public abstract class LcdDisplay
{
	/** The global locking object. */
	protected final Object lock;
	
	/** The index of this display. */
	protected final int index;
	
	/**
	 * Initializes the base display.
	 *
	 * @param __lock The global lock for the displays.
	 * @param __dx The display index.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public LcdDisplay(Object __lock, int __dx)
		throws NullPointerException
	{
		if (__lock == null)
			throw new NullPointerException("NARG");
		
		this.lock = __lock;
		this.index = __dx;;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/17
	 */
	@Override
	public final int hashCode()
	{
		return this.index;
	}
	
	/**
	 * Returns the index of the display.
	 *
	 * @return The display index.
	 * @since 2018/03/17
	 */
	public final int index()
	{
		return this.index;
	}
}

