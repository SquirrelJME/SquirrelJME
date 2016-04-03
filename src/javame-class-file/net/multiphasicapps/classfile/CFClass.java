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
public class CFClass
{
	/**
	 * Initializes the base class information.
	 *
	 * @param __cf The parser which loaded class file data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/16
	 */
	protected CFClass(CFClassParser __cf)
		throws NullPointerException
	{
		// Check
		if (__cf == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
		/*
		// FIELD
		// Get class flags
		CFClassFlags cl = inclass.getFlags();
		
		// If an interface
		if (cl.isInterface())
		{
			// Must have these flags set and some not set
			if ((!__fl.isPublic() || !__fl.isStatic() || !__fl.isFinal()) ||
				__fl.isProtected() || __fl.isPrivate() || __fl.isVolatile() ||
				__fl.isTransient() || __fl.isEnum())
				throw new CFFormatException(String.format("IN1c %s %s",
						__fl, cl));
		}
		
		// Continue with super call
		return (CFField)super.setFlags(__fl);
		
		// METHOD
				// Class initializer flags are ignored for the most part
		if (!isClassInitializer())
		{
			// If the class is an interface...
			if (cl.isInterface())
			{
				// Default methods are not supported
				if (__fl.isPrivate() || !__fl.isAbstract())
					throw new CFFormatException(String.format("IN19 %s",
						__fl));
				
				// Cannot have these flags
				if (__fl.isProtected() || __fl.isFinal() ||
					__fl.isSynchronized() || __fl.isNative())
					throw new CFFormatException(String.format("IN1a %s %s",
						__fl, cl));
			}
		*/
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
	public final CFClassFlags flags()
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
	public final CFClassInterfaces interfaces()
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

