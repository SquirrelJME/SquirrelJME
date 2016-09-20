// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.javac.base.CompilerOutput;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This implements the compiler output which writes to a ZIP file for any
 * classes that were compiled by the compiler.
 *
 * @since 2016/09/18
 */
class __CompilerOutput__
	implements CompilerOutput
{
	/** The output ZIP. */
	protected final ZipStreamWriter zip;
	
	/**
	 * Initializes the compiler output.
	 *
	 * @param __zip The output ZIP to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/18
	 */
	__CompilerOutput__(ZipStreamWriter __zip)
		throws NullPointerException
	{
		// Check
		if (__zip == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.zip = __zip;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public void flush()
		throws IOException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/18
	 */
	@Override
	public OutputStream output(String __n)
		throws IOException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

