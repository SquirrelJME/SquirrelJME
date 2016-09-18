// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

/**
 * This is the bootstrap which builds the bootstrap system and then launches
 * it. It bridges the bootstrap compiler and launcher system to the host
 * Jave SE VM.
 *
 * @since 2016/09/18
 */
public class Bootstrap
{
	/**
	 * Main entry point for the bootstrap bootstrapper.
	 *
	 * @param __args Program arguments.
	 * @since 2016/09/18
	 */
	public static void main(String... __args)
	{
		// Force
		if (__args == null)
			__args = new String[0];
		
		throw new Error("TODO");
	}
}

