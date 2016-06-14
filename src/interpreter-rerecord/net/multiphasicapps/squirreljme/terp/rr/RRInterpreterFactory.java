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

import net.multiphasicapps.squirreljme.rtobj.RuntimeObjectManager;
import net.multiphasicapps.squirreljme.terp.InterpreterFactory;

/**
 * This is a factory which creates the rerecording interpreter.
 *
 * @since 2016/06/09
 */
public class RRInterpreterFactory
	implements InterpreterFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2016/06/09
	 */
	@Override
	public RRInterpreter createInterpreter(RuntimeObjectManager __sm,
		String... __args)
	{
		return new RRInterpreter(__sm, __args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/09
	 */
	@Override
	public String toString()
	{
		return "rerecording";
	}
}

