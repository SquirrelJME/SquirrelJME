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
 * Stores a {@code float} constant.
 *
 * @since 2016/04/19
 */
public class CFConstantFloat
	extends CFConstantValue<Float>
	implements CFLDCLoadable.Narrow
{
	/** The value of this constant. */
	protected final Float value;
	
	/**
	 * Decodes the value.
	 *
	 * @param __icp The owning pool.
	 * @param __dis The input data stream.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/19
	 */
	CFConstantFloat(CFConstantPool __icp,
		DataInputStream __dis)
		throws IOException, NullPointerException
	{
		super(__icp, Float.class);
		
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Read
		value = Float.valueOf(__dis.readFloat());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/19
	 */
	@Override
	public Float getValue()
	{
		return value;
	}
}

