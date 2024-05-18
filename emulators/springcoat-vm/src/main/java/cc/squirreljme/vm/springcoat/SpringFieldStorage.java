// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.exceptions.SpringIllegalAccessException;
import cc.squirreljme.vm.springcoat.exceptions.SpringIncompatibleClassChangeException;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValue;
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
	
	/** Is this final? */
	protected final boolean isFinal;
	
	/** The field index. */
	protected final int fieldIndex;
	
	/** The volatile value of the field. */
	private volatile Object _volatile;
	
	/**
	 * Initializes the static field.
	 *
	 * @param __f The field to store for.
	 * @param __fieldDx The field index.
	 * @throws NullPointerException On null arguments.
	 * @since 2108/09/09
	 */
	SpringFieldStorage(SpringField __f, int __fieldDx)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Used for debug
		FieldNameAndType nameandtype;
		this.fieldIndex = __fieldDx;
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
				throw Debugging.oops();
		}
		
		// If the field starts with a constant, it must be initialized
		// But this is only considered for static variables
		ConstantValue cv = __f.field.constantValue();
		if (cv != null && __f.flags().isStatic())
			init = cv.boxedValue();
		
		// Set initial value
		synchronized (this)
		{
			this._volatile = init;
		}
		
		this.isFinal = __f.flags().isFinal();
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
		// Otherwise just set thread without worrying about any contention
		synchronized (this)
		{
			return this._volatile;
		}
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
	 * @param __writeFinal If true then final is overridden.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringIncompatibleClassChangeException If the field is final
	 * and we are not allowed to write to final fields.
	 * @since 2018/09/09
	 */
	public final void set(Object __v, boolean __writeFinal)
		throws SpringIncompatibleClassChangeException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Storing something that should not go into a field?
		if (!(__v instanceof SpringObject) &&
			!(__v instanceof Boolean) &&
			!(__v instanceof Integer) &&
			!(__v instanceof Long) &&
			!(__v instanceof Float) &&
			!(__v instanceof Double))
			throw new IllegalArgumentException(String.format(
				"Attempting to store %s (a %s)?", __v, __v.getClass()));
		
		// Debug
		/*todo.DEBUG.note("%s::%s = %s", this.inclass, this.nameandtype,
			__v);*/
		
		/* {@squirreljme.error BK18 Attempt to write to final field.} */
		if (this.isFinal && !__writeFinal)
			throw new SpringIllegalAccessException("BK18");
		
		// Volatile field, use volatile field instead
		synchronized (this)
		{
			this._volatile = __v;
		}
	}
}

