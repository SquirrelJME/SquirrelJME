// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.MidiShelf;
import cc.squirreljme.jvm.mle.brackets.MidiDeviceBracket;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

/**
 * As {@link MidiShelf}.
 * 
 * Note that Java SE is the opposite when it comes to receivers and
 * transmitters.
 *
 * @since 2022/04/21
 */
public class EmulatedMidiShelf
{
	/** Cached MIDI devices. */
	private static volatile MidiDeviceBracket[] _DEVICES;
	
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
	public static int dataReceive(MidiPortBracket __port,
		byte[] __b, int __o, int __l)
		throws MLECallError
	{
		if (__port == null || __b == null || __o < 0 || __l < 0 ||
			(__o + __l) > __b.length)
			throw new MLECallError("Invalid arguments.");
		
		EmulatedMidiPortBracket emul = (EmulatedMidiPortBracket)__port;
		if (emul._transmitter == null)
			throw new MLECallError("Not a MIDI receiver.");
		
		Debugging.todoNote("Implement MIDI receive?");
		return 0;
	}
	
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
	public static void dataTransmit(MidiPortBracket __port,
		byte[] __b, int __o, int __l)
		throws MLECallError
	{
		if (__port == null || __b == null || __o < 0 || __l < 0 ||
			(__o + __l) > __b.length)
			throw new MLECallError("Invalid arguments.");
		
		EmulatedMidiPortBracket emul = (EmulatedMidiPortBracket)__port;
		if (emul._receiver == null)
			throw new MLECallError("Not a MIDI transmitter.");
			
		// Copy data into a new buffer
		byte[] data = new byte[__l];
		System.arraycopy(__b, __o,
			data, 0, __l);
		
		// Send the data
		emul._receiver.send(
			new BasicMidiMessage(data), -1);
	}
	
	/**
	 * Returns the name of the given device.
	 * 
	 * @param __device The device to get the name of.
	 * @return The name of the device.
	 * @throws MLECallError If the device is not valid.
	 * @since 2022/04/21
	 */
	public static String deviceName(MidiDeviceBracket __device)
		throws MLECallError
	{
		if (__device == null)
			throw new MLECallError("Null argument.");
		
		return ((EmulatedMidiDeviceBracket)__device)._info.getName();
	}
	
	/**
	 * Returns the MIDI devices which are available for usage.
	 * 
	 * @return The set of devices that are available, will be empty if there
	 * are no MIDI devices, or it is not supported by the system.
	 * @since 2022/04/21
	 */
	public static MidiDeviceBracket[] devices()
	{
		// Pre-cached devices?
		MidiDeviceBracket[] devices = EmulatedMidiShelf._DEVICES;
		if (devices != null)
			return devices.clone();
		
		// Locate new devices
		List<EmulatedMidiDeviceBracket> result = new ArrayList<>();
		for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo())
			try
			{
				result.add(new EmulatedMidiDeviceBracket(
					MidiSystem.getMidiDevice(info), info));
			}
			catch (MidiUnavailableException|IllegalArgumentException ignored)
			{
			}
		
		devices = result.toArray(new EmulatedMidiDeviceBracket[result.size()]);
		EmulatedMidiShelf._DEVICES = devices;
		return devices.clone();
	}
	
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
	public static MidiPortBracket[] ports(MidiDeviceBracket __device,
		boolean __transmit)
		throws MLECallError
	{
		if (__device == null)
			throw new MLECallError("Null argument.");
		
		MidiPortBracket result = null;
		EmulatedMidiDeviceBracket emul = (EmulatedMidiDeviceBracket)__device;
		
		// Already cached?
		if (!__transmit && emul._receivePorts != null)
			return emul._receivePorts.clone();
		else if (__transmit && emul._transmitPorts != null)
			return emul._transmitPorts.clone();
		
		// Transmitter?
		try
		{
			// Does the device need to actually be opened?
			if (!emul._device.isOpen())
				emul._device.open();
			
			// Java SE follows the opposite direction, so this is the receiver
			if (!__transmit)
			{
				// Use already claimed one?
				for (Transmitter transmitter : emul._device.getTransmitters())
					result = new EmulatedMidiPortBracket(transmitter);
				
				// Make new one?
				if (result == null)
					result = new EmulatedMidiPortBracket(
						emul._device.getTransmitter());
				
				// Debug
				Debugging.debugNote("Found receiver: %s", result);
			}
			
			// Receiver?
			else
			{
				// Use already claimed one?
				for (Receiver receiver : emul._device.getReceivers())
					result = new EmulatedMidiPortBracket(receiver);
				
				// Make new one?
				if (result == null)
					result = new EmulatedMidiPortBracket(
						emul._device.getReceiver());
				
				// Debug
				Debugging.debugNote("Found transmitter: %s", result);
			}
		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
		}
		
		// Use the ports that are used
		MidiPortBracket[] rv;
		if (result == null)
			rv = new MidiPortBracket[0];
		else
			rv = new MidiPortBracket[]{result};
		
		// Cache
		if (!__transmit && emul._transmitPorts != null)
			emul._transmitPorts = rv.clone();
		else if (__transmit && emul._receivePorts != null)
			emul._receivePorts = rv.clone();
		
		// Use result
		return rv;
	}
}
