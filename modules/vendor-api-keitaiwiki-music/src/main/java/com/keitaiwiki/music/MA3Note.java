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

import cc.squirreljme.runtime.cldc.util.ExtraMath;

/**
 * Audio source
 */
class MA3Note
	implements BasicNote
{
	
	/**
	 * FM operator algorithm
	 */
	final MA3Algorithm algorithm;
	
	/**
	 * Encapsulating channel
	 */
	final MA3Channel channel;
	
	/**
	 * Encapsulating instance
	 */
	final MA3Sampler instance;
	
	/**
	 * OPL operators
	 */
	final MA3Operator[] operators;
	
	/**
	 * Current output sample
	 */
	final float sample;
	
	/**
	 * Frequency advancement when dissociated
	 */
	float advance;
	
	/**
	 * Amplitude modulator phase
	 */
	int amPhase;
	
	/**
	 * Effective left stereo amplitude
	 */
	float ampLeft;
	
	/**
	 * Effective right stereo amplitude
	 */
	float ampRight;
	
	/**
	 * OPL registers
	 * Octave index
	 */
	int block;
	
	/**
	 * All operator envelopes are finished
	 */
	boolean envDone;
	
	/**
	 * Frequency divider
	 */
	int f_number;
	
	/**
	 * Base frequency
	 */
	float freqBase;
	
	/**
	 * Note is currently active on its key
	 */
	boolean playing;
	
	/**
	 * Base volume
	 */
	float volBase;
	
	/**
	 * Left stereo output amplitude
	 */
	float volLeftOut;
	
	/**
	 * Right stereo output amplitude
	 */
	float volRightOut;
	
	
	MA3Note(MA3Channel channel, MA3Algorithm algorithm)
	{
		
		
		this.algorithm = algorithm;
		this.envDone = false;
		this.ampLeft = 0.0f;
		this.ampRight = 0.0f;
		this.channel = channel;
		this.instance = channel.instance;
		this.operators = new MA3Operator[algorithm.operators.length];
		this.sample = 0.0f;
		
		// Operators
		for (int x = 0; x < this.operators.length; x++)
			this.operators[x] = new MA3Operator(this, algorithm.operators[x]);
	}
	
	
	/**
	 * Perform easing on an amplitude controller
	 */
	float ease(float level, float target)
	{
		return level < target ? Math.min(target,
			level + this.instance.volRate) : level > target ? Math.max(target,
			level - this.instance.volRate) : level;
	}
	
	/**
	 * Key-off processing
	 */
	void off()
	{
		this.playing = false;
		for (MA3Operator op : this.operators)
		{
			if (op.envStage == MA3SamplerProvider.ENV_DONE || op.xof)
				continue;
			op.envRate = op.rr;
			op.envStage = MA3SamplerProvider.ENV_RELEASE;
		}
	}
	
	/**
	 * An envelope has finished
	 */
	void onEnvelopeDone()
	{
		this.envDone = true;
		
		// Test all relevant operators
		int bits = MA3SamplerProvider.ENV_FLAGS[this.algorithm.alg];
		for (int x = 0; x < this.operators.length; x++, bits >>= 1)
		{
			if ((bits & 1) != 0)
				this.envDone =
					this.envDone && this.operators[x].envStage == MA3SamplerProvider.ENV_DONE;
		}
		
		// If all relevant operators are done, shut off the note
		if (this.envDone)
			this.playing = false;
	}
	
	/**
	 * Frequency has changed
	 */
	void onFrequency(double bend)
	{
		
		// Wave notes don't use oscillators
		if (this.algorithm.isWave)
			return;
		
		// Compute BLOCK and F_NUMBER
		double freq =
			this.algorithm.isDrum ? this.freqBase : this.freqBase * bend;
		this.block = Math.min(7, Math.max(0, (int)(Math.round(
			ExtraMath.log(freq / 440) * MA3SamplerProvider.MAGIC_B) + 57) / 12));
		this.f_number = Math.min(1023, Math.max(0, (int)Math.round(
			freq * (1 << 20 - this.block) * MA3SamplerProvider.MAGIC_F)));
		
		// Notify operators
		for (MA3Operator op : this.operators)
			op.onFrequency();
	}
	
	/**
	 * Master volume has changed
	 */
	void onVolume()
	{
		this.volLeftOut =
			this.volBase * this.algorithm.volLeft * this.channel.volLeftOut;
		this.volRightOut =
			this.volBase * this.algorithm.volRight * this.channel.volRightOut;
	}
	
	/**
	 * Render the next input sample
	 */
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
		float sample = !this.algorithm.isWave ? this.sampleFM() :
			this.operators[0].sample(0, false) / 32768.0f;
		this.instance.smpNext[0] += sample * this.ampLeft;
		this.instance.smpNext[1] += sample * this.ampRight;
		
		// Adjust stereo levels
		this.ampLeft = this.ease(this.ampLeft, tgtLeft);
		this.ampRight = this.ease(this.ampRight, tgtRight);
		
		// Indicate whether the note has finished generating output
		return !this.playing && this.ampLeft == 0 && this.ampRight == 0;
	}
	
	/**
	 * Generate an FM sample
	 */
	float sampleFM()
	{
		int out1, out2, out3, out4;
		int ret = 0;
		switch (this.algorithm.alg)
		{
			case 0:
				out1 = this.operators[0].sample(0, true);
				out2 = this.operators[1].sample(out1, false);
				ret = out2;
				break;
			case 1:
				out1 = this.operators[0].sample(0, true);
				out2 = this.operators[1].sample(0, false);
				ret = out1 + out2;
				break;
			case 2:
				out1 = this.operators[0].sample(0, true);
				out2 = this.operators[1].sample(0, false);
				out3 = this.operators[2].sample(0, true);
				out4 = this.operators[3].sample(0, false);
				ret = out1 + out2 + out3 + out4;
				break;
			case 3:
				out1 = this.operators[0].sample(0, true);
				out2 = this.operators[1].sample(0, false);
				out3 = this.operators[2].sample(out2, false);
				out4 = this.operators[3].sample(out1 + out3, false);
				ret = out4;
				break;
			case 4:
				out1 = this.operators[0].sample(0, true);
				out2 = this.operators[1].sample(out1, false);
				out3 = this.operators[2].sample(out2, false);
				out4 = this.operators[3].sample(out3, false);
				ret = out4;
				break;
			case 5:
				out1 = this.operators[0].sample(0, true);
				out2 = this.operators[1].sample(out1, false);
				out3 = this.operators[2].sample(0, true);
				out4 = this.operators[3].sample(out3, false);
				ret = out2 + out4;
				break;
			case 6:
				out1 = this.operators[0].sample(0, true);
				out2 = this.operators[1].sample(0, false);
				out3 = this.operators[2].sample(out2, false);
				out4 = this.operators[3].sample(out3, false);
				ret = out1 + out4;
				break;
			case 7:
				out1 = this.operators[0].sample(0, true);
				out2 = this.operators[1].sample(0, false);
				out3 = this.operators[2].sample(out2, false);
				out4 = this.operators[3].sample(0, false);
				ret = out1 + out3 + out4;
				break;
		}
		//  Twice the max sample value
		return ret / 8170.0f;
	}
	
	/**
	 * Terminate playback
	 */
	void stop()
	{
		this.envDone = true;
		this.playing = false;
		this.volBase = 0.0f;
		for (MA3Operator op : this.operators)
		{
			op.envLevel = 511;
			op.envStage = MA3SamplerProvider.ENV_DONE;
		}
	}
	
}
