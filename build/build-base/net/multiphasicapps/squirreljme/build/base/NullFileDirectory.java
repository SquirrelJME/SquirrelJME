// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.base;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import net.multiphasicapps.util.empty.EmptyIterator;

/**
 * This is a file directory which contains no files.
 *
 * @since 2016/12/26
 */
public class NullFileDirectory
	implements FileDirectory
{
	/**
	 * {@inheritDoc}
	 * @since 2016/12/26
	 */
	@Override
	public void close()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/26
	 */
	@Override
	public boolean contains(String __fn)
		throws NullPointerException
	{
		// Check
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		// Never contains one
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/26
	 */
	@Override
	public Iterator<String> iterator()
	{
		return EmptyIterator.<String>empty();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/26
	 */
	@Override
	public InputStream open(String __fn)
		throws IOException, NullPointerException
	{
		// Check
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AU02 The null file directory contains no
		// files. (The requested file)}
		throw new IOException(String.format("AU02 %s", __fn));
	}
}

