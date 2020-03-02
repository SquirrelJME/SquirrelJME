// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

public class ImageItem
	extends Item
{
	public static final int LAYOUT_CENTER =
		3;
	
	public static final int LAYOUT_DEFAULT =
		0;
	
	public static final int LAYOUT_LEFT =
		1;
	
	public static final int LAYOUT_NEWLINE_AFTER =
		512;
	
	public static final int LAYOUT_NEWLINE_BEFORE =
		256;
	
	public static final int LAYOUT_RIGHT =
		2;
	
	/** Apperance mode. */
	final int _amode;
	
	/** Cache of the used image. */
	private volatile Image _image;
	
	/** Alternative text. */
	private volatile String _alt;
	
	/**
	 * Initializes the image item.
	 *
	 * @param __l The label.
	 * @param __i The image.
	 * @param __lay The layout.
	 * @param __alt The alternative text.
	 * @throws IllegalArgumentException If the layout is not valid.
	 * @since 2019/05/17
	 */
	public ImageItem(String __l, Image __i, int __lay, String __alt)
		throws IllegalArgumentException
	{
		this(__l, __i, __lay, __alt, Item.PLAIN);
	}
	
	/**
	 * Initializes the image item.
	 *
	 * @param __l The label.
	 * @param __i The image.
	 * @param __lay The layout.
	 * @param __alt The alternative text.
	 * @param __am The appearance mode.
	 * @throws IllegalArgumentException If the layout is not valid.
	 * @since 2019/05/17
	 */
	public ImageItem(String __l, Image __i, int __lay, String __alt, int __am)
		throws IllegalArgumentException
	{
		super(__l);
		
		// {@squirreljme.error EB2i The appearance mode is not valid.
		// (The appearance mode)}
		if (__am != Item.PLAIN && __am != Item.BUTTON && __am != Item.HYPERLINK)
			throw new IllegalArgumentException("EB2i " + __am);
		
		this._image = __i;
		this._alt = __alt;
		this._amode = __am;
		
		// Set the layout
		this.setLayout(__lay);
	}
	
	public String getAltText()
	{
		throw new todo.TODO();
	}
	
	public int getAppearanceMode()
	{
		throw new todo.TODO();
	}
	
	public Image getImage()
	{
		throw new todo.TODO();
	}
	
	public void setAltText(String __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the image to be displayed for this item. If the image is mutable
	 * then this will take a snapshot of the image and use that snapshot
	 * instead of the normal image.
	 *
	 * A new snapshot from a mutable image can be created by performing:
	 * {@code imageitem.setImage(imageitem.getImage())}.
	 *
	 * @param __i The image to set or {@code null} to clear it.
	 * @since 2018/04/06
	 */
	public void setImage(Image __i)
	{
		throw new todo.TODO();
		/*
		Image clone = (__i != null && __i.isMutable() ?
			Image.createImage(__i) : __i);
		LcdServiceCall.voidCall(LcdFunction.SET_IMAGE, this._handle,
			(__i == null ? -1 : __i._handle),
			(clone == null ? -1 : clone._handle));
		this._image = __i;
		*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/17
	 */
	@Override
	public void setLayout(int __lay)
		throws IllegalArgumentException
	{
		super.setLayout(__lay);
	}
}


