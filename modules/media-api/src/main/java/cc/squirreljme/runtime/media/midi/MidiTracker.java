// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
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
	@KeepWhenCompacting
	protected volatile boolean stopPlayback;
	
	/** MIDI trackers. */
	private final MTrkTracker[] _trackers;
	
	/** The nanosecond clock when the next event occurs. */
	private final long[] _nextNanos;
	
	/** The timing that is shared for all MIDI tracks. */
	final MidiTimeDiv _timeDiv;
	
	/** Has the base time been adjusted for the main loop? */
	private volatile int _baseAdjust;
	
	/**
	 * Initializes the MIDI tracker.
	 *
	 * @param __player The player used.
	 * @param __tracks MIDI tracks to run with.
	 * @param __timeDiv The time division.
	 * @param __volume The master volume.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/27
	 */
	public MidiTracker(MidiPlayer __player, MTrkParser[] __tracks,
		MidiTimeDiv __timeDiv, MidiVolume __volume)
		throws NullPointerException
	{
		super("SquirrelJME-MidiTracker-" +
			Math.abs(__player.hashCode()));
		
		if (__tracks == null || __timeDiv == null || __volume == null)
			throw new NullPointerException("NARG");
		
		this.player = __player;
		this.midiControl = __player.midiControl;
		
		// Store the time division
		this._timeDiv = __timeDiv;
		
		// Trackers for each track
		MTrkTracker[] trackers = new MTrkTracker[__tracks.length];
		for (int i = 0, n = __tracks.length; i < n; i++)
			trackers[i] = new MTrkTracker(__tracks[i], __timeDiv, __volume);
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
	@KeepWhenCompacting
	public void fastForward(long __micros)
	{
		long target = __micros * 1_000L;
		synchronized (this)
		{
			// Fast-forward or time adjustment happened, need to recalculate
			// the base time because the base clock no longer matches the
			// MIDI clock
			this._baseAdjust++;
			
			// Is this a back in time fast-forward?
			MidiTimeDiv timeDiv = this._timeDiv;
			MIDIControl control = this.midiControl;
			if (target < timeDiv._nanoClock)
			{
				// Turn off all notes and reset all controllers
				MidiTracker.squelch(control, true);
				
				// Go back to the starting clock of zero
				timeDiv._nanoClock = 0;
				
				// Reset all tracks to start at zero
				Arrays.fill(this._nextNanos, 0);
				
				// Reset each track to the start since we need read the data
				// all over again
				for (MTrkTracker tracker : this._trackers)
					tracker.reset();
			}
			
			// Fast-forward with relative time and no control output
			// We do not care what the target time is, just that it is the
			// media time
			for (long next = 0; next < target && next != Long.MAX_VALUE;)
				next = this.tracker(null, control, target);
			
			// Interrupt
			this.interrupt();
			this.notifyAll();
		}
	}
	
	/**
	 * Returns the current microsecond clock.
	 *
	 * @return The current microsecond clock.
	 * @since 2025/06/15
	 */
	@KeepWhenCompacting
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
			// The base timing for events
			long baseTime = System.nanoTime();
			long baseMidi = this._timeDiv._nanoClock;
			
			// The current base adjust
			int baseAdjust = this._baseAdjust;
			
			// Infinite tracker loop
			for (;;)
			{
				// Get the new adjustment value
				int newAdjust;
				synchronized (this)
				{
					newAdjust = this._baseAdjust;
				}
				
				// Does the base clock need adjusting?
				if (newAdjust != baseAdjust)
				{
					// Adjustment changed
					baseAdjust = newAdjust;
					
					// Recalculate the base time
					baseTime = System.nanoTime();
					baseMidi = this._timeDiv._nanoClock;
				}
				
				// How much time has passed since the base time, relatively?
				long deltaTime = System.nanoTime() - baseTime;
				long deltaMidi = baseMidi + deltaTime;
				
				// Try to keep up with the tracker
				long nextMidi = this.tracker(control, control,
					deltaMidi);
				
				// Stopping playback?
				if (nextMidi == Long.MIN_VALUE)
				{
					// Media is stopping, stop playing
					if (player.decrementLoop())
					{
						// Turn off all notes
						MidiTracker.squelch(control, true);
						
						// Stop everything
						player.stopViaMedia();
						return;
					}
					
					// Media is looping, go back to the start
					else
						player.loopViaMedia();
					
					// Run another loop
					continue;
				}
				
				// Set time to wait for the next event
				long waitMidi = nextMidi - deltaMidi;
				if (waitMidi > 10_000L)
					try
					{
						// Debug
						if (Debugging.VERBOSE)
							Debugging.debugNote(
								"Sleep %d = (%d - %d) / %d",
								waitMidi, nextMidi, deltaMidi,
								this._timeDiv._nanoClock);
						
						// Offset the time so we do not spend extra time
						// sleeping
						synchronized (this)
						{
							this.wait(waitMidi / 1_000_000L,
								(int)(waitMidi % 1_000_000L));
						}
					}
					catch (InterruptedException ignored)
					{
					}
			}
		}
		
		// Failed to stop or otherwise?
		catch (IllegalStateException|MediaException __e)
		{
			__e.printStackTrace();
		}
		
		// Stop all notes and stop the media playback
		finally
		{
			// Squelch all notes
			MidiTracker.squelch(control, true);
			
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
	@KeepWhenCompacting
	public long tracker(MIDIControl __play, MIDIControl __squelch,
		long __targetNanos)
	{
		synchronized (this)
		{
			// Stop playback?
			if (this.stopPlayback)
			{
				// Force squelch everything
				MidiTracker.squelch(__squelch, true);
				
				// Playback is stopping
				return Long.MAX_VALUE;
			}
			
			// If only squelch is specified, mute all notes but do not
			// reset all controls
			if (__play == null && __squelch != null)
				MidiTracker.squelch(__squelch, false);
			
			// Forward to internal tracking
			return this.__tracker(__play, __squelch, __targetNanos);
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
		long lowestNanos = Long.MAX_VALUE;
		
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
					long delta = __targetNanos - nextNano;
					if (delta < lowestDelta)
						lowestDelta = delta;
					
					// Is this a lower clock time
					if (nextNano < lowestNanos)
						lowestNanos = nextNano;
					
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
					tickDelta = tracker.playNext(__play, __squelch);
				
				// If there is a tick delta add to the track time
				nextNano += (tickDelta * timeDiv._nanosPerTickDiv); 
				nextNanos[track] = nextNano;
				
				// Is this a lower clock time
				if (nextNano < lowestNanos)
					lowestNanos = nextNano;
			}
		
		// Did all tracks end?
		if (endedTracks >= numTracks)
			return Long.MIN_VALUE;
		
		// Set the new MIDI time to the lowest nanos
		if (lowestNanos != Long.MAX_VALUE && lowestNanos >= 0)
			timeDiv._nanoClock = lowestNanos;
		
		// Return the time in nanoseconds where the earliest next event
		// should play
		return lowestNanos;
	}
	
	/**
	 * Squelches all MIDI notes.
	 *
	 * @param __control The control this is for.
	 * @param __reset Reset all controllers.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/01/02
	 */
	@KeepWhenCompacting
	public static final void squelch(MIDIControl __control, boolean __reset)
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
			if (__reset)
				__control.shortMidiEvent(
					MIDIControl.CONTROL_CHANGE | channel,
					121, 0);
		}
	}
}
