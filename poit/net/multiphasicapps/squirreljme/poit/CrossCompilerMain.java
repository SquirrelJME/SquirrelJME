// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.poit;

import net.multiphasicapps.squirreljme.zips.ZIPFile;

/**
 * Main entry point for the cross compiler using a real host virtual machine.
 *
 * @since 2016/02/26
 */
public class CrossCompilerMain
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/02/26
	 */
	public static void main(String... __args)
	{
		// Force arguments to exist
		if (__args == null)
			__args = new String[0];
		
		// Not enough arguments?
		if (__args.length <= 2)
			throw new IllegalArgumentException("Usage: (os) (arch) (JARs...)");
		
		throw new Error("TODO");
	}
}

