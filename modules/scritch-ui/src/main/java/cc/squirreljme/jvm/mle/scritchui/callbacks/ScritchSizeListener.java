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
 * Listener for resize events within ScritchUI.
 *
 * @since 2024/04/28
 */
@SquirrelJMEVendorApi
public interface ScritchSizeListener
	extends ScritchListener
{
	/**
	 * This is called when the given component has changed size.
	 *
	 * @param __component The component that changed size.
	 * @param __newWidth The new width.
	 * @param __newHeight The new height.
	 * @since 2024/04/28
	 */
	@SquirrelJMEVendorApi
	@ScritchEventLoop
	void sizeChanged(ScritchComponentBracket __component,
		int __newWidth, int __newHeight);
}
