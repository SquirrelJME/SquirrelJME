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
import java.io.UTFDataFormatException;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.IllegalSymbolException;
import net.multiphasicapps.descriptors.MemberTypeSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.squirreljme.ci.CIClassReference;
import net.multiphasicapps.squirreljme.ci.CIConstantDouble;
import net.multiphasicapps.squirreljme.ci.CIConstantFloat;
import net.multiphasicapps.squirreljme.ci.CIConstantInteger;
import net.multiphasicapps.squirreljme.ci.CIConstantLong;
import net.multiphasicapps.squirreljme.ci.CIConstantString;
import net.multiphasicapps.squirreljme.ci.CIException;
import net.multiphasicapps.squirreljme.ci.CIFieldReference;
import net.multiphasicapps.squirreljme.ci.CIMemberNameAndType;
import net.multiphasicapps.squirreljme.ci.CIMethodReference;
import net.multiphasicapps.squirreljme.ci.CIPool;
import net.multiphasicapps.squirreljme.ci.CIPoolEntry;
import net.multiphasicapps.squirreljme.ci.CIUTF;

/**
 * This decodes the constant pool of a class.
 *
 * @since 2016/04/24
 */
class __PoolDecoder__
{
	/** The UTF constant tag. */
	protected static final int TAG_UTF8 =
		1;
	
	/** Integer constant. */
	protected static final int TAG_INTEGER =
		3;
	
	/** Float constant. */
	protected static final int TAG_FLOAT =
		4;
	
	/** Long constant. */
	protected static final int TAG_LONG =
		5;
	
	/** Double constant. */
	protected static final int TAG_DOUBLE =
		6;
	
	/** Reference to another class. */
	protected static final int TAG_CLASS =
		7;
	
	/** String constant. */
	protected static final int TAG_STRING =
		8;
	
	/** Field reference. */
	protected static final int TAG_FIELDREF =
		9;
	
	/** Method reference. */
	protected static final int TAG_METHODREF =
		10;
	
	/** Interface method reference. */
	protected static final int TAG_INTERFACEMETHODREF =
		11;
	
	/** Name and type. */
	protected static final int TAG_NAMEANDTYPE =
		12;
	
	/** Method handle (illegal). */
	protected static final int TAG_METHODHANDLE =
		15;
	
	/** Method type (illegal). */
	protected static final int TAG_METHODTYPE =
		16;
	
	/** Invoke dynamic call site (illegal). */
	protected static final int TAG_INVOKEDYNAMIC =
		18;
	
	/** The outer class. */
	protected final CISClass outerclass;
	
	/** The input data stream. */
	protected final DataInputStream das;
	
	/** The target pool list. */
	protected final CIPoolEntry[] entries;
	
