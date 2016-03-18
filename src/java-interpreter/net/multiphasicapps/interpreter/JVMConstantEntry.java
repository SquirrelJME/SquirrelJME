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
import java.io.IOException;
import java.io.UTFDataFormatException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Objects;
import net.multiphasicapps.descriptors.BinaryNameSymbol;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.IllegalSymbolException;
import net.multiphasicapps.descriptors.MemberTypeSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents the base class for all constant pool entries.
 *
 * @since 2016/03/15
 */
public abstract class JVMConstantEntry
{
	/** The owning pool. */
	protected final JVMConstantPool pool;
	
	/**
	 * Initializes the base of an entry.
	 *
	 * @param __icp The owning constant pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/15
	 */
	private JVMConstantEntry(JVMConstantPool __icp)
		throws NullPointerException
	{
		// Check
		if (__icp == null)
			throw new NullPointerException("NARG");
		
		// Set
		pool = __icp;
	}
	
	/**
	 * Checks the range of a reference to make sure it is within bounds of
	 * an existing entry.
	 *
	 * @param __v The index to check the range for.
	 * @return {@code __v} if the range is valid.
	 * @throws JVMClassFormatError If the range is not valid.
	 * @since 2016/03/15
	 */
	int __rangeCheck(int __v)
		throws JVMClassFormatError
	{
		if (__v > 0 && __v < pool.size())
			return __v;
		throw new JVMClassFormatError(String.format("IN0d %d %d", __v,
			pool.size()));
	}
	
	/**
	 * This represents the base of the constant value pool.
	 *
	 * @param <C> The type of constant value to return.
	 * @since 2016/03/15
	 */
	public static abstract class ConstantValue<C>
		extends JVMConstantEntry
	{
		/** The type of value to store. */
		protected final Class<C> castas;
		
		/**
		 * Initializes the base constant information.
		 *
		 * @param __icp The owning constant pool.
		 * @param __cl The class to cast to.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/15
		 */
		ConstantValue(JVMConstantPool __icp, Class<C> __cl)
		{
			super(__icp);
			
			// Check
			if (__cl == null)
				throw new NullPointerException("NARG");
			
			// Set
			castas = __cl;
		}
		
		/**
		 * Returns the value of the constant.
		 *
		 * @return The constant value.
		 * @since 2016/03/15
		 */
		public abstract C getValue();
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/15
		 */
		@Override
		public final String toString()
		{
			return Objects.toString(getValue());
		}
	}
	
