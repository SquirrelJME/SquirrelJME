// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

/**
 * This represents the kind of register that this is.
 *
 * @since 2016/09/01
 */
public enum NativeRegisterKind
{
	/** Integer registers. */
	INTEGER,
	
	/** Floating point registers. */
	FLOAT,
	
	/** End. */
	;
}

