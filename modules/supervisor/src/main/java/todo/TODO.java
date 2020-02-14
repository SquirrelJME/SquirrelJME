// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package todo;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.SystemCallIndex;

/**
 * This is a class which when constructed indicates that stuff needs to be
 * done.
 *
 * @since 2019/05/25
 */
public class TODO
	extends Error
{
	/**
	 * Initializes the ToDo.
	 *
	 * @since 2019/05/25
	 */
	public TODO()
	{
		this(null);
	}
	
	/**
	 * Initializes the ToDo.
	 *
	 * @param __m The exception message.
	 * @since 2019/11/25
	 */
	public TODO(String __m)
	{
		super(__m);
		
		// Print stack trace
		todo.DEBUG.note("****** TODO HIT! ******");
		this.printStackTrace();
		todo.DEBUG.note("****** TODO HIT! ******");
		
		// Stop now
		Assembly.sysCallPV(SystemCallIndex.FATAL_TODO);
		Assembly.breakpoint();
		Assembly.sysCallPV(SystemCallIndex.EXIT, 1);
	}
}

