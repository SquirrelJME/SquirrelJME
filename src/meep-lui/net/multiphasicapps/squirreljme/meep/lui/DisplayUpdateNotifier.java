// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.meep.lui;

/**
 * When the text in the display has been changed, this is called to notify
 * the driver manually that the screen has been updated.
 *
 * @since 2016/09/11
 */
public interface DisplayUpdateNotifier
{
	/**
	 * This is called when the screen has been updated.
	 *
	 * @since 2016/09/11
	 */
	public abstract void screenUpdated();
}

