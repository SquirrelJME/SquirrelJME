// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

/**
 * Shaded main entry point for the virtual machine.
 *
 * @since 2018/11/17
 */
public class VMEntryShadedMain
{
	/**
	 * Main entry class.
	 *
	 * @param __args Program arguments.
	 * @since 2018/11/16
	 */
	public static void main(String... __args)
	{
		VMFactory.shadedMain(__args);
	}
}

