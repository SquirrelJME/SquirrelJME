// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;

/**
 * This is a UTF-8 string constant.
 *
 * @since 2016/03/13
 */
public final class CFUTF8
	extends CFConstantEntry
	implements CharSequence
{
	/** Internally read string. */
	protected final String string;
	
	/**
	 * Initializes the constant value.
	 *
	 * @param __icp The owning constant pool.
	 * @param __is Data input source.
	 * @throws CFFormatException If the modfied UTF string is
	 * malformed.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/13
	 */
	CFUTF8(CFConstantPool __icp, DataInputStream __dis)
		throws CFFormatException, IOException,
			NullPointerException
	{
		super(__icp);
		
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Read
		try
		{
			string = __dis.readUTF();
		}
		
		// Malformed sequence
		catch (UTFDataFormatException utfdfe)
		{
			// {@squirreljme.error CF0j The string which makes up a UTF-8
			// constant string is not a correctly formatted modified UTF-8
			// string.}
			throw new CFFormatException("CF0j", utfdfe);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/13
	 */
	@Override
	public char charAt(int __i)
	{
		return string.charAt(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/13
	 */
	@Override
	public int length()
	{
		return string.length();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/13
	 */
	@Override
	public CharSequence subSequence(int __s, int __e)
	{
		return string.subSequence(__s, __e);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/13
	 */
	@Override
	public String toString()
	{
		return string;
	}
}

