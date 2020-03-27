// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.FileSelector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TabbedPane;
import javax.microedition.lcdui.TextBox;

/**
 * This contains various drawing methods which modify how a display is
 * drawn.
 *
 * @since 2019/05/18
 */
public enum DrawingMethod
{
	/** Alert. */
	ALERT
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/05/18
		 */
		@Override
		public final void paint(Displayable __d, State __s, Graphics __g,
			int __w, int __h)
		{
			__g.drawString("ALERT", 0, 0, 0);
		}
	},
	
	/** Canvas. */
	CANVAS
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/05/18
		 */
		@Override
		public final void paint(Displayable __d, State __s, Graphics __g,
			int __w, int __h)
		{
			((ExposedDisplayable)__d).paint(__g);
		}
	},
	
	/** File selector. */
	FILE_SELECTOR
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/05/18
		 */
		@Override
		public final void paint(Displayable __d, State __s, Graphics __g,
			int __w, int __h)
		{
			__g.drawString("FILE_SELECTOR", 0, 0, 0);
		}
	},
	
	/** Form. */
	FORM
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/05/18
		 */
		@Override
		public final void paint(Displayable __d, State __s, Graphics __g,
			int __w, int __h)
		{
			Form form = (Form)__d;
			
			// Number of entries
			int n = form.size();
			
			// Working base coordinates for each item
			int dx = 0,
				dy = 0;
			
			// Font and height for labels
			Font labelfont = StandardMetrics.itemLabelFont();
			int labelh = labelfont.getHeight();
			
			// Draw each entry
			for (int i = 0; i < n; i++)
			{
				// Reset X draw
				dx = 0;
				
				// Get item here
				Item item = form.get(i);
				
				// Get item properties
				String il = item.getLabel();
				int iw = Math.min(__w, item.getPreferredWidth());
				int ih = item.getPreferredHeight();
				boolean ie = true;
				boolean is = false;
				
				// Fill background in
				__g.setAlphaColor(StandardMetrics.itemBackgroundColor(ie, is));
				__g.fillRect(dx, dy, iw, ih);
				
				// Set used color
				__g.setAlphaColor(StandardMetrics.itemForegroundColor(ie, is));
				
				// Draw label if one is used
				if (il != null)
				{
					// Draw label
					__g.setFont(labelfont);
					__g.drawString(il, dx, dy, Graphics.TOP);
					
					// Move to item area now
					dy += labelh;
				}
				
				// Draw the item
				DrawingItemMethod.of(item.getClass()).paint(__d, item, __s,
					__g, dx, dy, iw, ih, true, false);
				
				// Is this item being focused? Set the focus box on it
				/*if (item == form.getCurrent())
					__s.focusbox.set(dx, dy, iw - 2, ih - 2);*/
				
				// Move to next item
				dy += ih;
			}
		}
	},
	
	/** List. */
	LIST
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/05/18
		 */
		@Override
		public final void paint(Displayable __d, State __s, Graphics __g,
			int __w, int __h)
		{
			List list = (List)__d;
			
			// Working base coordinates for each item
			int dx = 0,
				dy = 0;
			
			// Selected index
			int sel = list.getSelectedIndex();
			
			// Make sure focus is in bounds
			int focusdx = __s.focusdx;
			int n = list.size();
			if (focusdx < 0)
				focusdx = 0;
			else if (focusdx >= n)
				focusdx = n - 1;
			
			// Default font
			Font dfont = Font.getFont(StandardMetrics.LIST_ITEM_FONT, 0,
				StandardMetrics.LIST_ITEM_HEIGHT);
			
			// Draw all list items
			for (int i = 0; i < n; i++)
			{
				// Reset X draw
				dx = 0;
				
				// Get item properties
				String vs = list.getString(i);
				Image vi = list.getImage(i);
				Font vf = list.getFont(i);
				boolean ve = list.isEnabled(i);
				boolean vl = list.isSelected(i);
				
				// Use a default fallback font?
				if (vf == null)
					vf = dfont;
				
				// Height of this item
				int ih = vf.getPixelSize();
				
				// Increase height for the image size?
				if (vi != null)
				{
					int mh = vi.getHeight();
					if (mh > ih)
						ih = mh;
				}
				
				// Draw background
				__g.setColor(StandardMetrics.itemBackgroundColor(ve, vl));
				__g.fillRect(dx, dy,
					__w, ih);
				
				// Draw image?
				if (vi != null)
				{
					__g.drawImage(vi, dx, dy, Graphics.TOP | Graphics.LEFT);
					dx += vi.getWidth();
				}
				
				// Draw text
				__g.setColor(StandardMetrics.itemForegroundColor(ve, vl));
				__g.setFont(vf);
				__g.drawString(vs, dx, dy, Graphics.TOP | Graphics.LEFT);
				
				// Is this item being focused?
				if (focusdx == i)
					__s.focusbox.set(dx, dy, __w - 2, ih - 2);
				
				// Move to next item
				dy += ih;
			}
		}
	},
	
	/** Tabbed pane. */
	TABBED_PANE
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/05/18
		 */
		@Override
		public final void paint(Displayable __d, State __s, Graphics __g,
			int __w, int __h)
		{
			__g.drawString("TABBED_PANE", 0, 0, 0);
		}
	},
	
	/** Text box. */
	TEXT_BOX
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/05/18
		 */
		@Override
		public final void paint(Displayable __d, State __s, Graphics __g,
			int __w, int __h)
		{
			__g.drawString("TEXT_BOX", 0, 0, 0);
		}
	},
	
	/* End. */
	;
	
	/**
	 * Paints the method.
	 *
	 * @param __d The displayable to be drawn.
	 * @param __s The draw state.
	 * @param __g The graphics to draw to.
	 * @param __w The display width.
	 * @param __h The display height.
	 * @since 2019/05/18
	 */
	public abstract void paint(Displayable __d, State __s, Graphics __g,
		int __w, int __h);
	
	/**
	 * Returns the drawing method for the given class.
	 *
	 * @param __cl The class to draw for.
	 * @return The drawing method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/18
	 */
	public static final DrawingMethod of(Class<?> __cl)
		throws NullPointerException
	{
		if (Alert.class.isAssignableFrom(__cl))
			return DrawingMethod.ALERT;
		else if (Canvas.class.isAssignableFrom(__cl))
			return DrawingMethod.CANVAS;
		else if (FileSelector.class.isAssignableFrom(__cl))
			return DrawingMethod.FILE_SELECTOR;
		else if (Form.class.isAssignableFrom(__cl))
			return DrawingMethod.FORM;
		else if (List.class.isAssignableFrom(__cl))
			return DrawingMethod.LIST;
		else if (TabbedPane.class.isAssignableFrom(__cl))
			return DrawingMethod.TABBED_PANE;
		else if (TextBox.class.isAssignableFrom(__cl))
			return DrawingMethod.TEXT_BOX;
		
		// {@squirreljme.error EB18 Could not get the drawing method of the
		// given class. (The class)}
		throw new IllegalArgumentException("EB18 " + __cl);
	}
}

