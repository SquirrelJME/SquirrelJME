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
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Yamaha MA-3 Audio source.
 *
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
class __MA3Note__
	implements BasicNote
{
	/** FM operator algorithm. */
	@SquirrelJMEVendorApi
	final __MA3Algorithm__ algorithm;
	
	/** Encapsulating channel. */
	@SquirrelJMEVendorApi
	final __MA3Channel__ channel;
	
	/** Encapsulating instance. */
	@SquirrelJMEVendorApi
	final MA3Sampler instance;
	
	/** OPL operators. */
	@SquirrelJMEVendorApi
	final __MA3Operator__[] operators;
	
	/** Current output sample. */
	@SquirrelJMEVendorApi
	final float sample;
	
	/** Frequency advancement when dissociated. */
	@SquirrelJMEVendorApi
	float advance;
	
	/** Amplitude modulator phase. */
	@SquirrelJMEVendorApi
	int amPhase;
	
	/** Effective left stereo amplitude. */
	@SquirrelJMEVendorApi
	float ampLeft;
	
	/** Effective right stereo amplitude. */
	@SquirrelJMEVendorApi
	float ampRight;
	
	/** Octave index. */
	@SquirrelJMEVendorApi
	int block;
	
	/** All operator envelopes are finished. */
	@SquirrelJMEVendorApi
	boolean envDone;
	
	/** Frequency divider. */
	@SquirrelJMEVendorApi
	int f_number;
	
	/** Base frequency. */
	@SquirrelJMEVendorApi
	float freqBase;
	
	/** Note is generating output. */
	@SquirrelJMEVendorApi
	boolean playing;
	
	/** Base volume. */
	@SquirrelJMEVendorApi
	float volBase;
	
	/** Left stereo output amplitude. */
	@SquirrelJMEVendorApi
	float volLeftOut;
	
	/** Right stereo output amplitude. */
	@SquirrelJMEVendorApi
	float volRightOut;
	
	/** Key index within channel. */
	@SquirrelJMEVendorApi
	int key;
	
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
	@SquirrelJMEVendorApi
	__MA3Note__(@NotNull __MA3Channel__ __channel, int __key,
		@NotNull __MA3Algorithm__ __algorithm)
	{
		if (__channel == null || __algorithm == null)
			throw new NullPointerException("NARG");

		this.algorithm = __algorithm;
		this.envDone = false;
		this.ampLeft = 0.0f;
		this.ampRight = 0.0f;
		this.channel = __channel;
		this.instance = __channel.instance;
		this.key = __key;
		this.operators = new __MA3Operator__[__algorithm.operators.length];
		this.playing = true;
		this.sample = 0.0f;
		
		// Operators
		for (int x = 0; x < this.operators.length; x++)
			this.operators[x] = new __MA3Operator__(this,
				__algorithm.operators[x]);
	}

	/**
	 * Called whenever a key-off event needs to be processed.
	 *
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void off()
	{
		// A data-supplied FM algorithm never decays
		if (this.algorithm.isForever)
		{
			this.stop();
			return;
		}
		
		// Ignore key-off for wave drums
		// Should apply to certain hi-hat notes, but needs research
		if (this.algorithm.isWave)
			return;
		
		// Regular processing: switch all operators to release stage
		for (__MA3Operator__ op : this.operators)
		{
			if (op.envStage == MA3SamplerProvider.ENV_DONE || op.xof)
				continue;
			op.envRate = op.rr;
			op.envStage = MA3SamplerProvider.ENV_RELEASE;
		}
	}
	
	/**
	 * Called whenever an envelope has finished.
	 *
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void onEnvelopeDone()
	{
		this.envDone = true;
		
		__MA3Operator__[] operators = this.operators;
		
		// Test all relevant operators
		int flags = this.algorithm.isWave ? 1 :
			MA3SamplerProvider.ENV_FLAGS[this.algorithm.alg];
		for (int x = 0; x < operators.length; x++, flags >>= 1)
		{
			if ((flags & 1) != 0)
				this.envDone = this.envDone &&
					operators[x].envStage == MA3SamplerProvider.ENV_DONE;
		}
		
		// If all relevant operators are done, shut off the note
		if (this.envDone)
			this.playing = false;
	}
	
	/**
	 * Called whenever the note frequency changes.
	 *
	 * @param __bend The Pitch Bend to apply on the new frequency.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void onFrequency(double __bend)
	{
		
		// Wave notes don't use oscillators
		if (this.algorithm.isWave)
			return;
		
		// Compute BLOCK and F_NUMBER
		double freq =
			this.algorithm.isDrum ? this.freqBase : this.freqBase * __bend;
		this.block = Math.min(7, Math.max(0, (int)(Math.round(ExtraMath.log(
			freq / 440) * MA3SamplerProvider.MAGIC_B) + 57) / 12));
		this.f_number = Math.min(1023, Math.max(0, (int)Math.round(
			freq * (1 << 20 - this.block) * MA3SamplerProvider.MAGIC_F)));
		
		// Notify operators
		for (__MA3Operator__ op : this.operators)
			op.onFrequency();
	}

	/**
	 * Called whenever the Master Volume for playback changes.
	 *
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void onVolume()
	{
		this.volLeftOut =
			this.volBase * this.algorithm.volLeft * this.channel.volLeftOut;
		this.volRightOut =
			this.volBase * this.algorithm.volRight * this.channel.volRightOut;
	}
	
	/**
	 * Renders the next audio sample.
	 *
	 * @return If the note has finished generating output.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	boolean render()
	{
		// Compute desired left and right volume levels
		float tgtLeft = 0.0f;
		float tgtRight = 0.0f;
		if (!this.envDone)
		{
			tgtLeft = this.volLeftOut;
			tgtRight = this.volRightOut;
		}
		
		// Generate the sample
		float sample = !this.algorithm.isWave ? this.__sampleFM() :
			this.operators[0].sample(0, false) / 32768.0f;
		this.instance.smpNext[0] += sample * this.ampLeft;
		this.instance.smpNext[1] += sample * this.ampRight;
		
		// Adjust stereo levels
		this.ampLeft = this.__ease(this.ampLeft, tgtLeft);
		this.ampRight = this.__ease(this.ampRight, tgtRight);
		
		// Indicate whether the note has finished generating output
		return !this.playing && this.ampLeft == 0 && this.ampRight == 0;
	}
	
	/**
	 * Stops playback on this audio source.
	 *
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	void stop()
	{
		this.envDone = true;
		this.playing = false;
		this.volBase = 0.0f;
		for (__MA3Operator__ op : this.operators)
		{
			op.envLevel = 511;
			op.envStage = MA3SamplerProvider.ENV_DONE;
		}
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
			__level + this.instance.volRate) : __level > __target ? Math.max(
			__target, __level - this.instance.volRate) : __level;
	}

	/**
	 * Generates an FM sample based on the current FM algorithm.
	 *
	 * @return The generated FM sample.
	 * @since 2025/05/05
	 */
	private float __sampleFM()
	{
		__MA3Operator__[] operators = this.operators;
		
		int out1, out2, out3, out4;
		int ret = 0;
		switch (this.algorithm.alg)
		{
			case 0:
				out1 = operators[0].sample(0, true);
				out2 = operators[1].sample(out1, false);
				ret = out2;
				break;
			case 1:
				out1 = operators[0].sample(0, true);
				out2 = operators[1].sample(0, false);
				ret = out1 + out2;
				break;
			case 2:
				out1 = operators[0].sample(0, true);
				out2 = operators[1].sample(0, false);
				out3 = operators[2].sample(0, true);
				out4 = operators[3].sample(0, false);
				ret = out1 + out2 + out3 + out4;
				break;
			case 3:
				out1 = operators[0].sample(0, true);
				out2 = operators[1].sample(0, false);
				out3 = operators[2].sample(out2, false);
				out4 = operators[3].sample(out1 + out3, false);
				ret = out4;
				break;
			case 4:
				out1 = operators[0].sample(0, true);
				out2 = operators[1].sample(out1, false);
				out3 = operators[2].sample(out2, false);
				out4 = operators[3].sample(out3, false);
				ret = out4;
				break;
			case 5:
				out1 = operators[0].sample(0, true);
				out2 = operators[1].sample(out1, false);
				out3 = operators[2].sample(0, true);
				out4 = operators[3].sample(out3, false);
				ret = out2 + out4;
				break;
			case 6:
				out1 = operators[0].sample(0, true);
				out2 = operators[1].sample(0, false);
				out3 = operators[2].sample(out2, false);
				out4 = operators[3].sample(out3, false);
				ret = out1 + out4;
				break;
			case 7:
				out1 = operators[0].sample(0, true);
				out2 = operators[1].sample(0, false);
				out3 = operators[2].sample(out2, false);
				out4 = operators[3].sample(0, false);
				ret = out1 + out3 + out4;
				break;
		}
		//  Twice the max sample value
		return ret / 8170.0f;
	}
}
