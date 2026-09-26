// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui.extra;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Extra state pairing.
 *
 * @since 2026/09/25
 */
final class __Pair__
{
	/** The owning reference. */
	final Reference<Object> _ref;
	
	/** The paired extra state. */
	final ExtraState _state;
	
	/**
	 * Initializes the extra state pair.
	 *
	 * @param __o The owning object.
	 * @param __state The paired extra state.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/09/25
	 */
	__Pair__(Object __o, ExtraState __state)
		throws NullPointerException
	{
		if (__o == null || __state == null)
			throw new NullPointerException("NARG");
		
		this._ref = new WeakReference<>(__o);
		this._state = __state;
	}
}
