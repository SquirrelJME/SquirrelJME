// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;

/**
 * Swing Window object.
 *
 * @since 2024/03/14
 */
@Deprecated
public class SwingWindowObject
	implements ScritchWindowBracket, SwingContainerObject
{
	/** The backing frame. */
	protected final JFrame frame =
		new JFrame();
	
	/**
	 * Initializes the window.
	 *
	 * @since 2024/03/19
	 */
	public SwingWindowObject()
	{
		// Null layout is absolute positioning layout
		this.frame.setLayout(null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/17
	 */
	@Override
	public Container swingContainer()
	{
		return (Container)this.frame;
	}
}
