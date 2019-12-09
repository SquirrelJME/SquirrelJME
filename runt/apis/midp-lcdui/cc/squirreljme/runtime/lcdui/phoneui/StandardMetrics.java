// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import javax.microedition.lcdui.Font;

/**
 * This contains standard metrics for screen parts and such.
 *
 * @since 2019/05/16
 */
public final class StandardMetrics
{
	/** UI Font size. */
	public static final byte UI_FONT_SIZE =
		16;
	
	/** Height of the title bar. */
	public static final int TITLE_BAR_HEIGHT =
		UI_FONT_SIZE;
	
	/** Height of the command bar. */
	public static final int COMMAND_BAR_HEIGHT =
		UI_FONT_SIZE;
	
	/** The background bar color. */
	public static final int BACKGROUND_BAR_COLOR =
		0xFF_000000;
	
	/** The foreground bar color. */
	public static final int FOREGROUND_BAR_COLOR =
		0xFF_FFFFFF;
	
	/** Transparent background color. */
	public static final int TRANSPARENT_COLOR =
		0xFF_FFFFFF;
	
	/** Vibration symbol color. */
	public static final int VIBRATE_COLOR =
		0xFF_FF00FF;
	
	/** Height of the ticker bar. */
	public static final int TICKER_BAR_HEIGHT =
		UI_FONT_SIZE;
	
	/** Background color of the ticker bar. */
	public static final int BACKGROUND_TICKER_COLOR =
		0xFF_A6F6FF;
	
	/** Foreground color of the ticker bar. */
	public static final int FOREGROUND_TICKER_COLOR =
		0xFF_000000;
	
	/** The font used for list items. */
	public static final String LIST_ITEM_FONT =
		"sansserif";
	
	/** The height of list items. */
	public static final int LIST_ITEM_HEIGHT =
		UI_FONT_SIZE;
	
	/** Enabled normal foreground. */
	public static final int ENABLED_NORMAL_FOREGROUND =
		0xFF_000000;
	
	/** Enabled normal background. */
	public static final int ENABLED_NORMAL_BACKGROUND =
		0xFF_FFFFFF;
	
	/** Enabled selected foreground. */
	public static final int ENABLED_SELECTED_FOREGROUND =
		0xFF_FFFFFF;
	
	/** Enable selected background. */
	public static final int ENABLED_SELECTED_BACKGROUND =
		0xFF_000088;
	
	/** Disabled normal foreground. */
	public static final int DISABLED_NORMAL_FOREGROUND =
		0xFF_888888;
	
	/** Disabled normal background. */
	public static final int DISABLED_NORMAL_BACKGROUND =
		0xFF_FFFFFF;
	
	/** Disabled selected foreground. */
	public static final int DISABLED_SELECTED_FOREGROUND =
		0xFF_AAAAAA;
	
	/** Disabled selected background. */
	public static final int DISABLED_SELECTED_BACKGROUND =
		0xFF_888888;
	
	/** The focus box color (A). */
	public static final int FOCUS_A_COLOR =
		0xFF_FF0000;
	
	/** The focus box color (B). */
	public static final int FOCUS_B_COLOR =
		0xFF_FFFF00;
	
	/**
	 * Not used.
	 *
	 * @since 2019/05/16
	 */
	private StandardMetrics()
	{
	}
	
	/**
	 * Returns the background color.
	 *
	 * @param __en Is this enabled?
	 * @param __sl Is this selected?
	 * @return The color to use.
	 * @since 2019/05/18
	 */
	public static final int itemBackgroundColor(boolean __en, boolean __sl)
	{
		if (__en)
			if (__sl)
				return ENABLED_SELECTED_BACKGROUND;
			else
				return ENABLED_NORMAL_BACKGROUND;
		else
			if (__sl)
				return DISABLED_SELECTED_BACKGROUND;
			else
				return DISABLED_NORMAL_BACKGROUND;
	}
	
	/**
	 * Returns the foreground color.
	 *
	 * @param __en Is this enabled?
	 * @param __sl Is this selected?
	 * @return The color to use.
	 * @since 2019/05/18
	 */
	public static final int itemForegroundColor(boolean __en, boolean __sl)
	{
		if (__en)
			if (__sl)
				return ENABLED_SELECTED_FOREGROUND;
			else
				return ENABLED_NORMAL_FOREGROUND;
		else
			if (__sl)
				return DISABLED_SELECTED_FOREGROUND;
			else
				return DISABLED_NORMAL_FOREGROUND;
	}
	
	/**
	 * Returns the item label font.
	 *
	 * @return The item label font.
	 * @since 2019/12/09
	 */
	public static final Font itemLabelFont()
	{
		return Font.getFont(LIST_ITEM_FONT, 0, LIST_ITEM_HEIGHT);
	}
}

