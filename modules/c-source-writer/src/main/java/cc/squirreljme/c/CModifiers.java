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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Multiple modifiers.
 *
 * @since 2023/05/29
 */
public class CModifiers
	implements CModifier
{
	/** Extern const as it is very common. */
	public static final CModifier EXTERN_CONST =
		CExternModifier.of(CBasicModifier.CONST);
	
	/** All the modifiers. */
	protected final List<CModifier> modifiers;
	
	/** Tokens that represent the modifiers. */
	private volatile Reference<List<String>> _tokens;
	
	/**
	 * Initializes multiple modifiers.
	 * 
	 * @param __of The modifiers to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	private CModifiers(Set<CModifier> __of)
		throws NullPointerException
	{
		if (__of == null || __of.isEmpty())
			throw new NullPointerException("NARG");
		
		this.modifiers = UnmodifiableList.of(new ArrayList<>(__of));
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
		if (!(__o instanceof CModifiers))
			return false;
		
		return this.modifiers.equals(((CModifiers)__o).modifiers);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public int hashCode()
	{
		return this.modifiers.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public List<String> modifierTokens()
	{
		Reference<List<String>> ref = this._tokens;
		List<String> rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			List<String> build = new ArrayList<>();
			for (CModifier modifier : this.modifiers)
				build.addAll(modifier.modifierTokens());
			
			rv = UnmodifiableList.of(build);
			this._tokens = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Initializes multiple modifiers.
	 * 
	 * @param __of The modifiers to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public static CModifier of(CModifier... __of)
		throws NullPointerException
	{
		if (__of == null || __of.length == 0)
			throw new NullPointerException("NARG");
		
		// If there is just a single modifier, there is no real point in doing
		// much to wrap them because it will only ever contain a single one
		if (__of.length == 1)
			return __of[0];
		
		// Build modifiers accordingly
		boolean hasExtern = false;
		Set<CModifier> build = new LinkedHashSet<>();
		
		// Put all existing modifiers onto the queue for processing
		Deque<CModifier> queue = new ArrayDeque<>(Arrays.asList(__of));
		
		// Keep processing the queue
		while (!queue.isEmpty())
		{
			CModifier modifier = queue.pollFirst();
			
			// Cannot be null
			if (modifier == null)
				throw new NullPointerException("NARG");
			
			// Is there an extern modifier? Move it to the outside since
			// there can only be one of these
			if (modifier instanceof CExternModifier)
			{
				hasExtern = true;
				
				// Extern has a target, so process that next
				queue.addFirst(((CExternModifier)modifier).wrapped);
			}
			
			// Multiple modifiers
			else if (modifier instanceof CModifiers)
			{
				// Add everything to the queue in reverse order otherwise
				// every time this is used, the order will swap
				List<CModifier> subList = ((CModifiers)modifier).modifiers;
				ListIterator<CModifier> it =
					subList.listIterator(subList.size());
				while (it.hasPrevious())
					queue.addFirst(it.previous());
			}
			
			// Single modifier, most likely a basic or custom one
			else
				build.add(modifier);
		}
		
		// This cannot technically be empty
		if (build.isEmpty())
		{
			// If this is just extern
			if (hasExtern)
				return CExternModifier.of(null);
			
			// Otherwise, there is no modifier here
			return null;
		}
		
		// If there is an extern modifier somewhere, we wrap the result with
		// one so that extern is always first
		CModifiers result = new CModifiers(build);
		return (hasExtern ? CExternModifier.of(result) : result);
	}
}
