// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests;

/**
 * This enumeration represents whether a test is a note, if it passed, or
 * if it failed.
 *
 * @since 2016/07/14
 */
public enum TestPassState
{
	/** Not passing or failing, but just a note. */
	NOTE,
	
	/** Test passed. */
	PASS,
	
	/** Test failed. */
	FAIL,
	
	/** End. */
	;
}

