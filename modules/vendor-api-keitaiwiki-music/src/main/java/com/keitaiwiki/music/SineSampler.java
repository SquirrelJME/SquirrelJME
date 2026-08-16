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
	final __SineChannel__[] _channels;
	
	/** Global pitch bend. */
	@SquirrelJMEVendorApi
	float _masterTune;
	
	/** Global volume. */
	@SquirrelJMEVendorApi
	float _masterVolume;
	
	/** Output sampling rate. */
	@SquirrelJMEVendorApi
	final float _sampleRate;
	
	/** Automatic volume adjustment rate. */
	@SquirrelJMEVendorApi
	final float _volRate;
	

	/**
	 * Creates a new Sine Wave Sampler that outputs audio samples.
	 *
	 * @param __sampleRate The audio sample rate.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public SineSampler(float __sampleRate)
	{
		this._channels = new __SineChannel__[16];
		this._sampleRate = __sampleRate;
		this._volRate = 1 / (__sampleRate * 0.1f);
		
		// Channels
		for (int x = 0; x < this._channels.length; x++)
		{
			__SineChannel__ chan = this._channels[x] = new __SineChannel__();
			chan._index = x;
			//  C-2 .. G8
			chan._notesOn = new __SineNote__[127];
			chan._notesOut = new ArrayList<>();
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
		__SineChannel__[] channels = this._channels;
		if (__channel < 0 || __channel >= channels.length ||
			SineSamplerProvider.A4 + __key < 0 ||
			SineSamplerProvider.A4 + __key >= 128)
			return;
		
		__SineChannel__ chan = channels[__channel];
		__SineNote__ note = chan._notesOn[SineSamplerProvider.A4 + __key];
		if (note != null)
		{
			note._playing = false;
			note._volBase = 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public boolean isFinished()
	{
		for (__SineChannel__ chan : this._channels)
		{
			if (chan._notesOut.size() != 0)
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
		
		if (__channel < 0 || __channel >= this._channels.length ||
			SineSamplerProvider.A4 + __key < 0 ||
			SineSamplerProvider.A4 + __key >= 128)
			return;
		
		// Working variables
		__SineChannel__ chan = this._channels[__channel];
		__SineNote__ note = chan._notesOn[SineSamplerProvider.A4 + __key];
		
		// No note is currently playing on the specified key
		if (note == null)
		{
			note = chan._notesOn[SineSamplerProvider.A4 + __key] =
				new __SineNote__();
			chan._notesOut.add(note);
			note._channel = chan;
			note._volLeftLevel = 0.0f;
			note._volRightLevel = 0.0f;
			note._wavPhase = 0.0f;
		}
		
		// Configure fields
		note._freqBase = (float)(440 * ExtraMath.pow(2, __key / 12.0));
		note._playing = true;
		note._volBase = __velocity;
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
		this._masterTune = (float)ExtraMath.pow(2, __semitones);
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
		this._masterVolume = __volume;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void panpot(int __channel, float __panpot)
	{
		__SineChannel__[] channels = this._channels;
		if (Float.isInfinite(__panpot) || __panpot < -1.0f || __panpot > 1.0f)
			throw new IllegalArgumentException("Invalid panpot.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__SineChannel__ chan = channels[__channel];
		chan._volPanning = (__panpot + 1) / 2;
		chan._volLeft = (1.0f - chan._volPanning) * chan._volLevel;
		chan._volRight = chan._volPanning * chan._volLevel;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void pitchBend(int __channel, float __semitones)
	{
		__SineChannel__[] channels = this._channels;
		if (Float.isInfinite(__semitones))
			throw new IllegalArgumentException("Invalid semitones.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__SineChannel__ chan = channels[__channel];
		chan._bendBase = __semitones;
		chan._bendOut = (float)ExtraMath.pow(2,
			chan._bendBase * chan._bendRange);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void pitchBendRange(int __channel, float __range)
	{
		__SineChannel__[] channels = this._channels;
		if (Float.isInfinite(__range) || __range < 0.0f)
			throw new IllegalArgumentException("Invalid range.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__SineChannel__ chan = channels[__channel];
		chan._bendRange = __range;
		chan._bendOut = (float)ExtraMath.pow(2,
			chan._bendBase * chan._bendRange);
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
		for (__SineChannel__ chan : this._channels)
			this.__chanRender(chan, __samples, __offset, __frames, __left,
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
		for (__SineChannel__ chan : this._channels)
		{
			Arrays.fill(chan._notesOn, null);
			for (__SineNote__ note : chan._notesOut)
			{
				note._playing = false;
				note._volBase = 0.0f;
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
		this._masterTune = 1.0f;
		this._masterVolume = 1.0f;
		
		// Channels
		for (__SineChannel__ chan : this._channels)
		{
			chan._bendBase = 0.0f;
			chan._bendOut = 1.0f;
			chan._bendRange = 2;
			chan._volLevel = 1.0f;
			chan._volPanning = 0.5f;
			chan._volLeft = 0.5f;
			chan._volRight = 0.5f;
			
			// Stop playing all notes
			Arrays.fill(chan._notesOn, null);
			for (__SineNote__ note : chan._notesOut)
			{
				note._playing = false;
				note._volBase = 0.0f;
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
		return this._sampleRate;
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
		__SineChannel__[] channels = this._channels;
		if (Float.isInfinite(__volume) || __volume < 0.0f)
			throw new IllegalArgumentException("Invalid volume.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__SineChannel__ chan = channels[__channel];
		chan._volLevel = __volume;
		chan._volLeft = (1.0f - chan._volPanning) * chan._volLevel;
		chan._volRight = chan._volPanning * chan._volLevel;
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
	void __chanRender(@NotNull __SineChannel__ __chan, @NotNull float[] __samples,
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
		float bend = this._masterTune * __chan._bendOut;
		__left *= __chan._volLeft;
		__right *= __chan._volRight;
		
		// Process all notes
		for (int x = 0; x < __chan._notesOut.size(); x++)
		{
			if (this.__noteRender(__chan._notesOut.get(x), __samples, __offset,
				__frames, __chan._volLeft * __left, __chan._volRight * __right,
				bend))
				__chan._notesOut.remove(x--);
		}
		
		// Disassociate inactive notes
		for (int x = 0; x < __chan._notesOn.length; x++)
		{
			__SineNote__ note = __chan._notesOn[x];
			if (note != null && !note._playing)
				__chan._notesOn[x] = null;
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
	float __ease(float __level, float __target)
	{
		return __level < __target ?
			Math.min(__target, __level + this._volRate) :
			__level > __target ?
			Math.max(__target, __level - this._volRate) : __level;
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
	boolean __noteRender(@NotNull __SineNote__ __note, @NotNull float[] __samples,
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
		float freq = __note._freqBase * __bend;
		float advance = freq / this._sampleRate;
		
		// Compute desired left and right volume levels
		__note._volLeftTarget = __note._volBase * __left;
		__note._volRightTarget = __note._volBase * __right;
		
		// Process all samples
		for (int x = 0; x < __frames; x++)
		{
			
			// Generate one sample
			float sample = this.__sample(__note, advance);
			__samples[__offset++] += sample * __note._volLeftLevel;
			__samples[__offset++] += sample * __note._volRightLevel;
			
			// Adjust stereo levels
			__note._volLeftLevel = this.__ease(__note._volLeftLevel,
				__note._volLeftTarget);
			__note._volRightLevel = this.__ease(__note._volRightLevel,
				__note._volRightTarget);
			
			// Note has finished
			if (!__note._playing && __note._volLeftLevel == 0 &&
				__note._volRightLevel == 0)
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
	float __sample(@NotNull __SineNote__ __note, float __advance)
		throws NullPointerException
	{
		if (__note == null)
			throw new NullPointerException("NARG");

		float ret = (float)Math.sin(__note._wavPhase * Math.PI * 2);
		__note._wavPhase = (__note._wavPhase + __advance) % 1;
		return ret;
	}
}
