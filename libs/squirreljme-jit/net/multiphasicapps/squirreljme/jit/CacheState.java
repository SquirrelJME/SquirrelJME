// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.RandomAccess;
import java.util.Map;
import net.multiphasicapps.squirreljme.classformat.CodeVariable;

/**
 * This contains a single state which specifies local values which may be
 * cached on the stack. This also contains the mappings between variables and
 * their bound native information, if applicable.
 *
 * For maximum flexibility, this class is mutable.
 *
 * This class is not thread safe.
 *
 * @since 2017/02/16
 */
public class CacheState
{
	/** Stack code variables. */
	protected final Stack stack;
	
	/** Local code variables. */
	protected final Locals locals;
	
	/** Bindings between code variables and natural placements. */
	protected final Map<CodeVariable, Binding> bindings =
		new HashMap<>();
	
	/** The global cache state. */
	private volatile GlobalBinding _global;
	
	/**
	 * Initializes the cache state.
	 *
	 * @param __ms The number of stack items.
	 * @param __ml The number of local items.
	 * @since 2017/02/18
	 */
	CacheState(int __ms, int __ml)
	{
		// Allocate
		this.stack = new Stack(__ms);
		this.locals = new Locals(__ml);
	}
	
	/**
	 * Returns a copy of this cache state and returns the new copy.
	 *
	 * @return The copy of this state.
	 * @since 2017/02/18
	 */
	public CacheState copy()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Gets the binding of a given code variable.
	 *
	 * @param <B> The binding sub-class.
	 * @param __cl The class to cast to.
	 * @param __cv The variable to get the binding of.
	 * @return The binding for the given variable, or {@code null} if it has
	 * not been set.
	 * @throws ClassCastException If the class does not match.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/18
	 */
	public <B extends Binding> B getBinding(Class<B> __cl, CodeVariable __cv)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__cl == null || __cv == null)
			throw new NullPointerException("NARG");
		
		// Get
		return __cl.cast(this.bindings.get(__cv));
	}
	
	/**
	 * Returns the global binding which is associated with the cached state
	 * as a whole.
	 *
	 * @param <B> The class type of the gloal binding.
	 * @param __cl The class to cast to.
	 * @return The global binding.
	 * @throws ClassCastException If the class does not match.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/20
	 */
	public <B extends GlobalBinding> B getGlobal(Class<B> __cl)
		throws ClassCastException, NullPointerException
	{
		return __cl.cast(this._global);
	}
	
	/**
	 * Returns the cached local variable assignments.
	 *
	 * @return The cached local variables.
	 * @since 2017/02/18
	 */
	public CacheState.Locals locals()
	{
		return this.locals;
	}
	
	/**
	 * Sets the binding for the given code variable.
	 *
	 * @param __cv The variable to set a binding for.
	 * @param __b The binding of the variable, may be {@code null}.
	 * @return The old binding.
	 * @throws NullPointerException If no variable was specified.
	 * @since 2017/02/18
	 */
	public Binding setBinding(CodeVariable __cv, Binding __b)
	{
		// Check
		if (__cv == null)
			throw new NullPointerException("NARG");
		
		// Set
		return this.bindings.put(__cv, __b);
	}
	
	/**
	 * Sets the binding for the given code variable.
	 *
	 * @param <B> The type of binding to put.
	 * @param __cv The variable to set a binding for.
	 * @param __cl The target class binding.
	 * @param __b The binding of the variable, may be {@code null}.
	 * @return The old binding.
	 * @throws ClassCastException If the class does not match.
	 * @throws NullPointerException If no variable or class was specified.
	 * @since 2017/02/18
	 */
	public <B extends Binding> B setBinding(CodeVariable __cv, Class<B> __cl,
		B __b)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__cl == null || __cv == null)
			throw new NullPointerException("NARG");
		
		return __cl.cast(this.bindings.put(__cv, __b));
	}
	
	/**
	 * Sets the global binding.
	 *
	 * @param __g The global binding to use.
	 * @return The old global binding.
	 * @since 2017/02/20
	 */
	public GlobalBinding setGlobal(GlobalBinding __g)
	{
		GlobalBinding rv = this._global;
		this._global = __g;
		return rv;
	}
	
	/**
	 * Sets the global binding.
	 *
	 * @param <B> The class type of the global binding.
	 * @param __cl The class type of the global binding.
	 * @param __g The global binding to use.
	 * @return The old global binding.
	 * @throws ClassCastException If the class type differs.
	 * @throws NullPointerException If no class was specified.
	 * @since 2017/02/20
	 */
	public <B extends GlobalBinding> B setGlobal(Class<B> __cl, B __g)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		GlobalBinding rv = this._global;
		this._global = __cl.cast(__g);
		return __cl.cast(rv);
	}
	
	/**
	 * Returns the cached stack variable assignments.
	 *
	 * @return The cached stack variables.
	 * @since 2017/02/18
	 */
	public CacheState.Stack stack()
	{
		return this.stack;
	}
	
	/**
	 * Contains the mappings for cached local values.
	 *
	 * @since 2017/02/18
	 */
	public static final class Locals
		extends __Tread__
	{
		/**
		 * Initializes locals.
		 *
		 * @param __n Local count.
		 * @since 2017/02/18
		 */
		private Locals(int __n)
		{
			super(__n);
		}
	}
	
	/**
	 * Contains the mappings for cached stack values.
	 *
	 * @since 2017/02/18
	 */
	public static final class Stack
		extends __Tread__
	{
		/**
		 * Initializes stack.
		 *
		 * @param __n Stack count.
		 * @since 2017/02/18
		 */
		private Stack(int __n)
		{
			super(__n);
		}
	}
	
	/**
	 * This represents a slot within the cache state and stores the
	 * information 
	 *
	 * @since 2017/02/18
	 */
	public static final class Slot
	{
	}
	
	/**
	 * This is a tread of variabels which stores cached state.
	 *
	 * @since 2017/02/18
	 */
	private static abstract class __Tread__
		extends AbstractList<CodeVariable>
		implements RandomAccess
	{
		/** Variables used. */
		private final CodeVariable[] _vars;
		
		/**
		 * Initializes the tread.
		 *
		 * @param __n The tread size.
		 * @since 2017/02/18
		 */
		private __Tread__(int __n)
		{
			this._vars = new CodeVariable[__n];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/18
		 */
		@Override
		public CodeVariable get(int __i)
		{
			return this._vars[__i];
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/18
		 */
		@Override
		public CodeVariable set(int __i, CodeVariable __e)
		{
			CodeVariable[] vars = this._vars;
			CodeVariable rv = vars[__i];
			vars[__i] = __e;
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/18
		 */
		@Override
		public int size()
		{
			return this._vars.length;
		}
	}
}

