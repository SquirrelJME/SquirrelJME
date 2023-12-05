// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.NativeBinding;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Native library loader.
 *
 * @since 2023/12/05
 */
final class __Native__
{
	/** The path where the library exists. */
	private static volatile Path _libPath;
	
	/**
	 * Binds methods accordingly.
	 *
	 * @since 2023/12/05
	 */
	private static native int __bindMethods();
	
	/**
	 * Loads the native library.
	 *
	 * @throws IOException On read/write errors.
	 * @return The path to the library, where it exists on disk.
	 * @since 2023/12/05
	 */
	static Path __loadLibrary()
		throws IOException
	{
		synchronized (__Native__.class)
		{
			if (__Native__._libPath == null)
			{
				__Native__._libPath = NativeBinding.libFromResources(
					"emulator-nanocoat");
			}
			
			// Load and initialize
			System.load(__Native__._libPath.toAbsolutePath().toString());
			__Native__.__bindMethods();
			
			return __Native__._libPath;
		}
	}
}
