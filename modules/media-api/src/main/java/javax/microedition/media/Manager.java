// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.media;

import cc.squirreljme.jvm.mle.AudioStreamShelf;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.VMType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.MarkableInputStream;
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import cc.squirreljme.runtime.media.NullPlayer;
import cc.squirreljme.runtime.media.SystemNanoTimeBase;
import cc.squirreljme.runtime.media.midi.MidiControlPlayer;
import cc.squirreljme.runtime.media.midi.MidiPlayer;
import cc.squirreljme.runtime.media.mld.IMelodyPlayer;
import cc.squirreljme.runtime.media.wav.WavPlayer;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.InputConnection;
import javax.microedition.media.control.MIDIControl;
import javax.microedition.media.protocol.DataSource;
import org.intellij.lang.annotations.Language;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * This is used to create instances of {@link Player} which are then used to
 * play back any media. 
 *
 * @since 2025/05/04
 */
@Api
public final class Manager
{
	/**
	 * Special player which is used to allow access to a {@link MIDIControl}.
	 */
	@Api
	@Language("http-url-reference")
	public static final String MIDI_DEVICE_LOCATOR =
		"device://midi";
	
	/** Locator for the playback of simple tones. */
	@Api
	@Language("http-url-reference")
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
	
	/**
	 * Creates a player which is capable of playing data from the given
	 * input stream.
	 *
	 * @param __in The source to play from.
	 * @param __contentType The content type of the source.
	 * @return The resultant player.
	 * @throws IOException If the source could not be read.
	 * @throws MediaException If the source and/or content type are not
	 * supported.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If audio playback is not supported.
	 * @since 2025/05/04
	 */
	@Api
	public static Player createPlayer(InputStream __in,
		@Language("mime-type-reference") String __contentType)
		throws IOException, MediaException, NullPointerException,
			SecurityException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Always make these streams markable
		if (!__in.markSupported())
			__in = new MarkableInputStream(__in);
		
		// Do we need to guess the content type for the stream?
		if (__contentType == null)
		{
			__contentType = Manager.__guessContentType(__in);
			
			/* {@squirreljme.error EA1a Could not determine the content
			type of the input data.} */
			if (__contentType == null)
				throw new MediaException("EA1a");
		}
		
		// Native audio stream support?
		try
		{
			if (AudioStreamShelf.decoderSupports(__contentType))
				throw Debugging.todo();
		}
		catch (MLECallError ignored)
		{
			// An error occurred while determining this
		}
		
		// Depends on the content type
		switch (__contentType)
		{
				// MIDI
			case "application/x-midi":
			case "audio/midi":
			case "audio/x-mid":
			case "audio/x-midi":
			case "music/crescendo":
				return new MidiPlayer(new InputStreamConnection(__in));
				
				// i-melody MLD
			case "application/x-mld":
			case "application/x-mld-music":
			case "audio/x-mld":
				if (RuntimeShelf.vmType() == VMType.SPRINGCOAT)
				{
					Debugging.todoNote("Accelerated MLD support.");
					return new NullPlayer(__contentType);
				}
				
				// Setup player
				return new IMelodyPlayer(new InputStreamConnection(__in));
				
			case "audio/vnd.wave":
			case "audio/wav":
			case "audio/wave":
			case "audio/x-wav":
				return new WavPlayer(new InputStreamConnection(__in));
				
				// Standardized but not yet supported by SquirrelJME
			case "audio/basic":
				
			case "audio/aiff":
			case "audio/x-aiff":
				
			case "audio/x-tone-seq":
				
			case "audio/mpeg":
			case "video/mpeg":
				
			case "application/x-smaf":
				Debugging.todoNote("Support media: %s", __contentType);
				return new NullPlayer(__contentType);
		}
		
		/* {@squirreljme.error EA1b Unsupported content type. (The content
		type)} */
		throw new MediaException("EA1b: " + __contentType);
	}
	
	/**
	 * Creates a player which plays from the given locator source. If a known
	 * locator is not used, this will fall back to sourcing from GCF.
	 *
	 * @param __locator The source to use.
	 * @return The resultant player.
	 * @throws IOException If the source could not be read.
	 * @throws MediaException If the source and/or content type are not
	 * supported.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If audio playback is not supported.
	 * @since 2025/05/04
	 */
	@Api
	public static Player createPlayer(
		@Language("http-url-reference") String __locator)
		throws IOException, MediaException, NullPointerException,
			SecurityException
	{
		if (__locator == null)
			throw new NullPointerException("NARG");
		
		// Using pre-defined locators?
		switch (__locator)
		{
				// MIDI devices?
			case Manager.MIDI_DEVICE_LOCATOR:
				return MidiControlPlayer.newMidiPlayer();
				
				// Tone?
			case Manager.TONE_DEVICE_LOCATOR:
				throw Debugging.todo(__locator);
		}
		
		// Use GCF to open the data instead
		try (Connection netSource = Connector.open(__locator))
		{
			/* {@squirreljme.error EA1c The specified locator does not
			support being read from. (The locator)} */
			if (!(netSource instanceof InputConnection))
				throw new MediaException("EA1c: " + __locator);
			
			// Open source and load from it
			try (InputStream in = ((InputConnection)netSource)
				.openInputStream())
			{
				return Manager.createPlayer(in, null);
			}
		}
	}
	
	@Api
	public static Player createPlayer(DataSource __source)
		throws IOException, MediaException
	{
		if (__source == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo(__source);
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
	@Language("mime-type-reference")
	private static String __guessContentType(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Read in header completely
		__in.mark(12);
		int a = __in.read();
		int b = __in.read();
		int c = __in.read();
		int d = __in.read();
		int e = __in.read();
		int f = __in.read();
		int g = __in.read();
		int h = __in.read();
		int i = __in.read();
		int j = __in.read();
		int k = __in.read();
		int l = __in.read();
		__in.reset();
		
		// MIDI (MThd/MTrk)
		if ((a == 'M' && b == 'T' && c == 'h' && d == 'd') ||
			(a == 'M' && b == 'T' && c == 'r' && d == 'k'))
			return "audio/midi";
		
		// WAVE
		if (a == 'R' && b == 'I' && c == 'F' && d == 'F' &&
			i == 'W' && j == 'A' && k == 'V' && l == 'E')
			return "audio/wave";
		
		// AIFF
		if (a == 'F' && b == 'O' && c == 'R' && d == 'M' &&
			i == 'A' && j == 'I' && k == 'F' && l == 'F')
			return "audio/aiff";
		
		// Basic sound
		if (a == 0x2E && b == 0x73 && c == 0x6E && d == 0x64)
			return "audio/basic";
		
		// i-melody MLD
		if ((a == 'm' && b == 'e' && c == 'l' && d == 'o'))
			return "audio/x-mld";
		
		// SMAF
		if (a == 'M' && b == 'M' && c == 'M' && d == 'D' &&
			i == 'C' && j == 'N' && k == 'T' && l == 'I')
			return "application/x-smaf";
		
		// Unknown
		return null;
	}
}


