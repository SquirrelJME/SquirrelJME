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
import javax.swing.JLabel;

/**
 * Standard label item.
 *
 * @since 2020/07/18
 */
public class SwingItemLabel
	extends SwingItem
{
	/** The label used. */
	private final JLabel label;
	
	/**
	 * Initializes the item.
	 * 
	 * @since 2020/07/18
	 */
	public SwingItemLabel()
	{
		this.label = new JLabel();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public JLabel component()
	{
		return this.label;
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
