// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

/**
 * Represents a single MIDI port.
 *
 * @since 2022/04/21
 */
public final class EmulatedMidiPortBracket
	implements MidiPortBracket
{
	/** The receiver for reading data. */
	final Receiver _receiver;
	
	/** The transmitter for sending data. */
	final Transmitter _transmitter;
	
	/**
	 * Initializes the port.
	 * 
	 * @param __receiver The receiver.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/21
	 */
	EmulatedMidiPortBracket(Receiver __receiver)
		throws NullPointerException
	{
		if (__receiver == null)
			throw new NullPointerException("NARG");
		
		this._receiver = __receiver;
		this._transmitter = null;
	}
	
	/**
	 * Initializes the port.
	 * 
	 * @param __transmitter The transmitter.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/21
	 */
	EmulatedMidiPortBracket(Transmitter __transmitter)
		throws NullPointerException
	{
		if (__transmitter == null)
			throw new NullPointerException("NARG");
		
		this._receiver = null;
		this._transmitter = __transmitter;
	}
}
