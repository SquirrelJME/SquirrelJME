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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Individual FM algorithm operator.
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
class MA3Operator
	implements BasicOperator
{
	/** Envelope attack rate register. */
	@SquirrelJMEVendorApi
	final int ar;
	
	/** Amplitude modulation depth register. */
	@SquirrelJMEVendorApi
	final int dam;
	
	/** Envelope decay rate register. */
	@SquirrelJMEVendorApi
	final int dr;
	
	/** Frequency modulation depth register. */
	@SquirrelJMEVendorApi
	final int dvb;
	
	/** Enable amplutide modulation register. */
	@SquirrelJMEVendorApi
	final boolean eam;
	
	/** Enable frequency modulation register. */
	@SquirrelJMEVendorApi
	final boolean evb;
	
	/** Envelope release rate register. */
	@SquirrelJMEVendorApi
	final int rr;
	
	/** Envelope sustain level register. */
	@SquirrelJMEVendorApi
	final int sl;
	
	/** Envelope sustain rate register. */
	@SquirrelJMEVendorApi
	final int sr;
	
	/** Register indicating if MIDI Hold 1 is supported. */
	@SquirrelJMEVendorApi
	final boolean sus;
	
	/** Envelope attenuation register. */
	@SquirrelJMEVendorApi
	final int tl;
	
	/** Ignore key-off response register. */
	@SquirrelJMEVendorApi
	final boolean xof;
	
	/** Encapsulating algorithm */
	@SquirrelJMEVendorApi
	final MA3Algorithm algorithm;
	
	/** u14 Amplitude modulation counter. */
	@SquirrelJMEVendorApi
	int amPhase;
	
	/** Detune shift. */
	@SquirrelJMEVendorApi
	int dt;
	
	/** u9  Current envelope level. */
	@SquirrelJMEVendorApi
	int envLevel;
	
	/** u9  Effective envelope output. */
	@SquirrelJMEVendorApi
	int envOut;
	
	/** u15 Envelope phase counter. */
	@SquirrelJMEVendorApi
	int envPhase;
	
	/** Current envelope rate of change. */
	@SquirrelJMEVendorApi
	int envRate;
	
	/** Envelope rate offset modifier. */
	@SquirrelJMEVendorApi
	int envRof;
	
	/** Envelope processing stage. */
	@SquirrelJMEVendorApi
	int envStage;
	
	/** Feedback rate index. */
	@SquirrelJMEVendorApi
	int fb;
	
	/** Most recent output sample. */
	@SquirrelJMEVendorApi
	int fb0;
	
	/** Second-most recent output sample. */
	@SquirrelJMEVendorApi
	int fb1;
	
	/** Encapsulating instance. */
	@SquirrelJMEVendorApi
	final MA3Sampler instance;
	
	/** Wave drum parameters are valid. */
	@SquirrelJMEVendorApi
	boolean isValid;
	
	/** Attenuation index per octave. */
	@SquirrelJMEVendorApi
	int ksl;
	
	/** KSL attenuation level. */
	@SquirrelJMEVendorApi
	int kslOut;
	
	/** Envelope rate modifier scale. */
	@SquirrelJMEVendorApi
	int ksr;
	
	/** Frequency multiplier. */
	@SquirrelJMEVendorApi
	int multi;
	
	/** Encapsulating note. */
	@SquirrelJMEVendorApi
	final MA3Note note;
	
	/** u10 Oscillator counter. */
	@SquirrelJMEVendorApi
	int oscPhase;
	
	/** Current wave source sample. */
	@SquirrelJMEVendorApi
	float wavSample;
	
	/** Wave function index. */
	@SquirrelJMEVendorApi
	int ws;
	
	/**
	 * Creates a new template Operator with non-sample data.
	 *
	 * @param __bytes Data array used to setup this Operator.
	 * @param __offset The offset to start reading the data array from.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	MA3Operator(@NotNull byte[] __bytes,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset)
	{
		// Not used for non-samples //
		this.algorithm = null;
		this.instance = null;
		this.note = null;
		//////////////////////////////
		
		this.sus = (__bytes[__offset] >> 3 & 1) != 0;
		this.ksr = __bytes[__offset] >> 2 & 1;
		this.eam = (__bytes[__offset] >> 1 & 1) != 0;
		this.evb = (__bytes[__offset] & 1) != 0;
		this.multi = __bytes[__offset + 1] >> 4 & 15;
		this.dt = __bytes[__offset + 1] >> 1 & 7;
		this.xof = (__bytes[__offset + 1] & 1) != 0;
		this.ar = __bytes[__offset + 2] >> 4 & 15;
		this.dr = __bytes[__offset + 2] & 15;
		this.sr = __bytes[__offset + 3] >> 4 & 15;
		this.rr = __bytes[__offset + 3] & 15;
		this.sl = __bytes[__offset + 4] >> 4 & 15;
		this.dam = __bytes[__offset + 4] >> 2 & 3;
		this.dvb = __bytes[__offset + 4] & 3;
		this.tl = __bytes[__offset + 5] >> 2 & 63;
		this.ksl = __bytes[__offset + 5] & 3;
		this.fb = __bytes[__offset + 6] >> 5 & 7;
		this.ws = __bytes[__offset + 6] & 31;
	}
	
	/**
	 * Creates a new Operator for Wave data.
	 *
	 * @param __offset The offset to start reading the data array from.
	 * @param __message Data array used to setup this Operator.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	MA3Operator(@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@NotNull byte[] __message)
	{
		// Not used for non-samples //
		this.algorithm = null;
		this.instance = null;
		this.note = null;
		//////////////////////////////
		
		int bits;
		bits = __message[__offset++] & 0xFF;
		this.sr = bits >> 4 & 15;
		this.xof = (bits >> 3 & 1) != 0;
		this.sus = (bits >> 1 & 1) != 0;
		bits = __message[__offset++] & 0xFF;
		this.rr = bits >> 4 & 15;
		this.dr = bits & 15;
		bits = __message[__offset++] & 0xFF;
		this.ar = bits >> 4 & 15;
		this.sl = bits & 15;
		bits = __message[__offset++] & 0xFF;
		this.tl = bits >> 2 & 63;
		bits = __message[__offset++] & 0xFF;
		this.dam = bits >> 5 & 3;
		this.eam = (bits >> 4 & 1) != 0;
		this.dvb = bits >> 1 & 3;
		this.evb = (bits & 1) != 0;
	}

	/**
	 * Creates a new Operator with the same configuration from another
	 * {@link MA3Operator} for playback of notes.
	 *
	 * @param __note The {@link MA3Note} containing playback data.
	 * @param __o The {@link MA3Operator} to replicate the config from.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	MA3Operator(@NotNull MA3Note __note, @NotNull MA3Operator __o)
	{
		
		// OPL registers
		this.ar = __o.ar;
		this.dam = __o.dam;
		this.dr = __o.dr;
		this.dt = __o.dt;
		this.dvb = __o.dvb;
		this.eam = __o.eam;
		this.evb = __o.evb;
		this.fb = __o.fb;
		this.ksl = __o.ksl;
		this.ksr = __o.ksr;
		this.multi = __o.multi;
		this.rr = __o.rr;
		this.sl = __o.sl;
		this.sr = __o.sr;
		this.sus = __o.sus;
		this.tl = __o.tl;
		this.ws = __o.ws;
		this.xof = __o.xof;
		
		
		this.algorithm = __note.algorithm;
		this.amPhase = __note.instance.amPhase;
		this.envLevel = 511;
		this.envPhase = 0;
		this.envRate = this.ar;
		this.envStage = MA3SamplerProvider.ENV_ATTACK;
		this.instance = __note.instance;
		this.note = __note;
		this.oscPhase = 0;
		this.wavSample = 0;
	}

	/**
	 * Creates a new Operator for SysEx messages.
	 *
	 * @param __message Data array used to setup this Operator.
	 * @param __offset The offset to start reading the data array from.
	 * @param __diff Only really used to differentiate it from other
	 * constructors.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	MA3Operator(@NotNull byte[] __message,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		boolean __diff)
	{
		// Not used for non-samples //
		this.algorithm = null;
		this.instance = null;
		this.note = null;
		//////////////////////////////
		
		int bits;
		bits = __message[__offset++] & 0xFF;
		this.sr = bits >> 4 & 15;
		this.xof = (bits >> 3 & 1) != 0;
		this.sus = (bits >> 1 & 1) != 0;
		this.ksr = bits & 1;
		bits = __message[__offset++] & 0xFF;
		this.rr = bits >> 4 & 15;
		this.dr = bits & 15;
		bits = __message[__offset++] & 0xFF;
		this.ar = bits >> 4 & 15;
		this.sl = bits & 15;
		bits = __message[__offset++] & 0xFF;
		this.tl = bits >> 2 & 63;
		this.ksl = bits & 3;
		bits = __message[__offset++] & 0xFF;
		this.dam = bits >> 5 & 3;
		this.eam = (bits >> 4 & 1) != 0;
		this.dvb = bits >> 1 & 3;
		this.evb = (bits & 1) != 0;
		bits = __message[__offset++] & 0xFF;
		this.multi = bits >> 4 & 15;
		this.dt = bits & 7;
		bits = __message[__offset++] & 0xFF;
		this.ws = bits >> 3 & 31;
		this.fb = bits & 7;
	}

	/**
	 * Called whenever the note frequency changes. Should only be called for
	 * FM samples.
	 *
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void onFrequency()
	{
		// These are only ever used for FM samples, so this failure condition
		// should never occur
		MA3Note note = this.note;
		if (note == null)
			throw Debugging.oops();
		
		this.envRof =
			(note.block << 1 | note.f_number >> 8 + 
				MA3SamplerProvider.NTS & 1) >> ((this.ksr ^ 1) << 1);
		this.kslOut = Math.max(0,
			MA3SamplerProvider.KSL_B[this.ksl] * ((note.block << 3) - 
				MA3SamplerProvider.KSL_F[note.f_number >> 6]));
	}

	/**
	 * Generates a sample on an operator.
	 *
	 * @param __mod The modifier to apply on the generation sample, may be
	 * previously generated samples depending on the operator configuration.
	 * @param __feedback Dictate whether feedback rate must be used for the
	 * sample's generation.
	 * @return The generated sample.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	int sample(int __mod, boolean __feedback)
	{
		int[] constSustains = MA3SamplerProvider.SUSTAINS;
		int[][] constWaves = MA3SamplerProvider.WAVES;
		int[] constExp = MA3SamplerProvider.EXP;
		int[][] constMa3WaveRom = MA3SamplerProvider.MA3_WAVEROM;
		int[] constWaveEnv = MA3SamplerProvider.WAVE_ENV;
		int[] constAmLfoA = MA3SamplerProvider.AM_LFO_A;
		int[] constAmLfoB = MA3SamplerProvider.AM_LFO_B;
		int[] constMultis = MA3SamplerProvider.MULTIS;
		
		//  Scratch
		int x, y;
		
		// The envelope has finished
		if (this.envStage == MA3SamplerProvider.ENV_DONE)
			return 0;
		
		// These are only ever used for FM samples, so this failure condition
		// should never occur
		MA3Algorithm algorithm = this.algorithm;
		MA3Sampler instance = this.instance;
		MA3Note note = this.note;
		if (algorithm == null || instance == null || note == null)
			throw Debugging.oops();
		
		// FM sample
		if (!algorithm.isWave)
		{
			if (__feedback && this.fb != 0)
				__mod += this.fb0 + this.fb1 >> 9 - this.fb;
			this.fb1 = this.fb0;
			x = constWaves[this.ws][(this.oscPhase >> 9) + __mod & 1023] +
					(this.envOut << 3);
			this.fb0 =
				constExp[x & 0xFF] << 1 >> (x >> 8 & 31) ^ x >> 31;
		}
		
		// Wave sample
		else
		{
			int[] samples = !algorithm.rm ? instance.wavRam :
				constMa3WaveRom[algorithm.waveId];
			
			// Select the sample from wave memory
			if (samples != null && this.wavSample < algorithm.ep)
			{
				// Produce the output sample
				x = (int)Math.floor(this.wavSample);
				this.fb0 =
					samples[x] * constWaveEnv[this.envOut] / 32767;
				
				// Advance to the next sample
				this.wavSample += algorithm.wavAdvance;
				if (this.wavSample >= algorithm.ep)
				{
					if (algorithm.lp < algorithm.ep)
					{
						this.wavSample = (this.wavSample - algorithm.lp) %
							(algorithm.ep - algorithm.lp) + algorithm.lp;
					}
					else
					{
						this.wavSample = algorithm.ep;
						note.stop();
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
				if (this.envStage == MA3SamplerProvider.ENV_DECAY && 
					this.envLevel >= constSustains[this.sl])
				{
					this.envLevel = constSustains[this.sl];
					this.envRate = this.sr;
					this.envStage = MA3SamplerProvider.ENV_SUSTAIN;
				}
				
				if (this.envLevel >= 511)
				{
					this.envLevel = 511;
					this.envStage = MA3SamplerProvider.ENV_DONE;
					note.onEnvelopeDone();
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
				constAmLfoA[this.amPhase >> 12] << this.dam >> 2;
			this.amPhase =
				(this.amPhase + constAmLfoB[algorithm.lfo]) % (0x34000);
		}
		this.envOut = Math.min(Math.max(this.envOut, 0), 511);
		
		// Wave drums have no oscillator
		if (algorithm.isWave)
			return this.fb0;
		
		// Advance the oscillator
		this.oscPhase +=
			(note.f_number << note.block >> 1) * constMultis[this.multi] >> 1;
		
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
