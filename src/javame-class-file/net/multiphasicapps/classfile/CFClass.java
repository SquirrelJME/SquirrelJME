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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MemberTypeSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This is the base class for all class related information.
 *
 * @since 2016/03/16
 */
public class JVMClass
	implements JVMComponentType
{
	/**
	 * Initializes the base class information.
	 *
	 * @param __eng The owning engine.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/16
	 */
	protected JVMClass(CFClassParser __cf)
		throws NullPointerException
	{
		// Check
		if (__eng == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the fields which are mapped to this class.
	 *
	 * @return The field mappings.
	 * @since 2016/03/20
	 */
	public final CFFields fields()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the flags which are specified by this class.
	 *
	 * @return The used class flags.
	 * @throws IllegalStateException If flags are not set.
	 * @since 2016/03/16
	 */
	public final JVMClassFlags flags()
		throws IllegalStateException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the set of interfaces which may be implemented by the class.
	 *
	 * @return The class interface set.
	 * @since 2016/03/19
	 */
	public final JVMClassInterfaces interfaces()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the methods which are mapped to this class.
	 *
	 * @return The method mappings.
	 * @since 2016/03/20
	 */
	public final CFMethods methods()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the super class name of this class.
	 *
	 * @return The super class this extends.
	 * @throws IllegalStateException If the super class name was not set.
	 * @since 2016/03/19
	 */
	public final ClassNameSymbol superName()
		throws IllegalStateException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the name of the current class.
	 *
	 * @return The current class name.
	 * @throws IllegalStateException On null arguments.
	 * @since 2016/03/19
	 */
	public final ClassNameSymbol thisName()
		throws IllegalStateException
	{
		throw new Error("TODO");
	}
}

