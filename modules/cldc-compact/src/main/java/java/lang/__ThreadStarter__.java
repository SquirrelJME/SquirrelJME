// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.ThreadStartIndex;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class is responsible for initializing and being the starting point
 * for threads.
 *
 * @since 2020/03/21
 */
@SuppressWarnings("unused")
final class __ThreadStarter__
{
	/**
	 * Not used.
	 *
	 * @since 2020/03/21
	 */
	private __ThreadStarter__()
	{
	}
	
	/**
	 * Starts the given thread.
	 *
	 * @param __tsi Multiple {@link ThreadStartIndex}, which describe how the
	 * thread is to start.
	 * @since 2002/03/21
	 */
	@SuppressWarnings("unused")
	static void __start(long[] __tsi)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
}
