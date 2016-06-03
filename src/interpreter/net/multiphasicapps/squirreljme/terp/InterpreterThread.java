// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodID;

/**
 * This is the base class for any threads which exist within the interpreter.
 *
 * @since 2016/06/03
 */
public abstract class InterpreterThread
{
	/**
	 * This initializes the base interpreter thread.
	 *
	 * @param __terp The owning interpreter.
	 * @param __proc The owning process.
	 * @param __mc The main class.
	 * @param __mm The main entry method.
	 * @param __args The arguments used at the start of the method.
	 * @since 2016/06/03
	 */
	public InterpreterThread(Interpreter __terp, InterpreterProcess __proc,
		ClassNameSymbol __mc, CIMethodID __mm, Object... __args)
	{
		throw new Error("TODO");
	}
}

