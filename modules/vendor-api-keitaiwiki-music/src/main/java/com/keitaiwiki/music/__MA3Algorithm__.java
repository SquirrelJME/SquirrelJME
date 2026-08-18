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

import cc.squirreljme.runtime.cldc.util.ExtraMath;
import javax.microedition.media.MediaException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Template algorithm for Yamaha MA-3's OPL synthesis.
 *
 * @since 2025/05/05
 */
class __MA3Algorithm__
	implements BasicAlgorithm
{
	/** Modulation LFO rate multiplier */
	final int _lfo;

	/** FM operator templates */
	final __MA3Operator__[] _operators;

	/** Stereo balance */
	final int _panpot;

	/** Operator connection algorithm */
	int _alg;

	/** Key played for drum notes */
	int _drumKey;

	/** Wave end point */
	int _ep;

	/** Drum frequency base */
	float _freqBase;

	/** Wave sampling frequency */
	int _fs;

	/** Is a drum note */
	boolean _isDrum;

	/** Envelopes never fully decay. */
	boolean _isForever;

	/** Is a wave drum algorithm */
	boolean _isWave;

	/** Wave loop point */
	int _lp;

	/** Unknown significance. */
	boolean _pe;

	/** Wave ROM select */
	boolean _rm;

	/** Left stereo amplitude */
	float _volLeft;

	/** Right stereo amplitude */
	float _volRight;

	/** Wave samples to advance per output sample */
	float _wavAdvance;

	/** Wave ROM index */
	int _waveId;

	/**
	 * Constructs a new FM synth algorithm from a data array.
	 *
	 * @param __bytes The byte data.
	 * @param __isDrum If this is a drum.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	__MA3Algorithm__(@NotNull byte[] __bytes, boolean __isDrum)
		throws NullPointerException
	{
		if (__bytes == null)
			throw new NullPointerException("NARG");

		// Decode bits
		this._lfo = __bytes[0] & 3;
		this._panpot = __bytes[1] >> 3 & 31;
		this._alg = __bytes[1] & 7;
		this._drumKey = __bytes[2] & 127;

		// operators
		this._operators = new __MA3Operator__[this._alg < 2 ? 2 : 4];
		for (int x = 0; x < this._operators.length; x++)
			this._operators[x] = new __MA3Operator__(__bytes, 3 + x * 7);


		this._freqBase = (float)(440 * ExtraMath.pow(2,
			(this._drumKey - 69) / 12.0));
		this._isDrum = __isDrum;
		this._isWave = false;
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
	__MA3Algorithm__(@NotNull byte[] __message,
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
		this._drumKey = __message[__offset++] & 0xFF;
		this._fs = (__message[__offset] & 0xFF) << 8 | __message[__offset + 1] &
			0xFF;
		__offset += 2;
		bits = __message[__offset++] & 0xFF;
		this._panpot = bits >> 3 & 31;
		this._pe = (bits & 1) != 0;
		bits = __message[__offset++] & 0xFF;
		this._lfo = bits >> 6 & 3;
		// pcm  = bits >> 1 & 1;
		this._operators = new __MA3Operator__[] {new __MA3Operator__(__offset,
			__message)};
		//  5 for operator, 2 unknown (always zero?)
		__offset += 7;
		this._lp = (__message[__offset] & 0xFF) << 8 | __message[__offset + 1] &
			0xFF;
		__offset += 2;
		this._ep = (__message[__offset] & 0xFF) << 8 | __message[__offset + 1] &
			0xFF;
		__offset += 2;
		bits = __message[__offset++] & 0xFF;
		this._rm = (bits >> 7 & 1) != 0;
		this._waveId = bits & 7;


		this._isDrum = true;
		this._isWave = true;
		this._wavAdvance = this._fs / MA3SamplerProvider.SAMPLE_RATE;
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
	__MA3Algorithm__(
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
		this._panpot = bits >> 3 & 31;
		bits = __message[__offset++] & 0xFF;
		this._lfo = bits >> 6 & 3;
		this._pe = (bits >> 5 & 1) != 0;
		this._alg = bits & 7;

		if (this._alg > 1 && type == 0x01)
			throw new MediaException("Operator count mismatch");

		this._operators = new __MA3Operator__[this._alg < 2 ? 2 : 4];
		try
		{
			for (int x = 0; x < this._operators.length; x++, __offset += 7)
				this._operators[x] = new __MA3Operator__(__message, __offset, true);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw new ArrayIndexOutOfBoundsException("Malformed Message data");
		}

		this.__initPost();
	}

	/**
	 * Initializes this synthesizer algorithm's postprocess settings.
	 *
	 * @since 2025/05/05
	 */
	private void __initPost()
	{
		// Test whether the envelopes fully decay
		if (!this._isWave /*|| this.lp < this.ep*/)
		{
			int flags =
				this._isWave ? 1 : MA3SamplerProvider.ENV_FLAGS[this._alg];
			for (int x = 0; !this._isForever && x < this._operators.length; x++,
				flags >>= 1)
			{
				__MA3Operator__ op = this._operators[x];
				this._isForever = (flags & 1) != 0 && (op._xof ?
					op._sr == 0 || op._dr == 0 && op._sr != 0 : op._rr == 0);
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
		this._volRight = this._panpot / (this._panpot <= 15 ? 30.0f : 31.0f);
		this._volLeft = 1 - this._volRight;
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
	static __MA3Algorithm__[] __from(@NotNull RomData __defs, boolean __isDrum,
		boolean __isWave)
		throws NullPointerException
	{
		if (__defs == null)
			throw new NullPointerException("NARG");

		__MA3Algorithm__[] ret;

		// FM presets
		if (!__isWave)
		{
			ret = new __MA3Algorithm__[__defs.count];
			for (int x = 0, n = __defs.count; x < n; x++)
				ret[x] = new __MA3Algorithm__(__defs.bytes(x), __isDrum);
		}

		// Wave drum presets
		else
		{
			ret = new __MA3Algorithm__[61];
			for (int x = 0, n = __defs.count; x < n; x++)
			{
				__MA3Algorithm__ alg = new __MA3Algorithm__(__defs.bytes(x), 0);
				ret[alg._drumKey - 24] = alg;
			}
		}

		return ret;
	}
}
