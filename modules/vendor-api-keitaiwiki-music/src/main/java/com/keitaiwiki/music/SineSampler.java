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

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.ExtraMath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Sampler instance
 */
class SineSampler
	implements Sampler
{
	
	
	/**
	 * Channel states
	 */
	final SineChannel[] channels;
	
	/**
	 * Global pitch bend
	 */
	float masterTune;
	
	/**
	 * Global volume
	 */
	float masterVolume;
	
	/**
	 * Output sampling rate
	 */
	final float sampleRate;
	
	/**
	 * Automatic volume adjustment rate
	 */
	final float volRate;
	
	
	/**
	 * Constructor
	 */
	SineSampler(float sampleRate)
	{
		
		
		this.channels = new SineChannel[16];
		this.sampleRate = sampleRate;
		this.volRate = 1 / (sampleRate * 0.01f);
		
		// Channels
		for (int x = 0; x < this.channels.length; x++)
		{
			SineChannel chan = this.channels[x] = new SineChannel();
			chan.index = x;
			//  C-2 .. G8
			chan.notesOn = new SineNote[127];
			chan.notesOut = new ArrayList<>();
		}
		
		// Reset all state
		this.reset();
	}
	
	
	/**
	 * Specify a channel's program bank.
	 */
	public void bankChange(int channel, int bank)
	{
		// Not implementing
	}
	
	/**
	 * Specify whether a channel should play drum notes.
	 */
	public void drumEnable(int channel, boolean enable)
	{
		// Not implementing
	}
	
	/**
	 * Deactivate a key that has previoulsy been activated on a channel.
	 */
	public void keyOff(int channel, int key)
	{
		if (channel < 0 || channel >= this.channels.length || SineSamplerProvider.A4 + key < 0 || SineSamplerProvider.A4 + key >= 128)
			return;
		SineChannel chan = this.channels[channel];
		SineNote note = chan.notesOn[SineSamplerProvider.A4 + key];
		if (note != null)
		{
			note.playing = false;
			note.volBase = 0;
		}
	}
	
	/**
	 * Determine whether or not any notes are producing output.
	 */
	public boolean isFinished()
	{
		for (SineChannel chan : this.channels)
		{
			if (chan.notesOut.size() != 0)
				return false;
		}
		return true;
	}
	
	/**
	 * Activate a key on a channel.
	 */
	public void keyOn(int channel, int key, float velocity)
	{
		
		// Error checking
		if (Float.isInfinite(velocity) || velocity < 0.0f)
			throw new IllegalArgumentException("Invalid velocity.");
		if (channel < 0 || channel >= this.channels.length || SineSamplerProvider.A4 + key < 0 || SineSamplerProvider.A4 + key >= 128)
			return;
		
		// Working variables
		SineChannel chan = this.channels[channel];
		SineNote note = chan.notesOn[SineSamplerProvider.A4 + key];
		
		// No note is currently playing on the specified key
		if (note == null)
		{
			note = chan.notesOn[SineSamplerProvider.A4 + key] =
				new SineNote();
			chan.notesOut.add(note);
			note.channel = chan;
			note.volLeftLevel = 0.0f;
			note.volRightLevel = 0.0f;
			note.wavPhase = 0.0f;
		}
		
		// Configure fields
		note.freqBase = (float)(440 * ExtraMath.pow(2, key / 12.0));
		note.playing = true;
		note.volBase = velocity;
	}
	
	/**
	 * Specify the global pitch bend.
	 */
	public void masterTune(float semitones)
	{
		if (Float.isInfinite(semitones))
			throw new IllegalArgumentException("Invalid semitones.");
		this.masterTune = (float)ExtraMath.pow(2, semitones);
	}
	
	/**
	 * Specify the global volume.
	 */
	public void masterVolume(float volume)
	{
		if (Float.isInfinite(volume) || volume < 0.0f)
			throw new IllegalArgumentException("Invalid volume.");
		this.masterVolume = volume;
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
		SineChannel chan = this.channels[channel];
		chan.volPanning = (panpot + 1) / 2;
		chan.volLeft = (1.0f - chan.volPanning) * chan.volLevel;
		chan.volRight = chan.volPanning * chan.volLevel;
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
		SineChannel chan = this.channels[channel];
		chan.bendBase = semitones;
		chan.bendOut = (float)ExtraMath.pow(2, chan.bendBase * chan.bendRange);
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
		SineChannel chan = this.channels[channel];
		chan.bendRange = range;
		chan.bendOut = (float)ExtraMath.pow(2, chan.bendBase * chan.bendRange);
	}
	
	/**
	 * Speicfy a channel's program number.
	 */
	public void programChange(int channel, int program)
	{
		// Not implementing
	}
	
	/**
	 * Generate output samples.
	 */
	public void render(float[] samples, int offset, int frames)
	{
		this.render(samples, offset, frames, 1.0f, true, true);
	}
	
	/**
	 * Generate output samples.
	 */
	public void render(float[] samples, int offset, int frames,
		float amplitude)
	{
		this.render(samples, offset, frames, amplitude, true, true);
	}
	
	@Override
	public void render(float[] samples, int offset, int frames, float left,
		float right)
	{
		throw Debugging.todo();
	}
	
	@Override
	public void render(float[] samples, int offset, int frames, float left,
		float right, boolean erase, boolean clamp)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Generate output samples.
	 */
	public void render(float[] samples, int offset, int frames,
		float amplitude, boolean erase, boolean clamp)
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
		if (Float.isInfinite(amplitude) || amplitude < 0.0f)
			throw new IllegalArgumentException("Invalid amplitude.");
		
		// Erase the output buffer
		if (erase)
		{
			for (int x = frames * 2 - 1; x >= 0; x--)
				samples[offset + x] = 0.0f;
		}
		
		// Render output samples
		for (SineChannel chan : this.channels)
			this.chanRender(chan, samples, offset, frames, amplitude);
		
		// Clamp the output buffer
		if (clamp)
		{
			for (int x = frames * 2 - 1; x >= 0; x--)
			{
				samples[offset + x] = Math.min(
					Math.max(samples[offset + x], -1.0f), 1.0f);
			}
		}
		
	}
	
	/**
	 * Initialize all output state.
	 */
	public void reset()
	{
		
		// Global fields
		this.masterTune = 1.0f;
		this.masterVolume = 1.0f;
		
		// Channels
		for (SineChannel chan : this.channels)
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
			for (SineNote note : chan.notesOut)
			{
				note.playing = false;
				note.volBase = 0.0f;
			}
		}
		
	}
	
	/**
	 * Process a SysEx message.
	 */
	public void sysEx(byte[] message)
	{
		// Not implementing
	}
	
	/**
	 * Specify a channel's volume
	 */
	public void volume(int channel, float volume)
	{
		if (Float.isInfinite(volume) || volume < 0.0f)
			throw new IllegalArgumentException("Invalid volume.");
		if (channel < 0 || channel >= this.channels.length)
			return;
		SineChannel chan = this.channels[channel];
		chan.volLevel = volume;
		chan.volLeft = (1.0f - chan.volPanning) * chan.volLevel;
		chan.volRight = chan.volPanning * chan.volLevel;
	}
	
	
	/**
	 * Render samples on a channel
	 */
	void chanRender(SineChannel chan, float[] samples, int offset, int frames,
		float amplitude)
	{
		
		// Working variables
		float bend = this.masterTune * chan.bendOut;
		
		// Process all notes
		Iterator<SineNote> iter = chan.notesOut.iterator();
		while (iter.hasNext())
		{
			if (this.noteRender(iter.next(), samples, offset, frames,
				amplitude, chan.volLeft, chan.volRight, bend))
				iter.remove();
		}
		
		// Disassociate inactive notes
		for (int x = 0; x < chan.notesOn.length; x++)
		{
			SineNote note = chan.notesOn[x];
			if (note != null && !note.playing)
				chan.notesOn[x] = null;
		}
		
	}
	
	/**
	 * Render samples on a note
	 */
	boolean noteRender(SineNote note, float[] samples, int offset, int frames,
		float amplitude, float left, float right, float bend)
	{
		
		// Working variables
		float freq = note.freqBase * bend;
		float advance = freq / this.sampleRate;
		
		// Compute desired left and right volume levels
		note.volLeftTarget = note.volBase * left;
		note.volRightTarget = note.volBase * right;
		
		// Process all samples
		for (int x = 0; x < frames; x++)
		{
			
			// Generate one sample
			float sample = this.sample(note, advance);
			samples[offset++] += sample * note.volLeftLevel * amplitude;
			samples[offset++] += sample * note.volRightLevel * amplitude;
			
			// Adjust stereo levels
			note.volLeftLevel = this.volAdjust(note.volLeftLevel,
				note.volLeftTarget);
			note.volRightLevel = this.volAdjust(note.volRightLevel,
				note.volRightTarget);
			
			// Note has finished
			if (!note.playing && note.volLeftLevel == 0 && note.volRightLevel == 0)
				return true;
		}
		
		// Note has not finished
		return false;
	}
	
	/**
	 * Generate a sample on a note
	 */
	float sample(SineNote note, float advance)
	{
		float ret = (float)Math.sin(note.wavPhase * Math.PI * 2);
		note.wavPhase = (note.wavPhase + advance) % 1;
		return ret;
	}
	
	/**
	 * Process a SysExt message
	 */
	public void sysExt(byte[] message)
	{
		// Not implementing
	}
	
	/**
	 * Move a volume level closer to its target
	 */
	float volAdjust(float level, float target)
	{
		return level < target ? Math.min(level + this.volRate, target) :
			Math.max(level - this.volRate, target);
	}
	
}
