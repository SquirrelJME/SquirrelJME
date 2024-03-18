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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchInputMethodType;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

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
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	void callAttention(@NotNull ScritchWindowBracket __window)
		throws MLECallError;
	
	/**
	 * Returns the height of the content area, that is what is used solely
	 * by widgets and not any window decorations or otherwise. 
	 *
	 * @param __window The window to get the content height of.
	 * @return The height of the content area.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/18
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	int contentHeight(@NotNull ScritchWindowBracket __window)
		throws MLECallError;
	
	/**
	 * Returns the width of the content area, that is what is used solely
	 * by widgets and not any window decorations or otherwise. 
	 *
	 * @param __window The window to get the content width of.
	 * @return The width of the content area.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/18
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	int contentWidth(@NotNull ScritchWindowBracket __window)
		throws MLECallError;
	
	/**
	 * Does this window have focus? 
	 *
	 * @param __window The window to query.
	 * @return If the window has focus.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	boolean hasFocus(@NotNull ScritchWindowBracket __window)
		throws MLECallError;
	
	/**
	 * Is this window visible.
	 *
	 * @param __window The window to query.
	 * @return If the window is visible.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	boolean isVisible(@NotNull ScritchWindowBracket __window)
		throws MLECallError;
	
	/**
	 * Returns the {@link ScritchInputMethodType}s that are possible for
	 * this specific window.
	 *
	 * @param __window The window to check.
	 * @return The valid {@link ScritchInputMethodType}s.
	 * @throws MLECallError On null arguments.
	 * @since 2024/03/11
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = ScritchInputMethodType.class)
	int inputTypes(@NotNull ScritchWindowBracket __window)
		throws MLECallError;
	
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
