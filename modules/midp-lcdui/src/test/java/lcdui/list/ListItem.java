// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.list;

import java.util.Objects;
import java.util.Random;
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
	
	/**
	 * Returns a randomized item.
	 * 
	 * @param __rand The randomized source.
	 * @return A newly randomized list item.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/13
	 */
	public static ListItem random(Random __rand)
		throws NullPointerException
	{
		if (__rand == null)
			throw new NullPointerException("NARG");
		
		// Face
		int face;
		switch (__rand.nextInt(3))
		{
			case 0:		face = Font.FACE_MONOSPACE; break;
			case 1:		face = Font.FACE_PROPORTIONAL; break;
			default:	face = Font.FACE_SYSTEM; break;
		}
		
		// Style
		int style;
		switch (__rand.nextInt(4))
		{
			case 0:		style = Font.STYLE_BOLD; break;
			case 1:		style = Font.STYLE_ITALIC; break;
			case 2:		style = Font.STYLE_PLAIN; break;
			default:	style = Font.STYLE_UNDERLINED; break;
		}
		
		// Size
		int size;
		switch (__rand.nextInt(3))
		{
			case 0:		size = Font.SIZE_LARGE; break;
			case 1:		size = Font.SIZE_MEDIUM; break;
			default:	size = Font.SIZE_SMALL; break;
		}
		
		return new ListItem(
			Character.toString((char)('a' + __rand.nextInt(25))),
			Image.createImage(1 + __rand.nextInt(16),
				1 + __rand.nextInt(16)),
			Font.getFont(face, style, size),
			__rand.nextBoolean(), __rand.nextBoolean());
	}
}
