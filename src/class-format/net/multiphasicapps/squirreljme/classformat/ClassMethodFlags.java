// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This represents the set of flags for methods.
 *
 * @since 2016/04/23
 */
public final class ClassMethodFlags
	extends ClassMemberFlags<ClassMethodFlag>
{
	/**
	 * Initializes the method flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The method flags.
	 * @since 2016/04/23
	 */
	public ClassMethodFlags(ClassClassFlags __oc, ClassMethodFlag... __fl)
	{
		super(ClassMethodFlag.class, __fl);
		
		__checkFlags(__oc);
	}
	
	/**
	 * Initializes the method flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The method flags.
	 * @since 2016/04/23
	 */
	public ClassMethodFlags(ClassClassFlags __oc, Iterable<ClassMethodFlag> __fl)
	{
		super(ClassMethodFlag.class, __fl);
		
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
		return contains(ClassMethodFlag.ABSTRACT);
	}
	
	/**
	 * Returns {@code true} if this is a bridge method.
	 *
	 * @return {@code true} if a bridge method.
	 * @since 2016/03/20
	 */
	public boolean isBridge()
	{
		return contains(ClassMethodFlag.BRIDGE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isFinal()
	{
		return contains(ClassMethodFlag.FINAL);
	}
	
	/**
	 * Returns {@code true} if this is a native method.
	 *
	 * @return {@code true} if it is native.
	 * @since 2016/03/20
	 */
	public boolean isNative()
	{
		return contains(ClassMethodFlag.NATIVE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPrivate()
	{
		return contains(ClassMethodFlag.PRIVATE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isProtected()
	{
		return contains(ClassMethodFlag.PROTECTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPublic()
	{
		return contains(ClassMethodFlag.PUBLIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isStatic()
	{
		return contains(ClassMethodFlag.STATIC);
	}
	
	/**
	 * Returns {@code true} if this is strict.
	 *
	 * @return {@code true} if strict.
	 * @since 2016/03/20
	 */
	public boolean isStrict()
	{
		return contains(ClassMethodFlag.STRICT);
	}
	
	/**
	 * Returns {@code true} if this is synchronized.
	 *
	 * @return {@code true} if synchronized.
	 * @since 2016/03/20
	 */
	public boolean isSynchronized()
	{
		return contains(ClassMethodFlag.SYNCHRONIZED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isSynthetic()
	{
		return contains(ClassMethodFlag.SYNTHETIC);
	}
	
	/**
	 * Returns {@code true} if this uses variable arguments.
	 *
	 * @return {@code true} if it uses variable arguments.
	 * @since 2016/03/20
	 */
	public boolean isVarArgs()
	{
		return contains(ClassMethodFlag.VARARGS);
	}
	/**
	 * Checks that the given flags are valid.
	 *
	 * @param __oc The outer class.
	 * @throws ClassFormatException If they are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	private final void __checkFlags(ClassClassFlags __oc)
		throws ClassFormatException
	{
		// Check
		if (__oc == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AY07 Native methods are not supported in Java ME
		// and as such, methods must not be {@code native}.}
		if (isNative())
			throw new ClassFormatException("AY07");
		
		// {@squirreljme.error AY08 An {@code abstract} method cannot be
		// {@code private}, {@code static}, {@code final},
		// {@code synchronized}, {@code native}, or {@code strictfp}. (The
		// method flags)}
		if (isAbstract())
			if (isPrivate() || isStatic() || isFinal() || isSynchronized() ||
				isNative() || isStrict())
				throw new ClassFormatException(String.format("AY08 %s", this));
		
		// If the class is an interface it cannot have specific flags set
		if (__oc.isInterface())
			for (ClassMethodFlag f : ClassMethodFlag.values())
			{
				// Must have these
				boolean must = (f == ClassMethodFlag.PUBLIC ||
					f == ClassMethodFlag.ABSTRACT);
				
				// Could have these
				boolean maybe = (f == ClassMethodFlag.SYNTHETIC ||
					f == ClassMethodFlag.VARARGS || f == ClassMethodFlag.BRIDGE);
				
				// Is it set?
				boolean has = contains(f);
				
				// {@squirreljme.error AY09 Flags for interface method has an
				// incorrect set of flags. (The method flags)}
				if (must != has && !maybe)
					throw new ClassFormatException(String.format("AY09 %s", this));
			}
	}
}

