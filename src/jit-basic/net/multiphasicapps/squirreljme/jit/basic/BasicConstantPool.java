// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This represents the shared constant pool which all basic namespaces use to
 * store specific constant values.
 *
 * @since 2016/09/11
 */
public class BasicConstantPool
{
	/** Stores objects that are stored in the pool. */
	private final Map<Object, BasicConstantEntry<Object>> _objects =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the basic pool.
	 *
	 * @since 2016/09/11
	 */
	BasicConstantPool()
	{
	}
	
	/**
	 * Adds the name of a class to the constant pool.
	 *
	 * @param __cn The name of the class.
	 * @return The entry for the given class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	public BasicConstantEntry<ClassNameSymbol> addClassName(
		ClassNameSymbol __cn)
		throws NullPointerException
	{
		return this.<ClassNameSymbol>__storeToString(__cn);
	}
	
	/**
	 * Adds the specified identifier to the constant pool.
	 *
	 * @param __i The identifier to add.
	 * @return The entry for the given identifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	public BasicConstantEntry<IdentifierSymbol> addIdentifier(
		IdentifierSymbol __i)
		throws NullPointerException
	{
		return this.<IdentifierSymbol>__storeToString(__i);
	}
	
	/**
	 * Adds the specified method symbol to the constant pool.
	 *
	 * @param __m The method symbol to add.
	 * @return The entry for the given The method symbol.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	public BasicConstantEntry<MethodSymbol> addMethodSymbol(MethodSymbol __m)
		throws NullPointerException
	{
		return this.<MethodSymbol>__storeToString(__m);
	}
	
	/**
	 * Adds a string to the constant pool.
	 *
	 * @param __s The string to add.
	 * @return The constant entry for the string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	public BasicConstantEntry<String> addString(String __s)
		throws NullPointerException
	{
		return this.<String>__store(__s, (BasicConstantEntry<?>[])null);
	}
	
	/**
	 * Internally gets an entry that already exists, otherwise it creates a
	 * entry to store the value.
	 *
	 * @param <T> The type of value to store.
	 * @param __v The value to store.
	 * @param __o Other references to base off.
	 * @return The entry for the value.
	 * @throws NullPointerException On null arguments, except for {@code __o}.
	 * @since 2016/09/11
	 */
	@SuppressWarnings({"unchecked"})
	private <T> BasicConstantEntry<T> __store(T __v,
		BasicConstantEntry<?>... __o)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Get entry, it may be already in the pool
		Map<Object, BasicConstantEntry<Object>> objects = this._objects;
		BasicConstantEntry<Object> rv = objects.get(__v);
		
		// Does not exist
		if (rv == null)
			objects.put(__v, (rv = new BasicConstantEntry<Object>(
				objects.size(), __v, __o)));
		
		// Return it
		return (BasicConstantEntry<T>)rv;
	}
	
	/**
	 * Adds an entry to the constant pool where the extra argument is just
	 * a {@code toString()} of the value.
	 *
	 * @param <T> The type to add.
	 * @param __v The value to add.
	 * @return The entry for this value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/14
	 */
	private <T> BasicConstantEntry<T> __storeToString(T __v)
		throws NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		return this.<T>__store(__v, addString(__v.toString()));
	}
}

