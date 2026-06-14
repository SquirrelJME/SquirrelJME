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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * i-melody MLD sequence player. Uses a {@code Sampler} to generate output to a
 * sample buffer.
 *
 * @see MLD
 * @see SamplerProvider
 */
@SquirrelJMEVendorApi
public class MLDPlayer
{
	
	/**
	 * Event type that notifies when a non-looping sequence finishes.
	 *
	 * @see MLDPlayerEvent
	 */
	public static final int EVENT_END = 0;
	
	/**
	 * Event type that notifies when a particular key is played.
	 *
	 * @see MLDPlayerEvent
	 */
	public static final int EVENT_KEY = 2;
	
	/**
	 * Event type that notifies when a sequence loops.
	 *
	 * @see MLDPlayerEvent
	 */
	public static final int EVENT_LOOP = 1;
	
	
	/**
	 * Key index bias
	 */
	static final int A4 = 48;
	
	
	/**
	 * Playback channels
	 */
	final MLDChannel[] channels;
	
	/**
	 * Pending events
	 */
	final ArrayList<MLDPlayerEvent> events;
	
	/**
	 * Key events enabled by key
	 */
	final HashSet<Integer> evtKeys;
	
	/**
	 * Sequence resource
	 */
	final MLD mld;
	
	/**
	 * Output sampling rate
	 */
	final float sampleRate;
	
	/**
	 * Sample generator
	 */
	@SquirrelJMEVendorApi
	public final Sampler sampler;
	
	/**
	 * Sequencer state
	 */
	final MLDPlayerTrack[] tracks;
	
	/**
	 * Playback events are enabled
	 */
	boolean evtPlayback;
	
	/**
	 * Sequencer has no more events
	 */
	boolean finished;
	
	/**
	 * Output frames in one tick
	 */
	float framesPerTick;
	
	/**
	 * Output frames to process
	 */
	float pendingFrames;
	
	/**
	 * Sequencer ticks to process
	 */
	int pendingTicks;
	
	/**
	 * Sequencer position in frames
	 */
	long position;
	
	/**
	 * Processing setTime()
	 */
	boolean seeking;
	
	/**
	 * Sequencer position in ticks
	 */
	long tickNow;
	
	/** Looping is enabled. */
	boolean loopEnabled;
	
	/** Stop all notes when looping. */
	boolean loopStopAll;
	
	/**
	 * Begin MLD playback. Instances of a {@code Sampler} are used in
	 * conjunction with the given sampling rate to render the sequence to a
	 * sample buffer.
	 *
	 * @param mld The MLD sequence to play.
	 * @param sampler A {@code Sampler} from which instances will be taken to
	 * generate output.
	 * @param sampleRate The samples per second of the output.
	 * @throws NullPointerException if {@code mld} or {@code sampler} is
	 * {@code null}.
	 * @throws IllegalArgumentException if {@code sampleRate} is a
	 * non-number or is less than or equal to zero.
	 * @see MLD
	 * @see SamplerProvider
	 */
	@SquirrelJMEVendorApi
	public MLDPlayer(MLD mld, SamplerProvider sampler, float sampleRate)
	{
		
		// Error checking
		if (mld == null)
			throw new NullPointerException("An MLD is required.");
		if (sampler == null)
			throw new NullPointerException("A sampler is required.");
		if (Float.isInfinite(sampleRate) || sampleRate <= 0.0f)
			throw new IllegalArgumentException("Invalid sampling rate.");
		
		this.channels = new MLDChannel[16];
		this.events = new ArrayList<>();
		this.evtKeys = new HashSet<>();
		this.evtPlayback = false;
		this.loopEnabled = true;
		this.loopStopAll = true;
		this.mld = mld;
		this.sampler = sampler.instance(sampleRate);
		this.sampleRate = sampleRate;
		this.seeking = false;
		this.tracks = new MLDPlayerTrack[mld.tracks.length];
		
		// Channels
		for (int x = 0; x < this.channels.length; x++)
		{
			MLDChannel chan = this.channels[x] = new MLDChannel();
			//  A0 .. C6
			chan.notesOn = new MLDNote[99];
			chan.notesOut = new ArrayList<>();
		}
		
		// Tracks
		for (int x = 0; x < this.tracks.length; x++)
		{
			MLDPlayerTrack track = this.tracks[x] = new MLDPlayerTrack();
			track.index = x;
			track.mld = mld.tracks[x];
		}
		
		// Prepare for playback
		this.reset();
	}
	
