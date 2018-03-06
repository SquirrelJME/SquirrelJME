// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * This represents a every exception that exists within a method.
 *
 * @since 2017/02/09
 */
public final class ExceptionHandlerTable
	extends AbstractList<ExceptionHandler>
	implements RandomAccess
{
	/** The exception handler table. */
	private final ExceptionHandler[] _table;
	
	/**
	 * Initializes the exception handler table.
	 *
	 * @param __t The entries to handle.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/09
	 */
	ExceptionHandlerTable(ExceptionHandler... __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Clone entries
		__t = __t.clone();
		for (ExceptionHandler e : __t)
			if (e == null)
				throw new NullPointerException("NARG");
		
		// Set
		this._table = __t;
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
	
	/**
	 * Parses the exception handler table.
	 *
	 * @param __in The input stream to read exceptions from.
	 * @param __pool The constant pool for class names.
	 * @param __len The length of the method in byte codes.
	 * @throws InvalidClassFormatException If the exception table is not valid.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/09
	 */
	public static ExceptionHandlerTable decode(DataInputStream __is,
		Pool __pool, int __len)
		throws InvalidClassFormatException, IOException, NullPointerException
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
			
			// {@squirreljme.error JC0o Address is outside of the bounds of the
			// method. (The start address; The end address; The handler
			// address; The code length)}
			if (spc >= __len || epc > __len || hpc >= __len)
				throw new InvalidClassFormatException(String.format(
					"JC0o %d %d %d %d", spc, epc, hpc, __len));
			
			// Setup exception
			table[i] = new ExceptionHandler(spc, epc, hpc, type);
		}
		
		// Setup
		return new ExceptionHandlerTable(table);
	}
}

