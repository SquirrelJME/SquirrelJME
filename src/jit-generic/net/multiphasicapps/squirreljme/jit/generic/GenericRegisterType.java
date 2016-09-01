// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

/**
 * This specifies the type of value that is stored in the given register.
 *
 * @since 2016/09/01
 */
public enum GenericRegisterType
{
	/** Half the word size of the CPU. */
	HALF,
	
	/** Matches the word size (either for integer or float). */
	SAME,
	
	/** Double the word size of the CPU. */
	DOUBLE,
	
	/** End. */
	;
}

