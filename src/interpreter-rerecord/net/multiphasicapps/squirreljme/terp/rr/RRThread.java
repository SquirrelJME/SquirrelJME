// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp.rr;

import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodID;
import net.multiphasicapps.squirreljme.terp.InterpreterThread;

/**
 * This is a rerecorded interpreter thread.
 *
 * @since 2016/06/03
 */
public class RRThread
	extends InterpreterThread
{
	/**
	 * Initializes the thread.
	 *
	 * @param __terp The owning interpreter.
	 * @param __ip The owning process.
	 * @param __mc The starting class.
	 * @param __mm The starting method.
	 * @param __args The arguments to the thread.
	 * @since 2016/06/03
	 */
	public RRThread(RRInterpreter __terp, RRProcess __ip, ClassNameSymbol __mc,
		CIMethodID __mm, Object... __args)
	{
		super(__terp, __ip, __mc, __mm, __args);
		
		throw new Error("TODO");
	}
}

