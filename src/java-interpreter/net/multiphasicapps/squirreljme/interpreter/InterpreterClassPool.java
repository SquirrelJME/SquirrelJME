// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;

/**
 * This represents the constant pool which exists within a class.
 *
 * Java ME targets cannot have constants pertaining to invokedynamic.
 *
 * @since 2016/03/13
 */
public class InterpreterClassPool
	extends AbstractList<InterpreterPoolEntry>
{
	/** Is invokedynamic supported anyway? */
	static final boolean SUPPORT_INVOKEDYNAMIC_ANYWAY =
		InterpreterClass.SUPPORT_INVOKEDYNAMIC_ANYWAY;	
	
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
	protected final InterpreterClass owner;	
	
	/** Number of entries in the pool. */
	protected final int numentries;
	
	/** Internal constant pool entries. */
	private final InterpreterPoolEntry[] _entries;
	
	/**
	 * Initializes and interprets the constant pool of a class.
	 *
	 * @param __cl The class owning the pool.
	 * @param __is The input data for the constant pool.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/13
	 */
	InterpreterClassPool(InterpreterClass __cl, DataInputStream __is)
		throws InterpreterClassFormatError, IOException, NullPointerException
	{
		// Check
		if (__cl == null || __is == null)
			throw new NullPointerException();
		
		// Set
		owner = __cl;
		
		// Read entry count, a class cannot have zero entries in it
		numentries = __is.readUnsignedShort();
		if (numentries <= 0)
			throw new InterpreterClassFormatError("Empty constant pool.");
		
		// Read them all
		InterpreterPoolEntry[] ents;
		_entries = ents = new InterpreterPoolEntry[numentries];
		for (int i = 1; i < numentries; i++)
		{
			// Read the tag
			int tag = __is.readUnsignedByte();
			
			// Depends on the tag
			InterpreterPoolEntry en;
			switch (tag)
			{
					// UTF-8 Constant
				case TAG_UTF8:
					en = new InterpreterPoolEntry.UTF8(this, __is);
					break;
					
					// The name of a class
				case TAG_CLASS:
					en = new InterpreterPoolEntry.ClassName(this, __is);
					break;
					
					// A reference to a field
				case TAG_FIELDREF:
					en = new InterpreterPoolEntry.FieldReference(this, __is);
					break;
					
					// A reference to a method
				case TAG_METHODREF:
					en = new InterpreterPoolEntry.MethodReference(this, __is);
					break;
					
					// A reference to an interface method
				case TAG_INTERFACEMETHODREF:
					en = new InterpreterPoolEntry.InterfaceMethodReference(
						this, __is);
					break;
					
					// String constant
				case TAG_STRING:
					en = new InterpreterPoolEntry.ConstantString(this, __is);
					break;
					
					// Name and type information
				case TAG_NAMEANDTYPE:
					en = new InterpreterPoolEntry.NameAndType(this, __is);
					break;
					
					// invokedynamic is not supported!
				case TAG_METHODHANDLE:
				case TAG_METHODTYPE:
				case TAG_INVOKEDYNAMIC:
					if (!SUPPORT_INVOKEDYNAMIC_ANYWAY &&
						!owner.version().hasInvokeDynamic())
						throw new NoInvokeDynamicException();
					throw new Error("INVOKEDYNAMIC -- Constant pool.");
					
					// Unknown
				case TAG_INTEGER:
				case TAG_FLOAT:
				case TAG_LONG:
				case TAG_DOUBLE:
				default:
					throw new InterpreterClassFormatError("Unsupported " +
						"constant pool tag " + tag + ".");
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
	public InterpreterPoolEntry get(int __i)
	{
		return _entries[__i];
	}
	
	/**
	 * Obtains an entry from the constant pool as a specific class type.
	 *
	 * @param <Q> The type of entry to return.
	 * @param __i The index of the entry.
	 * @param __cl The class type to cast to.
	 * @throws InterpreterClassFormatError If the type at this position is not
	 * of the given class.
	 * @since 2016/03/15
	 */
	public <Q extends InterpreterPoolEntry> Q getAs(int __i, Class<Q> __cl)
		throws InterpreterClassFormatError, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException();
		
		// Cast it
		try
		{
			return __cl.cast(get(__i));
		}
		
		// Could not cast
		catch (ClassCastException cce)
		{
			throw new InterpreterClassFormatError(cce);
		}
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

