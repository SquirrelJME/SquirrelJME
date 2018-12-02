// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

/**
 * This is a package public mutable class which represents single choices
 * within anything which uses choices.
 *
 * @since 2017/08/20
 */
final class __ChoiceEntry__
{
	/** The string to display for this choice. */
	volatile String _string;
	
	/** The image to display for this choice. */
	volatile Image _image;
	
	/** The font to use for this choice. */
	volatile Font _font;
	
	/** Is this entry selected? */
	volatile boolean _selected;
	
	/**
	 * Initializes a choice entry with default values.
	 *
	 * @param __s The string to display.
	 * @param __i The image to display.
	 * @since 2017/08/20
	 */
	__ChoiceEntry__(String __s, Image __i)
	{
		// Set
		this._string = __s;
		this._image = __i;
	}
}

