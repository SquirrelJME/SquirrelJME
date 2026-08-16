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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

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
	/** Key index bias. */
	@SquirrelJMEVendorApi
	public static final int A4 = 48;
	
	/** Sample generator. */
	@SquirrelJMEVendorApi
	public final Sampler sampler;
	
	/** Playback channels. */
	@SquirrelJMEVendorApi
	final __MLDChannel__[] _channels;
	
	/** Pending events. */
	@SquirrelJMEVendorApi
	final ArrayList<MLDPlayerEvent> _events;
	
	/** Key events enabled by key. */
	final HashSet<Integer> _evtKeys;
	
	/** Sequence resource. */
	final MLD _mld;
	
	/** Output sampling rate. */
	final float _sampleRate;
	
	/** Sequencer state. */
	@SquirrelJMEVendorApi
	final __MLDPlayerTrack__[] _tracks;
	
	/** Playback events are enabled. */
	@SquirrelJMEVendorApi
	boolean _evtPlayback;
	
	/** Sequencer has no more events. */
	@SquirrelJMEVendorApi
	boolean _finished;
	
	/** Output frames in one tick. */
	@SquirrelJMEVendorApi
	float _framesPerTick;
	
	/** Looping is enabled. */
	@SquirrelJMEVendorApi
	boolean _loopEnabled;
	
	/** Stop all notes when looping. */
	@SquirrelJMEVendorApi
	boolean _loopStopAll;
	
	/** Output frames to process. */
	@SquirrelJMEVendorApi
	float _pendingFrames;
	
	/** Sequencer ticks to process. */
	@SquirrelJMEVendorApi
	int _pendingTicks;
	
	/** Sequencer position in frames. */
	@SquirrelJMEVendorApi
	long _position;
	
	/** Processing setTime(). */
	@SquirrelJMEVendorApi
	boolean _seeking;
	
	/** Sequencer position in ticks. */
	@SquirrelJMEVendorApi
	long _tickNow;
	
	/**
	 * Begin MLD playback. Instances of a {@code Sampler} are used in
	 * conjunction with the given sampling rate to render the sequence to a
	 * sample buffer.
	 *
	 * @param __mld The MLD sequence to play.
	 * @param __sampler A {@code Sampler} from which instances will be taken to
	 * generate output.
	 * @param __sampleRate The samples per second of the output.
	 * @throws NullPointerException if {@code mld} or {@code sampler} is
	 * {@code null}.
	 * @throws IllegalArgumentException if {@code sampleRate} is a
	 * non-number or is less than or equal to zero.
	 * @see MLD
	 * @see SamplerProvider
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public MLDPlayer(@NotNull MLD __mld, @NotNull SamplerProvider __sampler,
		float __sampleRate)
		throws IllegalArgumentException, NullPointerException
	{
		// Error checking
		if (__mld == null)
			throw new NullPointerException("An MLD is required.");
		if (__sampler == null)
			throw new NullPointerException("A sampler is required.");

		if (Float.isInfinite(__sampleRate) || __sampleRate <= 0.0f)
			throw new IllegalArgumentException("Invalid sampling rate.");
		
		this._channels = new __MLDChannel__[16];
		this._events = new ArrayList<>();
		this._evtKeys = new HashSet<>();
		this._evtPlayback = false;
		this._loopEnabled = true;
		this._loopStopAll = true;
		this._mld = __mld;
		this.sampler = __sampler.instance(__sampleRate);
		this._sampleRate = __sampleRate;
		this._seeking = false;
		this._tracks = new __MLDPlayerTrack__[__mld._tracks.length];
		
		// Channels
		for (int x = 0; x < this._channels.length; x++)
		{
			__MLDChannel__ chan = this._channels[x] = new __MLDChannel__();
			//  A0 .. C6
			chan._notesOn = new __MLDNote__[99];
			chan._notesOut = new ArrayList<>();
		}
		
		// Tracks
		for (int x = 0; x < this._tracks.length; x++)
		{
			__MLDPlayerTrack__ track = this._tracks[x] = new __MLDPlayerTrack__();
			track._index = x;
			track._mld = _mld._tracks[x];
		}
		
		// Prepare for playback
		this.reset();
	}
	
	/**
	 * Determine the total length of the sequence in seconds. Equivalent to
	 * invoking {@code getDuration(withoutLoops)} on the underlying {@code
	 * MLD}
	 * object.
	 *
	 * @param __withoutLooping Whether or not to consider looping in the return
	 * value.
	 * @return If the sequence does not loop, the number of seconds in the
	 * sequence. If the sequence loops and {@code withoutLooping} is
	 * {@code false}, returns {@code Double.POSITIVE_INFINITY}. If the
	 * sequence
	 * loops and {@code withoutLooping} is {@code true}, returns the number of
	 * seconds in the sequence up until the first loop occurs.
	 * @see MLD#getDuration(boolean)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public double getDuration(boolean __withoutLooping)
	{
		return this._mld.getDuration(__withoutLooping);
	}
	
	/**
	 * Retrieve and acknowledge all pending events. If this method is not
	 * called, events will remain in the queue and prevent samples from being
	 * rendered.
	 *
	 * @return An array of all pending events, now acknowledged.
	 * @see MLDPlayerEvent
	 * @see #__addEventKey(int)
	 * @see #__addEventKeys(int[])
	 * @see #setPlaybackEventsEnabled(boolean)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public MLDPlayerEvent[] getEvents()
	{
		MLDPlayerEvent[] ret = this._events.toArray(
			new MLDPlayerEvent[this._events.size()]);
		this._events.clear();
		return ret;
	}
	
	/**
	 * Determine whether looping is enabled.
	 *
	 * @return {@code true} if looping is enabled.
	 * @see #setLoopEnabled(boolean)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public boolean getLoopEnabled()
	{
		return this._loopEnabled;
	}
	
	/**
	 * Determine whether notes are stopped when looping.
	 *
	 * @return {@code true} if all notes are stopped when looping.
	 * @see #setLoopStopAll(boolean)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public boolean getLoopStopAll()
	{
		return this._loopStopAll;
	}
	
	/**
	 * Retrieve the current playback position in the sequence. The range of
	 * values represents the start of the sequence at 0.0 and either the
	 * end of
	 * the sequence or the point where looping occurs at 1.0.
	 *
	 * @return The proportion of the total sequence for the current playback
	 * position.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public double getPosition()
	{
		return (double)this._tickNow / this._mld._tickEnd;
	}
	
	/**
	 * Retrieve the total number of seconds played back so far.
	 *
	 * @return The number of seconds processed, relative to the start of the
	 * sequence.
	 * @see #setTime(double)
	 * @see MLD#getDuration(boolean)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public double getTime()
	{
		return (double)this._position / this._sampleRate;
	}
	
	/**
	 * Determine whether playback has completed. The sequence is considered
	 * finished when all of its events have been processed and the last note
	 * has stopped generating samples.
	 *
	 * @return {@code true} if all playback has completed.
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public boolean isFinished()
	{
		if (!this.sampler.isFinished())
			return false;
		for (__MLDPlayerTrack__ track : this._tracks)
		{
			if (!track._finished)
				return false;
		}
		return true;
	}
	
	/**
	 * Generate output samples. This method is equivalent to
	 * {@code render(__samples, __offset, __frames, 1.0f, 1.0f,
	 * true, true)}.<br><br>
	 * For information regarding the operations of this method, see
	 * {@link Sampler#render(float[], int, int, float, float, boolean, boolean)}.
	 *
	 * @param __samples Output sample buffer.
	 * @param __offset Index in {@code __samples} of the first audio frame to
	 * output.
	 * @param __frames The number of audio frames to output.
	 * @return The number of samples generated, or -1 if playback has
	 * finished.
	 * May be less than {@code __frames} if playback of the underlying sequence
	 * completes before all frames have been processed.
	 * @throws NullPointerException if {@code __samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code __offset} is
	 * negative, or if {@code __offset + __frames * 2 > __samples.length}.
	 * @throws IllegalArgumentException if {@code __frames} is negative.
	 * @see #render(float[], int, int, float, float, boolean, boolean)
	 * @see Sampler#render(float[], int, int, float, float, boolean, boolean)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public int render(@NotNull float[] __samples,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frames)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
		NullPointerException
	{
		return this.render(__samples, __offset, __frames, 1.0f, 1.0f, true,
			true);
	}
	
	/**
	 * Generate output samples. This method is equivalent to
	 * {@code render(__samples, __offset, __frames, __amplitude, __amplitude,
	 * true, true)}.<br><br>
	 * For information regarding the operations of this method, see
	 * {@link Sampler#render(float[], int, int, float, float, boolean, boolean)}.
	 *
	 * @param __samples Output sample buffer.
	 * @param __offset Index in {@code __samples} of the first audio frame to
	 * output.
	 * @param __frames The number of audio frames to output.
	 * @param __amplitude A multiplier that is applied to all samples
	 * generated.
	 * @return The number of samples generated, or -1 if playback has
	 * finished.
	 * May be less than {@code __frames} if playback of the underlying sequence
	 * completes before all frames have been processed.
	 * @throws NullPointerException if {@code __samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code __offset} is
	 * negative, or if {@code __offset + __frames * 2 > __samples.length}.
	 * @throws IllegalArgumentException if {@code __frames} is negative, or if
	 * {@code __amplitude} is a non-number or is negative.
	 * @see #render(float[], int, int, float, float, boolean, boolean)
	 * @see Sampler#render(float[], int, int, float, float, boolean, boolean)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public int render(@NotNull float[] __samples,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frames,
		float __amplitude)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
		NullPointerException
	{
		return this.render(__samples, __offset, __frames, __amplitude,
			__amplitude, true, true);
	}
	
	/**
	 * Generate output samples. This method is equivalent to
	 * {@code render(__samples, __offset, __frames, __left, __right,
	 * true, true)}.<br><br>
	 * For information regarding the operations of this method, see
	 * {@link Sampler#render(float[], int, int, float, float, boolean, boolean)}.
	 *
	 * @param __samples Output sample buffer.
	 * @param __offset Index in {@code __samples} of the first audio frame to
	 * output.
	 * @param __frames The number of audio frames to output.
	 * @param __left A multiplier that is applied to all left-stereo samples
	 * generated.
	 * @param __right A multiplier that is applied to all right-stereo samples
	 * generated.
	 * @return The number of samples generated, or -1 if playback has
	 * finished.
	 * May be less than {@code __frames} if playback of the underlying sequence
	 * completes before all frames have been processed.
	 * @throws NullPointerException if {@code __samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code __offset} is
	 * negative, or if {@code __offset + __frames * 2 > __samples.length}.
	 * @throws IllegalArgumentException if {@code __frames} is negative, or if
	 * {@code __left} or {@code __right} is a non-number or is negative.
	 * @see #render(float[], int, int, float, float, boolean, boolean)
	 * @see Sampler#render(float[], int, int, float, float, boolean, boolean)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public int render(@NotNull float[] __samples,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frames,
		float __left, float __right)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
		NullPointerException
	{
		return this.render(__samples, __offset, __frames, __left, __right,
			true, true);
	}
	
	/**
	 * Generate output samples. <br><br>
	 * For information regarding the operations of this method, see
	 * {@link Sampler#render(float[], int, int, float, float, boolean, boolean)}.
	 * <br><br>
	 * If an event is raised during playback, rendering will stop and return
	 * before generating any more samples. When this happens, the return value
	 * may be less than {@code __frames}. {@link #getEvents()} should be called
	 * after every call to {@code render()} while events are enabled.
	 *
	 * @param __samples Output sample buffer.
	 * @param __offset Index in {@code __samples} of the first audio frame to
	 * output.
	 * @param __frames The number of audio frames to output.
	 * @param __left A multiplier that is applied to all left-stereo samples
	 * generated.
	 * @param __right A multiplier that is applied to all right-stereo samples
	 * generated.
	 * @param __erase Replace the buffer contents when {@code true}, or add
	 * to them when {@code false}
	 * @param __clamp Specifies whether to restrict the sample buffer values
	 * to -1.0f to +1.0f inclusive.
	 * @return The number of samples generated, or -1 if playback has
	 * finished.
	 * May be less than {@code __frames} if playback of the underlying sequence
	 * completes before all frames have been processed.
	 * @throws NullPointerException if {@code __samples} is {@code null}.
	 * @throws ArrayIndexOutOfBoundsException if {@code __offset} is
	 * negative, or if {@code __offset + __frames * 2 > __samples.length}.
	 * @throws IllegalArgumentException if {@code __frames} is negative, or if
	 * {@code __left} or {@code __right} is a non-number or is negative.
	 * @see Sampler#render(float[], int, int, float, float, boolean, boolean)
	 * @see #getEvents()
	 * @see #render(float[], int, int)
	 * @see #render(float[], int, int, float)
	 * @see #render(float[], int, int, float, float)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public int render(@NotNull float[] __samples,
		@Range(from = 0, to = Integer.MAX_VALUE) int __offset,
		@Range(from = 0, to = Integer.MAX_VALUE) int __frames,
		float __left, float __right, boolean __erase, boolean __clamp)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
		NullPointerException
	{
		//  Total frames output so far
		int ret = 0;
		
		// Error checking
		if (!this._seeking)
		{
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
		}
		
		// Sequencer is not playing
		if (this._finished)
			this._pendingFrames = __frames;
		
		// Process all output frames
		while (__frames > 0)
		{
			
			// Events are pending
			if (this._events.size() != 0)
				return ret;
			
			// Process output frames
			while (this._pendingFrames > 0)
			{
				
				// Render the samples
				int f = Math.min(__frames, (int)Math.floor(this._pendingFrames));
				if (!this._seeking)
					this.sampler.render(__samples, __offset, f, __left, __right,
						__erase, __clamp);
				
				// State management
				__frames -= f;
				__offset += f * 2;
				this._pendingFrames -= f;
				this._position += f;
				ret += f;
				
				// All output frames have been processed
				if (__frames == 0)
					return this._finished ? -1 : ret;
			}
			
			// Process event ticks
			if (this._pendingTicks > 0)
			{
				
				// Sequencer
				this._tickNow += this._pendingTicks;
				
				// Notes
				for (__MLDChannel__ chan : this._channels)
					for (__MLDNote__ note : chan._notesOut)
						note._gateTime -= this._pendingTicks;
				
				// Tracks
				for (__MLDPlayerTrack__ track : this._tracks)
					this.__process(track, this._pendingTicks);
				
				// Remove expired notes
				for (__MLDChannel__ chan : this._channels)
					for (int x = 0; x < chan._notesOut.size(); x++)
					{
						__MLDNote__ note = chan._notesOut.get(x);
						if (note._gateTime != 0)
							continue;
						this.sampler.keyOff(note._channel, note._key);
						chan._notesOut.remove(x--);
						chan._notesOn[MLDPlayer.A4 + note._key] = null;
					}
				
			}
			
			// Determine how many ticks and frames can be processed next
			int untilTrack = this.__untilTrack();
			if (untilTrack == -1)
			{
				this._finished = true;
				return ret;
			}
			int untilNote = this.__untilNote();
			this._pendingTicks = untilNote == -1 ? untilTrack : Math.min(
				untilTrack, untilNote);
			this._pendingFrames += (float)Math.floor(
				this._pendingTicks * this._framesPerTick);
		}
		
		return ret;
	}

	/**
	 * Initialize state in preparation for playback. All notes are stopped and
	 * all sequencer state is reset to the beginning of the sequence.
	 *
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public void reset()
	{
		// Instance fields
		this._pendingFrames = 0;
		this._pendingTicks = 0;
		this._position = 0;
		this._tickNow = 0;
		this.__setTempo(48, 125);
		this._events.clear();

		// Initialize sampler
		this.sampler.reset();

		// Channels
		for (__MLDChannel__ chan : this._channels)
		{
			Arrays.fill(chan._notesOn, null);
			chan._notesOut.clear();
		}

		// Tracks
		for (__MLDPlayerTrack__ track : this._tracks)
		{
			track._cuepoint = -1;
			track._offset = track._mld._cue;
			track._ticks = 0;
			track._finished = track._offset >= track._mld.size();
		}

		// Initialize playback
		this._finished = true;
		for (__MLDPlayerTrack__ track : this._tracks)
		{
			this.__process(track, 0);
			this._finished = this._finished && track._finished;
		}

	}

	/**
	 * Specify whether to enable looping. When disabled, loop points
	 * defined in the sequence data will not be processed.
	 *
	 * @param __enabled If {@code true}, looping will be enabled.
	 * @return the value of {@code enabled}
	 * @see #getLoopEnabled()
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public boolean setLoopEnabled(boolean __enabled)
	{
		return this._loopEnabled = __enabled;
	}

	/**
	 * Specify whether to stop all notes when looping. If notes are not
	 * stopped, it is possible for adjustments to volume or pitch-bend to
	 * affect ongoing notes in undesirable ways. If notes <i>are</i> stopped,
	 * it is possible for ongoing notes to be truncated in undesirable ways.
	 *
	 * @param __stopAll If {@code true}, all notes will be stopped when looping.
	 * @return the value of {@code stopAll}
	 * @see #getLoopStopAll()
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public boolean setLoopStopAll(boolean __stopAll)
	{
		return this._loopStopAll = __stopAll;
	}
	
	/**
	 * Specify whether or not to raise playback events. Playback events
	 * include
	 * {@link MLDPlayerEvent#EVENT_END} and {@link MLDPlayerEvent#EVENT_LOOP}.
	 *
	 * @param __enabled Whether or not playback events can be raised during
	 * rendering.
	 * @see MLDPlayerEvent
	 * @see #getEvents()
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public void setPlaybackEventsEnabled(boolean __enabled)
	{
		this._evtPlayback = __enabled;
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
	 * @param __seconds The number of seconds from the beginning of the
	 * sequence.
	 * @return {@code true} if the end of the sequence was encountered during
	 * the operation.
	 * @throws IllegalArgumentException if {@code seconds} is a non-number
	 * or is negative.
	 * @see #getTime()
	 * @see MLD#getDuration(boolean)
	 * @since 2025/05/05
	 */
	@SquirrelJMEVendorApi
	public boolean setTime(double __seconds)
	{
		// Error checking
		if (Double.isInfinite(__seconds) || __seconds < 0)
			throw new IllegalArgumentException("Invalid seconds.");
		
		// Compute the target number of frames
		long target = (long)Math.ceil(__seconds * this._sampleRate);
		
		// Already at the target
		if (target == this._position)
			return this.isFinished();
		
		// Target is earlier than the current frame
		if (target < this._position)
			this.reset();
		
		// Seek forward to the target time
		this._seeking = true;
		this.render(null, 0, (int)(target - this._position), 0.0f, 0.0f, false,
			false);
		this._seeking = false;
		return this.isFinished();
	}

	/**
	 * Registers a key to raise events for during rendering. Key number 0 is
	 * the note A<sub>4</sub>.
	 *
	 * @param __key A key number to register.
	 * @see MLDPlayerEvent
	 * @see #getEvents()
	 * @since 2025/05/05
	 */
	private void __addEventKey(int __key)
	{
		this._evtKeys.add(__key);
	}
	
	/**
	 * Registers multiple keys to raise events for during rendering. Key
	 * number
	 * 0 is the note A<sub>4</sub>.
	 *
	 * @param __keys A list of key numbers to register.
	 * @throws NullPointerException if {@code keys} is {@code null}.
	 * @see MLDPlayerEvent
	 * @see #getEvents()
	 * @since 2025/05/05
	 */
	private void __addEventKeys(@NotNull int[] __keys)
	{
		if (__keys == null)
			throw new NullPointerException("Key array is required.");
		for (int key : __keys)
			this._evtKeys.add(key);
	}
	
	/**
	 * Process a Bank Change event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtBankChange(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		this.sampler.bankChange(__event._channel, __event._bank);
		this.__setTrackOffset(__track, __track._offset + 1);
	}
	
	/**
	 * Process a Cuepoint event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtCuepoint(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		// Common processing
		this.__setTrackOffset(__track, __track._offset + 1);
		
		if (__event._cuepoint == MLD.EVENT_CUEPOINT_START)
		{
			for (__MLDPlayerTrack__ t : this._tracks)
				t._cuepoint = t._offset;

			return;
		}

		if (__event._cuepoint == MLD.EVENT_CUEPOINT_END &&
			this._tracks[0]._cuepoint != -1)
		{
			// Reached CUEPOINT_END, stop playback.
			this._finished = true;
		}
	}
	
	/**
	 * Process a Drum enable event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtDrumEnable(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		this.sampler.drumEnable(__event._channel, __event._enable);
		this.__setTrackOffset(__track, __track._offset + 1);
	}
	
	/**
	 * Process an End-Of-Track event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtEndOfTrack(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		__track._finished = true;
	}
	
	/**
	 * Process an Ext-B event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtExtB(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		switch (__event._id)
		{
			case MLD.EVENT_BANK_CHANGE:
				this.__evtBankChange(__track, __event);
				break;
			case MLD.EVENT_CUEPOINT:
				this.__evtCuepoint(__track, __event);
				break;
			case MLD.EVENT_END_OF_TRACK:
				this.__evtEndOfTrack(__track, __event);
				break;
			case MLD.EVENT_MASTER_VOLUME:
				this.__evtMasterVolume(__track, __event);
				break;
			case MLD.EVENT_MASTER_TUNE:
				this.__evtMasterTune(__track, __event);
				break;
			case MLD.EVENT_PANPOT:
				this.__evtPanPot(__track, __event);
				break;
			case MLD.EVENT_PITCHBEND:
				this.__evtPitchBend(__track, __event);
				break;
			case MLD.EVENT_PITCHBEND_RANGE:
				this.__evtPitchRange(__track, __event);
				break;
			case MLD.EVENT_PROGRAM_CHANGE:
				this.__evtProgramChange(__track, __event);
				break;
			case MLD.EVENT_TIMEBASE_TEMPO:
				this.__evtTimebaseTempo(__track, __event);
				break;
			case MLD.EVENT_VOLUME:
				this.__evtVolume(__track, __event);
				break;
			case MLD.EVENT_X_DRUM_ENABLE:
				this.__evtDrumEnable(__track, __event);
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
				this.__setTrackOffset(__track, __track._offset + 1);
		}
	}
	
	/**
	 * Process an Ext-Info event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtExtInfo(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		this.sampler.sysEx(__event._data);
		this.__setTrackOffset(__track, __track._offset + 1);
	}
	
	/**
	 * Process a Master Tune event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtMasterTune(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		this.sampler.masterTune(__event._semitones);
		this.__setTrackOffset(__track, __track._offset + 1);
	}
	
	/**
	 * Process a Master Volume event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtMasterVolume(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		this.sampler.masterVolume(__event._volume);
		this.__setTrackOffset(__track, __track._offset + 1);
	}
	
	/**
	 * Process a Note event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtNote(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		__MLDChannel__ chan = this._channels[__event._channel];
		__MLDNote__ note = chan._notesOn[MLDPlayer.A4 + __event._key];
		
		// Common processing
		this.__setTrackOffset(__track, __track._offset + 1);
		
		// Raise an event
		if (this._evtKeys.contains(__event._key))
			this._events.add(
				new MLDPlayerEvent(this.getTime(), MLDPlayerEvent.EVENT_KEY,
					__event._key));
		
		// Velocity 0 is regarded as key-off
		if (__event._velocity == 0)
		{
			this.sampler.keyOff(__event._channel, __event._key);
			if (note != null)
			{
				chan._notesOn[MLDPlayer.A4 + __event._key] = null;
				chan._notesOut.remove(note);
			}
			return;
		}
		
		// Velocity not zero is regarded as key-on
		if (!this._seeking)
			this.sampler.keyOn(__event._channel, __event._key, __event._velocity);
		
		// Get or create the note for this key
		if (note == null)
		{
			note = new __MLDNote__();
			note._channel = __event._channel;
			note._key = __event._key;
			chan._notesOn[MLDPlayer.A4 + __event._key] = note;
			chan._notesOut.add(note);
		}
		
		// Reconfigure the note
		note._gateTime = __event._gateTime;
	}
	
	/**
	 * Process a Panning event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtPanPot(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		this.sampler.panpot(__event._channel, __event._panpot);
		this.__setTrackOffset(__track, __track._offset + 1);
	}
	
	/**
	 * Process a Pitch Bend event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtPitchBend(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		this.sampler.pitchBend(__event._channel, __event._semitones);
		this.__setTrackOffset(__track, __track._offset + 1);
	}
	
	/**
	 * Process a Pitch Bend Range event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtPitchRange(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		this.sampler.pitchBendRange(__event._channel, __event._range);
		this.__setTrackOffset(__track, __track._offset + 1);
	}
	
	/**
	 * Process a Program Change event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtProgramChange(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		this.sampler.programChange(__event._channel, __event._program);
		this.__setTrackOffset(__track, __track._offset + 1);
	}
	
	/**
	 * Process a Timebase-Tempo event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtTimebaseTempo(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		if (__event._timebase == -1)
			return;
		float prev = this._framesPerTick;
		this.__setTempo(__event._timebase, __event._tempo);
		this._pendingFrames = this._pendingFrames * this._framesPerTick / prev;
		this.__setTrackOffset(__track, __track._offset + 1);
	}
	
	/**
	 * Process a Volume event.
	 *
	 * @param __track The track that the event belongs to.
	 * @param __event {@link __MLDEvent__} object containing event data.
	 * @since 2025/05/05
	 */
	private void __evtVolume(__MLDPlayerTrack__ __track, __MLDEvent__ __event)
	{
		this.sampler.volume(__event._channel, __event._volume);
		this.__setTrackOffset(__track, __track._offset + 1);
	}

	/**
	 * Process events on a track.
	 *
	 * @param __track The track to process.
	 * @param __ticks The amount of ticks pending to process.
	 * @since 2025/05/05
	 */
	private void __process(__MLDPlayerTrack__ __track, int __ticks)
	{
		// The track has finished
		if (__track._finished)
			return;
		
		// Update state
		__track._ticks -= __ticks;
		if (__track._ticks > 0)
			return;
		
		// Process all events this tick
		while (__track._ticks == 0)
		{
			__MLDEvent__ event = __track._mld.get(__track._offset);
			
			// Process the event
			switch (event._type)
			{
				case MLD.EVENT_TYPE_NOTE:
					this.__evtNote(__track, event);
					break;
				case MLD.EVENT_TYPE_EXT_B:
					this.__evtExtB(__track, event);
					break;
				case MLD.EVENT_TYPE_EXT_INFO:
					this.__evtExtInfo(__track, event);
					break;
				default:
					this.__setTrackOffset(__track, __track._offset + 1);
			}
			
			// Stop processing events
			if (__track._finished)
				return;
			
			// Schedule the next event
			__track._ticks = __track._mld.get(__track._offset)._delta;
		}
	}

	/**
	 * Unregisters a keys from raising events during rendering.
	 *
	 * @param __key A key number to unregister.
	 * @see MLDPlayerEvent
	 * @see #getEvents()
	 * @since 2025/05/05
	 */
	private void __removeEventKey(int __key)
	{
		this._evtKeys.remove(__key);
	}
	
	/**
	 * Unregisters multiple keys from raising events during rendering.
	 *
	 * @param __keys A list of key numbers to unregister.
	 * @throws NullPointerException if {@code keys} is {@code null}.
	 * @see MLDPlayerEvent
	 * @see #getEvents()
	 * @since 2025/05/05
	 */
	private void __removeEventKeys(int[] __keys)
	{
		if (__keys == null)
			throw new NullPointerException("Key array is required.");
		for (int key : __keys)
			this._evtKeys.remove(key);
	}
	
	/**
	 * Compute the number of output frames in one event tick.
	 *
	 * @param __timebase The timebase that events will use.
	 * @param __tempo The tempo that events will use.
	 * @since 2025/05/05
	 */
	private void __setTempo(int __timebase, int __tempo)
	{
		this._framesPerTick = (60 * this._sampleRate) / (__timebase * __tempo);
	}
	
	/**
	 * Specify the event offset of a track.
	 *
	 * @param __track The track to set the offset to.
	 * @param __offset The event offset value.
	 * @since 2025/05/05
	 */
	private void __setTrackOffset(__MLDPlayerTrack__ __track, int __offset)
	{
		// Configure the track
		__track._offset = __offset;
		__track._finished = __offset >= __track._mld.size();
		
		// Raise an event
		if (!__track._finished || !this._evtPlayback)
			return;
		boolean finished = true;
		for (__MLDPlayerTrack__ other : this._tracks)
			finished = finished && other._finished;
		if (finished)
			this._events.add(
				new MLDPlayerEvent(this.getTime(), MLDPlayerEvent.EVENT_END,
					0));
	}
	
	/**
	 * Determine how many ticks can be processed until a note expires.
	 *
	 * @return How many ticks can be processed.
	 * @since 2025/05/05
	 */
	private int __untilNote()
	{
		int ret = -1;
		for (__MLDChannel__ chan : this._channels)
			for (__MLDNote__ note : chan._notesOut)
			{
				if (ret == -1 || note._gateTime < ret)
					ret = note._gateTime;
			}
		return ret;
	}

	/**
	 * Determine how many ticks can be processed until the next event.
	 *
	 * @return How many ticks can be processed.
	 * @since 2025/05/05
	 */
	private int __untilTrack()
	{
		int ret = -1;
		for (__MLDPlayerTrack__ track : this._tracks)
		{
			if (track._finished)
				continue;
			if (ret == -1 || track._ticks < ret)
				ret = track._ticks;
		}
		return ret;
	}
}
