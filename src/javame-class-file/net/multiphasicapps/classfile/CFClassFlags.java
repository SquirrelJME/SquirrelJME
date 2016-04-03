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

/**
 * DESCRIBE THIS.
 *
 * @since 2016/03/19
 */
public final class CFClassFlags
	extends CFFlags<CFClassFlag>
{
	/**
	 * Initializes the class flags.
	 *
	 * @param __fl The flags to use.
	 * @throws CFFormatException If the given flag combination is not legal.
	 * @since 2016/03/19
	 */
	public CFClassFlags(int __fl)
		throws CFFormatException
	{
		super(__fl, CFClassFlag.class, CFClassFlag.allFlags());
		
		// Interface?
		if (isInterface())
		{
			// {@squirreljme.error CF03 An interface must also be abstract.
			// (The class flags}}
			if (!isAbstract())
				throw new CFFormatException(String.format("CF03 %s", this));
			
			// {@squirreljme.error CF04 An interface cannot be {@code final} or
			// {@code enum} and it must not have the special flag set. (The
			// class flags)}
			if (isFinal() || isSpecialInvokeSpecial() || isEnum())
				throw new CFFormatException(String.format("CF04 %s", this));
		}
		
		// Normal class
		else
		{
			// {@squirreljme.error CF05 Annotations must be interfaces. (The
			// class flags)}
			if (isAnnotation())
				throw new CFFormatException(String.format("CF05 %s", this));
				
			// {@squirreljme.error CF06 A class cannot be both {@code abstract}
			// and {@code final}. (The class flags)}
			if (isAbstract() && isFinal())
				throw new CFFormatException(String.format("CF06 %s", this));
		}
	}
	
	/**
	 * Is this class abstract?
	 *
	 * @return {@code true} if it is abstract.
	 * @since 2016/03/15
	 */
	public final boolean isAbstract()
	{
		return contains(CFClassFlag.ABSTRACT);
	}
	
	/**
	 * Is this an annotation?
	 *
	 * @return {@code true} if it is an annotation.
	 * @since 2016/03/15
	 */
	public final boolean isAnnotation()
	{
		return contains(CFClassFlag.ANNOTATION);
	}
	
	/**
	 * Is this an enumeration?
	 *
	 * @return {@code true} if it is an enumeration.
	 * @since 2016/03/15
	 */
	public final boolean isEnum()
	{
		return contains(CFClassFlag.ENUM);
	}
	
	/**
	 * Is this class final?
	 *
	 * @return {@code true} if it is final.
	 * @since 2016/03/15
	 */
	public final boolean isFinal()
	{
		return contains(CFClassFlag.FINAL);
	}
	
	/**
	 * Is this an interface?
	 *
	 * @return {@code true} if an interface.
	 * @since 2016/03/15
	 */
	public final boolean isInterface()
	{
		return contains(CFClassFlag.INTERFACE);
	}
	
	/**
	 * Is this class package private?
	 *
	 * @return {@code true} if it is package private.
	 * @since 2016/03/15
	 */
	public final boolean isPackagePrivate()
	{
		return !isPublic();
	}
	
	/**
	 * Is this class public?
	 *
	 * @return {@code true} if it is public.
	 * @since 2016/03/15
	 */
	public final boolean isPublic()
	{
		return contains(CFClassFlag.PUBLIC);
	}
	
	/**
	 * Is there special handling for super-class method calls?
	 *
	 * @return {@code true} if the super-class invocation special flag is set.
	 * @since 2016/03/15
	 */
	public final boolean isSpecialInvokeSpecial()
	{
		return contains(CFClassFlag.SUPER);
	}
}

