// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle;

import cc.squirreljme.jvm.mle.MidiShelf;
import cc.squirreljme.jvm.mle.brackets.MidiDeviceBracket;
import cc.squirreljme.jvm.mle.brackets.MidiPortBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests for invalid parameters for MIDI devices.
 *
 * @since 2022/04/21
 */
public class TestMidiDeviceInvalid
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/04/21
	 */
	@Override
	public void test()
	{
		// Find device to test with
		MidiDeviceBracket[] devices = MidiShelf.devices();
		MidiDeviceBracket device = (devices.length > 0 ? devices[0] : null);
		
		// Get ports, if available
		MidiPortBracket[] receivers = (device == null ?
			new MidiPortBracket[0] : MidiShelf.ports(device, false));
		MidiPortBracket[] transmitters = (device == null ?
			new MidiPortBracket[0] : MidiShelf.ports(device, true));
		
		// Fine port to use
		MidiPortBracket receiver = (receivers.length > 0 ?
			receivers[0] : null);
		MidiPortBracket transmitter = (transmitters.length > 0 ?
			transmitters[0] : null);
		
		// Buffer
		byte[] buf = new byte[3];
		
		for (int i = 0;; i++)
			try
			{
				switch (i)
				{
					case 0:
						MidiShelf.deviceName(null);
						break;
					
					case 1:
						MidiShelf.ports(null, false);
						break;
					
					case 2:
						MidiShelf.ports(null, true);
						break;
					
					case 3:
						MidiShelf.dataTransmit(null,
							buf, 0, buf.length);
						break;
					
					case 4:
						MidiShelf.dataTransmit(transmitter,
							null, 0, buf.length);
						break;
					
					case 5:
						MidiShelf.dataTransmit(transmitter,
							buf, -1, buf.length);
						break;
					
					case 6:
						MidiShelf.dataTransmit(transmitter,
							buf, 0, -1);
						break;
					
					case 7:
						MidiShelf.dataTransmit(transmitter,
							buf, buf.length + 1, buf.length + 1);
						break;
					
					case 8:
						MidiShelf.dataReceive(null,
							buf, 0, buf.length);
						break;
					
					case 9:
						MidiShelf.dataReceive(receiver,
							null, 0, buf.length);
						break;
					
					case 10:
						MidiShelf.dataReceive(receiver,
							buf, -1, buf.length);
						break;
					
					case 11:
						MidiShelf.dataReceive(receiver,
							buf, 0, -1);
						break;
					
					case 12:
						MidiShelf.dataReceive(receiver,
							buf, buf.length + 1, buf.length + 1);
						break;
					
					default:
						return;
				}
				
				// Should not be reached
				this.secondary("fail", i);
				throw new RuntimeException("fail");
			}
			catch (MLECallError ignored)
			{
			}
	}
}
