// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.lang.ref.Reference;

/**
 * This is a zone key which is used for synchronized entry and exit of the
 * method.
 *
 * @since 2017/05/29
 */
@Deprecated
public class SynchronizedZoneKey
	extends ZoneKey
{
	/** Is this synchronized method entry? */
	protected final boolean entry;
	
	/**
	 * Initializes the zone key for synchronized entry or exit.
	 *
	 * @param __pr The owning program.
	 * @param __e If {@code true} then this is for the program entry point,
	 * otherwise it will be for the exit point.
	 * @since 2017/05/29
	 */
	public SynchronizedZoneKey(Reference<ProgramState> __pr, boolean __e)
	{
		super(__pr);
		
		this.entry = __e;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/29
	 */
	@Override
	public int compareTo(ZoneKey __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/29
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
}

