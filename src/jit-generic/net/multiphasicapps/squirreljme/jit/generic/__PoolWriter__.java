// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITConstantEntry;
import net.multiphasicapps.squirreljme.jit.JITConstantPool;
import net.multiphasicapps.squirreljme.jit.JITMemberReference;
import net.multiphasicapps.squirreljme.jit.JITNameAndType;

/**
 * This writes the constant pool.
 *
 * @since 2016/08/14
 */
class __PoolWriter__
{
	/** The pool to write. */
	protected final JITConstantPool pool;
	
	/** The mapping of strings. */
	protected final Map<String, __String__> strings =
		new LinkedHashMap<>();
	
	/** The string table size. */
	final int _stringcount;
	
	/** Entries in the constant pool. */
	final int _poolcount;
	
	/** The position of the string table. */
	volatile int _stringpos;
	
	/** The position of the constant pool. */
	volatile int _poolpos;
	
	/**
	 * Initializes the pool writer.
	 *
	 * @param __pool The constant pool to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/14
	 */
	__PoolWriter__(JITConstantPool __pool)
		throws NullPointerException
	{
		// Check
		if (__pool == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.pool = __pool;
		
		// Go through all pool entries and extract strings from them to use
		int n = __pool.size();
		this._poolcount = n;
		for (int i = 0; i < n; i++)
		{
			JITConstantEntry e = __pool.get(i);
			
			// Ignore if not active since these strings will waste space when
			// they are pointless at runtime (such as type signatures)
			if (!e.isActive())
				continue;
			
			// Get tag here
			int tag = e.tag();
			
			// Depends on the tag
			switch (tag)
			{
					// No string data
				case 0:	// null entry or top
				case JITConstantPool.TAG_INTEGER:
				case JITConstantPool.TAG_FLOAT:
				case JITConstantPool.TAG_LONG:
				case JITConstantPool.TAG_DOUBLE:
					continue;
				
					// The first element is the string
				case JITConstantPool.TAG_UTF8:
				case JITConstantPool.TAG_CLASS:
				case JITConstantPool.TAG_STRING:
					__addString(e.<Object>get(false, Object.class).
						toString());
					break;
					
					// Name and type
				case JITConstantPool.TAG_NAMEANDTYPE:
					JITNameAndType jnt = e.<JITNameAndType>get(
						false, JITNameAndType.class);
					__addString(jnt.name().toString());
					__addString(jnt.type().toString());
					break;
				
					// Reference to other things
				case JITConstantPool.TAG_FIELDREF:
				case JITConstantPool.TAG_METHODREF:
				case JITConstantPool.TAG_INTERFACEMETHODREF:
					JITMemberReference jmr = e.
						<JITMemberReference>get(false,
						JITMemberReference.class);
					__addString(jmr.className().toString());
					__addString(jmr.memberName().toString());
					__addString(jmr.memberType().toString());
					break;
				
					// Unknown
				default:
					throw new RuntimeException("OOPS");
			}
		}
		
		// Write the string count
		this._stringcount = this.strings.size();
	}
	
	/**
	 * Adds a string.
	 *
	 * @param __s The string to add.
	 * @return The string information.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/14
	 */
	private __String__ __addString(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Check if the string is already in the table
		Map<String, __String__> strings = this.strings;
		__String__ rv = strings.get(__s);
		if (rv != null)
			return rv;
		
		// Otherwise add at the end
		strings.put(__s, (rv = new __String__(__s, strings.size())));
		return rv;
	}
	
	/**
	 * Writes the constant pool.
	 *
	 * @param __dos The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/14
	 */
	void __write(ExtendedDataOutputStream __dos)
		throws IOException, NullPointerException
	{
		// Check
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// Write strings
		__writeStrings(__dos);
		
		// Initialize positions
		int n = pool.size();
		int pos[] = new int[n];
		for (int i = 0; i < n; i++)
			pos[i] = -1;
		
		// Write pool data
		JITConstantPool pool = this.pool;
		for (int i = 0; i < n; i++)
		{
			JITConstantEntry e = pool.get(i);
			
			// Ignore if not active
			if (i > 0 && !e.isActive())
				continue;
			
			// Align
			while ((__dos.size() & 3) != 0)
				__dos.writeByte(0);
			
			// {@squirreljme.error BA0y The constant pool entry starts at a
			// position outside the range of the class size limit.}
			long ppp = __dos.size();
			if (ppp < 0 || ppp > GenericClassWriter._SIZE_LIMIT)
				throw new JITException("BA0y");
			pos[i] = (int)ppp;
			
			// Get details
			int tag = e.tag();
			
			// Depends on the tag
			__dos.writeShort(tag);
			switch (tag)
			{
					// Not used
				case 0:
					break;
				
					// Integer
				case JITConstantPool.TAG_INTEGER:
					__dos.writeInt(e.<Integer>get(false,
						Integer.class));
					break;
				
					// Float	
				case JITConstantPool.TAG_FLOAT:
					__dos.writeInt(Float.floatToRawIntBits(
						e.<Float>get(false, Float.class)));
					break;
					
					// Long
				case JITConstantPool.TAG_LONG:
					__dos.writeLong(e.<Long>get(false, Long.class));
					i++;
					break;
					
					// Double
				case JITConstantPool.TAG_DOUBLE:
					__dos.writeLong(Double.doubleToRawLongBits(
						e.<Double>get(false, Double.class)));
					i++;
					break;
					
					// Single string (use string table)
				case JITConstantPool.TAG_UTF8:
				case JITConstantPool.TAG_CLASS:
				case JITConstantPool.TAG_STRING:
					__dos.writeShort(__addString(
						e.<Object>get(false,
							Object.class).toString())._index);
					break;
				
					// Name and type
				case JITConstantPool.TAG_NAMEANDTYPE:
					JITNameAndType jnt = e.<JITNameAndType>get(false,
						JITNameAndType.class);
					__dos.writeShort(__addString(
						jnt.name().toString())._index);
					__dos.writeShort(__addString(
						jnt.type().toString())._index);
					break;
					
					// Reference to other things
				case JITConstantPool.TAG_FIELDREF:
				case JITConstantPool.TAG_METHODREF:
				case JITConstantPool.TAG_INTERFACEMETHODREF:
					JITMemberReference jmr = e.
						<JITMemberReference>get(false,
						JITMemberReference.class);
					__dos.writeShort(__addString(
						jmr.className().toString())._index);
					__dos.writeShort(__addString(
						jmr.memberName().toString())._index);
					__dos.writeShort(__addString(
						jmr.memberType().toString())._index);
					break;
					
					// Unknown
				default:
					throw new RuntimeException("OOPS");
			}
		}
		
		// Align
		while ((__dos.size() & 3) != 0)
			__dos.writeByte(0);
		
		// {@squirreljme.error BA0x The constant pool table starts at a
		// position outside the range of the class size limit.}
		long ctp = __dos.size();
		if (ctp < 0 || ctp > GenericClassWriter._SIZE_LIMIT)
			throw new JITException("BA0x");
		this._poolpos = (int)ctp;
		
		// Write the constant pool pointers
		int nulle = pos[0];
		for (int i = 0; i < n; i++)
		{
			int v = pos[i];
			
			// if active use the given position
			if (v >= 0)
				__dos.writeShort(v);
			
			// Otherwise use the null entry (since it consumes no space)
			else
				__dos.writeShort(nulle);
		}
	}
	
	/**
	 * Writes the string table.
	 *
	 * @param __dos The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/14
	 */
	private void __writeStrings(ExtendedDataOutputStream __dos)
		throws IOException, NullPointerException
	{
		// Check
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// Write out all the strings
		for (__String__ s : this.strings.values())
		{
			// Align
			while ((__dos.size() & 3) != 0)
				__dos.writeByte(0);
			
			// {@squirreljme.error BA0l String position is at an address
			// greater than the class size limit.}
			long at = __dos.size();
			if (at < 0 || at > GenericClassWriter._SIZE_LIMIT)
				throw new JITException("BA0l");
			s._position = (int)at;
			
			// Write string here
			GenericNamespaceWriter.__writeString(__dos, 0, s._string);
		}
		
		// Align
		while ((__dos.size() & 3) != 0)
			__dos.writeByte(0);
		
		// {@squirreljme.error BA0v The string table starts at a position
		// outside the range of the class size limit.}
		long stp = __dos.size();
		if (stp < 0 || stp > GenericClassWriter._SIZE_LIMIT)
			throw new JITException("BA0v");
		this._stringpos = (int)stp;
		
		// Write it out
		Map<String, __String__> strings = this.strings;
		for (__String__ s : strings.values())
			__dos.writeShort(s._position);
	}
	
	/**
	 * String storage holder.
	 *
	 * @since 2016/08/14
	 */
	private class __String__
	{
		/** The string to use. */
		final String _string;
		
		/** The index of the string in the table. */
		final int _index;
		
		/** The position from the start where the string data is. */
		volatile int _position;
		
		/**
		 * Initializes the string 
		 *
		 * @param __s The string which was used.
		 * @param __dx The index of the string.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/08/14
		 */
		private __String__(String __s, int __dx)
			throws NullPointerException
		{
			// Check
			if (__s == null)
				throw new NullPointerException("NARG");
			
			// Set
			this._string = __s;
			this._index = __dx;
		}
	}
}

