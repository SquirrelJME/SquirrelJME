// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * These are flags which modify how a class is accessed and is behaved.
 *
 * @since 2016/04/23
 */
public enum ClassFlag
	implements Flag
{
	/** Public access. */
	PUBLIC,
	
	/** Final. */
	FINAL,
	
	/** Super. */
	SUPER,
	
	/** Interface. */
	INTERFACE,
	
	/** Abstract. */
	ABSTRACT,
	
	/** Synthetic. */
	SYNTHETIC,
	
	/** Annotation. */
	ANNOTATION,
	
	/** Enumeration. */
	ENUM,
	
	/** End. */
	;
}

