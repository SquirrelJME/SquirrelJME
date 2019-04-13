// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents an operation that needs to be performed during stack
 * transitions.
 *
 * @since 2019/04/11
 */
public final class StateOperation
{
	/** The type of operation to perform. */
	public final StateOperation.Type type;
	
	/** The A register. */
	public final int a;
	
	/** The B register. */
	public final int b;
	
	/** Hashcode. */
	private int _hash;
	
	/** String. */
	private Reference<String> _string;
	
	/**
	 * Initializes the state operation.
	 *
	 * @param __t The type.
	 * @param __a Register A.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/13
	 */
	public StateOperation(StateOperation.Type __t, int __a)
		throws NullPointerException
	{
		this(__t, __a, __a);
	}
	
	/**
	 * Initializes the state operation.
	 *
	 * @param __t The type.
	 * @param __a Register A.
	 * @param __b Register B.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/13
	 */
	public StateOperation(StateOperation.Type __t, int __a, int __b)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this.type = __t;
		this.a = __a;
		this.b = __b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.type + "(" +
				this.a + ", " + this.b + ")"));
		
		return rv;
	}
	
	/**
	 * Represents the type of operation to perform.
	 *
	 * @since 2019/04/11
	 */
	public static enum Type
	{
		/** Count. */
		COUNT,
		
		/** Un-count. */
		UNCOUNT,
		
		/** Copy. */
		COPY,
		
		/** Wide copy. */
		WIDE_COPY,
		
		/** End. */
		;
	}
}