	/**
	 * Determine whether looping is enabled.
	 *
	 * @return {@code true} if looping is enabled.
	 * @see #setLoopEnabled(boolean)
	 */
	public boolean getLoopEnabled()
	{
		return this.loopEnabled;
	}
	
	/**
	 * Determine whether notes are stopped when looping.
	 *
	 * @return {@code true} if all notes are stopped when looping.
	 * @see #setLoopStopAll(boolean)
	 */
	public boolean getLoopStopAll()
	{
		return this.loopStopAll;
	}
	
	/**
	 * Registers a key to raise events for during rendering. Key number 0 is
	 * the note A<sub>4</sub>.
	 *
	 * @param key A key number to register.
	 * @see MLDPlayerEvent
	 * @see #getEvents()
	 */
	public void addEventKey(int key)
	{
		this.evtKeys.add(key);
	}
	
	/**
	 * Registers multiple keys to raise events for during rendering. Key
	 * number
	 * 0 is the note A<sub>4</sub>.
	 *
	 * @param keys A list of key numbers to register.
	 * @throws NullPointerException if {@code keys} is {@code null}.
	 * @see MLDPlayerEvent
	 * @see #getEvents()
	 */
	public void addEventKeys(int[] keys)
	{
		if (keys == null)
			throw new NullPointerException("Key array is required.");
		for (int key : keys)
			this.evtKeys.add(key);
	}
	
	/**
	 * Determine the total length of the sequence in seconds. Equivalent to
	 * invoking {@code getDuration(withoutLoops)} on the underlying {@code
	 * MLD}
	 * object.
	 *
	 * @param withoutLooping Whether or not to consider looping in the return
	 * value.
	 * @return If the sequence does not loop, the number of seconds in the
	 * sequence. If the sequence loops and {@code withoutLooping} is
	 * {@code false}, returns {@code Double.POSITIVE_INFINITY}. If the
	 * sequence
	 * loops and {@code withoutLooping} is {@code true}, returns the number of
	 * seconds in the sequence up until the first loop occurs.
	 * @see MLD#getDuration(boolean)
	 */
	@SquirrelJMEVendorApi
	public double getDuration(boolean withoutLooping)
	{
		return this.mld.getDuration(withoutLooping);
	}
	
	
	/**
	 * Retrieve and acknowledge all pending events. If this method is not
	 * called, events will remain in the queue and prevent samples from being
	 * rendered.
	 *
	 * @return An array of all pending events, now acknowledged.
	 * @see MLDPlayerEvent
	 * @see #addEventKey(int)
	 * @see #addEventKeys(int[])
	 * @see #setPlaybackEventsEnabled(boolean)
	 */
	@SquirrelJMEVendorApi
	public MLDPlayerEvent[] getEvents()
	{
		MLDPlayerEvent[] ret = this.events.toArray(
			new MLDPlayerEvent[this.events.size()]);
		this.events.clear();
		return ret;
	}
	
	
	/**
	 * Retrieve the current playback position in the sequence. The range of
	 * values represents the start of the sequence at 0.0 and either the
	 * end of
	 * the sequence or the point where looping occurs at 1.0.
	 *
	 * @return The proportion of the total sequence for the current playback
	 * position.
	 */
	public double getPosition()
	{
		return (double)this.tickNow / this.mld.tickEnd;
	}
	
	/**
	 * Retrieve the total number of seconds played back so far.
	 *
	 * @return The number of seconds processed, relative to the start of the
	 * sequence.
	 * @see #setTime(double)
	 * @see MLD#getDuration(boolean)
	 */
	@SquirrelJMEVendorApi
	public double getTime()
	{
		return (double)this.position / this.sampleRate;
	}
	
	/**
	 * Determine whether playback has completed. The sequence is considered
	 * finished when all of its events have been processed and the last note
	 * has stopped generating samples.
	 *
	 * @return {@code true} if all playback has completed.
	 */
	public boolean isFinished()
	{
		if (!this.sampler.isFinished())
			return false;
		for (MLDPlayerTrack track : this.tracks)
		{
			if (!track.finished)
				return false;
		}
		return true;
	}
	
	/**
	 * Unregisters a keys from raising events during rendering.
	 *
	 * @param key A key number to unregister.
	 * @see MLDPlayerEvent
	 * @see #getEvents()
	 */
	public void removeEventKey(int key)
	{
		this.evtKeys.remove(key);
	}
	
