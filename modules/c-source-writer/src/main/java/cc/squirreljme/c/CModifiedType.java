// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents a type which is modified by a modifier.
 *
 * @since 2023/06/05
 */
public final class CModifiedType
	extends __CAbstractType__
{
	/** The modifier for the type. */
	protected final CModifier modifier;
	
	/** The type. */
	protected final CType type;
	
	/** The tokens that make up this modified type. */
	private volatile Reference<List<String>> _tokens;
	
	/**
	 * Initializes the modified type.
	 * 
	 * @param __modifier The modifier to use.
	 * @param __type The type to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/05
	 */
	private CModifiedType(CModifier __modifier, CType __type)
		throws NullPointerException
	{
		if (__modifier == null || __type == null)
			throw new NullPointerException("NARG");
		
		this.modifier = __modifier;
		this.type = __type;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType constType()
		throws IllegalArgumentException
	{
		return CModifiedType.of(CConstModifier.CONST, this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		// {@squirreljme.error CW0j Not a pointer that can be de-referenced.}
		if (!this.isPointer())
			throw new IllegalArgumentException("CW0j");
		
		// We might be a non-const pointer to a const, in which case if we
		// dereference our own pointer we get the type we are pointing to
		return this.type.dereferenceType();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		if (!(__o instanceof CModifiedType))
			return false;
		
		CModifiedType o = (CModifiedType)__o;
		return this.type.equals(o.type) &&
			this.modifier.equals(o.modifier);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public int hashCode()
	{
		return this.modifier.hashCode() ^
			this.type.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public boolean isPointer()
	{
		return this.type.isPointer();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		return CPointerType.of(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public List<String> tokens()
	{
		Reference<List<String>> ref = this._tokens;
		List<String> rv;
		
		// Build tokens?
		if (ref == null || (rv = ref.get()) == null)
		{
			List<String> build = new ArrayList<>();
			
			CModifier modifier = this.modifier;
			CType type = this.type;
			
			// These modifiers are prefixes on the left unless the type is
			// a pointer, in which is appears on the left
			if (modifier instanceof __SinglePrefixModifier__ &&
				!(type instanceof CPointerType))
			{
				build.addAll(modifier.tokens());
				build.addAll(type.tokens());
			}
			
			// Otherwise they attach to the right side (postfix)
			else
			{
				build.addAll(type.tokens());
				build.addAll(modifier.tokens());
			}
			
			rv = UnmodifiableList.of(build);
			this._tokens = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	List<String> __generateTokens(CTokenSet __set)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Modifies the given type.
	 * 
	 * @param __modifier The modifier to attach to the type.
	 * @param __type The type to modify.
	 * @return The modified type.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @throws NullPointerException If no type was specified.
	 * @since 2023/06/05
	 */
	public static CType of(CModifier __modifier, CType __type)
		throws IllegalArgumentException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Not actually modifying the type?
		if (__modifier == null)
			return __type;
		
		// If the type is already modified, merge the modifiers together
		if (__type instanceof CModifiedType)
		{
			CModifiedType modifiedType = (CModifiedType)__type;
			return CModifiedType.of(CModifiers.of(__modifier,
				modifiedType.modifier), modifiedType.type);
		}
		
		// Functions are limited in what they can become
		if (__type instanceof CFunctionType)
		{
			// {@squirreljme.error CW0h Functions may only be static.}
			if (!CStaticModifier.STATIC.equals(__modifier))
				throw new IllegalArgumentException("CW0h");
			
			return new CModifiedType(CStaticModifier.STATIC, __type);
		}
		
		// Build modified type
		return new CModifiedType(__modifier, __type);
	}
}
