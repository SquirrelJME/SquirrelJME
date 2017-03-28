// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.test.collections;

import java.util.Hashtable;
import java.util.Map;
import net.multiphasicapps.squirreljme.test.TestResult;

/**
 * This tests the old hash table which is just a synchronized {@link HashMap}.
 *
 * @since 2017/03/28
 */
public class TestHashtable
	extends __BaseMap__
{
	/**
	 * Initializes the test.
	 *
	 * @since 2017/03/28
	 */
	public TestHashtable()
	{
		super(false, new Hashtable<String, String>());
	}
}

