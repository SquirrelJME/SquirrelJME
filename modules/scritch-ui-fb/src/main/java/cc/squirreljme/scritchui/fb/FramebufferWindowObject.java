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
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;

/**
 * Represents a wrapped native window.
 *
 * @since 2024/03/26
 */
@Deprecated
@SquirrelJMEVendorApi
public class FramebufferWindowObject
	extends FramebufferBaseObject
	implements FramebufferContainerObject, ScritchWindowBracket
{
	/** The container manager. */
	private final FramebufferContainerManager _container;
	
	/** The core window we are wrapping. */
	private final ScritchWindowBracket _coreWindow;
	
	/**
	 * Initializes the window object.
	 *
	 * @param __selfApi The reference to our own API.
	 * @param __coreApi The core API for accessing wrapped objects.
	 * @param __coreWindow The core window to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/26
	 */
	@SquirrelJMEVendorApi
	public FramebufferWindowObject(
		Reference<FramebufferScritchInterface> __selfApi,
		ScritchInterface __coreApi,
		ScritchWindowBracket __coreWindow)
		throws NullPointerException
	{
		super(__selfApi, __coreApi);
		
		if (__coreWindow == null)
			throw new NullPointerException("NARG");
		
		this._coreWindow = __coreWindow;
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
		return this._coreWindow;
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
	 * Sets the minimum content size.
	 *
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2024/03/26
	 */
	void __contentMinimumSize(int __w, int __h)
	{
		// Forward to core
		this.coreApi.window().contentMinimumSize(
			this._coreWindow, __w, __h);
	}
	
	/**
	 * Set the visibility of the window.
	 *
	 * @param __visible If it should be visible or not?
	 * @since 2024/03/26
	 */
	void __setVisible(boolean __visible)
	{
		// Forward to core
		this.coreApi.window().setVisible(
			this._coreWindow, __visible);
	}
}
