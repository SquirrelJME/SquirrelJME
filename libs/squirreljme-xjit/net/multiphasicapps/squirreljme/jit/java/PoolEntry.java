// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import net.multiphasicapps.squirreljme.jit.sym.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.sym.FieldSymbol;
import net.multiphasicapps.squirreljme.jit.sym.IdentifierSymbol;
import net.multiphasicapps.squirreljme.jit.sym.MemberTypeSymbol;
import net.multiphasicapps.squirreljme.jit.sym.MethodSymbol;
import net.multiphasicapps.squirreljme.linkage.FieldReference;
import net.multiphasicapps.squirreljme.linkage.MethodReference;

/**
 * This represents a single constant pool entry that may exist within a class.
 *
 * @since 2016/08/17
 */
public final class PoolEntry
{
	/** The owning pool. */
	protected final Pool pool;
	
	/** The index of this entry. */
	protected final int index;
	
	/** The item tag. */
	final byte _tag;
	
	/** Intialized data. */
	volatile Object _data;
	
	/**
	 * Initializes the entry.
	 *
	 * @param __pool The owning constant pool.
	 * @param __tag The tag identifier.
	 * @param __dx The entry index.
	 * @param __id The initial data for the entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/17
	 */
	PoolEntry(Pool __pool, byte __tag, int __dx, Object __id)
		throws NullPointerException
	{
		// Check
		if (__pool == null || __id == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.pool = __pool;
		this._tag = __tag;
		this.index = __dx;
		this._data = __id;
	}
	
	/**
	 * Obtains the index at the specified position as the given type.
	 *
	 * @param <R> The type of value to get.
	 * @param __cl The expected class type.
	 * @return The value at the given location.
	 * @throws JITException If the input is not of the expected type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public <R> R get(Class<R> __cl)
		throws JITException, NullPointerException
	{
		// Get
		R rv = this.<R>optional(__cl);
		
		// {@squirreljme.error AQ0h No constant pool entry was defined at
		// this position. (The index; The expected type)}
		if (rv == null)
			throw new JITException(String.format("AQ0h %d %s",
				this.index, __cl));
		
		// Ok
		return rv;
	}
	
	/**
	 * Returns the constant pool entry index.
	 *
	 * @return The index of this entry in the constant pool.
	 * @since 2016/08/18
	 */
	public int index()
	{
		return this.index;
	}
	
	/**
	 * Obtains the index at the specified position as the given type, if the
	 * index is zero then {@code null} is returned.
	 *
	 * @param <R> The type of value to get.
	 * @param __cl The expected class type.
	 * @return The value at the given location or {@code null} if zero was
	 * requested.
	 * @throws JITException If the input is not of the expected type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public <R> R optional(Class<R> __cl)
		throws JITException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Zero is always null
		int mydx = this.index;
		if (mydx == 0)
			return null;
		
		// Get raw data
		Object raw = this._data;
		
		// {@squirreljme.error AQ0i The requested entry does not contain a
		// value because it is the top of a long or double constant value.
		// (The index)}
		if (raw == null)
			throw new JITException(String.format("AQ0i %d", mydx));
		
		// If an integer array, requires conversion
		Pool pool = this.pool;
		if (raw instanceof int[])
		{
			// Get input fields
			int[] fields = (int[])raw;
			raw = null;
			
			// Depends on the tag
			byte tag = this._tag;
			switch (tag)
			{
					// Strings
				case Pool.TAG_STRING:
					raw = pool.get(fields[0]).<String>get(String.class);
					break;
					
					// Class name
				case Pool.TAG_CLASS:
					raw = (ClassNameSymbol.of(
						pool.get(fields[0]).<String>get(String.class)));
					break;
					
					// Name and type
				case Pool.TAG_NAMEANDTYPE:
					raw = new __NameAndType__(
						IdentifierSymbol.of(pool.get(fields[0]).<String>get(
							String.class)),
						MemberTypeSymbol.of(pool.get(fields[1]).<String>get(
							String.class)));
					break;
					
					// Field/method/interface reference
				case Pool.TAG_FIELDREF:
				case Pool.TAG_METHODREF:
				case Pool.TAG_INTERFACEMETHODREF:
					ClassNameSymbol rcl = (
						pool.get(fields[0]).<ClassNameSymbol>get(
							ClassNameSymbol.class));
					__NameAndType__ jna = pool.get(fields[1]).
						<__NameAndType__>get(__NameAndType__.class);
					
					// Field?
					if (tag == Pool.TAG_FIELDREF)
						raw = new FieldReference(rcl, jna.name(),
							(FieldSymbol)jna.type());
					
					// Method?
					else
						raw = new MethodReference(rcl, jna.name(),
							(MethodSymbol)jna.type(),
							tag == Pool.TAG_INTERFACEMETHODREF);
					break;
					
					// {@squirreljme.error AQ0j Could not obtain the constant
					// pool entry information because its tag data relation is
					// not known. (The index; The tag type)}
				default:
					throw new JITException(String.format("AQ0j %d %d",
						mydx, tag));
			}
			
			// {@squirreljme.error AQ0k The field data was never translated
			// to known useable data. (The index)}
			if (raw == null)
				throw new NullPointerException(String.format("AQ0k %d", mydx));
			
			// Reset
			this._data = raw;
		}
		
		// {@squirreljme.error AQ0l The value at the given index was not of
		// the expected class type. (The index; The expected type; The type
		// that it was)}
		if (!__cl.isInstance(raw))
			throw new JITException(String.format("AQ0l %d %s", mydx,
				__cl, raw.getClass()));
		
		// Cast
		return __cl.cast(raw);
	}
	
	/**
	 * Returns the tag of the constant.
	 *
	 * @return The constant pool tag.
	 * @since 2016/08/17
	 */
	public int tag()
	{
		return this._tag;
	}
}

