// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

/**
 * Not Described
 *
 * @since 2020/02/29
 */
public final class Poking
{
	/** Has this been poked? */
	private static volatile boolean _POKED =
		false;
	
	/**
	 * Attempts to poke some native portions of SquirrelJME so that they start.
	 *
	 * @since 2020/02/29
	 */
	public static void poke()
	{
		// Only poke once!
		if (Poking._POKED)
			return;
		Poking._POKED = true;
		
		// We might be running on the emulator, if we are try to poke the
		// NativeBinding class so that way our native calls can be initialized
		try
		{
			Class.forName("cc.squirreljme.emulator.NativeBinding")
				.newInstance();
		}
		catch (ClassNotFoundException|IllegalAccessException|
			InstantiationException ignored)
		{
		}
	}
}
