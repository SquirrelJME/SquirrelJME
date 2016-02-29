// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.io;

public abstract class Reader
	implements Closeable
{
	protected Object lock;
	
	protected Reader()
	{
		super();
		throw new Error("TODO");
	}
	
	protected Reader(Object __a)
	{
		super();
		throw new Error("TODO");
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
		throw new Error("TODO");
	}
	
	public boolean markSupported()
	{
		throw new Error("TODO");
	}
	
	public int read()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public int read(char[] __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public boolean ready()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public void reset()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public long skip(long __a)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
}

