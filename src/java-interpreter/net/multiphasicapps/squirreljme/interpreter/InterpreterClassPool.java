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

/**
 * This represents the constant pool which exists within a class.
 *
 * Java ME targets cannot have constants pertaining to invokedynamic.
 *
 * @since 2016/03/13
 */
public class InterpreterClassPool
{
	/** The UTF constant tag. */
	protected static final int TAG_UTF8 =
		1;	
	
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
		throws IOException, NullPointerException
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
			switch (tag)
			{
					// UTF-8 Constant
				case TAG_UTF8:
					ents[i] = new UTF8(__is);
					break;
					
					// Unknown
				default:
					throw new InterpreterClassFormatError("Unsupported " +
						"constant pool tag " + tag + ".");
			}
		}
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
	 * This is a UTF-8 string constant.
	 *
	 * @since 2016/03/13
	 */
	public class UTF8
		extends Entry
		implements CharSequence
	{
		/** Internally read string. */
		protected final String string;
		
		/**
		 * Initializes the constant value.
		 *
		 * @param __is Data input source.
		 * @throws IOException On read errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/03/13
		 */
		private UTF8(DataInputStream __dis)
			throws IOException, NullPointerException
		{
			// Check
			if (__dis == null)
				throw new NullPointerException();
			
			// Read
			string = __dis.readUTF();
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

