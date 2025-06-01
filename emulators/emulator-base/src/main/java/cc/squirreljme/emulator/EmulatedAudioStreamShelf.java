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
import cc.squirreljme.jvm.mle.brackets.AudioConnectionBracket;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamPlayer;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.jvm.mle.constants.AudioStreamChannels;
import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.jvm.mle.constants.AudioStreamRate;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.nio.ByteBuffer;
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
	/** The native stream. */
	static volatile EmulatedAudioStreamBracket _stream;
	
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
	public static AudioConnectionBracket attach(
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
		@NotNull AudioConnectionBracket __conn)
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
	 * Returns the native audio stream.
	 *
	 * @return The native audio stream.
	 * @throws MLECallError On null arguments, invalid arguments, or if the
	 * stream does not exist.
	 * @since 2025/05/04
	 */
	@NotNull
	@SquirrelJMEVendorApi
	public static AudioStreamBracket stream()
		throws MLECallError
	{
		// Make sure the dynamic library is initialized
		long statePtr = EmulatedAudioStreamShelf.__dylibInit();
		
		// Load or setup stream?
		EmulatedAudioStreamBracket result;
		synchronized (EmulatedAudioStreamShelf.class)
		{
			// Already been cached?
			result = EmulatedAudioStreamShelf._stream;
			if (result != null)
				return result;
			
			// Create wrapper for it
			result = new EmulatedAudioStreamBracket(statePtr,
				EmulatedAudioStreamShelf.__stream(statePtr));
			EmulatedAudioStreamShelf._stream = result;
			return result;
		}
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
	
	/**
	 * Renders the audio stream. 
	 *
	 * @param __conn The connection.
	 * @param __buf The buffer to render to.
	 * @param __clock The clock.
	 * @param __samples The samples.
	 * @param __totalSamples The total samples.
	 * @param __bytesPerSample The bytes per sample.
	 * @param __bufSize The buffer size.
	 * @param __format The format used.
	 * @param __rate The rate.
	 * @param __channels The channels.
	 * @since 2025/05/31
	 */
	@SquirrelJMEVendorApi
	static void __render(AudioStreamRenderer __conn, ByteBuffer __buf,
		long __clock, int __samples, int __totalSamples, int __bytesPerSample,
		int __bufSize, int __format, int __rate, int __channels)
	{
		if (__conn == null || __buf == null)
			throw new MLECallError("NARG");
		
		if (!(__conn instanceof AudioStreamRenderer))
			throw new MLECallError("CAST");
		
		AudioStreamRenderer renderer = (AudioStreamRenderer)__conn;
		
		// Set up a new array to render within
		Object lowerBuf;
		int lowerBufLen = __bufSize / __bytesPerSample;
		if (__format == AudioStreamFormat.FLOAT_F32)
			lowerBuf = new float[lowerBufLen];
		else if (__bytesPerSample == 4)
			lowerBuf = new int[lowerBufLen];
		else if (__bytesPerSample == 2)
			lowerBuf = new short[lowerBufLen];
		else
			lowerBuf = new byte[lowerBufLen];
		
		// Do the actual render
		renderer.render(__format, __rate, __channels,
			lowerBuf, 0, __bufSize / __bytesPerSample);
		
		// Copy everything back to the main buffer
		if (__format == AudioStreamFormat.FLOAT_F32)
			__buf.asFloatBuffer().put((float[])lowerBuf);
		else if (__bytesPerSample == 4)
			__buf.asIntBuffer().put((int[])lowerBuf);
		else if (__bytesPerSample == 2)
			__buf.asShortBuffer().put((short[])lowerBuf);
		else
			__buf.put((byte[])lowerBuf);
	}
	
	/**
	 * Returns the native stream pointer.
	 *
	 * @param __statePtr The state pointer.
	 * @return The audio stream pointer.
	 * @throws MLECallError If it could not be obtained.
	 * @since 2025/05/18
	 */
	native static long __stream(long __statePtr)
		throws MLECallError;
}
