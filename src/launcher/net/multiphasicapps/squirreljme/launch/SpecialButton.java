// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch;

/**
 * This describes a button which is special in that it has no real assigned
 * glyph or is natural to a language, but to computers instead.
 *
 * The indexes must be within Unicode's private use range, when a given button
 * is pressed it generates the given keycode. This also means that the button
 * codes can be printed to the screen potentially. Thus characters between
 * 0xE000 and 0xF8FF are valid for usage here.
 *
 * @since 2016/05/15
 */
public interface SpecialButton
{
	/** Start of buttons. */
	public static final int START_OF_LIST =
		0xE000;
	
	/** End of buttons. */
	public static final int END_OF_LIST =
		0xF8FF;
}

