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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.ListIterator;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Multiple modifiers.
 *
 * @since 2023/05/29
 */
public class CModifiers
	implements CModifier
{
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
	private CModifiers(List<CModifier> __of)
		throws NullPointerException
	{
		if (__of == null || __of.isEmpty())
			throw new NullPointerException("NARG");
		
		this.modifiers = UnmodifiableList.of(__of);
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
	public List<String> tokens()
		throws NullPointerException
	{
		Reference<List<String>> ref = this._tokens;
		List<String> rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			List<String> build = new ArrayList<>();
			for (CModifier modifier : this.modifiers)
				build.addAll(modifier.tokens());
			
			rv = UnmodifiableList.of(build);
			this._tokens = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Initializes multiple modifiers.
	 * 
	 * @param __of The modifiers to use.
	 * @throws IllegalArgumentException If the modifier results in an invalid
	 * modifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public static CModifier of(CModifier... __of)
		throws IllegalArgumentException, NullPointerException
	{
		// If there is just a single modifier, there is no real point in doing
		// much to wrap them because it will only ever contain a single one
		if (__of != null && __of.length == 1)
			return __of[0];
		
		// Build modifiers accordingly
		boolean hasExtern = false;
		boolean hasStatic = false;
		List<CModifier> build = new ArrayList<>();
		
		// Put all existing modifiers onto the queue for processing
		Deque<CModifier> queue = new ArrayDeque<>();
		if (__of != null)
			queue.addAll(Arrays.asList(__of));
		
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
				/* {@squirreljme.error CW3a Cannot have multiple extern
				modifiers.} */
				if (hasExtern)
					throw new IllegalArgumentException("CW3a");
				
				hasExtern = true;
				
				// Extern has a target, so process that next
				CExternModifier sub = (CExternModifier)modifier;
				if (sub.wrapped != null)
					queue.addFirst(sub.wrapped);
			}
			
			// Is there a static modifier?
			else if (modifier instanceof CStaticModifier)
			{
				/* {@squirreljme.error CW3b Cannot have multiple static
				modifiers.} */
				if (hasStatic)
					throw new IllegalArgumentException("CW3b");
			
				hasStatic = true;
				
				CStaticModifier sub = (CStaticModifier)modifier;
				if (sub.wrapped != null)
					queue.addFirst(sub.wrapped);
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
			{
				/* {@squirreljme.error CW0l Duplicate modifier. (The modifier)} */
				if (build.contains(modifier))
					throw new IllegalArgumentException("CW0l " + modifier);
				
				build.add(modifier);
			}
		}
		
		/* {@squirreljme.error CW0g A modifier cannot be both static and
		extern.} */
		if (hasStatic && hasExtern)
			throw new IllegalArgumentException("CW0g");
		
		// There are no actual modifiers, likely other than extern/static
		if (build.isEmpty())
		{
			// Only just extern
			if (hasExtern)
				return CExternModifier.EXTERN;
			
			// Only just static
			else if (hasStatic)
				return CStaticModifier.STATIC;
			
			// Otherwise, there is no modifier here
			return null;
		}
		
		// If there is just a single modifier, we do not need to specify
		// multiple modifiers, so just use it directly
		if (build.size() == 1)
			return build.get(0);
		
		// If there is an extern modifier somewhere, we wrap the result with
		// one so that extern is always first, then static, then const...
		CModifiers result = new CModifiers(build);
		if (hasExtern)
			return CExternModifier.of(result);
		else if (hasStatic)
			return CStaticModifier.of(result);
		return result;
	}
}
