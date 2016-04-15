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
import java.util.Objects;

/**
 * This represents the base of the constant value pool.
 *
 * @param <C> The type of constant value to return.
 * @since 2016/03/15
 */
public abstract class CFConstantValue<C>
	extends CFConstantEntry
{
	/** The type of value to store. */
	protected final Class<C> castas;
	
	/**
	 * Initializes the base constant information.
	 *
	 * @param __icp The owning constant pool.
	 * @param __cl The class to cast to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/15
	 */
	CFConstantValue(CFConstantPool __icp, Class<C> __cl)
	{
		super(__icp);
		
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Set
		castas = __cl;
	}
	
	/**
	 * Returns the value of the constant.
	 *
	 * @return The constant value.
	 * @since 2016/03/15
	 */
	public abstract C getValue();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public final String toString()
	{
		return Objects.toString(getValue());
	}
}

