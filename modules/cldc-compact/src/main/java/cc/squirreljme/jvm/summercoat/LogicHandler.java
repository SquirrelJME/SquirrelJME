// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This handles specific JVM logic that cannot easily be handled in byte code
 * or at the execution layer.
 *
 * @since 2020/11/28
 */
public final class LogicHandler
{
	/**
	 * Not used.
	 * 
	 * @since 2020/11/28
	 */
	private LogicHandler()
	{
	}
	
	/**
	 * Garbage collects the given handle.
	 * 
	 * @param __p The pointer to clear.
	 * @since 2020/11/28
	 */
	public static void gcMemHandle(int __p)
	{
		Assembly.breakpoint();
		throw Debugging.todo();
	}
}
