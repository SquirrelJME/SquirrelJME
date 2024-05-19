// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile;

import cc.squirreljme.emulator.NativeBinding;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;
import javax.swing.JFrame;

/**
 * Main entry point for the font compiler.
 *
 * @since 2024/05/17
 */
public class Main
{
	static
	{
		// We need to poke native binding, so it loads our emulation backend
		NativeBinding.loadedLibraryPath();
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Input arguments.
	 * @since 2024/05/17
	 */
	public static void main(String... __args)
	{
		throw Debugging.todo(Arrays.asList(__args));
	}
}
