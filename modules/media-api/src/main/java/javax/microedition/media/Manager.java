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
import cc.squirreljme.runtime.cldc.io.MarkableInputStream;
import cc.squirreljme.runtime.media.NullPlayer;
import cc.squirreljme.runtime.media.SystemNanoTimeBase;
import cc.squirreljme.runtime.media.midi.MidiControlPlayer;
import cc.squirreljme.runtime.media.midi.MidiPlayer;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.media.control.MIDIControl;
import javax.microedition.media.protocol.DataSource;

@Api
public final class Manager
{
	/**
	 * Special player which is used to allow access to a {@link MIDIControl}.
	 */
	@Api
	public static final String MIDI_DEVICE_LOCATOR =
		"device://midi";
	
	@Api
	public static final String TONE_DEVICE_LOCATOR =
		"device://tone";
	
	/** The system time base, used for song synchronization. */
	private static final TimeBase _SYSTEM_TIME_BASE = 
		new SystemNanoTimeBase();
	
	/**
	 * Not used.
	 *
	 * @since 2017/02/28
	 */
	private Manager()
	{
	}
	
	@Api
	public static Player createPlayer(InputStream __in, String __contentType)
		throws IOException, MediaException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Always make these streams markable
		if (!__in.markSupported())
			__in = new MarkableInputStream(__in);
		
		// Do we need to guess the content type for the stream?
		if (__contentType == null)
			__contentType = Manager.__guessContentType(__in);
		
		// Depends on the content type
		switch (__contentType)
		{
				// MIDI
			case "application/x-midi":
			case "audio/midi":
			case "audio/x-mid":
			case "audio/x-midi":
			case "music/crescendo":
				return new MidiPlayer(__in);
		}
		
		Debugging.todoNote("createPlayer(%s, %s)%n", __in, __contentType);
		if (true)
			return new NullPlayer(__contentType);
		
		if (false)
			throw new IOException();
		if (false)
			throw new MediaException();
		throw Debugging.todo();
	}
	
	@Api
	public static Player createPlayer(String __locator)
		throws IOException, MediaException, NullPointerException
	{
		if (__locator == null)
			throw new NullPointerException("NARG");
		
		// Using pre-defined locators?
		switch (__locator)
		{
				// MIDI devices?
			case Manager.MIDI_DEVICE_LOCATOR:
				return MidiControlPlayer.newMidiPlayer();
		}
		
		Debugging.todoNote("createPlayer(%s)%n", __locator);
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
		return Manager._SYSTEM_TIME_BASE;
	}
	
	@Api
	public static void playTone(int __note, int __duration, int __volume)
		throws IllegalArgumentException, MediaException
	{
		Debugging.todoNote("playTone(%d, %d, %d)",
			__note, __duration, __volume);
	}
	
	/**
	 * Attempts to guess the content type of the stream.
	 * 
	 * @param __in The stream to guess.
	 * @return The guessed content type or {@code null} if it could not be
	 * determined.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/24
	 */
	private static String __guessContentType(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Read in header completely
		__in.mark(4);
		int a = __in.read();
		int b = __in.read();
		int c = __in.read();
		int d = __in.read();
		__in.reset();
		
		// MIDI (MThd/MTrk)
		if ((a == 'M' && b == 'T' && c == 'h' && d == 'd') ||
			(a == 'M' && b == 'T' && c == 'r' && d == 'k'))
			return "audio/midi";
		
		// Unknown
		return null;
	}
}


