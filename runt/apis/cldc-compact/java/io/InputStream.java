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

public abstract class InputStream
	implements Closeable
{
	/**
	 * Initializes the base input stream.
	 *
	 * @since 2018/10/13
	 */
	public InputStream()
	{
	}
	
	public abstract int read()
		throws IOException;
	
	public int available()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	public void close()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	public void mark(int __a)
	{
		throw new todo.TODO();
	}
	
	public boolean markSupported()
	{
		throw new todo.TODO();
	}
	
	public int read(byte[] __a)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	public int read(byte[] __a, int __b, int __c)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	public void reset()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	public long skip(long __a)
		throws IOException
	{
		throw new todo.TODO();
	}
}

