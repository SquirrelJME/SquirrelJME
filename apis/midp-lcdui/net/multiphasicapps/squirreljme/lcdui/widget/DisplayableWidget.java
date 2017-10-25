// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.widget;

import java.lang.ref.Reference;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import net.multiphasicapps.squirreljme.lcdui.DisplayManager;
import net.multiphasicapps.squirreljme.lcdui.event.EventQueue;
import net.multiphasicapps.squirreljme.lcdui.NativeResource;

/**
 * This class represents a natively displayable widget.
 *
 * @since 2017/10/25
 */
public abstract class DisplayableWidget
	implements NativeResource
{
	/** The displayable reference. */
	protected final Reference<Displayable> reference;
	
	/** The widget embedded here. */
	private volatile Embedded _embed;
	
	/**
	 * Initializes the displayable widget.
	 *
	 * @param __ref The reference to the displayable.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/25
	 */
	public DisplayableWidget(Reference<Displayable> __ref)
		throws NullPointerException
	{
		if (__ref == null)
			throw new NullPointerException("NARG");
		
		this.reference = __ref;
	}
	
	/**
	 * Returns the graphics which are used to draw on this widget.
	 *
	 * @return The graphics object for drawing directly on this widget.
	 * @since 2017/10/25
	 */
	public abstract Graphics getGraphics();
	
	/**
	 * Returns the height of the widget.
	 *
	 * @return The height in pixels.
	 * @since 2017/10/25
	 */
	public abstract int getHeight();
	
	/**
	 * Returns the width of the widget.
	 *
	 * @return The width in pixels.
	 * @since 2017/10/25
	 */
	public abstract int getWidth();
	
	/**
	 * This specifies that the widget should be repainted.
	 *
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2017/10/25
	 */
	public abstract void shouldRepaint(int __x, int __y, int __w, int __h);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/25
	 */
	@Override
	public final Reference<? extends Object> boundObject()
	{
		return this.reference;
	}
	
	/**
	 * Returns the displayable this widget is bound to.
	 *
	 * @param <D> The class to cast to.
	 * @param __cl The class to cast to.
	 * @return The displayable.
	 * @throws ClassCastException If the class type is not valid.
	 * @throws IllegalStateException If it was garbage collected.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/25
	 */
	public final <D extends Displayable> D displayable(Class<D> __cl)
		throws ClassCastException, IllegalStateException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB22 The displayable has been garbage
		// collected.}
		Displayable rv = this.reference.get();
		if (rv == null)
			throw new IllegalStateException("EB22");
		return __cl.cast(rv);
	}
	
	/**
	 * Returns the embedded object as the given class.
	 *
	 * @param <E> The class to cast to.
	 * @param __cl The class to cast to.
	 * @return The embed for this displayable.
	 * @throws ClassCastException If the class type does not match.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/25
	 */
	public final <E extends Embedded> E embed(Class<E> __cl)
		throws ClassCastException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.cast(this._embed);
	}
	
	/**
	 * Returns the event queue.
	 *
	 * @return The event queue.
	 * @since 2017/10/25
	 */
	public final EventQueue eventQueue()
	{
		return DisplayManager.DISPLAY_MANAGER.getEventQueue();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/25
	 */
	@Override
	public final void freeResource()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Embeds the specified embedded into this displayable.
	 *
	 * @param __e The embedded to embed.
	 * @throws IllegalStateException If there is already something embedded.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/25
	 */
	public final void setEmbed(Embedded __e)
		throws IllegalStateException, NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Use global lock
		synchronized (DisplayManager.GLOBAL_LOCK)
		{
			// {@squirreljme.error EB20 This widget has already had an
			// embedded placed into it.}
			if (this._embed != null)
				throw new IllegalStateException("EB20");
			
			// Embed into this and store
			__e.__embeddedInto(this);
			this._embed = __e;
		}
	}
}

