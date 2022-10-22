// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.bus;

import cc.squirreljme.jvm.mle.BusTransportShelf;
import cc.squirreljme.jvm.mle.brackets.BusTransportBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class manages the primary bus.
 *
 * @since 2022/10/18
 */
public class PrimaryBusManager
{
	/**
	 * Main entry point for the bus manager.
	 * 
	 * @param __args Arguments to the bus manager.
	 * @since 2022/10/18
	 */
	public static void main(String... __args)
	{
		// Get the primary bus we will be listening on for connections
		BusTransportBracket primary = BusTransportShelf.primary();
		
		throw Debugging.todo();
	}
}
