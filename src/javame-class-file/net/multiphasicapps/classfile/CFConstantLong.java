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
 * Stores a {@code long} constant.
 *
 * @since 2016/04/19
 */
public class CFConstantLong
	extends CFConstantValue<Long>
	implements CFLDCLoadable.Wide
{
	/** The value of this constant. */
	protected final Long value;
	
	/**
	 * Decodes the value.
	 *
	 * @param __icp The owning pool.
	 * @param __dis The input data stream.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/19
	 */
	CFConstantLong(CFConstantPool __icp,
		DataInputStream __dis)
		throws IOException, NullPointerException
	{
		super(__icp, Long.class);
		
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Read
		value = Long.valueOf(__dis.readLong());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/19
	 */
	@Override
	public Long getValue()
	{
		return value;
	}
}

