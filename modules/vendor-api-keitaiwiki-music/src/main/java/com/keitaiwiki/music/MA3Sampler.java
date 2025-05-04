// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.keitaiwiki.music;

import cc.squirreljme.runtime.cldc.util.ExtraMath;
import java.util.Arrays;

/**
 * Not Described.
 *
 * @since 2025/05/02
 */
class MA3Sampler
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
	
	
	MA3Sampler(MA3SamplerProvider __ma3, float sampleRate)
	{
		
		
		this.channels = new MA3Channel[10];
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
	public void bankChange(int channel, int bank)
	{
		if (channel < 0 || channel >= this.channels.length)
			return;
		MA3Channel chan = this.channels[channel];
		chan.prgBank = bank;
	}
	
	/**
	 * Specify whether a channel should play drum notes.
	 */
	public void drumEnable(int channel, boolean enable)
	{
		if (channel < 0 || channel >= this.channels.length)
			return;
		MA3Channel chan = this.channels[channel];
		chan.isDrum = enable;
	}
	
	/**
	 * Determine whether or not any notes are producing output.
	 */
	public boolean isFinished()
	{
		for (MA3Channel chan : this.channels)
		{
			if (chan.notesOut.size() != 0)
				return false;
		}
		return true;
	}
	
	/**
	 * Deactivate a key that has previoulsy been activated on a channel.
	 */
	public void keyOff(int channel, int key)
	{
		if (channel < 0 || channel >= this.channels.length || MA3SamplerProvider.A4 + key < 0 || MA3SamplerProvider.A4 + key >= 128)
			return;
		MA3Channel chan = this.channels[channel];
		MA3Note note = chan.notesOn[MA3SamplerProvider.A4 + key];
		if (note != null)
			note.off();
	}
	
	/**
	 * Activate a key on a channel.
	 */
	public void keyOn(int channel, int key, float velocity)
	{
		
		// Error checking
		if (Float.isInfinite(velocity) || velocity < 0.0f)
			throw new IllegalArgumentException("Invalid velocity.");
		if (channel < 0 || channel >= this.channels.length || MA3SamplerProvider.A4 + key < 0 || MA3SamplerProvider.A4 + key >= 128)
			return;
		
		// Working variables
		MA3Algorithm algorithm = null;
		MA3Channel chan = this.channels[channel];
		float freqBase = 0;
		boolean isWave = false;
		MA3Note note = chan.notesOn[MA3SamplerProvider.A4 + key];
		
		// FM instrument algorithm
		if (!chan.isDrum)
		{
			int program = chan.prgProgram & 0x3F;
			
			// Adjust program by bank number
			switch (chan.prgBank)
			{
				case 0:
				case 1:
					// These banks appear to disregard the program number.
					program = 0;
					break;
				case 8:
				case 9:
					// These appear to be special filter banks associated
					// with SysEx messages beginning with 11 01 F0 04.
					
					// Fallthrough
				default:
					program |= (chan.prgBank & 1) << 6;
			}
			
			algorithm = this.ma3.algInstruments[program];
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
		
		// Force a new note if drums or algorithm change
		if (chan.isDrum || note != null && (!note.playing || note.algorithm != algorithm))
			note = null;
		
		// Spawn a new note
		if (note == null)
		{
			
			// Stop any non-drum notes on the channel
			for (MA3Note other : chan.notesOut)
			{
				if (!other.algorithm.isDrum)
					other.stop();
			}
			
			// Create the new note
			note = chan.notesOn[MA3SamplerProvider.A4 + key] = new MA3Note(
				chan, algorithm);
			chan.notesOut.add(note);
		}
		
		// Configure fields
		note.playing = true;
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
	public void masterVolume(float volume)
	{
		if (Float.isInfinite(volume) || volume < 0.0f)
			throw new IllegalArgumentException("Invalid volume.");
		this.volLevel = volume == 0.0f ? 0.0f : (float)ExtraMath.pow(2,
			(1 - volume) * -96 / 20);
		this.onVolume();
	}
	
	/**
	 * Specify stereo panning on a channel.
	 */
	public void panpot(int channel, float panpot)
	{
		if (Float.isInfinite(panpot) || panpot < -1.0f || panpot > 1.0f)
			throw new IllegalArgumentException("Invalid panpot.");
		if (channel < 0 || channel >= this.channels.length)
			return;
		MA3Channel chan = this.channels[channel];
		chan.volPanning = (panpot + 1) / 2;
		chan.volLeft = (1.0f - chan.volPanning) * chan.volLevel;
		chan.volRight = chan.volPanning * chan.volLevel;
		chan.onVolume();
	}
	
	/**
	 * Specify a channel's pitch bend.
	 */
	public void pitchBend(int channel, float semitones)
	{
		if (Float.isInfinite(semitones))
			throw new IllegalArgumentException("Invalid semitones.");
		if (channel < 0 || channel >= this.channels.length)
			return;
		MA3Channel chan = this.channels[channel];
		chan.bendBase = semitones;
		chan.bendOut = (float)ExtraMath.pow(2, chan.bendBase * chan.bendRange);
		chan.onFrequency();
	}
	
	/**
	 * Specify the range of a channel's pitch bend.
	 */
	public void pitchBendRange(int channel, float range)
	{
		if (Float.isInfinite(range) || range < 0.0f)
			throw new IllegalArgumentException("Invalid range.");
		if (channel < 0 || channel >= this.channels.length)
			return;
		MA3Channel chan = this.channels[channel];
		chan.bendRange = range;
		chan.bendOut = (float)ExtraMath.pow(2, chan.bendBase * chan.bendRange);
		chan.onFrequency();
	}
	
	/**
	 * Speicfy a channel's program number.
	 */
	public void programChange(int channel, int program)
	{
		MA3Channel chan = this.channels[channel];
		chan.prgProgram = program;
	}
	
	/**
	 * Generate output samples.
	 */
	public void render(float[] samples, int offset, int frames)
	{
		this.render(samples, offset, frames, 1.0f, 1.0f, true, true);
	}
	
	/**
	 * Generate output samples.
	 */
	public void render(float[] samples, int offset, int frames,
		float amplitude)
	{
		this.render(samples, offset, frames, amplitude, amplitude, true,
			true);
	}
	
	/**
	 * Generate output samples.
	 */
	public void render(float[] samples, int offset, int frames, float left,
		float right)
	{
		this.render(samples, offset, frames, left, right, true, true);
	}
	
	/**
	 * Generate output samples.
	 */
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
		
		// Process all output frames
		float[] frame = new float[2];
		for (int x = 0; x < frames; x++)
		{
			float l = this.smpPosition;
			float r = l + this.smpWidth;
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
					this.smpPrev[0] + (this.smpNext[0] - this.smpPrev[0]) * a;
				frame[1] =
					this.smpPrev[1] + (this.smpNext[1] - this.smpPrev[1]) * a;
			}
			
			// Left and right span input samples
			else
			{
				
				// First partial
				a = (l + 1.0f) / 2;
				b = 1.0f - l;
				frame[0] =
					(this.smpPrev[0] + (this.smpNext[0] - this.smpPrev[0]) * a) * b;
				frame[1] =
					(this.smpPrev[1] + (this.smpNext[1] - this.smpPrev[1]) * a) * b;
				
				// All wholes
				for (int y = (int)Math.floor(r) - 1; y > 0; y--)
				{
					this.smpPrev[0] = this.smpNext[0];
					this.smpPrev[1] = this.smpNext[1];
					this.sample();
					frame[0] += (this.smpPrev[0] + this.smpNext[0]) / 2;
					frame[1] += (this.smpPrev[1] + this.smpNext[1]) / 2;
				}
				
				// Record the latest input sample
				this.smpPrev[0] = this.smpNext[0];
				this.smpPrev[1] = this.smpNext[1];
				
				// Last partial
				r %= 1.0f;
				if (r != 0.0f)
				{
					this.sample();
					a = r / 2;
					frame[0] +=
						(this.smpPrev[0] + (this.smpNext[0] - this.smpPrev[0]) * a) * r;
					frame[1] +=
						(this.smpPrev[1] + (this.smpNext[1] - this.smpPrev[1]) * a) * r;
				}
				
				// Take the weigted average of all spanned input samples
				frame[0] /= this.smpWidth;
				frame[1] /= this.smpWidth;
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
			this.smpPosition = r;
		}
		
	}
	
	/**
	 * Initialize all output state.
	 */
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
		for (MA3Channel chan : this.channels)
			chan.reset();
		Arrays.fill(this.wavDrums, null);
	}
	
	/**
	 * Process a SysEx message.
	 */
	public void sysEx(byte[] message)
	{
		
		// Error checking
		if (message == null || message.length < 4 || message[0] != (byte)0x11 || message[1] != (byte)0x01 || (message[2] & 0xF0) != 0xF0)
			return;
		
		// Processing by sub-message type
		switch (message[3] & 0xFF)
		{
			case 0x03: // Specify the global fade
				this.setMasterFade(message);
				break;
			case 0x04:
				// This message appears to define post-processing line
				// filters for instruments.
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
	public void volume(int channel, float volume)
	{
		if (Float.isInfinite(volume) || volume < 0.0f)
			throw new IllegalArgumentException("Invalid volume.");
		if (channel < 0 || channel >= this.channels.length)
			return;
		MA3Channel chan = this.channels[channel];
		chan.volLevel = volume == 0.0f ? 0.0f : (float)ExtraMath.pow(2,
			(1 - volume) * -96 / 20);
		chan.volLeft = (1.0f - chan.volPanning) * chan.volLevel;
		chan.volRight = chan.volPanning * chan.volLevel;
		chan.onVolume();
	}
	
	
	/**
	 * Retrieve an algorithm for playing an FM drum note
	 */
	MA3Algorithm getDrumFM(int key)
	{
		
		// Transform wave drum keys into FM drum keys
		if (key < 0)
			key += 35;
		
		// Error checking
		if (key < 0 || key >= this.ma3.algDrums.length)
			return null;
		
		// Select the preset algorithm
		return this.ma3.algDrums[key];
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
		if (ret != null && !ret.rm && (this.wavRam == null || ret.ep >= this.wavRam.length))
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
		Arrays.fill(this.wavDrums, null);
		
		// Decode wave drums
		int count = (message.length - 4) / 18;
		for (int x = 0, src = 4; x < count; x++, src += 18)
		{
			
			// Working variables
			MA3Algorithm drum = new MA3Algorithm(message, src + 1);
			
			// Error checking
			if (drum.drumKey >= 24 && drum.drumKey <= 91 || drum.ep < drum.lp || drum.rm && (drum.waveId == 7 || drum.ep > MA3SamplerProvider.MA3_WAVEROM[drum.waveId].length))
				continue;
			
			// Register the wave drum
			this.wavDrums[drum.drumKey] = drum;
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
