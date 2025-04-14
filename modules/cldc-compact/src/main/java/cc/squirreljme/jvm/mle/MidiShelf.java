// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.jvm.mle.brackets.MidiDeviceBracket;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This allows for the native playback and recording of MIDI based events.
 *
 * @see MidiPortBracket
 * @since 2022/04/20
 */
@SquirrelJMEVendorApi
public final class MidiShelf
{
	/**
	 * This reads data that is incoming from a MIDI device, it may not include
	 * a full event and may only be in partial form.
	 * 
	 * @param __port The port to receive data from.
	 * @param __b The buffer.
	 * @param __o The offset into the buffer.
	 * @param __l The maximum number of bytes to receive.
	 * @return The number of bytes read from the port, if there is no data
	 * then {@code 0} will be returned.
	 * @throws MLECallError If the port is not valid receiver, the buffer is
	 * {@code null}, or the offset and/or length exceed the array bounds.
	 * @since 2022/04/21
	 */
	@SquirrelJMEVendorApi
	public static native int dataReceive(MidiPortBracket __port,
		byte[] __b, int __o, int __l)
		throws MLECallError;
	
	/**
	 * This writes data directly to a MIDI port, it does not perform any
	 * checking if the data is valid or otherwise.
	 * 
	 * @param __port The port to transmit data to.
	 * @param __b The buffer.
	 * @param __o The offset into the buffer.
	 * @param __l The number of bytes to transmit.
	 * @throws MLECallError If the port is not valid transmitter, the buffer is
	 * {@code null}, or the offset and/or length exceed the array bounds.
	 * @since 2022/04/21
	 */
	@SquirrelJMEVendorApi
	public static native void dataTransmit(MidiPortBracket __port,
		byte[] __b, int __o, int __l)
		throws MLECallError;
	
	/**
	 * Returns the name of the given device.
	 * 
	 * @param __device The device to get the name of.
	 * @return The name of the device.
	 * @throws MLECallError If the device is not valid.
	 * @since 2022/04/21
	 */
	@SquirrelJMEVendorApi
	public static native String deviceName(MidiDeviceBracket __device)
		throws MLECallError;
	
	/**
	 * Returns the MIDI devices which are available for usage.
	 * 
	 * @return The set of devices that are available, will be empty if there
	 * are no MIDI devices, or it is not supported by the system.
	 * @since 2022/04/21
	 */
	@SquirrelJMEVendorApi
	public static native MidiDeviceBracket[] devices();
	
	/**
	 * Returns the ports that are available to the given MIDI device.
	 * 
	 * @param __device The device to get the ports of.
	 * @param __transmit If {@code true} this will return the transmitter
	 * ports, otherwise it will return the receiver ports.
	 * @return The ports that are available for the device, will be empty
	 * if there are none for a given device.
	 * @throws MLECallError If the device is not valid.
	 * @since 2022/04/21
	 */
	@SquirrelJMEVendorApi
	public static native MidiPortBracket[] ports(MidiDeviceBracket __device,
		boolean __transmit)
		throws MLECallError;
}
