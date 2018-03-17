// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

/**
 * This represents the type of a displayable that something is.
 *
 * @since 2018/03/17
 */
public enum DisplayableType
{
	/** Canvas, */
	CANVAS,
	
	/** Alert. */
	ALERT,
	
	/** File selector. */
	FILE_SELECTOR,
	
	/** Form. */
	FORM,
	
	/** List. */
	LIST,
	
	/** Tabbed pane. */
	TABBED_PANE,
	
	/** Text Box. */
	TEXT_BOX,
	
	/** End. */
	;
}

