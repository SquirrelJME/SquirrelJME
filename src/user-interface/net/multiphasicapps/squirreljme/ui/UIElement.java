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
 * This is the base class for all displayable elements.
 *
 * @since 2016/05/21
 */
public abstract class UIElement
{
	/** The external display manager. */
	protected final UIDisplayManager displaymanager;
	
	/**
	 * Initializes the base element.
	 *
	 * @param __dm The external display manager.
	 * @throws NullPointerException On null arguments if this is not a display
	 * manager.
	 * @since 2016/05/21
	 */
	UIElement(UIDisplayManager __dm)
		throws NullPointerException
	{
		// If this is a display manager then null is acceptable because the
		// value to set is just this.
		if (this instanceof UIDisplayManager)
			this.displaymanager = (UIDisplayManager)this;
		
		// Otherwise this is some other element type.
		else
		{
			// Check
			if (__dm == null)
				throw new NullPointerException("NARG");
		
			// Set
			this.displaymanager = __dm;
		}
	}
	
	/**
	 * Returns the external display manager owning this element.
	 *
	 * @return The owning display manager.
	 * @since 2016/05/22
	 */
	public final UIDisplayManager displayManager()
	{
		return this.displaymanager;
	}
}

