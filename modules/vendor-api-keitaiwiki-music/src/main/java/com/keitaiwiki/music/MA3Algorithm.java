// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Keitai Wiki Community Music Implementation
//     Originally written and contributed by Guy Perfect
//     Continued maintenance and upkeep by SquirrelJME/Stephanie Gawroriski
// ---------------------------------------------------------------------------
// This specific file is under the given license:
// This is free and unencumbered software released into the public domain.
//
// Anyone is free to copy, modify, publish, use, compile, sell, or
// distribute this software, either in source code form or as a compiled
// binary, for any purpose, commercial or non-commercial, and by any
// means.
//
// In jurisdictions that recognize copyright laws, the author or authors
// of this software dedicate any and all copyright interest in the
// software to the public domain. We make this dedication for the benefit
// of the public at large and to the detriment of our heirs and
// successors. We intend this dedication to be an overt act of
// relinquishment in perpetuity of all present and future rights to this
// software under copyright law.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
// IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
// OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.
//
// For more information, please refer to <https://unlicense.org/>
// ---------------------------------------------------------------------------

package com.keitaiwiki.music;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.util.ExtraMath;
import javax.microedition.media.MediaException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Template algorithm for Yamaha MA-3's OPL synthesis.
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
class MA3Algorithm
	implements BasicAlgorithm
{
	/** Key played for drum notes */
	@SquirrelJMEVendorApi
	int drumKey;

	/** Is a drum note */
	@SquirrelJMEVendorApi
	boolean isDrum;

	/** Envelopes never fully decay. */
	@SquirrelJMEVendorApi
	boolean isForever;

	/** Is a wave drum algorithm */
	@SquirrelJMEVendorApi
	boolean isWave;

	/** Modulation LFO rate multiplier */
	@SquirrelJMEVendorApi
	final int lfo;

	/** FM operator templates */
	@SquirrelJMEVendorApi
	final MA3Operator[] operators;

	/** Stereo balance */
	@SquirrelJMEVendorApi
	final int panpot;

	/** Unknown significance. */
	@SquirrelJMEVendorApi
	boolean pe;

	/** Operator connection algorithm */
	@SquirrelJMEVendorApi
	int alg;

	/** Wave end point */
	@SquirrelJMEVendorApi
	int ep;

	/** Drum frequency base */
	@SquirrelJMEVendorApi
	float freqBase;

	/** Wave sampling frequency */
	@SquirrelJMEVendorApi
	int fs;

	/** Wave loop point */
	@SquirrelJMEVendorApi
	int lp;

	/** Wave ROM select */
	@SquirrelJMEVendorApi
	boolean rm;

	/** Left stereo amplitude */
	@SquirrelJMEVendorApi
	float volLeft;

	/** Right stereo amplitude */
	@SquirrelJMEVendorApi
	float volRight;

	/** Wave samples to advance per output sample */
	@SquirrelJMEVendorApi
	float wavAdvance;

	/** Wave ROM index */
	@SquirrelJMEVendorApi
	int waveId;

	/**
	 * Constructs a new FM synth algorithm from a data array.
	 *
	 * @param __offset Offset from which the data should start being read from.
	 * @param __message The data array to construct the algorithm from.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	MA3Algorithm(@NotNull byte[] __bytes, boolean __isDrum)
		throws NullPointerException
	{
		if (__bytes == null)
			throw new NullPointerException("NARG");

		// Decode bits
		this.lfo = __bytes[0] & 3;
		this.panpot = __bytes[1] >> 3 & 31;
		this.alg = __bytes[1] & 7;
		this.drumKey = __bytes[2] & 127;

		// operators
		this.operators = new MA3Operator[this.alg < 2 ? 2 : 4];
		for (int x = 0; x < this.operators.length; x++)
			this.operators[x] = new MA3Operator(__bytes, 3 + x * 7);


		this.freqBase = (float)(440 * ExtraMath.pow(2,
			(this.drumKey - 69) / 12.0));
		this.isDrum = __isDrum;
		this.isWave = false;
		this.__initPost();
	}

	/**
	 * Constructs a new wave drum algorithm from a data array.
	 *
	 * @param __offset Offset from which the data should start being read from.
	 * @param __message The data array to construct the algorithm from.
	 * @throws IllegalArgumentException If {@code __offset} is less than zero.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	MA3Algorithm(@NotNull byte[] __message,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset)
		throws IllegalArgumentException, NullPointerException
	{
		if (__message == null)
			throw new NullPointerException("NARG");

		if (__offset < 0)
			throw new IllegalArgumentException("NEGV");

		//  Scratch
		int bits;

		// Parse fields
		this.drumKey = __message[__offset++] & 0xFF;
		this.fs = (__message[__offset] & 0xFF) << 8 | __message[__offset + 1] &
			0xFF;
		__offset += 2;
		bits = __message[__offset++] & 0xFF;
		this.panpot = bits >> 3 & 31;
		this.pe = (bits & 1) != 0;
		bits = __message[__offset++] & 0xFF;
		this.lfo = bits >> 6 & 3;
		// pcm  = bits >> 1 & 1;
		this.operators = new MA3Operator[] {new MA3Operator(__offset,
			__message)};
		//  5 for operator, 2 unknown (always zero?)
		__offset += 7;
		this.lp = (__message[__offset] & 0xFF) << 8 | __message[__offset + 1] &
			0xFF;
		__offset += 2;
		this.ep = (__message[__offset] & 0xFF) << 8 | __message[__offset + 1] &
			0xFF;
		__offset += 2;
		bits = __message[__offset++] & 0xFF;
		this.rm = (bits >> 7 & 1) != 0;
		this.waveId = bits & 7;


		this.isDrum = true;
		this.isWave = true;
		this.wavAdvance = this.fs / MA3SamplerProvider.SAMPLE_RATE;
		this.__initPost();
	}

	/**
	 * Constructs a new SysEx type algorithm from a data array.
	 *
	 * @param __offset Offset from which the data should start being read from.
	 * @param __message The data array to construct the algorithm from.
	 * @throws ArrayIndexOutOfBoundsException If the message is malformed.
	 * @throws IllegalArgumentException If {@code __offset} is less than zero.
	 * @throws MediaException If there is an operator count mismatch.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	MA3Algorithm(
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@NotNull byte[] __message)
		throws IllegalArgumentException, MediaException, NullPointerException
	{
		if (__message == null)
			throw new NullPointerException("NARG");

		if (__offset < 0)
			throw new IllegalArgumentException("NEGV");

		int bits;
		int type = __message[__offset] & 0xFF;
		__offset += 4;
		bits = __message[__offset++] & 0xFF;
		this.panpot = bits >> 3 & 31;
		bits = __message[__offset++] & 0xFF;
		this.lfo = bits >> 6 & 3;
		this.pe = (bits >> 5 & 1) != 0;
		this.alg = bits & 7;

		if (this.alg > 1 && type == 0x01)
			throw new MediaException("Operator count mismatch");

		this.operators = new MA3Operator[this.alg < 2 ? 2 : 4];
		try
		{
			for (int x = 0; x < this.operators.length; x++, __offset += 7)
				this.operators[x] = new MA3Operator(__message, __offset, true);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw new ArrayIndexOutOfBoundsException("Malformed Message data");
		}

		this.__initPost();
	}

	/**
	 * Initializes a set of Yamaha MA-3 algorithms based on the provided
	 * {@link RomData} and preset information.
	 *
	 * @param __defs A {@link RomData} file containing instrument definitions.
	 * @param __isDrum Indicates whether the data is a drum preset
	 * @param __isWave Indicates whether the data is a wave preset.
	 * @return A new array of Yamaha MA-3 synthesizer algorithms.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	static MA3Algorithm[] from(@NotNull RomData __defs, boolean __isDrum,
		boolean __isWave)
		throws NullPointerException
	{
		if (__defs == null)
			throw new NullPointerException("NARG");

		MA3Algorithm[] ret;

		// FM presets
		if (!__isWave)
		{
			ret = new MA3Algorithm[__defs.count];
			for (int x = 0, n = __defs.count; x < n; x++)
				ret[x] = new MA3Algorithm(__defs.bytes(x), __isDrum);
		}

		// Wave drum presets
		else
		{
			ret = new MA3Algorithm[61];
			for (int x = 0, n = __defs.count; x < n; x++)
			{
				MA3Algorithm alg = new MA3Algorithm(__defs.bytes(x), 0);
				ret[alg.drumKey - 24] = alg;
			}
		}

		return ret;
	}

	/**
	 * Initializes this synthesizer algorithm's postprocess settings.
	 *
	 * @since 2025/05/05
	 */
	private void __initPost()
	{
		// Test whether the envelopes fully decay
		if (!this.isWave /*|| this.lp < this.ep*/)
		{
			int flags =
				this.isWave ? 1 : MA3SamplerProvider.ENV_FLAGS[this.alg];
			for (int x = 0; !this.isForever && x < this.operators.length; x++,
				flags >>= 1)
			{
				MA3Operator op = this.operators[x];
				this.isForever = (flags & 1) != 0 && (op.xof ?
					op.sr == 0 || op.dr == 0 && op.sr != 0 : op.rr == 0);
			}
		}
		this.__initVolume();
	}

	/**
	 * Initializes this synthesizer algorithm's volume settings.
	 *
	 * @since 2025/05/05
	 */
	private void __initVolume()
	{
		this.volRight = this.panpot / (this.panpot <= 15 ? 30.0f : 31.0f);
		this.volLeft = 1 - this.volRight;
	}
}
