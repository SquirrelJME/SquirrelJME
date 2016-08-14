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
import java.util.HashMap;
import java.util.Map;
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
	
	/** The mapping of strings. */
	protected final Map<String, __String__> strings =
		new HashMap<>();
	
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
		
		// Go through all pool entries and extract strings from them to use
		int n = __pool.size();
		for (int i = 0; i < n; i++)
		{
			// Get tag here
			int tag = __pool.tag(i);
			
			// Depends on the tag
			switch (tag)
			{
					// No string data
				case JITConstantPool.TAG_INTEGER:
				case JITConstantPool.TAG_FLOAT:
				case JITConstantPool.TAG_LONG:
				case JITConstantPool.TAG_DOUBLE:
					continue;
				
					// Unknown
				default:
					throw new RuntimeException("OOPS");
			}
		}
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
	
	/**
	 * String storage holder.
	 *
	 * @since 2016/08/14
	 */
	private class __String__
	{
		/** The string to use. */
		final String _string;
		
		/** The index of the string in the table. */
		final int _index;
		
		/** The position from the start where the string data is. */
		volatile int _position;
		
		/**
		 * Initializes the string 
		 *
		 * @param __s The string which was used.
		 * @param __dx The index of the string.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/08/14
		 */
		private __String__(String __s, int __dx)
			throws NullPointerException
		{
			// Check
			if (__s == null)
				throw new NullPointerException("NARG");
			
			// Set
			this._string = __s;
			this._index = __dx;
		}
	}
}

