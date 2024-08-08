// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Single prefix modifier.
 *
 * @since 2023/06/05
 */
abstract class __SinglePrefixModifier__
	implements CModifier
{
	/** The wrapped modifier. */
	protected final CModifier wrapped;
	
	/** The keyword for the modifier. */
	private final String _keyword;
	
	/** Tokens that represent the modifiers. */
	private volatile Reference<List<String>> _tokens;
	
	/**
	 * Initializes the prefixed modifier wrapper.
	 * 
	 * @param __keyword The key word for the modifier.
	 * @param __modifier The modifier to wrap.
	 * @throws NullPointerException If no keyword was specified.
	 * @since 2023/06/05
	 */
	__SinglePrefixModifier__(String __keyword, CModifier __modifier)
		throws NullPointerException
	{
		if (__keyword == null)
			throw new NullPointerException("NARG");
		
		this._keyword = __keyword;
		this.wrapped = __modifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof __SinglePrefixModifier__))
			return false;
		if (this.getClass() != __o.getClass())
			return false;
		
		__SinglePrefixModifier__ o = (__SinglePrefixModifier__)__o;
		return this._keyword.equals(o._keyword) &&
			Objects.equals(this.wrapped, o.wrapped);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.wrapped);
	}
	
	/**
	 * Is this the same prefix type?
	 * 
	 * @param __modifier The modifier to check.
	 * @return If the specified modifier is of the same type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public final boolean isSame(CModifier __modifier)
		throws NullPointerException
	{
		if (__modifier == null)
			throw new NullPointerException("NARG");
		
		// This one is simple
		if (__modifier.getClass() == this.getClass())
			return true;
		
		// Multiple ones?
		if (__modifier instanceof CModifiers)
			for (CModifier modifier : ((CModifiers)__modifier).modifiers)
				if (this.isSame(modifier))
					return true;
		
		// Not so
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public List<String> tokens()
		throws NullPointerException
	{
		Reference<List<String>> ref = this._tokens;
		List<String> rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			List<String> build = new ArrayList<>();
			this.__buildTokens(build);
			
			rv = UnmodifiableList.of(build);
			this._tokens = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Builds tokens that represent the modifier.
	 * 
	 * @param __result The result.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/05
	 */
	void __buildTokens(List<String> __result)
		throws NullPointerException
	{
		if (__result == null)
			throw new NullPointerException("NARG");
		
		__result.add(this._keyword);
		if (this.wrapped != null)
			__result.addAll(this.wrapped.tokens());
	}
}
