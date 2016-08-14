// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import java.io.IOException;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.JITConstantPool;

/**
 * This writes the constant pool.
 *
 * @since 2016/08/14
 */
class __PoolWriter__
{
	/** The pool to write. */
	protected final JITConstantPool pool;
	
	/**
	 * Initializes the pool writer.
	 *
	 * @param __pool The constant pool to use.
	 * @since 2016/08/14
	 */
	__PoolWriter__(JITConstantPool __pool)
		throws NullPointerException
	{
		// Check
		if (__pool == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.pool = __pool;
	}
	
	/**
	 * Writes the constant pool.
	 *
	 * @param __dos The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/14
	 */
	void __write(ExtendedDataOutputStream __dos)
		throws IOException, NullPointerException
	{
		// Check
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

