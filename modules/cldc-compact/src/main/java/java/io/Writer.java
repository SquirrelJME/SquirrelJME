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

public abstract class Writer
	implements Appendable, Closeable
{
	protected Object lock;
	
	protected Writer()
	{
		throw new todo.TODO();
	}
	
	protected Writer(Object __a)
	{
		throw new todo.TODO();
	}
	
	public abstract void flush()
		throws IOException;
	
	public abstract void write(char[] __a, int __b, int __c)
		throws IOException;
	
	@Override
	public Writer append(CharSequence __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public Writer append(CharSequence __a, int __b, int __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	@Override
	public Writer append(char __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public void write(int __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public void write(char[] __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public void write(String __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
	
	public void write(String __a, int __b, int __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new todo.TODO();
	}
}

