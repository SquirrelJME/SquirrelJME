// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.runtime.cldc.debug.Debugging;
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
	protected volatile boolean _stopPlayback;
	
	/**
	 * Initializes the MIDI tracker.
	 * 
	 * @param __player The player used.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/27
	 */
	public MidiTracker(MidiPlayer __player)
		throws NullPointerException
	{
		super("SquirrelJME-MidiTracker-" +
			Math.abs(__player.hashCode()));
		
		this.player = __player;
		this.midiControl = __player.midiControl;
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
		
		// Play almost forever
		synchronized (this)
		{
			for (int i = 0;; i++)
			{
				// Stop playback immediately?
				if (this._stopPlayback)
					break;
				
				// For testing
				System.err.printf("Emitting %s...%n", i);
				boolean emit = ((i & 1) == 0);
				control.shortMidiEvent((emit ? 0b1001_0000 : 0b1000_0000),
					60 + (i % 20), (emit ? 127 : 0));
				
				// For the next note to appear
				try
				{
					this.wait(250);
				}
				catch (InterruptedException ignored)
				{
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
