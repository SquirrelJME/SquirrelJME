// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

/**
 * This specifies the locations which are used for input and is needed for
 * lookup of classes and source code.
 *
 * @since 2017/11/28
 */
public enum CompilerInputLocation
{
	/**
	 * Source code.
	 *
	 * Note that this location is used for lookup of source code as needed.
	 */
	SOURCE,
	
	/** Classes. */
	CLASS,
	
	/** End. */
	;
}

