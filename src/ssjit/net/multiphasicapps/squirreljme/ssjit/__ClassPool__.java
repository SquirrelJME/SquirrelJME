// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * This represents the constant pool of a class which has been read, this is
 * only temporarily used during the translation of the class.
 *
 * @since 2016/06/29
 */
class __ClassPool__
{
	/** The UTF constant tag. */
	public static final int TAG_UTF8 =
		1;
	
	/** Integer constant. */
	public static final int TAG_INTEGER =
		3;
	
	/** Float constant. */
	public static final int TAG_FLOAT =
		4;
	
	/** Long constant. */
	public static final int TAG_LONG =
		5;
	
	/** Double constant. */
	public static final int TAG_DOUBLE =
		6;
	
	/** Reference to another class. */
	public static final int TAG_CLASS =
		7;
	
	/** String constant. */
	public static final int TAG_STRING =
		8;
	
	/** Field reference. */
	public static final int TAG_FIELDREF =
		9;
	
	/** Method reference. */
	public static final int TAG_METHODREF =
		10;
	
	/** Interface method reference. */
	public static final int TAG_INTERFACEMETHODREF =
		11;
	
	/** Name and type. */
	public static final int TAG_NAMEANDTYPE =
		12;
	
	/** Method handle (illegal). */
	public static final int TAG_METHODHANDLE =
		15;
	
	/** Method type (illegal). */
	public static final int TAG_METHODTYPE =
		16;
	
	/** Invoke dynamic call site (illegal). */
	public static final int TAG_INVOKEDYNAMIC =
		18;
	
	/**
	 * Decodes the constant pool of an input class file.
	 *
	 * @param __dis The input stream to read for class files.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @throws SSJITException If the constant pool is malformed.
	 * @since 2016/06/29
	 */
	__ClassPool__(DataInputStream __dis)
		throws IOException, NullPointerException, SSJITException
	{
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error DV05 The input class has an empty constant
		// pool.}
		int count = __dis.readUnsignedShort();
		if (count <= 0)
			throw new SSJITException("DV05");
		
		// Decode all entry data
		for (int i = 0; i < count; i++)
		{
			// Read the tag
			int tag = __dis.readUnsignedByte();
			
			// {@squirreljme.error DV06 Java ME does not support dynamic
			// invocation (such as method handles or lambda expressions).}
			if (tag == TAG_METHODHANDLE || tag == TAG_METHODTYPE ||
				tag == TAG_INVOKEDYNAMIC)
				throw new SSJITException("DV06");
			
			throw new Error("TODO");
		}
		
		throw new Error("TODO");
	}
}

