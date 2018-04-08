// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.layout;

/**
 * Represents the type of class something is.
 *
 * @since 2018/04/08
 */
public enum ClassType
{
	/** Standard class. */
	CLASS,
	
	/** Interface. */
	INTERFACE,
	
	/** Annotation. */
	ANNOTATION,
	
	/** Enumeration. */
	ENUM,
	
	/** End. */
	;
}

