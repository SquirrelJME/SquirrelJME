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

import java.util.List;

/**
 * This is the base class for member type flags.
 *
 * @param <F> The type of flags to use.
 * @since 2016/03/19
 */
public abstract class CFMemberFlags<F extends CFMemberFlag>
	extends CFFlags<F>
{
	/**
	 * Initializes the member flags.
	 *
	 * @param __b The input bits.
	 * @param __type The flag type.
	 * @param __all All available flags.
	 * @throws CFFormatException If a member has multiple access flags
	 * specified.
	 * @since 2016/03/19
	 */
	public CFMemberFlags(int __b, Class<F> __type, List<F> __all)
		throws CFFormatException
	{
		super(__b, __type, __all);
		
		// Cannot have multiple modifiers attached
		int q = (isPublic() ? 1 : 0);
		q += (isProtected() ? 1 : 0);
		q += (isPrivate() ? 1 : 0);
		
		// {@squirreljme.error CF0u A member must have zero or exactly one
		// access modifier. (The member flags)}
		if (q > 1)
			throw new CFFormatException(String.format("CF0u %s", this));
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

