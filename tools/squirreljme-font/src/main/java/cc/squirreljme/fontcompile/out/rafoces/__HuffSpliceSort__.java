// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.out.rafoces;

import java.util.Comparator;

/**
 * Huffman splice comparator sort.
 *
 * @since 2024/06/03
 */
final class __HuffSpliceSort__
	implements Comparator<HuffSpliceItem>
{
	/**
	 * {@inheritDoc}
	 * @since 2024/06/03
	 */
	@Override
	public int compare(HuffSpliceItem __a, HuffSpliceItem __b)
	{
		// Larger values come first
		int cmp = Integer.compare(__a.count(), __b.count());
		if (cmp != 0)
			return -cmp;
		
		// Then sort the actual chain list
		return __a.getList().compareTo(__b.getList());
	}
}
