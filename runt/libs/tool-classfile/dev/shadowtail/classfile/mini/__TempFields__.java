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

import dev.shadowtail.classfile.pool.DualClassRuntimePoolBuilder;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores temporary field data as it is processed.
 *
 * @since 2019/03/11
 */
final class __TempFields__
{
	/** The fields in the table. */
	final List<MinimizedField> _fields =
		new ArrayList<>();
	
	/** The number of fields in the table. */
	int _count;
	
	/** The current byte size of the field table. */
	int _bytes;
	
	/** The total number of objects in the table. */
	int _objects;
	
	/**
	 * Returns the byte representation of the data here.
	 *
	 * @param __pool The constant pool.
	 * @return The byte data representation.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/14
	 */
	public final byte[] getBytes(DualClassRuntimePoolBuilder __pool)
		throws NullPointerException
	{
		if (__pool == null)
			throw new NullPointerException("NARG");
		
		// Write
		try
		{
			// Actual table data
			ByteArrayOutputStream dbytes = new ByteArrayOutputStream();
			DataOutputStream ddos = new DataOutputStream(dbytes);
			
			// Write field information
			for (MinimizedField m : this._fields)
			{
				// 16-bytes
				ddos.writeInt(m.flags);
				ddos.writeShort(Minimizer.__checkUShort(m.offset));
				ddos.writeShort(Minimizer.__checkUShort(m.size));
				ddos.writeShort(Minimizer.__checkUShort(
					__pool.add(false, m.name.toString()).index));
				ddos.writeShort(Minimizer.__checkUShort(
					__pool.add(false, m.type.className()).index));
				ddos.writeShort(Minimizer.__checkUShort(
					__pool.add(false, m.value).index));
				ddos.writeByte(m.datatype.ordinal());
				ddos.writeByte(0);
			}
			
			// Write end of table
			ddos.writeInt(0xFFFFFFFF);
			
			// Finish
			return dbytes.toByteArray();
		}
		
		// Should not occur
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
}

