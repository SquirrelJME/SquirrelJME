// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui;

import cc.squirreljme.runtime.lcdui.mle.UIBackendFactory;

/**
 * Useful form utilities.
 *
 * @since 2020/07/27
 */
public final class FormUtils
{
	/**
	 * Not used.
	 * 
	 * @since 2020/07/27
	 */
	private FormUtils()
	{
	}
	
	/**
	 * Flushes the event queue and waits.
	 * 
	 * @param __ms The time to wait.
	 * @since 2020/07/27
	 */
	public static void flushAndWait(long __ms)
	{
		// Flush events
		UIBackendFactory.getInstance().flushEvents();
		
		// Wait a bit
		for (long at = System.currentTimeMillis(), end = at + __ms;
			at < end; at = System.currentTimeMillis())
			try
			{
				Thread.sleep(end - at);
			}
			catch (InterruptedException ignored)
			{
			}
	}
	
	/**
	 * Returns a time in the future.
	 * 
	 * @param __ms The time to put into the future.
	 * @return The time into the future.
	 * @since 2020/07/27
	 */
	public static long futureTime(int __ms)
	{
		return System.currentTimeMillis() + __ms;
	}
	
	/**
	 * Continues until the given time occurs.
	 * 
	 * @param __end The time to stop at.
	 * @return If still waiting for the end.
	 * @since 2020/07/27
	 */
	public static boolean untilThen(long __end)
	{
		return System.currentTimeMillis() < __end;
	}
}
