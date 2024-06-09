// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	 * Initializes the method flags, with no checks performed on them.
	 *
	 * @param __i The method flags.
	 * @since 2016/04/23
	 */
	public MethodFlags(int __i)
	{
		super(MethodFlag.class,
			Flags.<MethodFlag>__decode(__i, MethodFlag.values()));
	}
	
	/**
	 * Initializes the method flags.
	 *
	 * @param __oc The outer class.
	 * @param __mn The name of the method.
	 * @param __fl The method flags.
	 * @since 2016/04/23
	 */
	public MethodFlags(ClassFlags __oc, MethodName __mn, MethodFlag... __fl)
	{
		super(MethodFlag.class, __fl);
		
		this.__checkFlags(__oc, __mn);
	}
	
	/**
	 * Initializes the method flags.
	 *
	 * @param __oc The outer class.
	 * @param __mn The name of the method.
	 * @param __fl The method flags.
	 * @since 2016/04/23
	 */
	public MethodFlags(ClassFlags __oc, MethodName __mn,
		Iterable<MethodFlag> __fl)
	{
		super(MethodFlag.class, __fl);
		
		this.__checkFlags(__oc, __mn);
	}
	
	/**
	 * Decodes method flags from the bit field.
	 *
	 * @param __oc The outer class flags.
	 * @param __mn The name of the method.
	 * @param __i The bitfield to decode.
	 * @since 2017/07/07
	 */
	public MethodFlags(ClassFlags __oc, MethodName __mn, int __i)
	{
		this(__oc, __mn, Flags.<MethodFlag>__decode(__i, MethodFlag.values()));
	}
	
	/**
	 * Returns {@code true} if this is abstract.
	 *
	 * @return {@code true} if abstract.
	 * @since 2016/03/20
	 */
	public boolean isAbstract()
	{
		return this.contains(MethodFlag.ABSTRACT);
	}
	
	/**
	 * Returns {@code true} if this is a bridge method.
	 *
	 * @return {@code true} if a bridge method.
	 * @since 2016/03/20
	 */
	public boolean isBridge()
	{
		return this.contains(MethodFlag.BRIDGE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isFinal()
	{
		return this.contains(MethodFlag.FINAL);
	}
	
	/**
	 * Returns {@code true} if this is a native method.
	 *
	 * @return {@code true} if it is native.
	 * @since 2016/03/20
	 */
	public boolean isNative()
	{
		return this.contains(MethodFlag.NATIVE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPrivate()
	{
		return this.contains(MethodFlag.PRIVATE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isProtected()
	{
		return this.contains(MethodFlag.PROTECTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPublic()
	{
		return this.contains(MethodFlag.PUBLIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isStatic()
	{
		return this.contains(MethodFlag.STATIC);
	}
	
	/**
	 * Returns {@code true} if this is strict.
	 *
	 * @return {@code true} if strict.
	 * @since 2016/03/20
	 */
	public boolean isStrict()
	{
		return this.contains(MethodFlag.STRICT);
	}
	
	/**
	 * Returns {@code true} if this is synchronized.
	 *
	 * @return {@code true} if synchronized.
	 * @since 2016/03/20
	 */
	public boolean isSynchronized()
	{
		return this.contains(MethodFlag.SYNCHRONIZED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isSynthetic()
	{
		return this.contains(MethodFlag.SYNTHETIC);
	}
	
	/**
	 * Returns {@code true} if this uses variable arguments.
	 *
	 * @return {@code true} if it uses variable arguments.
	 * @since 2016/03/20
	 */
	public boolean isVarArgs()
	{
		return this.contains(MethodFlag.VARARGS);
	}
	
	/**
	 * Checks that the given flags are valid.
	 *
	 * @param __oc The outer class.
	 * @param __mn The name of the method.
	 * @throws InvalidClassFormatException If they are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/23
	 */
	private void __checkFlags(ClassFlags __oc, MethodName __mn)
		throws InvalidClassFormatException
	{
		// Check
		if (__oc == null || __mn == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error JC3l An {@code abstract} method cannot be
		{@code private}, {@code static}, {@code final},
		{@code synchronized}, {@code native}, or {@code strictfp}. (The
		method flags)} */
		if (this.isAbstract())
			if (this.isPrivate() || this.isStatic() || this.isFinal() || this
				.isSynchronized() || this.isNative() || this.isStrict())
				throw new InvalidClassFormatException(
					String.format("JC3l %s", this), this);
		
		// If the class is an interface it cannot have specific flags set
		// Ignore checking these interface flags if we are in an interface and
		// this is a static constructor because otherwise the check will fail
		// since there cannot be static items
		if (__oc.isInterface() && !__mn.isStaticInitializer())
			for (MethodFlag f : MethodFlag.values())
			{
				// Must have these
				boolean must = (f == MethodFlag.PUBLIC ||
					f == MethodFlag.ABSTRACT);
				
				// Could have these
				boolean maybe = (f == MethodFlag.SYNTHETIC ||
					f == MethodFlag.VARARGS || f == MethodFlag.BRIDGE);
				
				// Is it set?
				boolean has = this.contains(f);
				
				/* {@squirreljme.error JC3m Flags for interface method has an
				incorrect set of flags. (The method flags)} */
				if (must != has && !maybe)
					throw new InvalidClassFormatException(
						String.format("JC3m %s", this), this);
			}
	}
}

