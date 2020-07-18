// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.swing.JList;

/**
 * List.
 *
 * @since 2020/07/18
 */
public class SwingItemList
	extends SwingItem
{
	/** The list used. */
	private final JList<Object> list;
	
	/**
	 * Initializes the item.
	 * 
	 * @since 2020/07/18
	 */
	public SwingItemList()
	{
		this.list = new JList<Object>();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public JList<?> component()
	{
		return this.list;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public void delete()
	{
	}
}
