// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.displays.terminal;

/**
 * This interface contains the set of colors that may be used by the terminal.
 *
 * @since 2016/09/08
 */
public interface TerminalColor
{
	/** Black. */
	public static final int BLACK =
		0b000;
	
	/** Red. */
	public static final int RED =
		0b001;
	
	/** Green. */
	public static final int GREEN =
		0b010;
	
	/** Yellow. */
	public static final int YELLOW =
		0b011;
	
	/** Blue. */
	public static final int BLUE =
		0b100;
	
	/** Magenta. */
	public static final int MAGENTA =
		0b101;
	
	/** Cyan. */
	public static final int CYAN =
		0b110;
	
	/** White. */
	public static final int WHITE =
		0b111;
}