	/**
	 * Unregisters multiple keys from raising events during rendering.
	 *
	 * @param keys A list of key numbers to unregister.
	 * @throws NullPointerException if {@code keys} is {@code null}.
	 * @see MLDPlayerEvent
	 * @see #getEvents()
	 */
	public void removeEventKeys(int[] keys)
	{
		if (keys == null)
			throw new NullPointerException("Key array is required.");
		for (int key : keys)
			this.evtKeys.remove(key);
	}
	
	/**
	 * Generate output samples. This method is equivalent to
	 * {@code render(samples, offset, frames, 1.0f, 1.0f, true, true)}.<br
	 * ><br>
	 * For information regarding the operations of this method, see
	 * {@link Sampler#render(float[], int, int, float, float, boolean, boolean)}.
	 *
	 * @param samples Output sample buffer.
	 * @param offset Index in {@code samples} of the first audio frame to
	 * output.
	 * @param frames The number of audio frames to output.
	 * @return The number of samples generated, or -1 if playback has
	 * finished.
	 * May be less than {@code frames} if playback of the underlying sequence
	 * completes before all frames have been processed.
	 * @throws NullPointerException if {@code samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code offset} is
	 * negative, or if {@code offset + frames * 2 > samples.length}.
	 * @throws IllegalArgumentException if {@code frames} is negative.
	 * @see #render(float[], int, int, float, float, boolean, boolean)
	 * @see Sampler#render(float[], int, int, float, float, boolean, boolean)
	 */
	public int render(float[] samples, int offset, int frames)
	{
		return this.render(samples, offset, frames, 1.0f, 1.0f, true, true);
	}
	
	/**
	 * Generate output samples. This method is equivalent to
	 * {@code render(samples, offset, frames, amplitude, amplitude,
	 * true, true)}.<br><br>
	 * For information regarding the operations of this method, see
	 * {@link Sampler#render(float[], int, int, float, float, boolean, boolean)}.
	 *
	 * @param samples Output sample buffer.
	 * @param offset Index in {@code samples} of the first audio frame to
	 * output.
	 * @param frames The number of audio frames to output.
	 * @param amplitude A multiplier that is applied to all samples
	 * generated.
	 * @return The number of samples generated, or -1 if playback has
	 * finished.
	 * May be less than {@code frames} if playback of the underlying sequence
	 * completes before all frames have been processed.
	 * @throws NullPointerException if {@code samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code offset} is
	 * negative, or if {@code offset + frames * 2 > samples.length}.
	 * @throws IllegalArgumentException if {@code frames} is negative, or if
	 * {@code amplitude} is a non-number or is negative.
	 * @see #render(float[], int, int, float, float, boolean, boolean)
	 * @see Sampler#render(float[], int, int, float, float, boolean, boolean)
	 */
	public int render(float[] samples, int offset, int frames,
		float amplitude)
	{
		return this.render(samples, offset, frames, amplitude, amplitude,
			true,
			true);
	}
	
	/**
	 * Generate output samples. This method is equivalent to
	 * {@code render(samples, offset, frames, left, right, true, true)}.
	 * <br><br>
	 * For information regarding the operations of this method, see
	 * {@link Sampler#render(float[], int, int, float, float, boolean, boolean)}.
	 *
	 * @param samples Output sample buffer.
	 * @param offset Index in {@code samples} of the first audio frame to
	 * output.
	 * @param frames The number of audio frames to output.
	 * @param left A multiplier that is applied to all left-stereo samples
	 * generated.
	 * @param right A multiplier that is applied to all right-stereo samples
	 * generated.
	 * @return The number of samples generated, or -1 if playback has
	 * finished.
	 * May be less than {@code frames} if playback of the underlying sequence
	 * completes before all frames have been processed.
	 * @throws NullPointerException if {@code samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code offset} is
	 * negative, or if {@code offset + frames * 2 > samples.length}.
	 * @throws IllegalArgumentException if {@code frames} is negative, or if
	 * {@code left} or {@code right} is a non-number or is negative.
	 * @see #render(float[], int, int, float, float, boolean, boolean)
	 * @see Sampler#render(float[], int, int, float, float, boolean, boolean)
	 */
	public int render(float[] samples, int offset, int frames, float left,
		float right)
	{
		return this.render(samples, offset, frames, left, right, true, true);
	}
	
