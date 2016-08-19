// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This interface is used by the class writer to write output logic to the
 * resulting native machine code generator.
 *
 * @since 2016/08/19
 */
public interface JITMethodWriter
	extends AutoCloseable
{
	/**
	 * {@inheritDoc}
	 * @since 2016/08/19
	 */
	@Override
	public abstract void close()
		throws JITException;
}

