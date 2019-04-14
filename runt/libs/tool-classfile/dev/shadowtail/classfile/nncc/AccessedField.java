// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.FieldReference;

/**
 * This represents a field which has been accessed.
 *
 * @since 2019/03/24
 */
public final class AccessedField
{
	/** The field reference. */
	protected final FieldReference field;
	
	/** The access time. */
	protected final FieldAccessTime time;
	
	/** The access type. */
	protected final FieldAccessType type;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the accessed field.
	 *
	 * @param __ti The access time.
	 * @param __ty The access type.
	 * @param __f The field to access.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	public AccessedField(FieldAccessTime __ti, FieldAccessType __ty,
		FieldReference __f)
		throws NullPointerException
	{
		if (__ti == null || __ty == null || __f == null)
			throw new NullPointerException("NARG");
		
		this.time = __ti;
		this.type = __ty;
		this.field = __f;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof AccessedField))
			return false;
		
		AccessedField o = (AccessedField)__o;
		return this.field.equals(o.field) &&
			this.time.equals(o.time) &&
			this.type.equals(o.type);
	}
	
	/**
	 * Returns the field reference.
	 *
	 * @return The field reference.
	 * @since 2019/03/26
	 */
	public final FieldReference field()
	{
		return this.field;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public final int hashCode()
	{
		return this.field.hashCode() ^ this.time.hashCode() ^
			this.type.hashCode();
	}
	
	/**
	 * Returns the field access time.
	 *
	 * @return The access time.
	 * @since 2019/04/14
	 */
	public final FieldAccessTime time()
	{
		return this.time;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				this.time + "+" + this.type + "+" + this.field));
		
		return rv;
	}
	
	/**
	 * Returns the field access type.
	 *
	 * @return The access type.
	 * @since 2019/04/14
	 */
	public final FieldAccessType type()
	{
		return this.type;
	}
}

