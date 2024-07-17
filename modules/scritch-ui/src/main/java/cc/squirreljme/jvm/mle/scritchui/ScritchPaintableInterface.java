// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPaintableBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for anything that can be painted on.
 *
 * @since 2024/03/19
 */
@SquirrelJMEVendorApi
public interface ScritchPaintableInterface
	extends ScritchApiInterface
{
	/**
	 * Repaints the given component.
	 *
	 * @param __component The component to repaint.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/19
	 */
	@SquirrelJMEVendorApi
	void repaint(@NotNull ScritchComponentBracket __component)
		throws MLECallError;
	
	/**
	 * Sets the paint listener for when a paint should occur.
	 *
	 * @param __component The panel to paint inside.
	 * @param __listener The listener which is called on painting events.
	 * @throws MLECallError If {@code __panel} is {@code null}.
	 * @since 2024/03/19
	 */
	@SquirrelJMEVendorApi
	void setPaintListener(@NotNull ScritchPaintableBracket __component,
		@Nullable ScritchPaintListener __listener)
		throws MLECallError;
}
