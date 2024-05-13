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
import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;

/**
 * The container interface for Swing.
 *
 * @since 2024/03/16
 */
@Deprecated
public class SwingContainerInterface
	implements ScritchContainerInterface
{	
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
		
		// Get the container used
		JComponent component = ((SwingComponentObject)__component).component();
		Container swing = ((SwingContainerObject)__container).swingContainer();
		
		// Add it
		swing.add(component);
		
		// Revalidate has to happen for it to appear
		swing.revalidate();
		component.revalidate();
	}
	
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
	 * @since 2024/03/26
	 */
	@Override
	public void setBounds(ScritchContainerBracket __container,
		ScritchComponentBracket __component,
		int __x, int __y, int __w,  int __h)
		throws MLECallError
	{
		if (__container == null || __component == null)
			throw new MLECallError("Null arguments.");
		
		// Get the container and component used
		Container container = ((SwingContainerObject)__container)
			.swingContainer();
		Component component = ((SwingComponentObject)__component).component(); 
		
		// Set bounds
		component.setBounds(__x, __y, __w, __h);
	}
}
