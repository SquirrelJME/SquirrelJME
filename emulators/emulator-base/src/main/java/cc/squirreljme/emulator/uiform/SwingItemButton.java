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
import javax.swing.JButton;

/**
 * Button.
 *
 * @since 2020/07/18
 */
public class SwingItemButton
	extends SwingItem
{
	/** The button. */
	private final JButton button;
	
	/**
	 * Initializes the item.
	 * 
	 * @since 2020/07/18
	 */
	public SwingItemButton()
	{
		this.button = new JButton();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public JButton component()
	{
		return this.button;
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
