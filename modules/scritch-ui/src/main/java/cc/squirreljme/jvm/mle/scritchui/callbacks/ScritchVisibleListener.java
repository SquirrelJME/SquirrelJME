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
 * Listener for changes in component visibility.
 *
 * @since 2024/06/28
 */
@SquirrelJMEVendorApi
public interface ScritchVisibleListener
	extends ScritchListener
{
	/**
	 * Called when a component's visibility has changed. 
	 *
	 * @param __component The component with changed visibility.
	 * @param __from The previous visibility.
	 * @param __to The current visibility.
	 * @since 2024/06/28
	 */
	@SquirrelJMEVendorApi
	@ScritchEventLoop
	void visibilityChanged(ScritchComponentBracket __component,
		boolean __from, boolean __to);
}
