// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.cldc.system.type.Array;
import cc.squirreljme.runtime.cldc.system.type.IntegerArray;
import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.cldc.system.type.VoidType;
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;
import cc.squirreljme.runtime.lcdui.LcdCallback;
import cc.squirreljme.runtime.lcdui.LcdWidgetOwnedException;
import cc.squirreljme.runtime.lcdui.WidgetType;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the base class for a widget which may have operations performed
 * on it through widget handles.
 *
 * @since 2018/03/23
 */
public abstract class LcdWidget
{
	/** The handle of this widget. */
	protected final int handle;
	
	/** The type of widget this is. */
	protected final WidgetType type;
	
	/** The local display for this widget. */
	LcdDisplay _localdisplay;
	
	/** The local callback for the display. */
	RemoteMethod _localcallback;
	
	/** The parent widget. */
	private LcdWidget _parent;
	
	/** The kids for the widget. */
	private final List<LcdWidget> _kids =
		new ArrayList<>();
	
	/** The title to use. */
	private String _title;
	
	/**
	 * Initializes the base widget.
	 *
	 * @param __handle The widget handle.
	 * @param __type The widget type.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public LcdWidget(int __handle, WidgetType __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		this.handle = __handle;
		this.type = __type;
	}
	
	/**
	 * Returns the height of the widget.
	 *
	 * @return The widget height.
	 * @since 2018/03/23
	 */
	public abstract int getHeight();
	
	/**
	 * Returns the widget of the widget.
	 *
	 * @return The widget width.
	 * @since 2018/03/23
	 */
	public abstract int getWidth();
	
	/**
	 * Internally adds a widget.
	 *
	 * @param __w The widget to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	public abstract void internalAdd(LcdWidget __w)
		throws NullPointerException;
	
	/**
	 * Specifies that the widget should be repainted.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/03/23
	 */
	public abstract void repaint(int __x, int __y, int __w, int __h);
	
	/**
	 * Adds the specified widget to this widget.
	 *
	 * @param __w The widget to add.
	 * @throws IllegalStateException If the widget is owned by another
	 * container.
	 * @throws LcdWidgetOwnedException If the widget is already owned by
	 * another widget.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	public final void add(LcdWidget __w)
		throws IllegalStateException, LcdWidgetOwnedException,
			NullPointerException
	{
		if (__w == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB2a Cannot add a widget that is already a
		// child of another widget.}
		if (__w._parent != null)
			throw new LcdWidgetOwnedException("EB2a");
		
		// Register widget accordingly
		List<LcdWidget> kids = this._kids;
		kids.add(__w);
		
		// Link to this widget
		__w._parent = this;
		
		// If this is in a container, update that title accordingly
		LcdWidget container = this.getContainer();
		if (container != null)
			container.setContainedTitle(__w, __w.getTitle());
		
		// Have this added to the widget
		this.internalAdd(__w);
		
		// If this is a display head then since something was added it gets
		// to have something on its display
		if (this.type == WidgetType.DISPLAY_HEAD)
			this._localdisplay.setCurrent(__w);
	}
	
	/**
	 * Returns the callback method.
	 *
	 * @return The callback method.
	 * @since 2018/03/24
	 */
	protected final RemoteMethod callback()
	{
		LcdWidget ld = this.getLocalDisplay();
		if (ld != null)
			return ld._localcallback;
		return null;
	}
	
	/**
	 * Callback that the size of the widget has changed.
	 *
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/03/24
	 */
	public final void callbackSizeChanged(int __w, int __h)
	{
		RemoteMethod callback = this.callback();
		if (callback != null)
			callback.<VoidType>invoke(VoidType.class,
				LcdCallback.WIDGET_SIZE_CHANGED, this.handle, __w, __h);
	}
	
