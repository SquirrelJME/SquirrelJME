// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.classformat.FieldDescriptionStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This is used to write fields to the output classes.
 *
 * @since 2016/09/10
 */
public interface JITFieldWriter
	extends AutoCloseable, FieldDescriptionStream
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/10
	 */
	@Override
	public abstract void close()
		throws JITException;
}

