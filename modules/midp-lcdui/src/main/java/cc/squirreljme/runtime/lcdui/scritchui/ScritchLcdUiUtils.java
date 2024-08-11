// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchComponentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFElementColor;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFImageElementType;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLineStyle;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.debug.ErrorCode;
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
	 * Determines the size of the display that should be used.
	 *
	 * @param __state The current displayable state.
	 * @param __height Request the height?
	 * @return The size of the display.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/18
	 */
	public static int lcduiDisplaySize(DisplayableState __state,
		boolean __height)
		throws NullPointerException
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		ScritchPanelBracket panel = __state.panel;
		ScritchInterface scritchApi = __state.scritchApi;
		
		// Use the actual panel size here
		DisplayState display = __state.currentDisplay();
		if (display != null)
		{
			ScritchComponentInterface componentApi = scritchApi.component();
			
			if (__height)
				return componentApi.componentGetHeight(panel);
			return componentApi.componentWidth(panel);
		}
		
		// Otherwise use the default display size
		else
		{
			Display firstDisplay = Display.getDisplays(0)[0];
			if (__height)
				return firstDisplay.getWidth();
			return firstDisplay.getWidth();
		}
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
		switch (__c)
		{
			case Display.COLOR_BACKGROUND:
			case Display.COLOR_IDLE_BACKGROUND:
				return ScritchLAFElementColor.BACKGROUND;
				
			case Display.COLOR_BORDER:
				return ScritchLAFElementColor.BORDER;
				
			case Display.COLOR_FOREGROUND:
			case Display.COLOR_IDLE_FOREGROUND:
				return ScritchLAFElementColor.FOREGROUND;
			
			case Display.COLOR_HIGHLIGHTED_BACKGROUND:
			case Display.COLOR_IDLE_HIGHLIGHTED_BACKGROUND:
				return ScritchLAFElementColor.HIGHLIGHTED_BACKGROUND;
			
			case Display.COLOR_HIGHLIGHTED_BORDER:
				return ScritchLAFElementColor.HIGHLIGHTED_BORDER;
			
			case Display.COLOR_HIGHLIGHTED_FOREGROUND:
			case Display.COLOR_IDLE_HIGHLIGHTED_FOREGROUND:
				return ScritchLAFElementColor.HIGHLIGHTED_FOREGROUND;
		}
		
		// Unknown
		return -1;
	}
	
	/**
	 * Maps a display element type to ScritchUI.
	 *
	 * @param __in The input {@link Display} element.
	 * @return The resultant mapped type to {@link ScritchLAFImageElementType}.
	 * @throws IllegalArgumentException If the element is not valid.
	 * @since 2024/03/09
	 */
	@MagicConstant(valuesFromClass = ScritchLAFImageElementType.class)
	@SquirrelJMEVendorApi
	public static int scritchElementType(
		@MagicConstant(valuesFromClass = Display.class) int __in)
		throws IllegalArgumentException
	{
		switch (__in)
		{
			case Display.LIST_ELEMENT:
				return ScritchLAFImageElementType.LIST_ELEMENT;
				
			case Display.CHOICE_GROUP_ELEMENT:
				return ScritchLAFImageElementType.CHOICE_GROUP;
			
			case Display.ALERT:
				return ScritchLAFImageElementType.ALERT;
			
			case Display.TAB:
				return ScritchLAFImageElementType.TAB;
			
			case Display.COMMAND:
				return ScritchLAFImageElementType.COMMAND;
			
			case Display.NOTIFICATION:
				return ScritchLAFImageElementType.NOTIFICATION;
			
			case Display.MENU:
				return ScritchLAFImageElementType.MENU;
		}
		
		/* {@squirreljme.error EB1h Invalid element type. (The type)} */
		throw new IllegalArgumentException(ErrorCode.__error__(
			"EB1h", __in));
	}
}
