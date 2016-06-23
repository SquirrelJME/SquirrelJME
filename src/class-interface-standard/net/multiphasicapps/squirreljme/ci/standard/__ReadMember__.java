// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ci.standard;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MemberTypeSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.io.region.BufferAreaInputStream;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIConstantValue;
import net.multiphasicapps.squirreljme.ci.CIException;
import net.multiphasicapps.squirreljme.ci.CIField;
import net.multiphasicapps.squirreljme.ci.CIFieldFlag;
import net.multiphasicapps.squirreljme.ci.CIFieldFlags;
import net.multiphasicapps.squirreljme.ci.CIFieldID;
import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.ci.CIMethodFlag;
import net.multiphasicapps.squirreljme.ci.CIMethodFlags;
import net.multiphasicapps.squirreljme.ci.CIMethodID;
import net.multiphasicapps.squirreljme.ci.CIUTF;

/**
 * This reads class members.
 *
 * @since 2016/04/26
 */
class __ReadMember__
{
	/**
	 * Not initialized.
	 *
	 * @since 2016/04/26
	 */
	private __ReadMember__()
	{
		throw new RuntimeException("WTFX");
	}
	
	/**
	 * Reads a field from the given class.
	 *
	 * @param __oc The outer class.
	 * @param __das The input data source.
	 * @param __into The target mappings.
	 * @throws IOException On read errors.
	 * @sicne 2016/04/26
	 */
	static void __field(CISClass __oc, DataInputStream __das,
		Map<CIFieldID, CIField> __into)
		throws IOException
	{
		// Read flags
		int flags = __das.readUnsignedShort();
		
		// Read name and type
		IdentifierSymbol name = __readIdentifier(__oc, __das);
		FieldSymbol type = __readType(__oc, __das).asField();
		
		// Setup ID
		CIFieldID id = new CIFieldID(name, type);
		
		// Read in attributes
		Object constval = null;
		int na = __das.readUnsignedShort();
		for (int i = 0; i < na; i++)
		{
			// Read kind
			String kind = __oc.constantPool().<CIUTF>requiredAs(
				__das.readUnsignedShort(), CIUTF.class).toString();
			int len = __das.readInt();
			
			// {@squirreljme.error AQ1q Field attribute has negative length.
			// (The field id)}
			if (len < 0)
				throw new CIException(String.format("AQ1q", id));
			
			// Setup area
			try (BufferAreaInputStream bais = new BufferAreaInputStream(__das,
				len))
			{
				// Only constants are valid
				if (!kind.equals("ConstantValue"))
					continue;
				
				try (DataInputStream dais = new DataInputStream(bais))
				{
					// {@squirreljme.error AQ1r Field already has a constant
					// value. (The field ID)}
					if (constval != null)
						throw new CIException(String.format("AQ1r", id));
					
					// Reads the constant field value
					constval = __oc.constantPool().<CIConstantValue>
						requiredAs(dais.readUnsignedShort(),
						CIConstantValue.class).get();
				}
			}
		}
		
		// {@squirreljme.error AQ1s Duplicate field in class. (The field ID)}
		CIField old = __into.put(id, new CISField(__oc,
			id, __FlagDecoder__.__field(__oc, flags), constval));
		if (old != null)
			throw new CIException(String.format("AQ1s", id));
	}
	
	/**
	 * Reads a method from the given class.
	 *
	 * @param __oc The outer class.
	 * @param __das The input data source.
	 * @param __into The target mappings.
	 * @throws IOException On read errors.
	 * @sicne 2016/04/26
	 */
	static void __method(CISClass __oc, DataInputStream __das,
		Map<CIMethodID, CIMethod> __into)
		throws IOException
	{
		// Read flags
		int flags = __das.readUnsignedShort();
		
		// Read name and type
		IdentifierSymbol name = __readIdentifier(__oc, __das);
		MethodSymbol type = __readType(__oc, __das).asMethod();
		
		// Setup ID
		CIMethodID id = new CIMethodID(name, type);
		
		// Read in attributes
		byte[] code = null;
		int na = __das.readUnsignedShort();
		for (int i = 0; i < na; i++)
		{
			// Read kind
			String kind = __oc.constantPool().<CIUTF>requiredAs(
				__das.readUnsignedShort(), CIUTF.class).toString();
			int len = __das.readInt();
			
			// {@squirreljme.error AQ1w Method attribute has negative length.
			// (The method id)}
			if (len < 0)
				throw new CIException(String.format("AQ1w", id));
			
			// Setup area
			try (BufferAreaInputStream bais = new BufferAreaInputStream(__das,
				len))
			{
				// Only code is valid
				if (!kind.equals("Code"))
					continue;
				
				try (DataInputStream dais = new DataInputStream(bais))
				{
					// {@squirreljme.error AQ1y Method already has a code
					// attribute. (The method ID)}
					if (code != null)
						throw new CIException(String.format("AQ1y", id));
					
					// {@squirreljme.error AQ1z Did not read the entire code
					// attribute for a method. (The method ID)}
					code = new byte[len];
					if (len != dais.read(code))
						throw new CIException(String.format("AQ1z", id));
				}
			}
		}
		
		// {@squirreljme.error AQ1u Duplicate field in class. (The field ID)}
		CIMethod old = __into.put(id, new CISMethod(__oc,
			id, __FlagDecoder__.__method(__oc, flags), code));
		if (old != null)
			throw new CIException(String.format("AQ1u", id));
	}
	
	/**
	 * Reads the identifier of a member from the input stream.
	 *
	 * @param __oc The outer class.
	 * @param __das The input data source.
	 * @return The read identifier.
	 * @throws IOException On read errors.
	 * @since 2016/04/26
	 */
	private static IdentifierSymbol __readIdentifier(CISClass __oc,
		DataInputStream __das)
		throws IOException
	{
		return __oc.constantPool().<CIUTF>requiredAs(
			__das.readUnsignedShort(), CIUTF.class).asIdentifier();
	}
	
	/**
	 * Reads the type of a member from the input stream.
	 *
	 * @param __oc The outer class.
	 * @param __das The input data source.
	 * @return The read type.
	 * @throws IOException On read errors.
	 * @since 2016/04/26
	 */
	private static MemberTypeSymbol __readType(CISClass __oc,
		DataInputStream __das)
		throws IOException
	{
		return __oc.constantPool().<CIUTF>requiredAs(
			__das.readUnsignedShort(), CIUTF.class).asMember();
	}
	
	/**
	 * Skips the given amount of bytes.
	 *
	 * @param __das The input data source.
	 * @param __len The number of bytes to skip.
	 * @throws IOException On read errors.
	 * @since 2016/04/26
	 */
	static void __skipBytes(DataInputStream __das, int __len)
		throws IOException
	{
		for (int total = 0; total != __len;)
		{
			// Skip bytes
			int act = (int)__das.skip(__len - total);
			
			// Calculate new total
			int nt = total + act;
			
			// {@squirreljme.error AQ1p End of file reached while skipping
			// the class attributes.}
			if (total >= nt)
				throw new CIException("AQ1p");
			
			// Set new total
			total = nt;
		}
	}
}

