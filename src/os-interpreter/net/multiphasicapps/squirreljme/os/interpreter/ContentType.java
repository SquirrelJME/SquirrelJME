// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.os.interpreter;

/**
 * This is the type of content that a namespace has.
 *
 * @since 2016/07/23
 */
public enum ContentType
{
	/** A resource. */
	RESOURCE,
	
	/** A class. */
	CLASS,
	
	/** The string table. */
	STRING_TABLE,
	
	/** End. */
	;
}

