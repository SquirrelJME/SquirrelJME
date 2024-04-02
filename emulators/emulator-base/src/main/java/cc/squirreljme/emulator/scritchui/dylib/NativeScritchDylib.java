// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import java.nio.file.Path;

/**
 * Native dynamic library that directly wraps the C-based ScritchUI API.
 *
 * @since 2024/03/29
 */
public final class NativeScritchDylib
{
	/** Structure pointer. */
	private final long _structP;
	
	/**
	 * Initializes the native library layer for ScritchUI.
	 *
	 * @param __libPath The library path to load.
	 * @param __name The name of the ScritchUI interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/30
	 */
	public NativeScritchDylib(Path __libPath, String __name)
		throws NullPointerException
	{
		if (__libPath == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Link in native library and locate the structure
		long structP = NativeScritchDylib.__link(
			__libPath.toAbsolutePath().toString(),
			__name.toLowerCase());
		if (structP == 0)
			throw new MLECallError(String.format(
				"No native structure found in library '%s' (%s)",
				__libPath, __name));
		this._structP = structP;
	}
	
	/**
	 * Initializes the ScritchUI API.
	 *
	 * @return The resultant state pointer.
	 * @since 2024/04/02
	 */
	public long apiInit()
	{
		return NativeScritchDylib.__apiInit(this._structP);
	}
	
	/**
	 * Initializes the ScritchUI API.
	 *
	 * @param __structP The API structure pointer.
	 * @return The resultant state pointer.
	 * @since 2024/04/02
	 */
	private static native long __apiInit(long __structP);
	
	/**
	 * Link in the library and load the given structure pointer.
	 *
	 * @param __libPath The library path.
	 * @param __name The interface name.
	 * @return The resultant struct implementation pointer.
	 * @since 2024/03/31
	 */
	private static native long __link(String __libPath, String __name);
}
