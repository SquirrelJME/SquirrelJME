// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.MIDIControl;

/**
 * This is the thread which is used to track an actual MIDI that is playing.
 *
 * @since 2022/04/27
 */
public final class MidiTracker
	extends Thread
{
	/** The number if MIDI channels. */
	private static final int _MAX_CHANNELS =
		16;
	
	/** The MIDI player that is currently being played. */
	protected final MidiPlayer player;
	
	/** The MIDI control to emit sounds into. */
	protected final MIDIControl midiControl;
	
	/** Stop playing? */
	@SquirrelJMEVendorApi
	protected volatile boolean stopPlayback;
	
	/** MIDI trackers. */
	private final MTrkTracker[] _trackers;
	
	/** The nanosecond clock when the next event occurs. */
	private final long[] _nextNanos;
	
	/** The timing that is shared for all MIDI tracks. */
	final MidiTimeDiv _timeDiv;
	
	/** The time to fast-forward to. */
	private volatile long _targetNanos =
		Long.MIN_VALUE;
	
	/**
	 * Initializes the MIDI tracker.
	 *
	 * @param __player The player used.
	 * @param __tracks MIDI tracks to run with.
	 * @param __timeDiv The time division.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/27
	 */
	public MidiTracker(MidiPlayer __player, MTrkParser[] __tracks,
		MidiTimeDiv __timeDiv)
		throws NullPointerException
	{
		super("SquirrelJME-MidiTracker-" +
			Math.abs(__player.hashCode()));
		
		if (__tracks == null || __timeDiv == null)
			throw new NullPointerException("NARG");
		
		this.player = __player;
		this.midiControl = __player.midiControl;
		
		// Store the time division
		this._timeDiv = __timeDiv;
		
		// Trackers for each track
		MTrkTracker[] trackers = new MTrkTracker[__tracks.length];
		for (int i = 0, n = __tracks.length; i < n; i++)
			trackers[i] = new MTrkTracker(__tracks[i], __timeDiv);
		this._trackers = trackers;
		
		// Times the next event occurs for each track
		this._nextNanos = new long[__tracks.length];
	}
	
	/**
	 * Tells the tracker to fast-forward.
	 *
	 * @param __micros The microseconds to use.
	 * @since 2025/06/03
	 */
	@SquirrelJMEVendorApi
	public void fastForward(long __micros)
	{
		// Fast-forward with relative time and no control output
		// We do not care what the target time is, just that it is the media
		// time
		this.tracker(null, this.midiControl,
			__micros * 1_000L);
	}
	
	/**
	 * Returns the current microsecond clock.
	 *
	 * @return The current microsecond clock.
	 * @since 2025/06/15
	 */
	@SquirrelJMEVendorApi
	protected long micros()
	{
		// Just return the MIDI clock
		return this._timeDiv._nanoClock / 1_000L;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/27
	 */
	@SuppressWarnings({"ConditionCoveredByFurtherCondition", "BusyWait"})
	@Override
	public void run()
	{
		MidiPlayer player = this.player;
		MIDIControl control = this.midiControl;
		
		// Needed for final cleanup
		try
		{
			// Set the current track time to the nano time
			long targetNanos = this._timeDiv._nanoClock;
			
			// Infinite tracker loop
			for (;;)
			{
				// Track time we entered
				long startTime = System.nanoTime();
				
				// Track until we reach the target time
				long nextNanos = this.tracker(control, control,
					targetNanos);
				
				// Stopping playback?
				if (nextNanos == Long.MIN_VALUE)
					break;
				
				// Otherwise rest the current thread so the CPU is not set
				// on fire playing back music. Do not sleep if the delay is
				// too short however
				long diffNanos = (nextNanos - targetNanos);
				if (false && diffNanos >= 25_000_000L)
					try
					{
						// Offset the time so we do not spend extra time
						// sleeping
						Thread.sleep(
							(diffNanos - 25_000_000L) / 1_000_000L);
					}
					catch (InterruptedException __ignored)
					{
						break;
					}
				
				// How much time was spent in here, with sleeping?
				// Move up the current clock up based on the time difference
				// so that we know what new time to target
				long diffTime = (System.nanoTime() - startTime);
				if (diffTime > 0)
				{
					targetNanos += diffTime;
					this._timeDiv._nanoClock = targetNanos;
				}
			}
		}
		
		// Stop all notes and stop the media playback
		finally
		{
			// Squelch all notes
			MidiTracker.squelch(control);
			
			// Indicate stop
			try
			{
				player.stopViaMedia();
			}
			catch (IllegalStateException|MediaException __e)
			{
				__e.printStackTrace();
			}
		}
	}
	
	/**
	 * Performs a tracker tick.
	 *
	 * @param __play The control to play into.
	 * @param __squelch The squelch control.
	 * @param __targetNanos The target MIDI nanoseconds that we want to
	 * play up to.
	 * @return The clock where the next event will be at.
	 * @since 2026/01/02
	 */
	@SquirrelJMEVendorApi
	public long tracker(MIDIControl __play, MIDIControl __squelch,
		long __targetNanos)
	{
		synchronized (this)
		{
			// Stop playback?
			if (this.stopPlayback)
				return Long.MAX_VALUE;
		}
		
		// Forward to internal tracking
		return this.__tracker(__play, __squelch, __targetNanos);
	}
	
	/**
	 * Performs a tracker tick.
	 *
	 * @param __play The control to play into.
	 * @param __squelch The squelch control.
	 * @param __targetNanos The target MIDI nanoseconds that we want to
	 * play up to.
	 * @return The clock where the next event will be at.
	 * @since 2026/01/02
	 */
	private long __tracker(MIDIControl __play, MIDIControl __squelch,
		long __targetNanos)
	{
		// MIDI Tracks
		MTrkTracker[] trackers = this._trackers;
		int numTracks = trackers.length;
		
		// Timing
		MidiTimeDiv timeDiv = this._timeDiv;
		long[] nextNanos = this._nextNanos;
		
		// The lowest delta time to meet the next target
		long lowestDelta = Long.MAX_VALUE;
		
		// Tracks that have ended and which are not ready
		int stallTracks = 0;
		int endedTracks = 0;
		
		// Go through each track and process events
		// Note that the inner for() loop is to catch up if the timing was
		// too slow
		for (int track = 0; track < numTracks; track++)
			for (;;)
			{
				// Get the current track and the timing
				MTrkTracker tracker = trackers[track];
				long nextNano = nextNanos[track];
				
				// Track has finished?
				if (tracker._trackEnded)
				{
					// Both ended and stalled go up
					stallTracks++;
					endedTracks++;
					
					// Go to the next track
					break;
				}
				
				// We are not playing up to this point?
				if (__targetNanos < nextNano)
				{
					// Is there a new lowest delta time?
					long delta = nextNano - __targetNanos;
					if (delta < lowestDelta)
						lowestDelta = delta;
					
					// Stalled tracks go up
					stallTracks++;
					
					// Go to the next track
					break;
				}
				
				// Advance the track, keep playing notes when the delta is
				// zero. Once the delta is non-zero we need to pause.
				// However, if the track ends, we do not want to freeze!
				int tickDelta = 0;
				while (tickDelta == 0 && !tracker._trackEnded)
					tickDelta = tracker.playNext(__play);
				
				// If there is a tick delta add to the track time
				nextNanos[track] += (tickDelta * timeDiv._nanosPerTickDiv);
			}
		
		// Did all tracks end?
		if (endedTracks >= numTracks)
			return Long.MIN_VALUE;
		
		// Return the lowest delta time
		return lowestDelta;
	}
	
	/**
	 * Squelches all MIDI notes.
	 *
	 * @param __control The control this is for.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/02
	 */
	@SquirrelJMEVendorApi
	public static final void squelch(MIDIControl __control)
		throws NullPointerException
	{
		if (__control == null)
			throw new NullPointerException("NARG");
		
		// Put every channel into a default state before leaving
		for (int channel = 0; channel < MidiTracker._MAX_CHANNELS; channel++)
		{
			// All sound off
			__control.shortMidiEvent(
				MIDIControl.CONTROL_CHANGE | channel,
				120, 0);
			
			// All notes off
			__control.shortMidiEvent(
				MIDIControl.CONTROL_CHANGE | channel,
				123, 0);
			
			// Reset all controllers
			__control.shortMidiEvent(
				MIDIControl.CONTROL_CHANGE | channel,
				121, 0);
		}
	}
}
