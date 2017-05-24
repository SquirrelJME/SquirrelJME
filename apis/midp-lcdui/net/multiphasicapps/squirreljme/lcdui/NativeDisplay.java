// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import net.multiphasicapps.squirreljme.unsafe.SquirrelJME;

/**
 * This is the base class for native display engines which also provides
 * native widgets and interfaces.
 *
 * @since 2017/05/23
 */
public abstract class NativeDisplay
{
	/** The default native display to use. */
	public static final NativeDisplay DISPLAY;
	
	/** The default head. */
	private volatile Reference<NativeDisplay.Head> _defaulthead;
	
	/**
	 * Initialize the single native display instance.
	 *
	 * @since 2017/06/24
	 */
	static
	{
		// Setup native display engine
		NativeDisplay nd = SquirrelJME.systemService(NativeDisplay.class);
		if (nd == null)
			nd = new NullNativeDisplay();
		DISPLAY = nd;
	}
	
	/**
	 * Creates a native canvas which is used as the most direct means of
	 * rendering graphics. If the native display does not support a native
	 * set of widgets then it will be simulated using a canvas.
	 *
	 * @param __ref The reference to the canvas.
	 * @return The native canvas.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/24
	 */
	public abstract NativeCanvas createCanvas(Reference<Displayable> __ref)
		throws NullPointerException;
	
	/**
	 * Returns the display heads which are available to be used to display
	 * canvases and other widgets. Head initialization should only be performed
	 * once.
	 *
	 * @return The display heads.
	 * @since 2017/05/24
	 */
	public abstract NativeDisplay.Head[] heads();
	
	/**
	 * Creates a new displayable of the given type.
	 *
	 * @param __t The type of displayable to create.
	 * @param __ref The back reference to the displayable object.
	 * @return The native displayable.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/24
	 */
	public NativeDisplayable create(DisplayableType __t,
		Reference<Displayable> __ref)
		throws NullPointerException
	{
		// Check
		if (__t == null || __ref == null)
			throw new NullPointerException("NARG");
		
		// Depends on the type
		switch (__t)
		{
				// Canvases are never simulated
			case CANVAS:
				return createCanvas((Reference<Displayable>)__ref);
			
				// Should not happen
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Returns the default display head.
	 *
	 * @return The default display head.
	 * @since 2017/05/24
	 */
	public final NativeDisplay.Head defaultHead()
	{
		Reference<NativeDisplay.Head> ref = this._defaulthead;
		NativeDisplay.Head rv;
		
		// Check
		if (ref == null || null == (rv = ref.get()))
			this._defaulthead = new WeakReference<>((rv = heads()[0]));
		
		return rv;
	}
	
	/**
	 * This is the head for a single display.
	 *
	 * @since 2017/05/23
	 */
	public abstract class Head
	{
		/**
		 * Initializes the display head.
		 *
		 * @since 2017/05/24
		 */
		protected Head()
		{
		}
		
		/**
		 * Returns the current height of the content contained within this
		 * head.
		 *
		 * @return The height of the head's content.
		 * @since 2017/05/24
		 */
		public abstract int getContentHeight();
		
		/**
		 * Returns the maximum height of this head.
		 *
		 * @return This head's height.
		 * @since 2017/05/24
		 */
		public abstract int getMaximumHeight();
	
		/**
		 * Returns the maximum width of this head.
		 *
		 * @return This head's width.
		 * @since 2017/07/24
		 */
		public abstract int getMaximumWidth();
		
		/**
		 * Returns the current width of the content contained within this
		 * head.
		 *
		 * @return The width of the head's content.
		 * @since 2017/05/24
		 */
		public abstract int getContentWidth();
	}
}

