// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.keitaiwiki.music;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.util.ExtraMath;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Generates live MA-3 samples.
 *
 * @see Sampler
 * @since 2025/05/05
 */
@SquirrelJMEVendorApi
public class MA3Sampler
	extends AbstractSampler
	implements Sampler
{
	/** The amount of channels the MA-3 has. */
	@SquirrelJMEVendorApi
	public static final byte NUM_CHANNELS =
		32;

	/** Channel states. */
	@SquirrelJMEVendorApi
	final __MA3Channel__[] _channels;
	
	/** The sample generator used for audio rendering. */
	@SquirrelJMEVendorApi
	private final MA3SamplerProvider _ma3;
	
	/** Output sampling rate. */
	@SquirrelJMEVendorApi
	final float _sampleRate;
	
	/** Next input sample. */
	@SquirrelJMEVendorApi
	final float[] _smpNext;
	
	/** Previous input sample. */
	@SquirrelJMEVendorApi
	final float[] _smpPrev;
	
	/** Number of input samples per output sample. */
	@SquirrelJMEVendorApi
	final float _smpWidth;
	
	/** Automatic volume adjustment rate. */
	@SquirrelJMEVendorApi
	final float _volRate;
	
	/** Registered wave drums. */
	@SquirrelJMEVendorApi
	final __MA3Algorithm__[] _wavDrums;
	
	/** Amplitude modulator phase. */
	@SquirrelJMEVendorApi
	int _amPhase;
	
	/** Global pitch bend. */
	@SquirrelJMEVendorApi
	float _bendOut;
	
	/** 2-operator instruments. */
	@SquirrelJMEVendorApi
	Map<Integer, __MA3Algorithm__> _fm2ops;
	
	/** 4-operator instruments. */
	@SquirrelJMEVendorApi
	Map<Integer, __MA3Algorithm__> _fm4pos;
	
	/** Position between input samples. */
	@SquirrelJMEVendorApi
	float _smpPosition;
	
	/** Frequency modulator phase. */
	@SquirrelJMEVendorApi
	int _vibPhase;
	
	/** Global attenuation. */
	@SquirrelJMEVendorApi
	float _volFade;
	
	/** Global volume. */
	@SquirrelJMEVendorApi
	float _volLevel;
	
	/** Effective global volume. */
	@SquirrelJMEVendorApi
	float _volOut;
	
	/** Wave RAM, decoded from ADPCM. */
	@SquirrelJMEVendorApi
	int[] _wavRam;
	
	/**
	 * Creates a new MA-3 Sampler that outputs audio samples.
	 *
	 * @param __ma3 The sample generator to use.
	 * @param __sampleRate The audio sample rate.
	 * @throws NullPointerException If {@code __ma3} is {@code null}.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public MA3Sampler(@NotNull MA3SamplerProvider __ma3, float __sampleRate)
		throws NullPointerException
	{
		if (__ma3 == null)
			throw new NullPointerException("NARG");

		this._channels = new __MA3Channel__[NUM_CHANNELS];
		this._fm2ops = new HashMap<>();
		this._fm4pos = new HashMap<>();
		this._sampleRate = __sampleRate;
		this._smpNext = new float[2];
		this._smpPrev = new float[2];
		this._smpWidth = MA3SamplerProvider.SAMPLE_RATE / __sampleRate;
		this._volRate = 1 / (__sampleRate * 0.01f);
		this._wavDrums = new __MA3Algorithm__[128];
		
		// Channels
		for (int x = 0; x < this._channels.length; x++)
			this._channels[x] = new __MA3Channel__(this, x);
		
		// Initialize state
		this.reset();
		this._ma3 = __ma3;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void bankChange(
		@Range(from = 0, to = MA3Sampler.NUM_CHANNELS) int __channel,
		@Range(from = 0, to = 255) int __bank)
	{
		__MA3Channel__[] channels = this._channels;
		if (__channel < 0 || __channel >= channels.length || __bank < 0 ||
			__bank > 255)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan._prgBank = __bank;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void drumEnable(
		@Range(from = 0, to = MA3Sampler.NUM_CHANNELS) int __channel,
		boolean __enable)
	{
		__MA3Channel__[] channels = this._channels;
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan._isDrum = __enable;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public boolean isFinished()
	{
		for (__MA3Channel__ chan : this._channels)
			if (chan._notesOut.size() != 0)
				return false;
		
		return true;
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void keyOff(
		@Range(from = 0, to = MA3Sampler.NUM_CHANNELS) int __channel,
		int __key)
	{
		__MA3Channel__[] channels = this._channels;
		if (__channel < 0 || __channel >= channels.length ||
			MA3SamplerProvider.A4 + __key < 0 ||
			MA3SamplerProvider.A4 + __key >= 128)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		__MA3Note__ note = chan._notesOn[MA3SamplerProvider.A4 + __key];
		if (note != null)
			note.__off();
		
		chan._notesOn[MA3SamplerProvider.A4 + __key] = null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void keyOn(
		@Range(from = 0, to = MA3Sampler.NUM_CHANNELS) int __channel,
		int __key,
		float __velocity)
	{
		// Error checking
		__MA3Channel__[] channels = this._channels;
		if (Float.isInfinite(__velocity) || __velocity < 0.0f)
			throw new IllegalArgumentException("Invalid velocity.");
		
		if (__channel < 0 || __channel >= channels.length ||
			MA3SamplerProvider.A4 + __key < 0 ||
			MA3SamplerProvider.A4 + __key >= 128)
			return;
		
		// Working variables
		__MA3Algorithm__ algorithm = null;
		__MA3Channel__ chan = channels[__channel];
		float freqBase = 0;
		boolean isWave = false;
		__MA3Note__ note = chan._notesOn[MA3SamplerProvider.A4 + __key];
		
		// FM instrument algorithm
		if (!chan._isDrum)
		{
			algorithm = this.__getFMInstrument(chan._prgBank, chan._prgProgram);
			freqBase = (float)(440 * ExtraMath.pow(2, __key / 12.0));
		}
		
		// Drum algorithm
		else
		{
			if (this._ma3._prgWaveDrumType != MA3SamplerProvider.WAVE_DRUM_NONE)
			{
				algorithm = this.__getDrumWave(__key);
				isWave = algorithm != null;
			}
			
			if (algorithm == null)
				algorithm = this.__getDrumFM(__key);
			
			if (algorithm == null)
				return;
			
			freqBase = algorithm._freqBase;
			isWave = algorithm._isWave;
		}
		
		
		// Stop the previous note if necessary
		if (note != null && (chan._isDrum || note._algorithm != algorithm))
		{
			this.keyOff(__channel, __key);
			note = null;
		}
		
		// Spawn a new note if necessary
		if (note == null)
		{
			// Create the new note
			note = new __MA3Note__(chan, __key, algorithm);
			chan._notesOn[MA3SamplerProvider.A4 + __key] = note;
			chan._notesOut.add(note);
		}
		
		// Configure fields
		note._volBase = __velocity;
		note.__onVolume();
		if (!isWave)
		{
			note._freqBase = freqBase;
			note.__onFrequency(this._bendOut * chan._bendOut);
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void masterTune(float __semitones)
		throws IllegalArgumentException
	{
		if (Float.isInfinite(__semitones))
			throw new IllegalArgumentException("Invalid semitones value.");
		
		this._bendOut = (float)ExtraMath.pow(2, __semitones);
		for (__MA3Channel__ chan : this._channels)
			chan.__onFrequency();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void masterVolume(float __volume)
		throws IllegalArgumentException
	{
		if (Float.isInfinite(__volume) || __volume < 0.0f)
			throw new IllegalArgumentException("Invalid volume value.");

		this._volLevel = __volume;
		this.__onVolume();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void panpot(
		@Range(from = 0, to = MA3Sampler.NUM_CHANNELS) int __channel,
		float __panpot)
		throws IllegalArgumentException
	{
		__MA3Channel__[] channels = this._channels;
		if (Float.isInfinite(__panpot) || __panpot < -1.0f || __panpot > 1.0f)
			throw new IllegalArgumentException("Invalid panpot.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan._volPanning = (__panpot + 1) / 2;
		chan._volLeft = (1.0f - chan._volPanning) * chan._volLevel;
		chan._volRight = chan._volPanning * chan._volLevel;
		chan.__onVolume();
	}

	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void pitchBend(
		@Range(from = 0, to = MA3Sampler.NUM_CHANNELS) int __channel,
		float __semitones)
		throws IllegalArgumentException
	{
		__MA3Channel__[] channels = this._channels;
		if (Float.isInfinite(__semitones))
			throw new IllegalArgumentException("Invalid semitones.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan._bendBase = __semitones;
		chan._bendOut = (float)ExtraMath.pow(2,
			chan._bendBase * chan._bendRange);
		chan.__onFrequency();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void pitchBendRange(
		@Range(from = 0, to = MA3Sampler.NUM_CHANNELS) int __channel,
		float __range)
		throws IllegalArgumentException
	{
		__MA3Channel__[] channels = this._channels;
		if (Float.isInfinite(__range) || __range < 0.0f)
			throw new IllegalArgumentException("Invalid pitch bend range.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan._bendRange = __range;
		chan._bendOut = (float)ExtraMath.pow(2,
			chan._bendBase * chan._bendRange);
		chan.__onFrequency();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void programChange(
		@Range(from = 0, to = MA3Sampler.NUM_CHANNELS) int __channel,
		@Range(from = 0, to = 255) int __program)
	{
		__MA3Channel__[] channels = this._channels;
		if (__channel < 0 || __channel >= channels.length || __program < 0 ||
			__program > 255)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan._prgProgram = __program;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void render(float[] __samples, int __offset, int __frames)
	{
		this.render(__samples, __offset, __frames, 1.0f, 1.0f,
			true, true);
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
		this.render(__samples, __offset, __frames, __left, __right,
			true, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void render(float[] __samples, int __offset, int __frames,
		float __left, float __right, boolean __erase, boolean __clamp)
	{
		
		// Error checking
		if (__samples == null)
			throw new NullPointerException(
				"A sample buffer is required" + ".");
		
		if (__frames < 0)
			throw new IllegalArgumentException("Invalid frames.");
		
		if (__offset < 0 || __offset + __frames * 2 > __samples.length)
		{
			throw new ArrayIndexOutOfBoundsException(
				"Invalid range in sample buffer.");
		}
		
		if (Float.isInfinite(__left) || __left < 0.0f)
			throw new IllegalArgumentException("Invalid left amplitude.");
		
		if (Float.isInfinite(__right) || __right < 0.0f)
			throw new IllegalArgumentException(
				"Invalid right amplitude" + ".");
		
		// Used in the loop
		float[] smpPrev = this._smpPrev;
		float[] smpNext = this._smpNext;
		float smpWidth = this._smpWidth;
		
		// Modified in the loop
		float smpPosition = this._smpPosition;
		
		try
		{
			// Process all output frames
			float[] frame = new float[2];
			for (int x = 0; x < __frames; x++)
			{
				float l = smpPosition;
				float r = l + smpWidth;
				
				//  Scratch
				float a, b;
				
				// Edge case: need the next input sample
				if (l == 0.0f)
					this.__sample();
				
				// Left and right are in the same input sample
				if (l < 1.0f)
				{
					a = (l + r) / 2;
					frame[0] =
						smpPrev[0] + (smpNext[0] - smpPrev[0]) * a;
					frame[1] =
						smpPrev[1] + (smpNext[1] - smpPrev[1]) * a;
				}
				
				// Left and right span input samples
				else
				{
					// First partial
					a = (l + 1.0f) / 2;
					b = 1.0f - l;
					frame[0] =
						(smpPrev[0] + (smpNext[0] - smpPrev[0]) * a) * b;
					frame[1] =
						(smpPrev[1] + (smpNext[1] - smpPrev[1]) * a) * b;
					
					// All wholes
					for (int y = (int)Math.floor(r) - 1; y > 0; y--)
					{
						smpPrev[0] = smpNext[0];
						smpPrev[1] = smpNext[1];
						this.__sample();
						frame[0] += (smpPrev[0] + smpNext[0]) / 2;
						frame[1] += (smpPrev[1] + smpNext[1]) / 2;
					}
					
					// Record the latest input sample
					smpPrev[0] = smpNext[0];
					smpPrev[1] = smpNext[1];
					
					// Last partial
					r %= 1.0f;
					if (r != 0.0f)
					{
						this.__sample();
						a = r / 2;
						frame[0] += (smpPrev[0] + 
							(smpNext[0] - smpPrev[0]) * a) * r;
						frame[1] += (smpPrev[1] + 
							(smpNext[1] - smpPrev[1]) * a) * r;
					}
					
					// Take the weighted average of all spanned input samples
					frame[0] /= smpWidth;
					frame[1] /= smpWidth;
				}
				
				// Output scaling
				frame[0] *= __left;
				frame[1] *= __right;
				
				// Incorporate the existing contents of the buffer
				if (!__erase)
				{
					frame[0] += __samples[__offset];
					frame[1] += __samples[__offset + 1];
				}
				
				// Constrain the output
				if (__clamp)
				{
					frame[0] = Math.min(Math.max(frame[0], -1.0f), 1.0f);
					frame[1] = Math.min(Math.max(frame[1], -1.0f), 1.0f);
				}
				
				// Output the frame
				__samples[__offset++] = frame[0];
				__samples[__offset++] = frame[1];
				
				// Advance to the next output sample
				smpPosition = r;
			}
		}
		finally
		{
			this._smpPosition = smpPosition;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void reset()
	{
		this._amPhase = 0;
		this._bendOut = 1.0f;
		this._smpPosition = 0.0f;
		this._smpPrev[0] = this._smpPrev[1] = 0.0f;
		this._vibPhase = 0;
		this._volFade = 0.0f;
		this._volLevel = 1.0f;
		this._volOut = 1.0f;
		this._wavRam = null;
		this._fm2ops.clear();
		this._fm4pos.clear();
		for (__MA3Channel__ chan : this._channels)
			chan.__reset();
		Arrays.fill(this._wavDrums, null);
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
	 * Terminates all currently active notes.
	 *
	 * @since 2025/05/05
	 */
	public void stopAll()
	{
		for (__MA3Channel__ chan : this._channels)
		{
			Arrays.fill(chan._notesOn, null);
			for (__MA3Note__ note : chan._notesOut)
				note.__stop();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void sysEx(byte[] __message)
	{
		if (__message == null)
			throw new NullPointerException("NARG");

		// Error checking
		if (__message == null || __message.length < 4 ||
			__message[0] != (byte)0x11 || __message[1] != (byte)0x01 ||
			(__message[2] & 0xF0) != 0xF0)
			return;
		
		// Processing by sub-message type
		switch (__message[3] & 0xFF)
		{
			case 0x00:// Seen in Smwemu_N.dll at 10028975
				break;
			case 0x01:// Seen in Smwemu_N.dll at 1002899D
				break;
			case 0x02:// Seen in Smwemu_N.dll at 100289B4
				break;
			case 0x03: // Specify the global fade
				this.__setMasterFade(__message);
				break;
			case 0x04:
				this.__setFMAlgorithms(__message);
				break;
			case 0x05: // Register wave drum algorithms
				this.__setWaveDrums(__message);
				this.__stopWaveDrums();
				break;
			case 0x06: // Supply wave drum samples
				this._wavRam = MA3SamplerProvider.__decodeAICA(__message, 4,
					__message.length - 4);
				this.__stopWaveDrums();
				break;
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void volume(int __channel, float __volume)
	{
		__MA3Channel__[] channels = this._channels;
		if (Float.isInfinite(__volume) || __volume < 0.0f)
			throw new IllegalArgumentException("Invalid volume value.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan._volLevel = __volume;
		chan._volLeft = (1.0f - chan._volPanning) * chan._volLevel;
		chan._volRight = chan._volPanning * chan._volLevel;
		chan.__onVolume();
	}
	
	/**
	 * Retrieves an algorithm for playing an FM drum note.
	 *
	 * @param __key The key for which an algorithm must be retrieved.
	 * @return The algorithm to be used for playing FM drum notes, or
	 * {@code null} if the requested key does not match to a valid FM drum
	 * algorithm.
	 * @since 2025/05/05
	 */
	__MA3Algorithm__ __getDrumFM(int __key)
	{
		__MA3Algorithm__[] algDrums = this._ma3._algDrums;
		
		// Transform wave drum keys into FM drum keys
		if (__key < 0)
			__key += 35;
		
		// Error checking
		if (__key < 0 || __key >= algDrums.length)
			return null;
		
		// Select the preset algorithm
		return algDrums[__key];
	}
	
	/** Specify FM algorithms. */

	/**
	 * Retrieves an algorithm for playing a wave drum note.
	 *
	 * @param __key The key for which an algorithm must be retrieved.
	 * @return The algorithm to be used for playing wave drum notes, or
	 * {@code null} if the requested key does not match to a valid wave drum
	 * algorithm.
	 * @since 2025/05/05
	 */
	__MA3Algorithm__ __getDrumWave(int __key)
	{
		// Error checking
		if (__key < -24)
			return null;
		
		// Select the registered wave algorithm, if available
		__MA3Algorithm__[] algs = this._ma3._algWaveDrums;
		__MA3Algorithm__ ret = null;
		if (__key < 0)
		{
			algs = this._wavDrums;
			__key += 24;
		}

		if (__key >= 0 && __key < algs.length)
			ret = algs[__key];
		
		// Error checking
		int[] _wavRam = this._wavRam;
		if (ret != null && !ret._rm &&
			(_wavRam == null || ret._ep >= _wavRam.length))
			ret = null;
		
		return ret;
	}
	
	/** Retrieve an algorithm for playing an FM instrument. */
	__MA3Algorithm__ __getFMInstrument(int __bank, int __program)
	{
		int hashKey = __bank << 8 | __program;
		__MA3Algorithm__ ret = null;
		
		MA3SamplerProvider ma3 = this._ma3;
		Map<Integer, __MA3Algorithm__> _fm4pos = this._fm4pos;
		
		// Running in 4-algorithm mode
		if (ma3._prgInstrumentType == MA3SamplerProvider.FM_MA3_4OP)
			ret = _fm4pos.get(hashKey);
		
		// Fallback to 2-algorithm mode
		if (ret == null)
			ret = this._fm2ops.get(hashKey);
		
		// Fallback to preset
		if (ret == null)
		{
			ret = ma3._algInstruments[__bank < 2 ? 0 : // Apparent behavior
				(__bank & 1) << 6 | __program & 0x3F];
		}
		
		return ret;
	}
	
	/**
	 * This function is called whenever there must be a volume change in this
	 * sampler.
	 *
	 * @since 2025/05/05
	 */
	void __onVolume()
	{
		this._volOut = (1.0f - this._volFade) * this._volLevel;
		for (__MA3Channel__ chan : this._channels)
			chan.__onVolume();
	}
	
	/**
	 * Produces a single audio sample.
	 *
	 * @since 2025/05/05
	 */
	void __sample()
	{
		this._smpNext[0] = this._smpNext[1] = 0.0f;
		for (__MA3Channel__ chan : this._channels)
			chan.__render();
		
		this._amPhase = (this._amPhase + 1) % 0x34000;
		this._vibPhase++;
	}
	
	/**
	 * Specifies a set of FM algorithms from the given data array.
	 *
	 * @param __message The data array containing the FM algorithms.
	 * @throws NullPointerException if {@code __message} is {@code null};
	 * @since 2025/05/05
	 */
	void __setFMAlgorithms(@NotNull byte[] __message)
		throws NullPointerException
	{
		if (__message == null)
			throw new NullPointerException("NARG");

		Map<Integer, __MA3Algorithm__> _fm2ops = this._fm2ops;
		Map<Integer, __MA3Algorithm__> _fm4pos = this._fm4pos;
		
		// Process all algorithms in the message
		for (int offset = 4; offset < __message.length; )
		{
			
			// Algorithm type: 1=two-operator, 2=four-operator
			int type = __message[offset] & 0xFF;
			if (type != 1 && type != 2)
				break;
			
			// Error checking
			int size = type == 1 ? 20 : 34;
			if (offset + size > __message.length)
				break;
			
			// Decode the algorithm
			__MA3Algorithm__ algorithm;
			try
			{
				algorithm = new __MA3Algorithm__(offset, __message);
			}
			catch (Exception e)
			{
				break;
			}
			
			// Error checking
			if (type == 1 && algorithm._operators.length == 4)
				continue;
			
			// Register the algorithm
			(type == 1 ? _fm2ops : _fm4pos).put(
				(__message[offset + 1] & 0xFF) << 8 | // Bank
					__message[offset + 2] & 0xFF,     // Program
				algorithm);
			
			// Advance to the next algorithm
			offset += size;
		}
	}
	
	/**
	 * Specifies a global volume fade level.
	 *
	 * @param __message A message from which the fade level must be extracted.
	 * @throws NullPointerException If {@code __message} is {@code null}.
	 * @since 2025/05/05
	 */
	void __setMasterFade(@NotNull byte[] __message)
		throws NullPointerException
	{
		if (__message == null)
			throw new NullPointerException("NARG");

		if (__message.length < 5)
			return;
		
		this._volFade = (__message[4] & 0x7F) / 127.0f;
		this.__onVolume();
	}
	
	/**
	 * Decodes and registers wave drum definitions from a byte array.
	 *
	 * @param __message A message containing the wave drum definitions.
	 * @throws NullPointerException If {@code __message} is {@code null}.
	 * @since 2025/05/05
	 */
	void __setWaveDrums(@NotNull byte[] __message)
		throws NullPointerException
	{
		if (__message == null)
			throw new NullPointerException("NARG");

		// De-register existing wave drums
		__MA3Algorithm__[] wavDrums = this._wavDrums;
		Arrays.fill(wavDrums, null);
		
		// Decode wave drums
		int count = (__message.length - 4) / 18;
		for (int x = 0, src = 4; x < count; x++, src += 18)
		{
			// Working variables
			__MA3Algorithm__ drum = new __MA3Algorithm__(__message, src + 1);
			
			// Error checking
			if (drum._drumKey >= 24 && drum._drumKey <= 91 || 
				drum._ep < drum._lp || drum._rm && (drum._waveId == 7 ||
				drum._ep > MA3SamplerProvider.MA3_WAVEROM[drum._waveId].length))
				continue;
			
			// Register the wave drum
			wavDrums[drum._drumKey] = drum;
		}
		
	}
	
	/**
	 * Stops any currnetly playing wave drum notes.
	 *
	 * @since 2025/05/05
	 */
	void __stopWaveDrums()
	{
		for (__MA3Channel__ chan : this._channels)
			for (__MA3Note__ note : chan._notesOut)
				if (note._algorithm._isWave)
					note.__stop();
	}
}
