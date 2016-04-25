// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UTFDataFormatException;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.IllegalSymbolException;
import net.multiphasicapps.descriptors.MemberTypeSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.narf.classinterface.NCIClassReference;
import net.multiphasicapps.narf.classinterface.NCIConstantDouble;
import net.multiphasicapps.narf.classinterface.NCIConstantFloat;
import net.multiphasicapps.narf.classinterface.NCIConstantInteger;
import net.multiphasicapps.narf.classinterface.NCIConstantLong;
import net.multiphasicapps.narf.classinterface.NCIConstantString;
import net.multiphasicapps.narf.classinterface.NCIException;
import net.multiphasicapps.narf.classinterface.NCIFieldReference;
import net.multiphasicapps.narf.classinterface.NCIMemberNameAndType;
import net.multiphasicapps.narf.classinterface.NCIMethodReference;
import net.multiphasicapps.narf.classinterface.NCIPool;
import net.multiphasicapps.narf.classinterface.NCIPoolEntry;
import net.multiphasicapps.narf.classinterface.NCIUTF;

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
	protected final NCFClass outerclass;
	
	/** The input data stream. */
	protected final DataInputStream das;
	
	/** The target pool list. */
	protected final NCIPoolEntry[] entries;
	
	/**
	 * Initializes the constant pool decoder.
	 *
	 * @param __oc The outer class.
	 * @param __das The input data source.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	__PoolDecoder__(NCFClass __oc, DataInputStream __das)
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
			throw new NCIException(NCIException.Issue.INVALID_POOL_SIZE,
				"CF0k");
		
		// Setup target array
		entries = new NCIPoolEntry[numentries];
	}
	
	/**
	 * This decodes the constant pool.
	 *
	 * @return The loaded constant pool.
	 * @throws IOException On read errors.
	 * @since 2016/04/24
	 */
	public NCIPool get()
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
						entries[i] = new NCIUTF(das.readUTF());
					}
		
					// Malformed sequence
					catch (UTFDataFormatException utfdfe)
					{
						// {@squirreljme.error CF0j The string which makes up a
						// UTF-8 constant string is not a correctly formatted
						// modified UTF-8 string.}
						throw new NCIException(NCIException.Issue.ILLEGAL_MUTF,
							"CF0j", utfdfe);
					}
					break;
					
					// Integer constant
				case TAG_INTEGER:
					entries[i] = new NCIConstantInteger(das.readInt());
					break;
					
					// Float constant
				case TAG_FLOAT:
					entries[i] = new NCIConstantFloat(das.readFloat());
					break;
					
					// Long constant
				case TAG_LONG:
					entries[i++] = new NCIConstantLong(das.readLong());
					break;
					
					// Double constant
				case TAG_DOUBLE:
					entries[i++] = new NCIConstantDouble(das.readDouble());
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
					// {@squirreljme.error CF0l {@code invokedynamic} is not
					// supported in Java ME.}
					throw new NCIException(NCIException.Issue.INVOKEDYNAMIC,
						"CF0l");
					
					// Unknown
				default:
					// {@squirreljme.error CF0m The specified constant pool
					// tag is not valid. (The illegal constant pool tag).}
					throw new NCIException(NCIException.Issue.ILLEGAL_TAG,
						String.format("CF0m %d", tag));
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
						entries[i] = new NCIConstantString(
							((NCIUTF)entries[refs[1]]).toString());
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
						NCIMemberNameAndType nat = __nat(dref, refs[2]);
						
						// A field
						if (tag == TAG_FIELDREF)
							entries[i] = new NCIFieldReference(cl, nat.name(),
								nat.type().asField());
						
						// Methods
						else
							entries[i] = new NCIMethodReference(
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
				// {@squirrejme.error CF1g A constant pool entry references a
				// constant which is not valid. (The tag; Reference A;
				// Reference B)}
				int q = refs.length;
				throw new NCIException(NCIException.Issue.ILLEGAL_CONSTANT,
					String.format("CF1g %d %d %d", tag, (1 < q ? refs[1] : -1),
					(2 < q ? refs[2] : -1)), e);
			}
		}
		
		// Build it
		return new NCIPool(entries);
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
	private NCIClassReference __class(int[][] __refs, int __ei)
		throws NullPointerException
	{
		// Check
		if (__refs == null)
			throw new NullPointerException("NARG");
		
		// Get entry set
		NCIPoolEntry ents[] = entries;
		
		// Already here?
		NCIPoolEntry erv;
		if ((erv = ents[__ei]) != null)
			return (NCIClassReference)erv;
		
		// Construct it
		int[] refs = __refs[__ei];
		NCIClassReference rv = new NCIClassReference(new ClassNameSymbol(
			((NCIUTF)entries[refs[1]]).toString()));
		
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
	private NCIMemberNameAndType __nat(int[][] __refs, int __ei)
		throws NullPointerException
	{
		// Check
		if (__refs == null)
			throw new NullPointerException("NARG");
		
		// Get entry set
		NCIPoolEntry ents[] = entries;
		
		// Already here?
		NCIPoolEntry erv;
		if ((erv = ents[__ei]) != null)
			return (NCIMemberNameAndType)erv;
		
		// Need to construct it
		int[] refs = __refs[__ei];
		NCIMemberNameAndType rv = new NCIMemberNameAndType(
			new IdentifierSymbol(((NCIUTF)ents[refs[1]]).toString()),
			(((NCIUTF)ents[refs[2]]).asMember()));
		
		// Set it
		ents[__ei] = rv;
		return rv;
	}
}

