// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import dev.shadowtail.classfile.xlate.JavaStackState;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a transition between one stack state and another.
 *
 * @since 2019/04/11
 */
public final class StateTransition
{
	/** The first state. */
	protected final JavaStackState a;
	
	/** The resulting state. */
	protected final JavaStackState b;
	
	/** String representation. */
	private Reference<String> _string;
	
	/** Hashcode. */
	private int _hash;
	
	/**
	 * Initializes the state transition.
	 *
	 * @param __a From.
	 * @param __b To.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/11
	 */
	public StateTransition(JavaStackState __a, JavaStackState __b)
		throws NullPointerException
	{
		if (__a == null || __b == null)
			throw new NullPointerException("NARG");
		
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
		if (this == __o)
			return true;
		
		if (!(__o instanceof StateTransition))
			return false;
		
		if (this.hashCode() != __o.hashCode())
			return false;
		
		StateTransition o = (StateTransition)__o;
		return this.a.equals(o.a) &&
			this.b.equals(o.b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final int hashCode()
	{
		int rv = this._hash;
		if (rv == 0)
			this._hash = (rv = this.a.hashCode() & this.b.hashCode());
		return rv;
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
			this._string = new WeakReference<>((rv = String.format(
				"Transition:%s->%s", this.a, this.b)));
		
		return rv;
	}
}

