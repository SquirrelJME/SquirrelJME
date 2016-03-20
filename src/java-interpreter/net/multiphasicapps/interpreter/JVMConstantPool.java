// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

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
public class JVMConstantPool
	extends AbstractList<JVMConstantEntry>
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
	protected final JVMClassFile owner;	
	
	/** Number of entries in the pool. */
	protected final int numentries;
	
	/** Internal constant pool entries. */
	private final JVMConstantEntry[] _entries;
	
	/**
	 * Initializes and interprets the constant pool of a class.
	 *
	 * @param __cl The class parser owning the pool.
	 * @param __is The input data for the constant pool.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/13
	 */
	JVMConstantPool(JVMClassFile __cl, DataInputStream __is)
		throws JVMClassFormatError, IOException, NullPointerException
	{
		// Check
		if (__cl == null || __is == null)
			throw new NullPointerException("NARG");
		
		// Set
		owner = __cl;
		
		// Read entry count, a class cannot have zero entries in it
		numentries = __is.readUnsignedShort();
		if (numentries <= 0)
			throw new JVMClassFormatError("IN0g");
		
		// Read them all
		JVMConstantEntry[] ents;
		_entries = ents = new JVMConstantEntry[numentries];
		for (int i = 1; i < numentries; i++)
		{
			// Read the tag
			int tag = __is.readUnsignedByte();
			
			// Depends on the tag
			JVMConstantEntry en;
			switch (tag)
			{
					// UTF-8 Constant
				case TAG_UTF8:
					en = new JVMConstantEntry.UTF8(this, __is);
					break;
					
					// The name of a class
				case TAG_CLASS:
					en = new JVMConstantEntry.ClassName(this, __is);
					break;
					
					// A reference to a field
				case TAG_FIELDREF:
					en = new JVMConstantEntry.FieldReference(this, __is);
					break;
					
					// A reference to a method
				case TAG_METHODREF:
					en = new JVMConstantEntry.MethodReference(this, __is);
					break;
					
					// A reference to an interface method
				case TAG_INTERFACEMETHODREF:
					en = new JVMConstantEntry.InterfaceMethodReference(
						this, __is);
					break;
					
					// String constant
				case TAG_STRING:
					en = new JVMConstantEntry.ConstantString(this, __is);
					break;
					
					// Name and type information
				case TAG_NAMEANDTYPE:
					en = new JVMConstantEntry.NameAndType(this, __is);
					break;
					
					// invokedynamic is not supported!
				case TAG_METHODHANDLE:
				case TAG_METHODTYPE:
				case TAG_INVOKEDYNAMIC:
					throw new JVMClassFormatError("IN0h");
					
					// Unknown
				case TAG_INTEGER:
				case TAG_FLOAT:
				case TAG_LONG:
				case TAG_DOUBLE:
				default:
					throw new JVMClassFormatError(String.format("IN0i", tag));
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
	public JVMConstantEntry get(int __i)
	{
		return _entries[__i];
	}
	
	/**
	 * Obtains an entry from the constant pool as a specific class type.
	 *
	 * @param <Q> The type of entry to return.
	 * @param __i The index of the entry.
	 * @param __cl The class type to cast to.
	 * @throws JVMClassFormatError If the type at this position is not
	 * of the given class.
	 * @since 2016/03/15
	 */
	public <Q extends JVMConstantEntry> Q getAs(int __i, Class<Q> __cl)
		throws JVMClassFormatError, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Cast it
		try
		{
			return __cl.cast(Objects.requireNonNull(get(__i)));
		}
		
		// Could not cast
		catch (ClassCastException|NullPointerException e)
		{
			throw new JVMClassFormatError(String.format("IN0j %d %s", __i,
				__cl), e);
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

