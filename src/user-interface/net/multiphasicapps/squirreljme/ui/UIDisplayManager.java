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
 * This is a class which provides the ability to create {@link UIDisplay}s as
 * needed to interact and show information to the user. A display manager can
 * create multiple displays at the same time, however there may be limitations
 * as to the number of displays which are active at any one time. For example,
 * on modern desktop systems there may be a single window for each unique
 * display, while on a mobile device there might only be just a single display
 * at a time.
 *
 * This wraps an {@link InternalDisplayManager} so that if it lacks
 * specific functionality it can be emulated by the front layer set of classes.
 *
 * @since 2016/05/20
 */
public final class UIDisplayManager
	extends UIElement
{
	/** The internal display manager to wrap. */
	protected final InternalDisplayManager internal;
	
	/**
	 * Initializes the display manager which wraps the internal representation
	 * of the user interface.
	 *
	 * @param __idm The internal display to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	public UIDisplayManager(InternalDisplayManager __idm)
		throws NullPointerException
	{
		// Check
		if (__idm == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.internal = __idm;
		__idm.__setLink(this);
	}
	
	/**
	 * Creates a new display.
	 *
	 * @return The newly created display.
	 * @since 2016/05/21
	 */
	public UIDisplay createDisplay()
	{
		throw new Error("TODO");
	}
}