	/**
	 * This represents a reference type.
	 *
	 * @param V The symbol
	 * @since 2016/03/15
	 */
	public static abstract class MemberReference<V extends MemberTypeSymbol>
		extends JVMConstantEntry
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
		 * @param __icp The owning constant pool.
		 * @param __dis The constant data to load in.
		 * @param __cl The class to cast the type to.
		 * @throws IOException On read errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/15
		 */
		MemberReference(JVMConstantPool __icp,
			DataInputStream __dis, Class<V> __cl)
			throws IOException, NullPointerException
		{
			super(__icp);
			
			// Check
			if (__dis == null || __cl == null)
				throw new NullPointerException("NARG");
			
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
			return pool.<ClassName>getAs(classdx,
				ClassName.class);
		}
	}
	
	/**
	 * This represents the name of a class.
	 *
	 * @since 2016/03/15
	 */
	public static final class ClassName
		extends JVMConstantEntry
	{
		/** The class name index. */
		protected final int index;
		
		/** The actual class symbol. */
		private volatile Reference<ClassNameSymbol> _cname;
		
		/**
		 * Initializes the class name.
		 *
		 * @param __icp The owning constant pool.
		 * @param __dis Input stream to read data from.
		 * @throws JVMClassFormatError If the class name is not
		 * valid.
		 * @throws IOException On read errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/15
		 */
		ClassName(JVMConstantPool __icp, DataInputStream __dis)
			throws JVMClassFormatError, IOException,
				NullPointerException
		{
			super(__icp);
			
			// Check
			if (__dis == null)
				throw new NullPointerException("NARG");
			
			// Get id
			index = __rangeCheck(__dis.readUnsignedShort());
		}
		
		/**
		 * Returns the symbol associated with this class.
		 *
		 * @return The class name symbol.
		 * @throws JVMClassFormatError If the class name symbol is
		 * invalid.
		 * @since 2016/03/15
		 */
		public ClassNameSymbol symbol()
			throws JVMClassFormatError
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
						pool.<UTF8>getAs(index, UTF8.class).toString())));
				}
				
				// Bad symbol
				catch (IllegalSymbolException ise)
				{
					throw new JVMClassFormatError("IN0e", ise);
				}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * This represents a constant string value.
	 *
	 * @since 2016/03/15
	 */
	public static final class ConstantString
		extends ConstantValue<String>
		implements CharSequence
	{
		/** The indexed UTF-8 constant. */
		protected final int index;
		
		/**
		 * Initializes the string constant.
		 *
		 * @param __icp The owning constant pool.
		 * @param __dis Data source.
		 * @throws IOException On read errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/15
		 */
		ConstantString(JVMConstantPool __icp,
			DataInputStream __dis)
			throws IOException, NullPointerException
		{
			super(__icp, String.class);
			
			// Check
			if (__dis == null)
				throw new NullPointerException("NARG");
			
			// Read the string index
			index = __dis.readUnsignedShort();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/15
		 */
		@Override
		public char charAt(int __i)
		{
			return toString().charAt(__i);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/15
		 */
		@Override
		public String getValue()
		{
			return pool.<UTF8>getAs(index, UTF8.class).toString();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/15
		 */
		@Override
		public int length()
		{
			return toString().length();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/15
		 */
		@Override
		public CharSequence subSequence(int __s, int __e)
		{
			return toString().subSequence(__s, __e);
		}
	}
	
	/**
	 * This represents a field reference.
	 *
	 * @since 2016/03/15
	 */
	public static final class FieldReference
		extends MemberReference<FieldSymbol>
	{
		/**
		 * Initializes the field reference.
		 *
		 * @param __icp The owning constant pool.
		 * @param __dis Data source.
		 * @throws IOException On read errors.
		 * @since 2016/03/15
		 */
		FieldReference(JVMConstantPool __icp,
			DataInputStream __dis)
			throws IOException
		{
			super(__icp, __dis, FieldSymbol.class);
		}
	}
	
	/**
	 * This implements a interface method reference.
	 *
	 * @since 2016/03/15
	 */
	public static final class InterfaceMethodReference
		extends MemberReference<MethodSymbol>
	{
		/**
		 * Initializes the interface method reference.
		 *
		 * @param __icp The owning constant pool.
		 * @param __dis Data source.
		 * @throws IOException On read errors.
		 * @since 2016/03/15
		 */
		InterfaceMethodReference(JVMConstantPool __icp,
			DataInputStream __dis)
			throws IOException
		{
			super(__icp, __dis, MethodSymbol.class);
		}
	}
	
	/**
	 * This implements a method reference.
	 *
	 * @since 2016/03/15
	 */
	public static final class MethodReference
		extends MemberReference<MethodSymbol>
	{
		/**
		 * Initializes the method reference.
		 *
		 * @param __icp The owning constant pool.
		 * @param __dis Data source.
		 * @throws IOException On read errors.
		 * @since 2016/03/15
		 */
		MethodReference(JVMConstantPool __icp,
			DataInputStream __dis)
			throws IOException
		{
			super(__icp, __dis, MethodSymbol.class);
		}
	}
	
	/**
	 * This represents name and type information.
	 *
	 * @since 2016/03/15
	 */
	public static final class NameAndType
		extends JVMConstantEntry
		implements Map.Entry<IdentifierSymbol, MemberTypeSymbol>
	{
		/** Lock. */
		protected final Object lock =
			new Object();
		
		/** Name index. */
		protected final int namedx;
		
		/** Type index. */
		protected final int typedx;
		
		/** The name as a symbol. */
		private volatile Reference<IdentifierSymbol> _name;
		
		/** The descriptor as a symbol. */
		private volatile Reference<MemberTypeSymbol> _type;
		
		/**
		 * Initializes name and type information.
		 *
		 * @param __icp The owning constant pool.
		 * @param __dis Data source.
		 * @throws IOException On read errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/15
		 */
		NameAndType(JVMConstantPool __icp, DataInputStream __dis)
			throws IOException, NullPointerException
		{
			super(__icp);
			
			// Check
			if (__dis == null)
				throw new NullPointerException("NARG");
			
			// Read values
			namedx = __rangeCheck(__dis.readUnsignedShort());
			typedx = __rangeCheck(__dis.readUnsignedShort());
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/17
		 */
		@Override
		public boolean equals(Object __o)
		{
			// If not a NAT, do not bother
			if (!(__o instanceof NameAndType))
				return false;
			
			// Cast
			NameAndType o = (NameAndType)__o;
			
			// The symbols in either one of them could be illegal;
			try
			{
				return Objects.equals(getKey(), o.getKey()) &&
					Objects.equals(getValue(), o.getValue());
			}
			
			// One has an illegal set of symbols.
			catch (JVMClassFormatError jvmcfe)
			{
				// Do not match
				return false;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/17
		 */
		@Override
		public IdentifierSymbol getKey()
		{
			// Lock
			synchronized (lock)
			{
				// Get reference
				Reference<IdentifierSymbol> ref = _name;
				IdentifierSymbol rv = null;
				
				// In reference?
				if (ref != null)
					rv = ref.get();
				
				// Needs caching
				if (rv == null)
					try
					{
						_name = new WeakReference<>((rv = new IdentifierSymbol(
							pool.<UTF8>getAs(namedx, UTF8.class).toString())));
					}
					
					// Bad identifier
					catch (IllegalSymbolException ise)
					{
						throw new JVMClassFormatError("IN0s", ise);
					}
				
				// Return it
				return rv;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/17
		 */
		@Override
		public MemberTypeSymbol getValue()
		{
			// Lock
			synchronized (lock)
			{
				// Get reference
				Reference<MemberTypeSymbol> ref = _type;
				MemberTypeSymbol rv = null;
				
				// In reference?
				if (ref != null)
					rv = ref.get();
				
				// Needs caching
				if (rv == null)
					try
					{
						// Types are either fields or methods
						String str = pool.<UTF8>getAs(typedx, UTF8.class).
							toString();
					
						// If it starts with a ( it is a method
						if (str.startsWith("("))
							rv = new MethodSymbol(str);
						
						// Otherwise a field
						else
							rv = new FieldSymbol(str);
					
						// Cache it
						_type = new WeakReference<>(rv);
					}
					
					// Not a valid symbol
					catch (IllegalSymbolException ise)
					{
						throw new JVMClassFormatError(String.format("IN0r"),
							ise);
					}
				
				// Return it
				return rv;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/17
		 */
		@Override
		public int hashCode()
		{
			// Could fail
			try
			{
				return Objects.hashCode(getKey()) ^
					Objects.hashCode(getValue());
			}
			
			// Badly formed symbol
			catch (JVMClassFormatError jvmcfe)
			{
				return 0xDEADBEEF;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/17
		 */
		@Override
		public MemberTypeSymbol setValue(MemberTypeSymbol __v)
		{
			throw new UnsupportedOperationException("RORO");
		}
		
		/**
		 * Returns the represented symbol as a field symbol.
		 *
		 * @return The type as a field symbol.
		 * @throws JVMClassFormatError If the type is not a field symbol.
		 * @since 2016/03/17
		 */
		public FieldSymbol typeAsField()
			throws JVMClassFormatError
		{
			try
			{
				return getValue().asFieldSymbol();
			}
			
			// Is not one
			catch (ClassCastException cce)
			{
				throw new JVMClassFormatError("IN0p", cce);
			}
		}
		
		/**
		 * Returns the represented symbol as a method symbol.
		 *
		 * @return The type as a method symbol.
		 * @throws JVMClassFormatError If the type is not a method symbol.
		 * @since 2016/03/17
		 */
		public MethodSymbol typeAsMethod()
			throws JVMClassFormatError
		{
			try
			{
				return getValue().asMethodSymbol();
			}
			
			// Is not one
			catch (ClassCastException cce)
			{
				throw new JVMClassFormatError("IN0q", cce);
			}
		}
	}
	
	/**
	 * This is a UTF-8 string constant.
	 *
	 * @since 2016/03/13
	 */
	public static final class UTF8
		extends JVMConstantEntry
		implements CharSequence
	{
		/** Internally read string. */
		protected final String string;
		
		/**
		 * Initializes the constant value.
		 *
		 * @param __icp The owning constant pool.
		 * @param __is Data input source.
		 * @throws JVMClassFormatError If the modfied UTF string is
		 * malformed.
		 * @throws IOException On read errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/13
		 */
		UTF8(JVMConstantPool __icp, DataInputStream __dis)
			throws JVMClassFormatError, IOException,
				NullPointerException
		{
			super(__icp);
			
			// Check
			if (__dis == null)
				throw new NullPointerException("NARG");
			
			// Read
			try
			{
				string = __dis.readUTF();
			}
			
			// Malformed sequence
			catch (UTFDataFormatException utfdfe)
			{
				throw new JVMClassFormatError("IN0f", utfdfe);
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

