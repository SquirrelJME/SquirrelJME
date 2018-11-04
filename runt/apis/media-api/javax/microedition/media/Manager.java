// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media;

import java.io.InputStream;
import java.io.IOException;
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
		todo.DEBUG.note("createPlayer(%s, %s)%n", __a, __b);
		if (false)
			throw new IOException();
		if (false)
			throw new MediaException();
		throw new todo.TODO();
	}
	
	public static Player createPlayer(String __a)
		throws IOException, MediaException
	{
		todo.DEBUG.note("createPlayer(%s)%n", __a);
		if (false)
			throw new IOException();
		if (false)
			throw new MediaException();
		throw new todo.TODO();
	}
	
	public static Player createPlayer(DataSource __a)
		throws IOException, MediaException
	{
		todo.DEBUG.note("createPlayer(%s)%n", __a);
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
	
	public static TimeBase getSystemTimeBase()
	{
		todo.DEBUG.note("getSystemTimeBase()%n");
		throw new todo.TODO();
	}
	
	public static void playTone(int __a, int __b, int __c)
		throws MediaException
	{
		todo.DEBUG.note("playTone(%d, %d, %d)%n", __a, __b, __c);
		if (false)
			throw new MediaException();
		throw new todo.TODO();
	}
}


