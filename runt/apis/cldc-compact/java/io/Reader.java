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

public abstract class Reader
	implements Closeable
{
	protected Object lock;
	
	protected Reader()
	{
		super();
		throw new todo.TODO();
	}
	
	protected Reader(Object __a)
	{
		super();
		throw new todo.TODO();
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
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
}

