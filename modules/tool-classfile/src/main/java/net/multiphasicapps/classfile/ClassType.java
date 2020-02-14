// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * Returns the type of class this is.
 *
 * @since 2018/05/14
 */
public enum ClassType
{
	/** Normal class. */
	CLASS,
	
	/** Interface. */
	INTERFACE,
	
	/** Enumeration. */
	ENUM,
	
	/** Annotation. */
	ANNOTATION,
	
	/** End. */
	;
}

