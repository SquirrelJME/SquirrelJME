// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.interpreter;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Base class for any interpreter implementation.
 *
 * @since 2022/09/08
 */
public abstract class AotInterpreter
{
	/**
	 * Installs the interpreter.
	 * 
	 * @since 2022/09/11
	 */
	public final void install()
	{
		throw Debugging.todo();
	}
}
