// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.ScritchContainerInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchContainerBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Interface for components.
 *
 * @since 2024/03/24
 */
@Deprecated
@SquirrelJMEVendorApi
public class FramebufferContainerInterface
	extends FramebufferBaseInterface
	implements ScritchContainerInterface
{
	/**
	 * Initializes this interface. 
	 *
	 * @param __self The framebuffer self interface.
	 * @param __core The core interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/24
	 */
	public FramebufferContainerInterface(
		Reference<FramebufferScritchInterface> __self,
		ScritchInterface __core)
		throws NullPointerException
	{
		super(__self, __core);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public void add(ScritchContainerBracket __container,
		ScritchComponentBracket __component)
		throws MLECallError
	{
		if (__container == null || __component == null)
			throw new MLECallError("NARG");
		
		// Get framebuffer wrappers of these
		FramebufferContainerObject container =
			(FramebufferContainerObject)__container;
		FramebufferComponentObject component =
			(FramebufferComponentObject)__component;
		
		// Forward
		container.__containerManager().add(component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/24
	 */
	@Override
	public void removeAll(ScritchContainerBracket __container)
		throws MLECallError
	{
		throw Debugging.todo();
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
			throw new MLECallError("NARG");
		
		// Get framebuffer wrappers of these
		FramebufferContainerObject container =
			(FramebufferContainerObject)__container;
		FramebufferComponentObject component =
			(FramebufferComponentObject)__component;
		
		// Forward
		container.__containerManager().setBounds(component,
			__x, __y, __w, __h);
	}
}
