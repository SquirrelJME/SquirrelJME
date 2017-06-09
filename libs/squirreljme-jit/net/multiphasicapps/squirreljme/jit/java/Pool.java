// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This class decodes the constant pool and provides generic access to the
 * contents of it.
 *
 * @since 2017/06/08
 */
public class Pool
{
	/** The UTF constant tag. */
	private static final int _TAG_UTF8 =
		1;
	
	/** Integer constant. */
	private static final int _TAG_INTEGER =
		3;
	
	/** Float constant. */
	private static final int _TAG_FLOAT =
		4;
	
	/** Long constant. */
	private static final int _TAG_LONG =
		5;
	
	/** Double constant. */
	private static final int _TAG_DOUBLE =
		6;
	
	/** Reference to another class. */
	private static final int _TAG_CLASS =
		7;
	
	/** String constant. */
	private static final int _TAG_STRING =
		8;
	
	/** Field reference. */
	private static final int _TAG_FIELDREF =
		9;
	
	/** Method reference. */
	private static final int _TAG_METHODREF =
		10;
	
	/** Interface method reference. */
	private static final int _TAG_INTERFACEMETHODREF =
		11;
	
	/** Name and type. */
	private static final int _TAG_NAMEANDTYPE =
		12;
	
	/** Method handle (illegal). */
	private static final int _TAG_METHODHANDLE =
		15;
	
	/** Method type (illegal). */
	private static final int _TAG_METHODTYPE =
		16;
	
	/** Invoke dynamic call site (illegal). */
	private static final int _TAG_INVOKEDYNAMIC =
		18;
	
	/**
	 * Parses and initializes the constant pool structures.
	 *
	 * @param __in The input class containing the constant pool to be read.
	 * @throws IOException On read errors.
	 * @throws JITException If the constant pool is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/08
	 */
	public Pool(DataInputStream __in)
		throws IOException, JITException, NullPointerException
	{
		// Check
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Entries in the pool
		int count = __in.readUnsignedShort();
		
		throw new todo.TODO();
	}
	
	/**
	 * Obtains the entry at the specified index.
	 *
	 * @param <C> The type of class to get.
	 * @param __cl The type of class to get.
	 * @param __i The index of the entry to get.
	 * @return The entry at the specified position as the given class.
	 * @throws JITException If the class type does not match or the pool index
	 * is out of range.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/08
	 */
	public <C> C get(Class<C> __cl, int __i)
		throws JITException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

