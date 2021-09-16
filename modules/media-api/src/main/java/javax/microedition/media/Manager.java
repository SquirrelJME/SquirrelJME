// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media;

import cc.squirreljme.runtime.media.NullPlayer;
import cc.squirreljme.runtime.media.SystemNanoTimeBase;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.protocol.DataSource;

public final class Manager
{
	public static final String MIDI_DEVICE_LOCATOR =
		"device://midi";
	
	public static final String TONE_DEVICE_LOCATOR =
		"device://tone";
	
	/**
	 * Not used.
	 *
	 * @since 2017/02/28
	 */
	private Manager()
	{
	}
	
	public static Player createPlayer(InputStream __a, String __b)
		throws IOException, MediaException
	{
		todo.TODO.note("createPlayer(%s, %s)%n", __a, __b);
		if (true)
			return new NullPlayer(__b);
		
		if (false)
			throw new IOException();
		if (false)
			throw new MediaException();
		throw new todo.TODO();
	}
	
	public static Player createPlayer(String __a)
		throws IOException, MediaException
	{
		todo.TODO.note("createPlayer(%s)%n", __a);
		if (true)
			return new NullPlayer("application/octet-stream");
			
		if (false)
			throw new IOException();
		if (false)
			throw new MediaException();
		throw new todo.TODO();
	}
	
	public static Player createPlayer(DataSource __a)
		throws IOException, MediaException
	{
		todo.TODO.note("createPlayer(%s)%n", __a);
		if (true)
			return new NullPlayer(__a.getContentType());
		
		if (false)
			throw new IOException();
		if (false)
			throw new MediaException();
		throw new todo.TODO();
	}
	
	public static String[] getSupportedContentTypes(String __a)
	{
		todo.DEBUG.note("getSupportedContentTypes(%s)%n", __a);
		throw new todo.TODO();
	}
	
	public static String[] getSupportedProtocols(String __a)
	{
		todo.DEBUG.note("getSupportedProtocols(%s)%n", __a);
		throw new todo.TODO();
	}
	
	/**
	 * Returns the default timebase.
	 *
	 * @return The default timebase.
	 * @since 2019/04/15
	 */
	public static TimeBase getSystemTimeBase()
	{
		return new SystemNanoTimeBase();
	}
	
	public static void playTone(int __a, int __b, int __c)
		throws MediaException
	{
		todo.DEBUG.note("playTone(%d, %d, %d)%n", __a, __b, __c);
		if (true)
			throw new MediaException("TODO");
		
		if (false)
			throw new MediaException();
		throw new todo.TODO();
	}
}


