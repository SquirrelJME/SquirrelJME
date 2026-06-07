// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.pcm;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public interface PCMDecoder
{
	/**
	 * This method will decode compressed samples (be it ADPCM, X-Law, etc)
	 * into PCM S16LE ones.
	 * 
	 * @param __input The input compressed data.
	 * @param __inLen The input data size in valid audio samples.
	 * @param __numCh The amount of audio channels the input data has.
	 * @param __frameSize The amount of samples in each frame of input data.
	 * @param __output The PCM S16LE output array.
	 * @param __outLen The PCM S16LE output array's size.
	 * @param __outOff The PCM S16LE output array's starting offset.
	 * @param __volMult The volume multiplier for the generated samples.
	 * @return The position in the input data array that this decoder stopped
	 * at. Used to resume decoding if the output buffer is not big enough to
	 * decode everything in a single run.
	 * @throws NullPointerException If {@code __input} is null.
	 * @throws IndexOutOfBoundsException If {@__inOff} is outside
	 * {@code __input}'s bounds or {@code __outOff} is outside
	 * {@code __output}'s bounds.
	 * @since 2026/01/05
	 */
	@Range(from = 0, to = Integer.MAX_VALUE)
	int decode(@NotNull byte[] __input,
		@Range(from = 0, to = Integer.MAX_VALUE) int __inLen,
		@Range(from = 0, to = Integer.MAX_VALUE) int __inOff,
		@Range(from = 1, to = 2) byte __numCh,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frameSize,
		@Nullable short[] __output,
		@Range(from = 0, to = Integer.MAX_VALUE) int __outLen,
		@Range(from = 0, to = Integer.MAX_VALUE) int __outOff,
		@Range(from = 0, to = 100) byte __volMult)
		throws NullPointerException, IndexOutOfBoundsException;

	/**
	 * Resets the decoder to the specified sample's position.
	 * 
	 * If {@code __pos} is more than 0, this method expects an data array
	 * as well as its number of channels and frame size in order to properly
	 * reset the marker to a specific point in its data. This is because
	 * fast-forwarding relies on the prior samples to update its internal
	 * state.
	 * 
	 * @param __pos The position to reset to.
	 * @param __input The data array to be used for fast-forwarding.
	 * @param __numCh THe amount of audio channels the data array has.
	 * @param __frameSize The amount of samples in each data frame.
	 * @return The sample index that this decoder was actually reset to.
	 * @throws NullPointerException If {@code __pos > 0} and {@code __input} is
	 * null.
	 * @throws IndexOutOfBoundsException If {@code __pos} is outside of {@code
	 * __input}'s bounds.
	 * @since 2026/01/05
	 */
	@Range(from = 0, to = Integer.MAX_VALUE)
	int reset(@Range(from = 0, to = Integer.MAX_VALUE) int __pos, 
		@Nullable byte[] __input,
		@Range(from = 1, to = 2) byte __numCh,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frameSize)
		throws NullPointerException, IndexOutOfBoundsException;
}
