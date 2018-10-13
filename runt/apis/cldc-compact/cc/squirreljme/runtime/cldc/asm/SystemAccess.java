// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

/**
 * Access to system related details.
 *
 * @since 2018/10/13
 */
public final class SystemAccess
{
	/**
	 * Not used.
	 *
	 * @since 2018/10/13
	 */
	private SystemAccess()
	{
	}
	
	/**
	 * Exits the process with the system exit code.
	 *
	 * @param __code The exit code.
	 * @since 2018/10/13
	 */
	public static final native void exit(int __code);
}

