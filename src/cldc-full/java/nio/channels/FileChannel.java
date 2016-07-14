// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Set;

public abstract class FileChannel
	extends Object
	implements SeekableByteChannel
{
	protected FileChannel()
	{
		throw new Error("TODO");
	}
	
	public abstract void force(boolean __a)
		throws IOException;
	
	public abstract long position()
		throws IOException;
	
	public abstract FileChannel position(long __a)
		throws IOException;
	
	public abstract int read(ByteBuffer __a)
		throws IOException;
	
	public abstract int read(ByteBuffer __a, long __b)
		throws IOException;
	
	public abstract long size()
		throws IOException;
	
	public abstract FileChannel truncate(long __a)
		throws IOException;
	
	public abstract int write(ByteBuffer __a)
		throws IOException;
	
	public abstract int write(ByteBuffer __a, long __b)
		throws IOException;
	
	public static FileChannel open(Path __a, Set<? extends OpenOption> __b,
		FileAttribute<?>... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
	
	public static FileChannel open(Path __a, OpenOption... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw new Error("TODO");
	}
}

