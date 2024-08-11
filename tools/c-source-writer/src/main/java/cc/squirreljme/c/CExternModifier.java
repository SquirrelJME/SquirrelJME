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
 * This is a modifier which is {@code extern}.
 *
 * @since 2023/06/05
 */
public final class CExternModifier
	extends __SinglePrefixModifier__
{
	/** Constant for only extern. */
	public static final CExternModifier EXTERN =
		new CExternModifier(null);
	
	/**
	 * Initializes the extern wrapper.
	 * 
	 * @param __modifier The modifier to wrap.
	 * @since 2023/06/05
	 */
	private CExternModifier(CModifier __modifier)
	{
		super("extern", __modifier);
	}
	
	/**
	 * Is this extern?
	 * 
	 * @param __modifier The modifier to check.
	 * @return If this it is extern.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public static boolean isExtern(CModifier __modifier)
		throws NullPointerException
	{
		return CExternModifier.EXTERN.isSame(__modifier);
	}
	
	/**
	 * Wraps the modifier to make it {@code extern}.
	 * 
	 * @param __modifier The modifier to wrap, may be {@code null}.
	 * @return The modifier which is now {@code extern}.
	 * @throws IllegalArgumentException If the modifier would not be valid.
	 * @since 2023/06/05
	 */
	public static CModifier of(CModifier __modifier)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error CW0e Cannot have a static extern modifier.} */
		if (__modifier instanceof CStaticModifier)
			throw new IllegalArgumentException("CW0e");
		
		// If the modifier already is extern, do nothing with it
		// Note that functions are always implicitly extern
		if (__modifier instanceof CExternModifier ||
			__modifier instanceof CFunctionType)
			return __modifier;
		
		// If there is no target modifier, just get the extern one
		if (__modifier == null)
			return CExternModifier.EXTERN;
		
		// Otherwise wrap it
		return new CExternModifier(__modifier);
	}
}
