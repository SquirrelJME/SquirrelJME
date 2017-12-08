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
import net.multiphasicapps.squirreljme.runtime.cldc.program.Program;

/**
 * This represents the base for a Java chore.
 *
 * @since 2017/12/08
 */
public abstract class JavaChore
	extends Chore
{
	/** The group the chore is in. */
	protected final JavaChoreGroup group;
	
	/**
	 * Initializes the base chore.
	 *
	 * @param __group The group the chore is in.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/08
	 */
	public JavaChore(JavaChoreGroup __group)
		throws NullPointerException
	{
		if (__group == null)
			throw new NullPointerException("NARG");
		
		this.group = __group;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public final ChoreGroup group()
	{
		return this.group;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public String mainClass()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public final int priority()
	{
		// Just refer to threads using normal priorities since that is the
		// most simple approach
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/08
	 */
	@Override
	public Program program()
	{
		throw new todo.TODO();
	}
}

