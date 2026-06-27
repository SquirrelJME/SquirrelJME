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
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.MarkableInputStream;
import cc.squirreljme.runtime.gcf.ContentTypeUtil;
import cc.squirreljme.runtime.gcf.CustomConnectionFactory;
import cc.squirreljme.runtime.gcf.InputStreamConnection;
import cc.squirreljme.runtime.media.AbstractPlayer;
import cc.squirreljme.runtime.media.NullPlayer;
import cc.squirreljme.runtime.media.PlayerProvider;
import cc.squirreljme.runtime.media.SystemNanoTimeBase;
import cc.squirreljme.runtime.media.ericsson.EricssonMelodyPlayer;
import cc.squirreljme.runtime.media.midi.MidiControlPlayer;
import cc.squirreljme.runtime.media.midi.MidiPlayer;
import cc.squirreljme.runtime.media.wav.WavPlayer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.ServiceLoader;
import java.util.Set;
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
	
	/** GCF services, this is needed for supported types. */
	private static volatile ServiceLoader<CustomConnectionFactory> _gcf;
	
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
			__contentType = ContentTypeUtil.guess(__in);
			
			/* {@squirreljme.error EA1a Could not determine the content
			type of the input data.} */
			if (__contentType == null)
				throw new MediaException(
					__error__("EA1a"));
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
		
		// Lookup through services
		for (PlayerProvider provider : AbstractPlayer.providers())
			if (provider.acceptsContentType(__contentType) &&
				provider.acceptsInputConnection())
				return provider.viaInputConnection(
					new InputStreamConnection(__in), __contentType);
		
		// TODO: Implement these as they are standard Media API, currently
		// TODO: they use NullPlayer as to not fail
		switch (__contentType)
		{
				// Standardized but not yet supported by SquirrelJME
			case "audio/basic":
				
			case "audio/aiff":
			case "audio/x-aiff":
				
			case "audio/x-tone-seq":
				
			case "audio/mpeg":
			case "video/mpeg":
				Debugging.todoNote("Support media: %s", __contentType);
				return new NullPlayer(__contentType);
		}
		
		/* {@squirreljme.error EA1b Unsupported content type. (The content
		type)} */
		throw new MediaException(
			__error__("EA1b: %s", __contentType));
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
				throw new MediaException(
					__error__("EA1c: %s", __locator));
			
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
	
	/**
	 * Returns the set of content types which are supported by this player for
	 * the given protocol.
	 *
	 * @param __protocol The protocol scheme to check, if {@code null} then
	 * this will be all protocols.
	 * @return The set of supported content types.
	 * @see Manager#getSupportedProtocols(String) 
	 * @since 2026/06/27
	 */
	@Api
	@Language("mime-type-reference")
	public static String[] getSupportedContentTypes(
		@Language("http-url-reference") String __protocol)
	{
		// These may be modified based on the protocol type
		boolean viaInput = false;
		
		// No specified protocol, means every type
		if (__protocol == null)
		{
			viaInput = true;
		}
		
		// Otherwise, we need to check the protocols for valid types
		else
		{
			// Go through and find this protocol
			for (CustomConnectionFactory gcf : Manager.__gcf())
			{
				// Wrong one?
				if (!__protocol.equals(gcf.scheme()))
					continue;
				
				// Is this an input type connection?
				if (gcf.implementsInterface(InputConnection.class))
					viaInput = true;
			}
		}
		
		// Look through player services
		Set<String> result = new LinkedHashSet<>();
		for (PlayerProvider provider : AbstractPlayer.providers())
			if (viaInput && provider.acceptsInputConnection())
				result.addAll(Arrays.asList(provider.acceptsContentTypes()));
		
		// Return all the supported types
		return result.toArray(new String[result.size()]);
	}
	
	/**
	 * Returns the set of protocols types which are supported by this player
	 * for the given content type.
	 *
	 * @param __contentType The content type to check, if {@code null} then
	 * this will be all content types.
	 * @return The set of supported protocol schemes.
	 * @see Manager#getSupportedContentTypes(String) 
	 * @since 2026/06/27
	 */
	@Api
	@Language("http-url-reference")
	public static String[] getSupportedProtocols(
		@Language("mime-type-reference") String __contentType)
	{
		// Go through each protocol to determine what it likes
		Set<String> result = new LinkedHashSet<>();
		for (PlayerProvider provider : AbstractPlayer.providers())
		{
			// Is this the wrong content type if we wanted a specific one?
			if (__contentType != null &&
				!provider.acceptsContentType(__contentType))
				continue;
			
			// Need to go through each connection type to find the appropriate
			// connection types
			for (CustomConnectionFactory gcf : Manager.__gcf())
			{
				boolean hit = false;
				
				// Uses an input stream and protocol provides that?
				if (provider.acceptsInputConnection() &&
					gcf.implementsInterface(InputConnection.class))
					hit = true;
					
				// Does this meet the criteria?
				if (hit)
					result.add(gcf.scheme());
			}
		}
		
		// Return all the supported types
		return result.toArray(new String[result.size()]);
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
	 * Returns the services for loading GCF connections.
	 *
	 * @return The iteration over the GCF connection service loader.
	 * @since 2026/06/27
	 */
	private static Iterable<CustomConnectionFactory> __gcf()
	{
		// Need to load in services?
		ServiceLoader<CustomConnectionFactory> result = Manager._gcf;
		if (result == null)
		{
			result = ServiceLoader.load(CustomConnectionFactory.class);
			Manager._gcf = result;
		}
		
		// Iterate over this
		return result;
	}
}
