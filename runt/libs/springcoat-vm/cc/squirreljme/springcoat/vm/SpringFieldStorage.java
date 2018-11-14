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

import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValue;
import net.multiphasicapps.classfile.FieldFlags;
import net.multiphasicapps.classfile.FieldNameAndType;

/**
 * Contains storage and other information for fields.
 *
 * @since 2018/09/09
 */
public final class SpringFieldStorage
{
	/** The class this is in. */
	protected final ClassName inclass;
	
	/** Name and type of the field. */
	protected final FieldNameAndType nameandtype;
	
	/** Is this volatile? */
	protected final boolean isvolatile;
	
	/** Is this final? */
	protected final boolean isfinal;
	
	/** The value of the field. */
	private Object _normalvalue;
	
	/** The volatile value of the field. */
	private volatile Object _volatilevalue;
	
	/**
	 * Initializes the static field.
	 *
	 * @param __f The field to store for.
	 * @throws NullPointerException On null arguments.
	 * @since 2108/09/09
	 */
	SpringFieldStorage(SpringField __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Used for debug
		FieldNameAndType nameandtype;
		this.inclass = __f.inClass();
		this.nameandtype = (nameandtype = __f.nameAndType());
		
		// Initialize value depending on the field type
		Object init;
		switch (nameandtype.type().simpleStorageType())
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
		
		// If the field starts with a constant, it must be initialized
		ConstantValue cv = __f.field.constantValue();
		if (cv != null)
			init = cv.boxedValue();
		
		// Set initial value
		if ((this.isvolatile = __f.flags().isVolatile()))
			this._volatilevalue = init;
		else
			this._normalvalue = init;
		
		this.isfinal = __f.flags().isFinal();
	}
	
	/**
	 * Returns the value of the field.
	 *
	 * @return The field value.
	 * @since 2018/09/15
	 */
	public final Object get()
	{
		// Volatile field, use volatile field instead
		if (this.isvolatile)
			return this._volatilevalue;
		
		// Otherwise just set thread without worrying about any contention
		else
			return this._normalvalue;
	}
	 
	/**
	 * Sets the static field to the given value.
	 *
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/09
	 */
	public final void set(Object __v)
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
	public final void set(Object __v, boolean __writetofinal)
		throws SpringIncompatibleClassChangeException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Debug
		/*todo.DEBUG.note("%s::%s = %s", this.inclass, this.nameandtype,
			__v);*/
		
		// {@squirreljme.error BK0t Attempt to write to final field.}
		if (this.isfinal && !__writetofinal)
			throw new SpringIllegalAccessException("BK0t");
		
		// Volatile field, use volatile field instead
		if (this.isvolatile)
			this._volatilevalue = __v;
		
		// Otherwise just set thread without worrying about any contention
		else
			this._normalvalue = __v;
	}
}

