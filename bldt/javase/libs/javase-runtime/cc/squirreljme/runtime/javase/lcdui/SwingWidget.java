// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.javase.lcdui;

import cc.squirreljme.runtime.lcdui.server.LcdWidget;
import cc.squirreljme.runtime.lcdui.WidgetType;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JComponent;

/**
 * This is a displayable which utilizes Swing.
 *
 * @since 2018/03/18
 */
public class SwingWidget
	extends LcdWidget
	implements ComponentListener
{
	/** The component which makes up this widget. */
	final JComponent _component;
	
	/**
	 * Initializes the swing displayable.
	 *
	 * @param __handle The handle for this displayable.
	 * @param __task The task owning this displayable.
	 * @param __type The type of displayable this is.
	 * @param __cb The callback manager.
	 * @since 2018/03/18
	 */
	public SwingWidget(int __handle, WidgetType __type)
	{
		super(__handle, __type);
		
		// Create base component
		JComponent component;
		switch (__type)
		{
			case DISPLAY_HEAD:
				component = new SwingDisplayHeadPanel();
				break;
			
			case DISPLAYABLE_CANVAS:
				component = new SwingCanvasPanel(this);
				break;
			
				// {@squirreljme.error AF06 Unknown displayable type. (The
				// type)}
			default:
				throw new RuntimeException(String.format("AF06 %s", __type));
		}
		
		// Set
		this._component = component;
		
		// Certain events always happen so always set them
		component.addComponentListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void componentHidden(ComponentEvent __e)
	{
	}

	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void componentMoved(ComponentEvent __e)
	{
	}

	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void componentResized(ComponentEvent __e)
	{
		this.callbackSizeChanged(this.getWidth(), this.getHeight());
	}

	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public void componentShown(ComponentEvent __e)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/19
	 */
	@Override
	public final int getHeight()
	{
		return this._component.getHeight();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/19
	 */
	@Override
	public final int getWidth()
	{
		return this._component.getWidth();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/24
	 */
	@Override
	public final void internalAdd(LcdWidget __w)
		throws NullPointerException
	{
		if (__w == null)
			throw new NullPointerException("NARG");
		
		// Add that component's widget
		this._component.add(((SwingWidget)__w)._component);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final void repaint(int __x, int __y, int __w, int __h)
	{
		JComponent component = this._component;
		component.repaint(Math.max(0, __x), Math.max(0, __y),
			Math.min(component.getWidth(), __w),
			Math.min(component.getHeight(), __h));
	}
}