	/**
	 * Initializes the constant pool decoder.
	 *
	 * @param __oc The outer class.
	 * @param __das The input data source.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	__PoolDecoder__(CISClass __oc, DataInputStream __das)
		throws IOException, NullPointerException
	{
		// Check
		if (__oc == null || __das == null)
			throw new NullPointerException("NARG");
		
		// Set
		outerclass = __oc;
		das = __das;
		
		// Read entry count, a class cannot have zero entries in it
		int numentries = das.readUnsignedShort();
		
		// {@squirreljme.errr CF0k Class has a constant pool with a negative
		// number of entries.}
		if (numentries <= 0)
			throw new CIException(CIException.Issue.INVALID_POOL_SIZE,
				"AQ0k");
		
		// Setup target array
		entries = new CIPoolEntry[numentries];
	}
	
	/**
	 * This decodes the constant pool.
	 *
	 * @return The loaded constant pool.
	 * @throws IOException On read errors.
	 * @since 2016/04/24
	 */
	public CIPool get()
		throws IOException
	{
		// Some entries refer to other entries
		int n = entries.length;
		int[][] dref = new int[n][];
		
		// Decode all entries
		for (int i = 1; i < n; i++)
		{
			// Read tag
			int tag = das.readUnsignedByte();
			switch (tag)
			{
					// UTF string
				case TAG_UTF8:
					// Read
					try
					{
						entries[i] = new CIUTF(das.readUTF());
					}
		
					// Malformed sequence
					catch (UTFDataFormatException utfdfe)
					{
						// {@squirreljme.error AQ0j The string which makes up a
						// UTF-8 constant string is not a correctly formatted
						// modified UTF-8 string.}
						throw new CIException(CIException.Issue.ILLEGAL_MUTF,
							"AQ0j", utfdfe);
					}
					break;
					
					// Integer constant
				case TAG_INTEGER:
					entries[i] = new CIConstantInteger(das.readInt());
					break;
					
					// Float constant
				case TAG_FLOAT:
					entries[i] = new CIConstantFloat(das.readFloat());
					break;
					
					// Long constant
				case TAG_LONG:
					entries[i++] = new CIConstantLong(das.readLong());
					break;
					
					// Double constant
				case TAG_DOUBLE:
					entries[i++] = new CIConstantDouble(das.readDouble());
					break;
					
					// Single reference
				case TAG_CLASS:
				case TAG_STRING:
					dref[i] = new int[]{tag, das.readUnsignedShort()};
					break;
					
					// Double reference
				case TAG_NAMEANDTYPE:
				case TAG_FIELDREF:
				case TAG_METHODREF:
				case TAG_INTERFACEMETHODREF:
					dref[i] = new int[]{tag, das.readUnsignedShort(),
						das.readUnsignedShort()};
					break;
					
					// invokedynamic is not supported!
				case TAG_METHODHANDLE:
				case TAG_METHODTYPE:
				case TAG_INVOKEDYNAMIC:
					// {@squirreljme.error AQ0l {@code invokedynamic} is not
					// supported in Java ME.}
					throw new CIException(CIException.Issue.INVOKEDYNAMIC,
						"AQ0l");
					
					// Unknown
				default:
					// {@squirreljme.error AQ0m The specified constant pool
					// tag is not valid. (The illegal constant pool tag).}
					throw new CIException(CIException.Issue.ILLEGAL_TAG,
						String.format("AQ0m %d", tag));
			}
		}
		
		// Build entries for references
		for (int i = 0; i < n; i++)
		{
			// Uses references?
			int refs[] = dref[i];
			
			// Not shared
			if (refs == null)
				continue;
			
			// Could be wrong
			int tag = refs[0];
			try
			{
				// Depends on the tag
				switch (tag)
				{
						// Class
					case TAG_CLASS:
						__class(dref, i);
						break;
					
						// String
					case TAG_STRING:
						entries[i] = new CIConstantString(
							((CIUTF)entries[refs[1]]).toString());
						break;
					
						// Name and type
					case TAG_NAMEANDTYPE:
						__nat(dref, i);
						break;
					
						// References to members
					case TAG_FIELDREF:
					case TAG_METHODREF:
					case TAG_INTERFACEMETHODREF:
						ClassNameSymbol cl = __class(dref, refs[1]).get();
						CIMemberNameAndType nat = __nat(dref, refs[2]);
						
						// A field
						if (tag == TAG_FIELDREF)
							entries[i] = new CIFieldReference(cl, nat.name(),
								nat.type().asField());
						
						// Methods
						else
							entries[i] = new CIMethodReference(
								tag != TAG_METHODREF, cl, nat.name(),
								nat.type().asMethod());
						break;
					
						// Unknown
					default:
						throw new RuntimeException(String.format("WTFX %d",
							tag));
				}
			}
			
			// Bad pool
			catch (IndexOutOfBoundsException|ClassCastException|
				NullPointerException|IllegalSymbolException e)
			{
				// {@squirrejme.error AQ1g A constant pool entry references a
				// constant which is not valid. (The tag; Reference A;
				// Reference B)}
				int q = refs.length;
				throw new CIException(CIException.Issue.ILLEGAL_CONSTANT,
					String.format("AQ1g %d %d %d", tag, (1 < q ? refs[1] : -1),
					(2 < q ? refs[2] : -1)), e);
			}
		}
		
		// Build it
		return new CIPool(entries);
	}
	
	/**
	 * initializes the class reference.
	 *
	 * @param __refs The reference data.
	 * @param __ei The index of the class name.
	 * @return The class reference here.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	private CIClassReference __class(int[][] __refs, int __ei)
		throws NullPointerException
	{
		// Check
		if (__refs == null)
			throw new NullPointerException("NARG");
		
		// Get entry set
		CIPoolEntry ents[] = entries;
		
		// Already here?
		CIPoolEntry erv;
		if ((erv = ents[__ei]) != null)
			return (CIClassReference)erv;
		
		// Construct it
		int[] refs = __refs[__ei];
		CIClassReference rv = new CIClassReference((
			((CIUTF)entries[refs[1]]).asClassName()));
		
		// Set and return it
		ents[__ei] = rv;
		return rv;
	}
	
	/**
	 * Initializes the name and type information.
	 *
	 * @param __refs The reference data.
	 * @param __ei The index of the name and type.
	 * @return The initialized member name and type.
	 * @throws NullPointerException On null arguments
	 * @since 2016/04/24
	 */
	private CIMemberNameAndType __nat(int[][] __refs, int __ei)
		throws NullPointerException
	{
		// Check
		if (__refs == null)
			throw new NullPointerException("NARG");
		
		// Get entry set
		CIPoolEntry ents[] = entries;
		
		// Already here?
		CIPoolEntry erv;
		if ((erv = ents[__ei]) != null)
			return (CIMemberNameAndType)erv;
		
		// Need to construct it
		int[] refs = __refs[__ei];
		CIMemberNameAndType rv = new CIMemberNameAndType(
			(((CIUTF)ents[refs[1]]).asIdentifier()),
			(((CIUTF)ents[refs[2]]).asMember()));
		
		// Set it
		ents[__ei] = rv;
		return rv;
	}
}

