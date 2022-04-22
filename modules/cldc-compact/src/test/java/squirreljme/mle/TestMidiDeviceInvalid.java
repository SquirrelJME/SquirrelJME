// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle;

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
		this.secondary("key", "value");
	}
}
