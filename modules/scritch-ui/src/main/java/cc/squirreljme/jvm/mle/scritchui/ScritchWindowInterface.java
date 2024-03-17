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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchInputMethodType;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for {@link ScritchWindowBracket}.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public interface ScritchWindowInterface
{
	/**
	 * Calls attention to this window, it may be through whatever means
	 * the operating system performs.
	 *
	 * @param __window The window to query.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	void callAttention(@NotNull ScritchWindowBracket __window);
	
	/**
	 * Does this window have focus? 
	 *
	 * @param __window The window to query.
	 * @return If the window has focus.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	boolean hasFocus(@NotNull ScritchWindowBracket __window);
	
	/**
	 * Is this window visible.
	 *
	 * @param __window The window to query.
	 * @return If the window is visible.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	boolean isVisible(@NotNull ScritchWindowBracket __window);
	
	/**
	 * Returns the {@link ScritchInputMethodType}s that are possible for
	 * this specific window.
	 *
	 * @param __window The window to check.
	 * @return The valid {@link ScritchInputMethodType}s.
	 * @since 2024/03/11
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = ScritchInputMethodType.class)
	int inputTypes(@NotNull ScritchWindowBracket __window);
	
	/**
	 * Creates a new empty window.
	 *
	 * @return A new empty window.
	 * @since 2024/03/13
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchWindowBracket newWindow();
	
	/**
	 * Sets the visibility of the given window.
	 *
	 * @param __window The window to set.
	 * @param __visible Should the window be visible?
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/17
	 */
	@SquirrelJMEVendorApi
	void setVisible(@NotNull ScritchWindowBracket __window, boolean __visible)
		throws MLECallError;
}
