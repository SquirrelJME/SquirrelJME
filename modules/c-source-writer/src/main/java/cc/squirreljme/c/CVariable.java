// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.util.Objects;

/**
 * C variable type.
 *
 * @since 2023/05/30
 */
public class CVariable
	implements CDeclarable, CDefinable
{
	/** Modifier for the variable. */
	public final CModifier modifier;
	
	/** The type of this variable. */
	public final CType type;
	
	/** The name of this variable. */
	public final String name;
	
	/**
	 * Initializes a variable.
	 * 
	 * @param __modifier The variable modifier.
	 * @param __type The type used.
	 * @param __name The name of the variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/30
	 */
	private CVariable(CModifier __modifier, CType __type, String __name)
		throws NullPointerException
	{
		if (__type == null || __name == null)
			throw new NullPointerException("NARG");
		
		this.modifier = __modifier;
		this.type = __type;
		this.name = __name;
	}
	
	/**
	 * {@inheritDoc}
	 * @isnce 2023/05/30
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof CVariable))
			return false;
		
		CVariable o = (CVariable)__o;
		return Objects.equals(this.modifier, o.modifier) &&
			this.name.equals(o.name) &&
			this.type.equals(o.type);
	}
	
	/**
	 * Returns the {@code extern} variant of this variable. 
	 * 
	 * @return The {@code extern} version of this variable.
	 * @since 2023/06/05
	 */
	public CVariable extern()
	{
		// If the modifier is already extern, then nothing needs to be
		// done
		if (this.modifier instanceof CExternModifier)
			return this;
		
		// Otherwise setup new variable
		return CVariable.of(CExternModifier.of(this.modifier),
			this.type, this.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @isnce 2023/05/30
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.modifier) ^
			this.name.hashCode() ^ this.type.hashCode();
	}
	
	/**
	 * Initializes a variable.
	 * 
	 * @param __type The type used.
	 * @param __name The name of the variable.
	 * @return The created variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/30
	 */
	public static CVariable of(CType __type, String __name)
		throws NullPointerException
	{
		return CVariable.of(null, __type, __name);
	}
	
	/**
	 * Initializes a variable.
	 * 
	 * @param __modifier The variable modifier.
	 * @param __type The type used.
	 * @param __name The name of the variable.
	 * @return The created variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/30
	 */
	public static CVariable of(CModifier __modifier, CType __type,
		String __name)
		throws NullPointerException
	{
		return new CVariable(__modifier, __type, __name);
	}
}
