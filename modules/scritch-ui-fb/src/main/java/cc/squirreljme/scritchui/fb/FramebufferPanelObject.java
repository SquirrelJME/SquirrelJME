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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchContainerBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPencilBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchPaintListener;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;

/**
 * Framebuffer panel, which is generally used for general drawing onto with
 * a Canvas or to build custom components.
 *
 * @since 2024/03/26
 */
@SquirrelJMEVendorApi
public class FramebufferPanelObject
	extends FramebufferComponentObject
	implements FramebufferContainerObject, ScritchPanelBracket
{
	/** The container manager. */
	private final FramebufferContainerManager _container;
	
	/** The listener to call on paint events. */
	private volatile ScritchPaintListener _paintListener;
	
	/**
	 * Initializes the framebuffer panel.
	 *
	 * @param __selfApi Our own API.
	 * @param __coreApi The native API to access.
	 * @param __corePanel The panel that exists at the core API level.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/26
	 */
	public FramebufferPanelObject(
		Reference<FramebufferScritchInterface> __selfApi,
		ScritchInterface __coreApi, ScritchPanelBracket __corePanel)
		throws NullPointerException
	{
		super(__selfApi, __coreApi, __corePanel);
		
		this._container = new FramebufferContainerManager(
			__selfApi, __coreApi, this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/26
	 */
	@Override
	public ScritchContainerBracket __container()
	{
		return this.corePanel;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/26
	 */
	@Override
	public FramebufferContainerManager __containerManager()
	{
		return this._container;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/26
	 */
	@Override
	void __paintInternal(ScritchPencilBracket __g, int __sw, int __sh,
		int __special)
	{
		// Forward to the canvas listener
		ScritchPaintListener listener = this._paintListener;
		if (listener != null)
			listener.paint(this, __g, __sw, __sh, __special);
	}
	
	/**
	 * Sets or clears the layered paint handler.
	 *
	 * @param __listener The listener to call into when additionally drawing.
	 * @since 2024/03/26
	 */
	void __setPaintListener(ScritchPaintListener __listener)
	{
		this._paintListener = __listener;
	}
}
