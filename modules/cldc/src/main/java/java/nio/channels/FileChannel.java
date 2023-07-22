// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.channels;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

@Api
public abstract class FileChannel
	implements SeekableByteChannel
{
	@Api
	protected FileChannel()
	{
		throw Debugging.todo();
	}
	
	@Api
	public abstract void force(boolean __a)
		throws IOException;
	
	@Override
	public abstract FileChannel position(long __a)
		throws IOException;
	
	@Api
	public abstract int read(ByteBuffer __a, long __b)
		throws IOException;
	
	@Override
	public abstract FileChannel truncate(long __a)
		throws IOException;
	
	@Api
	public abstract int write(ByteBuffer __a, long __b)
		throws IOException;
	
	@Api
	public static FileChannel open(Path __a, Set<? extends OpenOption> __b,
		FileAttribute<?>... __c)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
	
	@Api
	public static FileChannel open(Path __a, OpenOption... __b)
		throws IOException
	{
		if (false)
			throw new IOException();
		throw Debugging.todo();
	}
}

