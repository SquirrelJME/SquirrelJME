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
import net.multiphasicapps.classfile.CFFields;
import net.multiphasicapps.classfile.CFMemberKey;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.FieldSymbol;

/**
 * This represents the collection of fields which are availale to a class..
 *
 * @since 2016/04/04
 */
public class JVMFields
	extends JVMMembers<FieldSymbol, CFFieldFlags, CFField, JVMField>
{
	/**
	 * Initializes the class field mappings.
	 *
	 * @param __cl The owning class.
	 * @since 2016/04/04
	 */
	JVMFields(JVMClass __cl)
	{
		super(__cl, __cl.base().fields());
	}
	
	/**
	 * Initializes the class field mappings with no actual mappings.
	 *
	 * @param __cl The owning class.
	 * @param __ign Ignored.
	 * @since 2016/04/04
	 */
	JVMFields(JVMClass __cl, boolean __ign)
	{
		super(__cl, null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/05
	 */
	protected JVMField wrapInternally(CFField __cf)
	{
		return new JVMField(this, __cf);
	}
}

