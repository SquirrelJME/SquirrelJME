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
 * This represents the type of invocation to be performed.
 *
 * @since 2016/09/05
 */
public enum JITInvokeType
{
	/** Static. */
	STATIC,
	
	/** Virtual. */
	VIRTUAL,
	
	/** Special. */
	SPECIAL,
	
	/** Interface. */
	INTERFACE,
	
	/** End. */
	;
}

