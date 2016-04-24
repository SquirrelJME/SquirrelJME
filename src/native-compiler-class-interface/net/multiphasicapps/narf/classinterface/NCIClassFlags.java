// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

/**
 * This represents the flags which a class may be.
 *
 * @since 2016/04/23
 */
public class NCIClassFlags
	extends NCIFlags<NCIClassFlag>
{
	/**
	 * Initializes the class flags.
	 *
	 * @param __fl The class flags.
	 * @since 2016/04/23
	 */
	public NCIClassFlags(NCIClassFlag... __fl)
	{
		super(NCIClassFlag.class, __fl);
		
		__checkFlags();
	}
	
	/**
	 * Initializes the class flags.
	 *
	 * @param __fl The class flags.
	 * @since 2016/04/23
	 */
	public NCIClassFlags(Iterable<NCIClassFlag> __fl)
	{
		super(NCIClassFlag.class, __fl);
		
		__checkFlags();
	}
	
	/**
	 * Is this class abstract?
	 *
	 * @return {@code true} if it is abstract.
	 * @since 2016/03/15
	 */
	public final boolean isAbstract()
	{
		return contains(NCIClassFlag.ABSTRACT);
	}
	
	/**
	 * Is this an annotation?
	 *
	 * @return {@code true} if it is an annotation.
	 * @since 2016/03/15
	 */
	public final boolean isAnnotation()
	{
		return contains(NCIClassFlag.ANNOTATION);
	}
	
	/**
	 * Is this an enumeration?
	 *
	 * @return {@code true} if it is an enumeration.
	 * @since 2016/03/15
	 */
	public final boolean isEnum()
	{
		return contains(NCIClassFlag.ENUM);
	}
	
	/**
	 * Is this class final?
	 *
	 * @return {@code true} if it is final.
	 * @since 2016/03/15
	 */
	public final boolean isFinal()
	{
		return contains(NCIClassFlag.FINAL);
	}
	
	/**
	 * Is this an interface?
	 *
	 * @return {@code true} if an interface.
	 * @since 2016/03/15
	 */
	public final boolean isInterface()
	{
		return contains(NCIClassFlag.INTERFACE);
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
		return contains(NCIClassFlag.PUBLIC);
	}
	
	/**
	 * Is there special handling for super-class method calls?
	 *
	 * @return {@code true} if the super-class invocation special flag is set.
	 * @since 2016/03/15
	 */
	public final boolean isSpecialInvokeSpecial()
	{
		return contains(NCIClassFlag.SUPER);
	}
	
	/**
	 * Checks that the given flags are valid.
	 *
	 * @throws NCIException If they are not valid.
	 * @since 2016/04/23
	 */
	private final void __checkFlags()
		throws NCIException
	{
		// Interface?
		if (isInterface())
		{
			// {@squirreljme.error NC03 An interface must also be abstract.
			// (The class flags}}
			if (!isAbstract())
				throw new NCIException(NCIException.Issue.ILLEGAL_FLAGS,
					String.format("NC03 %s", this));
			
			// {@squirreljme.error NC04 An interface cannot be {@code final} or
			// {@code enum} and it must not have the special flag set. (The
			// class flags)}
			if (isFinal() || isSpecialInvokeSpecial() || isEnum())
				throw new NCIException(NCIException.Issue.ILLEGAL_FLAGS,
					String.format("NC04 %s", this));
		}
		
		// Normal class
		else
		{
			// {@squirreljme.error NC05 Annotations must be interfaces. (The
			// class flags)}
			if (isAnnotation())
				throw new NCIException(NCIException.Issue.ILLEGAL_FLAGS,
					String.format("NC05 %s", this));
				
			// {@squirreljme.error NC06 A class cannot be both {@code abstract}
			// and {@code final}. (The class flags)}
			if (isAbstract() && isFinal())
				throw new NCIException(NCIException.Issue.ILLEGAL_FLAGS,
					String.format("NC06 %s", this));
		}
	}
}

