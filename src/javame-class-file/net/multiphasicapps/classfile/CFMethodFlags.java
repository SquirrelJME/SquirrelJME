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
 * This is the set of method flags.
 *
 * @since 2016/03/19
 */
public final class CFMethodFlags
	extends CFMemberFlags<CFMethodFlag>
{
	/**
	 * Initializes the method flags.
	 *
	 * @param __b The bits used for the method.
	 * @throws CFFormatException If the method flag combinations are not
	 * valid.
	 * @since 2016/03/19
	 */
	public CFMethodFlags(int __b)
		throws CFFormatException
	{
		super(__b, CFMethodFlag.class, CFMethodFlag.allFlags());
		
		// {@squirreljme.error CF0z Native methods are not supported in Java ME
		// and as such, methods must not be {@code native}.}
		if (isNative())
			throw new CFFormatException("CF0z");
		
		// {@squirreljme.error CF10 An {@code abstract} method cannot be
		// {@code private}, {@code static}, {@code final},
		// {@code synchronized}, {@code native}, or {@code strictfp}. (The
		// method flags)}
		if (isAbstract())
			if (isPrivate() || isStatic() || isFinal() || isSynchronized() ||
				isNative() || isStrict())
				throw new CFFormatException(String.format("CF10 %s", this));
	}
	
	/**
	 * Returns {@code true} if this is abstract.
	 *
	 * @return {@code true} if abstract.
	 * @since 2016/03/20
	 */
	public boolean isAbstract()
	{
		return contains(CFMethodFlag.ABSTRACT);
	}
	
	/**
	 * Returns {@code true} if this is a bridge method.
	 *
	 * @return {@code true} if a bridge method.
	 * @since 2016/03/20
	 */
	public boolean isBridge()
	{
		return contains(CFMethodFlag.BRIDGE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isFinal()
	{
		return contains(CFMethodFlag.FINAL);
	}
	
	/**
	 * Returns {@code true} if this is a native method.
	 *
	 * @return {@code true} if it is native.
	 * @since 2016/03/20
	 */
	public boolean isNative()
	{
		return contains(CFMethodFlag.NATIVE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPrivate()
	{
		return contains(CFMethodFlag.PRIVATE);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isProtected()
	{
		return contains(CFMethodFlag.PROTECTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isPublic()
	{
		return contains(CFMethodFlag.PUBLIC);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isStatic()
	{
		return contains(CFMethodFlag.STATIC);
	}
	
	/**
	 * Returns {@code true} if this is strict.
	 *
	 * @return {@code true} if strict.
	 * @since 2016/03/20
	 */
	public boolean isStrict()
	{
		return contains(CFMethodFlag.STRICT);
	}
	
	/**
	 * Returns {@code true} if this is synchronized.
	 *
	 * @return {@code true} if synchronized.
	 * @since 2016/03/20
	 */
	public boolean isSynchronized()
	{
		return contains(CFMethodFlag.SYNCHRONIZED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/20
	 */
	@Override
	public boolean isSynthetic()
	{
		return contains(CFMethodFlag.SYNTHETIC);
	}
	
	/**
	 * Returns {@code true} if this uses variable arguments.
	 *
	 * @return {@code true} if it uses variable arguments.
	 * @since 2016/03/20
	 */
	public boolean isVarArgs()
	{
		return contains(CFMethodFlag.VARARGS);
	}
}

