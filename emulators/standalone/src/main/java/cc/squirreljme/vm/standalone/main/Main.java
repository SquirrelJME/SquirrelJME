// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.standalone.main;

import cc.squirreljme.emulator.vm.VMFactory;

/**
 * Main entry point for standalone virtual machine.
 *
 * @since 2022/06/13
 */
public class Main
{
	/**
	 * Main entry point for the virtual machine.
	 * 
	 * @param __args Arguments to the program.
	 * @since 2022/06/13
	 */
	public static void main(String... __args)
	{
		// Just forward straight to VMFactory since it does everything
		VMFactory.main(__args);
	}
}
