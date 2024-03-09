// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFElementColor;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFImageElementType;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLineStyle;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Utilities for LCDUI on ScritchUI.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public final class ScritchLcdUiUtils
{
	/**
	 * Not used.
	 *
	 * @since 2024/03/09
	 */
	private ScritchLcdUiUtils()
	{
	}
	
	/**
	 * Maps the line style. 
	 *
	 * @param __style The input line style, one of {@link ScritchLineStyle}.
	 * @return A line style from {@link Graphics}.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = Graphics.class)
	public static int lcduiLineStyle(
		@MagicConstant(valuesFromClass = ScritchLineStyle.class) int __style)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Maps {@link Display} element color types
	 * to {@link ScritchLAFElementColor}. 
	 *
	 * @param __c One of {@link Display}'s element color types.
	 * @return One of {@link ScritchLAFElementColor}.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	@MagicConstant(valuesFromClass = ScritchLAFElementColor.class)
	public static int scritchElementColor(
		@MagicConstant(valuesFromClass = Display.class) int __c)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Maps a display element type to ScritchUI.
	 *
	 * @param __in The input {@link Display} element.
	 * @return The resultant mapped type to {@link ScritchLAFImageElementType}.
	 * @since 2024/03/09
	 */
	@MagicConstant(valuesFromClass = ScritchLAFImageElementType.class)
	@SquirrelJMEVendorApi
	public static int scritchElementType(
		@MagicConstant(valuesFromClass = Display.class) int __in)
	{
		// ScritchLAFImageElementType
		throw Debugging.todo();
	}
}
