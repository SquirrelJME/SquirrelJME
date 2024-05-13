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
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;

/**
 * Base class for all framebuffer object types.
 *
 * @since 2024/03/26
 */
@Deprecated
@SquirrelJMEVendorApi
public abstract class FramebufferBaseObject
{
	/** The core API to be based upon. */
	protected final ScritchInterface coreApi;
	
	/** Reference to our own API set. */
	private final Reference<FramebufferScritchInterface> _selfApi;
	
	/**
	 * Initializes the base object.
	 *
	 * @param __selfApi The reference to our own API.
	 * @param __coreApi The core API for accessing wrapped objects.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/26
	 */
	public FramebufferBaseObject(
		Reference<FramebufferScritchInterface> __selfApi,
		ScritchInterface __coreApi)
		throws NullPointerException
	{
		if (__selfApi == null || __coreApi == null)
			throw new NullPointerException("NARG");
		
		this._selfApi = __selfApi;
		this.coreApi = __coreApi;
	}
	
	/**
	 * Returns the self API.
	 *
	 * @return The self API.
	 * @throws IllegalStateException If it was garbage collected.
	 * @since 2024/03/26
	 */
	final FramebufferScritchInterface __selfApi()
		throws IllegalStateException
	{
		FramebufferScritchInterface result = this._selfApi.get();
		if (result == null)
			throw new IllegalStateException("GCGC");
		
		return result;
	}	
}
