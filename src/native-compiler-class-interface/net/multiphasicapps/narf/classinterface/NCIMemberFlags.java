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
 * This is the base class for flags which are for member types.
 *
 * @param <F> The flag type.
 * @since 2016/04/23
 */
public abstract class NCIMemberFlags<F extends NCIMemberFlag>
	extends NCIFlags<F>
{
	/**
	 * Initializes the member flags.
	 *
	 * @param __cl The required class type.
	 * @param __fl The flags used.
	 * @since 2016/04/23
	 */
	NCIMemberFlags(Class<F> __cl, F[] __fl)
	{
		super(__cl, __fl);
	}
	
	/**
	 * Initializes the member flags.
	 *
	 * @param __cl The required class type.
	 * @param __fl The flags used.
	 * @since 2016/04/23
	 */
	NCIMemberFlags(Class<F> __cl, Iterable<F> __fl)
	{
		super(__cl, __fl);
	}
	
	/**
	 * Returns {@code true} if this is final.
	 *
	 * @return {@code true} if final.
	 * @since 2016/03/20
	 */
	public abstract boolean isFinal();
	
	/**
	 * Returns {@code true} if this is private.
	 *
	 * @return {@code true} if private.
	 * @since 2016/03/20
	 */
	public abstract boolean isPrivate();
	
	/**
	 * Returns {@code true} if this is protected.
	 *
	 * @return {@code true} if protected.
	 * @since 2016/03/20
	 */
	public abstract boolean isProtected();
	
	/**
	 * Returns {@code true} if this is public.
	 *
	 * @return {@code true} if public.
	 * @since 2016/03/20
	 */
	public abstract boolean isPublic();
	
	/**
	 * Returns {@code true} if this is static.
	 *
	 * @return {@code true} if static.
	 * @since 2016/03/20
	 */
	public abstract boolean isStatic();
	
	/**
	 * Returns {@code true} if this is synthetic.
	 *
	 * @return {@code true} if synthetic.
	 * @since 2016/03/20
	 */
	public abstract boolean isSynthetic();
	
	/**
	 * Returns {@code true} if this is package private.
	 *
	 * @return {@code true} if package private.
	 * @since 2016/03/20
	 */
	public final boolean isPackagePrivate()
	{
		return !isPublic() && !isProtected() && !isPrivate();
	}
}

