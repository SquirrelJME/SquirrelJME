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

/**
 * Generates live MA-3 samples.
 *
 * @since 2025/05/02
 */
public class MA3Sampler
	extends AbstractSampler
	implements Sampler
{
	private final MA3SamplerProvider ma3;
	
	/**
	 * Amplitude modulator phase
	 */
	int amPhase;
	
	/**
	 * Global pitch bend
	 */
	float bendOut;
	
	/**
	 * Channel states
	 */
	final MA3Channel[] channels;
	
	/**
	 * Output sampling rate
	 */
	final float sampleRate;
	
	/**
	 * Next input sample
	 */
	final float[] smpNext;
	
	/**
	 * Position between input samples
	 */
	float smpPosition;
	
	/**
	 * Previous input sample
	 */
	final float[] smpPrev;
	
	/**
	 * Number of input samples per output sample
	 */
	final float smpWidth;
	
	/**
	 * Frequency modulator phase
	 */
	int vibPhase;
	
	/**
	 * Global attenuation
	 */
	float volFade;
	
	/**
	 * Global volume
	 */
	float volLevel;
	
	/**
	 * Effective global volume
	 */
	float volOut;
	
	/**
	 * Automatic volume adjustment rate
	 */
	final float volRate;
	
	/**
	 * Registered wave drums
	 */
	final MA3Algorithm[] wavDrums;
	
	/**
	 * Wave RAM, decoded from ADPCM
	 */
	int[] wavRam;
	
	/** 2-operator instruments. */
	Map<Integer, MA3Algorithm> fm2ops;
	
	/** 4-operator instruments. */
	Map<Integer, MA3Algorithm> fm4ops;
	
	public MA3Sampler(MA3SamplerProvider __ma3, float sampleRate)
	{
		this.channels = new MA3Channel[10];
		this.fm2ops = new HashMap<>();
		this.fm4ops = new HashMap<>();
		this.sampleRate = sampleRate;
		this.smpNext = new float[2];
		this.smpPrev = new float[2];
		this.smpWidth = MA3SamplerProvider.SAMPLE_RATE / sampleRate;
		this.volRate = 1 / (sampleRate * 0.01f);
		this.wavDrums = new MA3Algorithm[128];
		
		// Channels
		for (int x = 0; x < this.channels.length; x++)
			this.channels[x] = new MA3Channel(this, x);
		
		// Initialize state
		this.reset();
		this.ma3 = __ma3;
	}
	
	
	/**
	 * Specify a channel's program bank.
	 */
	@Override
	public void bankChange(int channel, int bank)
	{
		MA3Channel[] channels = this.channels;
		if (channel < 0 || channel >= channels.length)
			return;
		
		MA3Channel chan = channels[channel];
		chan.prgBank = bank;
	}
	
	/**
	 * Specify whether a channel should play drum notes.
	 */
	@Override
	public void drumEnable(int channel, boolean enable)
	{
		MA3Channel[] channels = this.channels;
		if (channel < 0 || channel >= channels.length)
			return;
		
		MA3Channel chan = channels[channel];
		chan.isDrum = enable;
	}
	
	/**
	 * Determine whether or not any notes are producing output.
	 */
	@Override
	public boolean isFinished()
	{
		for (MA3Channel chan : this.channels)
			if (chan.notesOut.size() != 0)
				return false;
		
		return true;
	}
	
	/**
	 * Deactivate a key that has previoulsy been activated on a channel.
	 */
	@Override
	public void keyOff(int channel, int key)
	{
		MA3Channel[] channels = this.channels;
		if (channel < 0 || channel >= channels.length ||
			MA3SamplerProvider.A4 + key < 0 || 
			MA3SamplerProvider.A4 + key >= 128)
			return;
		
		MA3Channel chan = channels[channel];
		MA3Note note = chan.notesOn[MA3SamplerProvider.A4 + key];
		if (note != null)
			note.off();
		
		chan.notesOn[MA3SamplerProvider.A4 + key] = null;
	}
	
	/**
	 * Activate a key on a channel.
	 */
	@Override
	public void keyOn(int channel, int key, float velocity)
	{
		// Error checking
		MA3Channel[] channels = this.channels;
		if (Float.isInfinite(velocity) || velocity < 0.0f)
			throw new IllegalArgumentException("Invalid velocity.");
		
		if (channel < 0 || channel >= channels.length || 
			MA3SamplerProvider.A4 + key < 0 || 
			MA3SamplerProvider.A4 + key >= 128)
			return;
		
		// Working variables
		MA3Algorithm algorithm = null;
		MA3Channel chan = channels[channel];
		float freqBase = 0;
		boolean isWave = false;
		MA3Note note = chan.notesOn[MA3SamplerProvider.A4 + key];
		
		// FM instrument algorithm
		if (!chan.isDrum)
		{
			algorithm = this.getFMInstrument(chan.prgBank, chan.prgProgram);
			freqBase = (float)(440 * ExtraMath.pow(2, key / 12.0));
		}
		
		// Drum algorithm
		else
		{
			if (this.ma3.prgWaveDrumType != MA3SamplerProvider.WAVE_DRUM_NONE)
			{
				algorithm = this.getDrumWave(key);
				isWave = algorithm != null;
			}
			
			if (algorithm == null)
				algorithm = this.getDrumFM(key);
			
			if (algorithm == null)
				return;
			
			freqBase = algorithm.freqBase;
			isWave = algorithm.isWave;
		}
		
		
		// Stop the previous note if necessary
		if (note != null && (chan.isDrum || note.algorithm != algorithm))
		{
			this.keyOff(channel, key);
			note = null;
		}
		
		// Spawn a new note if necessary
		if (note == null)
		{
			// Create the new note
			note = new MA3Note(chan, key, algorithm);
			chan.notesOn[MA3SamplerProvider.A4 + key] = note;
			chan.notesOut.add(note);
		}
		
		// Configure fields
		note.volBase = velocity;
		note.onVolume();
		if (!isWave)
		{
			note.freqBase = freqBase;
			note.onFrequency(this.bendOut * chan.bendOut);
		}
		
	}
	
	/**
	 * Specify the global pitch bend.
	 */
	@Override
	public void masterTune(float semitones)
	{
		if (Float.isInfinite(semitones))
			throw new IllegalArgumentException("Invalid semitones.");
		
		this.bendOut = (float)ExtraMath.pow(2, semitones);
		for (MA3Channel chan : this.channels)
			chan.onFrequency();
	}
	
	/**
	 * Specify the global volume.
	 */
	@Override
	public void masterVolume(float volume)
	{
		if (Float.isInfinite(volume) || volume < 0.0f)
			throw new IllegalArgumentException("Invalid volume.");
		this.volLevel = volume;
		this.onVolume();
	}
	
	/**
	 * Specify stereo panning on a channel.
	 */
	@Override
	public void panpot(int channel, float panpot)
	{
		MA3Channel[] channels = this.channels;
		if (Float.isInfinite(panpot) || panpot < -1.0f || panpot > 1.0f)
			throw new IllegalArgumentException("Invalid panpot.");
		
		if (channel < 0 || channel >= channels.length)
			return;
		
		MA3Channel chan = channels[channel];
		chan.volPanning = (panpot + 1) / 2;
		chan.volLeft = (1.0f - chan.volPanning) * chan.volLevel;
		chan.volRight = chan.volPanning * chan.volLevel;
		chan.onVolume();
	}
	
	/**
	 * Specify a channel's pitch bend.
	 */
	@Override
	public void pitchBend(int channel, float semitones)
	{
		MA3Channel[] channels = this.channels;
		if (Float.isInfinite(semitones))
			throw new IllegalArgumentException("Invalid semitones.");
		
		if (channel < 0 || channel >= channels.length)
			return;
		
		MA3Channel chan = channels[channel];
		chan.bendBase = semitones;
		chan.bendOut = (float)ExtraMath.pow(2,
			chan.bendBase * chan.bendRange);
		chan.onFrequency();
	}
	
	/**
	 * Specify the range of a channel's pitch bend.
	 */
	@Override
	public void pitchBendRange(int channel, float range)
	{
		MA3Channel[] channels = this.channels;
		if (Float.isInfinite(range) || range < 0.0f)
			throw new IllegalArgumentException("Invalid range.");
		
		if (channel < 0 || channel >= channels.length)
			return;
		
		MA3Channel chan = channels[channel];
		chan.bendRange = range;
		chan.bendOut = (float)ExtraMath.pow(2,
			chan.bendBase * chan.bendRange);
		chan.onFrequency();
	}
	
	/**
	 * Specify a channel's program number.
	 */
	@Override
	public void programChange(int channel, int program)
	{
		MA3Channel[] channels = this.channels;
		if (channel < 0 || channel >= channels.length)
			return;
		
		MA3Channel chan = channels[channel];
		chan.prgProgram = program;
	}
	
	/**
	 * Generate output samples.
	 */
	@Override
	public void render(float[] samples, int offset, int frames)
	{
		this.render(samples, offset, frames, 1.0f, 1.0f,
			true, true);
	}
	
	/**
	 * Generate output samples.
	 */
	@Override
	public void render(float[] samples, int offset, int frames,
		float amplitude)
	{
		this.render(samples, offset, frames, amplitude, amplitude, true,
			true);
	}
	
	/**
	 * Generate output samples.
	 */
	@Override
	public void render(float[] samples, int offset, int frames, float left,
		float right)
	{
		this.render(samples, offset, frames, left, right,
			true, true);
	}
	
	/**
	 * Generate output samples.
	 */
	@Override
	public void render(float[] samples, int offset, int frames, float left,
		float right, boolean erase, boolean clamp)
	{
		
		// Error checking
		if (samples == null)
			throw new NullPointerException(
				"A sample buffer is required" + ".");
		
		if (frames < 0)
			throw new IllegalArgumentException("Invalid frames.");
		
		if (offset < 0 || offset + frames * 2 > samples.length)
		{
			throw new ArrayIndexOutOfBoundsException(
				"Invalid range in sample buffer.");
		}
		
		if (Float.isInfinite(left) || left < 0.0f)
			throw new IllegalArgumentException("Invalid left amplitude.");
		
		if (Float.isInfinite(right) || right < 0.0f)
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
			for (int x = 0; x < frames; x++)
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
				frame[0] *= left;
				frame[1] *= right;
				
				// Incorporate the existing contents of the buffer
				if (!erase)
				{
					frame[0] += samples[offset];
					frame[1] += samples[offset + 1];
				}
				
				// Constrain the output
				if (clamp)
				{
					frame[0] = Math.min(Math.max(frame[0], -1.0f), 1.0f);
					frame[1] = Math.min(Math.max(frame[1], -1.0f), 1.0f);
				}
				
				// Output the frame
				samples[offset++] = frame[0];
				samples[offset++] = frame[1];
				
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
	 * Initialize all output state.
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
		this.fm4ops.clear();
		for (MA3Channel chan : this.channels)
			chan.reset();
		Arrays.fill(this.wavDrums, null);
	}
	
	/** Terminate all active notes. */
	public void stopAll()
	{
		for (MA3Channel chan : this.channels)
		{
			Arrays.fill(chan.notesOn, null);
			for (MA3Note note : chan.notesOut)
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
	 * Process a SysEx message.
	 */
	@Override
	public void sysEx(byte[] message)
	{
		
		// Error checking
		if (message == null || message.length < 4 || 
			message[0] != (byte)0x11 || message[1] != (byte)0x01 || 
			(message[2] & 0xF0) != 0xF0)
			return;
		
		// Processing by sub-message type
		switch (message[3] & 0xFF)
		{
			case 0x00:// Seen in Smwemu_N.dll at 10028975
				break;
			case 0x01:// Seen in Smwemu_N.dll at 1002899D
				break;
			case 0x02:// Seen in Smwemu_N.dll at 100289B4
				break;
			case 0x03: // Specify the global fade
				this.setMasterFade(message);
				break;
			case 0x04:
				this.setFMAlgorithms(message);
				break;
			case 0x05: // Register wave drum algorithms
				this.setWaveDrums(message);
				this.stopWaveDrums();
				break;
			case 0x06: // Supply wave drum samples
				this.wavRam = MA3SamplerProvider.decodeAICA(message, 4,
					message.length - 4);
				this.stopWaveDrums();
				break;
		}
		
	}
	
	/**
	 * Specify a channel's volume.
	 */
	@Override
	public void volume(int channel, float volume)
	{
		MA3Channel[] channels = this.channels;
		if (Float.isInfinite(volume) || volume < 0.0f)
			throw new IllegalArgumentException("Invalid volume.");
		
		if (channel < 0 || channel >= channels.length)
			return;
		
		MA3Channel chan = channels[channel];
		chan.volLevel = volume;
		chan.volLeft = (1.0f - chan.volPanning) * chan.volLevel;
		chan.volRight = chan.volPanning * chan.volLevel;
		chan.onVolume();
	}
	
	/** Retrieve an algorithm for playing an FM instrument. */
	MA3Algorithm getFMInstrument(int bank, int program)
	{
		int hashKey = bank << 8 | program;
		MA3Algorithm ret = null;
		
		MA3SamplerProvider ma3 = this.ma3;
		Map<Integer, MA3Algorithm> fm4ops = this.fm4ops;
		
		// Running in 4-algorithm mode
		if (ma3.prgInstrumentType == MA3SamplerProvider.FM_MA3_4OP)
			ret = fm4ops.get(hashKey);
		
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
	void setFMAlgorithms(byte[] message)
	{
		Map<Integer, MA3Algorithm> fm2ops = this.fm2ops;
		Map<Integer, MA3Algorithm> fm4ops = this.fm4ops;
		
		// Process all algorithms in the message
		for (int offset = 4; offset < message.length; )
		{
			
			// Algorithm type: 1=two-operator, 2=four-operator
			int type = message[offset] & 0xFF;
			if (type != 1 && type != 2)
				break;
			
			// Error checking
			int size = type == 1 ? 20 : 34;
			if (offset + size > message.length)
				break;
			
			// Decode the algorithm
			MA3Algorithm algorithm;
			try
			{
				algorithm = new MA3Algorithm(offset, message);
			}
			catch (Exception e)
			{
				break;
			}
			
			// Error checking
			if (type == 1 && algorithm.operators.length == 4)
				continue;
			
			// Register the algorithm
			(type == 1 ? fm2ops : fm4ops).put(
				(message[offset + 1] & 0xFF) << 8 | // Bank
					message[offset + 2] & 0xFF,        // Program
				algorithm);
			
			// Advance to the next algorithm
			offset += size;
		}
		
	}
	
	/**
	 * Retrieve an algorithm for playing an FM drum note
	 */
	MA3Algorithm getDrumFM(int key)
	{
		MA3Algorithm[] algDrums = this.ma3.algDrums;
		
		// Transform wave drum keys into FM drum keys
		if (key < 0)
			key += 35;
		
		// Error checking
		if (key < 0 || key >= algDrums.length)
			return null;
		
		// Select the preset algorithm
		return algDrums[key];
	}
	
	/**
	 * Retrieve an algorithm for playing a wave drum note
	 */
	MA3Algorithm getDrumWave(int key)
	{
		// Error checking
		if (key < -24)
			return null;
		
		// Select the registered wave algorithm, if available
		MA3Algorithm[] algs = this.ma3.algWaveDrums;
		MA3Algorithm ret = null;
		if (key < 0)
		{
			algs = this.wavDrums;
			key += 24;
		}
		if (key >= 0 && key < algs.length)
			ret = algs[key];
		
		// Error checking
		int[] wavRam = this.wavRam;
		if (ret != null && !ret.rm &&
			(wavRam == null || ret.ep >= wavRam.length))
			ret = null;
		
		return ret;
	}
	
	/**
	 * Master volume has changed
	 */
	void onVolume()
	{
		this.volOut = (1.0f - this.volFade) * this.volLevel;
		for (MA3Channel chan : this.channels)
			chan.onVolume();
	}
	
	/**
	 * Produce one input sample
	 */
	void sample()
	{
		this.smpNext[0] = this.smpNext[1] = 0.0f;
		for (MA3Channel chan : this.channels)
			chan.render();
		
		this.amPhase = (this.amPhase + 1) % 0x34000;
		this.vibPhase++;
	}
	
	/**
	 * Specify the global fade.
	 */
	void setMasterFade(byte[] message)
	{
		if (message.length < 5)
			return;
		
		this.volFade = (message[4] & 0x7F) / 127.0f;
		this.onVolume();
	}
	
	/**
	 * Decode and register wave drum definitions
	 */
	void setWaveDrums(byte[] message)
	{
		// De-register existing wave drums
		MA3Algorithm[] wavDrums = this.wavDrums;
		Arrays.fill(wavDrums, null);
		
		// Decode wave drums
		int count = (message.length - 4) / 18;
		for (int x = 0, src = 4; x < count; x++, src += 18)
		{
			// Working variables
			MA3Algorithm drum = new MA3Algorithm(message, src + 1);
			
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
	 * Terminate any existing wave drum notes
	 */
	void stopWaveDrums()
	{
		for (MA3Channel chan : this.channels)
			for (MA3Note note : chan.notesOut)
				if (note.algorithm.isWave)
					note.stop();
	}
}
