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
import cc.squirreljme.jvm.mle.callbacks.AudioStreamPlayer;
import cc.squirreljme.jvm.mle.callbacks.AudioStreamRenderer;
import cc.squirreljme.jvm.mle.constants.AudioPositionType;
import cc.squirreljme.jvm.mle.constants.AudioStreamFormat;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
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
	public static native AudioStreamBracket create(
		@NotNull String __name,
		@MagicConstant(valuesFromClass = AudioStreamFormat.class)
		@Range(from = -1, to = AudioStreamFormat.NUM_FORMATS)
			int __format,
		@Range(from = -1, to = Integer.MAX_VALUE) int __rate,
		@MagicConstant(valuesFromClass = AudioPositionType.class)
			int[] __channels)
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
		@Nullable String __mimeType,
		@MagicConstant(valuesFromClass = AudioStreamFormat.class)
		@Range(from = -1, to = AudioStreamFormat.NUM_FORMATS)
			int __format,
		@NotNull byte[] __buf,
		@Range(from = 0, to = Integer.MAX_VALUE) int __off,
		@Range(from = 0, to = Integer.MAX_VALUE) int __len)
		throws MLECallError;
	
	/**
	 * Registers the given renderer to the stream.
	 *
	 * @param __stream The stream to render to.
	 * @param __renderer The renderer to register.
	 * @param __format The format to use for rendering, if {@code -1} then it
	 * will use the same format as {@code __stream} and not perform any
	 * re-encoding.
	 * @throws MLECallError On null arguments or if the renderer could not
	 * be registered.
	 * @since 2025/05/04
	 */
	@SquirrelJMEVendorApi
	public static native void register(
		@NotNull AudioStreamFormat __stream,
		@NotNull AudioStreamRenderer __renderer,
		@MagicConstant(valuesFromClass = AudioStreamFormat.class)
		@Range(from = -1, to = AudioStreamFormat.NUM_FORMATS)
			int __format)
		throws MLECallError;
}
