// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui;

import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFElementColor;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFImageElementType;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLineStyle;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.Range;

/**
 * Look and feel information within ScritchUI.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public interface ScritchLAFInterface
{
	/**
	 * Returns the color of the given element.
	 *
	 * @param __element One of {@link ScritchLAFElementColor}.
	 * @return A 32-bit ARGB color.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	int elementColor(
		@MagicConstant(valuesFromClass = ScritchLAFElementColor.class)
		int __element);
	
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
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	@Range(from = 0, to = Integer.MAX_VALUE)
	int imageSize(
		@MagicConstant(valuesFromClass = ScritchLAFImageElementType.class)
		int __elem, boolean __height);
}
