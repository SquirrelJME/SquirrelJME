// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.c.CIdentifier;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Comparator;

/**
 * Comparator for quick searching a name, compares by string hashcode first
 * and then the actual string so that the hash itself can be quickly
 * binary searched for first rather than needing to hash the name.
 *
 * @since 2023/07/25
 */
public class QuickSearchComparator
	implements Comparator<String>
{
	/**
	 * {@inheritDoc}
	 * @since 2023/07/25
	 */
	@Override
	public int compare(String __a, String __b)
	{
		int ha = __a.hashCode();
		int hb = __b.hashCode();
		
		if (ha < hb)
			return -1;
		else if (ha > hb)
			return 1;
		
		return __a.compareTo(__b);
	}
}
