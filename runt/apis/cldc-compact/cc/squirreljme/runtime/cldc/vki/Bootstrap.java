// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.vki;

/**
 * This class is responsible for bootstrapping the SquirrelJME environment
 * and initializing everything properly.
 *
 * @since 2019/04/20
 */
public final class Bootstrap
{
	/**
	 * Not used.
	 *
	 * @since 2019/04/20
	 */
	private Bootstrap()
	{
	}
	
	/**
	 * This is the booting point for the SquirrelJME kernel, it is
	 *
	 * @param __int The pointer which contains the ROM address.
	 * @param __cfg Kernel configuration space, this configures the VM and
	 * how it is to enter.
	 * @param __mbp The memory base pointer.
	 * @param __msz The amount of memory which is available.
	 * @since 2019/04/20
	 */
	private static final void __boot(int __rom, int __cfg,
		int __mbp, int __msz)
	{
		Assembly.fatalExit();
	}
}

