// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.lcdui.scritchui.MenuAction;

/**
 * This contains the desired command policy layout state.
 *
 * @since 2020/09/27
 */
@Deprecated
final class __Layout__
	implements AutoCloseable
{
	/** The number of possible placements. */
	@Deprecated
	private static final int _MAX_PLACEMENTS =
		(Display.SOFTKEY_INDEX_MASK * 5);
	
	/** The placements used. */
	@Deprecated
	private final MenuAction[] _placements =
		new MenuAction[__Layout__._MAX_PLACEMENTS];
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/27
	 */
	@Override
	@Deprecated
	public void close()
	{
		// Has no effect
	}
	
	/**
	 * Gets the item at the given position.
	 * 
	 * @param __pos The position to get from.
	 * @return The action at the given position or null.
	 * @throws IllegalArgumentException If the position is not valid.
	 * @since 2020/09/27
	 */
	@Deprecated
	public final MenuAction get(int __pos)
		throws IllegalArgumentException
	{
		return this._placements[__Layout__.__posToIndex(__pos)];
	}
	
	/**
	 * Gets the priority of the item at the given position.
	 * 
	 * @param __pos The position to get from.
	 * @return The priority of the given item at the position.
	 * @throws IllegalArgumentException If the position is not valid.
	 * @since 2020/09/27
	 */
	@Deprecated
	public final int getPriority(int __pos)
		throws IllegalArgumentException
	{
		// Blank items always get the lowest priority
		MenuAction action = this.get(__pos);
		if (action == null)
			return Integer.MAX_VALUE;
		
		return MenuAction.__getPriority(action);
	}
	
	/**
	 * Sets the action at the given position.
	 * 
	 * @param __act The action to set, may be {@code null}.
	 * @param __pos The position to set.
	 * @throws IllegalArgumentException If the position is not valid.
	 * @since 2020/09/27
	 */
	@Deprecated
	public final void set(MenuAction __act, int __pos)
		throws IllegalArgumentException
	{
		this._placements[__Layout__.__posToIndex(__pos)] = __act;
	}
	
	/**
	 * Maps the position to an index.
	 * 
	 * @param __pos The position to map.
	 * @return The index of the position.
	 * @throws IllegalArgumentException If the position is not valid.
	 * @since 2020/09/27
	 */
	@Deprecated
	private static int __posToIndex(int __pos)
		throws IllegalArgumentException
	{
		int border = __pos & (~Display.SOFTKEY_INDEX_MASK);
		int index = (__pos & Display.SOFTKEY_INDEX_MASK);
		
		// Determine if the grouping is valid, the border is always a
		// multiple of 20 so if there is a remainder left over then we know
		// it is not a valid placement
		/* {@squirreljme.error EB3m Invalid position. (The position)} */
		int groupId = (border - Display.SOFTKEY_BOTTOM) / 20;
		int xvDiv = (border - Display.SOFTKEY_BOTTOM) % 20;
		if (index <= 0 || xvDiv != 0 || groupId < 0 || groupId > 4)
			throw new IllegalArgumentException("EB3m " + __pos);
		
		return (groupId * Display.SOFTKEY_INDEX_MASK) + (index - 1);
	}
}
