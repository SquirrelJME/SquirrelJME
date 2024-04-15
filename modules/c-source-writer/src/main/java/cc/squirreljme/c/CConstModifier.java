// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

/**
 * Constant C modifier.
 *
 * @since 2023/06/05
 */
public final class CConstModifier
	extends __SinglePrefixModifier__
{
	/** Constant for only const. */
	public static final CConstModifier CONST =
		new CConstModifier(null);
	
	/**
	 * Initializes the static wrapper.
	 * 
	 * @param __modifier The modifier to wrap.
	 * @since 2023/06/05
	 */
	private CConstModifier(CModifier __modifier)
	{
		super("const", __modifier);
	}
	
	/**
	 * Wraps the modifier to make it {@code const}.
	 * 
	 * @param __modifier The modifier to wrap, may be {@code null}.
	 * @return The modifier which is now {@code const}.
	 * @throws IllegalArgumentException If the modifier would not be valid.
	 * @since 2023/06/05
	 */
	public static CModifier of(CModifier __modifier)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error CW0k Cannot const const a modifier.} */
		if (__modifier instanceof CConstModifier)
			throw new IllegalArgumentException("CW0k");
		
		/* {@squirreljme.error CW0l Cannot have a const static or
		const extern, as in a static/extern.} */
		if (__modifier instanceof CExternModifier ||
			__modifier instanceof CStaticModifier)
			throw new IllegalArgumentException("CW0l");
		
		// If there is no target modifier, just get the static one
		if (__modifier == null)
			return CConstModifier.CONST;
		
		// Otherwise wrap it
		return new CConstModifier(__modifier);
	}
	
	/**
	 * Checks if the modifier is constant.
	 * 
	 * @param __modifier The modifier to check.
	 * @return If it is constant.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public static boolean isConst(CModifier __modifier)
		throws NullPointerException
	{
		if (__modifier == null)
			throw new NullPointerException("NARG");
		
		// This one is simple
		if (__modifier instanceof CConstModifier)
			return true;
		
		// Check multiple modifiers
		if (__modifier instanceof CModifiers)
			for (CModifier modifier : ((CModifiers)__modifier).modifiers)
				if (CConstModifier.isConst(modifier))
					return true;
		
		// Not one
		return false;
	}
}
