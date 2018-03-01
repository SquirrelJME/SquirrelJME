// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.test.collections;

import cc.squirreljme.test.TestResult;
import java.util.Hashtable;
import java.util.Map;

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

