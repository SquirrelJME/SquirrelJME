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
 * Base class for all Framebuffer ScritchUI Interfaces.
 *
 * @since 2024/03/24
 */
@Deprecated
@SquirrelJMEVendorApi
public abstract class FramebufferBaseInterface
{
	/** The core interface. */
	@SquirrelJMEVendorApi
	protected final ScritchInterface coreApi;
	
	/** The framebuffer self interface. */
	@SquirrelJMEVendorApi
	protected final Reference<FramebufferScritchInterface> selfApi;
	
	/**
	 * Initializes this interface. 
	 *
	 * @param __self The framebuffer self interface.
	 * @param __core The core interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/24
	 */
	@SquirrelJMEVendorApi
	public FramebufferBaseInterface(
		Reference<FramebufferScritchInterface> __self,
		ScritchInterface __core)
		throws NullPointerException
	{
		if (__self == null || __core == null)
			throw new NullPointerException("NARG");
		
		// Store for later usage
		this.selfApi = __self;
		this.coreApi = __core;
	}
}
