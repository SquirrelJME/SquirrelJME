// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

/**
 * This represents the set of flags for methods.
 *
 * @since 2016/04/23
 */
public final class CIMethodFlags
	extends CIMemberFlags<CIMethodFlag>
{
	/**
	 * Initializes the method flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The method flags.
	 * @since 2016/04/23
	 */
	public CIMethodFlags(CIClass __oc, CIMethodFlag... __fl)
	{
		super(CIMethodFlag.class, __fl);
		
		__checkFlags(__oc);
	}
	
	/**
	 * Initializes the method flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The method flags.
	 * @since 2016/04/23
	 */
	public CIMethodFlags(CIClass __oc, Iterable<CIMethodFlag> __fl)
	{
		super(CIMethodFlag.class, __fl);
		
		__checkFlags(__oc);
	}
	
	/**
	 * Returns {@code true} if this is abstract.
	 *
	 * @return {@code true} if abstract.
	 * @since 2016/03/20
	 */
	public boolean isAbstract()
	{
		return contains(CIMethodFlag.ABSTRACT);
	}
	
	/**
	 * Returns {@code true} if this is a bridge method.
	 *
	 * @return {@code true} if a bridge method.
	 * @since 2016/03/20
	 */
	public boolean isBridge()
	{
		return contains(CIMethodFlag.BRIDGE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isFinal()
	{
		return contains(CIMethodFlag.FINAL);
	}
	
	/**
	 * Returns {@code true} if this is a native method.
	 *
	 * @return {@code true} if it is native.
	 * @since 2016/03/20
	 */
	public boolean isNative()
	{
		return contains(CIMethodFlag.NATIVE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPrivate()
	{
		return contains(CIMethodFlag.PRIVATE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isProtected()
	{
		return contains(CIMethodFlag.PROTECTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPublic()
	{
		return contains(CIMethodFlag.PUBLIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isStatic()
	{
		return contains(CIMethodFlag.STATIC);
	}
	
	/**
	 * Returns {@code true} if this is strict.
	 *
	 * @return {@code true} if strict.
	 * @since 2016/03/20
	 */
	public boolean isStrict()
	{
		return contains(CIMethodFlag.STRICT);
	}
	
	/**
	 * Returns {@code true} if this is synchronized.
	 *
	 * @return {@code true} if synchronized.
	 * @since 2016/03/20
	 */
	public boolean isSynchronized()
	{
		return contains(CIMethodFlag.SYNCHRONIZED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isSynthetic()
	{
		return contains(CIMethodFlag.SYNTHETIC);
	}
	
	/**
	 * Returns {@code true} if this uses variable arguments.
	 *
	 * @return {@code true} if it uses variable arguments.
	 * @since 2016/03/20
	 */
	public boolean isVarArgs()
	{
		return contains(CIMethodFlag.VARARGS);
	}
	/**
	 * Checks that the given flags are valid.
	 *
	 * @param __oc The outer class.
	 * @throws CIException If they are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	private final void __checkFlags(CIClass __oc)
		throws CIException
	{
		// Check
		if (__oc == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AO0z Native methods are not supported in Java ME
		// and as such, methods must not be {@code native}.}
		if (isNative())
			throw new CIException("AO0z");
		
		// {@squirreljme.error AO10 An {@code abstract} method cannot be
		// {@code private}, {@code static}, {@code final},
		// {@code synchronized}, {@code native}, or {@code strictfp}. (The
		// method flags)}
		if (isAbstract())
			if (isPrivate() || isStatic() || isFinal() || isSynchronized() ||
				isNative() || isStrict())
				throw new CIException(String.format("AO10 %s", this));
		
		// If the class is an interface it cannot have specific flags set
		if (__oc.flags().isInterface())
			for (CIMethodFlag f : CIMethodFlag.values())
			{
				// Must have these
				boolean must = (f == CIMethodFlag.PUBLIC ||
					f == CIMethodFlag.ABSTRACT);
				
				// Could have these
				boolean maybe = (f == CIMethodFlag.SYNTHETIC ||
					f == CIMethodFlag.VARARGS || f == CIMethodFlag.BRIDGE);
				
				// Is it set?
				boolean has = contains(f);
				
				// {@squirreljme.error AO1x Flags for interface method has an
				// incorrect set of flags. (The method flags)}
				if (must != has && !maybe)
					throw new CIException(String.format("AO1x %s", this));
			}
	}
}

