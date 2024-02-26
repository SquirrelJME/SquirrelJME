// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.media.MediaException;
import javax.microedition.media.control.MIDIControl;

/**
 * This is a basic class which provides a base simple implementation of
 * MIDI controls.
 *
 * @since 2022/04/23
 */
@SquirrelJMEVendorApi
public abstract class SimpleMidiControl
	implements MIDIControl
{
	/** Short MIDI message buffer. */
	private final byte[] _shortBuf =
		new byte[3];
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	@SquirrelJMEVendorApi
	public final int[] getBankList(boolean __custom)
		throws IllegalStateException, MediaException
	{
		throw new MediaException("NOPE");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	@SquirrelJMEVendorApi
	public final int getChannelVolume(int __channel)
		throws IllegalArgumentException, IllegalStateException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	@SquirrelJMEVendorApi
	public final String getKeyName(int __bank, int __prog, int __key)
		throws IllegalArgumentException, IllegalStateException, MediaException
	{
		throw new MediaException("NOPE");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	@SquirrelJMEVendorApi
	public final int[] getProgram(int __channel)
		throws IllegalArgumentException, IllegalStateException, MediaException
	{
		throw new MediaException("NOPE");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	@SquirrelJMEVendorApi
	public final int[] getProgramList(int __bank)
		throws IllegalArgumentException, IllegalStateException, MediaException
	{
		throw new MediaException("NOPE");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	@SquirrelJMEVendorApi
	public final String getProgramName(int __bank, int __program)
		throws IllegalArgumentException, IllegalStateException, MediaException
	{
		throw new MediaException("NOPE");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	@SquirrelJMEVendorApi
	public final boolean isBankQuerySupported()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	@SquirrelJMEVendorApi
	public final void setChannelVolume(int __channel, int __volume)
		throws IllegalArgumentException, IllegalStateException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	@SquirrelJMEVendorApi
	public final void setProgram(int __channel, int __bank, int __program)
		throws IllegalArgumentException, IllegalStateException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/04/23
	 */
	@Override
	@SquirrelJMEVendorApi
	public final void shortMidiEvent(int __type, int __data1, int __data2)
		throws IllegalArgumentException, IllegalStateException
	{
		// {@squirreljme.error EA0a Invalid event type. (The event)}
		if (!((__type >= 0x80 && __type < 0xF0) ||
			(__type >= 0xF8 && __type <= 0xFF)))
			throw new IllegalArgumentException("EA0a " + __type);
		
		// Need this so that we know how much data to actually send
		int msgLen = MidiUtils.midiMessageLength(__type);
		
		// Check sub-event parameters
		// {@squirreljme.error EA0b MIDI event out of range.}
		if (msgLen >= 2 && (__data1 < 0 || __data1 > 127))
			throw new IllegalArgumentException(
				"EA0b " + __type + " " + __data1);
		if (msgLen >= 3 && (__data2 < 0 || __data2 > 127))
			throw new IllegalArgumentException(
				"EA0b " + __type + " " + __data1 + " " + __data2);
		
		// Fill short buffer with the data, we use the same buffer over and
		// over so we do not just keep allocating memory
		byte[] shortBuf = this._shortBuf;
		shortBuf[0] = (byte)__type;
		shortBuf[1] = (byte)__data1;
		shortBuf[2] = (byte)__data2;
		
		// Forward to long event handler which does no checking
		this.longMidiEvent(shortBuf, 0, msgLen);
	}
}
