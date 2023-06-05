// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
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
 * This is a modifier which is {@code extern}.
 *
 * @since 2023/06/05
 */
public final class CExternModifier
	implements CModifier
{
	/** Constant for only extern. */
	private static final CExternModifier _JUST_EXTERN =
		new CExternModifier(null);
	
	/** The wrapped modifier. */
	protected final CModifier wrapped;
	
	/** Tokens that represent the modifiers. */
	private volatile Reference<List<String>> _tokens;
	
	/**
	 * Initializes the extern wrapper.
	 * 
	 * @param __modifier The modifier to wrap.
	 * @since 2023/06/05
	 */
	private CExternModifier(CModifier __modifier)
	{
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
		if (!(__o instanceof CExternModifier))
			return false;
		
		return Objects.equals(this.wrapped, ((CExternModifier)__o).wrapped);
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
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public List<String> modifierTokens()
	{
		Reference<List<String>> ref = this._tokens;
		List<String> rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			List<String> build = new ArrayList<>(
				this.wrapped.modifierTokens());
			build.add(0, "extern");
			
			rv = UnmodifiableList.of(build);
			this._tokens = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Wraps the modifier to make it {@code extern}.
	 * 
	 * @param __modifier The modifier to wrap, may be {@code null}.
	 * @return The modifier which is now {@code extern}.
	 * @since 2023/06/05
	 */
	public static CModifier of(CModifier __modifier)
	{
		// If the modifier already is extern, do nothing with it
		if (__modifier instanceof CExternModifier)
			return __modifier;
		
		// If there is no target modifier, just get the extern one
		if (__modifier == null)
			return CExternModifier._JUST_EXTERN;
		
		// Otherwise wrap it
		return new CExternModifier(__modifier);
	}
}
