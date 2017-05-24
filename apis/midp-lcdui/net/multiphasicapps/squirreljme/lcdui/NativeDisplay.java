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

/**
 * This is the base class for native display engines which also provides
 * native widgets and interfaces.
 *
 * @since 2017/05/23
 */
public abstract class NativeDisplay
{
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
	}
}

