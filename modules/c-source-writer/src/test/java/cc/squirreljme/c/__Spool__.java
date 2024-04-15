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
import java.util.List;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Token spool, used for testing, this makes it easier to collect all the
 * tokens and shove them into {@link TestRunnable#secondary(String, Object)}.
 *
 * @since 2023/06/19
 */
final class __Spool__
	extends CFileProxy
	implements AutoCloseable
{
	/** The CFile we are writing to. */
	private final CFile _cFile;
	
	/** The resultant list. */
	private final Collection<String> _list;
	
	/**
	 * Initializes the spool.
	 * 
	 * @param __result The resultant list where tokens go.
	 * @param __cFile The file we are wrting to.
	 * @since 2023/08/08
	 */
	public __Spool__(List<String> __result, CFile __cFile)
	{
		super(__cFile);
		
		this._list = __result;
		this._cFile = __cFile;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public void close()
		throws IOException
	{
		this._cFile.close();
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
	
	/**
	 * Initializes the spool.
	 * 
	 * @param __whitespace Is whitespace important for this?
	 * @since 2023/08/08
	 */
	static __Spool__ __init(boolean __whitespace)
	{
		// Setup output
		ArrayList<String> result = new ArrayList<>();
		StringCollectionCTokenOutput output =
			new StringCollectionCTokenOutput(result, __whitespace);
		
		// Setup CFile
		CFile cFile = new CFile(output);
		
		// Pass everything to the spool
		return new __Spool__(result, cFile);
	}
}
