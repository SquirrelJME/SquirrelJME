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

	/** The sample generator used for audio rendering. */
	@SquirrelJMEVendorApi
	private final MA3SamplerProvider ma3;
	
	/** Amplitude modulator phase. */
	@SquirrelJMEVendorApi
	int amPhase;
	
	/** Global pitch bend. */
	@SquirrelJMEVendorApi
	float bendOut;
	
	/** Channel states. */
	@SquirrelJMEVendorApi
	final __MA3Channel__[] channels;
	
	/** Output sampling rate. */
	@SquirrelJMEVendorApi
	final float sampleRate;
	
	/** Next input sample. */
	@SquirrelJMEVendorApi
	final float[] smpNext;
	
	/** Position between input samples. */
	@SquirrelJMEVendorApi
	float smpPosition;
	
	/** Previous input sample. */
	@SquirrelJMEVendorApi
	final float[] smpPrev;
	
	/** Number of input samples per output sample. */
	@SquirrelJMEVendorApi
	final float smpWidth;
	
	/** Frequency modulator phase. */
	@SquirrelJMEVendorApi
	int vibPhase;
	
	/** Global attenuation. */
	@SquirrelJMEVendorApi
	float volFade;
	
	/** Global volume. */
	@SquirrelJMEVendorApi
	float volLevel;
	
	/** Effective global volume. */
	@SquirrelJMEVendorApi
	float volOut;
	
	/** Automatic volume adjustment rate. */
	@SquirrelJMEVendorApi
	final float volRate;
	
	/** Registered wave drums. */
	@SquirrelJMEVendorApi
	final __MA3Algorithm__[] wavDrums;
	
	/** Wave RAM, decoded from ADPCM. */
	@SquirrelJMEVendorApi
	int[] wavRam;
	
	/** 2-operator instruments. */
	@SquirrelJMEVendorApi
	Map<Integer, __MA3Algorithm__> fm2ops;
	
	/** 4-operator instruments. */
	@SquirrelJMEVendorApi
	Map<Integer, __MA3Algorithm__> fm4pos;
	
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

		this.channels = new __MA3Channel__[NUM_CHANNELS];
		this.fm2ops = new HashMap<>();
		this.fm4pos = new HashMap<>();
		this.sampleRate = __sampleRate;
		this.smpNext = new float[2];
		this.smpPrev = new float[2];
		this.smpWidth = MA3SamplerProvider.SAMPLE_RATE / __sampleRate;
		this.volRate = 1 / (__sampleRate * 0.01f);
		this.wavDrums = new __MA3Algorithm__[128];
		
		// Channels
		for (int x = 0; x < this.channels.length; x++)
			this.channels[x] = new __MA3Channel__(this, x);
		
		// Initialize state
		this.reset();
		this.ma3 = __ma3;
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
		__MA3Channel__[] channels = this.channels;
		if (__channel < 0 || __channel >= channels.length || __bank < 0 ||
			__bank > 255)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan.prgBank = __bank;
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
		__MA3Channel__[] channels = this.channels;
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan.isDrum = __enable;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public boolean isFinished()
	{
		for (__MA3Channel__ chan : this.channels)
			if (chan.notesOut.size() != 0)
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
		__MA3Channel__[] channels = this.channels;
		if (__channel < 0 || __channel >= channels.length ||
			MA3SamplerProvider.A4 + __key < 0 ||
			MA3SamplerProvider.A4 + __key >= 128)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		__MA3Note__ note = chan.notesOn[MA3SamplerProvider.A4 + __key];
		if (note != null)
			note.off();
		
		chan.notesOn[MA3SamplerProvider.A4 + __key] = null;
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
		__MA3Channel__[] channels = this.channels;
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
		__MA3Note__ note = chan.notesOn[MA3SamplerProvider.A4 + __key];
		
		// FM instrument algorithm
		if (!chan.isDrum)
		{
			algorithm = this.getFMInstrument(chan.prgBank, chan.prgProgram);
			freqBase = (float)(440 * ExtraMath.pow(2, __key / 12.0));
		}
		
		// Drum algorithm
		else
		{
			if (this.ma3.prgWaveDrumType != MA3SamplerProvider.WAVE_DRUM_NONE)
			{
				algorithm = this.getDrumWave(__key);
				isWave = algorithm != null;
			}
			
			if (algorithm == null)
				algorithm = this.getDrumFM(__key);
			
			if (algorithm == null)
				return;
			
			freqBase = algorithm.freqBase;
			isWave = algorithm.isWave;
		}
		
		
		// Stop the previous note if necessary
		if (note != null && (chan.isDrum || note.algorithm != algorithm))
		{
			this.keyOff(__channel, __key);
			note = null;
		}
		
		// Spawn a new note if necessary
		if (note == null)
		{
			// Create the new note
			note = new __MA3Note__(chan, __key, algorithm);
			chan.notesOn[MA3SamplerProvider.A4 + __key] = note;
			chan.notesOut.add(note);
		}
		
		// Configure fields
		note.volBase = __velocity;
		note.onVolume();
		if (!isWave)
		{
			note.freqBase = freqBase;
			note.onFrequency(this.bendOut * chan.bendOut);
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
		
		this.bendOut = (float)ExtraMath.pow(2, __semitones);
		for (__MA3Channel__ chan : this.channels)
			chan.onFrequency();
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

		this.volLevel = __volume;
		this.onVolume();
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
		__MA3Channel__[] channels = this.channels;
		if (Float.isInfinite(__panpot) || __panpot < -1.0f || __panpot > 1.0f)
			throw new IllegalArgumentException("Invalid panpot.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan.volPanning = (__panpot + 1) / 2;
		chan.volLeft = (1.0f - chan.volPanning) * chan.volLevel;
		chan.volRight = chan.volPanning * chan.volLevel;
		chan.onVolume();
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
		__MA3Channel__[] channels = this.channels;
		if (Float.isInfinite(__semitones))
			throw new IllegalArgumentException("Invalid semitones.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan.bendBase = __semitones;
		chan.bendOut = (float)ExtraMath.pow(2,
			chan.bendBase * chan.bendRange);
		chan.onFrequency();
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
		__MA3Channel__[] channels = this.channels;
		if (Float.isInfinite(__range) || __range < 0.0f)
			throw new IllegalArgumentException("Invalid pitch bend range.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan.bendRange = __range;
		chan.bendOut = (float)ExtraMath.pow(2,
			chan.bendBase * chan.bendRange);
		chan.onFrequency();
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
		__MA3Channel__[] channels = this.channels;
		if (__channel < 0 || __channel >= channels.length || __program < 0 ||
			__program > 255)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan.prgProgram = __program;
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
		float[] smpPrev = this.smpPrev;
		float[] smpNext = this.smpNext;
		float smpWidth = this.smpWidth;
		
		// Modified in the loop
		float smpPosition = this.smpPosition;
		
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
					this.sample();
				
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
						this.sample();
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
						this.sample();
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
			this.smpPosition = smpPosition;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/05/05
	 */
	@Override
	public void reset()
	{
		this.amPhase = 0;
		this.bendOut = 1.0f;
		this.smpPosition = 0.0f;
		this.smpPrev[0] = this.smpPrev[1] = 0.0f;
		this.vibPhase = 0;
		this.volFade = 0.0f;
		this.volLevel = 1.0f;
		this.volOut = 1.0f;
		this.wavRam = null;
		this.fm2ops.clear();
		this.fm4pos.clear();
		for (__MA3Channel__ chan : this.channels)
			chan.reset();
		Arrays.fill(this.wavDrums, null);
	}
	
	/**
	 * Terminates all currently active notes.
	 *
	 * @since 2025/05/05
	 */
	public void stopAll()
	{
		for (__MA3Channel__ chan : this.channels)
		{
			Arrays.fill(chan.notesOn, null);
			for (__MA3Note__ note : chan.notesOut)
				note.stop();
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
				this.setMasterFade(__message);
				break;
			case 0x04:
				this.setFMAlgorithms(__message);
				break;
			case 0x05: // Register wave drum algorithms
				this.setWaveDrums(__message);
				this.stopWaveDrums();
				break;
			case 0x06: // Supply wave drum samples
				this.wavRam = MA3SamplerProvider.decodeAICA(__message, 4,
					__message.length - 4);
				this.stopWaveDrums();
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
		__MA3Channel__[] channels = this.channels;
		if (Float.isInfinite(__volume) || __volume < 0.0f)
			throw new IllegalArgumentException("Invalid volume value.");
		
		if (__channel < 0 || __channel >= channels.length)
			return;
		
		__MA3Channel__ chan = channels[__channel];
		chan.volLevel = __volume;
		chan.volLeft = (1.0f - chan.volPanning) * chan.volLevel;
		chan.volRight = chan.volPanning * chan.volLevel;
		chan.onVolume();
	}
	
	/** Retrieve an algorithm for playing an FM instrument. */
	__MA3Algorithm__ getFMInstrument(int bank, int program)
	{
		int hashKey = bank << 8 | program;
		__MA3Algorithm__ ret = null;
		
		MA3SamplerProvider ma3 = this.ma3;
		Map<Integer, __MA3Algorithm__> _fm4pos = this.fm4pos;
		
		// Running in 4-algorithm mode
		if (ma3.prgInstrumentType == MA3SamplerProvider.FM_MA3_4OP)
			ret = _fm4pos.get(hashKey);
		
		// Fallback to 2-algorithm mode
		if (ret == null)
			ret = this.fm2ops.get(hashKey);
		
		// Fallback to preset
		if (ret == null)
		{
			ret = ma3.algInstruments[bank < 2 ? 0 : // Apparent behavior
				(bank & 1) << 6 | program & 0x3F];
		}
		
		return ret;
	}
	
	/** Specify FM algorithms. */

	/**
	 * Specifies a set of FM algorithms from the given data array.
	 *
	 * @param __message The data array containing the FM algorithms.
	 * @throws NullPointerException if {@code __message} is {@code null};
	 * @since 2025/05/05
	 */
	void setFMAlgorithms(@NotNull byte[] __message)
		throws NullPointerException
	{
		if (__message == null)
			throw new NullPointerException("NARG");

		Map<Integer, __MA3Algorithm__> _fm2ops = this.fm2ops;
		Map<Integer, __MA3Algorithm__> _fm4pos = this.fm4pos;
		
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
			if (type == 1 && algorithm.operators.length == 4)
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
	 * Retrieves an algorithm for playing an FM drum note.
	 *
	 * @param __key The key for which an algorithm must be retrieved.
	 * @return The algorithm to be used for playing FM drum notes, or
	 * {@code null} if the requested key does not match to a valid FM drum
	 * algorithm.
	 * @since 2025/05/05
	 */
	__MA3Algorithm__ getDrumFM(int __key)
	{
		__MA3Algorithm__[] algDrums = this.ma3.algDrums;
		
		// Transform wave drum keys into FM drum keys
		if (__key < 0)
			__key += 35;
		
		// Error checking
		if (__key < 0 || __key >= algDrums.length)
			return null;
		
		// Select the preset algorithm
		return algDrums[__key];
	}
	
	/**
	 * Retrieves an algorithm for playing a wave drum note.
	 *
	 * @param __key The key for which an algorithm must be retrieved.
	 * @return The algorithm to be used for playing wave drum notes, or
	 * {@code null} if the requested key does not match to a valid wave drum
	 * algorithm.
	 * @since 2025/05/05
	 */
	__MA3Algorithm__ getDrumWave(int key)
	{
		// Error checking
		if (key < -24)
			return null;
		
		// Select the registered wave algorithm, if available
		__MA3Algorithm__[] algs = this.ma3.algWaveDrums;
		__MA3Algorithm__ ret = null;
		if (key < 0)
		{
			algs = this.wavDrums;
			key += 24;
		}

		if (key >= 0 && key < algs.length)
			ret = algs[key];
		
		// Error checking
		int[] _wavRam = this.wavRam;
		if (ret != null && !ret.rm &&
			(_wavRam == null || ret.ep >= _wavRam.length))
			ret = null;
		
		return ret;
	}
	
	/**
	 * This function is called whenever there must be a volume change in this
	 * sampler.
	 *
	 * @since 2025/05/05
	 */
	void onVolume()
	{
		this.volOut = (1.0f - this.volFade) * this.volLevel;
		for (__MA3Channel__ chan : this.channels)
			chan.onVolume();
	}
	
	/**
	 * Produces a single audio sample.
	 *
	 * @since 2025/05/05
	 */
	void sample()
	{
		this.smpNext[0] = this.smpNext[1] = 0.0f;
		for (__MA3Channel__ chan : this.channels)
			chan.render();
		
		this.amPhase = (this.amPhase + 1) % 0x34000;
		this.vibPhase++;
	}
	
	/**
	 * Specifies a global volume fade level.
	 *
	 * @param __message A message from which the fade level must be extracted.
	 * @throws NullPointerException If {@code __message} is {@code null}.
	 * @since 2025/05/05
	 */
	void setMasterFade(@NotNull byte[] __message)
		throws NullPointerException
	{
		if (__message == null)
			throw new NullPointerException("NARG");

		if (__message.length < 5)
			return;
		
		this.volFade = (__message[4] & 0x7F) / 127.0f;
		this.onVolume();
	}
	
	/**
	 * Decodes and registers wave drum definitions from a byte array.
	 *
	 * @param __message A message containing the wave drum definitions.
	 * @throws NullPointerException If {@code __message} is {@code null}.
	 * @since 2025/05/05
	 */
	void setWaveDrums(@NotNull byte[] __message)
		throws NullPointerException
	{
		if (__message == null)
			throw new NullPointerException("NARG");

		// De-register existing wave drums
		__MA3Algorithm__[] wavDrums = this.wavDrums;
		Arrays.fill(wavDrums, null);
		
		// Decode wave drums
		int count = (__message.length - 4) / 18;
		for (int x = 0, src = 4; x < count; x++, src += 18)
		{
			// Working variables
			__MA3Algorithm__ drum = new __MA3Algorithm__(__message, src + 1);
			
			// Error checking
			if (drum.drumKey >= 24 && drum.drumKey <= 91 || 
				drum.ep < drum.lp || drum.rm && (drum.waveId == 7 ||
				drum.ep > MA3SamplerProvider.MA3_WAVEROM[drum.waveId].length))
				continue;
			
			// Register the wave drum
			wavDrums[drum.drumKey] = drum;
		}
		
	}
	
	/**
	 * Stops any currnetly playing wave drum notes.
	 *
	 * @since 2025/05/05
	 */
	void stopWaveDrums()
	{
		for (__MA3Channel__ chan : this.channels)
			for (__MA3Note__ note : chan.notesOut)
				if (note.algorithm.isWave)
					note.stop();
	}
}
