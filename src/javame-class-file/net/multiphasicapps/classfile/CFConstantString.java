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

/**
 * This represents a constant string value.
 *
 * @since 2016/03/15
 */
public final class CFConstantString
	extends CFConstantValue<String>
	implements CFLDCLoadable.Narrow, CharSequence
{
	/** The indexed UTF-8 constant. */
	protected final int index;
	
	/**
	 * Initializes the string constant.
	 *
	 * @param __icp The owning constant pool.
	 * @param __dis Data source.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/15
	 */
	CFConstantString(CFConstantPool __icp,
		DataInputStream __dis)
		throws IOException, NullPointerException
	{
		super(__icp, String.class);
		
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Read the string index
		index = __dis.readUnsignedShort();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public char charAt(int __i)
	{
		return toString().charAt(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public String getValue()
	{
		return pool.<CFUTF8>getAs(index, CFUTF8.class).toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public int length()
	{
		return toString().length();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public CharSequence subSequence(int __s, int __e)
	{
		return toString().subSequence(__s, __e);
	}
}

