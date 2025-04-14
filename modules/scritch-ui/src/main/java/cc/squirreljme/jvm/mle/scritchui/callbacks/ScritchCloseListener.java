// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.callbacks;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.annotation.ScritchEventLoop;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Listener which is called when a window is requested to be closed.
 *
 * @since 2024/05/13
 */
@SquirrelJMEVendorApi
public interface ScritchCloseListener
	extends ScritchListener
{
	/**
	 * This is called when a window is closed.
	 *
	 * @param __window The window being closed.
	 * @return Return {@code true} to cancel a close.
	 * @throws MLECallError If the window could not be closed.
	 * @since 2024/05/13
	 */
	@SquirrelJMEVendorApi
	@ScritchEventLoop
	boolean closed(ScritchWindowBracket __window)
		throws MLECallError;
}
