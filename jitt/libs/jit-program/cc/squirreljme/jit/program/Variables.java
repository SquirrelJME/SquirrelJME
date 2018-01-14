// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.program;

import cc.squirreljme.jit.classfile.JavaType;
import cc.squirreljme.jit.classfile.StackMapTableEntry;
import cc.squirreljme.jit.classfile.StackMapTableState;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This class contains the variable treads for the stack, local, and
 * any needed temporary variables to be used when executing the program.
 *
 * @since 2017/10/17
 */
public final class Variables
{
	/** Data variables. */
	protected final DataValues data;
	
	/** Local variables. */
	protected final VariableTread locals;
	
	/** Stack variables. */
	protected final VariableTread stack;
	
	/**
	 * Initializes variable storage.
	 *
	 * @param __ms The maximum number of stack entries.
	 * @param __ml The maximum number of local entries.
	 * @since 2017/10/17
	 */
	public Variables(int __ms, int __ml)
	{
		Reference<Variables> selfref = new WeakReference<>(this);
		this.data = new DataValues(selfref);
		this.locals = new VariableTread(selfref, __ml, false);
		this.stack = new VariableTread(selfref, __ms, true);
	}
	
	/**
	 * Initializes the local variable state from the given stack map table
	 * state.
	 *
	 * @param __s The stack map state to initialize from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/20
	 */
	public void initializeLocalsFrom(StackMapTableState __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Only care about locals
		VariableTread locals = this.locals;
		for (int i = 0, n = locals.size(); i < n; i++)
		{
			// Only use valid entries
			StackMapTableEntry se = __s.getLocal(i);
			if (se == null)
				continue;
			
			// Ignore anything set to nothing or is the top type
			JavaType set = se.type();
			if (set == null || set.isNothing() || set.isTop())
				continue;
			
			// Set new value from the given data
			DataValue dv = (se.isInitialized() ? locals.setNewValue(i, set) :
				locals.setNewValue(i, set, new InitializationKey(-(i + 1))));
			
			// Debug
			System.err.printf("DEBUG -- %s -> %s%n", se, dv);
		}
	}
}