	/**
	 * Generate output samples. <br><br>
	 * For information regarding the operations of this method, see
	 * {@link Sampler#render(float[], int, int, float, float, boolean, boolean)}.
	 * <br><br>
	 * If an event is raised during playback, rendering will stop and return
	 * before generating any more samples. When this happens, the return value
	 * may be less than {@code frames}. {@link #getEvents()} should be called
	 * after every call to {@code render()} while events are enabled.
	 *
	 * @param samples Output sample buffer.
	 * @param offset Index in {@code samples} of the first audio frame to
	 * output.
	 * @param frames The number of audio frames to output.
	 * @param left A multiplier that is applied to all left-stereo samples
	 * generated.
	 * @param right A multiplier that is applied to all right-stereo samples
	 * generated.
	 * @param erase Replace the buffer contents when {@code true}, or add
	 * to them when {@code false}
	 * @param clamp Specifies whether to restrict the sample buffer values
	 * to -1.0f to +1.0f inclusive.
	 * @return The number of samples generated, or -1 if playback has
	 * finished.
	 * May be less than {@code frames} if playback of the underlying sequence
	 * completes before all frames have been processed.
	 * @throws NullPointerException if {@code samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code offset} is
	 * negative, or if {@code offset + frames * 2 > samples.length}.
	 * @throws IllegalArgumentException if {@code frames} is negative, or if
	 * {@code left} or {@code right} is a non-number or is negative.
	 * @see Sampler#render(float[], int, int, float, float, boolean, boolean)
	 * @see #getEvents()
	 * @see #render(float[], int, int)
	 * @see #render(float[], int, int, float)
	 * @see #render(float[], int, int, float, float)
	 */
	@SquirrelJMEVendorApi
	public int render(float[] samples, int offset, int frames, float left,
		float right, boolean erase, boolean clamp)
	{
		//  Total frames output so far
		int ret = 0;
		
		// Error checking
		if (!this.seeking)
		{
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
		}
		
		// Sequencer is not playing
		if (this.finished)
			this.pendingFrames = frames;
		
		// Process all output frames
		while (frames > 0)
		{
			
			// Events are pending
			if (this.events.size() != 0)
				return ret;
			
			// Process output frames
			while (this.pendingFrames > 0)
			{
				
				// Render the samples
				int f = Math.min(frames, (int)Math.floor(this.pendingFrames));
				if (!this.seeking)
					this.sampler.render(samples, offset, f, left, right,
						erase,
						clamp);
				
				// State management
				frames -= f;
				offset += f * 2;
				this.pendingFrames -= f;
				this.position += f;
				ret += f;
				
				// All output frames have been processed
				if (frames == 0)
					return this.finished ? -1 : ret;
			}
			
			// Process event ticks
			if (this.pendingTicks > 0)
			{
				
				// Sequencer
				this.tickNow += this.pendingTicks;
				
				// Notes
				for (MLDChannel chan : this.channels)
					for (MLDNote note : chan.notesOut)
						note.gateTime -= this.pendingTicks;
				
				// Tracks
				for (MLDPlayerTrack track : this.tracks)
					this.process(track, this.pendingTicks);
				
				// Remove expired notes
				for (MLDChannel chan : this.channels)
					for (int x = 0; x < chan.notesOut.size(); x++)
					{
						MLDNote note = chan.notesOut.get(x);
						if (note.gateTime != 0)
							continue;
						this.sampler.keyOff(note.channel, note.key);
						chan.notesOut.remove(x--);
						chan.notesOn[MLDPlayer.A4 + note.key] = null;
					}
				
			}
			
			// Determine how many ticks and frames can be processed next
			int untilTrack = this.untilTrack();
			if (untilTrack == -1)
			{
				this.finished = true;
				return ret;
			}
			int untilNote = this.untilNote();
			this.pendingTicks = untilNote == -1 ? untilTrack : Math.min(
				untilTrack, untilNote);
			this.pendingFrames += (float)Math.floor(
				this.pendingTicks * this.framesPerTick);
		}
		
		return ret;
	}
	
	/**
	 * Specify whether or not to raise playback events. Playback events
	 * include
	 * {@code EVENT_END} and {@code EVENT_LOOP}.
	 *
	 * @param enabled Whether or not playback events can be raised during
	 * rendering.
	 * @see MLDPlayerEvent
	 * @see #getEvents()
	 */
	@SquirrelJMEVendorApi
	public void setPlaybackEventsEnabled(boolean enabled)
	{
		this.evtPlayback = enabled;
	}
	
