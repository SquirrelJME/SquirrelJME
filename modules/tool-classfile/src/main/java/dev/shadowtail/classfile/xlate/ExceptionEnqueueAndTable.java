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
 * This is a combination of an enqueue and exception handler table.
 *
 * @since 2019/04/03
 */
public final class ExceptionEnqueueAndTable
{
	/** The enqueue of the stack. */
	public final JavaStackEnqueueList enqueue;
	
	/** The exception handle table. */
	public final ExceptionHandlerTable table;
	
	/**
	 * Initializes the exception combo.
	 *
	 * @param __ops The stack state.
	 * @param __t The table used.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	public ExceptionEnqueueAndTable(JavaStackEnqueueList __ops,
		ExceptionHandlerTable __t)
		throws NullPointerException
	{
		if (__ops == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.enqueue = __ops;
		this.table = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/03
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof ExceptionEnqueueAndTable))
			return false;
		
		ExceptionEnqueueAndTable o = (ExceptionEnqueueAndTable)__o;
		return this.enqueue.equals(o.enqueue) &&
			this.table.equals(o.table);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/03
	 */
	@Override
	public final int hashCode()
	{
		return this.enqueue.hashCode() ^ this.table.hashCode();
	}
}