	/**
	 * Specifies that the given displayable should be painted.
	 *
	 * @param __pf The format the pixels are in.
	 * @param __cx The clipping X coordinate.
	 * @param __cy The clipping Y coordinate.
	 * @param __cw The clipping width.
	 * @param __ch The clipping height.
	 * @param __buf The buffer to send the result into after drawing.
	 * @param __pal The palette to use for the remote buffer.
	 * @param __bw The buffer width.
	 * @param __bh The buffer height.
	 * @param __alpha Is an alpha channel being used?
	 * @param __pitch The number of elements for the width in the buffer.
	 * @param __offset The offset into the buffer to the actual image data.
	 * @throws NullPointerException On null arguments except for {@code __pal}.
	 * @since 2018/03/18
	 */
	public final void callbackPaint(PixelFormat __pf, int __cx, int __cy,
		int __cw, int __ch, Array __buf, IntegerArray __pal, int __bw,
		int __bh, boolean __alpha, int __pitch, int __offset)
		throws NullPointerException
	{
		if (__pf == null || __buf == null)
			throw new NullPointerException("NARG");
		
		// Perform the call if it is attached
		RemoteMethod callback = this.callback();
		if (callback != null)
			callback.<VoidType>invoke(VoidType.class, LcdCallback.WIDGET_PAINT,
				this.handle, __pf, __cx, __cy, __cw, __ch, __buf, __pal,
				__bw, __bh, __alpha, __pitch, __offset);
	}
	
	/**
	 * Returns the container which eventually contains this widget.
	 *
	 * @return The container for this widget or {@code null} if this is not
	 * in a container or is actually the top level container.
	 * @since 2018/03/24
	 */
	public final LcdWidget getContainer()
	{
		// Start at the parent and go up
		for (LcdWidget w = this._parent;;)
		{
			// No widget
			if (w == null)
				return null;
			
			// Stop at containers
			if (w.type.isContainer())
				return w;
			
			// Go to the parent widget
			w = w._parent;
		}
	}
	
	/**
	 * Returns the display that this is visible under or {@code null} if it is
	 * not associated with a display.
	 *
	 * @return The associated display or {@code null} if it is not associated
	 * with a display.
	 * @since 2018/03/23
	 */
	public final LcdDisplay getActualDisplay()
	{
		LcdWidget ld = this.getLocalDisplay();
		if (ld != null)
			return ld._localdisplay;
		return null;
	}
	
	/**
	 * Returns the local widget for the display head this is visible under.
	 *
	 * @return The associated display or {@code null} if it is not associated
	 * with a display.
	 * @since 2018/03/23
	 */
	public final LcdWidget getLocalDisplay()
	{
		// Constantly go up until a display is reached
		for (LcdWidget w = this;;)
		{
			// No widget
			if (w == null)
				return null;
			
			// If it is a display then stop
			if (w.type == WidgetType.DISPLAY_HEAD)
				return w;
			
			// Go to the parent widget
			w = w._parent;
		}
	}
	
	/**
	 * Returns the title of the widget.
	 *
	 * @return The widget title.
	 * @since 2018/03/23
	 */
	public final String getTitle()
	{
		return this._title;
	}
	
	/**
	 * Returns the widget handle.
	 *
	 * @return The widget handle.
	 * @since 2018/03/18
	 */
	public final int handle()
	{
		return this.handle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int hashCode()
	{
		return this.handle;
	}
	
	/**
	 * Removes the specified widget from this widget.
	 *
	 * @param __w The widget to remove.
	 * @throws IllegalStateException If the given widget is not owned.
	 * @since 2018/03/24
	 */
	public final void remove(LcdWidget __w)
		throws IllegalStateException, NullPointerException
	{
		if (__w == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Removes all widgets which are in the container.
	 *
	 * @since 2018/03/24
	 */
	public final void removeAll()
	{
		List<LcdWidget> kids = this._kids;
		while (!kids.isEmpty())
			this.remove(kids.get(kids.size() - 1));
	}
	
	/**
	 * Sets the title for this widget in the container.
	 *
	 * @param __w The inner widget being contained.
	 * @param __t The title to set.
	 * @since 2018/03/24
	 */
	public void setContainedTitle(LcdWidget __w, String __t)
	{
		// Does nothing unless this is replaced
	}
	
	/**
	 * Sets the title of the widget.
	 *
	 * @param __t The title to set, if {@code null} then a default should be
	 * used.
	 * @since 2018/03/23
	 */
	public final void setTitle(String __t)
	{
		// Cache local title
		this._title = __t;
		
		// If this is in a container, update that title accordingly
		LcdWidget container = this.getContainer();
		if (container != null)
			container.setContainedTitle(this, __t);
	}
}

