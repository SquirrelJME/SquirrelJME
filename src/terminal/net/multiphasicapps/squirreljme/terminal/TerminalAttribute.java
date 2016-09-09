// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terminal;

/**
 * This contains attribute constants for the terminal.
 *
 * The uppermost 4th bit on the color means that it is a high intensity color.
 *
 * @since 2016/09/08
 */
public interface TerminalAttribute
{
	/** The foregound color shifted mask. */
	public static final int FOREGROUND_COLOR_SHIFT_MASK =
		0b0000_0000__0000_1111;
	
	/** The foreground color shift. */
	public static final int FOREGROUND_COLOR_SHIFT =
		Integer.numberOfTrailingZeros(FOREGROUND_COLOR_SHIFT_MASK);
	
	/** The foreground color value mask. */
	public static final int FOREGROUND_COLOR_VALUE_MASK =
		FOREGROUND_COLOR_SHIFT_MASK >>> FOREGROUND_COLOR_SHIFT;
		
	/** The background color shifted mask. */
	public static final int BACKGROUND_COLOR_SHIFT_MASK =
		0b0000_0000__1111_0000;
	
	/** The background color shift. */
	public static final int BACKGROUND_COLOR_SHIFT =
		Integer.numberOfTrailingZeros(BACKGROUND_COLOR_SHIFT_MASK);
	
	/** The background color value mask. */
	public static final int BACKGROUND_COLOR_VALUE_MASK =
		BACKGROUND_COLOR_SHIFT_MASK >>> BACKGROUND_COLOR_SHIFT;
}

