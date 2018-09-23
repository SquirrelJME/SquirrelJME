// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a single entry within the stack map table which may
 * additionally has a flag indicating if an entry is initialized or not.
 *
 * @since 2017/09/02
 */
public final class StackMapTableEntry
{
	/** The top of a long. */
	public static final StackMapTableEntry TOP_LONG =
		new StackMapTableEntry(JavaType.TOP_LONG, true);
	
	/** The top of a double. */
	public static final StackMapTableEntry TOP_DOUBLE =
		new StackMapTableEntry(JavaType.TOP_DOUBLE, true);
	
	/** Undefined top type. */
	public static final StackMapTableEntry TOP_UNDEFINED =
		new StackMapTableEntry(JavaType.TOP_UNDEFINED, true);
	
	/** Not used. */
	public static final StackMapTableEntry NOTHING =
		new StackMapTableEntry(JavaType.NOTHING, false);
	
	/** Integer. */
	public static final StackMapTableEntry INTEGER =
		new StackMapTableEntry(JavaType.INTEGER, true);
	
	/** Long. */
	public static final StackMapTableEntry LONG =
		new StackMapTableEntry(JavaType.LONG, true);
	
	/** Float. */
	public static final StackMapTableEntry FLOAT =
		new StackMapTableEntry(JavaType.FLOAT, true);
	
	/** Double. */
	public static final StackMapTableEntry DOUBLE =
		new StackMapTableEntry(JavaType.DOUBLE, true);
	
	/** The type. */
	protected final JavaType type;
	
	/** Is this type initialized? */
	protected final boolean isinitialized;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the stack map entry.
	 *
	 * @param __t The type of variable to store.
	 * @param __init If {@code true} this variable is initialized.
	 * @throws InvalidClassFormatException If a non-object is set as not
	 * initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/02
	 */
	public StackMapTableEntry(JavaType __t, boolean __init)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Nothing can never be initialized
		if (__t.equals(JavaType.NOTHING))
		{
			// {@squirreljme.error JC1j The nothing type cannot be initialized.
			// (The type)}
			if (__init && __t.equals(JavaType.NOTHING))
				throw new InvalidClassFormatException(
					String.format("JI3w %s", __t));
		}
		
		// Otherwise only objects may be initialized
		else
		{
			// {@squirreljme.error JC1k Non-object types cannot be.
			// uninitialized (The type)}
			if (!__init && !__t.isObject())
				throw new InvalidClassFormatException(
					String.format("JC1k %s", __t));
		}
		
		// Set
		this.type = __t;
		this.isinitialized = __init;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/02
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof StackMapTableEntry))
			return false;
		
		StackMapTableEntry o = (StackMapTableEntry)__o;
		return this.type.equals(o.type) &&
			this.isinitialized == o.isinitialized;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/02
	 */
	@Override
	public int hashCode()
	{
		return this.type.hashCode() ^ (this.isinitialized ? 1 : 0);
	}
	
	/**
	 * Has this type been initialized?
	 *
	 * @return {@code true} if this type was initialized.
	 * @since 2017/08/13
	 */
	public boolean isInitialized()
	{
		return this.isinitialized;
	}
	
	/**
	 * Is this a top type?
	 *
	 * @return If this is a top type.
	 * @since 2018/09/23
	 */
	public final boolean isTop()
	{
		JavaType type = this.type;
		return type != null && type.isTop();
	}
	
	/**
	 * Does this represent a wide type?
	 *
	 * @return If this is a wide type.
	 * @since 2017/10/16
	 */
	public boolean isWide()
	{
		JavaType t = this.type;
		return t != null && t.isWide();
	}
	
	/**
	 * Returns the top type for this entry.
	 *
	 * @return The top type or {@code null} if there is none.
	 * @since 2018/09/22
	 */
	public final StackMapTableEntry topType()
	{
		if (this.equals(StackMapTableEntry.LONG))
			return StackMapTableEntry.TOP_LONG;
		else if (this.equals(StackMapTableEntry.DOUBLE))
			return StackMapTableEntry.TOP_DOUBLE;
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/02
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format("%s%s",
				(this.isinitialized ? "" : "!"), this.type)));
		
		return rv;
	}
	
	/**
	 * Returns the type.
	 *
	 * @return The type.
	 * @since 2017/09/02
	 */
	public JavaType type()
	{
		return this.type;
	}
}

