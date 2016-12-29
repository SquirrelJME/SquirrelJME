// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This is an option that modifies how classes are decoded.
 *
 * @since 2016/09/27
 */
public enum ClassDecoderOption
{
	/** No method code will be processed. */
	NO_CODE,
	
	/**
	 * This goes through the method code and determines all of the variable
	 * types that are used for local and stack variables. This information can
	 * be used to determine the size of the stack that may be needed (in the
	 * event where the size of objects is not the same as integer).
	 */
	CALCULATE_ALL_VARIABLE_TYPES,
	
	/** End. */
	;
}

