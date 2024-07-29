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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchViewBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.Async;

/**
 * Listener for size suggestions for viewports
 *
 * @since 2024/07/29
 */
@SquirrelJMEVendorApi
public interface ScritchSizeSuggestListener
	extends ScritchListener
{
	/**
	 * This is called when a component has a suggested size for a view.
	 *
	 * @param __view The view this is for.
	 * @param __subComponent The component that is suggesting a size.
	 * @param __x The X coordinate of the component.
	 * @param __y The Y coordinate of the component.
	 * @param __w The component width.
	 * @param __h The component height.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/29
	 */
	@SquirrelJMEVendorApi
	@ScritchEventLoop
	void sizeSuggest(ScritchViewBracket __view,
		ScritchComponentBracket __subComponent,
		int __x, int __y, int __w, int __h)
		throws NullPointerException;
}
