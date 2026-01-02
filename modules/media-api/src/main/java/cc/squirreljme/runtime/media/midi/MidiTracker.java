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
	
	/** The timing that is shared for all MIDI tracks. */
	final MidiTimeDiv _timeDiv;
	
	/** The time signature. */
	volatile int _timeSignature =
		1;
	
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
		Debugging.todoNote("fastForward(%d)", __micros);
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
		Debugging.todoNote("micros()");
		return 0;
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
		MTrkTracker[] trackers = this._trackers;
		MidiTimeDiv timeDiv = this._timeDiv;
		int numTracks = trackers.length;
		
		// Used to indicate when the next track time should play
		long[] readyAts = new long[numTracks];
		Arrays.fill(readyAts, Long.MIN_VALUE);
		
		// Reset all trackers so they start at the beginning
		for (MTrkTracker tracker : trackers)
			tracker.reset();
		
		// Play almost forever
		for (;;)
		{
			// Stop playback immediately?
			synchronized (this)
			{
				if (this.stopPlayback)
					break;
			}
			
			// The current time for this loop
			long nowTime = System.nanoTime();
			
			// How many tracks have ended?
			int endedTracks = 0;
			
			// Update each tracker accordingly
			long soonestReady = Long.MAX_VALUE;
			for (int track = 0; track < numTracks; track++)
			{
				// Get the current track to play
				MTrkTracker tracker = trackers[track];
				
				// If a track has ended, count up the end tracker
				if (tracker._trackEnded)
					endedTracks++;
				
				// We are not ready here yet
				long readyAt = readyAts[track];
				if (readyAt != Long.MIN_VALUE && nowTime < readyAt)
				{
					// Used for sleeping
					if (readyAt < soonestReady)
						soonestReady = readyAt;
					continue;
				}
				
				// Advance the track
				int delta = 0;
				while (delta == 0)
					delta = tracker.playNext(this, control);
				
				// Determine time when the track is ready
				long nanosPerTickDiv = timeDiv._nanosPerTickDiv;
				if (nanosPerTickDiv > 0)
					readyAts[track] = nowTime + (delta * nanosPerTickDiv);
			}
			
			// End of MIDI reached, stop or loop depending on what was
			// requested
			if (endedTracks >= numTracks)
				try
				{
					// Tracks that ended
					Debugging.debugNote("ended: %d/%d",
						endedTracks, numTracks);
					
					// Either stop or loop
					if (player.decrementLoop())
						player.stopViaMedia();
					else
						player.loopViaMedia();
					
					// Reset the soonest ready time as we are looping
					soonestReady = Long.MAX_VALUE;
				}
				catch (MediaException __e)
				{
					throw new RuntimeException(__e.getMessage(), __e);
				}
			
			// Sleep until the next event can occur
			if (soonestReady != Long.MAX_VALUE && soonestReady > nowTime)
				try
				{
					// Do not rest for too long
					long millis = (soonestReady - nowTime) / 1_000_000;
					if (millis > 250)
						millis = 250;
					
					// Rest
					Thread.sleep(millis);
				}
				catch (InterruptedException __ignored)
				{
					break;
				}
		}
		
		// Put every channel into a default state before leaving
		for (int channel = 0; channel < MidiTracker._MAX_CHANNELS; channel++)
		{
			// All sound off
			control.shortMidiEvent(MIDIControl.CONTROL_CHANGE | channel,
				120, 0);
			
			// All notes off
			control.shortMidiEvent(MIDIControl.CONTROL_CHANGE | channel,
				123, 0);
			
			// Reset all controllers
			control.shortMidiEvent(MIDIControl.CONTROL_CHANGE | channel,
				121, 0);
		}
		
		// Indicate stop
		try
		{
			player.stopViaMedia();
		}
		catch (MediaException __e)
		{
			__e.printStackTrace();
		}
	}
}
