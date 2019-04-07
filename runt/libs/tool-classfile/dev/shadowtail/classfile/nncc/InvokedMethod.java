// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.MethodHandle;

/**
 * Represents a method to be invoked.
 *
 * @since 2019/03/21
 */
public final class InvokedMethod
{
	/** The type of method to invoke. */
	protected final InvokeType type;
	
	/** The handle of the method being invoke. */
	protected final MethodHandle handle;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the invoked method.
	 *
	 * @param __t The type of method to invoke.
	 * @param __h The handle of the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/21
	 */
	public InvokedMethod(InvokeType __t, MethodHandle __h)
		throws NullPointerException
	{
		if (__t == null || __h == null)
			throw new NullPointerException("NARG");
		
		this.type = __t;
		this.handle = __h;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/24
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof InvokedMethod))
			return false;
		
		InvokedMethod o = (InvokedMethod)__o;
		return this.type.equals(o.type) &&
			this.handle.equals(o.handle);
	}
	
	/**
	 * Returns the method handle.
	 *
	 * @return The method handle.
	 * @since 2019/03/24
	 */
	public final MethodHandle handle()
	{
		return this.handle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/24
	 */
	@Override
	public final int hashCode()
	{
		return this.type.hashCode() ^ this.handle.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/21
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = this.type + "+" + this.handle));
		
		return rv;
	}
}

