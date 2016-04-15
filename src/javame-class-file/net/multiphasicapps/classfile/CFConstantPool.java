// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.Objects;

/**
 * This represents the constant pool which exists within a class.
 *
 * Java ME targets cannot have constants pertaining to invokedynamic.
 *
 * @since 2016/03/13
 */
public class CFConstantPool
	extends AbstractList<CFConstantEntry>
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
	
	/** The class which owns the constant pool. */
	protected final CFClassParser owner;	
	
	/** Number of entries in the pool. */
	protected final int numentries;
	
	/** Internal constant pool entries. */
	private final CFConstantEntry[] _entries;
	
	/**
	 * Initializes and interprets the constant pool of a class.
	 *
	 * @param __cl The class parser owning the pool.
	 * @param __is The input data for the constant pool.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/13
	 */
	CFConstantPool(CFClassParser __cl, DataInputStream __is)
		throws CFFormatException, IOException, NullPointerException
	{
		// Check
		if (__cl == null || __is == null)
			throw new NullPointerException("NARG");
		
		// Set
		owner = __cl;
		
		// Read entry count, a class cannot have zero entries in it
		numentries = __is.readUnsignedShort();
		
		// {@squirreljme.errr CF0k Class has a constant pool with a negative
		// number of entries.}
		if (numentries <= 0)
			throw new CFFormatException("CF0k");
		
		// Read them all
		CFConstantEntry[] ents;
		_entries = ents = new CFConstantEntry[numentries];
		for (int i = 1; i < numentries; i++)
		{
			// Read the tag
			int tag = __is.readUnsignedByte();
			
			// Depends on the tag
			CFConstantEntry en;
			switch (tag)
			{
					// UTF-8 Constant
				case TAG_UTF8:
					en = new CFUTF8(this, __is);
					break;
					
					// The name of a class
				case TAG_CLASS:
					en = new CFClassName(this, __is);
					break;
					
					// A reference to a field
				case TAG_FIELDREF:
					en = new CFFieldReference(this, __is);
					break;
					
					// A reference to a method
				case TAG_METHODREF:
					en = new CFMethodReference(this, __is);
					break;
					
					// A reference to an interface method
				case TAG_INTERFACEMETHODREF:
					en = new CFInterfaceMethodReference(
						this, __is);
					break;
					
					// String constant
				case TAG_STRING:
					en = new CFConstantString(this, __is);
					break;
					
					// Name and type information
				case TAG_NAMEANDTYPE:
					en = new CFNameAndType(this, __is);
					break;
					
					// invokedynamic is not supported!
				case TAG_METHODHANDLE:
				case TAG_METHODTYPE:
				case TAG_INVOKEDYNAMIC:
					// {@squirreljme.error CF0l {@code invokedynamic} is not
					// supported in Java ME.}
					throw new CFFormatException("CF0l");
					
					// Unknown
				case TAG_INTEGER:
				case TAG_FLOAT:
				case TAG_LONG:
				case TAG_DOUBLE:
				default:
					// {@squirreljme.error CF0m The specified constant pool
					// tag is not valid. (The illegal constant pool tag).}
					throw new CFFormatException(String.format("CF0m", tag));
			}
			
			// Set
			ents[i] = en;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/13
	 */
	@Override
	public CFConstantEntry get(int __i)
	{
		return _entries[__i];
	}
	
	/**
	 * Obtains an entry from the constant pool as a specific class type.
	 *
	 * @param <Q> The type of entry to return.
	 * @param __i The index of the entry.
	 * @param __cl The class type to cast to.
	 * @throws CFFormatException If the type at this position is not
	 * of the given class.
	 * @since 2016/03/15
	 */
	public <Q extends CFConstantEntryKind> Q getAs(int __i, Class<Q> __cl)
		throws CFFormatException, NullPointerException
	{
		// Locate entry
		Object maybe = null;
		try
		{
			return Objects.<Q>requireNonNull(__cl.cast((maybe = get(__i))));
		}
		
		// Is missing
		catch (ClassCastException|NullPointerException e)
		{
			// {@squirreljme.error CF0n Cannot get a constant of the specified
			// type because it does not match the type that it was expected
			// to be. (The index to get; The type of class it was expected to
			// be; The class that it really was)}
			throw new CFFormatException(String.format("CF0n %d %s %s", __i,
				__cl, (maybe != null ? maybe.getClass() : null)), e);
		}
	}
	
	/**
	 * Obtains the specified constant pool entry at the given location
	 *
	 * @param <Q> The type of entry to return.
	 * @param __i The index of this entry, if zero then {@code null} is
	 * returned.
	 * @param __cl The type of the expected entry.
	 * @return The entry at the given position or {@code null} if it is the
	 * zero index.
	 * @throws CFFormatException If the type at this position is not
	 * of the given class.
	 * @since 2016/03/31
	 */
	public <Q extends CFConstantEntry> Q getAsOptional(int __i, Class<Q> __cl)
		throws CFFormatException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// If zero, then nothing needs to be done
		if (__i == 0)
			return null;
		
		// Otherwise get it
		return this.<Q>getAs(__i, __cl);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/13
	 */
	@Override
	public int size()
	{
		return numentries;
	}
}

