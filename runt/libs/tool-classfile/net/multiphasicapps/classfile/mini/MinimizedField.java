// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.mini;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.classfile.FieldDescriptor;
import net.multiphasicapps.classfile.FieldName;

/**
 * This represents a field which has been minimized.
 *
 * @since 2019/03/11
 */
public final class MinimizedField
{
	/** The flags for this field. */
	public final int flags;
	
	/** Offset to the field data. */
	public final int offset;
	
	/** The size of this field. */
	public final int size;
	
	/** The field name. */
	public final FieldName name;
	
	/** The field type. */
	public final FieldDescriptor type;
	
	/** The field value. */
	public final Object value;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the minimized field.
	 *
	 * @param __f The field flags.
	 * @param __o The offset.
	 * @param __s The size.
	 * @param __n The name of the field.
	 * @param __t The type of the field.
	 * @param __v The value of this field.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/11
	 */
	public MinimizedField(int __f, int __o, int __s, FieldName __n,
		FieldDescriptor __t, Object __v)
		throws NullPointerException
	{
		if (__n == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.flags = __f;
		this.offset = __o;
		this.size = __s;
		this.name = __n;
		this.type = __t;
		this.value = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/11
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"Field %s:%s @ %d (%d bytes), flags=%x, value=%s", this.name,
				this.type, this.offset, this.size, this.flags, this.value)));
		
		return rv;
	}
}

