// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import dev.shadowtail.classfile.xlate.InvokeType;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.ClassName;

/**
 * This represents a reference to the method dispatch table for a class which
 * is used for instance invocations.
 *
 * @since 2019/04/27
 */
public final class MethodDispatchTable
{
	/** The invocation type, which table to be found. */
	public final InvokeType type;
	
	/** The class name. */
	public final ClassName name;
	
	/** Hash. */
	private int _hash;
	
	/** String. */
	private Reference<String> _string;
	
	/**
	 * Initializes the dispatch table reference.
	 *
	 * @param __s The string to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	public MethodDispatchTable(InvokeType __t, ClassName __n)
		throws NullPointerException
	{
		if (__t == null || __n == null)
			throw new NullPointerException("NARG");
		
		this.type = __t;
		this.name = __n;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/27
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof MethodDispatchTable))
			return false;
		
		if (this.hashCode() != __o.hashCode())
			return false;
		
		MethodDispatchTable o = (MethodDispatchTable)__o;
		return this.type.equals(o.type) &&
			this.name.equals(o.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/27
	 */
	@Override
	public final int hashCode()
	{
		int rv = this._hash;
		if (rv == 0)
			this._hash = (rv = this.type.hashCode() ^ this.name.hashCode());
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/27
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = this.type +
				"+" + this.name));
		
		return rv;
	}
}

