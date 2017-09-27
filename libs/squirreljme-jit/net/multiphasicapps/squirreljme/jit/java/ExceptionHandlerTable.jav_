// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.AbstractList;
import java.util.RandomAccess;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents a every exception that exists within a method.
 *
 * @since 2017/02/09
 */
@Deprecated
public final class ExceptionHandlerTable
	extends AbstractList<ExceptionHandler>
	implements RandomAccess
{
	/** The exception handler table. */
	private final ExceptionHandler[] _table;
	
	/**
	 * Parses the exception handler table.
	 *
	 * @param __in The input stream to read exceptions from.
	 * @param __pool The constant pool for class names.
	 * @param __len The length of the method in byte codes.
	 * @throws IOException On read errors.
	 * @throws JITException If the exception table is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/09
	 */
	ExceptionHandlerTable(DataInputStream __is, Pool __pool, int __len)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__is == null || __pool == null)
			throw new NullPointerException("NARG");
		
		// Read exception table count
		int n = __is.readUnsignedShort();
		ExceptionHandler[] table = new ExceptionHandler[n];
		for (int i = 0; i < n; i++)
		{
			// Read values
			int spc = __is.readUnsignedShort();
			int epc = __is.readUnsignedShort();
			int hpc = __is.readUnsignedShort();
			ClassName type = __pool.<ClassName>require(ClassName.class,
				__is.readUnsignedShort());
			
			// {@squirreljme.error JI1e Address is outside of the bounds of the
			// method. (The start address; The end address; The handler
			// address; The code length)}
			if (spc >= __len || epc > __len || hpc >= __len)
				throw new JITException(String.format(
					"JI1e %d %d %d %d", spc, epc, hpc, __len));
			
			// Setup exception
			table[i] = new ExceptionHandler(spc, epc, hpc, type);
		}
		this._table = table;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/09
	 */
	@Override
	public ExceptionHandler get(int __i)
	{
		return this._table[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/09
	 */
	@Override
	public int size()
	{
		return this._table.length;
	}
}

