// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

/**
 * DESCRIBE THIS.
 *
 * @since 2016/04/23
 */
public enum NCIMethodFlag
	implements NCIMemberFlag
{
	/** Public method. */
	PUBLIC,
	
	/** Private method. */
	PRIVATE,
	
	/** Protected method. */
	PROTECTED,
	
	/** Static method. */
	STATIC,
	
	/** Final method. */
	FINAL,
	
	/** Synchronized method. */
	SYNCHRONIZED,
	
	/** Bridge method. */
	BRIDGE,
	
	/** Variable argument method. */
	VARARGS,
	
	/** Native method. */
	NATIVE,
	
	/** Abstract method. */
	ABSTRACT,
	
	/** Strict floating point method. */
	STRICT,
	
	/** Synthetic method. */
	SYNTHETIC,
	
	/** End. */
	;
}

