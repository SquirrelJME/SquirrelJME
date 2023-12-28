// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Arrays;
import java.util.List;

/**
 * A flat list of pointers.
 *
 * @param <E> The pointer type.
 * @since 2023/12/17
 */
public class PointerFlatList<E extends Pointer>
	extends FlatList<E>
{
	/**
	 * Initializes the flat list.
	 *
	 * @param __link The link used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/17
	 */
	public PointerFlatList(AllocLink __link)
		throws NullPointerException
	{
		super(__link, true, (E[])null);
	}
	
	/**
	 * Initializes the flat list.
	 *
	 * @param __link The link used.
	 * @param __existing Existing elements.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/27
	 */
	public PointerFlatList(AllocLink __link, E... __existing)
		throws NullPointerException
	{
		super(__link, true, __existing);
	}
	
	/**
	 * Initializes the flat list.
	 *
	 * @param __link The link used.
	 * @param __existing Existing elements.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/27
	 */
	public PointerFlatList(AllocLink __link, List<E> __existing)
		throws NullPointerException
	{
		super(__link, true, __existing);
	}
}
