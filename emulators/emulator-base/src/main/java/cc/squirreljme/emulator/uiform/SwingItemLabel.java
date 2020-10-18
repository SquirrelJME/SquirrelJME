// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
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
	public void deletePost()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public void property(int __id, int __sub, int __newValue)
		throws MLECallError
	{
		switch (__id)
		{
			default:
				throw new MLECallError("Invalid property: " + __id);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/18
	 */
	@Override
	public void property(int __id, int __sub, String __newValue)
	{
		switch (__id)
		{
			case UIWidgetProperty.STRING_LABEL:
				this.label.setText(__newValue);
				break;
			
			default:
				throw new MLECallError("Invalid property: " + __id);
		}
	}
}