	/**
	 * Specify the playback position of the sequence in seconds. The resulting
	 * position in the sequence will be the earliest internal time at or after
	 * {@code seconds}.<br><br>
	 * If the end of the sequence is encountered during seeking, this method
	 * will return {@code true}. When this happens, it is possible that the
	 * position in the sequence retrieved by subsequent calls to
	 * {@code getTime()} may be less than {@code seconds}.
	 *
	 * @param seconds The number of seconds from the beginning of the
	 * sequence.
	 * @return {@code true} if the end of the sequence was encountered during
	 * the operation.
	 * @throws IllegalArgumentException if {@code seconds} is a non-number
	 * or is negative.
	 * @see #getTime()
	 * @see MLD#getDuration(boolean)
	 */
	@SquirrelJMEVendorApi
	public boolean setTime(double seconds)
	{
		
		// Error checking
		if (Double.isInfinite(seconds) || seconds < 0)
			throw new IllegalArgumentException("Invalid seconds.");
		
		// Compute the target number of frames
		long target = (long)Math.ceil(seconds * this.sampleRate);
		
		// Already at the target
		if (target == this.position)
			return this.isFinished();
		
		// Target is earlier than the current frame
		if (target < this.position)
			this.reset();
		
		// Seek forward to the target time
		this.seeking = true;
		this.render(null, 0, (int)(target - this.position), 0.0f, 0.0f, false,
			false);
		this.seeking = false;
		return this.isFinished();
	}
	
	
	/**
	 * bank-change
	 */
	void evtBankChange(MLDPlayerTrack track, MLDEvent event)
	{
		this.sampler.bankChange(event.channel, event.bank);
		this.setTrackOffset(track, track.offset + 1);
	}
	
	/**
	 * cuepoint
	 */
	void evtCuepoint(MLDPlayerTrack track, MLDEvent event)
	{
		// Common processing
		this.setTrackOffset(track, track.offset + 1);
		
		if (event.cuepoint == MLD.CUEPOINT_START)
		{
			for (MLDPlayerTrack t : this.tracks)
				t.cuepoint = t.offset;

			return;
		}

		if (event.cuepoint == MLD.CUEPOINT_END && this.tracks[0].cuepoint != -1)
		{
			// Reached CUEPOINT_END, stop playback.
			this.finished = true;
		}
	}
	
	/**
	 * drum-enable
	 */
	void evtDrumEnable(MLDPlayerTrack track, MLDEvent event)
	{
		this.sampler.drumEnable(event.channel, event.enable);
		this.setTrackOffset(track, track.offset + 1);
	}
	
	/**
	 * end-of-track
	 */
	void evtEndOfTrack(MLDPlayerTrack track, MLDEvent event)
	{
		track.finished = true;
	}
	
	/**
	 * ext-B event
	 */
	void evtExtB(MLDPlayerTrack track, MLDEvent e)
	{
		switch (e.id)
		{
			case MLD.EVENT_BANK_CHANGE:
				this.evtBankChange(track, e);
				break;
			case MLD.EVENT_CUEPOINT:
				this.evtCuepoint(track, e);
				break;
			case MLD.EVENT_END_OF_TRACK:
				this.evtEndOfTrack(track, e);
				break;
			case MLD.EVENT_MASTER_VOLUME:
				this.evtMasterVolume(track, e);
				break;
			case MLD.EVENT_MASTER_TUNE:
				this.evtMasterTune(track, e);
				break;
			case MLD.EVENT_PANPOT:
				this.evtPanPot(track, e);
				break;
			case MLD.EVENT_PITCHBEND:
				this.evtPitchBend(track, e);
				break;
			case MLD.EVENT_PITCHBEND_RANGE:
				this.evtPitchRange(track, e);
				break;
			case MLD.EVENT_PROGRAM_CHANGE:
				this.evtProgramChange(track, e);
				break;
			case MLD.EVENT_TIMEBASE_TEMPO:
				this.evtTimebaseTempo(track, e);
				break;
			case MLD.EVENT_VOLUME:
				this.evtVolume(track, e);
				break;
			case MLD.EVENT_X_DRUM_ENABLE:
				this.evtDrumEnable(track, e);
				break;
			
			// Not implemented
			//case EVENT_JUMP:
			//case EVENT_CHANNEL_ASSIGN:
			//case EVENT_NOP:
			//case EVENT_PART_CONFIGURATION:
			//case EVENT_PAUSE:
			//case EVENT_RESET:
			//case EVENT_STOP:
			//case EVENT_WAVE_CHANNEL_VOLUME:
			//case EVENT_WAVE_CHANNEL_PANPOT:
			
			// Unrecognized events
			default:
				this.setTrackOffset(track, track.offset + 1);
		}
	}
	
