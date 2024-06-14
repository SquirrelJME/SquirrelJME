// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.brackets.PencilFontBracket;
import cc.squirreljme.jvm.mle.constants.PencilFontStyle;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchWindowManagerType;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Interface which describes the environment ScritchUI is running under.
 *
 * @since 2024/03/07
 */
@SquirrelJMEVendorApi
public interface ScritchEnvironmentInterface
{
	/**
	 * Returns all the fonts which are internally built into the UI
	 * interface.
	 *
	 * @return The internal built-in fonts.
	 * @since 2024/06/12
	 */
	@SquirrelJMEVendorApi
	@NotNull
	PencilFontBracket[] builtinFonts();
	
	/**
	 * Derives the given font.
	 *
	 * @param __font The font to derive.
	 * @param __style The new style to select.
	 * @param __pixelSize The pixel size of the font.
	 * @return The resultant font.
	 * @throws MLECallError On null arguments, if the style is not valid,
	 * or the pixel size is zero or negative.
	 * @since 2024/06/14
	 */
	@SquirrelJMEVendorApi
	@NotNull
	PencilFontBracket fontDerive(@NotNull PencilFontBracket __font,
		@MagicConstant(flagsFromClass = PencilFontStyle.class) int __style,
		@Range(from = 1, to = Integer.MAX_VALUE) int __pixelSize)
		throws MLECallError;
	
	/**
	 * Is this inhibiting sleep and/or screensaver? 
	 *
	 * @return If sleep is being inhibited.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	boolean isInhibitingSleep();
	
	/**
	 * Returns the look and feel interface.
	 *
	 * @return The look and feel interface.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	@NotNull
	ScritchLAFInterface lookAndFeel();
	
	/**
	 * Returns the screen interface for each screen.
	 *
	 * @return The screen interface for each screen.
	 * @since 2024/03/07
	 */
	@NotNull
	@SquirrelJMEVendorApi
	ScritchScreenBracket[] screens();
	
	/**
	 * Sets whether sleep and/or screen saver should be inhibited.
	 *
	 * @param __inhibit If sleep and/or screen saver should be inhibited.
	 * @since 2024/03/09
	 */
	void setInhibitSleep(boolean __inhibit);
	
	/**
	 * Returns the type of window manager ScritchUI is running on.
	 *
	 * @return One of {@link ScritchWindowManagerType}.
	 * @see ScritchWindowManagerType
	 * @since 2024/03/07
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = ScritchWindowManagerType.NUM_TYPES)
	@MagicConstant(valuesFromClass = ScritchWindowManagerType.class)
	int windowManagerType();
}
