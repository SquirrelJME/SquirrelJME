// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.cff;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This class represents the byte code within a method.
 *
 * @since 2017/10/09
 */
public final class ByteCode
{
	/** The code is always at this offset. */
	private static final int _CODE_OFFSET =
		8;
	
	/** The maximum number of bytes the byte code may be. */
	private static final int _MAX_CODE_LENGTH =
		65535;
	
	/** The maximum number of stack entries. */
	protected final int maxstack;
	
	/** The maximum number of local entries. */
	protected final int maxlocals;
	
	/** The length of the method code in bytes. */
	protected final int codelen;
	
	/** The exceptions within this method. */
	protected final ExceptionHandlerTable exceptions;
	
	/** The stack map table for the method code. */
	protected final StackMapTable stackmaptable;
	
	/** The input attribute code, used for instruction lookup. */
	private final byte[] _rawattributedata;
	
	/** The owning method reference. */
	private final Reference<Method> _methodref;
	
	/**
	 * Initializes the byte code.
	 *
	 * @param __mr The owning method reference.
	 * @param __ca The raw code attribute data.
	 * @throws InvalidClassFormatException If the byte code is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/09
	 */
	ByteCode(Reference<Method> __mr, byte[] __ca)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__mr == null || __ca == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._methodref = __mr;
		this._rawattributedata = __ca;
		
		// If any IOExceptions are generated then the attribute is not valid
		Method method = __mr.get();
		Pool pool = method.pool();
		try (DataInputStream in = new DataInputStream(
			new ByteArrayInputStream(__ca)))
		{
			// The number of variables allocated to the method
			int maxstack = in.readUnsignedShort(),
				maxlocals = in.readUnsignedShort();
				
			// {@squirreljme.error JI1d The specified code length is not valid.
			// (The code length)}
			int codelen = in.readInt();
			if (codelen <= 0 || codelen > _MAX_CODE_LENGTH)
				throw new InvalidClassFormatException(
					String.format("JI1d %d", codelen));
		
			// Ignore that many bytes
			for (int i = 0; i < codelen; i++)
				in.readByte();
			
			// Read exception handler table
			ExceptionHandlerTable eht = ExceptionHandlerTable.decode(in, pool,
				codelen);
			
			// The stack map table is used for verification
			StackMapTable smt = null;
			
			// Handle attributes
			int na = in.readUnsignedShort();
			String[] attr = new String[1];
			int[] alen = new int[1];
			for (int j = 0; j < na; j++)
				try (DataInputStream ai = ClassFile.__nextAttribute(in, pool,
					attr, alen))
				{
					String a;
					boolean newtable = false;
					switch ((a = attr[0]))
					{
							// The stack map table, either new or old
						case "StackMapTable":
							newtable = true;
						case "StackMap":
							// {@squirreljme.error JI3g Duplicate stack map
							// tables exist within the method byte code.}
							if (smt != null)
								throw new InvalidClassFormatException("JI3g");
							
							// Decode
							smt = StackMapTable.decode(pool, method, newtable,
								ai);
							break;
						
							// Unknown, ignore
						default:
							continue;
					}
				}
			
			// If there is no stack map, then use a default one (which has
			// just no entries)
			if (smt == null)
				smt = StackMapTable.decode(pool, method, true,
					new DataInputStream(
					new ByteArrayInputStream(new byte[2])));
			
			// Can set fields now
			this.maxstack = maxstack;
			this.maxlocals = maxlocals;
			this.codelen = codelen;
			this.exceptions = eht;
			this.stackmaptable = smt;
		}
		
		// {@squirreljme.error JI3f Failed to read from the code attribute.}
		catch (IOException e)
		{
			throw new InvalidClassFormatException("JI3f", e);
		}
	}
}

