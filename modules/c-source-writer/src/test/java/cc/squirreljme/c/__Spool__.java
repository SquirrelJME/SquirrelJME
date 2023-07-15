// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.c.out.StringCollectionCTokenOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Token spool, used for testing, this makes it easier to collect all the
 * tokens and shove them into {@link TestRunnable#secondary(String, Object)}.
 *
 * @since 2023/06/19
 */
final class __Spool__
	extends __CFileProxy__
	implements AutoCloseable
{
	/** The resultant list. */
	private final Collection<String> _list;
	
	/**
	 * Initializes the spool.
	 * 
	 * @param __whitespace Is whitespace important for this?
	 * @since 2023/06/22
	 */
	__Spool__(boolean __whitespace)
	{
		super(new CFile(new StringCollectionCTokenOutput(
			new ArrayList<String>(), __whitespace)));
		
		this._list = ((StringCollectionCTokenOutput)super.__file().out)
			.output();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public void close()
		throws IOException
	{
		this.__file().close();
	}
	
	/**
	 * Returns the written tokens.
	 * 
	 * @return The tokens written.
	 * @since 2023/06/19
	 */
	public String[] tokens()
	{
		Collection<String> list = this._list;
		return list.toArray(new String[list.size()]);
	}
}
