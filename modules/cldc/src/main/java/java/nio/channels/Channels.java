// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.channels;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.InputStream;
import java.io.OutputStream;

public final class Channels
{
	private Channels()
	{
		throw Debugging.todo();
	}
	
	public static ReadableByteChannel newChannel(InputStream __a)
	{
		throw Debugging.todo();
	}
	
	public static WritableByteChannel newChannel(OutputStream __a)
	{
		throw Debugging.todo();
	}
	
	public static InputStream newInputStream(ReadableByteChannel __a)
	{
		throw Debugging.todo();
	}
	
	public static OutputStream newOutputStream(WritableByteChannel __a)
	{
		throw Debugging.todo();
	}
}

