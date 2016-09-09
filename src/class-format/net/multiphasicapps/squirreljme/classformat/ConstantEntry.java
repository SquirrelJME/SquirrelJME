// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MemberTypeSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;

/**
 * This represents a single constant pool entry that may exist within a class.
 *
 * @since 2016/08/17
 */
public final class ConstantEntry
{
	/** The owning pool. */
	protected final ConstantPool pool;
	
	/** The index of this entry. */
	protected final int index;
	
	/** The item tag. */
	final byte _tag;
	
	/** Intialized data. */
	volatile Object _data;
	
	/** The activation index. */
	volatile int _actdx =
		-1;
	
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
	ConstantEntry(ConstantPool __pool, byte __tag, int __dx, Object __id)
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
	 * @param __act Should the given constant pool entry be activated?
	 * @param __cl The expected class type.
	 * @return The value at the given location.
	 * @throws ClassFormatException If the input is not of the expected type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public <R> R get(boolean __act, Class<R> __cl)
		throws ClassFormatException, NullPointerException
	{
		// Get
		R rv = this.<R>optional(__act, __cl);
		
		// {@squirreljme.error AY0b No constant pool entry was defined at
		// this position. (The index; The expected type)}
		if (rv == null)
			throw new ClassFormatException(String.format("AY0b %d %s", this.index,
				__cl));
		
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
	 * Has this entry been activated?
	 *
	 * @return {@code true} if it has been activated.
	 * @since 2016/08/17
	 */
	public boolean isActive()
	{
		return this._actdx >= 0;
	}
	
	/**
	 * Obtains the index at the specified position as the given type, if the
	 * index is zero then {@code null} is returned.
	 *
	 * @param <R> The type of value to get.
	 * @param __act Should the given constant pool entry be activated?
	 * @param __cl The expected class type.
	 * @return The value at the given location or {@code null} if zero was
	 * requested.
	 * @throws ClassFormatException If the input is not of the expected type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public <R> R optional(boolean __act, Class<R> __cl)
		throws ClassFormatException, NullPointerException
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
		
		// {@squirreljme.error AY0d The requested entry does not contain a
		// value because it is the top of a long or double constant value.
		// (The index)}
		if (raw == null)
			throw new ClassFormatException(String.format("AY0d %d", mydx));
		
		// If an integer array, requires conversion
		ConstantPool pool = this.pool;
		ClassDecoder decoder = pool._decoder;
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
				case ConstantPool.TAG_STRING:
					raw = pool.get(fields[0]).<String>get(false, String.class);
					break;
					
					// Class name
				case ConstantPool.TAG_CLASS:
					raw = (ClassNameSymbol.of(
						pool.get(fields[0]).<String>get(false, String.class)));
					break;
					
					// Name and type
				case ConstantPool.TAG_NAMEANDTYPE:
					raw = new MemberNameAndType(
						IdentifierSymbol.of(pool.get(fields[0]).<String>get(
							false, String.class)),
						MemberTypeSymbol.of(pool.get(fields[1]).<String>get(
							false, String.class)));
					break;
					
					// Field/method/interface reference
				case ConstantPool.TAG_FIELDREF:
				case ConstantPool.TAG_METHODREF:
				case ConstantPool.TAG_INTERFACEMETHODREF:
					ClassNameSymbol rcl = (
						pool.get(fields[0]).<ClassNameSymbol>get(false,
							ClassNameSymbol.class));
					MemberNameAndType jna = pool.get(fields[1]).
						<MemberNameAndType>get(false, MemberNameAndType.class);
					
					// Field?
					if (tag == ConstantPool.TAG_FIELDREF)
						raw = new FieldReference(rcl, jna.name(),
							(FieldSymbol)jna.type());
					
					// Method?
					else
						raw = new MethodReference(rcl, jna.name(),
							(MethodSymbol)jna.type(),
							tag == ConstantPool.TAG_INTERFACEMETHODREF);
					break;
					
					// {@squirreljme.error AY0f Could not obtain the constant
					// pool entry information because its tag data relation is
					// not known. (The index; The tag type)}
				default:
					throw new ClassFormatException(String.format("AY0f %d %d", mydx,
						tag));
			}
			
			// {@squirreljme.error AY0g The field data was never translated
			// to known useable data. (The index)}
			if (raw == null)
				throw new NullPointerException(String.format("AY0g %d", mydx));
			
			// Reset
			this._data = raw;
		}
		
		// {@squirreljme.error AY0e The value at the given index was not of
		// the expected class type. (The index; The expected type; The type
		// that it was)}
		if (!__cl.isInstance(raw))
			throw new ClassFormatException(String.format("AY0e %d %s", mydx, __cl,
				raw.getClass()));
		
		// Activate?
		if (__act && this._actdx < 0)
			this._actdx = pool._nextadx++;
		
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

