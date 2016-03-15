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
import java.io.UTFDataFormatException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IllegalSymbolException;
import net.multiphasicapps.descriptors.MemberTypeSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents the constant pool which exists within a class.
 *
 * Java ME targets cannot have constants pertaining to invokedynamic.
 *
 * @since 2016/03/13
 */
public class InterpreterClassPool
	extends AbstractList<InterpreterClassPool.Entry>
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
	private final Entry[] _entries;
	
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
		Entry[] ents;
		_entries = ents = new Entry[numentries];
		for (int i = 1; i < numentries; i++)
		{
			// Read the tag
			int tag = __is.readUnsignedByte();
			
			// Depends on the tag
			Entry en;
			switch (tag)
			{
					// UTF-8 Constant
				case TAG_UTF8:
					en = new UTF8(__is);
					break;
					
					// The name of a class
				case TAG_CLASS:
					en = new ClassName(__is);
					break;
					
					// A reference to a field
				case TAG_FIELDREF:
					en = new FieldReference(__is);
					break;
					
					// A reference to a method
				case TAG_METHODREF:
					en = new MethodReference(__is);
					break;
					
					// A reference to an interface method
				case TAG_INTERFACEMETHODREF:
					en = new InterfaceMethodReference(__is);
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
				case TAG_STRING:
				case TAG_NAMEANDTYPE:
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
	public InterpreterClassPool.Entry get(int __i)
	{
		return _entries[__i];
	}
	
	/**
	 * Obtains an entry from the constant pool as a specific class type.
	 *
	 * @param <Q> The type of entry to return.
	 * @param __cl The class type to cast to.
	 * @param __i The index of the entry.
	 * @throws InterpreterClassFormatError If the type at this position is not
	 * of the given class.
	 * @since 2016/03/15
	 */
	public <Q extends Entry> Q getAs(Class<Q> __cl, int __i)
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
	
	/**
	 * Checks the range of a reference to make sure it is within bounds of
	 * an existing entry.
	 *
	 * @param __v The index to check the range for.
	 * @return {@code __v} if the range is valid.
	 * @throws InterpreterClassFormatError If the range is not valid.
	 * @since 2016/03/15
	 */
	private int __rangeCheck(int __v)
		throws InterpreterClassFormatError
	{
		if (__v > 0 && __v < numentries)
			return __v;
		throw new InterpreterClassFormatError("Reference index " + __v +
			"is nothing with the constant pool bounds.");
	}
	
	/**
	 * This is the base class for constant pool entries.
	 *
	 * @since 2016/03/13
	 */
	public abstract class Entry
	{
		/**
		 * Initializes the entry.
		 *
		 * @since 2016/03/13
		 */
		private Entry()
		{
		}
	}
	
	/**
	 * This represents a reference type.
	 *
	 * @param V The symbol
	 * @since 2016/03/15
	 */
	public abstract class MemberReference<V extends MemberTypeSymbol>
		extends Entry
	{
		/** The type to cast the type as. */
		protected final Class<V> castas;
		
		/** The class index. */
		protected final int classdx;
		
		/** The name and type index. */
		protected final int natdx;
		
		/**
		 * This initializes
		 *
		 * @param __dis The constant data to load in.
		 * @param __cl The class to cast the type to.
		 * @throws IOException On read errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/15
		 */
		private MemberReference(DataInputStream __dis, Class<V> __cl)
			throws IOException, NullPointerException
		{
			// Check
			if (__dis == null || __cl == null)
				throw new NullPointerException();
			
			// Set
			castas = __cl;
			
			// Read in
			classdx = __rangeCheck(__dis.readUnsignedShort());
			natdx = __rangeCheck(__dis.readUnsignedShort());
		}
		
		/**
		 * Returns the utilized class name.
		 *
		 * @return The class name for the member reference.
		 * @since 2016/03/15
		 */
		public final ClassName className()
		{
			return InterpreterClassPool.this.<ClassName>getAs(ClassName.class,
				classdx);
		}
	}
	
	/**
	 * This represents the name of a class.
	 *
	 * @since 2016/03/15
	 */
	public final class ClassName
		extends Entry
	{
		/** The class name index. */
		protected final int index;
		
		/** The actual class symbol. */
		private volatile Reference<ClassNameSymbol> _cname;
		
		/**
		 * Initializes the class name.
		 *
		 * @param __dis Input stream to read data from.
		 * @throws InterpreterClassFormatError If the class name is not
		 * valid.
		 * @throws IOException On read errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/15
		 */
		private ClassName(DataInputStream __dis)
			throws InterpreterClassFormatError, IOException,
				NullPointerException
		{
			// Check
			if (__dis == null)
				throw new NullPointerException();
			
			// Get id
			index = __rangeCheck(__dis.readUnsignedShort());
		}
		
		/**
		 * Returns the symbol associated with this class.
		 *
		 * @return The class name symbol.
		 * @throws InterpreterClassFormatError If the class name symbol is
		 * invalid.
		 * @since 2016/03/15
		 */
		public ClassNameSymbol symbol()
			throws InterpreterClassFormatError
		{
			// Get reference
			Reference<ClassNameSymbol> ref = _cname;
			ClassNameSymbol rv = null;
			
			// In reference?
			if (ref != null)
				rv = ref.get();
			
			// Needs initialization
			if (rv == null)
				try
				{
					_cname = new WeakReference<>((rv = new ClassNameSymbol(
						InterpreterClassPool.this.<UTF8>getAs(
							UTF8.class, index).toString())));
				}
				
				// Bad symbol
				catch (IllegalSymbolException ise)
				{
					throw new InterpreterClassFormatError(ise);
				}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * This represents a field reference.
	 *
	 * @since 2016/03/15
	 */
	public final class FieldReference
		extends MemberReference<FieldSymbol>
	{
		/**
		 * Initializes the field reference.
		 *
		 * @param __dis Data source.
		 * @throws IOException On read errors.
		 * @since 2016/03/15
		 */
		private FieldReference(DataInputStream __dis)
			throws IOException
		{
			super(__dis, FieldSymbol.class);
		}
	}
	
	/**
	 * This implements a interface method reference.
	 *
	 * @since 2016/03/15
	 */
	public final class InterfaceMethodReference
		extends MemberReference<MethodSymbol>
	{
		/**
		 * Initializes the interface method reference.
		 *
		 * @param __dis Data source.
		 * @throws IOException On read errors.
		 * @since 2016/03/15
		 */
		private InterfaceMethodReference(DataInputStream __dis)
			throws IOException
		{
			super(__dis, MethodSymbol.class);
		}
	}
	
	/**
	 * This implements a method reference.
	 *
	 * @since 2016/03/15
	 */
	public final class MethodReference
		extends MemberReference<MethodSymbol>
	{
		/**
		 * Initializes the method reference.
		 *
		 * @param __dis Data source.
		 * @throws IOException On read errors.
		 * @since 2016/03/15
		 */
		private MethodReference(DataInputStream __dis)
			throws IOException
		{
			super(__dis, MethodSymbol.class);
		}
	}
	
	/**
	 * This is a UTF-8 string constant.
	 *
	 * @since 2016/03/13
	 */
	public final class UTF8
		extends Entry
		implements CharSequence
	{
		/** Internally read string. */
		protected final String string;
		
		/**
		 * Initializes the constant value.
		 *
		 * @param __is Data input source.
		 * @throws InterpreterClassFormatError If the modfied UTF string is
		 * malformed.
		 * @throws IOException On read errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/13
		 */
		private UTF8(DataInputStream __dis)
			throws InterpreterClassFormatError, IOException,
				NullPointerException
		{
			// Check
			if (__dis == null)
				throw new NullPointerException();
			
			// Read
			try
			{
				string = __dis.readUTF();
			}
			
			// Malformed sequence
			catch (UTFDataFormatException utfdfe)
			{
				throw new InterpreterClassFormatError(utfdfe);
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/13
		 */
		@Override
		public char charAt(int __i)
		{
			return string.charAt(__i);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/13
		 */
		@Override
		public int length()
		{
			return string.length();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/13
		 */
		@Override
		public CharSequence subSequence(int __s, int __e)
		{
			return string.subSequence(__s, __e);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/13
		 */
		@Override
		public String toString()
		{
			return string;
		}
	}
}

