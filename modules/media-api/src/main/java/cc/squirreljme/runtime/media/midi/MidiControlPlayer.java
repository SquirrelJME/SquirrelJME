// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.jvm.mle.MidiShelf;
import cc.squirreljme.jvm.mle.brackets.MidiDeviceBracket;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.media.Control;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.TimeBase;
import javax.microedition.media.control.MIDIControl;

/**
 * This is a player for the usage of gaining access to MIDI controls.
 *
 * @since 2022/04/24
 */
@SquirrelJMEVendorApi
public class MidiControlPlayer
	implements Player
{
	/**
	 * {@squirreljme.property cc.squirreljme.midi.device=name/id Use the
	 * selected MIDI device to play audio, if found. May be a name or number.}
	 */
	@SquirrelJMEVendorApi
	public static final String MIDI_DEVICE_PROPERTY =
		"cc.squirreljme.midi.device";
	
	/**
	 * {@squirreljme.property cc.squirreljme.midi.port=id Use the given port
	 * number for the selected MIDI device.}
	 */
	@SquirrelJMEVendorApi
	public static final String MIDI_PORT_PROPERTY =
		"cc.squirreljme.midi.port";
	
	/** The MIDI control to use. */
	private final MidiShelfControl control;
	
	/**
	 * Initializes the MIDI control player.
	 * 
	 * @param __control The control to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/24
	 */
	@SquirrelJMEVendorApi
	public MidiControlPlayer(MidiShelfControl __control)
		throws NullPointerException
	{
		if (__control == null)
			throw new NullPointerException("NARG");
		
		this.control = __control;
	}
	
	@Override
	@SquirrelJMEVendorApi
	public void addPlayerListener(PlayerListener __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public void close()
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public void deallocate()
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public String getContentType()
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public Control getControl(String __control)
		throws NullPointerException
	{
		if (__control == null)
			throw new NullPointerException("NARG");
		
		// Is this the right control?
		if (__control.equals(MIDIControl.class.getName()))
			return this.control;
		
		// Not found
		return null;
	}
	
	@Override
	@SquirrelJMEVendorApi
	public Control[] getControls()
	{
		// There is just this control
		return new Control[]{this.control};
	}
	
	@Override
	@SquirrelJMEVendorApi
	public long getDuration()
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public long getMediaTime()
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public int getState()
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public TimeBase getTimeBase()
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public void prefetch()
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public void realize()
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public void removePlayerListener(PlayerListener __a)
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public void setLoopCount(int __count)
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public long setMediaTime(long __now)
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public void setTimeBase(TimeBase __timeBase)
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public void start()
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	@Override
	@SquirrelJMEVendorApi
	public void stop()
		throws MediaException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Initializes a new MIDI player.
	 * 
	 * @return A MIDI player.
	 * @throws MediaException If no MIDI is supported.
	 * @since 2022/04/24
	 */
	@SquirrelJMEVendorApi
	public static MidiControlPlayer newMidiPlayer()
		throws MediaException
	{
		// See if the MIDI device property exists
		String deviceName = null;
		try
		{
			deviceName = System.getProperty(
				MidiControlPlayer.MIDI_DEVICE_PROPERTY);
		}
		catch (SecurityException ignored)
		{
		}
		
		// See if this maps to an ID number
		int deviceId = -1;
		if (deviceName != null)
			try
			{
				deviceId = Integer.parseInt(deviceName, 10);
			}
			catch (NumberFormatException ignored)
			{
			}
		
		// Used for device selection
		MidiDeviceBracket selectedDevice = null;
		
		// Use the device by its ID?
		MidiDeviceBracket[] devices = MidiShelf.devices();
		if (deviceId >= 0 && deviceId < devices.length)
			selectedDevice = devices[deviceId]; 
		
		// Try to find one, or a default by name
		if (selectedDevice == null)
		{
			MidiDeviceBracket fallback = null;
			for (MidiDeviceBracket device : devices)
			{
				// Always set a fallback, as long as the device has at least
				// one transmit port we can send data to
				if (fallback == null && MidiShelf.ports(device,
					true).length > 0)
					fallback = device;
				
				// Device matches by name?
				if (deviceName != null)
					if (deviceName.equalsIgnoreCase(
						MidiShelf.deviceName(device)))
					{
						selectedDevice = device;
						break;
					}
			}
			
			// Use fallback device if none found
			if (selectedDevice == null)
				selectedDevice = fallback;
		}
		
		// {@squirreljme.error EA0c The current device does not support MIDI
		// playback.}
		if (selectedDevice == null)
			throw new MediaException("EA0c");
		
		// See if the port ID exists
		String portName = null;
		try
		{
			portName = System.getProperty(
				MidiControlPlayer.MIDI_PORT_PROPERTY);
		}
		catch (SecurityException ignored)
		{
		}
		
		// See if the port maps to a number
		int portId = 0;
		if (portName != null)
			try
			{
				portId = Integer.parseInt(portName, 10);
			}
			catch (NumberFormatException ignored)
			{
			}
		
		// Setup control player using this port
		MidiPortBracket[] ports = MidiShelf.ports(selectedDevice,
			true);
		if (portId >= 0 && portId < ports.length)
			return new MidiControlPlayer(new MidiShelfControl(ports[portId]));
			
		// {@squirreljme.error EA0d Found MIDI device, however (The device)}
		throw new MediaException("EA0d " +
			MidiShelf.deviceName(selectedDevice));
	}
}
