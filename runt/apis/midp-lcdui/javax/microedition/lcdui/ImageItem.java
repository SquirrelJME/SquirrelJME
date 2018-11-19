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

import cc.squirreljme.runtime.lcdui.SerializedEvent;

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
	
	/** Cache of the used image. */
	private volatile Image _image;
	
	public ImageItem(String __a, Image __b, int __c, String __d)
	{
		super();
		throw new todo.TODO();
	}
	
	public ImageItem(String __a, Image __b, int __c, String __d, int __e)
	{
		super();
		throw new todo.TODO();
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
	
	@Override
	public int getLayout()
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
	
	@Override
	public void setLayout(int __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	void __drawChain(Graphics __g)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/18
	 */
	@Override
	void __updateDrawChain(__DrawSlice__ __sl)
	{
		throw new todo.TODO();
	}
}


