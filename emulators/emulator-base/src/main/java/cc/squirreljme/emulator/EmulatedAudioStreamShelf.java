// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.AudioStreamShelf;
import cc.squirreljme.jvm.mle.brackets.AudioStreamBracket;
import cc.squirreljme.jvm.mle.brackets.AudioStreamConnectionBracket;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamPlayer;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.jvm.mle.constants.AudioStreamChannels;
import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.jvm.mle.constants.AudioStreamRate;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Emulated {@link AudioStreamShelf}.
 *
 * @since 2025/05/06
 */
public class EmulatedAudioStreamShelf
{
	/** The state pointer. */
	static volatile long _statePtr;
	
	/**
	 * Not used. 
	 *
	 * @since 2025/05/04
	 */
	private EmulatedAudioStreamShelf()
	{
	}
	
	/**
	 * Attaches the given renderer to the stream.
	 *
	 * @param __stream The stream to render to.
	 * @param __renderer The renderer to register.
	 * @throws MLECallError On null arguments or if the renderer could not
	 * be registered.
	 * @since 2025/05/04
	 */
	@SquirrelJMEVendorApi
	public static AudioStreamConnectionBracket attach(
		@NotNull AudioStreamBracket __stream,
		@NotNull AudioStreamRenderer __renderer)
		throws MLECallError
	{
		if (__stream == null || __renderer == null)
			throw new MLECallError("NARG");
		
		// Make sure the dynamic library is initialized
		long statePtr = EmulatedAudioStreamShelf.__dylibInit();
		
		// Wrap renderer
		return new EmulatedAudioSourceBracket(statePtr, 
			EmulatedAudioStreamShelf.__attach(statePtr,
				((EmulatedAudioStreamBracket)__stream).streamPtr,
				__renderer), (EmulatedAudioStreamBracket)__stream,
			__renderer);
	}
	
	/**
	 * Creates a native audio stream.
	 *
	 * @param __name The name of the audio stream.
	 * @param __format The format of the audio stream, {@code -1} means to
	 * use the preferred format that the system uses.
	 * @param __rate The rate of the audio stream, {@code -1} means to use
	 * the preferred rate that the system uses.
	 * @param __channels The number of channels in the stream and their
	 * mappings.
	 * @return The resultant audio stream.
	 * @throws MLECallError On null arguments, invalid arguments, or if the
	 * stream could not be created.
	 * @since 2025/05/04
	 */
	@NotNull
	@SquirrelJMEVendorApi
	public static AudioStreamBracket create(
		@NotNull String __name,
		@MagicConstant(valuesFromClass = AudioStreamFormat.class)
			int __format,
		@MagicConstant(valuesFromClass = AudioStreamRate.class)
			int __rate,
		@MagicConstant(valuesFromClass = AudioStreamChannels.class)
			int __channels)
		throws MLECallError
	{
		// Make sure the dynamic library is initialized
		long statePtr = EmulatedAudioStreamShelf.__dylibInit();
		
		// Create new stream and wrap it
		return new EmulatedAudioStreamBracket(statePtr,
			EmulatedAudioStreamShelf.__create(statePtr, __name,
				__format, __rate, __channels),
			__name, __format, __rate, __channels);
	}
	
