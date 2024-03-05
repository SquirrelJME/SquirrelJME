// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

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
	protected volatile boolean stopPlayback;
	
	/** MIDI trackers. */
	private final MTrkTracker[] _trackers;
	
	/** The number of nanoseconds per tick division. */
	volatile long _nanosPerTickDiv =
		-1;
	
	/** The time signature. */
	volatile int _timeSignature =
		1;
	
	/**
	 * Initializes the MIDI tracker.
	 *
	 * @param __player The player used.
	 * @param __tracks MIDI tracks to run with.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/27
	 */
	public MidiTracker(MidiPlayer __player, MTrkParser[] __tracks)
		throws NullPointerException
	{
		super("SquirrelJME-MidiTracker-" +
			Math.abs(__player.hashCode()));
		
		if (__tracks == null)
			throw new NullPointerException("NARG");
		
		this.player = __player;
		this.midiControl = __player.midiControl;
		
		// Trackers for each track
		MTrkTracker[] trackers = new MTrkTracker[__tracks.length];
		for (int i = 0, n = __tracks.length; i < n; i++)
			trackers[i] = new MTrkTracker(__tracks[i]);
		this._trackers = trackers;
	}
	
	/**
	 * End of track has been reached, so stop.
	 *
	 * @since 2024/02/26
	 */
	public void endOfTrack()
	{
		synchronized (this)
		{
			// Indicate to stop
			this.stopPlayback = true;
			
			// We might be in a lock
			this.notifyAll();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/27
	 */
	@SuppressWarnings("ConditionCoveredByFurtherCondition")
	@Override
	public void run()
	{
		MidiPlayer player = this.player;
		MIDIControl control = this.midiControl;
		MTrkTracker[] trackers = this._trackers;
		int numTracks = trackers.length;
		
		// Used to indicate when the next track time should play
		long[] readyAts = new long[numTracks];
		for (int i = 0; i < numTracks; i++)
			readyAts[i] = Long.MIN_VALUE;
		
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
			
			// Current micros per tick div, used for sleeping... if no tempo
			// was previously set then use the default for MIDI?
			long nanosPerTickDiv = this._nanosPerTickDiv;
			if (nanosPerTickDiv < 0)
			{
				nanosPerTickDiv = this.player._nanosPerTickDiv;
				this._nanosPerTickDiv = nanosPerTickDiv;
			}
			
			// Update each tracker accordingly
			long soonestReady = Long.MAX_VALUE;
			for (int track = 0; track < numTracks; track++)
			{
				// We are not ready here yet
				long readyAt = readyAts[track];
				if (readyAt != Long.MIN_VALUE && nowTime < readyAt)
				{
					// Used for sleeping
					if (readyAt < soonestReady)
						soonestReady = readyAt;
					continue;
				}
				
				// Get the current track to play
				MTrkTracker tracker = trackers[track];
				
				// Advance the track
				int delta = 0;
				while (delta == 0)
					delta = tracker.playNext(this, control);
				
				// Determine time when the track is ready
				if (nanosPerTickDiv > 0)
					readyAts[track] = nowTime +
						((delta * nanosPerTickDiv) / player._tickDiv);
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
		catch (MediaException __ignored)
		{
		}
	}
}
