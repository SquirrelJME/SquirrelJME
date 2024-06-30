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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchInputListener;
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
	 * @param __default Should default focus be used?
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/24
	 */
	@SquirrelJMEVendorApi
	void enableFocus(ScritchPanelBracket __panel, boolean __enabled,
		boolean __default)
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
	
	/**
	 * Sets the listener for input events.
	 *
	 * @param __panel The panel to set for.
	 * @param __listener The listener to call to, {@code null} will clear it.
	 * @throws MLECallError If the component is not valid or if the listener
	 * could not be set.
	 * @since 2024/06/30
	 */
	@SquirrelJMEVendorApi
	void setInputListener(@NotNull ScritchPanelBracket __panel,
		@Nullable ScritchInputListener __listener)
		throws MLECallError;
}
