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
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.FileSelector;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
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
			__g.drawString("FORM", 0, 0, 0);
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
			
			__g.drawString("LIST", 0, 0, 0);
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
	
	/** End. */
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
			return ALERT;
		else if (Canvas.class.isAssignableFrom(__cl))
			return CANVAS;
		else if (FileSelector.class.isAssignableFrom(__cl))
			return FILE_SELECTOR;
		else if (Form.class.isAssignableFrom(__cl))
			return FORM;
		else if (List.class.isAssignableFrom(__cl))
			return LIST;
		else if (TabbedPane.class.isAssignableFrom(__cl))
			return TABBED_PANE;
		else if (TextBox.class.isAssignableFrom(__cl))
			return TEXT_BOX;
		
		// {@squirreljme.error EB3d Could not get the drawing method of the
		// given class. (The class)}
		throw new IllegalArgumentException("EB3d " + __cl);
	}
}

