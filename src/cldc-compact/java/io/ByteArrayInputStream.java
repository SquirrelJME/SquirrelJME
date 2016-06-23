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

public class ByteArrayInputStream
	extends InputStream
{
	protected byte[] buf;
	
	protected int count;
	
	protected int mark;
	
	protected int pos;
	
	public ByteArrayInputStream(byte[] __a)
	{
		super();
		throw new Error("TODO");
	}
	
	public ByteArrayInputStream(byte[] __a, int __b, int __c)
	{
		super();
		throw new Error("TODO");
	}
	
	@Override
	public int available()
	{
		synchronized (this)
		{
			throw new Error("TODO");
		}
	}
	
	@Override
	public void close()
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	@Override
	public void mark(int __a)
	{
		throw new Error("TODO");
	}
	
	@Override
	public boolean markSupported()
	{
		throw new Error("TODO");
	}
	
	@Override
	public int read()
	{
		synchronized (this)
		{
			throw new Error("TODO");
		}
	}
	
	@Override
	public int read(byte[] __a, int __b, int __c)
	{
		synchronized (this)
		{
			throw new Error("TODO");
		}
	}
	
	@Override
	public void reset()
	{
		synchronized (this)
		{
			throw new Error("TODO");
		}
	}
	
	@Override
	public long skip(long __a)
	{
		synchronized (this)
		{
			throw new Error("TODO");
		}
	}
}

