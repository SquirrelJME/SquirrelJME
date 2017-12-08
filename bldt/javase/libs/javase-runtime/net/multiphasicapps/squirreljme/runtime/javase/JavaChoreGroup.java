// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import net.multiphasicapps.squirreljme.runtime.cldc.chore.Chore;
import net.multiphasicapps.squirreljme.runtime.cldc.chore.ChoreGroup;

/**
 * This represents a chore group within the Java VM.
 *
 * @since 2017/12/08
 */
public class JavaChoreGroup
	extends ChoreGroup
{
	/** Is this a system chore group? */
	protected final boolean issystem;
	
	/**
	 * Initializes the chore group.
	 *
	 * @param __sys Is this a system group?
	 * @since 2017/12/08
	 */
	public JavaChoreGroup(boolean __sys)
	{
		this.issystem = __sys;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public int basicPermissions()
	{
		// Grant the system every permission available
		if (this.issystem)
			return ~0;
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public int flags()
	{
		int rv = 0;
		
		if (this.issystem)
			rv |= ChoreGroup.FLAG_SYSTEM;
		
		return rv;
	}
}

