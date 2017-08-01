// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin.cond;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.bin.LinkerState;
import net.multiphasicapps.squirreljme.jit.java.ClassName;

/**
 * This is a condition to see if one class can extend another.
 *
 * @since 2017/07/07
 */
public final class CanExtendCondition
	extends Condition
{
	/** The current class. */
	protected final ClassName thisclass;
	
	/** The super class. */
	protected final ClassName superclass;
	
	/**
	 * Initializes the condition.
	 *
	 * @param __this The current class.
	 * @param __super The class being extended.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/07
	 */
	public CanExtendCondition(ClassName __this, ClassName __super)
		throws NullPointerException
	{
		// Check
		if (__this == null || __super == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.thisclass = __this;
		this.superclass = __super;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public final boolean check(LinkerState __ls)
		throws NullPointerException
	{
		// Check
		if (__ls == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof CanExtendCondition))
			return false;
		
		CanExtendCondition o = (CanExtendCondition)__o;
		return this.thisclass.equals(o.thisclass) &&
			this.superclass.equals(o.superclass);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public final int hashCode()
	{
		return this.thisclass.hashCode() ^
			this.superclass.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	protected final String internalToString()
	{
		return String.format("%s extends %s", this.thisclass, this.superclass);
	}
	
	/**
	 * Returns the super class.
	 *
	 * @return The super class.
	 * @since 2017/07/07
	 */
	public final ClassName superClass()
	{
		return this.superclass;
	}
	
	/**
	 * Returns the this class.
	 *
	 * @return The this class.
	 * @since 2017/07/07
	 */
	public final ClassName thisClass()
	{
		return this.thisclass;
	}
}

