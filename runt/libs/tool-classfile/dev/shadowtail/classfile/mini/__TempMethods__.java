// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * Contains temporary method information.
 *
 * @since 2019/03/13
 */
final class __TempMethods__
{
	/** The name of this class. */
	protected final ClassName classname;
	
	/** The methods in this table. */
	final List<MinimizedMethod> _methods =
		new ArrayList<>();
	
	/** The number of methods that are available. */
	int _count;
	
	/**
	 * Initializes.
	 *
	 * @param __cn The class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/22
	 */
	__TempMethods__(ClassName __cn)
		throws NullPointerException
	{
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		this.classname = __cn;
	}
	
	/**
	 * Returns the byte representation of the data here.
	 *
	 * @param __pool The constant pool.
	 * @return The byte data representation.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	public final byte[] getBytes(MinimizedPoolBuilder __pool)
		throws NullPointerException
	{
		if (__pool == null)
			throw new NullPointerException("NARG");
		
		// Write
		MinimizedMethod lastm = null;
		try
		{
			List<MinimizedMethod> methods = this._methods;
			
			// Actual table data
			ByteArrayOutputStream dbytes = new ByteArrayOutputStream();
			DataOutputStream ddos = new DataOutputStream(dbytes);
			
			// Needed to filter in lines and code so that they are together
			int count = this._count;
			
			// Merge all the code data
			int[] offcode = new int[count],
				lencode = new int[count];
			ByteArrayOutputStream cbytes = new ByteArrayOutputStream();
			DataOutputStream cdos = new DataOutputStream(cbytes);
			for (int i = 0; i < count; i++)
			{
				MinimizedMethod m = methods.get(i);
				
				// Ignore if there is no code
				byte[] code = m._code;
				if (code == null)
					continue;
				
				// Offset to this code
				offcode[i] = cdos.size();
				lencode[i] = code.length;
				
				// Write all the data
				cdos.write(code);
				
				// Pad with the breakpoint operation
				while ((cdos.size() & 3) != 0)
					cdos.write(0xFF);
			}
			
			// Offset to code and line regions
			int codeoff = 4 + (count * MinimizedMethod.ENCODE_ENTRY_SIZE);
			
			// Write method information
			for (int i = 0; i < count; i++)
			{
				MinimizedMethod m = methods.get(i);
				lastm = m;
				
				// Flags, name, and type
				ddos.writeInt(m.flags);
				ddos.writeShort(Minimizer.__checkUShort(m.index));
				ddos.writeShort(Minimizer.__checkUShort(
					__pool.get(m.name.toString())));
				ddos.writeShort(Minimizer.__checkUShort(__pool.get(m.type)));
				
				// Code
				ddos.writeShort(Minimizer.__checkUShort(codeoff + offcode[i]));
				ddos.writeShort(Minimizer.__checkUShort(lencode[i]));
			}
			
			// Write end of table
			ddos.writeInt(0xFFFFFFFF);
			
			// Merge in the code and line information
			ddos.write(cbytes.toByteArray());
			return dbytes.toByteArray();
		}
		
		// {@squirreljme.error JC4g Could not process the method. (The method
		// this stopped at)}
		catch (InvalidClassFormatException|IOException e)
		{
			throw new InvalidClassFormatException("JC4g " + lastm, e);
		}
	}
}

