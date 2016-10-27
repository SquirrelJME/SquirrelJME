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

public abstract class InputStream
	implements Closeable
{
	public InputStream()
	{
		throw new Error("TODO");
	}
	
	public abstract int read()
		throws IOException;
	
	public int available()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	public void mark(int __a)
	{
		throw new Error("TODO");
	}
	
	public boolean markSupported()
	{
		throw new Error("TODO");
	}
	
	public int read(byte[] __a)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	public int read(byte[] __a, int __b, int __c)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	public void reset()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	public long skip(long __a)
		throws IOException
	{
		throw new Error("TODO");
	}
}

