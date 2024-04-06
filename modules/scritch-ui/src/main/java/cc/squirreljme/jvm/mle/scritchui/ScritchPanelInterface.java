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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for panels.
 *
 * @since 2024/03/16
 */
@SquirrelJMEVendorApi
public interface ScritchPanelInterface
	extends ScritchPaintableInterface
{
	/**
	 * Enables or disables focus on the panel along with traversal of tabs.
	 *
	 * @param __panel The panel to modify.
	 * @param __enabled Whether focus and tab traversal should be enabled.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/24
	 */
	@SquirrelJMEVendorApi
	void enableFocus(@NotNull ScritchPanelBracket __panel, boolean __enabled)
		throws MLECallError;
	
	/**
	 * Creates a new panel.
	 *
	 * @return The newly created panel.
	 * @throws MLECallError If the panel could not be created.
	 * @since 2024/03/16
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchPanelBracket newPanel()
		throws MLECallError;
}
