// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import java.io.Closeable;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is an {@link Appendable} which does not implement {@link Closeable}
 * but refers to the argument via a weak reference.
 *
 * @since 2022/06/23
 */
public final class NonClosedWeakAppendable
	implements Appendable
{
	/** The target appendable. */
	private final Reference<Appendable> _wrapped;
	
	/**
	 * 
	 * 
	 * @param __wrapped The wrapped appendable.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/06/23
	 */
	public NonClosedWeakAppendable(Appendable __wrapped)
		throws NullPointerException
	{
		if (__wrapped == null)
			throw new NullPointerException("NARG");
		
		this._wrapped = new WeakReference<>(__wrapped);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/23
	 */
	@Override
	public Appendable append(CharSequence __c)
		throws IOException
	{
		return this.__get().append(__c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/23
	 */
	@Override
	public Appendable append(CharSequence __c, int __s, int __e)
		throws IndexOutOfBoundsException, IOException
	{
		return this.__get().append(__c, __s, __e);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/23
	 */
	@Override
	public Appendable append(char __c)
		throws IOException
	{
		return this.__get().append(__c);
	}
	
	/**
	 * Returns the appendable.
	 * 
	 * @return The appendable.
	 * @throws IllegalStateException If it was garbage collected.
	 * @since 2022/06/23
	 */
	private Appendable __get()
		throws IllegalStateException
	{
		Appendable rv = this._wrapped.get();
		if (rv == null)
			throw new IllegalStateException("GCGC");
		
		return rv;
	}
}
