// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Main entry point for the compiler runner.
 *
 * @since 2020/11/21
 */
public class Main
{
	/**
	 * Main entry point for the compiler interface.
	 * 
	 * @param __args Arguments to the main class.
	 * @since 2020/11/2
	 */
	public static void main(String... __args)
	{
		System.err.println("Hello AOT?");
		
		throw Debugging.todo("AOT Main");
	}
}
