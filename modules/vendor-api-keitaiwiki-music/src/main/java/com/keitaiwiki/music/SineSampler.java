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
import java.util.ArrayList;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Generates Sine Wave samples.
 *
 * @see Sampler
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
public class SineSampler
	extends AbstractSampler
	implements Sampler
{
	/** Channel states. */
	@SquirrelJMEVendorApi
	final __SineChannel__[] channels;
	
	/** Global pitch bend. */
	@SquirrelJMEVendorApi
	float masterTune;
	
	/** Global volume. */
	@SquirrelJMEVendorApi
	float masterVolume;
	
	/** Output sampling rate. */
	@SquirrelJMEVendorApi
	final float sampleRate;
	
	/** Automatic volume adjustment rate. */
	@SquirrelJMEVendorApi
	final float volRate;
	

	/**
	 * Creates a new Sine Wave Sampler that outputs audio samples.
	 *
	 * @param __sampleRate The audio sample rate.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public SineSampler(float __sampleRate)
	{
		this.channels = new __SineChannel__[16];
		this.sampleRate = __sampleRate;
		this.volRate = 1 / (__sampleRate * 0.1f);
		
		// Channels
		for (int x = 0; x < this.channels.length; x++)
		{
			__SineChannel__ chan = this.channels[x] = new __SineChannel__();
			chan.index = x;
			//  C-2 .. G8
			chan.notesOn = new __SineNote__[127];
			chan.notesOut = new ArrayList<>();
		}
		
		// Reset all state
		this.reset();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void bankChange(int __channel, int __bank)
	{
		// Not implementing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void drumEnable(int __channel, boolean __enable)
	{
		// Not implementing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void keyOff(int __channel, int __key)
	{
		__SineChannel__[] channels = this.channels;
		if (__channel < 0 || __channel >= channels.length ||
			SineSamplerProvider.A4 + __key < 0 ||
			SineSamplerProvider.A4 + __key >= 128)
			return;
		
		__SineChannel__ chan = channels[__channel];
		__SineNote__ note = chan.notesOn[SineSamplerProvider.A4 + __key];
		if (note != null)
		{
			note.playing = false;
			note.volBase = 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public boolean isFinished()
	{
		for (__SineChannel__ chan : this.channels)
		{
			if (chan.notesOut.size() != 0)
				return false;
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void keyOn(int __channel, int __key, float __velocity)
	{
		
		// Error checking
		if (Float.isInfinite(__velocity) || __velocity < 0.0f)
			throw new IllegalArgumentException("Invalid velocity.");
		
		if (__channel < 0 || __channel >= this.channels.length ||
			SineSamplerProvider.A4 + __key < 0 ||
			SineSamplerProvider.A4 + __key >= 128)
			return;
		
		// Working variables
		__SineChannel__ chan = this.channels[__channel];
		__SineNote__ note = chan.notesOn[SineSamplerProvider.A4 + __key];
		
		// No note is currently playing on the specified key
		if (note == null)
		{
			note = chan.notesOn[SineSamplerProvider.A4 + __key] =
				new __SineNote__();
			chan.notesOut.add(note);
			note.channel = chan;
			note.volLeftLevel = 0.0f;
			note.volRightLevel = 0.0f;
			note.wavPhase = 0.0f;
		}
		
		// Configure fields
		note.freqBase = (float)(440 * ExtraMath.pow(2, __key / 12.0));
		note.playing = true;
		note.volBase = __velocity;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void masterTune(float __semitones)
	{
		if (Float.isInfinite(__semitones))
			throw new IllegalArgumentException("Invalid semitones.");
		this.masterTune = (float)ExtraMath.pow(2, __semitones);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void masterVolume(float __volume)
	{
		if (Float.isInfinite(__volume) || __volume < 0.0f)
			throw new IllegalArgumentException("Invalid volume.");
		this.masterVolume = __volume;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void panpot(int __channel, float __panpot)
	{
		__SineChannel__[] channels = this.channels;
		if (Float.isInfinite(__panpot) || __panpot < -1.0f || __panpot > 1.0f)
			throw new IllegalArgumentException("Invalid panpot.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__SineChannel__ chan = channels[__channel];
		chan.volPanning = (__panpot + 1) / 2;
		chan.volLeft = (1.0f - chan.volPanning) * chan.volLevel;
		chan.volRight = chan.volPanning * chan.volLevel;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void pitchBend(int __channel, float __semitones)
	{
		__SineChannel__[] channels = this.channels;
		if (Float.isInfinite(__semitones))
			throw new IllegalArgumentException("Invalid semitones.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__SineChannel__ chan = channels[__channel];
		chan.bendBase = __semitones;
		chan.bendOut = (float)ExtraMath.pow(2,
			chan.bendBase * chan.bendRange);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void pitchBendRange(int __channel, float __range)
	{
		__SineChannel__[] channels = this.channels;
		if (Float.isInfinite(__range) || __range < 0.0f)
			throw new IllegalArgumentException("Invalid range.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__SineChannel__ chan = channels[__channel];
		chan.bendRange = __range;
		chan.bendOut = (float)ExtraMath.pow(2,
			chan.bendBase * chan.bendRange);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void programChange(int __channel, int __program)
	{
		// Not implementing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void render(float[] __samples, int __offset, int __frames)
	{
		this.render(__samples, __offset, __frames,
			1.0f, 1.0f, true, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void render(float[] __samples, int __offset, int __frames,
		float __amplitude)
	{
		this.render(__samples, __offset, __frames, __amplitude, __amplitude,
			true, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void render(float[] __samples, int __offset, int __frames,
		float __left, float __right)
	{
		this.render(__samples, __offset, __frames,
			__left, __right, true, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	public void render(float[] __samples, int __offset, int __frames,
		float __left, float __right, boolean __erase, boolean __clamp)
	{
		// Error checking
		if (__samples == null)
			throw new NullPointerException("NARG");
		
		if (__frames < 0)
			throw new IllegalArgumentException("NEGV");
		
		if (__offset < 0 || __offset + __frames * 2 > __samples.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		if (Float.isInfinite(__left) || __left < 0.0f)
			throw new IllegalArgumentException("Invalid left.");
		if (Float.isInfinite(__right) || __right < 0.0f)
			throw new IllegalArgumentException("Invalid right.");
		
		// Erase the output buffer
		if (__erase)
		{
			for (int x = __frames * 2 - 1; x >= 0; x--)
				__samples[__offset + x] = 0.0f;
		}
		
		// Render output samples
		for (__SineChannel__ chan : this.channels)
			this.chanRender(chan, __samples, __offset, __frames, __left,
			__right);
		
		// Clamp the output buffer
		if (__clamp)
		{
			for (int x = __frames * 2 - 1; x >= 0; x--)
			{
				__samples[__offset + x] = Math.min(
					Math.max(__samples[__offset + x], -1.0f), 1.0f);
			}
		}
		
	}
	
	/**
	 * Terminates all currently active notes.
	 *
	 * @since 2025/05/05
	 */
	public void stopAll()
	{
		for (__SineChannel__ chan : this.channels)
		{
			Arrays.fill(chan.notesOn, null);
			for (__SineNote__ note : chan.notesOut)
			{
				note.playing = false;
				note.volBase = 0.0f;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void reset()
	{
		
		// Global fields
		this.masterTune = 1.0f;
		this.masterVolume = 1.0f;
		
		// Channels
		for (__SineChannel__ chan : this.channels)
		{
			chan.bendBase = 0.0f;
			chan.bendOut = 1.0f;
			chan.bendRange = 2;
			chan.volLevel = 1.0f;
			chan.volPanning = 0.5f;
			chan.volLeft = 0.5f;
			chan.volRight = 0.5f;
			
			// Stop playing all notes
			Arrays.fill(chan.notesOn, null);
			for (__SineNote__ note : chan.notesOut)
			{
				note.playing = false;
				note.volBase = 0.0f;
			}
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public float sampleRate()
	{
		return this.sampleRate;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void sysEx(byte[] __message)
	{
		// Not implementing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void volume(int __channel, float __volume)
	{
		__SineChannel__[] channels = this.channels;
		if (Float.isInfinite(__volume) || __volume < 0.0f)
			throw new IllegalArgumentException("Invalid volume.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__SineChannel__ chan = channels[__channel];
		chan.volLevel = __volume;
		chan.volLeft = (1.0f - chan.volPanning) * chan.volLevel;
		chan.volRight = chan.volPanning * chan.volLevel;
	}
	
	/**
	 * Renders audio samples generated from a Sine Wave channel.
	 *
	 * @param __chan The channel used to generate audio.
	 * @param __samples Output sample buffer.
	 * @param __offset Index in {@code __samples} of the first audio frame to
	 * output.
	 * @param __frames The number of audio frames to output.
	 * @param __left A multiplier that is applied to all left-stereo samples
	 * generated.
	 * @param __right A multiplier that is applied to all right-stereo
	 * samples generated.
	 * @throws NullPointerException if {@code __samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code __offset} is
	 * negative, or if {@code __offset + __frames * 2 > __samples.length}.
	 * @throws IllegalArgumentException if {@code frames} is negative,
	 * or if {@code __left} or {@code __right} is a non-number or is negative.
	 * @since 2025/05/05
	 */
	void chanRender(@NotNull __SineChannel__ __chan, @NotNull float[] __samples,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frames,
		float __left, float __right)
		throws ArrayIndexOutOfBoundsException, NullPointerException,
		IllegalArgumentException
	{
		if (__samples == null)
			throw new NullPointerException("NARG");
		
		if (__frames < 0)
			throw new IllegalArgumentException("NEGV");

		if (__offset < 0 || __offset + __frames * 2 > __samples.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		if (Float.isInfinite(__left) || __left < 0.0f)
			throw new IllegalArgumentException("Invalid left.");
		if (Float.isInfinite(__right) || __right < 0.0f)
			throw new IllegalArgumentException("Invalid right.");

		// Working variables
		float bend = this.masterTune * __chan.bendOut;
		__left *= __chan.volLeft;
		__right *= __chan.volRight;
		
		// Process all notes
		for (int x = 0; x < __chan.notesOut.size(); x++)
		{
			if (this.noteRender(__chan.notesOut.get(x), __samples, __offset,
				__frames, __chan.volLeft * __left, __chan.volRight * __right,
				bend))
				__chan.notesOut.remove(x--);
		}
		
		// Disassociate inactive notes
		for (int x = 0; x < __chan.notesOn.length; x++)
		{
			__SineNote__ note = __chan.notesOn[x];
			if (note != null && !note.playing)
				__chan.notesOn[x] = null;
		}
		
	}
	
	/**
	 * Applies an easing effect to the amplitude controller.
	 *
	 * @param __level The base volume level for the easing effect.
	 * @param __target The target volume level for the easing effect.
	 * @return The resulting volume level.
	 * @since 2025/05/05
	 */
	float ease(float __level, float __target)
	{
		return __level < __target ?
			Math.min(__target, __level + this.volRate) :
			__level > __target ?
			Math.max(__target, __level - this.volRate) : __level;
	}

	/**
	 * Renders audio samples generated from a Sine Wave note and pitch bend
	 * value.
	 *
	 * @param __note The note used to generate audio.
	 * @param __samples Output sample buffer.
	 * @param __offset Index in {@code __samples} of the first audio frame to
	 * output.
	 * @param __frames The number of audio frames to output.
	 * @param __left A multiplier that is applied to all left-stereo samples
	 * generated.
	 * @param __right A multiplier that is applied to all right-stereo
	 * samples generated.
	 * @param __bend The pitch bend to apply to generated samples.
	 * @throws NullPointerException if {@code __samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code __offset} is
	 * negative, or if {@code __offset + __frames * 2 > __samples.length}.
	 * @throws IllegalArgumentException if {@code frames} is negative,
	 * or if {@code __left} or {@code __right} is a non-number or is negative.
	 * @since 2025/05/05
	 */
	boolean noteRender(@NotNull __SineNote__ __note, @NotNull float[] __samples,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frames,
		float __left, float __right, float __bend)
		throws ArrayIndexOutOfBoundsException, NullPointerException,
		IllegalArgumentException
	{
		if (__samples == null)
			throw new NullPointerException("NARG");
		
		if (__frames < 0)
			throw new IllegalArgumentException("NEGV");

		if (__offset < 0 || __offset + __frames * 2 > __samples.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		if (Float.isInfinite(__left) || __left < 0.0f)
			throw new IllegalArgumentException("Invalid left.");
		if (Float.isInfinite(__right) || __right < 0.0f)
			throw new IllegalArgumentException("Invalid right.");

		// Working variables
		float freq = __note.freqBase * __bend;
		float advance = freq / this.sampleRate;
		
		// Compute desired left and right volume levels
		__note.volLeftTarget = __note.volBase * __left;
		__note.volRightTarget = __note.volBase * __right;
		
		// Process all samples
		for (int x = 0; x < __frames; x++)
		{
			
			// Generate one sample
			float sample = this.sample(__note, advance);
			__samples[__offset++] += sample * __note.volLeftLevel;
			__samples[__offset++] += sample * __note.volRightLevel;
			
			// Adjust stereo levels
			__note.volLeftLevel = this.ease(__note.volLeftLevel,
				__note.volLeftTarget);
			__note.volRightLevel = this.ease(__note.volRightLevel,
				__note.volRightTarget);
			
			// Note has finished
			if (!__note.playing && __note.volLeftLevel == 0 &&
				__note.volRightLevel == 0)
				return true;
		}
		
		// Note has not finished
		return false;
	}
	
	/**
	 * Produces a single audio sample based on a note.
	 *
	 * @param __note The note used to generate audio.
	 * @param __advance The amount of wave phase to increment.
	 * @throws NullPointerException if {@code __note} is {@code null}.
	 * @since 2025/05/05
	 */
	float sample(@NotNull __SineNote__ __note, float __advance)
		throws NullPointerException
	{
		if (__note == null)
			throw new NullPointerException("NARG");

		float ret = (float)Math.sin(__note.wavPhase * Math.PI * 2);
		__note.wavPhase = (__note.wavPhase + __advance) % 1;
		return ret;
	}
}
