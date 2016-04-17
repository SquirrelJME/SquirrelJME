// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.util.Set;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;

/**
 * This represents a field which is defined in a class.
 *
 * @since 2016/03/17
 */
public class CFField
	extends CFMember<FieldSymbol, CFFieldFlags>
{
	/** The constant value of the field. */
	protected final Object constantvalue;
	
	/**
	 * Initializes the interpreted method.
	 *
	 * @param __nat The name and type of the field.
	 * @param __fl The flags the field uses.
	 * @param __cv The constant value of the field.
	 * @throws NullPointerException On null arguments, except for {@code __cv}.
	 * @since 2016/03/01
	 */
	CFField(CFMemberKey<FieldSymbol> __nat, CFFieldFlags __fl, Object __cv)
		throws NullPointerException
	{
		super(FieldSymbol.class, __nat, CFFieldFlags.class, __fl);
		
		if (__cv != null)
		{
			// {@squirreljme.error CF0o A field cannot have a constant value
			// which is not of the standard boxed and fixed immutable types; or
			// the constant value is not compatible with the field type.
			// (The class of the object attempted to be used)}
			FieldSymbol fs = type();
			if (((fs.equals("I") || fs.equals("Z") || fs.equals("B") ||
					fs.equals("S") || fs.equals("C")) &&
						!(__cv instanceof Integer)) ||
				(fs.equals("J") && !(__cv instanceof Long)) ||
				(fs.equals("F") && !(__cv instanceof Float)) ||
				(fs.equals("D") && !(__cv instanceof Double)) ||
				(fs.equals("Ljava/lang/String;") && !(__cv instanceof String)))
				throw new CFFormatException(String.format("CF0o %s",
					__cv.getClass()));
		}
		
		// {@squirreljme.error CF0p A field cannot be both volatile and final.
		// (the flags the field uses)}
		if (__fl.isFinal() && __fl.isVolatile())
			throw new CFFormatException(String.format("CF0p %s", __fl));
		
		// Set
		if (__cv instanceof Boolean)
			constantvalue = (((Boolean)__cv) ? 1 : 0);
		else if (__cv instanceof Byte || __cv instanceof Short)
			constantvalue = Integer.valueOf(((Number)__cv).intValue());
		else if (__cv instanceof Character)
			constantvalue = (int)(((Character)__cv).charValue());
		else
			constantvalue = __cv;
	}
	
	/**
	 * Returns the field constant value, if any has been set.
	 *
	 * @return The constant value or {@code null} if there is no value set.
	 * @since 2016/04/01
	 */
	public Object getConstantValue()
	{
		return constantvalue;
	}
}

