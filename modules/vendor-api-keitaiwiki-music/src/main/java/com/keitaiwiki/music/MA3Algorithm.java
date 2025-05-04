// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Keitai Wiki Community Music Implementation
//     Originally written and contributed by Guy Perfect
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
	@SquirrelJMEVendorApi
	final int drumKey;
	
	/**
	 * Is a drum note
	 */
	@SquirrelJMEVendorApi
	final boolean isDrum;
	
	/**
	 * Is a wave rum algorithm
	 */
	@SquirrelJMEVendorApi
	final boolean isWave;
	
	/**
	 * Modulation LFO rate multiplier
	 */
	@SquirrelJMEVendorApi
	final int lfo;
	
	/**
	 * FM operator templates
	 */
	@SquirrelJMEVendorApi
	final MA3Operator[] operators;
	
	/**
	 * Stereo balance
	 */
	@SquirrelJMEVendorApi
	final int panpot;
	
	/**
	 * Operator connection algorithm
	 */
	@SquirrelJMEVendorApi
	int alg;
	
	/**
	 * Wave end point
	 */
	@SquirrelJMEVendorApi
	int ep;
	
	/**
	 * Drum frequency base
	 */
	@SquirrelJMEVendorApi
	float freqBase;
	
	/**
	 * Wave sampling frequency
	 */
	@SquirrelJMEVendorApi
	int fs;
	
	/**
	 * Wave loop point
	 */
	@SquirrelJMEVendorApi
	int lp;
	
	/**
	 * Wave ROM select
	 */
	@SquirrelJMEVendorApi
	boolean rm;
	
	/**
	 * Left stereo amplitude
	 */
	@SquirrelJMEVendorApi
	float volLeft;
	
	/**
	 * Right stereo amplitude
	 */
	@SquirrelJMEVendorApi
	float volRight;
	
	/**
	 * Wave samples to advance per output sample
	 */
	@SquirrelJMEVendorApi
	float wavAdvance;
	
	/**
	 * Wave ROM index
	 */
	@SquirrelJMEVendorApi
	int waveId;
	
	
	/**
	 * FM constructor
	 */
	@SquirrelJMEVendorApi
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
		this.initVolume();
	}
	
	
	/**
	 * Wave drum constructor
	 */
	@SquirrelJMEVendorApi
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
		// pe   = bits & 1;
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
		this.initVolume();
	}
	
	/**
	 * Debugging output
	 */
	@SquirrelJMEVendorApi
	String debug()
	{
		StringBuilder ret = new StringBuilder();
		ret.append(String.format("LFO:     %d\n", this.lfo));
		ret.append(String.format("PANPOT:  %d\n", this.panpot));
		ret.append(String.format("ALG:     %d\n", this.alg));
		ret.append(String.format("DrumKey: %d\n", this.drumKey));
		ret.append(String.format("Fs:      %d\n", this.fs));
		ret.append(String.format("RM:      %d\n", this.rm ? 1 : 0));
		ret.append(String.format("Wave ID: %d\n", this.waveId));
		ret.append(String.format("LP:      %d\n", this.lp));
		ret.append(String.format("EP:      %d\n", this.ep));
		for (int x = 0; x < this.operators.length; x++)
		{
			MA3Operator op = this.operators[x];
			ret.append(String.format("Operator %d\n", x + 1));
			ret.append(String.format("  MULTI: %d\n", op.multi));
			ret.append(String.format("  DT:    %d\n", op.dt));
			ret.append(String.format("  AR:    %d\n", op.ar));
			ret.append(String.format("  DR:    %d\n", op.dr));
			ret.append(String.format("  SR:    %d\n", op.sr));
			ret.append(String.format("  RR:    %d\n", op.rr));
			ret.append(String.format("  SL:    %d\n", op.sl));
			ret.append(String.format("  TL:    %d\n", op.tl));
			ret.append(String.format("  KSL:   %d\n", op.ksl));
			ret.append(String.format("  DAM:   %d\n", op.dam));
			ret.append(String.format("  DVB:   %d\n", op.dvb));
			ret.append(String.format("  FB:    %d\n", op.fb));
			ret.append(String.format("  WS:    %d\n", op.ws));
			ret.append(String.format("  XOF:   %s\n", op.xof + ""));
			ret.append(String.format("  SUS:   %s\n", op.sus + ""));
			ret.append(String.format("  KSR:   %d\n", op.ksr));
			ret.append(String.format("  EAM:   %s\n", op.eam + ""));
			ret.append(String.format("  EVB:   %s\n", op.evb + ""));
		}
		return ret.toString();
	}
	
	/**
	 * Initialize volume settings
	 */
	@SquirrelJMEVendorApi
	void initVolume()
	{
		this.volRight = this.panpot / (this.panpot <= 15 ? 30.0f : 31.0f);
		this.volLeft = 1 - this.volRight;
	}
	
	@SquirrelJMEVendorApi
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
