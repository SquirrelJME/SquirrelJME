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
 * Stores an {@link int} constant.
 *
 * @since 2016/04/19
 */
public class CFConstantInteger
	extends CFConstantValue<Integer>
	implements CFLDCLoadable.Narrow
{
	/** The value of this constant. */
	protected final Integer value;
	
	/**
	 * Decodes the value.
	 *
	 * @param __icp The owning pool.
	 * @param __dis The input data stream.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/19
	 */
	CFConstantInteger(CFConstantPool __icp,
		DataInputStream __dis)
		throws IOException, NullPointerException
	{
		super(__icp, Integer.class);
		
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Read
		value = Integer.valueOf(__dis.readInt());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/19
	 */
	@Override
	public Integer getValue()
	{
		return value;
	}
}

