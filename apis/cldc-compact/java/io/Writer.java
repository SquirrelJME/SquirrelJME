// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.io;

/**
 * TODO.
 *
 * Note that code which relies on {@code Writer} being {@code Flushable} will
 * fail.
 *
 * @since 2016/08/12
 */
public abstract class Writer
	implements Appendable, Closeable
{
	protected Object lock;
	
	protected Writer()
	{
		super();
		throw new Error("TODO");
	}
	
	protected Writer(Object __a)
	{
		super();
		throw new Error("TODO");
	}
	
	@Override
	public abstract void close()
		throws IOException;
	
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
		throw new Error("TODO");
	}
	
	@Override
	public Writer append(CharSequence __a, int __b, int __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public Writer append(char __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public void write(int __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public void write(char[] __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public void write(String __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public void write(String __a, int __b, int __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
}

