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
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

/**
 * This represents a every exception that exists within a method.
 *
 * @since 2017/02/09
 */
public final class ExceptionHandlerTable
{
	/** The exception handler table. */
	private final ExceptionHandler[] _table;
	
	/** String representation. */
	private Reference<String> _string;
	
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
	 * Return all of the exception handlers which apply to the given address.
	 *
	 * @param __pc The address to use.
	 * @return An array containing the exceptions which handle for the given
	 * address.
	 * @since 2018/10/13
	 */
	public final ExceptionHandler[] at(int __pc)
	{
		List<ExceptionHandler> rv = new ArrayList<>();
		
		// Add any handlers which are in range
		for (ExceptionHandler e : this._table)
			if (e.inRange(__pc))
				rv.add(e);
		
		return rv.<ExceptionHandler>toArray(new ExceptionHandler[rv.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/21
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof ExceptionHandlerTable))
			return false;
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the exception handler at the given index.
	 *
	 * @return The exception handler at the given index.
	 * @since 2017/02/09
	 */
	public final ExceptionHandler get(int __i)
	{
		return this._table[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/21
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the number of exception handlers that exist.
	 *
	 * @return The total number of exception handlers.
	 * @since 2017/02/09
	 */
	public final int size()
	{
		return this._table.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = Arrays.asList(this._table).toString()));
		
		return rv;
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
			ClassName type = __pool.<ClassName>get(ClassName.class,
				__is.readUnsignedShort());
			
			// {@squirreljme.error JC0v Address is outside of the bounds of the
			// method. (The start address; The end address; The handler
			// address; The code length)}
			if (spc >= __len || epc > __len || hpc >= __len)
				throw new InvalidClassFormatException(String.format(
					"JC0v %d %d %d %d", spc, epc, hpc, __len));
			
			// Setup exception
			table[i] = new ExceptionHandler(spc, epc, hpc, type);
		}
		
		// Setup
		return new ExceptionHandlerTable(table);
	}
}

