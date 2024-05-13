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
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Manager for container type objects.
 *
 * @since 2024/03/26
 */
@Deprecated
@SquirrelJMEVendorApi
public final class FramebufferContainerManager
	extends FramebufferBaseObject
{
	/** The wrapped container. */
	private final Reference<FramebufferContainerObject> _container;
	
	/** Internal component order. */
	private volatile FramebufferComponentObject[] _order;
	
	/**
	 * Initializes the container manager.
	 *
	 * @param __selfApi Our own API.
	 * @param __coreApi The core API we are wrapping.
	 * @param __container The container to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/26
	 */
	FramebufferContainerManager(
		Reference<FramebufferScritchInterface> __selfApi,
		ScritchInterface __coreApi, FramebufferContainerObject __container)
		throws NullPointerException
	{
		super(__selfApi, __coreApi);
		
		if (__container == null)
			throw new NullPointerException("NARG");
		
		this._container = new WeakReference<>(__container);
	}
	
	/**
	 * Adds the component to this container.
	 *
	 * @param __component The component to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/26
	 */
	public void add(FramebufferComponentObject __component)
		throws NullPointerException
	{
		if (__component == null)
			throw new NullPointerException("NARG");
		
		// Do nothing if the container was GCed
		FramebufferContainerObject container =
			this._container.get();
		if (container == null)
			return;
		
		// If the component is already within, do nothing
		FramebufferComponentObject[] order = this._order;
		int orderLen = (order == null ? 0 : order.length);
		if (order != null && orderLen > 0)
			for (int i = 0; i < orderLen; i++)
				if (order[i] == __component)
					return;
		
		// Build new array
		FramebufferComponentObject[] newOrder =
			new FramebufferComponentObject[orderLen + 1];
		
		// Copy anything old in there
		if (orderLen > 0)
			System.arraycopy(order, 0,
				newOrder, 0, orderLen);
		
		// Add component to the end
		newOrder[orderLen] = __component;
			
		// Cache it
		this._order = newOrder;
		
		// Now that it is in there, make sure it is on the real container
		this.coreApi.container().add(container.__container(),
			__component.corePanel);
	}
	
	/**
	 * Sets the bounds of the given component in the container.
	 *
	 * @param __component The component to change the size and position of.
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/26
	 */
	public void setBounds(FramebufferComponentObject __component,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		if (__component == null)
			throw new NullPointerException("NARG");
		
		// Do nothing if the container was GCed
		FramebufferContainerObject container =
			this._container.get();
		if (container == null)
			return;
		
		// Just forward to the core handler
		this.coreApi.container().setBounds(container.__container(),
			__component.corePanel, __x, __y, __w, __h);
	}
}