	/**
	 * ext-info event
	 */
	void evtExtInfo(MLDPlayerTrack track, MLDEvent e)
	{
		this.sampler.sysEx(e.data);
		this.setTrackOffset(track, track.offset + 1);
	}
	
	/**
	 * master-tune
	 */
	void evtMasterTune(MLDPlayerTrack track, MLDEvent event)
	{
		this.sampler.masterTune(event.semitones);
		this.setTrackOffset(track, track.offset + 1);
	}
	
	/**
	 * master-volume
	 */
	void evtMasterVolume(MLDPlayerTrack track, MLDEvent event)
	{
		this.sampler.masterVolume(event.volume);
		this.setTrackOffset(track, track.offset + 1);
	}
	
	/**
	 * note
	 */
	void evtNote(MLDPlayerTrack track, MLDEvent event)
	{
		MLDChannel chan = this.channels[event.channel];
		MLDNote note = chan.notesOn[MLDPlayer.A4 + event.key];
		
		// Common processing
		this.setTrackOffset(track, track.offset + 1);
		
		// Raise an event
		if (this.evtKeys.contains(event.key))
			this.events.add(
				new MLDPlayerEvent(this.getTime(), MLDPlayer.EVENT_KEY,
					event.key));
		
		// Velocity 0 is regarded as key-off
		if (event.velocity == 0)
		{
			this.sampler.keyOff(event.channel, event.key);
			if (note != null)
			{
				chan.notesOn[MLDPlayer.A4 + event.key] = null;
				chan.notesOut.remove(note);
			}
			return;
		}
		
		// Velocity not zero is regarded as key-on
		if (!this.seeking)
			this.sampler.keyOn(event.channel, event.key, event.velocity);
		
		// Get or create the note for this key
		if (note == null)
		{
			note = new MLDNote();
			note.channel = event.channel;
			note.key = event.key;
			chan.notesOn[MLDPlayer.A4 + event.key] = note;
			chan.notesOut.add(note);
		}
		
		// Reconfigure the note
		note.gateTime = event.gateTime;
	}
	
	/**
	 * panpot
	 */
	void evtPanPot(MLDPlayerTrack track, MLDEvent event)
	{
		this.sampler.panpot(event.channel, event.panpot);
		this.setTrackOffset(track, track.offset + 1);
	}
	
	/**
	 * pitchbend
	 */
	void evtPitchBend(MLDPlayerTrack track, MLDEvent event)
	{
		this.sampler.pitchBend(event.channel, event.semitones);
		this.setTrackOffset(track, track.offset + 1);
	}
	
	/**
	 * pitchbend-range
	 */
	void evtPitchRange(MLDPlayerTrack track, MLDEvent event)
	{
		this.sampler.pitchBendRange(event.channel, event.range);
		this.setTrackOffset(track, track.offset + 1);
	}
	
	/**
	 * program-change
	 */
	void evtProgramChange(MLDPlayerTrack track, MLDEvent event)
	{
		this.sampler.programChange(event.channel, event.program);
		this.setTrackOffset(track, track.offset + 1);
	}
	
	/**
	 * timebase-tempo
	 */
	void evtTimebaseTempo(MLDPlayerTrack track, MLDEvent event)
	{
		if (event.timebase == -1)
			return;
		float prev = this.framesPerTick;
		this.setTempo(event.timebase, event.tempo);
		this.pendingFrames = this.pendingFrames * this.framesPerTick / prev;
		this.setTrackOffset(track, track.offset + 1);
	}
	
