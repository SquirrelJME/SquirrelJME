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

/**
 * Individual FM algorithm operator
 */
@SquirrelJMEVendorApi
class MA3Operator
	implements BasicOperator
{
	/**
	 * OPL registers
	 * Envelope attack rate
	 */
	@SquirrelJMEVendorApi
	final int ar;
	
	/**
	 * Amplitude modulation depth
	 */
	@SquirrelJMEVendorApi
	final int dam;
	
	/**
	 * Envelope decay rate
	 */
	@SquirrelJMEVendorApi
	final int dr;
	
	/**
	 * Frequency modulation depth
	 */
	@SquirrelJMEVendorApi
	final int dvb;
	
	/**
	 * Enable amplutide modulation
	 */
	@SquirrelJMEVendorApi
	final boolean eam;
	
	/**
	 * Enable frequency modulation
	 */
	@SquirrelJMEVendorApi
	final boolean evb;
	
	/**
	 * Envelope release rate
	 */
	@SquirrelJMEVendorApi
	final int rr;
	
	/**
	 * Envelope sustain level
	 */
	@SquirrelJMEVendorApi
	final int sl;
	
	/**
	 * Envelope sustain rate
	 */
	@SquirrelJMEVendorApi
	final int sr;
	
	/**
	 * MIDI Hold 1 is supported
	 */
	@SquirrelJMEVendorApi
	final boolean sus;
	
	/**
	 * Envelope attenuation
	 */
	@SquirrelJMEVendorApi
	final int tl;
	
	/**
	 * Ignore key-off response
	 */
	@SquirrelJMEVendorApi
	final boolean xof;
	
	/**
	 * Encapsulating algorithm
	 */
	@SquirrelJMEVendorApi
	MA3Algorithm algorithm;
	
	/**
	 * u14 Amplitude modulation counter
	 */
	@SquirrelJMEVendorApi
	int amPhase;
	
	/**
	 * Detune shift
	 */
	@SquirrelJMEVendorApi
	int dt;
	
	/**
	 * u9  Current envelope level
	 */
	@SquirrelJMEVendorApi
	int envLevel;
	
	/**
	 * u9  Effective envelope output
	 */
	@SquirrelJMEVendorApi
	int envOut;
	
	/**
	 * u15 Envelope phase counter
	 */
	@SquirrelJMEVendorApi
	int envPhase;
	
	/**
	 * Current envelope rate of change
	 */
	@SquirrelJMEVendorApi
	int envRate;
	
	/**
	 * Envelope rate offset modifier
	 */
	@SquirrelJMEVendorApi
	int envRof;
	
	/**
	 * Envelope processing stage
	 */
	@SquirrelJMEVendorApi
	int envStage;
	
	/**
	 * Feedback rate index
	 */
	@SquirrelJMEVendorApi
	int fb;
	
	/**
	 * Most recent output sample
	 */
	@SquirrelJMEVendorApi
	int fb0;
	
	/**
	 * Second-most recent output sample
	 */
	@SquirrelJMEVendorApi
	int fb1;
	
	/**
	 * Encapsulating instance
	 */
	@SquirrelJMEVendorApi
	MA3Sampler instance;
	
	/**
	 * Wave drum parameters are valid
	 */
	@SquirrelJMEVendorApi
	boolean isValid;
	
	/**
	 * Attenuation index per octave
	 */
	@SquirrelJMEVendorApi
	int ksl;
	
	/**
	 * KSL attenuation level
	 */
	@SquirrelJMEVendorApi
	int kslOut;
	
	/**
	 * Envelope rate modifier scale
	 */
	@SquirrelJMEVendorApi
	int ksr;
	
	/**
	 * Frequency multiplier
	 */
	@SquirrelJMEVendorApi
	int multi;
	
	/**
	 * Encapsulating note
	 */
	@SquirrelJMEVendorApi
	MA3Note note;
	
	/**
	 * u10 Oscillator counter
	 */
	@SquirrelJMEVendorApi
	int oscPhase;
	
	/**
	 * Current wave source sample
	 */
	@SquirrelJMEVendorApi
	float wavSample;
	
	/**
	 * Wave function index
	 */
	@SquirrelJMEVendorApi
	int ws;
	
	/**
	 * Template constructor
	 */
	@SquirrelJMEVendorApi
	MA3Operator(byte[] bytes, int offset)
	{
		this.sus = (bytes[offset] >> 3 & 1) != 0;
		this.ksr = bytes[offset] >> 2 & 1;
		this.eam = (bytes[offset] >> 1 & 1) != 0;
		this.evb = (bytes[offset] & 1) != 0;
		this.multi = bytes[offset + 1] >> 4 & 15;
		this.dt = bytes[offset + 1] >> 1 & 7;
		this.xof = (bytes[offset + 1] & 1) != 0;
		this.ar = bytes[offset + 2] >> 4 & 15;
		this.dr = bytes[offset + 2] & 15;
		this.sr = bytes[offset + 3] >> 4 & 15;
		this.rr = bytes[offset + 3] & 15;
		this.sl = bytes[offset + 4] >> 4 & 15;
		this.dam = bytes[offset + 4] >> 2 & 3;
		this.dvb = bytes[offset + 4] & 3;
		this.tl = bytes[offset + 5] >> 2 & 63;
		this.ksl = bytes[offset + 5] & 3;
		this.fb = bytes[offset + 6] >> 5 & 7;
		this.ws = bytes[offset + 6] & 31;
	}
	
	/**
	 * Wave constructor
	 */
	@SquirrelJMEVendorApi
	MA3Operator(int offset, byte[] message)
	{
		int bits;
		bits = message[offset++] & 0xFF;
		this.sr = bits >> 4 & 15;
		this.xof = (bits >> 3 & 1) != 0;
		this.sus = (bits >> 1 & 1) != 0;
		bits = message[offset++] & 0xFF;
		this.rr = bits >> 4 & 15;
		this.dr = bits & 15;
		bits = message[offset++] & 0xFF;
		this.ar = bits >> 4 & 15;
		this.sl = bits & 15;
		bits = message[offset++] & 0xFF;
		this.tl = bits >> 2 & 63;
		bits = message[offset++] & 0xFF;
		this.dam = bits >> 5 & 3;
		this.eam = (bits >> 4 & 1) != 0;
		this.dvb = bits >> 1 & 3;
		this.evb = (bits & 1) != 0;
	}
	
	/**
	 * Playback constructor
	 */
	@SquirrelJMEVendorApi
	MA3Operator(MA3Note note, MA3Operator o)
	{
		
		// OPL registers
		this.ar = o.ar;
		this.dam = o.dam;
		this.dr = o.dr;
		this.dt = o.dt;
		this.dvb = o.dvb;
		this.eam = o.eam;
		this.evb = o.evb;
		this.fb = o.fb;
		this.ksl = o.ksl;
		this.ksr = o.ksr;
		this.multi = o.multi;
		this.rr = o.rr;
		this.sl = o.sl;
		this.sr = o.sr;
		this.sus = o.sus;
		this.tl = o.tl;
		this.ws = o.ws;
		this.xof = o.xof;
		
		
		this.algorithm = note.algorithm;
		this.amPhase = note.instance.amPhase;
		this.envLevel = 511;
		this.envPhase = 0;
		this.envRate = this.ar;
		this.envStage = MA3SamplerProvider.ENV_ATTACK;
		this.instance = note.instance;
		this.note = note;
		this.oscPhase = 0;
		this.wavSample = 0;
	}
	
	
	/**
	 * Frequency has changed
	 */
	@SquirrelJMEVendorApi
	void onFrequency()
	{
		this.envRof =
			(this.note.block << 1 | this.note.f_number >> 8 + MA3SamplerProvider.NTS & 1) >> ((this.ksr ^ 1) << 1);
		this.kslOut = Math.max(0,
			MA3SamplerProvider.KSL_B[this.ksl] * ((this.note.block << 3) - MA3SamplerProvider.KSL_F[this.note.f_number >> 6]));
	}
	
	/**
	 * Generate a sample on an operator
	 */
	@SquirrelJMEVendorApi
	int sample(int mod, boolean feedback)
	{
		//  Scratch
		int x, y;
		
		// The envelope has finished
		if (this.envStage == MA3SamplerProvider.ENV_DONE)
			return 0;
		
		// FM sample
		if (!this.algorithm.isWave)
		{
			if (feedback && this.fb != 0)
				mod += this.fb0 + this.fb1 >> 9 - this.fb;
			this.fb1 = this.fb0;
			x =
				MA3SamplerProvider.WAVES[this.ws][(this.oscPhase >> 9) + mod & 1023] + (this.envOut << 3);
			this.fb0 =
				MA3SamplerProvider.EXP[x & 0xFF] << 1 >> (x >> 8 & 31) ^ x >> 31;
		}
		
		// Wave sample
		else
		{
			int[] samples = !this.algorithm.rm ? this.instance.wavRam :
				MA3SamplerProvider.MA3_WAVEROM[this.algorithm.waveId];
			
			// Select the sample from wave memory
			if (samples != null && this.wavSample < this.algorithm.ep)
			{
				
				// Produce the output sample
				x = (int)Math.floor(this.wavSample);
				this.fb0 =
					samples[x] * MA3SamplerProvider.WAVE_ENV[this.envOut] / 32767;
				
				// Advance to the next sample
				this.wavSample += this.algorithm.wavAdvance;
				if (this.wavSample >= this.algorithm.ep)
				{
					if (this.algorithm.lp < this.algorithm.ep)
					{
						this.wavSample =
							(this.wavSample - this.algorithm.lp) % (this.algorithm.ep - this.algorithm.lp) + this.algorithm.lp;
					}
					else
					{
						this.wavSample = this.algorithm.ep;
						this.note.stop();
					}
				}
				
			}
			
			// Do not select a sample from wave memory
			else
				this.fb0 = 0;
		}
		
		// Advance the envelope
		x = this.envRate == 0 ? 0 : Math.min(63,
			(this.envRate << 2) + this.envRof);
		this.envPhase += this.envRate == 0 ? 0 : (4 | x & 3) << (x >> 2);
		y = this.envPhase >> 15;
		this.envPhase &= 0x7FFF;
		switch (this.envStage)
		{
			case MA3SamplerProvider.ENV_ATTACK:
				if (y == 0)
					break;
				this.envLevel += ~(this.envLevel * y >> 3);
				if (this.envLevel <= 0)
				{
					this.envLevel = 0;
					this.envRate = this.dr;
					this.envStage = MA3SamplerProvider.ENV_DECAY;
				}
				break;
			case MA3SamplerProvider.ENV_DECAY:
			case MA3SamplerProvider.ENV_SUSTAIN:
			case MA3SamplerProvider.ENV_RELEASE:
				this.envLevel += y;
				if (this.envStage == MA3SamplerProvider.ENV_DECAY && this.envLevel >= MA3SamplerProvider.SUSTAINS[this.sl])
				{
					this.envLevel = MA3SamplerProvider.SUSTAINS[this.sl];
					this.envRate = this.sr;
					this.envStage = MA3SamplerProvider.ENV_SUSTAIN;
				}
				if (this.envLevel >= 511)
				{
					this.envLevel = 511;
					this.envStage = MA3SamplerProvider.ENV_DONE;
					this.note.onEnvelopeDone();
				}
				break;
			case MA3SamplerProvider.ENV_DONE:
				this.envLevel = 511;
				break;
		}
		
		// Attenuate the envelope output
		this.envOut = this.envLevel + this.kslOut + (this.tl << 2);
		if (this.eam)
		{
			this.envOut +=
				MA3SamplerProvider.AM_LFO_A[this.amPhase >> 12] << this.dam >> 2;
			this.amPhase =
				(this.amPhase + MA3SamplerProvider.AM_LFO_B[this.algorithm.lfo]) % (0x34000);
		}
		this.envOut = Math.min(Math.max(this.envOut, 0), 511);
		
		// Wave drums have no oscillator
		if (this.algorithm.isWave)
			return this.fb0;
		
		// Advance the oscillator
		this.oscPhase +=
			(this.note.f_number << this.note.block >> 1) * MA3SamplerProvider.MULTIS[this.multi] >> 1;
		
		// According to available resources, the below algorithm should be
		// correct for vibrato, but no significance has been observed and
		// the output from ATS-MA3-N is no different. It has been disabled
		// pending further reserach. A real MA-3 may be needed.
		//
		// The DVB settings in the MA-2 algorithms are as defined in
		// ATS-MA2-N, with two bits, although the OPL register only uses
		// one bit for DVB. The MA-2 presets may need to be adjusted once
		// the vibrato thing is pinned down.
		//
		// if (evb) {
		//     oscPhase += instance.vibPhase << 19 >> 31 ^ note
		//     .f_number >>
		//        (9 - dvb + ((instance.vibPhase >> 10 & 3) == 3 ? 1 : 
		//        0));
		// }
		
		return this.fb0;
	}
	
}
