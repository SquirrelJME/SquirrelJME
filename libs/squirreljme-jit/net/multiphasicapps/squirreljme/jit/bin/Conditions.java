// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.bin.cond.*;
import net.multiphasicapps.squirreljme.jit.java.ClassName;

/**
 * This condition table is used to verify that all conditions within the state
 * of the output executable pass in which case compilation is a success and
 * symbol resolution can be performed. Essentially this allows the compiler to
 * be simpler in design by deferring what would happen 99% of the time until
 * the near end of compilation (since most classes and compilation sets should
 * not be malformed).
 *
 * Despite a waste of potential resources this makes life much easier and
 * reduces stress levels and also reduces tons of complexity and waste.
 *
 * @since 2017/07/07
 */
public final class Conditions
	extends __SubState__
{
	/** The conditions which must be checked . */
	private volatile Set<Condition> _conditions =
		new LinkedHashSet<>();
	
	/**
	 * Initializes the conditions table.
	 *
	 * @param __ls The reference to the owning linker state.
	 * @since 2017/07/07
	 */
	Conditions(Reference<LinkerState> __ls)
	{
		super(__ls);
	}
	
	/**
	 * Checks whether the specified class can extend the given super class.
	 *
	 * @param __this The this class.
	 * @param __super The class to check if it can extend.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/07
	 */
	public final void canExtend(ClassName __this, ClassName __super)
		throws NullPointerException
	{
		// Check
		if (__this == null || __super == null)
			throw new NullPointerException("NARG");
		
		__add(new CanExtendCondition(__this, __super));
	}
	
	/**
	 * Checks whether the given class can implement the specified class.
	 *
	 * @param __this The class implementing the interface.
	 * @param __int The interface to check if it can be implemented.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/07
	 */
	public final void canImplement(ClassName __this, ClassName __int)
		throws NullPointerException
	{
		// Check
		if (__this == null || __int == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Adds the specified condition to the set of conditions.
	 *
	 * @param __c The condition to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/07
	 */
	private final void __add(Condition __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		this._conditions.add(__c);
	}
}

