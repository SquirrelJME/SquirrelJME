// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

/**
 * This is used as a key which is used for creating back bounded native
 * displayable by specifying a type since it is not really possible to use
 * this references during construction.
 *
 * @since 2017/05/24
 */
public enum DisplayableType
{
	/** Alert. */
	ALERT,
	
	/** A canvas. */
	CANVAS,
	
	/** Used to select files. */
	FILE_SELECTOR,
	
	/** A form. */
	FORM,
	
	/** A list. */
	LIST,
	
	/** A tabbed pane. */
	TABBED_PANE,
	
	/** A text box. */
	TEXT_BOX,
	
	/** End. */
	;
}

