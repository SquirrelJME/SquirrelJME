// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * The JIT compiler is very ordered and as such this may be used by
 * implementation of the class writer to determine if such processing of the
 * code is in the correct order.
 *
 * @since 2016/07/18
 */
public enum JITCompilerOrder
{
	/** The flags for the class. */
	CLASS_FLAGS,
	
	/** End. */
	;
}

