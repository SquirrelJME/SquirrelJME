// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui.dylib;

import java.lang.ref.Reference;

/**
 * Base interfaces for the Dylib ScritchUI interface.
 *
 * @since 2024/04/02
 */
public abstract class DylibBaseInterface
{
	/** Dynamic library interface. */
	protected final NativeScritchDylib dyLib;
	
	/** State pointer. */
	protected final long stateP;
	
	/** Reference to our own API. */
	private final Reference<DylibScritchInterface> _selfApi;
	
	/**
	 * Initializes the base interface.
	 *
	 * @param __selfApi Reference to our own API.
	 * @param __dyLib The dynamic library interface.
	 * @param __stateP The current state pointer.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/04/02
	 */
	public DylibBaseInterface(Reference<DylibScritchInterface> __selfApi,
		NativeScritchDylib __dyLib, long __stateP)
		throws NullPointerException
	{
		if (__selfApi == null || __dyLib == null || __stateP == 0)
			throw new NullPointerException("NARG");
		
		this._selfApi = __selfApi;
		this.dyLib = __dyLib;
		this.stateP = __stateP;
	}
}
