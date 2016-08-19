// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITMethodWriter;

/**
 * This is the base class for the native machine code generator which is common
 * to many architectures. Support for architectures extends this class which
 * is used to create the actual machine code.
 *
 * @since 2016/08/19
 */
public abstract class GenericMethodWriter
	implements JITMethodWriter
{
	/**
	 * {@inheritDoc}
	 * @since 2016/08/19
	 */
	@Override
	public void close()
		throws JITException
	{
		throw new Error("TODO");
	}
}

