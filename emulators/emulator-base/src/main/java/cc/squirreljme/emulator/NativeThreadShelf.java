// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Native thread shelf.
 *
 * @since 2020/09/12
 */
public final class NativeThreadShelf
{
	/**
	 * Not used.
	 * 
	 * @since 2020/09/12
	 */
	private NativeThreadShelf()
	{
	}
	
	/**
	 * As {@link ThreadShelf#javaThreadSetDaemon(Thread)}. 
	 * 
	 * @param __thread The thread to use.
	 * @throws MLECallError If the thread is null or is already alive.
	 * @since 2020/09/12
	 */
	public static void javaThreadSetDaemon(Thread __thread)
		throws MLECallError
	{
		if (__thread == null)
			throw new MLECallError("Null thread.");
		
		try
		{
			__thread.setDaemon(true);
		}
		catch (IllegalThreadStateException ignored)
		{
			throw new MLECallError("Thread is alive.");
		}
	}
}
