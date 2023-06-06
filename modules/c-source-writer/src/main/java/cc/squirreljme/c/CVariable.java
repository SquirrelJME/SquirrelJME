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
 * C variable type.
 *
 * @since 2023/05/30
 */
public class CVariable
	implements CDeclarable, CDefinable
{
	/** The type of this variable. */
	public final CType type;
	
	/** The name of this variable. */
	public final String name;
	
	/**
	 * Initializes a variable.
	 * 
	 * @param __type The type used.
	 * @param __name The name of the variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/30
	 */
	private CVariable(CType __type, String __name)
		throws NullPointerException
	{
		if (__type == null || __name == null)
			throw new NullPointerException("NARG");
		
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
		return this.name.equals(o.name) &&
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
		// If the type is already modified, we need not do anything
		if (this.isExtern())
			return this;
		
		// Otherwise setup new variable
		return CVariable.of(CExternModifier.EXTERN, this.type, this.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @isnce 2023/05/30
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode() ^ this.type.hashCode();
	}
	
	/**
	 * Returns whether this is extern.
	 * 
	 * @return If this is {@code extern}.
	 * @since 2023/06/05
	 */
	public boolean isExtern()
	{
		// We need to figure out if this is extern
		CType type = this.type;
		if (type instanceof CModifiedType)
		{
			CModifiedType modifiedType = (CModifiedType)type;
			
			// Already extern?
			return modifiedType.modifier instanceof CExternModifier;
		}
		
		// Not extern
		return false;
	}
	
	/**
	 * Returns if this variable is static.
	 * 
	 * @return If this variable is static.
	 * @since 2023/06/05
	 */
	public boolean isStatic()
	{
		// We need to figure out if this is extern
		CType type = this.type;
		if (type instanceof CModifiedType)
		{
			CModifiedType modifiedType = (CModifiedType)type;
			
			// Already static?
			return modifiedType.modifier instanceof CStaticModifier;
		}
		
		// Not extern
		return false;
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
		return new CVariable(CModifiedType.of(__modifier, __type), __name);
	}
}
