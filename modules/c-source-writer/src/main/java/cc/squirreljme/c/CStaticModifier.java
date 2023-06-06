// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

/**
 * Represents a modifier which includes {@code static}.
 *
 * @since 2023/06/05
 */
public final class CStaticModifier
	extends __SinglePrefixModifier__
{
	/** Constant for only static. */
	private static final CStaticModifier _JUST_STATIC =
		new CStaticModifier(null);
	
	/**
	 * Initializes the static wrapper.
	 * 
	 * @param __modifier The modifier to wrap.
	 * @since 2023/06/05
	 */
	private CStaticModifier(CModifier __modifier)
	{
		super("static", __modifier);
	}
	
	/**
	 * Wraps the modifier to make it {@code static}.
	 * 
	 * @param __modifier The modifier to wrap, may be {@code null}.
	 * @return The modifier which is now {@code static}.
	 * @throws IllegalArgumentException If the modifier would not be valid.
	 * @since 2023/06/05
	 */
	public static CModifier of(CModifier __modifier)
		throws IllegalArgumentException
	{
		// {@squirreljme.error CW0f Cannot have a static extern modifier.}
		if (__modifier instanceof CExternModifier)
			throw new IllegalArgumentException("CW0f");
		
		// Do nothing if already static
		if (__modifier instanceof CStaticModifier)
			return __modifier;
		
		// If there is no target modifier, just get the static one
		if (__modifier == null)
			return CStaticModifier._JUST_STATIC;
		
		// Otherwise wrap it
		return new CStaticModifier(__modifier);
	}
}
