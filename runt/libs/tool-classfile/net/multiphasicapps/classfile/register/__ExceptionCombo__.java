// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import net.multiphasicapps.classfile.ExceptionHandlerTable;

/**
 * This is a combination of an objects snapshot and exception handler table.
 *
 * @since 2019/03/22
 */
final class __ExceptionCombo__
{
	/** The enqueued positions. */
	protected final JavaStackEnqueueList enqueue;
	
	/** The exception handle table. */
	protected final ExceptionHandlerTable table;
	
	/**
	 * Initializes the exception combo.
	 *
	 * @param __ops The enqueued registers.
	 * @param __t The table used.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	__ExceptionCombo__(JavaStackEnqueueList __ops,
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
	 * @since 2019/03/22
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof __ExceptionCombo__))
			return false;
		
		__ExceptionCombo__ o = (__ExceptionCombo__)__o;
		return this.enqueue.equals(o.ops) &&
			this.table.equals(o.table);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/22
	 */
	@Override
	public final int hashCode()
	{
		return this.enqueue.hashCode() ^ this.table.hashCode();
	}
}

