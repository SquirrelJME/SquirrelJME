// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import net.multiphasicapps.classfile.FieldFlags;

/**
 * Contains storage and other information for static fields.
 *
 * @since 2018/09/09
 */
public final class SpringStaticField
{
	/** The field this stores information for. */
	protected final SpringField field;
	
	/** The value of the field. */
	private Object _value;
	
	/**
	 * Initializes the static field.
	 *
	 * @param __f The field to store for.
	 * @throws NullPointerException On null arguments.
	 * @since 2108/09/09
	 */
	SpringStaticField(SpringField __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		this.field = __f;
	}
	 
	/**
	 * Sets the static field to the given value.
	 *
	 * @param __v The value to set.
	 * @since 2018/09/09
	 */
	public void set(Object __v)
	{
		this.set(__v, false);
	}
	
	/**
	 * Sets the static field to the given value, final may be overridden
	 * potentially.
	 *
	 * @param __v The value to set.
	 * @param __writetofinal If true then final is overridden.
	 * @throws SpringIncompatibleClassChangeException If the field is final
	 * and we are not allowed to write to final fields.
	 * @since 2018/09/09
	 */
	public void set(Object __v, boolean __writetofinal)
		throws SpringIncompatibleClassChangeException
	{
		// {@squirreljme.error BK0o Attempt to write to final field.}
		FieldFlags flags = this.field.flags();
		if (flags.isFinal() && !__writetofinal)
			throw new SpringIncompatibleClassChangeException("BK0o");
		
		// Volatile field, only a single thread may access at a time
		if (flags.isVolatile())
			synchronized (this)
			{
				this._value = __v;
			}
		
		// Otherwise just set thread without worrying about any contention
		else
			this._value = __v;
	}
}

