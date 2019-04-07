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
 * This is used to specify a class which is created along with any associated
 * exception handlers as needed.
 *
 * @since 2019/04/02
 */
public final class ExceptionClassStackAndTable
{
	/** The class name to use. */
	protected final ClassName name;
	
	/** The exception combo to target. */
	protected final ExceptionStackAndTable stackandtable;
	
	/**
	 * Initializes the exception information.
	 *
	 * @param __n The class to target.
	 * @param __ops The stack state.
	 * @param __t The exception table.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/03
	 */
	public ExceptionClassStackAndTable(ClassName __n,
		JavaStackState __ops, ExceptionHandlerTable __t)
		throws NullPointerException
	{
		this(__n, new ExceptionStackAndTable(__ops, __t));
	}
	
	/**
	 * Initializes the exception information.
	 *
	 * @param __n The class to target.
	 * @param __c The target for the exception handler.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/02
	 */
	public ExceptionClassStackAndTable(ClassName __n,
		ExceptionStackAndTable __c)
		throws NullPointerException
	{
		if (__n == null || __c == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
		this.stackandtable = __c;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/02
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof ExceptionClassStackAndTable))
			return false;
		
		ExceptionClassStackAndTable o = (ExceptionClassStackAndTable)__o;
		return this.name.equals(o.name) &&
			this.stackandtable.equals(o.stackandtable);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/02
	 */
	@Override
	public final int hashCode()
	{
		return this.name.hashCode() ^ this.stackandtable.hashCode();
	}
}

