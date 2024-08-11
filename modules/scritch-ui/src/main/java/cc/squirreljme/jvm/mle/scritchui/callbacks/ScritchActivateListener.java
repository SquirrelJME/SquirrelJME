// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.callbacks;

import cc.squirreljme.jvm.mle.scritchui.annotation.ScritchEventLoop;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This is called when an item has been activated.
 *
 * @since 2024/07/17
 */
@SquirrelJMEVendorApi
public interface ScritchActivateListener
	extends ScritchListener
{
	/**
	 * This is called when the component has been activated.
	 *
	 * @param __component The component that was activated.
	 * @since 2024/07/28
	 */
	@SquirrelJMEVendorApi
	@ScritchEventLoop
	void activate(ScritchComponentBracket __component);
}
