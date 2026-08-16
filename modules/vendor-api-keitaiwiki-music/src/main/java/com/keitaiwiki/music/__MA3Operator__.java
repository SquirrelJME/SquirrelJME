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

import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Individual FM algorithm operator.
 *
 * @since 2025/05/05
 */
class __MA3Operator__
	implements BasicOperator
{
	/** Encapsulating algorithm */
	final __MA3Algorithm__ _algorithm;
	
	/** Envelope attack rate register. */
	final int _ar;
	
	/** Amplitude modulation depth register. */
	final int _dam;
	
	/** Envelope decay rate register. */
	final int _dr;
	
	/** Frequency modulation depth register. */
	final int _dvb;
	
	/** Enable amplutide modulation register. */
	final boolean _eam;
	
	/** Enable frequency modulation register. */
	final boolean _evb;
	
	/** Encapsulating instance. */
	final MA3Sampler _instance;
	
	/** Encapsulating note. */
	final __MA3Note__ _note;
	
	/** Envelope release rate register. */
	final int _rr;
	
	/** Envelope sustain level register. */
	final int _sl;
	
	/** Envelope sustain rate register. */
	final int _sr;
	
	/** Register indicating if MIDI Hold 1 is supported. */
	final boolean _sus;
	
	/** Envelope attenuation register. */
	final int _tl;
	
	/** Ignore key-off response register. */
	final boolean _xof;
	
	/** u14 Amplitude modulation counter. */
	int _amPhase;
	
	/** Detune shift. */
	int _dt;
	
	/** u9  Current envelope level. */
	int _envLevel;
	
	/** u9  Effective envelope output. */
	int _envOut;
	
	/** u15 Envelope phase counter. */
	int _envPhase;
	
	/** Current envelope rate of change. */
	int _envRate;
	
	/** Envelope rate offset modifier. */
	int _envRof;
	
	/** Envelope processing stage. */
	int _envStage;
	
	/** Feedback rate index. */
	int _fb;
	
	/** Most recent output sample. */
	int _fb0;
	
	/** Second-most recent output sample. */
	int _fb1;
	
	/** Wave drum parameters are valid. */
	boolean _isValid;
	
	/** Attenuation index per octave. */
	int _ksl;
	
	/** KSL attenuation level. */
	int _kslOut;
	
	/** Envelope rate modifier scale. */
	int _ksr;
	
	/** Frequency multiplier. */
	int _multi;
	
	/** u10 Oscillator counter. */
	int _oscPhase;
	
	/** Current wave source sample. */
	float _wavSample;
	
	/** Wave function index. */
	int _ws;
	
	/**
	 * Creates a new template Operator with non-sample data.
	 *
	 * @param __bytes Data array used to setup this Operator.
	 * @param __offset The offset to start reading the data array from.
	 * @since 2025/05/05
	 */
	__MA3Operator__(@NotNull byte[] __bytes,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset)
	{
		// Not used for non-samples //
		this._algorithm = null;
		this._instance = null;
		this._note = null;
		//////////////////////////////
		
		this._sus = (__bytes[__offset] >> 3 & 1) != 0;
		this._ksr = __bytes[__offset] >> 2 & 1;
		this._eam = (__bytes[__offset] >> 1 & 1) != 0;
		this._evb = (__bytes[__offset] & 1) != 0;
		this._multi = __bytes[__offset + 1] >> 4 & 15;
		this._dt = __bytes[__offset + 1] >> 1 & 7;
		this._xof = (__bytes[__offset + 1] & 1) != 0;
		this._ar = __bytes[__offset + 2] >> 4 & 15;
		this._dr = __bytes[__offset + 2] & 15;
		this._sr = __bytes[__offset + 3] >> 4 & 15;
		this._rr = __bytes[__offset + 3] & 15;
		this._sl = __bytes[__offset + 4] >> 4 & 15;
		this._dam = __bytes[__offset + 4] >> 2 & 3;
		this._dvb = __bytes[__offset + 4] & 3;
		this._tl = __bytes[__offset + 5] >> 2 & 63;
		this._ksl = __bytes[__offset + 5] & 3;
		this._fb = __bytes[__offset + 6] >> 5 & 7;
		this._ws = __bytes[__offset + 6] & 31;
	}
	
	/**
	 * Creates a new Operator for Wave data.
	 *
	 * @param __offset The offset to start reading the data array from.
	 * @param __message Data array used to setup this Operator.
	 * @since 2025/05/05
	 */
	__MA3Operator__(@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@NotNull byte[] __message)
	{
		// Not used for non-samples //
		this._algorithm = null;
		this._instance = null;
		this._note = null;
		//////////////////////////////
		
		int bits;
		bits = __message[__offset++] & 0xFF;
		this._sr = bits >> 4 & 15;
		this._xof = (bits >> 3 & 1) != 0;
		this._sus = (bits >> 1 & 1) != 0;
		bits = __message[__offset++] & 0xFF;
		this._rr = bits >> 4 & 15;
		this._dr = bits & 15;
		bits = __message[__offset++] & 0xFF;
		this._ar = bits >> 4 & 15;
		this._sl = bits & 15;
		bits = __message[__offset++] & 0xFF;
		this._tl = bits >> 2 & 63;
		bits = __message[__offset++] & 0xFF;
		this._dam = bits >> 5 & 3;
		this._eam = (bits >> 4 & 1) != 0;
		this._dvb = bits >> 1 & 3;
		this._evb = (bits & 1) != 0;
	}

	/**
	 * Creates a new Operator with the same configuration from another
	 * {@link __MA3Operator__} for playback of notes.
	 *
	 * @param __note The {@link __MA3Note__} containing playback data.
	 * @param __o The {@link __MA3Operator__} to replicate the config from.
	 * @since 2025/05/05
	 */
	__MA3Operator__(@NotNull __MA3Note__ __note, @NotNull __MA3Operator__ __o)
	{
		
		// OPL registers
		this._ar = __o._ar;
		this._dam = __o._dam;
		this._dr = __o._dr;
		this._dt = __o._dt;
		this._dvb = __o._dvb;
		this._eam = __o._eam;
		this._evb = __o._evb;
		this._fb = __o._fb;
		this._ksl = __o._ksl;
		this._ksr = __o._ksr;
		this._multi = __o._multi;
		this._rr = __o._rr;
		this._sl = __o._sl;
		this._sr = __o._sr;
		this._sus = __o._sus;
		this._tl = __o._tl;
		this._ws = __o._ws;
		this._xof = __o._xof;
		
		
		this._algorithm = __note._algorithm;
		this._amPhase = __note._instance._amPhase;
		this._envLevel = 511;
		this._envPhase = 0;
		this._envRate = this._ar;
		this._envStage = MA3SamplerProvider.ENV_ATTACK;
		this._instance = __note._instance;
		this._note = __note;
		this._oscPhase = 0;
		this._wavSample = 0;
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
	__MA3Operator__(@NotNull byte[] __message,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		boolean __diff)
	{
		// Not used for non-samples //
		this._algorithm = null;
		this._instance = null;
		this._note = null;
		//////////////////////////////
		
		int bits;
		bits = __message[__offset++] & 0xFF;
		this._sr = bits >> 4 & 15;
		this._xof = (bits >> 3 & 1) != 0;
		this._sus = (bits >> 1 & 1) != 0;
		this._ksr = bits & 1;
		bits = __message[__offset++] & 0xFF;
		this._rr = bits >> 4 & 15;
		this._dr = bits & 15;
		bits = __message[__offset++] & 0xFF;
		this._ar = bits >> 4 & 15;
		this._sl = bits & 15;
		bits = __message[__offset++] & 0xFF;
		this._tl = bits >> 2 & 63;
		this._ksl = bits & 3;
		bits = __message[__offset++] & 0xFF;
		this._dam = bits >> 5 & 3;
		this._eam = (bits >> 4 & 1) != 0;
		this._dvb = bits >> 1 & 3;
		this._evb = (bits & 1) != 0;
		bits = __message[__offset++] & 0xFF;
		this._multi = bits >> 4 & 15;
		this._dt = bits & 7;
		bits = __message[__offset++] & 0xFF;
		this._ws = bits >> 3 & 31;
		this._fb = bits & 7;
	}

	/**
	 * Called whenever the note frequency changes. Should only be called for
	 * FM samples.
	 *
	 * @since 2025/05/05
	 */
	void __onFrequency()
	{
		// These are only ever used for FM samples, so this failure condition
		// should never occur
		__MA3Note__ note = this._note;
		if (note == null)
			throw Debugging.oops();
		
		this._envRof =
			(note._block << 1 | note._fNumber >> 8 + 
				MA3SamplerProvider.NTS & 1) >> ((this._ksr ^ 1) << 1);
		this._kslOut = Math.max(0,
			MA3SamplerProvider.KSL_B[this._ksl] * ((note._block << 3) - 
				MA3SamplerProvider.KSL_F[note._fNumber >> 6]));
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
	int __sample(int __mod, boolean __feedback)
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
		if (this._envStage == MA3SamplerProvider.ENV_DONE)
			return 0;
		
		// These are only ever used for FM samples, so this failure condition
		// should never occur
		__MA3Algorithm__ algorithm = this._algorithm;
		MA3Sampler instance = this._instance;
		__MA3Note__ note = this._note;
		if (algorithm == null || instance == null || note == null)
			throw Debugging.oops();
		
		// FM sample
		if (!algorithm._isWave)
		{
			if (__feedback && this._fb != 0)
				__mod += this._fb0 + this._fb1 >> 9 - this._fb;
			this._fb1 = this._fb0;
			x = constWaves[this._ws][(this._oscPhase >> 9) + __mod & 1023] +
					(this._envOut << 3);
			this._fb0 =
				constExp[x & 0xFF] << 1 >> (x >> 8 & 31) ^ x >> 31;
		}
		
		// Wave sample
		else
		{
			int[] samples = !algorithm._rm ? instance._wavRam :
				constMa3WaveRom[algorithm._waveId];
			
			// Select the sample from wave memory
			if (samples != null && this._wavSample < algorithm._ep)
			{
				// Produce the output sample
				x = (int)Math.floor(this._wavSample);
				this._fb0 =
					samples[x] * constWaveEnv[this._envOut] / 32767;
				
				// Advance to the next sample
				this._wavSample += algorithm._wavAdvance;
				if (this._wavSample >= algorithm._ep)
				{
					if (algorithm._lp < algorithm._ep)
					{
						this._wavSample = (this._wavSample - algorithm._lp) %
							(algorithm._ep - algorithm._lp) + algorithm._lp;
					}
					else
					{
						this._wavSample = algorithm._ep;
						note.__stop();
					}
				}
				
			}
			
			// Do not select a sample from wave memory
			else
				this._fb0 = 0;
		}
		
		// Advance the envelope
		x = this._envRate == 0 ? 0 : Math.min(63,
			(this._envRate << 2) + this._envRof);
		this._envPhase += this._envRate == 0 ? 0 : (4 | x & 3) << (x >> 2);
		y = this._envPhase >> 15;
		this._envPhase &= 0x7FFF;
		switch (this._envStage)
		{
			case MA3SamplerProvider.ENV_ATTACK:
				if (y == 0)
					break;
				this._envLevel += ~(this._envLevel * y >> 3);
				if (this._envLevel <= 0)
				{
					this._envLevel = 0;
					this._envRate = this._dr;
					this._envStage = MA3SamplerProvider.ENV_DECAY;
				}
				break;
				
			case MA3SamplerProvider.ENV_DECAY:
			case MA3SamplerProvider.ENV_SUSTAIN:
			case MA3SamplerProvider.ENV_RELEASE:
				this._envLevel += y;
				if (this._envStage == MA3SamplerProvider.ENV_DECAY && 
					this._envLevel >= constSustains[this._sl])
				{
					this._envLevel = constSustains[this._sl];
					this._envRate = this._sr;
					this._envStage = MA3SamplerProvider.ENV_SUSTAIN;
				}
				
				if (this._envLevel >= 511)
				{
					this._envLevel = 511;
					this._envStage = MA3SamplerProvider.ENV_DONE;
					note.__onEnvelopeDone();
				}
				break;
				
			case MA3SamplerProvider.ENV_DONE:
				this._envLevel = 511;
				break;
		}
		
		// Attenuate the envelope output
		this._envOut = this._envLevel + this._kslOut + (this._tl << 2);
		if (this._eam)
		{
			this._envOut +=
				constAmLfoA[this._amPhase >> 12] << this._dam >> 2;
			this._amPhase =
				(this._amPhase + constAmLfoB[algorithm._lfo]) % (0x34000);
		}
		this._envOut = Math.min(Math.max(this._envOut, 0), 511);
		
		// Wave drums have no oscillator
		if (algorithm._isWave)
			return this._fb0;
		
		// Advance the oscillator
		this._oscPhase +=
			(note._fNumber << note._block >> 1) * constMultis[this._multi] >> 1;
		
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
		
		return this._fb0;
	}
}
