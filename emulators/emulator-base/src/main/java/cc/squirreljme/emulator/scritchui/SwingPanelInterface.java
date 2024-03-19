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
import cc.squirreljme.jvm.mle.scritchui.ScritchPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Swing interface around panels.
 *
 * @since 2024/03/16
 */
public class SwingPanelInterface
	implements ScritchPanelInterface
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/16
	 */
	@Override
	public ScritchPanelBracket newPanel()
	{
		return new SwingPanelObject();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/19
	 */
	@Override
	public void repaint(@NotNull ScritchComponentBracket __component)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments");
		
		SwingPanelObject panel = (SwingPanelObject)__component;
		panel.panel.repaint();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/19
	 */
	@Override
	public void setPaintListener(@NotNull ScritchComponentBracket __component,
		@Nullable ScritchPaintListener __listener)
		throws MLECallError
	{
		if (__component == null)
			throw new MLECallError("Null arguments");
		
		SwingPanelObject panel = (SwingPanelObject)__component;
		panel.setPaintListener(__listener);
	}
}
