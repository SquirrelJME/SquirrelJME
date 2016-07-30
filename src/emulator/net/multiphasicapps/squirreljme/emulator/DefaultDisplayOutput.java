// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This implements the default display output which just prints output
 * bytes directly to an associated output stream.
 *
 * @since 2016/07/30
 */
public class DefaultDisplayOutput
	implements DisplayOutput
{
	/** The stream to write to. */
	protected final OutputStream output;
	
	/**
	 * Initializes a display output which sends characters to standard
	 * output.
	 *
	 * @since 2016/07/30
	 */
	public DefaultDisplayOutput()
	{
		this(System.out);
	}
	
	/**
	 * Initializes a display output which outputs to the specified stream.
	 *
	 * @param __os The stream to write bytes to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public DefaultDisplayOutput(OutputStream __os)
		throws NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = __os;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public void stdOut(byte[] __b, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		int n = __b.length;
		if (__o < 0 || __l < 0 || (__o + __l) > n)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Write to the output
		this.output.write(__b, __o, __l);
	}
}

