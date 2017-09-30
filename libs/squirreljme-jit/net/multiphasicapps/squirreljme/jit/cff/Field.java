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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * This represents a field in a class which is used to store values either as
 * static instances or as instance variables outside of methods.
 *
 * @since 2017/09/30
 */
public final class Field
	extends Member
{
	/**
	 * Decodes all fields from the input class data.
	 *
	 * @param __ver The version of the class.
	 * @param __tn The name of the owning class.
	 * @param __cf The flags for the owning class.
	 * @param __pool The constant pool for the class.
	 * @throws InvalidClassFormatException If the class is not formatted
	 * correctly.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/30
	 */
	public static Field[] decode(ClassVersion __ver, ClassName __tn,
		ClassFlags __cf, Pool __pool, DataInputStream __in)
		throws InvalidClassFormatException, IOException, NullPointerException
	{
		if (__ver == null || __tn == null || __cf == null || __pool == null ||
			__in == null)
			throw new NullPointerException("NARG");
		
		int nf = __in.readUnsignedShort();
		Field[] rv = new Field[nf];
		Set<NameAndType> dup = new HashSet<>();
		
		// Parse fields
		for (int i = 0; i < nf; i++)
		{
			FieldFlags flags = new FieldFlags(__cf, __in.readUnsignedShort());
			FieldName name = new FieldName(
				__pool.<UTFConstantEntry>require(UTFConstantEntry.class,
				__in.readUnsignedShort()).toString());
			FieldDescriptor type = new FieldDescriptor(
				__pool.<UTFConstantEntry>require(UTFConstantEntry.class,
				__in.readUnsignedShort()).toString());
			
			// {@squirreljme.error JI2w A duplicate method exists within the
			// class. (The method name; The method descriptor)}
			if (!dup.add(new NameAndType(name.toString(), type.toString())))
				throw new InvalidClassFormatException(String.format(
					"JI2w %s %s", name, type));
			
			throw new todo.TODO();
		}
		
		// All done!
		return rv;
	}
}

