// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents a single state within the stack map table which contains
 * a listing of all of the types used for local and stack variable along with
 * the current depth of the stack.
 *
 * @since 2017/07/28
 */
public final class StackMapTableState
{
	/** The depth of the stack. */
	protected final int depth;
	
	/** Local variables. */
	private final JavaType[] _locals;
	
	/** Stack variables. */
	private final JavaType[] _stack;
	
	/**
	 * Initializes the stack map table state.
	 *
	 * @param __l Local variables.
	 * @param __s Stack variables.
	 * @param __d The depth of the stack.
	 * @throws JITException If the state is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/28
	 */
	public StackMapTableState(JavaType[] __l, JavaType[] __s, int __d)
		throws JITException, NullPointerException
	{
		// Check
		if (__l == null || __s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

