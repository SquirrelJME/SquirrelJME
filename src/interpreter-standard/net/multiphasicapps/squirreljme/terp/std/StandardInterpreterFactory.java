// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp.std;

import net.multiphasicapps.squirreljme.sm.StructureManager;
import net.multiphasicapps.squirreljme.terp.InterpreterFactory;

/**
 * This is a factory which creates the standard interpreter, this is generally
 * faster than the rerecording one (which is meant for debugging).
 *
 * @since 2016/06/09
 */
public class StandardInterpreterFactory
	implements InterpreterFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2016/06/09
	 */
	@Override
	public StandardInterpreter createInterpreter(StructureManager __sm,
		String... __args)
	{
		return new StandardInterpreter(__sm, __args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/09
	 */
	@Override
	public String toString()
	{
		return "standard";
	}
}

