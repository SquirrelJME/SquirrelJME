// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.media.NullPlayer;
import cc.squirreljme.runtime.media.SystemNanoTimeBase;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.protocol.DataSource;

@Api
public final class Manager
{
	@Api
	public static final String MIDI_DEVICE_LOCATOR =
		"device://midi";
	
	@Api
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
	
	@Api
	public static Player createPlayer(InputStream __a, String __b)
		throws IOException, MediaException
	{
		Debugging.todoNote("createPlayer(%s, %s)%n", __a, __b);
		if (true)
			return new NullPlayer(__b);
		
		if (false)
			throw new IOException();
		if (false)
			throw new MediaException();
		throw Debugging.todo();
	}
	
	@Api
	public static Player createPlayer(String __a)
		throws IOException, MediaException
	{
		Debugging.todoNote("createPlayer(%s)%n", __a);
		if (true)
			return new NullPlayer("application/octet-stream");
			
		if (false)
			throw new IOException();
		if (false)
			throw new MediaException();
		throw Debugging.todo();
	}
	
	@Api
	public static Player createPlayer(DataSource __a)
		throws IOException, MediaException
	{
		Debugging.todoNote("createPlayer(%s)%n", __a);
		if (true)
			return new NullPlayer(__a.getContentType());
		
		if (false)
			throw new IOException();
		if (false)
			throw new MediaException();
		throw Debugging.todo();
	}
	
	@Api
	public static String[] getSupportedContentTypes(String __a)
	{
		Debugging.debugNote("getSupportedContentTypes(%s)%n", __a);
		throw Debugging.todo();
	}
	
	@Api
	public static String[] getSupportedProtocols(String __a)
	{
		Debugging.debugNote("getSupportedProtocols(%s)%n", __a);
		throw Debugging.todo();
	}
	
	/**
	 * Returns the default timebase.
	 *
	 * @return The default timebase.
	 * @since 2019/04/15
	 */
	@Api
	public static TimeBase getSystemTimeBase()
	{
		return new SystemNanoTimeBase();
	}
	
	@Api
	public static void playTone(int __note, int __duration, int __volume)
		throws IllegalArgumentException, MediaException
	{
		Debugging.todoNote("playTone(%d, %d, %d)",
			__note, __duration, __volume);
	}
}


