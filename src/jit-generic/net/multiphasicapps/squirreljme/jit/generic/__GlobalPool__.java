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
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.JITConstantPool;
import net.multiphasicapps.squirreljme.os.generic.ConstantTagType;
import net.multiphasicapps.squirreljme.os.generic.GenericBlob;

/**
 * This is the global constant pool which is shared among all classes within
 * a single namespace.
 *
 * @since 2016/08/17
 */
final class __GlobalPool__
{
	/** The owning namespace writer. */
	protected final GenericNamespaceWriter owner;
	
	/** Strings in the namespace. */
	private final Map<String, __StringEntry__> _strings =
		new LinkedHashMap<>();
	
	/** Global constant pool. */
	private final Map<Object, __GlobalEntry__> _entries =
		new LinkedHashMap<>();
	
	/** The currently active pool. */
	private volatile JITConstantPool _current;
	
	/** The string table position. */
	volatile int _stringpos;
	
	/** The string table count. */
	volatile int _stringcount;
	
	/** The constant pool position. */
	volatile int _poolpos;
	
	/** The constant pool count. */
	volatile int _poolcount;
	
	/** The constant pool tag table. */
	volatile int _tagpos;
	
	/**
	 * Initializes the global pool.
	 *
	 * @param __nsw The namespace writer.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/17
	 */
	__GlobalPool__(GenericNamespaceWriter __nsw)
		throws NullPointerException
	{
		// Check
		if (__nsw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __nsw;
		
		// The first constant is magically null (for super class)
		this._entries.put(null, new __GlobalEntry__(0));
	}
	
	/**
	 * Since all external globals are virtually represented in the same object
	 * mapping this performs the same work for each of them.
	 *
	 * @param __str Load the object as a string also?
	 * @param __o The object to map.
	 * @return The mapped object.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	private __GlobalEntry__ __internalLoad(boolean __str, Object __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Load as a string?
		if (__str)
			__loadString(__o.toString());
		
		// Already placed?
		Map<Object, __GlobalEntry__> entries = this._entries;
		__GlobalEntry__ rv = entries.get(__o);
		if (rv != null)
			return rv;
		
		// {@squirreljme.error BA16 The number of constant pool entries exceeds
		// 65,536.}
		int sz = entries.size();
		if (sz >= 65535)
			throw new JITException("BA16");
		
		// Place it otherwise
		entries.put(__o, (rv = new __GlobalEntry__(sz)));
		return rv;
	}
	
	/**
	 * Loads a class that was named.
	 *
	 * @param __n The name of the class to load.
	 * @return The entry for the given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	__GlobalEntry__ __loadClass(ClassNameSymbol __n)
		throws NullPointerException
	{
		return __internalLoad(true, __n);
	}
	
	/**
	 * Loads a string into the string table.
	 *
	 * @param __s The string to load.
	 * @return The representing string entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/17
	 */
	__StringEntry__ __loadString(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Already placed?
		Map<String, __StringEntry__> strings = this._strings;
		__StringEntry__ rv = strings.get(__s);
		if (rv != null)
			return rv;
		
		// {@squirreljme.error BA17 The number of strings exceeds 65,536.}
		int sz = strings.size();
		if (sz >= 65535)
			throw new JITException("BA17");
		
		// Place it otherwise
		strings.put(__s, (rv = new __StringEntry__(sz)));
		return rv;
	}
	
	/**
	 * Sets the current constant pool.
	 *
	 * @param __pool The pool to use.
	 * @since 2016/08/17
	 */
	void __setCurrent(JITConstantPool __pool)
	{
		this._current = __pool;
	}
	
	/**
	 * Writes the string table and the constant pool table.
	 *
	 * @param __dos The stream to write to.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	void __write(ExtendedDataOutputStream __dos)
		throws IOException, NullPointerException
	{
		// Check
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// Align
		GenericNamespaceWriter owner = this.owner;
		owner.__align();
		
		// Write strings
		__writeStrings(__dos);
		
		// Align
		owner.__align();
		
		// Write constants
		__writeConstants(__dos);
	}
	
	/**
	 * Writes the constant data.
	 *
	 * @param __dos The target stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	private void __writeConstants(ExtendedDataOutputStream __dos)
		throws IOException, NullPointerException
	{
		// Check
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// Will constantly align data
		GenericNamespaceWriter owner = this.owner;
		
		// Write the constant data
		Map<Object, __GlobalEntry__> entries = this._entries;
		int cn = entries.size();
		this._poolcount = cn;
		int[] cpos = new int[cn];
		int[] tags = new int[cn];
		int at = 0;
		for (Map.Entry<Object, __GlobalEntry__> e : entries.entrySet())
		{
			// Get
			Object data = e.getKey();
			
			// Align and position
			owner.__align();
			tags[at] = (data == null ? 0 :
				ConstantTagType.ofClass(data.getClass()).ordinal());
			cpos[at++] = (int)__dos.size();
			
			// No data to write
			if (data == null)
				;
			
			// A simple and single string reference
			else if ((data instanceof ClassNameSymbol) ||
				(data instanceof String))
				__dos.writeShort(__loadString(data.toString())._index);
			
			// {@squirreljme.error BA19 Do not know how to record an object
			// of the specified type. (The data class type)}
			else
				throw new JITException(String.format("BA19 %s",
					data.getClass()));
		}
		
		// Align
		owner.__align();
		
		// Write the tag table
		this._tagpos = (int)__dos.size();
		for (int i = 0; i < cn; i++)
			__dos.writeByte(tags[i]);
		
		// Align
		owner.__align();
		
		// Write the constant pool table
		this._poolpos = (int)__dos.size();
		int ns = GenericBlob.NAMESPACE_SHIFT;
		for (int i = 0; i < cn; i++)
			__dos.writeShort(cpos[i] >>> ns);
	}
	
	/**
	 * Writes the string data.
	 *
	 * @param __dos The target stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	private void __writeStrings(ExtendedDataOutputStream __dos)
		throws IOException, NullPointerException
	{
		// Check
		if (__dos == null)
			throw new NullPointerException("NARG");
		
		// Needed to align
		GenericNamespaceWriter owner = this.owner;
		
		// Write the string data
		Map<String, __StringEntry__> strings = this._strings;
		int sn = strings.size();
		this._stringcount = sn;
		int[] spos = new int[sn];
		int at = 0;
		for (Map.Entry<String, __StringEntry__> e : strings.entrySet())
		{
			// Align and position
			owner.__align();
			spos[at++] = (int)__dos.size();
			
			// Write string data
			owner.__writeString(__dos, 0, e.getKey());
		}
		
		// Align
		owner.__align();
		
		// Write the string table
		this._stringpos = (int)__dos.size();
		int ns = GenericBlob.NAMESPACE_SHIFT;
		for (int i = 0; i < sn; i++)
			__dos.writeShort(spos[i] >>> ns);
	}
}

