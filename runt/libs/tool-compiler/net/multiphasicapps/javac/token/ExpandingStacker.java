// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

import java.io.IOException;

/**
 * This is an expanded token source which can be given an input queue of
 * tokens which have already been read.
 *
 * @since 2018/03/22
 */
public final class ExpandingStacker
	extends ExpandingSource
{
	/**
	 * {@inheritDoc}
	 * @since 2018/03/22
	 */
	@Override
	public final void close()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/22
	 */
	@Override
	protected final ExpandedToken readNext()
		throws IOException
	{
		throw new todo.TODO();
	}
}

