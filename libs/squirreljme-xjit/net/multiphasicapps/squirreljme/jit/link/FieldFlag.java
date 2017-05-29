// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.link;

/**
 * These are flags which are associated with class fields.
 *
 * @since 2016/04/23
 */
public enum FieldFlag
	implements MemberFlag
{
	/** Public field. */
	PUBLIC,
	
	/** Private field. */
	PRIVATE,
	
	/** Protected field. */
	PROTECTED,
	
	/** Static field. */
	STATIC,
	
	/** Final field. */
	FINAL,
	
	/** Volatile field. */
	VOLATILE,
	
	/** Transient field. */
	TRANSIENT,
	
	/** Synthetic field. */
	SYNTHETIC,
	
	/** Enumeration. */
	ENUM,
	
	/** End. */
	;
}

