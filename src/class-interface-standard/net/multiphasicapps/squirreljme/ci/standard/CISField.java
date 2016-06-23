// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci.standard;

import net.multiphasicapps.squirreljme.java.symbols.FieldSymbol;
import net.multiphasicapps.squirreljme.java.ci.CIException;
import net.multiphasicapps.squirreljme.java.ci.CIField;
import net.multiphasicapps.squirreljme.java.ci.CIFieldFlags;
import net.multiphasicapps.squirreljme.java.ci.CIFieldID;

/**
 * This represents a class which is contained within a class file.
 *
 * @since 2016/04/26
 */
public final class CISField
	extends CISMember<CIFieldID, CIFieldFlags>
	implements CIField
{
	/** The field constant value. */
	protected final Object constant;
	
	/**
	 * Initializes the class field.
	 *
	 * @param __oc The outer class.
	 * @param __id The identifier of the field.
	 * @param __fl The field flags.
	 * @param __cv The constant value of the field.
	 * @since 2016/04/26
	 */
	CISField(CISClass __oc, CIFieldID __id, CIFieldFlags __fl, Object __cv)
	{
		super(__oc, __id, __fl);
		
		// Optional
		constant = __cv;
		
		// Check that the type is valid
		if (__cv != null)
		{
			// {@squirreljme.error AQ0o A field cannot have a constant value
			// which is not of the standard boxed and fixed immutable types; or
			// the constant value is not compatible with the field type.
			// (The class of the object attempted to be used)}
			FieldSymbol fs = nameAndType().type();
			if (((fs.equals("I") || fs.equals("Z") || fs.equals("B") ||
					fs.equals("S") || fs.equals("C")) &&
						!(__cv instanceof Integer)) ||
				(fs.equals("J") && !(__cv instanceof Long)) ||
				(fs.equals("F") && !(__cv instanceof Float)) ||
				(fs.equals("D") && !(__cv instanceof Double)) ||
				(fs.equals("Ljava/lang/String;") && !(__cv instanceof String)))
				throw new CIException(String.format("AQ0o %s",
					__cv.getClass()));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/26
	 */
	@Override
	public Object constantValue()
	{
		return constant;
	}
}

