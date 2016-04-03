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
public final class JVMClassFlags
	extends JVMFlags<JVMClassFlag>
{
	/**
	 * Initializes the class flags.
	 *
	 * @param __fl The flags to use.
	 * @throws CFFormatException If the given flag combination is not legal.
	 * @since 2016/03/19
	 */
	public JVMClassFlags(int __fl)
		throws CFFormatException
	{
		super(__fl, JVMClassFlag.class, JVMClassFlag.allFlags());
		
		// Interface?
		if (isInterface())
		{
			// Must be abstract
			if (!isAbstract())
				throw new CFFormatException(String.format("IN03 %s",
					toString()));
			
			// cannot have some flags set
			if (isFinal() || isSpecialInvokeSpecial() || isEnum())
				throw new CFFormatException(String.format("IN04 %s",
					toString()));
		}
		
		// Normal class
		else
		{
			// Cannot be an annotation
			if (isAnnotation())
				throw new CFFormatException(String.format("IN05 %s",
					toString()));
				
			// Cannot be abstract and final
			if (isAbstract() && isFinal())
				throw new CFFormatException(String.format("IN06 %s",
					toString()));
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
		return contains(JVMClassFlag.ABSTRACT);
	}
	
	/**
	 * Is this an annotation?
	 *
	 * @return {@code true} if it is an annotation.
	 * @since 2016/03/15
	 */
	public final boolean isAnnotation()
	{
		return contains(JVMClassFlag.ANNOTATION);
	}
	
	/**
	 * Is this an enumeration?
	 *
	 * @return {@code true} if it is an enumeration.
	 * @since 2016/03/15
	 */
	public final boolean isEnum()
	{
		return contains(JVMClassFlag.ENUM);
	}
	
	/**
	 * Is this class final?
	 *
	 * @return {@code true} if it is final.
	 * @since 2016/03/15
	 */
	public final boolean isFinal()
	{
		return contains(JVMClassFlag.FINAL);
	}
	
	/**
	 * Is this an interface?
	 *
	 * @return {@code true} if an interface.
	 * @since 2016/03/15
	 */
	public final boolean isInterface()
	{
		return contains(JVMClassFlag.INTERFACE);
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
		return contains(JVMClassFlag.PUBLIC);
	}
	
	/**
	 * Is there special handling for super-class method calls?
	 *
	 * @return {@code true} if the super-class invocation special flag is set.
	 * @since 2016/03/15
	 */
	public final boolean isSpecialInvokeSpecial()
	{
		return contains(JVMClassFlag.SUPER);
	}
}

