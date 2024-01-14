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
 * A list of character strings.
 *
 * @since 2023/12/17
 */
public class CharStarFlatList
	extends PointerFlatList<CharStarPointer>
{
	/**
	 * Initializes the flat list.
	 *
	 * @param __link The link used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/17
	 */
	public CharStarFlatList(AllocLink __link)
		throws NullPointerException
	{
		super(__link);
	}
}
