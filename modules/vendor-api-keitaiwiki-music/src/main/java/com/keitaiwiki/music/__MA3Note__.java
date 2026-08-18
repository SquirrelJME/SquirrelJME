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
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Yamaha MA-3 Audio source.
 *
 * @since 2025/05/05
 */
class __MA3Note__
	implements BasicNote
{
	/** FM operator algorithm. */
	final __MA3Algorithm__ _algorithm;
	
	/** Encapsulating channel. */
	final __MA3Channel__ _channel;
	
	/** Encapsulating instance. */
	final MA3Sampler _instance;
	
	/** OPL operators. */
	final __MA3Operator__[] _operators;
	
	/** Current output sample. */
	final float _sample;
	
	/** Frequency advancement when dissociated. */
	float _advance;
	
	/** Amplitude modulator phase. */
	int _amPhase;
	
	/** Effective left stereo amplitude. */
	float _ampLeft;
	
	/** Effective right stereo amplitude. */
	float _ampRight;
	
	/** Octave index. */
	int _block;
	
	/** All operator envelopes are finished. */
	boolean _envDone;
	
	/** Frequency divider. */
	int _fNumber;
	
	/** Base frequency. */
	float _freqBase;
	
	/** Key index within channel. */
	int _key;
	
	/** Note is generating output. */
	boolean _playing;
	
	/** Base volume. */
	float _volBase;
	
	/** Left stereo output amplitude. */
	float _volLeftOut;
	
	/** Right stereo output amplitude. */
	float _volRightOut;
	
	/**
	 * Creates a new audio source for the specified channel, key index and
	 * FM algorithm.
	 *
	 * @param __channel The {@link __MA3Channel__} to use.
	 * @param __key The key index within the specified channel.
	 * @param __algorithm The {@link __MA3Algorithm__} for audio generation.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/05/05
	 */
	__MA3Note__(@NotNull __MA3Channel__ __channel, int __key,
		@NotNull __MA3Algorithm__ __algorithm)
	{
		if (__channel == null || __algorithm == null)
			throw new NullPointerException("NARG");

		this._algorithm = __algorithm;
		this._envDone = false;
		this._ampLeft = 0.0f;
		this._ampRight = 0.0f;
		this._channel = __channel;
		this._instance = __channel._instance;
		this._key = __key;
		this._operators = new __MA3Operator__[__algorithm._operators.length];
		this._playing = true;
		this._sample = 0.0f;
		
		// Operators
		for (int x = 0; x < this._operators.length; x++)
			this._operators[x] = new __MA3Operator__(this,
				__algorithm._operators[x]);
	}

	/**
	 * Called whenever an easing effect needs to be applied to the amplitude
	 * controller.
	 *
	 * @param __level The base volume level for the easing effect.
	 * @param __target The target volume level for the easing effect.
	 * @return The resulting volume level.
	 * @since 2025/05/05
	 */
	private float __ease(float __level, float __target)
	{
		return __level < __target ? Math.min(__target,
			__level + this._instance._volRate) : __level > __target ? Math.max(
			__target, __level - this._instance._volRate) : __level;
	}
	
	/**
	 * Called whenever a key-off event needs to be processed.
	 *
	 * @since 2025/05/05
	 */
	void __off()
	{
		// A data-supplied FM algorithm never decays
		if (this._algorithm._isForever)
		{
			this.__stop();
			return;
		}
		
		// Ignore key-off for wave drums
		// Should apply to certain hi-hat notes, but needs research
		if (this._algorithm._isWave)
			return;
		
		// Regular processing: switch all operators to release stage
		for (__MA3Operator__ op : this._operators)
		{
			if (op._envStage == MA3SamplerProvider.ENV_DONE || op._xof)
				continue;
			op._envRate = op._rr;
			op._envStage = MA3SamplerProvider.ENV_RELEASE;
		}
	}
	
	/**
	 * Called whenever an envelope has finished.
	 *
	 * @since 2025/05/05
	 */
	void __onEnvelopeDone()
	{
		this._envDone = true;
		
		__MA3Operator__[] operators = this._operators;
		
		// Test all relevant operators
		int flags = this._algorithm._isWave ? 1 :
			MA3SamplerProvider.ENV_FLAGS[this._algorithm._alg];
		for (int x = 0; x < operators.length; x++, flags >>= 1)
		{
			if ((flags & 1) != 0)
				this._envDone = this._envDone &&
					operators[x]._envStage == MA3SamplerProvider.ENV_DONE;
		}
		
		// If all relevant operators are done, shut off the note
		if (this._envDone)
			this._playing = false;
	}

	/**
	 * Called whenever the note frequency changes.
	 *
	 * @param __bend The Pitch Bend to apply on the new frequency.
	 * @since 2025/05/05
	 */
	void __onFrequency(double __bend)
	{
		
		// Wave notes don't use oscillators
		if (this._algorithm._isWave)
			return;
		
		// Compute BLOCK and F_NUMBER
		double freq =
			this._algorithm._isDrum ? this._freqBase : this._freqBase * __bend;
		this._block = Math.min(7, Math.max(0, (int)(Math.round(ExtraMath.log(
			freq / 440) * MA3SamplerProvider.MAGIC_B) + 57) / 12));
		this._fNumber = Math.min(1023, Math.max(0, (int)Math.round(
			freq * (1 << 20 - this._block) * MA3SamplerProvider.MAGIC_F)));
		
		// Notify operators
		for (__MA3Operator__ op : this._operators)
			op.__onFrequency();
	}
	
	/**
	 * Called whenever the Master Volume for playback changes.
	 *
	 * @since 2025/05/05
	 */
	void __onVolume()
	{
		this._volLeftOut =
			this._volBase * this._algorithm._volLeft * this._channel._volLeftOut;
		this._volRightOut =
			this._volBase * this._algorithm._volRight * this._channel._volRightOut;
	}
	
	/**
	 * Renders the next audio sample.
	 *
	 * @return If the note has finished generating output.
	 * @since 2025/05/05
	 */
	boolean __render()
	{
		// Compute desired left and right volume levels
		float tgtLeft = 0.0f;
		float tgtRight = 0.0f;
		if (!this._envDone)
		{
			tgtLeft = this._volLeftOut;
			tgtRight = this._volRightOut;
		}
		
		// Generate the sample
		float sample = !this._algorithm._isWave ? this.__sampleFM() :
			this._operators[0].__sample(0, false) / 32768.0f;
		this._instance._smpNext[0] += sample * this._ampLeft;
		this._instance._smpNext[1] += sample * this._ampRight;
		
		// Adjust stereo levels
		this._ampLeft = this.__ease(this._ampLeft, tgtLeft);
		this._ampRight = this.__ease(this._ampRight, tgtRight);
		
		// Indicate whether the note has finished generating output
		return !this._playing && this._ampLeft == 0 && this._ampRight == 0;
	}

	/**
	 * Generates an FM sample based on the current FM algorithm.
	 *
	 * @return The generated FM sample.
	 * @since 2025/05/05
	 */
	private float __sampleFM()
	{
		__MA3Operator__[] operators = this._operators;
		
		int out1, out2, out3, out4;
		int ret = 0;
		switch (this._algorithm._alg)
		{
			case 0:
				out1 = operators[0].__sample(0, true);
				out2 = operators[1].__sample(out1, false);
				ret = out2;
				break;
			case 1:
				out1 = operators[0].__sample(0, true);
				out2 = operators[1].__sample(0, false);
				ret = out1 + out2;
				break;
			case 2:
				out1 = operators[0].__sample(0, true);
				out2 = operators[1].__sample(0, false);
				out3 = operators[2].__sample(0, true);
				out4 = operators[3].__sample(0, false);
				ret = out1 + out2 + out3 + out4;
				break;
			case 3:
				out1 = operators[0].__sample(0, true);
				out2 = operators[1].__sample(0, false);
				out3 = operators[2].__sample(out2, false);
				out4 = operators[3].__sample(out1 + out3, false);
				ret = out4;
				break;
			case 4:
				out1 = operators[0].__sample(0, true);
				out2 = operators[1].__sample(out1, false);
				out3 = operators[2].__sample(out2, false);
				out4 = operators[3].__sample(out3, false);
				ret = out4;
				break;
			case 5:
				out1 = operators[0].__sample(0, true);
				out2 = operators[1].__sample(out1, false);
				out3 = operators[2].__sample(0, true);
				out4 = operators[3].__sample(out3, false);
				ret = out2 + out4;
				break;
			case 6:
				out1 = operators[0].__sample(0, true);
				out2 = operators[1].__sample(0, false);
				out3 = operators[2].__sample(out2, false);
				out4 = operators[3].__sample(out3, false);
				ret = out1 + out4;
				break;
			case 7:
				out1 = operators[0].__sample(0, true);
				out2 = operators[1].__sample(0, false);
				out3 = operators[2].__sample(out2, false);
				out4 = operators[3].__sample(0, false);
				ret = out1 + out3 + out4;
				break;
		}
		//  Twice the max sample value
		return ret / 8170.0f;
	}

	/**
	 * Stops playback on this audio source.
	 *
	 * @since 2025/05/05
	 */
	void __stop()
	{
		this._envDone = true;
		this._playing = false;
		this._volBase = 0.0f;
		for (__MA3Operator__ op : this._operators)
		{
			op._envLevel = 511;
			op._envStage = MA3SamplerProvider.ENV_DONE;
		}
	}
}
