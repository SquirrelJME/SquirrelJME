// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Listener for component events.
 *
 * @since 2020/10/17
 */
@Deprecated
public class HandleComponentEvents
	extends AbstractListener
	implements ComponentListener
{
	/**
	 * Initializes the handler.
	 * 
	 * @param __item The item to watch.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	public HandleComponentEvents(SwingWidget __item)
		throws NullPointerException
	{
		super(__item);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/17
	 */
	@Override
	public void componentResized(ComponentEvent __e)
	{
		SwingWidget widget = this.item();
		if (widget == null)
			return;
		
		UIFormCallback callback = widget.callback();
		if (callback == null)
			return;
		
		// Report changes
		Component component = __e.getComponent();
		callback.propertyChange(widget.form(), widget.item(),
			UIWidgetProperty.INT_WIDTH_AND_HEIGHT, 0, component.getWidth(), component.getHeight());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/17
	 */
	@Override
	public void componentMoved(ComponentEvent __e)
	{
		SwingWidget widget = this.item();
		if (widget == null)
			return;
		
		UIFormCallback callback = widget.callback();
		if (callback == null)
			return;
		
		// Report changes
		Component component = __e.getComponent();
		callback.propertyChange(widget.form(), widget.item(),
			UIWidgetProperty.INT_X_POSITION, 0, -1, component.getX());
		callback.propertyChange(widget.form(), widget.item(),
			UIWidgetProperty.INT_Y_POSITION, 0, -1, component.getY());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/17
	 */
	@Override
	public void componentShown(ComponentEvent __e)
	{
		SwingWidget widget = this.item();
		if (widget == null)
			return;
		
		UIFormCallback callback = widget.callback();
		if (callback == null)
			return;
		
		// Report changes
		callback.propertyChange(widget.form(), widget.item(),
			UIWidgetProperty.INT_IS_SHOWN, 0, -1, 1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/17
	 */
	@Override
	public void componentHidden(ComponentEvent __e)
	{
		SwingWidget widget = this.item();
		if (widget == null)
			return;
		
		UIFormCallback callback = widget.callback();
		if (callback == null)
			return;
		
		// Report changes
		callback.propertyChange(widget.form(), widget.item(),
			UIWidgetProperty.INT_IS_SHOWN, 0, -1, 0);
	}
}
