// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

/**
 * A flat list of integers.
 *
 * @since 2023/12/20
 */
public class IntegerFlatList
	extends FlatList<Integer>
{
	/**
	 * Initializes the flat list.
	 *
	 * @param __link The link where the data is.
	 * @since 2023/12/20
	 */
	public IntegerFlatList(AllocLink __link)
	{
		super(__link, false, (Integer[])null);
	}
}
