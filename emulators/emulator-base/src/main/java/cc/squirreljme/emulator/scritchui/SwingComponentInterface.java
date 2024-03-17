// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchComponentInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;

/**
 * The interface for generic components under Swing.
 *
 * @since 2024/03/16
 */
public class SwingComponentInterface
	implements ScritchComponentInterface
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/17
	 */
	@Override
	public void revalidate(@NotNull ScritchComponentBracket __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments.");
		
		JComponent component = ((SwingComponentObject)__component).component();
		
		// Make sure it is updated and appears updated as well
		component.revalidate();
		component.repaint();
	}
}