	/**
	 * volume
	 */
	void evtVolume(MLDPlayerTrack track, MLDEvent event)
	{
		this.sampler.volume(event.channel, event.volume);
		this.setTrackOffset(track, track.offset + 1);
	}
	
	
	/**
	 * Process events on a track
	 */
	void process(MLDPlayerTrack track, int ticks)
	{
		
		// The track has finished
		if (track.finished)
			return;
		
		// Update state
		track.ticks -= ticks;
		if (track.ticks > 0)
			return;
		
		// Process all events this tick
		while (track.ticks == 0)
		{
			MLDEvent event = track.mld.get(track.offset);
			
			// Process the event
			switch (event.type)
			{
				case MLD.EVENT_TYPE_NOTE:
					this.evtNote(track, event);
					break;
				case MLD.EVENT_TYPE_EXT_B:
					this.evtExtB(track, event);
					break;
				case MLD.EVENT_TYPE_EXT_INFO:
					this.evtExtInfo(track, event);
					break;
				default:
					this.setTrackOffset(track, track.offset + 1);
			}
			
			// Stop processing events
			if (track.finished)
				return;
			
			// Schedule the next event
			track.ticks = track.mld.get(track.offset).delta;
		}
		
	}
	
	/**
	 * Initialize state in preparation for playback. All notes are stopped and
	 * all sequencer state is reset to the beginning of the sequence.
	 */
	@SquirrelJMEVendorApi
	public void reset()
	{
		// Instance fields
		this.pendingFrames = 0;
		this.pendingTicks = 0;
		this.position = 0;
		this.tickNow = 0;
		this.setTempo(48, 125);
		this.events.clear();
		
		// Initialize sampler
		this.sampler.reset();
		
		// Channels
		for (MLDChannel chan : this.channels)
		{
			Arrays.fill(chan.notesOn, null);
			chan.notesOut.clear();
		}
		
		// Tracks
		for (MLDPlayerTrack track : this.tracks)
		{
			track.cuepoint = -1;
			track.offset = track.mld.cue;
			track.ticks = 0;
			track.finished = track.offset >= track.mld.size();
		}
		
		// Initialize playback
		this.finished = true;
		for (MLDPlayerTrack track : this.tracks)
		{
			this.process(track, 0);
			this.finished = this.finished && track.finished;
		}
		
	}
	
	/**
	 * Specify whether to enable looping. When disabled, loop points
	 * defined in
	 * the sequence data will not be processed.
	 *
	 * @param enabled If {@code true}, looping will be enabled.
	 * @return the value of {@code enabled}
	 * @see #getLoopEnabled()
	 */
	public boolean setLoopEnabled(boolean enabled)
	{
		return this.loopEnabled = enabled;
	}
	
	/**
	 * Specify whether to stop all notes when looping. If notes are not
	 * stopped, it is possible for adjustments to volume or pitch-bend to
	 * affect ongoing notes in undesirable ways. If notes <i>are</i> stopped,
	 * it is possible for ongoing notes to be truncated in undesirable ways.
	 *
	 * @param stopAll If {@code true}, all notes will be stopped when looping.
	 * @return the value of {@code stopAll}
	 * @see #getLoopStopAll()
	 */
	public boolean setLoopStopAll(boolean stopAll)
	{
		return this.loopStopAll = stopAll;
	}
	
	/**
	 * Compute the number of output frames in one event tick
	 */
	void setTempo(int timebase, int tempo)
	{
		this.framesPerTick = (60 * this.sampleRate) / (timebase * tempo);
	}
	
	/**
	 * Specify the event offset of a track
	 */
	void setTrackOffset(MLDPlayerTrack track, int offset)
	{
		
		// Configure the track
		track.offset = offset;
		track.finished = offset >= track.mld.size();
		
		// Raise an event
		if (!track.finished || !this.evtPlayback)
			return;
		boolean finished = true;
		for (MLDPlayerTrack other : this.tracks)
			finished = finished && other.finished;
		if (finished)
			this.events.add(
				new MLDPlayerEvent(this.getTime(), MLDPlayer.EVENT_END,
					0));
	}
	
	/**
	 * Determine how many ticks can be processed until a note expires
	 */
	int untilNote()
	{
		int ret = -1;
		for (MLDChannel chan : this.channels)
			for (MLDNote note : chan.notesOut)
			{
				if (ret == -1 || note.gateTime < ret)
					ret = note.gateTime;
			}
		return ret;
	}
	
	/**
	 * Determine how many ticks can be processed until the next event
	 */
	int untilTrack()
	{
		int ret = -1;
		for (MLDPlayerTrack track : this.tracks)
		{
			if (track.finished)
				continue;
			if (ret == -1 || track.ticks < ret)
				ret = track.ticks;
		}
		return ret;
	}
	
	
}
