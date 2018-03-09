// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

/**
 * This represents the area in which a token was read from.
 *
 * @since 2018/03/07
 */
public enum ContextArea
{
	/** Potentially read package statement */
	INTRO_PACKAGE,
	
	/** Read import statements. */
	INTRO_IMPORTS,
	
	/** Outside class area. */
	INTRO_OUTSIDE_CLASS,
	
	/** End. */
	;
}

