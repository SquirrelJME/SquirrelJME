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

import cc.squirreljme.runtime.cldc.system.type.LocalIntegerArray;
import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.DisplayableType;
import cc.squirreljme.runtime.lcdui.server.LcdCallbackManager;
import cc.squirreljme.runtime.lcdui.server.LcdDisplayable;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JPanel;

/**
 * This is a displayable which utilizes Swing.
 *
 * @since 2018/03/18
 */
public class SwingDisplayable
	extends LcdDisplayable
{
	/** The panel which makes up this displayable. */
	final JPanel _panel;
	
	/** The title to use. */
	private volatile String _title;
	
	/**
	 * Initializes the swing displayable.
	 *
	 * @param __handle The handle for this displayable.
	 * @param __task The task owning this displayable.
	 * @param __type The type of displayable this is.
	 * @param __cb The callback manager.
	 * @since 2018/03/18
	 */
	public SwingDisplayable(int __handle, SystemTask __task,
		DisplayableType __type, LcdCallbackManager __cb)
	{
		super(__handle, __task, __type, __cb);
		
		JPanel panel;
		switch (__type)
		{
			case CANVAS:
				panel = new SwingCanvasPanel();
				break;
			
				// {@squirreljme.error AF06 Unknown displayable type. (The
				// type)}
			default:
				throw new RuntimeException(String.format("AF06 %s", __type));
		}
		this._panel = panel;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final String getTitle()
	{
		return this._title;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final void repaint(int __x, int __y, int __w, int __h)
	{
		JPanel panel = this._panel;
		panel.repaint(Math.max(0, __x), Math.max(0, __y),
			Math.min(panel.getWidth(), __w), Math.min(panel.getHeight(), __h));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final void setTitle(String __t)
	{
		this._title = __t;
		
		// If this is bound to a display then update the title
		SwingDisplay display = (SwingDisplay)this.getCurrent();
		if (display != null)
			display._frame.setTitle(__t);
	}
	
	/**
	 * The panel for canvases for displaying basic graphics.
	 *
	 * @since 2018/03/18
	 */
	public final class SwingCanvasPanel
		extends JPanel
		implements ComponentListener
	{
		/** The image to display in the panel. */
		private volatile BufferedImage _image =
			new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		
		/**
		 * Handles events and resizing as needed.
		 *
		 * @since 2018/03/18
		 */
		{
			this.addComponentListener(this);
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
			BufferedImage image = this._image;
			int oldw = image.getWidth(),
				oldh = image.getHeight(),
				neww = this.getWidth(),
				newh = this.getHeight();
		
			// Recreate the image if it is larger
			if (neww != oldw || newh != oldh)
				this._image = new BufferedImage(neww, newh,
					BufferedImage.TYPE_INT_RGB);
		
			// Send repaint event
			this.repaint(0, 0, neww, newh);
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
		 * @since 2017/10/25
		 */
		@Override
		protected void paintComponent(java.awt.Graphics __g)
		{
			// This must always be called
			super.paintComponent(__g);
			
			BufferedImage image = this._image;
			int xw = getWidth(),
				xh = getHeight();
			
			// Have the remote end draw into our buffer as needed
			Rectangle rect = __g.getClipBounds();
			SwingDisplayable.this.callbacks.displayablePaint(
				SwingDisplayable.this,
				rect.x, rect.y, rect.width, rect.height, new LocalIntegerArray(
				((DataBufferInt)image.getRaster().getDataBuffer()).getData()),
				null, xw, xh, false, xw, 0);
			
			// Draw the backed buffered image
			__g.drawImage(image, 0, 0, xw, xh,
				0, 0, xw, xh, null);
		}
	}
}

