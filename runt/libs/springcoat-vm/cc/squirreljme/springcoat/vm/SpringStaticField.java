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
		
		// Initialize value depending on the field type
		Object init;
		switch (__f.nameAndType().type().simpleStorageType())
		{
			case OBJECT:
				init = SpringNullObject.NULL;
				break;
			
			case INTEGER:
				init = Integer.valueOf(0);
				break;
			
			case LONG:
				init = Long.valueOf(0);
				break;
			
			case FLOAT:
				init = Float.valueOf(0);
				break;
			
			case DOUBLE:
				init = Double.valueOf(0);
				break;
			
				// Should not occur
			default:
				throw new RuntimeException("OOPS");
		}
		this._value = init;
	}
	 
	/**
	 * Sets the static field to the given value.
	 *
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/09
	 */
	public void set(Object __v)
		throws NullPointerException
	{
		this.set(__v, false);
	}
	
	/**
	 * Sets the static field to the given value, final may be overridden
	 * potentially.
	 *
	 * @param __v The value to set.
	 * @param __writetofinal If true then final is overridden.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringIncompatibleClassChangeException If the field is final
	 * and we are not allowed to write to final fields.
	 * @since 2018/09/09
	 */
	public void set(Object __v, boolean __writetofinal)
		throws SpringIncompatibleClassChangeException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		SpringField field = this.field;
		
		// Debug
		todo.DEBUG.note("static %s::%s = %s", field.inClass(),
			field.nameAndType(), __v);
		
		// {@squirreljme.error BK0o Attempt to write to final field.}
		FieldFlags flags = field.flags();
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

