// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests;

/**
 * This represents an option that tests may have.
 *
 * @since 2016/07/12
 */
public enum TestOption
{
	/** Ignore failing tests. */
	IGNORE_FAIL,
	
	/** Ignore passing tests. */
	IGNORE_PASS,
	
	/** Ignore exceptions. */
	IGNORE_EXCEPTIONS,
	
	/** End. */
	;
}

