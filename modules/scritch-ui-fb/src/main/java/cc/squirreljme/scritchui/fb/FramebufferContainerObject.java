// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchContainerBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Interface for containers within the framebuffer.
 *
 * @since 2024/03/26
 */
@Deprecated
@SquirrelJMEVendorApi
public interface FramebufferContainerObject
{
	/**
	 * Returns the core container.
	 *
	 * @return The core container.
	 * @since 2024/03/26
	 */
	@SquirrelJMEVendorApi
	ScritchContainerBracket __container();
	
	/**
	 * Returns the container manager for this object. 
	 *
	 * @return The container manager for this object.
	 * @since 2024/03/26
	 */
	@SquirrelJMEVendorApi
	FramebufferContainerManager __containerManager();
}
