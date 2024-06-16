// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.brackets.MidiDeviceBracket;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import javax.sound.midi.MidiDevice;

/**
 * This is an emulated MIDI device.
 *
 * @since 2022/04/21
 */
public final class EmulatedMidiDeviceBracket
	implements MidiDeviceBracket
{
	/** The referenced MIDI device. */
	final MidiDevice _device;
	
	/** Device information. */
	final MidiDevice.Info _info;
	
	/** Transmit ports. */
	volatile MidiPortBracket[] _transmitPorts;
	
	/** Receive ports. */
	volatile MidiPortBracket[] _receivePorts;
	
	/**
	 * Initializes the MIDI device bracket.
	 * 
	 * @param __device The device.
	 * @param __info The information.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/21
	 */
	EmulatedMidiDeviceBracket(MidiDevice __device,
		MidiDevice.Info __info)
		throws NullPointerException
	{
		if (__device == null || __info == null)
			throw new NullPointerException("NARG");
		
		this._device = __device;
		this._info = __info;
	}
}
