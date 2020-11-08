// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.list;

import java.util.Objects;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;

/**
 * This stores information about a single list item.
 *
 * @since 2020/11/08
 */
public final class ListItem
{
	/** Label. */
	public String label;
	
	/** Image. */
	public Image image;
	
	/** Font. */
	public Font font;
	
	/** Is this enabled? */
	public boolean enabled;
	
	/** Is this selected? */
	public boolean selected;
	
	/**
	 * Initializes the given list item.
	 * 
	 * @param __label The label.
	 * @param __image The image.
	 * @param __font The font.
	 * @param __enabled Is this enabled?
	 * @param __selected Is this selected?
	 * @throws NullPointerException If there is no label.
	 * @since 2020/11/08
	 */
	public ListItem(String __label, Image __image, Font __font,
		boolean __enabled, boolean __selected)
		throws NullPointerException
	{
		if (__label == null)
			throw new NullPointerException("NARG");
		
		this.label = __label;
		this.image = __image;
		this.font = __font;
		this.enabled = __enabled;
		this.selected = __selected;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/08
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		
		if (!(__o instanceof ListItem))
			return false;
		
		ListItem o = (ListItem)__o;
		return this.hashCode() == o.hashCode() &&
			this.enabled == o.enabled &&
			this.selected == o.selected &&
			Objects.equals(this.label, o.label) &&
			Objects.equals(this.image, o.image) &&
			Objects.equals(this.font, o.font);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/08
	 */
	@Override
	public int hashCode()
	{
		int rv = 0;
		
		rv ^= (this.selected ? 0x8000_0000 : 0x4000_0000);
		rv ^= (this.enabled ? 0x0800_0000 : 0x0400_0000);
		rv ^= (this.label != null ?
			(this.label.hashCode() | 0x0080_0000) : 0x0040_0000);
		rv ^= (this.image != null ?
			(this.image.hashCode() | 0x0008_0000) : 0x0004_0000);
		rv ^= (this.font != null ?
			(this.font.hashCode() | 0x0000_8000) : 0x0000_4000);
		
		return rv;
	}
	
	/**
	 * Sets the given list item.
	 * 
	 * @param __list The list to set.
	 * @param __dx The index to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/08
	 */
	public final void into(List __list, int __dx)
		throws NullPointerException
	{
		if (__list == null)
			throw new NullPointerException("NARG");
		
		__list.set(__dx, this.label, this.image);
		__list.setFont(__dx, this.font);
		__list.setSelectedIndex(__dx, this.selected);
		__list.setEnabled(__dx, this.enabled);
	}
	
	/**
	 * Returns information on the given list item.
	 * 
	 * @param __list The list to get from.
	 * @param __dx The index to read.
	 * @return The resulting list item.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/08
	 */
	public static ListItem of(List __list, int __dx)
		throws NullPointerException
	{
		if (__list == null)
			throw new NullPointerException("NARG");
		
		return new ListItem(__list.getString(__dx),
			__list.getImage(__dx),
			__list.getFont(__dx),
			__list.isEnabled(__dx),
			__list.isSelected(__dx));
	}
}
