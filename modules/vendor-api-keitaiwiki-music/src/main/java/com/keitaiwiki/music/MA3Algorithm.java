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

/**
 * Template algorithm for OPL synthesis
 */
class MA3Algorithm
	implements BasicAlgorithm
{
	/**
	 * Key played for drum notes
	 */
	int drumKey;
	
	/**
	 * Is a drum note
	 */
	boolean isDrum;
	
	/** Envelopes never fully decay. */
	boolean isForever;
	
	/**
	 * Is a wave rum algorithm
	 */
	boolean isWave;
	
	/**
	 * Modulation LFO rate multiplier
	 */
	final int lfo;
	
	/**
	 * FM operator templates
	 */
	final MA3Operator[] operators;
	
	/**
	 * Stereo balance
	 */
	final int panpot;
	
	/** Unknown significance. */
	boolean pe;
	
	/**
	 * Operator connection algorithm
	 */
	int alg;
	
	/**
	 * Wave end point
	 */
	int ep;
	
	/**
	 * Drum frequency base
	 */
	float freqBase;
	
	/**
	 * Wave sampling frequency
	 */
	int fs;
	
	/**
	 * Wave loop point
	 */
	int lp;
	
	/**
	 * Wave ROM select
	 */
	boolean rm;
	
	/**
	 * Left stereo amplitude
	 */
	float volLeft;
	
	/**
	 * Right stereo amplitude
	 */
	float volRight;
	
	/**
	 * Wave samples to advance per output sample
	 */
	float wavAdvance;
	
	/**
	 * Wave ROM index
	 */
	int waveId;
	
	
	/**
	 * FM constructor
	 */
	MA3Algorithm(byte[] bytes, boolean isDrum)
	{
		
		// Decode bits
		this.lfo = bytes[0] & 3;
		this.panpot = bytes[1] >> 3 & 31;
		this.alg = bytes[1] & 7;
		this.drumKey = bytes[2] & 127;
		
		// Operators
		this.operators = new MA3Operator[this.alg < 2 ? 2 : 4];
		for (int x = 0; x < this.operators.length; x++)
			this.operators[x] = new MA3Operator(bytes, 3 + x * 7);
		
		
		this.freqBase = (float)(440 * ExtraMath.pow(2,
			(this.drumKey - 69) / 12.0));
		this.isDrum = isDrum;
		this.isWave = false;
		this.initPost();
	}
	
	
	/**
	 * Wave drum constructor
	 */
	MA3Algorithm(byte[] message, int offset)
	{
		//  Scratch
		int bits;
		
		// Parse fields
		this.drumKey = message[offset++] & 0xFF;
		this.fs = (message[offset] & 0xFF) << 8 | message[offset + 1] & 0xFF;
		offset += 2;
		bits = message[offset++] & 0xFF;
		this.panpot = bits >> 3 & 31;
		this.pe = (bits & 1) != 0;
		bits = message[offset++] & 0xFF;
		this.lfo = bits >> 6 & 3;
		// pcm  = bits >> 1 & 1;
		this.operators = new MA3Operator[] {new MA3Operator(offset, message)};
		//  5 for operator, 2 unknown (always zero?)
		offset += 7;
		this.lp = (message[offset] & 0xFF) << 8 | message[offset + 1] & 0xFF;
		offset += 2;
		this.ep = (message[offset] & 0xFF) << 8 | message[offset + 1] & 0xFF;
		offset += 2;
		bits = message[offset++] & 0xFF;
		this.rm = (bits >> 7 & 1) != 0;
		this.waveId = bits & 7;
		
		
		this.isDrum = true;
		this.isWave = true;
		this.wavAdvance = this.fs / MA3SamplerProvider.SAMPLE_RATE;
		this.initPost();
	}
	
	/** FM SysEx constructor. */
	MA3Algorithm(int offset, byte[] message)
	{
		int bits;
		int type = message[offset] & 0xFF;
		offset += 4;
		bits = message[offset++] & 0xFF;
		this.panpot = bits >> 3 & 31;
		bits = message[offset++] & 0xFF;
		this.lfo = bits >> 6 & 3;
		this.pe = (bits >> 5 & 1) != 0;
		this.alg = bits & 7;
		if (this.alg > 1 && type == 0x01)
			throw new RuntimeException("Operator count mismatch");
		this.operators = new MA3Operator[this.alg < 2 ? 2 : 4];
		for (int x = 0; x < this.operators.length; x++, offset += 7)
			this.operators[x] = new MA3Operator(message, offset, true);
		this.initPost();
	}
	
	/**
	 * Initialize volume settings
	 */
	void initVolume()
	{
		this.volRight = this.panpot / (this.panpot <= 15 ? 30.0f : 31.0f);
		this.volLeft = 1 - this.volRight;
	}
	
	void initPost()
	{
		// Test whether the envelopes fully decay
		if (!this.isWave /*|| lp < ep*/)
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
		this.initVolume();
	}
	
	static MA3Algorithm[] from(RomData defs, boolean isDrum, boolean isWave)
	{
		MA3Algorithm[] ret;
		
		// FM presets
		if (!isWave)
		{
			ret = new MA3Algorithm[defs.count];
			for (int x = 0, n = defs.count; x < n; x++)
				ret[x] = new MA3Algorithm(defs.bytes(x), isDrum);
		}
		
		// Wave drum presets
		else
		{
			ret = new MA3Algorithm[61];
			for (int x = 0, n = defs.count; x < n; x++)
			{
				MA3Algorithm alg = new MA3Algorithm(defs.bytes(x), 0);
				ret[alg.drumKey - 24] = alg;
			}
		}
		
		return ret;
	}
}
