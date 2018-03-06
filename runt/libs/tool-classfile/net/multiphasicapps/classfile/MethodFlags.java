// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represents the set of flags for methods.
 *
 * @since 2016/04/23
 */
public final class MethodFlags
	extends MemberFlags<MethodFlag>
{
	/**
	 * Initializes the method flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The method flags.
	 * @since 2016/04/23
	 */
	public MethodFlags(ClassFlags __oc, MethodFlag... __fl)
	{
		super(MethodFlag.class, __fl);
		
		__checkFlags(__oc);
	}
	
	/**
	 * Initializes the method flags.
	 *
	 * @param __oc The outer class.
	 * @param __fl The method flags.
	 * @since 2016/04/23
	 */
	public MethodFlags(ClassFlags __oc, Iterable<MethodFlag> __fl)
	{
		super(MethodFlag.class, __fl);
		
		__checkFlags(__oc);
	}
	
	/**
	 * Decodes method flags from the bit field.
	 *
	 * @param __oc The outer class flags.
	 * @param __i The bitfield to decode.
	 * @since 2017/07/07
	 */
	public MethodFlags(ClassFlags __oc, int __i)
	{
		this(__oc, Flags.<MethodFlag>__decode(__i, MethodFlag.values()));
	}
	
	/**
	 * Returns {@code true} if this is abstract.
	 *
	 * @return {@code true} if abstract.
	 * @since 2016/03/20
	 */
	public boolean isAbstract()
	{
		return contains(MethodFlag.ABSTRACT);
	}
	
	/**
	 * Returns {@code true} if this is a bridge method.
	 *
	 * @return {@code true} if a bridge method.
	 * @since 2016/03/20
	 */
	public boolean isBridge()
	{
		return contains(MethodFlag.BRIDGE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isFinal()
	{
		return contains(MethodFlag.FINAL);
	}
	
	/**
	 * Is this an instance method?
	 *
	 * @return {@code true} if an instance method.
	 * @since 2017/08/13
	 */
	public boolean isInstance()
	{
		return !isStatic();
	}
	
	/**
	 * Returns {@code true} if this is a native method.
	 *
	 * @return {@code true} if it is native.
	 * @since 2016/03/20
	 */
	public boolean isNative()
	{
		return contains(MethodFlag.NATIVE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPrivate()
	{
		return contains(MethodFlag.PRIVATE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isProtected()
	{
		return contains(MethodFlag.PROTECTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPublic()
	{
		return contains(MethodFlag.PUBLIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isStatic()
	{
		return contains(MethodFlag.STATIC);
	}
	
	/**
	 * Returns {@code true} if this is strict.
	 *
	 * @return {@code true} if strict.
	 * @since 2016/03/20
	 */
	public boolean isStrict()
	{
		return contains(MethodFlag.STRICT);
	}
	
	/**
	 * Returns {@code true} if this is synchronized.
	 *
	 * @return {@code true} if synchronized.
	 * @since 2016/03/20
	 */
	public boolean isSynchronized()
	{
		return contains(MethodFlag.SYNCHRONIZED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isSynthetic()
	{
		return contains(MethodFlag.SYNTHETIC);
	}
	
	/**
	 * Returns {@code true} if this uses variable arguments.
	 *
	 * @return {@code true} if it uses variable arguments.
	 * @since 2016/03/20
	 */
	public boolean isVarArgs()
	{
		return contains(MethodFlag.VARARGS);
	}
	
	/**
	 * Checks that the given flags are valid.
	 *
	 * @param __oc The outer class.
	 * @throws InvalidClassFormatException If they are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	private final void __checkFlags(ClassFlags __oc)
		throws InvalidClassFormatException
	{
		// Check
		if (__oc == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JC19 An {@code abstract} method cannot be
		// {@code private}, {@code static}, {@code final},
		// {@code synchronized}, {@code native}, or {@code strictfp}. (The
		// method flags)}
		if (isAbstract())
			if (isPrivate() || isStatic() || isFinal() || isSynchronized() ||
				isNative() || isStrict())
				throw new InvalidClassFormatException(
					String.format("JC19 %s", this));
		
		// If the class is an interface it cannot have specific flags set
		if (__oc.isInterface())
			for (MethodFlag f : MethodFlag.values())
			{
				// Must have these
				boolean must = (f == MethodFlag.PUBLIC ||
					f == MethodFlag.ABSTRACT);
				
				// Could have these
				boolean maybe = (f == MethodFlag.SYNTHETIC ||
					f == MethodFlag.VARARGS || f == MethodFlag.BRIDGE);
				
				// Is it set?
				boolean has = contains(f);
				
				// {@squirreljme.error JC1a Flags for interface method has an
				// incorrect set of flags. (The method flags)}
				if (must != has && !maybe)
					throw new InvalidClassFormatException(
						String.format("JC1a %s", this));
			}
	}
}

