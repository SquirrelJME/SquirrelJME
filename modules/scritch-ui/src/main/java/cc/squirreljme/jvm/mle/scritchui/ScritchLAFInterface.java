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
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFElementColor;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFFontElementType;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFImageElementType;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLineStyle;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Look and feel information within ScritchUI.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public interface ScritchLAFInterface
	extends ScritchApiInterface
{
	/**
	 * Looks up the given font for a given element.
	 *
	 * @param __element The element to get the front from.
	 * @return The resultant font or {@code null} if it is unknown.
	 * @throws MLECallError If the requested element is not valid.
	 * @since 2024/05/17
	 */
	@SquirrelJMEVendorApi
	@Nullable
	PencilFontBracket font(
		@MagicConstant(valuesFromClass = ScritchLAFFontElementType.class)
			int __element)
		throws MLECallError;
	
	/**
	 * Returns the color of the given element.
	 *
	 * @param __context The context widget to get the style from, otherwise
	 * this will use a default style.
	 * @param __element One of {@link ScritchLAFElementColor}.
	 * @return A 32-bit ARGB color.
	 * @throws MLECallError If the constant is not valid.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	int elementColor(@Nullable ScritchComponentBracket __context,
		@MagicConstant(valuesFromClass = ScritchLAFElementColor.class)
		int __element)
		throws MLECallError;
	
	/**
	 * Returns the border style for focused items.
	 *
	 * @param __focused If the style should be one for focused items.
	 * @return The resultant border style, one of {@link ScritchLineStyle}.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = ScritchLineStyle.class)
	int focusBorderStyle(boolean __focused);
	
	/**
	 * Returns the image size that best represents the given element.
	 *
	 * @param __elem The element to get.
	 * @param __height Should the height be returned? If not then the width
	 * is returned.
	 * @return Either the width or the height depending on {@code __height}.
	 * @throws MLECallError If the element is not valid.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	int imageSize(
		@MagicConstant(valuesFromClass = ScritchLAFImageElementType.class)
		int __elem, boolean __height)
		throws MLECallError;
	
	/**
	 * Returns whether dark mode is currently in use.
	 *
	 * @return If dark mode is in use.
	 * @since 2024/03/24
	 */
	@SquirrelJMEVendorApi
	boolean isDarkMode();
	
	/**
	 * Returns the color that is used for the background of panels.
	 *
	 * @return The panel background color.
	 * @since 2024/03/19
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = 0xFFFFFF)
	int panelBackgroundColor();
	
	/**
	 * Returns the color that is used for the foreground of panels.
	 *
	 * @return The panel foreground color.
	 * @since 2024/03/19
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = 0xFFFFFF)
	int panelForegroundColor();
}
