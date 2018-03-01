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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This tests the linked hash map.
 *
 * @since 2017/03/28
 */
public class TestLinkedHashMapInsert
	extends __BaseMap__
{
	/**
	 * Initializes the test.
	 *
	 * @since 2017/03/28
	 */
	public TestLinkedHashMapInsert()
	{
		super(true, new LinkedHashMap<String, String>(16, 0.75F, false));
	}
}

