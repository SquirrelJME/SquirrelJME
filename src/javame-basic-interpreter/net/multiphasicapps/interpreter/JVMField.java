// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import net.multiphasicapps.classfile.CFField;
import net.multiphasicapps.classfile.CFFieldFlags;
import net.multiphasicapps.classprogram.CPVariableType;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.PrimitiveSymbol;

/**
 * This represents a virtual machine field.
 *
 * @since 2016/04/04
 */
public class JVMField
	extends JVMMember<FieldSymbol, CFFieldFlags, CFField, JVMField>
{
	/** The static field value. */
	protected final JVMDataStore.Window svalue;
	
	/**
	 * Initializes the field.
	 *
	 * @param __o The owning group.
	 * @param __b The base for it.
	 * @since 2016/04/05
	 */
	JVMField(JVMFields __o, CFField __b)
	{
		super(__o, __b);
		
		// Is this a static field?
		if (flags().isStatic())
		{
			// Get the type used
			CPVariableType type = CPVariableType.bySymbol(type());
			
			// If wide, allocate 2, otherwise just one
			svalue = outerClass().staticData().pushWindow(
				(type.isWide() ? 2 : 1));
			
			// Handle constant value
			Object cons = base.getConstantValue();
			if (cons != null)
				svalue.set(0, cons);
		}
		
		// Not static
		else
			svalue = null;
	}
	
	/**
	 * Obtains the value of this static field.
	 *
	 * @return The value of this static field.
	 * @throws JVMEngineException If this field is not static.
	 * @since 2016/04/16
	 */
	public Object getStaticValue()
		throws JVMEngineException
	{
		// {@squirreljme.error IN0z Cannot get static value because this field
		// is not static. (This field)}
		JVMDataStore.Window var = svalue;
		if (var == null)
			throw new JVMEngineException(null, String.format("IN0z %s", this));
		
		// Lock
		synchronized (var)
		{
			return var.get(0);
		}
	}
	
	/**
	 * Sets the value of this static field.
	 *
	 * @param __v The value to set.
	 * @return The old value.
	 * @throws JVMEngineException If this field is not static.
	 * @since 2016/04/16
	 */
	public Object setStaticValue(Object __v)
		throws JVMEngineException
	{
		// {@squirreljme.error IN10 Cannot put static value because this field
		// is not static. (This field)}
		JVMDataStore.Window var = svalue;
		if (var == null)
			throw new JVMEngineException(null, String.format("IN10 %s", this));
		
		// Lock
		synchronized (var)
		{
			return var.set(0, __v);
		}
	}
}

