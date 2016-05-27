// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ci;

/**
 * These are flags which are associated with class fields.
 *
 * @since 2016/04/23
 */
public enum CIFieldFlag
	implements CIMemberFlag
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

