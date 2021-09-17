// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import net.multiphasicapps.classfile.ExceptionHandlerTable;

/**
 * This is a combination of a stack and exception handler table.
 *
 * @since 2019/03/22
 */
public final class ExceptionStackAndTable
{
	/** The state of the stack. */
	public final JavaStackState stack;
	
	/** The exception handle table. */
	public final ExceptionHandlerTable table;
	
	/**
	 * Initializes the exception combo.
	 *
	 * @param __ops The stack state.
	 * @param __t The table used.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public ExceptionStackAndTable(JavaStackState __ops,
		ExceptionHandlerTable __t)
		throws NullPointerException
	{
		if (__ops == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.stack = __ops;
		this.table = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/22
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof ExceptionStackAndTable))
			return false;
		
		ExceptionStackAndTable o = (ExceptionStackAndTable)__o;
		return this.stack.equals(o.stack) &&
			this.table.equals(o.table);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/22
	 */
	@Override
	public final int hashCode()
	{
		return this.stack.hashCode() ^ this.table.hashCode();
	}
}
