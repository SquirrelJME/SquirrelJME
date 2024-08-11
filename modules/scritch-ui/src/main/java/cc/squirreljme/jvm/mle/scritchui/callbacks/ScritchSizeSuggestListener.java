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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	 * @param __subComponent The component that is suggesting a size, may
	 * be {@code null}.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/29
	 */
	@SquirrelJMEVendorApi
	@ScritchEventLoop
	void sizeSuggest(@NotNull ScritchViewBracket __view,
		@Nullable ScritchComponentBracket __subComponent,
		int __w, int __h)
		throws NullPointerException;
}
