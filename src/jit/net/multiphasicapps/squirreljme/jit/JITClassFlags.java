// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This represents the flags which a class may be.
 *
 * @since 2016/04/23
 */
public class JITClassFlags
	extends JITFlags<JITClassFlag>
	implements JITAccessibleFlags
{
	/**
	 * Initializes the class flags.
	 *
	 * @param __fl The class flags.
	 * @since 2016/04/23
	 */
	public JITClassFlags(JITClassFlag... __fl)
	{
		super(JITClassFlag.class, __fl);
		
		__checkFlags();
	}
	
	/**
	 * Initializes the class flags.
	 *
	 * @param __fl The class flags.
	 * @since 2016/04/23
	 */
	public JITClassFlags(Iterable<JITClassFlag> __fl)
	{
		super(JITClassFlag.class, __fl);
		
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
		return contains(JITClassFlag.ABSTRACT);
	}
	
	/**
	 * Is this an annotation?
	 *
	 * @return {@code true} if it is an annotation.
	 * @since 2016/03/15
	 */
	public final boolean isAnnotation()
	{
		return contains(JITClassFlag.ANNOTATION);
	}
	
	/**
	 * Is this an enumeration?
	 *
	 * @return {@code true} if it is an enumeration.
	 * @since 2016/03/15
	 */
	public final boolean isEnum()
	{
		return contains(JITClassFlag.ENUM);
	}
	
	/**
	 * Is this class final?
	 *
	 * @return {@code true} if it is final.
	 * @since 2016/03/15
	 */
	public final boolean isFinal()
	{
		return contains(JITClassFlag.FINAL);
	}
	
	/**
	 * Is this an interface?
	 *
	 * @return {@code true} if an interface.
	 * @since 2016/03/15
	 */
	public final boolean isInterface()
	{
		return contains(JITClassFlag.INTERFACE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public final boolean isPackagePrivate()
	{
		return !isPublic();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public final boolean isPrivate()
	{
		// Classes are never private
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public final boolean isProtected()
	{
		// Classes are never protected
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/15
	 */
	@Override
	public final boolean isPublic()
	{
		return contains(JITClassFlag.PUBLIC);
	}
	
	/**
	 * Is there special handling for super-class method calls?
	 *
	 * @return {@code true} if the super-class invocation special flag is set.
	 * @since 2016/03/15
	 */
	public final boolean isSpecialInvokeSpecial()
	{
		return contains(JITClassFlag.SUPER);
	}
	
	/**
	 * Checks that the given flags are valid.
	 *
	 * @throws JITException If they are not valid.
	 * @since 2016/04/23
	 */
	private final void __checkFlags()
		throws JITException
	{
		// Interface?
		if (isInterface())
		{
			// {@squirreljme.error ED23 An interface must also be abstract.
			// (The class flags}}
			if (!isAbstract())
				throw new JITException(String.format("ED23 %s", this));
			
			// {@squirreljme.error ED24 An interface cannot be {@code final} or
			// {@code enum} and it must not have the special flag set. (The
			// class flags)}
			if (isFinal() || isSpecialInvokeSpecial() || isEnum())
				throw new JITException(String.format("ED24 %s", this));
		}
		
		// Normal class
		else
		{
			// {@squirreljme.error ED25 Annotations must be interfaces. (The
			// class flags)}
			if (isAnnotation())
				throw new JITException(String.format("ED25 %s", this));
				
			// {@squirreljme.error ED26 A class cannot be both {@code abstract}
			// and {@code final}. (The class flags)}
			if (isAbstract() && isFinal())
				throw new JITException(String.format("ED26 %s", this));
		}
	}
}

