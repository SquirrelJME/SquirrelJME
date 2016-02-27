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

public abstract class OutputStream
	implements Closeable, Flushable
{
	public OutputStream()
	{
		throw new Error("TODO");
	}
	
	public abstract void write(int __a)
		throws IOException;
	
	public void close()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	public void flush()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	public void write(byte[] __a)
		throws IOException
	{
		throw new Error("TODO");
	}
	
	public void write(byte[] __a, int __b, int __c)
		throws IOException
	{
		throw new Error("TODO");
	}
}

