// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.Spacer;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;

/**
 * The method used for drawing items.
 *
 * @since 2019/12/09
 */
public enum DrawingItemMethod
{
	/** Choice group. */
	CHOICE_GROUP
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/12/09
		 */
		@Override
		public void paint(Displayable __d, Item __i, State __s, Graphics __g,
			int __x, int __y, int __w, int __h, boolean __enb, boolean __sel)
		{
			__g.drawString("Choice Group", __x, __y, Graphics.TOP);
		}
	},
	
	/** Custom item. */
	CUSTOM_ITEM
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/12/09
		 */
		@Override
		public void paint(Displayable __d, Item __i, State __s, Graphics __g,
			int __x, int __y, int __w, int __h, boolean __enb, boolean __sel)
		{
			__g.drawString("Custom Item", __x, __y, Graphics.TOP);
		}
	},
	
	/** Date field. */
	DATE_FIELD
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/12/09
		 */
		@Override
		public void paint(Displayable __d, Item __i, State __s, Graphics __g,
			int __x, int __y, int __w, int __h, boolean __enb, boolean __sel)
		{
			__g.drawString("Date Field", __x, __y, Graphics.TOP);
		}
	},
	
	/** Gauge. */
	GAUGE
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/12/09
		 */
		@Override
		public void paint(Displayable __d, Item __i, State __s, Graphics __g,
			int __x, int __y, int __w, int __h, boolean __enb, boolean __sel)
		{
			__g.drawString("Gauge", __x, __y, Graphics.TOP);
		}
	},
	
	/** Image item. */
	IMAGE_ITEM
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/12/09
		 */
		@Override
		public void paint(Displayable __d, Item __i, State __s, Graphics __g,
			int __x, int __y, int __w, int __h, boolean __enb, boolean __sel)
		{
			__g.drawString("Image Item", __x, __y, Graphics.TOP);
		}
	},
	
	/** Spacer. */
	SPACER
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/12/09
		 */
		@Override
		public void paint(Displayable __d, Item __i, State __s, Graphics __g,
			int __x, int __y, int __w, int __h, boolean __enb, boolean __sel)
		{
			__g.drawString("Spacer", __x, __y, Graphics.TOP);
		}
	},
	
	/** String item. */
	STRING_ITEM
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/12/09
		 */
		@Override
		public void paint(Displayable __d, Item __i, State __s, Graphics __g,
			int __x, int __y, int __w, int __h, boolean __enb, boolean __sel)
		{
			StringItem item = (StringItem)__i;
			
			// Only draw text if it is to be displayed
			String text = item.getText();
			if (text != null)
			{
				// Set font to use
				__g.setFont(item.getFont());
				
				// Draw text
				__g.drawString(text, __x, __y, 0);
			}
		}
	},
	
	/** Text field. */
	TEXT_FIELD
	{
		/**
		 * {@inheritDoc}
		 * @since 2019/12/09
		 */
		@Override
		public void paint(Displayable __d, Item __i, State __s, Graphics __g,
			int __x, int __y, int __w, int __h, boolean __enb, boolean __sel)
		{
			__g.drawString("Text Field", __x, __y, Graphics.TOP);
		}
	},
	
	/* End. */
	;
	
	/**
	 * Paints the method.
	 *
	 * @param __d The displayable to be drawn.
	 * @param __i The item to draw.
	 * @param __s The draw state.
	 * @param __g The graphics to draw to.
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __w The item width.
	 * @param __h The item height.
	 * @param __enb Is this enabled?
	 * @param __sel Is this selected?
	 * @since 2019/12/09
	 */
	public abstract void paint(Displayable __d, Item __i, State __s,
		Graphics __g, int __x, int __y, int __w, int __h, boolean __enb,
		boolean __sel);
	
	/**
	 * Returns the drawing method for the given class.
	 *
	 * @param __cl The class to draw for.
	 * @return The drawing method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/09
	 */
	public static final DrawingItemMethod of(Class<?> __cl)
		throws NullPointerException
	{
		if (ChoiceGroup.class.isAssignableFrom(__cl))
			return DrawingItemMethod.CHOICE_GROUP;
		else if (CustomItem.class.isAssignableFrom(__cl))
			return DrawingItemMethod.CUSTOM_ITEM;
		else if (DateField.class.isAssignableFrom(__cl))
			return DrawingItemMethod.DATE_FIELD;
		else if (Gauge.class.isAssignableFrom(__cl))
			return DrawingItemMethod.GAUGE;
		else if (ImageItem.class.isAssignableFrom(__cl))
			return DrawingItemMethod.IMAGE_ITEM;
		else if (Spacer.class.isAssignableFrom(__cl))
			return DrawingItemMethod.SPACER;
		else if (StringItem.class.isAssignableFrom(__cl))
			return DrawingItemMethod.STRING_ITEM;
		else if (TextField.class.isAssignableFrom(__cl))
			return DrawingItemMethod.TEXT_FIELD;
		
		// {@squirreljme.error EB39 Could not get the item drawing method of
		// the given class. (The class)}
		throw new IllegalArgumentException("EB39 " + __cl);
	}
}

