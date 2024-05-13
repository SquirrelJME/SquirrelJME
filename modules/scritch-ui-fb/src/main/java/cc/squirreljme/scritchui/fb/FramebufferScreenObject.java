// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;

/**
 * Wraps a single screen.
 *
 * @since 2024/03/26
 */
@Deprecated
@SquirrelJMEVendorApi
public class FramebufferScreenObject
	extends FramebufferBaseObject
	implements ScritchScreenBracket
{
	/** The core screen we are wrapping. */
	private final ScritchScreenBracket _coreScreen;
	
	/** The screen identifier. */
	final int _screenId;
	
	/**
	 * Initializes the wrapped screen.
	 *
	 * @param __selfApi Our own API.
	 * @param __coreApi The core API we are wrapping.
	 * @param __coreScreen The core screen interface.
	 * @param __screenId The ID of this screen.
	 * @since 2024/03/26
	 */
	public FramebufferScreenObject(
		Reference<FramebufferScritchInterface> __selfApi,
		ScritchInterface __coreApi, ScritchScreenBracket __coreScreen,
		int __screenId)
	{
		super(__selfApi, __coreApi);
		
		if (__coreScreen == null)
			throw new NullPointerException("NARG");
		
		// Store for later
		this._coreScreen = __coreScreen;
		this._screenId = __screenId;
	}
}
