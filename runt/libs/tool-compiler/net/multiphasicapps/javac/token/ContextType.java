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
 * This is the type of context sensitive token that a token is.
 *
 * @since 2018/03/07
 */
public enum ContextType
{
	/** Represents a package declaration. */
	PACKAGE_DECLARATION,
	
	/** Import a class. */
	IMPORT_CLASS,
	
	/** Import package. */
	IMPORT_PACKAGE,
	
	/** Static import, specific. */
	IMPORT_STATIC,
	
	/** Static wildcard import. */
	IMPORT_STATIC_WILDCARD,
	
	/** End. */
	;
}

