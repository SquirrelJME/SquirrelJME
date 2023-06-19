// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.out;

import java.io.IOException;
import java.util.Collection;

/**
 * Outputs to a {@link Collection} of {@link String}.
 *
 * @since 2023/06/19
 */
public class StringCollectionCTokenOutput
	implements CTokenOutput
{
	/**
	 * Initializes the output to the string collection.
	 * 
	 * @param __out The output.
	 * @param __whitespace Should whitespace be considered?
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	public StringCollectionCTokenOutput(Collection<String> __out,
		boolean __whitespace)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public void close()
		throws IOException
	{
		// Collections do not get closed
	}
}
