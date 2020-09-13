// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.swing.JTextField;

/**
 * Multiline text box.
 *
 * @since 2020/07/18
 */
public class SwingItemMultiLineTextBox
	extends SwingItem
{
	/** The text field. */
	private final JTextField text;
	
	/**
	 * Initializes the item.
	 * 
	 * @since 2020/07/18
	 */
	public SwingItemMultiLineTextBox()
	{
		this.text = new JTextField();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public JTextField component()
	{
		return this.text;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public void deletePost()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public void property(int __id, int __newValue)
		throws MLECallError
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public void property(int __id, String __newValue)
	{
		throw Debugging.todo();
	}
}
