// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import cc.squirreljme.runtime.lcdui.server.LcdTicker;
import cc.squirreljme.runtime.lcdui.server.LcdTickerListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is the compoent which is shown to display text.
 *
 * @since 2018/03/27
 */
public class SwingTickerComponent
	extends JPanel
	implements LcdTickerListener
{
	/** The label. */
	protected final JLabel label;
	
	/**
	 * Initializes the component.
	 *
	 * @since 2018/03/27
	 */
	public SwingTickerComponent()
	{
		JLabel label = new JLabel();
		this.label = label;
		
		// Only the label is here
		this.add(label);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/26
	 */
	@Override
	public void textChanged(LcdTicker __lcd, String __text)
		throws NullPointerException
	{
		this.label.setText(__text);
	}
}

