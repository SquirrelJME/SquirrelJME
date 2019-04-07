// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import dev.shadowtail.classfile.xlate.JavaStackEnqueueList;
import dev.shadowtail.classfile.xlate.JavaStackState;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ExceptionHandlerTable;

/**
 * Represents a class, enqueue, and exception table.
 *
 * @since 2019/04/03
 */
public final class ExceptionClassEnqueueAndTable
{
	/** The class name to use. */
	protected final ClassName name;
	
	/** The exception combo to target. */
	protected final ExceptionEnqueueAndTable enqueueandtable;
	
	/**
	 * Initializes the exception information.
	 *
	 * @param __n The class to target.
	 * @param __ops The enqueue.
	 * @param __t The exception table.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	public ExceptionClassEnqueueAndTable(ClassName __n,
		JavaStackEnqueueList __ops, ExceptionHandlerTable __t)
		throws NullPointerException
	{
		this(__n, new ExceptionEnqueueAndTable(__ops, __t));
	}
	
	/**
	 * Initializes the exception information.
	 *
	 * @param __n The class to target.
	 * @param __c The target for the exception handler.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	public ExceptionClassEnqueueAndTable(ClassName __n,
		ExceptionEnqueueAndTable __c)
		throws NullPointerException
	{
		if (__n == null || __c == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
		this.enqueueandtable = __c;
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
		
		if (!(__o instanceof ExceptionClassEnqueueAndTable))
			return false;
		
		ExceptionClassEnqueueAndTable o = (ExceptionClassEnqueueAndTable)__o;
		return this.name.equals(o.name) &&
			this.enqueueandtable.equals(o.enqueueandtable);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/03
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode() ^ this.enqueueandtable.hashCode();
	}
}

