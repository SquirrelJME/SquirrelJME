// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media.midi;

import cc.squirreljme.jvm.mle.MidiShelf;
import cc.squirreljme.jvm.mle.brackets.MidiDeviceBracket;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.media.AbstractMidiControl;
import cc.squirreljme.runtime.media.AbstractPlayer;
import javax.microedition.media.MediaException;

/**
 * This is a player for the usage of gaining access to MIDI controls.
 *
 * @since 2022/04/24
 */
@KeepWhenCompacting
public class MidiControlPlayer
	extends AbstractPlayer
{
	/**
	 * {@squirreljme.property cc.squirreljme.midi.device=name/id Use the
	 * selected MIDI device to play audio, if found. May be a name or number.}
	 */
	@KeepWhenCompacting
	public static final String MIDI_DEVICE_PROPERTY =
		"cc.squirreljme.midi.device";
	
	/**
	 * {@squirreljme.property cc.squirreljme.midi.port=id Use the given port
	 * number for the selected MIDI device.}
	 */
	@KeepWhenCompacting
	public static final String MIDI_PORT_PROPERTY =
		"cc.squirreljme.midi.port";
	
	/** The MIDI control to use. */
	private final AbstractMidiControl control;
	
	/**
	 * Initializes the MIDI control player.
	 * 
	 * @param __control The control to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/04/24
	 */
	@KeepWhenCompacting
	public MidiControlPlayer(AbstractMidiControl __control)
		throws NullPointerException
	{
		super("audio/midi");
		
		if (__control == null)
			throw new NullPointerException("NARG");
		
		this.control = __control;
		
		// The control always gets registered
		this.registerControl(__control);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	@KeepWhenCompacting
	public void becomingDeallocated()
	{
		// Nothing needs to be done
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	protected void becomingPrefetched()
		throws MediaException
	{
		// Nothing needs to be done
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected void becomingPrimed()
		throws MediaException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	protected void becomingRealized()
		throws MediaException
	{
		// Nothing needs to be done
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/03
	 */
	@Override
	protected void becomingSolvent()
		throws MediaException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	protected boolean becomingStarted()
		throws MediaException
	{
		// This idles in the background, so do set the state
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	protected void becomingStopped()
		throws MediaException
	{
		// Nothing needs to be done
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/02
	 */
	@Override
	protected void clockFastForward(long __micros)
		throws MediaException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/15
	 */
	@Override
	protected long clockGet()
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/15
	 */
	@Override
	protected void clockSet(long __micros)
		throws MediaException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	protected long determineDuration()
		throws MediaException
	{
		// The duration is always invalid
		return MidiControlPlayer.TIME_UNKNOWN;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/01/02
	 */
	@Override
	protected boolean resetFastForward()
	{
		// This does not make sense here
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/06/03
	 */
	@Override
	protected void useVolume(int __volume)
	{
		// Does nothing
	}
	
	/**
	 * Initializes a new MIDI player.
	 * 
	 * @return A MIDI player.
	 * @throws MediaException If no MIDI is supported.
	 * @since 2022/04/24
	 */
	@KeepWhenCompacting
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
			
		/* {@squirreljme.error EA0d Found MIDI device, however the
		device is not valid. (The device)} */
		throw new MediaException("EA0d " +
			MidiShelf.deviceName(selectedDevice));
	}
}
