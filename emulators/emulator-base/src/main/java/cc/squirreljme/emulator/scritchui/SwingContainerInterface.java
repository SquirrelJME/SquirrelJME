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
import cc.squirreljme.jvm.mle.scritchui.ScritchContainerInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchContainerBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.awt.Container;
import java.util.Arrays;
import javax.swing.JComponent;

/**
 * The container interface for Swing.
 *
 * @since 2024/03/16
 */
public class SwingContainerInterface
	implements ScritchContainerInterface
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/17
	 */
	@Override
	public void removeAll(ScritchContainerBracket __container)
		throws MLECallError
	{
		if (__container == null)
			throw new MLECallError("Null arguments.");
		
		// Get the container used
		Container swing = ((SwingContainerObject)__container).swingContainer();
		
		// Remove everything
		swing.removeAll();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/17
	 */
	@Override
	public void add(ScritchContainerBracket __container,
		ScritchComponentBracket __component)
		throws MLECallError
	{
		if (__container == null || __component == null)
			throw new MLECallError("Null arguments.");
		
		if (true)
			throw Debugging.todo("ADD CALLED");
		
		// Get the container used
		Container swing = ((SwingContainerObject)__container).swingContainer();
		
		// Add it
		swing.add(((SwingComponentObject)__component).component());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/17
	 */
	@Override
	public void add(ScritchContainerBracket __container,
		ScritchComponentBracket __component, int __layoutInfo)
		throws MLECallError
	{
		if (__container == null || __component == null)
			throw new MLECallError("Null arguments.");
		
		// Debug
		Debugging.debugNote("scritch.add(%p, %p, %d)",
			__container, __component, __layoutInfo);
		
		// Get the container used
		SwingContainerObject container = (SwingContainerObject)__container;
		Container swing = container.swingContainer();
		
		Object mappedConstraint = SwingScritchUtils.mapLayout(container,
			__layoutInfo);
		
		// Debug
		Debugging.debugNote("Mapped %s -> %s",
			__layoutInfo, mappedConstraint);
		
		// Add it
		JComponent component = ((SwingComponentObject)__component).component();
		swing.add(component, mappedConstraint);
		
		// Revalidate has to happen for it to appear
		swing.revalidate();
		component.revalidate();
	}
}
