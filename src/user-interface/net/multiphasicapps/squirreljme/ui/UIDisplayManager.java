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
 * Generally at any time, there will be two distinct variants of the display
 * manager being used. One will be an implementation by the kernel
 * implementation to provide native integration, user space processes will
 * also have a UIDisplayManager which uses the kernel IPC system to interact
 * with the concrete implementation by an implementation.
 *
 * @since 2016/05/20
 */
public abstract class UIDisplayManager
{
	/**
	 * Creates a new display.
	 *
	 * @return The newly created display.
	 * @since 2016/05/21
	 */
	public abstract UIDisplay createDisplay();
}

