// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

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
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * This shelf provides access and callbacks for audio streaming.
 *
 * @since 2025/05/04
 */
@SquirrelJMEVendorApi
public final class AudioStreamShelf
{
	/**
	 * Not used. 
	 *
	 * @since 2025/05/04
	 */
	private AudioStreamShelf()
	{
	}
	
	/**
	 * Attach the given renderer to the stream.
	 *
	 * @param __stream The stream to render to.
	 * @param __renderer The renderer to attach.
	 * @param __format The format used.
	 * @param __rate The rate.
	 * @param __channels The channels.
	 * @return The connection state.
	 * @throws MLECallError On null arguments or if the renderer could not
	 * be attached.
	 * @since 2025/05/04
	 */
	@SquirrelJMEVendorApi
	public static native AudioConnectionBracket attach(
		@NotNull AudioStreamBracket __stream,
		@NotNull AudioStreamRenderer __renderer,
		@MagicConstant(valuesFromClass = AudioStreamFormat.class)
			int __format,
		@MagicConstant(valuesFromClass = AudioStreamRate.class)
			int __rate,
		@MagicConstant(valuesFromClass = AudioStreamChannels.class)
			int __channels)
		throws MLECallError;
	
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
	 * @param __channels The channels count, if {@code -1} then the
	 * decoder will use its preferred channel count.
	 * @param __buf The buffer of the stream data.
	 * @param __off The offset into the buffer.
	 * @param __len The length of the buffer data.
	 * @return The player which is capable of playing the given audio.
	 * @throws MLECallError If the decoder could not be created.
	 * @since 2025/05/04
	 */
	@SquirrelJMEVendorApi
	@NotNull
	public static native AudioStreamPlayer decoder(
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
		throws MLECallError;
	
	/**
	 * Checks if the native audio system supports decoding the given format.
	 *
	 * @param __contentType The content type.
	 * @return If the audio system supports the given format.
	 * @throws MLECallError On null arguments.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public static native boolean decoderSupports(
		@NotNull @Language("mime-type-reference") String __contentType)
		throws MLECallError;
	
	/**
	 * Disconnects the given connection.
	 *
	 * @param __conn The connection to disconnect.
	 * @throws MLECallError On null arguments or if the connection could not
	 * be disconnected.
	 * @since 2025/05/25
	 */
	@SquirrelJMEVendorApi
	public static native void disconnect(
		@NotNull AudioConnectionBracket __conn)
		throws MLECallError;
	
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
	 * @param __channels The channels count, if {@code -1} then the
	 * decoder will use its preferred channel count.
	 * @return The MIDI port.
	 * @throws MLECallError On null arguments or if the mime type does not
	 * support MIDI playback.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	@NotNull
	public static native MidiPortBracket midiPort(
		@NotNull @Language("mime-type-reference") String __mimeType,
		@MagicConstant(valuesFromClass = AudioStreamFormat.class)
			int __format,
		@MagicConstant(valuesFromClass = AudioStreamRate.class)
			int __rate,
		@MagicConstant(valuesFromClass = AudioStreamChannels.class)
			int __channels)
		throws MLECallError;
	
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
	public static native AudioStreamRenderer midiRenderer(
		@NotNull MidiPortBracket __midiPort)
		throws MLECallError;
	
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
	public static native AudioStreamBracket stream()
		throws MLECallError;
}