	/**
	 * Creates a decoder that is capable of playing the given audio format.
	 *
	 * @param __urlOrFile The URL or file name.
	 * @param __mimeType The mime-type of the data.
	 * @param __format The format to use as a suggestion, if {@code -1} then
	 * the decoder will use its preferred format. If the player does
	 * not support the given format it should treat this as if it
	 * were {@code -1}.
	 * @param __rate The frequency of the audio, if {@code -1} then the
	 * decoder will use its preferred rate.
	 * @param __buf The buffer of the stream data.
	 * @param __off The offset into the buffer.
	 * @param __len The length of the buffer data.
	 * @return The player which is capable of playing the given audio.
	 * @throws MLECallError If the decoder could not be created.
	 * @since 2025/05/04
	 */
	@SquirrelJMEVendorApi
	@NotNull
	public static AudioStreamPlayer decoder(
		@Nullable String __urlOrFile,
		@Nullable @Language("mime-type-reference") String __mimeType,
		@MagicConstant(valuesFromClass = AudioStreamFormat.class)
			int __format,
		@MagicConstant(valuesFromClass = AudioStreamRate.class)
			int __rate,
		@MagicConstant(valuesFromClass = AudioStreamChannels.class)
			int __channels,
		@NotNull byte[] __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws MLECallError
	{
		if (__urlOrFile == null && __mimeType == null)
			throw new MLECallError("NARG");
		if (__format < -1 || __format >= AudioStreamFormat.NUM_FORMATS ||
			__rate < -1 || __channels <= 0)
			throw new MLECallError("Invalid rate/format/channels");
		
		// Make sure the dynamic library is initialized
		long statePtr = EmulatedAudioStreamShelf.__dylibInit();
		
		throw Debugging.todo();
	}
	
	/**
	 * Checks if the native audio system supports decoding the given format.
	 *
	 * @param __contentType The content type.
	 * @return If the audio system supports the given format.
	 * @throws MLECallError On null arguments.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public static boolean decoderSupports(
		@NotNull @Language("mime-type-reference") String __contentType)
		throws MLECallError
	{
		if (__contentType == null)
			throw new MLECallError("NARG");
		
		// Depends on the type
		switch (__contentType)
		{
				// Not yet supported
			default:
				return false;
		}
	}
	
	/**
	 * Disconnects the given connection.
	 *
	 * @param __conn The connection to disconnect.
	 * @throws MLECallError On null arguments or if the connection could not
	 * be disconnected.
	 * @since 2025/05/25
	 */
	@SquirrelJMEVendorApi
	public static void disconnect(
		@NotNull AudioStreamConnectionBracket __conn)
		throws MLECallError
	{
		if (__conn == null)
			throw new MLECallError("NARG");
		
		// Make sure the dynamic library is initialized
		long statePtr = EmulatedAudioStreamShelf.__dylibInit();
		
		EmulatedAudioStreamShelf.__disconnect(statePtr,
			((EmulatedAudioConnectionBracket)__conn).instancePtr);
	}
	
	/**
	 * Creates a {@link MidiPortBracket} attached to a decoder that is capable
	 * of playing and decoding MIDI.
	 *
	 * @param __mimeType The mime type of the decoder.
	 * @param __format The format to use as a suggestion, if {@code -1} then
	 * the decoder will use its preferred format. If the player does
	 * not support the given format it should treat this as if it
	 * were {@code -1}.
	 * @param __rate The frequency of the audio, if {@code -1} then the
	 * decoder will use its preferred rate.
	 * @return The MIDI port.
	 * @throws MLECallError On null arguments or if the mime type does not
	 * support MIDI playback.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	@NotNull
	public static MidiPortBracket midiPort(
		@NotNull @Language("mime-type-reference") String __mimeType,
		@MagicConstant(valuesFromClass = AudioStreamFormat.class)
			int __format,
		@MagicConstant(valuesFromClass = AudioStreamRate.class)
			int __rate,
		@MagicConstant(valuesFromClass = AudioStreamChannels.class)
			int __channels)
		throws MLECallError
	{
		if (__mimeType == null)
			throw new MLECallError("NARG");
		
		if (__format < -1 || __format >= AudioStreamFormat.NUM_FORMATS ||
			__rate < -1 || __channels <= 0)
			throw new MLECallError("Invalid rate/format/channels");
		
		// Make sure the dynamic library is initialized
		long statePtr = EmulatedAudioStreamShelf.__dylibInit();
		
		throw Debugging.todo();
	}
	
	/**
	 * Returns the renderer that is associated with a {@link MidiPortBracket}
	 * that can be used to render to an audio stream.
	 *
	 * @param __midiPort The MIDI port to get the audio stream from.
	 * @return The audio stream renderer for the given MIDI port.
	 * @throws MLECallError On null arguments or if the MIDI port is not
	 * one that is managed by audio streams.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	@NotNull
	public static AudioStreamRenderer midiRenderer(
		@NotNull MidiPortBracket __midiPort)
		throws MLECallError
	{
		if (__midiPort == null)
			throw new MLECallError("NARG");
		
		// Make sure the dynamic library is initialized
		long statePtr = EmulatedAudioStreamShelf.__dylibInit();
		
		throw Debugging.todo();
	}
	
	/**
	 * Attaches the given renderer to the stream.
	 *
	 * @param __statePtr The state pointer.
	 * @param __streamPtr The stream pointer.
	 * @param __renderer The renderer to attach.
	 * @return The pointer to the renderer.
	 * @throws MLECallError If the renderer could not be attached.
	 * @since 2025/05/18
	 */
	native static long __attach(long __statePtr, long __streamPtr,
		AudioStreamRenderer __renderer)
		throws MLECallError;
	
	/**
	 * Creates a new audio stream.
	 *
	 * @param __statePtr The state pointer.
	 * @param __name The audio stream name.
	 * @param __format The stream format.
	 * @param __rate The stream rate.
	 * @param __channels The stream channels.
	 * @return The audio stream pointer.
	 * @throws MLECallError If it could not be created.
	 * @since 2025/05/18
	 */
	native static long __create(long __statePtr, String __name, int __format,
		int __rate, int __channels)
		throws MLECallError;
	
	/**
	 * Disconnects the given connection.
	 *
	 * @param __statePtr The state pointer.
	 * @param __connPtr The connection pointer.
	 * @throws MLECallError On null arguments or if the connection could not
	 * be disconnected.
	 * @since 2025/05/26
	 */
	private static native void __disconnect(long __statePtr, long __connPtr)
		throws MLECallError;
	
	/**
	 * Initializes the dynamic library interface.
	 *
	 * @return The state pointer.
	 * @since 2025/05/11
	 */
	static long __dylibInit()
	{
		synchronized (EmulatedAudioStreamShelf.class)
		{
			// Does not need initialization?
			if (EmulatedAudioStreamShelf._statePtr != 0)
				return EmulatedAudioStreamShelf._statePtr;
			
			// Try multiple libraries for a given order
			for (String order : EmulatedAudioStreamShelf.__dylibOrder())
			{
				long maybe = EmulatedAudioStreamShelf.__dylibInit(order);
				if (maybe != 0)
				{
					EmulatedAudioStreamShelf._statePtr = maybe;
					return maybe;
				}
			}
		}
		
		// Could not initialize any audio
		throw new MLECallError("No audio support");
	}
	
	/**
	 * Initializes the specific library. 
	 *
	 * @param __name The name of the library to initialize.
	 * @return The state pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/11
	 */
	static long __dylibInit(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// This could fail
		try
		{
			// Determine the actual library name
			String libName = System.mapLibraryName(
				"squirreljme-scritchaudio-" + __name);
			
			// Where does this library exist?
			Path path = NativeBinding.libFromResources(libName, false);
			
			// Attempt native load of state
			return EmulatedAudioStreamShelf.__dylibLoad(
				path.toAbsolutePath().toString(), __name.toLowerCase());
		}
		catch (LinkageError|MLECallError __e)
		{
			__e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * Performs the actual library load.
	 *
	 * @param __path The path to load.
	 * @param __name The name of the library.
	 * @return The state pointer.
	 * @throws MLECallError If loading failed.
	 * @since 2025/05/11
	 */
	static native long __dylibLoad(String __path, String __name)
		throws MLECallError;
	
	/**
	 * Returns the dynamic library order to use.
	 *
	 * @return The dynamic library order.
	 * @since 2025/05/11
	 */
	static List<String> __dylibOrder()
	{
		switch (NativeBinding.nativeOs())
		{
			case "windows":
				return Arrays.asList("pulse", "winmm");
				
			case "macos":
				return Arrays.asList("pulse", "coreaudio");
				
			default:
				return Arrays.asList("pulse", "alsa", "oss");
		}
	}
}
