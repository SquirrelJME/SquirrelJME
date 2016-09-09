// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.symbols.IdentifierSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MemberTypeSymbol;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This represents a single constant pool entry that may exist within a class.
 *
 * @since 2016/08/17
 */
public final class JITConstantEntry
{
	/** The owning pool. */
	protected final JITConstantPool pool;
	
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
	JITConstantEntry(JITConstantPool __pool, byte __tag, int __dx, Object __id)
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
	 * @throws JITException If the input is not of the expected type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public <R> R get(boolean __act, Class<R> __cl)
		throws JITException, NullPointerException
	{
		// Get
		R rv = this.<R>optional(__act, __cl);
		
		// {@squirreljme.error ED0b No constant pool entry was defined at
		// this position. (The index; The expected type)}
		if (rv == null)
			throw new JITException(String.format("ED0b %d %s", this.index,
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
	 * @throws JITException If the input is not of the expected type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/06
	 */
	public <R> R optional(boolean __act, Class<R> __cl)
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
		
		// {@squirreljme.error ED0d The requested entry does not contain a
		// value because it is the top of a long or double constant value.
		// (The index)}
		if (raw == null)
			throw new JITException(String.format("ED0d %d", mydx));
		
		// If an integer array, requires conversion
		JITConstantPool pool = this.pool;
		__ClassDecoder__ decoder = pool._decoder;
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
				case JITConstantPool.TAG_STRING:
					raw = pool.get(fields[0]).<String>get(false, String.class);
					break;
					
					// Class name
				case JITConstantPool.TAG_CLASS:
					raw = decoder.__rewriteClass(ClassNameSymbol.of(
						pool.get(fields[0]).<String>get(false, String.class)));
					break;
					
					// Name and type
				case JITConstantPool.TAG_NAMEANDTYPE:
					raw = new JITNameAndType(
						IdentifierSymbol.of(pool.get(fields[0]).<String>get(
							false, String.class)),
						MemberTypeSymbol.of(pool.get(fields[1]).<String>get(
							false, String.class)));
					break;
					
					// Field/method/interface reference
				case JITConstantPool.TAG_FIELDREF:
				case JITConstantPool.TAG_METHODREF:
				case JITConstantPool.TAG_INTERFACEMETHODREF:
					ClassNameSymbol rcl = decoder.__rewriteClass(
						pool.get(fields[0]).<ClassNameSymbol>get(false,
							ClassNameSymbol.class));
					JITNameAndType jna = pool.get(fields[1]).
						<JITNameAndType>get(false, JITNameAndType.class);
					
					// Field?
					if (tag == JITConstantPool.TAG_FIELDREF)
						raw = new ClassFieldReference(rcl, jna.name(),
							(FieldSymbol)jna.type());
					
					// Method?
					else
						raw = new ClassMethodReference(rcl, jna.name(),
							(MethodSymbol)jna.type(),
							tag == JITConstantPool.TAG_INTERFACEMETHODREF);
					break;
					
					// {@squirreljme.error ED0f Could not obtain the constant
					// pool entry information because its tag data relation is
					// not known. (The index; The tag type)}
				default:
					throw new JITException(String.format("ED0f %d %d", mydx,
						tag));
			}
			
			// {@squirreljme.error ED0g The field data was never translated
			// to known useable data. (The index)}
			if (raw == null)
				throw new NullPointerException(String.format("ED0g %d", mydx));
			
			// Reset
			this._data = raw;
		}
		
		// {@squirreljme.error ED0e The value at the given index was not of
		// the expected class type. (The index; The expected type; The type
		// that it was)}
		if (!__cl.isInstance(raw))
			throw new JITException(String.format("ED0e %d %s", mydx, __cl,
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

