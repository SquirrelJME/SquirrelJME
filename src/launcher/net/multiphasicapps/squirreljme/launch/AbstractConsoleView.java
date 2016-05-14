// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch;

/**
 * This provides an abstract implementation of a standard console interface
 * which is used for limited systems which only have a graphical text console
 * available for usage.
 *
 * The internal character buffers and such are managed by the console view,
 * the implementing interface just has to draw the console in most cases.
 *
 * @since 2016/05/14
 */
public abstract class AbstractConsoleView
{
	/**
	 * Initializes the base console view, which is initially sized to be a
	 * 1x1 character terminal.
	 *
	 * @since 2016/05/14
	 */
	public AbstractConsoleView()
	{
		setSize(1, 1);
	}
	
	/**
	 * Displays the internal console buffer to the system's specific console
	 * interface.
	 *
	 * @since 2016/05/14
	 */
	public abstract void displayConsole();
	
	/**
	 * Sets the size of the console device.
	 *
	 * @param __c The number of columns to use.
	 * @param __r The number of rows to use.
	 * @throws IllegalArgumentException If the number rows or columns are
	 * zero or negative.
	 * @since 2016/05/14
	 */
	public final void setSize(int __c, int __r)
	{
		// {@squirreljme.error AY01 Requested the the console view be set to
		// a zero or negative size. (The requested columns; The requested
		// rows)}
		if (__c <= 0 || __r <= 0)
			throw new IllegalArgumentException(String.format("AY01 %d %d",
				__c, __r));
		
		throw new Error("TODO");
	}
}

