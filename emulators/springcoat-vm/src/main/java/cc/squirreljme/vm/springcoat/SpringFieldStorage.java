// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jdwp.JDWPTripValue;
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
	
	/** Is this volatile? */
	protected final boolean isvolatile;
	
	/** Is this final? */
	protected final boolean isfinal;
	
	/** The field index. */
	protected final int fieldIndex;
	
	/** The value of the field. */
	private Object _normal;
	
	/** The volatile value of the field. */
	private volatile Object _volatile;
	
	/** The trip used for debugging. */
	volatile JDWPTripValue _tripValue;
	
	/**
	 * Initializes the static field.
	 *
	 * @param __f The field to store for.
	 * @param __fieldDx
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
				throw new todo.OOPS();
		}
		
		// If the field starts with a constant, it must be initialized
		ConstantValue cv = __f.field.constantValue();
		if (cv != null)
			init = cv.boxedValue();
		
		// Set initial value
		if ((this.isvolatile = __f.flags().isVolatile()))
			this._volatile = init;
		else
			this._normal = init;
		
		this.isfinal = __f.flags().isFinal();
	}
	
	/**
	 * Returns the value of the field.
	 *
	 * @param __ctxThread The context thread.
	 * @param __ctxRef The context reference.
	 * @return The field value.
	 * @since 2018/09/15
	 */
	public final Object get(SpringThread __ctxThread, Object __ctxRef)
	{
		// Volatile field, use volatile field instead
		// Otherwise just set thread without worrying about any contention
		Object rv = (this.isvolatile ? this._volatile : this._normal);
		
		// Are we debug tripping on this read?
		JDWPTripValue trip = this._tripValue;
		if (__ctxThread != null && trip != null && trip.isRead())
			trip.signalTrip(__ctxThread, __ctxRef, this.fieldIndex);
		
		return rv;
	}
	 
	/**
	 * Sets the static field to the given value.
	 *
	 * @param __ctxThread Context thread.
	 * @param __ctxRef The context value.
	 * @param __v The value to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/09
	 */
	public final void set(SpringThread __ctxThread, Object __ctxRef,
		Object __v)
		throws NullPointerException
	{
		this.set(__ctxThread, __ctxRef, __v, false);
	}
	
	/**
	 * Sets the static field to the given value, final may be overridden
	 * potentially.
	 *
	 * @param __ctxThread Context thread.
	 * @param __ctxRef The context reference.
	 * @param __v The value to set.
	 * @param __writeFinal If true then final is overridden.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringIncompatibleClassChangeException If the field is final
	 * and we are not allowed to write to final fields.
	 * @since 2018/09/09
	 */
	public final void set(SpringThread __ctxThread, Object __ctxRef,
		Object __v, boolean __writeFinal)
		throws SpringIncompatibleClassChangeException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Debug
		/*todo.DEBUG.note("%s::%s = %s", this.inclass, this.nameandtype,
			__v);*/
		
		// {@squirreljme.error BK18 Attempt to write to final field.}
		if (this.isfinal && !__writeFinal)
			throw new SpringIllegalAccessException("BK18");
		
		// Volatile field, use volatile field instead
		if (this.isvolatile)
			this._volatile = __v;
		
		// Otherwise just set thread without worrying about any contention
		else
			this._normal = __v;
		
		// Are we debug tripping on this write?
		JDWPTripValue trip = this._tripValue;
		if (__ctxThread != null && trip != null && trip.isWrite())
			trip.signalTrip(__ctxThread, __ctxRef, this.fieldIndex);
	}
}

