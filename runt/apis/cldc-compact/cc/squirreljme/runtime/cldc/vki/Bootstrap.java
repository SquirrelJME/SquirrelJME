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
	 * @param __cfg Kernel configuration space, this configures the VM and
	 * all of the various properties needed to initialize it properly.
	 * @since 2019/04/20
	 */
	private static final strictfp void __start(JVMConfiguration __cfg)
	{
		Assembly.fatalExit();
	}
}

