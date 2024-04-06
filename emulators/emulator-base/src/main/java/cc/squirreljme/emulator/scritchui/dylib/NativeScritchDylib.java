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
	/** The state pointer. */
	private final long _stateP;
	
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
		long stateP = NativeScritchDylib.__linkInit(
			__libPath.toAbsolutePath().toString(),
			__name.toLowerCase());
		if (stateP == 0)
			throw new MLECallError(String.format(
				"Could not initialize ScritchUI library '%s' (%s)",
				__libPath, __name));
		this._stateP = stateP;
	}
	
	/**
	 * Initializes a new panel.
	 *
	 * @return The newly created panel.
	 * @since 2024/04/06
	 */
	public DylibPanelObject panelNew()
	{
		long panelP = NativeScritchDylib.__panelNew(this._stateP);
		if (panelP == 0)
			throw new MLECallError("Could not create panel.");
		
		return new DylibPanelObject(panelP);
	}
	
	/**
	 * Link in the library and load the given structure pointer.
	 *
	 * @param __libPath The library path.
	 * @param __name The interface name.
	 * @return The resultant ScritchUI state pointer.
	 * @since 2024/03/31
	 */
	private static native long __linkInit(String __libPath, String __name);
	
	/**
	 * Initializes a new panel. 
	 *
	 * @param __stateP The state pointer.
	 * @return The pointer to the panel.
	 * @since 2024/04/06
	 */
	private static native long __panelNew(long __stateP);
}
