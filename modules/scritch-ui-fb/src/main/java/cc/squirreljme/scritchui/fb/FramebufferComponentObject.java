// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.scritchui.fb;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPencilBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Base class for all component types.
 *
 * @since 2024/03/26
 */
@Deprecated
@SquirrelJMEVendorApi
public abstract class FramebufferComponentObject
	extends FramebufferBaseObject
	implements ScritchComponentBracket
{
	/** The panel this is based upon. */
	@SquirrelJMEVendorApi
	protected final ScritchPanelBracket corePanel;
	
	/**
	 * Initializes the base component object.
	 *
	 * @param __selfApi The reference to our own API.
	 * @param __coreApi The core API for accessing wrapped objects.
	 * @param __corePanel The core panel to draw on top of.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/26
	 */
	@SquirrelJMEVendorApi
	public FramebufferComponentObject(
		Reference<FramebufferScritchInterface> __selfApi,
		ScritchInterface __coreApi,
		ScritchPanelBracket __corePanel)
		throws NullPointerException
	{
		super(__selfApi, __coreApi);
		
		if (__corePanel == null)
			throw new NullPointerException("NARG");
		
		// Store for later
		this.corePanel = __corePanel;
		
		// We handle drawing everything ourselves depending on the widget we
		// will be drawing for
		__coreApi.panel().setPaintListener(__corePanel,
			new __PaintListener__(this));
	}
	
	/**
	 * Internal painting logic for this component.
	 *
	 * @param __g The target pencil to draw with.
	 * @param __sw The surface width.
	 * @param __sh The surface height.
	 * @param __special Special value.
	 * @since 2024/03/26
	 */
	@SquirrelJMEVendorApi
	@Async.Execute
	abstract void __paintInternal(
		@NotNull ScritchPencilBracket __g,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sw,
		@Range(from = 0, to = Integer.MAX_VALUE) int __sh,
		int __special);
	
	/**
	 * Common focus enablement.
	 *
	 * @param __enabled Should focus be enabled?
	 * @since 2024/03/26
	 */
	void __enableFocus(boolean __enabled)
	{
		// Forward to core
		this.coreApi.panel().enableFocus(this.corePanel, __enabled);
	}
	
	/**
	 * Requests repainting of the component. 
	 *
	 * @since 2024/03/26
	 */
	void __repaint()
	{
		// Forward to core
		this.coreApi.panel().repaint(this.corePanel);
	}
	
	/**
	 * Revalidates this object.
	 *
	 * @since 2024/03/26
	 */
	void __revalidate()
	{
		// Forward to core
		this.coreApi.component().revalidate(this.corePanel);
	}
}
