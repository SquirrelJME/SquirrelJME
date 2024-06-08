// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.c.std.CTypeProvider;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * C variable type.
 *
 * @since 2023/05/30
 */
public class CVariable
	implements CExpression
{
	/** Null reference. */
	public static final CVariable NULL =
		CVariable.of(CPrimitiveType.VOID.pointerType(), "NULL");
	
	/** The type of this variable. */
	public final CType type;
	
	/** The expression of this variable. */
	public final CIdentifier name;
	
	/** The tokens used for declaring this variable. */
	private volatile Reference<List<String>> _declareTokens;
	
	/**
	 * Initializes a variable.
	 * 
	 * @param __type The type used.
	 * @param __name The name of the variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/30
	 */
	private CVariable(CType __type, CIdentifier __name)
		throws NullPointerException
	{
		if (__type == null || __name == null)
			throw new NullPointerException("NARG");
		
		this.type = __type;
		this.name = __name;
	}
	
	/**
	 * The tokens used to define this variable.
	 * 
	 * @return The tokens used for declaration.
	 * @since 2023/06/24
	 */
	public List<String> declareTokens()
	{
		Reference<List<String>> ref = this._declareTokens;
		List<String> rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = UnmodifiableList.of(this.type.declareTokens(this.name));
			this._declareTokens = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/30
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
		return CVariable.of(
			CModifiedType.of(CExternModifier.EXTERN, this.type), this.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/30
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
			return CExternModifier.isExtern(((CModifiedType)type).modifier);
		
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
	 * Renames this variable.
	 * 
	 * @param __newIdentifier The new name.
	 * @return The variable with the new name.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/25
	 */
	public CVariable rename(String __newIdentifier)
		throws NullPointerException
	{
		return this.rename(CIdentifier.of(__newIdentifier));
	}
	
	/**
	 * Renames this variable.
	 * 
	 * @param __newIdentifier The new name.
	 * @return The variable with the new name.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/25
	 */
	public CVariable rename(CIdentifier __newIdentifier)
		throws NullPointerException
	{
		if (__newIdentifier == null)
			throw new NullPointerException("NARG");
		
		return new CVariable(this.type, __newIdentifier);
	}
	
	/**
	 * Makes this variable {@code static}.
	 *
	 * @return This variable as a {@code static}.
	 * @since 2024/06/08
	 */
	public CVariable staticize()
	{
		if (this.isStatic())
			return this;
		
		return CVariable.of(
			CModifiedType.of(CStaticModifier.STATIC, this.type), this.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/03
	 */
	@Override
	public List<String> tokens()
	{
		return this.name.tokens();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/09
	 */
	@Override
	public String toString()
	{
		return this.tokens().toString();
	}
	
	/**
	 * Returns the type used.
	 * 
	 * @return The type.
	 * @since 2023/06/24
	 */
	public CType type()
	{
		return this.type;
	}
	
	/**
	 * Returns the type as a specified type.
	 * 
	 * @param <T> The type used.
	 * @param __type The type used.
	 * @return The type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public <T extends CType> T type(Class<T> __type)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		return __type.cast(this.type);
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
		return CVariable.of(__type, CIdentifier.of(__name));
	}
	
	/**
	 * Initializes a variable.
	 * 
	 * @param __type The type used.
	 * @param __name The name of the variable.
	 * @return The created variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public static CVariable of(CTypeProvider __type, String __name)
		throws NullPointerException
	{
		return CVariable.of(__type, CIdentifier.of(__name));
	}
	
	/**
	 * Initializes a variable.
	 * 
	 * @param __type The type used.
	 * @param __name The name of the variable.
	 * @return The created variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	public static CVariable of(CType __type, CIdentifier __name)
		throws NullPointerException
	{
		return new CVariable(__type, __name);
	}
	
	/**
	 * Initializes a variable.
	 * 
	 * @param __type The type used.
	 * @param __name The name of the variable.
	 * @return The created variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public static CVariable of(CTypeProvider __type, CIdentifier __name)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		return CVariable.of(__type.type(), __name);
	}
}
