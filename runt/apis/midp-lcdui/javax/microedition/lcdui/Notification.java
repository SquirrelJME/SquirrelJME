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

import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.LcdServiceCall;

public class Notification
	extends __Collectable__
{
	/** Cache of the used label. */
	private volatile String _label;
	
	/** Cache of the used image. */
	private volatile Image _image;
	
	public Notification(NotificationType __t)
	{
		throw new todo.TODO();
	}
	
	public Notification(NotificationType __t, String __l)
	{
		throw new todo.TODO();
	}
	
	public Notification(NotificationType __t, String __l, Image __i)
	{
		throw new todo.TODO();
	}
	
	public long getTimeStamp()
	{
		throw new todo.TODO();
	}
	
	public NotificationType getType()
	{
		throw new todo.TODO();
	}
	
	public void post(boolean __s)
	{
		todo.TODO.note("Update mutable image on post.");
		throw new todo.TODO();
	}
	
	public void post(boolean __s, int __dur)
	{
		todo.TODO.note("Update mutable image on post.");
		throw new todo.TODO();
	}
	
	public void remove()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the image to be displayed for this notification. If the image is
	 * mutable then this will take a snapshot of the image and use that
	 * snapshot instead of the normal image.
	 *
	 * A new snapshot is always taken when the post command is called.
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
	
	public void setLabel(String __l)
	{
		throw new todo.TODO();
	}
	
	public void setListener(NotificationListener __nl)
	{
		throw new todo.TODO();
	}
}

