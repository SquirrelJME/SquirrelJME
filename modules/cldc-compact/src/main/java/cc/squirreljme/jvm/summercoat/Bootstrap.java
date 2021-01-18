// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.runtime.cldc.SquirrelJME;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Main bootstrap entry point.
 *
 * @since 2020/11/27
 */
public final class Bootstrap
{
	/**
	 * Main entry point for the virtual machine.
	 * 
	 * @since 2020/11/28
	 */
	public static void vmEntry()
	{
		// Introduction banner for the virtual machine itself
		Debugging.notice("SquirrelJME %s",
			SquirrelJME.RUNTIME_VERSION);
		Debugging.notice("(C) 2013-2021 Stephanie Gawroriski");
		Debugging.notice("Licensed under the GPLv3!");
		Debugging.notice("E-Mail : xerthesquirrel@gmail.com");
		Debugging.notice("Website: https://squirreljme.cc/");
		Debugging.notice("Donate!: https://patreon.com/SquirrelJME");
		
		Assembly.breakpoint();
		throw Debugging.todo();
	}
}
