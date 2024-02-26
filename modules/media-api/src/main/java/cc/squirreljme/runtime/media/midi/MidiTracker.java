// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

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
	 * {@inheritDoc}
	 * @since 2022/04/27
	 */
	@Override
	public void run()
	{
		MidiPlayer player = this.player;
		MIDIControl control = this.midiControl;
		MTrkTracker[] trackers = this._trackers;
		int numTracks = trackers.length;
		
		// Play almost forever
		synchronized (this)
		{
			for (int midiLoop = 0;; midiLoop++)
			{
				// Stop playback immediately?
				if (this.stopPlayback)
					break;
				
				// Update each tracker accordingly
				for (int track = 0; track < numTracks; track++)
				{
					// Get the current track to play
					MTrkTracker tracker = trackers[track];
					
					// Advance the track
					int delta = 0;
					while (delta == 0)
						delta = tracker.playNext(control);
				}
				
				// Sleep a bit to make it more sane
				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException __ignored)
				{
					break;
				}
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
	}
}
