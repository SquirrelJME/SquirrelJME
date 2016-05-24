// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

/**
 * This represents a component which is the base for all widgets that are
 * used inside of a display.
 *
 * @since 2016/05/22
 */
public class UIComponent
	extends UIBase
{
	/** The container this component is inside. */
	private volatile UIContainer _incontainer;
	
	/**
	 * Initializes the base component that is placed within a display.
	 *
	 * @param __dm The owning display manager.
	 * @since 2016/05/22
	 */
	public UIComponent(UIManager __dm)
	{
		super(__dm);
	}
	
	/**
	 * Returns the container that this component is inside of.
	 *
	 * @return The container this component is in or {@code null} if it is not
	 * in a container.
	 * @throws UIException If it could not be obtained
	 * @since 2016/05/24
	 */
	public final UIContainer inContainer()
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			return this._incontainer;
		}
	}
	
	/**
	 * Sets the container of the component.
	 *
	 * @param __c The component to use as the container, {@code null} clears
	 * it.
	 * @throws UIException If the container could not be set, likely because
	 * a container is already set and has not been cleared.
	 * @since 2016/05/24
	 */
	final void __setContainer(UIContainer __c)
		throws UIException
	{
		// Lock
		synchronized (this.lock)
		{
			// {@squirreljme.error BD0d The component already has a container
			// set for it.}
			if (__c != null && this._incontainer != null)
				throw new UIException("BD0d");
			
			// Set
			this._incontainer = __c;
		}
	}
}

