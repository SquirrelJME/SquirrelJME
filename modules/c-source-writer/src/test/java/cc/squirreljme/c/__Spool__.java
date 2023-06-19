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
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Token spool, used for testing.
 *
 * @since 2023/06/19
 */
final class __Spool__
	implements AutoCloseable
{
	/** The resultant list. */
	private final List<String> _list =
		new ArrayList<>();
	
	/**
	 * Initializes the spool.
	 * 
	 * @since 2023/06/19
	 */
	__Spool__()
	{
		this._output = ;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public void close()
	{
	}
	
	/**
	 * Returns the file for writing.
	 * 
	 * @return The file.
	 * @since 2023/06/19
	 */
	public CFile file()
	{
		return new CFile(new StringCollectionCTokenOutput(
			this._list, false));
	}
	
	/**
	 * Returns the written tokens.
	 * 
	 * @return The tokens written.
	 * @since 2023/06/19
	 */
	public String[] tokens()
	{
		List<String> list = this._list;
		return list.toArray(new String[list.size()]);
	}
}
