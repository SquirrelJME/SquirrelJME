// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;

public abstract class Reader
	implements Closeable
{
	/** The lock to use when performing read operations. */
	@ImplementationNote("This is only used with the skip() method.")
	protected Object lock;
	
	/**
	 * Initializes the base reader.
	 *
	 * @since 2018/10/13
	 */
	@ImplementationNote("The lock should be initialized to this, however " +
		"this would result in the reader itself never able to be freed " +
		"because it refers to itself.")
	protected Reader()
	{
		this.lock = null;
	}
	
	/**
	 * Initializes the reader to lock against the given object.
	 *
	 * @param __l The object to lock against.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/13
	 */
	protected Reader(Object __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		this.lock = __l;
	}
	
	public abstract void close()
		throws IOException;
	
	public abstract int read(char[] __a, int __b, int __c)
		throws IOException;
	
	public void mark(int __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public boolean markSupported()
	{
		throw new todo.TODO();
	}
	
	public int read()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public int read(char[] __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public boolean ready()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public void reset()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public long skip(long __a)
		throws IOException
	{
		// To prevent clashes when skipping characters
		synchronized (this.__lock())
		{
			if (false)
				throw new IOException();
			throw new todo.TODO();
		}
	}
	
	/**
	 * Returns the locking object.
	 *
	 * @return The locking object.
	 * @since 2018/10/13
	 */
	final Object __lock()
	{
		Object rv = this.lock;
		return (rv == null ? this : rv);
	}
}

