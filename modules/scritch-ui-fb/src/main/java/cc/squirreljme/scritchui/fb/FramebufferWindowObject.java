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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a wrapped native window.
 *
 * @since 2024/03/26
 */
@SquirrelJMEVendorApi
public class FramebufferWindowObject
	extends FramebufferBaseObject
	implements ScritchWindowBracket
{
	/** The core window we are wrapping. */
	private final ScritchWindowBracket _coreWindow;
	
	/**
	 * Initializes the window object.
	 *
	 * @param __selfApi The reference to our own API.
	 * @param __coreApi The core API for accessing wrapped objects.
	 * @param __coreWindow The core window to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/26
	 */
	@SquirrelJMEVendorApi
	public FramebufferWindowObject(
		Reference<FramebufferScritchInterface> __selfApi,
		ScritchInterface __coreApi,
		ScritchWindowBracket __coreWindow)
		throws NullPointerException
	{
		super(__selfApi, __coreApi);
		
		if (__coreWindow == null)
			throw new NullPointerException("NARG");
		
		this._coreWindow = __coreWindow;
	}
}
