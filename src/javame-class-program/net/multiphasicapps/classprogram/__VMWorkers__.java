// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This class contains the loaders and such of workers which perform
 * computations and such.
 *
 * @since 2016/04/08
 */
class __VMWorkers__
{
	/** The number of operations. */
	private static final int _NUM_OPS =
		512;
	
	/** Handler shift count. */
	private static final int _HANDLER_SHIFT =
		4;
	
	/** Handler shift mask. */
	private static final int _HANDLER_MASK =
		(1 << _HANDLER_SHIFT) - 1;
	
	/** Handlers for operations. */
	private final Reference<__Worker__>[] _HANDLERS =
		__makeByteOpRefArray();
	
	/** Handlers for determination. */
	private final Reference<__Determiner__>[] _DETERMINERS =
		__makeDeterminerRefArray();
	
	/**
	 * Initializes the worker dispatcher.
	 *
	 * @since 2016/04/08
	 */
	__VMWorkers__()
	{
	}
	
	/**
	 * Obtains the type determination for the given opcode.
	 *
	 * @param __code The operation to get the determiner for.
	 * @return The determiner for the given operation.
	 * @throws CPProgramException If there is no determiner for the given
	 * operation.
	 * @since 2016/04/18
	 */
	__Determiner__ __determine(int __code)
		throws CPProgramException
	{
		return this.<__Determiner__>__lookupInternal(__code,
			__Determiner__.class, _DETERMINERS,
			"net.multiphasicapps.classprogram.__Determine", "__");
	}
	
	/**
	 * Obtains from the cache or caches a class which is used for the handling
	 * of byte code operations. This is to prevent this file from being a
	 * massive 5000 line file.
	 *
	 * @param __code The opcode, if the value is >= 0x100 then it is shifted
	 * down to not become wide.
	 * @return The handler for the given operation.
	 * @throws CPProgramException If the opcode is not valid.
	 * @since 2016/03/23
	 */
	__Worker__ __lookup(int __code)
		throws CPProgramException
	{
		return this.<__Worker__>__lookupInternal(__code, __Worker__.class,
			_HANDLERS, "net.multiphasicapps.classprogram.__OpHandler", "__");
	}
	
	/**
	 * Obtains from the cache or caches a class which is used for the handling
	 * of byte code operations. This is to prevent this file from being a
	 * massive 5000 line file. This is the general lookup which can do it for
	 * any kind of class.
	 *
	 * {@squirreljme.error CP0n No handler exists for this given
	 * instruction. (The opcode)}
	 * 
	 * @param <G> The type of value to return
	 * @param __code The opcode, if the value is >= 0x100 then it is shifted
	 * down to not become wide.
	 * @param __cl The class type to lookup.
	 * @param __from The source array.
	 * @param __prefix The class prefix.
	 * @param __suffix The class suffix.
	 * @return The handler for the given operation.
	 * @throws CPProgramException If the opcode is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/18
	 */
	private <G> G __lookupInternal(int __code, Class<G> __cl,
		Reference<G>[] __from, String __prefix, String __suffix)
		throws CPProgramException, NullPointerException
	{
		// Check
		if (__prefix == null || __suffix == null || __cl == null ||
			__from == null)
			throw new NullPointerException("NARG");
		
		// Is this wide?
		int upper = (__code & (~0xFF));
		if (upper != 0)
		{
			// Not wide?
			if (upper != (CPOpcodes.WIDE << 8))
				throw new CPProgramException(String.format("CP0n %d", __code));
			
			// Make it down to 0x100 level
			__code = 0x100 | (__code & 0xFF);
		}
		
		// Major shift
		int major = __code >>> _HANDLER_SHIFT;
		
		// Out of range?
		if (major < 0 || major >= __from.length)
			throw new CPProgramException(String.format("CP0n %d", __code));
		
		// Lock on the handlers
		synchronized (__from)
		{
			// Get reference
			Reference<G> ref = __from[major];
			G rv;
			
			// Needs caching?
			if (ref == null || null == (rv = ref.get()))
			{
				// Can fail
				try
				{
					// Find class first
					Class<?> ohcl = Class.forName(__prefix +
						(__code & ~_HANDLER_MASK) + "To" +
						(__code | _HANDLER_MASK) + __suffix);
					
					// Create instance of it
					rv = __cl.cast(ohcl.newInstance());
				}
				
				// Could not find, create, or cast.
				catch (InstantiationException|IllegalAccessException|
					ClassNotFoundException|ClassCastException e)
				{
					throw new CPProgramException(
						String.format("CP0n %d", __code), e);
				}
				
				// Cache it
				__from[major] = new WeakReference<G>(rv);
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * This creates the byte operation handler reference array.
	 *
	 * @return The handler cache array.
	 * @since 2016/03/23
	 */
	@SuppressWarnings({"unchecked"})
	private static Reference<__Worker__>[] __makeByteOpRefArray()
	{
		return (Reference<__Worker__>[])
			((Object)new Reference[_NUM_OPS >>> _HANDLER_SHIFT]);
	}
	
	/**
	 * This creates the determiner cache.
	 *
	 * @return The determiner cache.
	 * @since 2016/04/18
	 */
	@SuppressWarnings({"unchecked"})
	private static Reference<__Determiner__>[] __makeDeterminerRefArray()
	{
		return (Reference<__Determiner__>[])
			((Object)new Reference[_NUM_OPS >>> _HANDLER_SHIFT]);
	}
	
	/**
	 * Thrown when the operation is not known.
	 *
	 * @since 2016/04/09
	 */
	static final class __UnknownOp__
		extends RuntimeException
	{
	}
	
	/**
	 * This is the base class for all determination helpers.
	 *
	 * @since 2016/04/18
	 */
	static abstract class __Determiner__
	{
		/**
		 * Initialize if needed.
		 *
		 * @since 2016/04/18
		 */
		__Determiner__()
		{
		}
	}
	
	/**
	 * This is the actual worker base.
	 *
	 * @since 2016/04/08
	 */
	static abstract class __Worker__
	{
		/**
		 * Any needed initialization.
		 *
		 * @since 2016/04/09
		 */
		__Worker__()
		{
		}
		
		/**
		 * Performs the computation as required.
		 *
		 * @param __cm The computation machine.
		 * @param __a First pass-through.
		 * @param __b Second pass-through.
		 * @param __op The operation to work with.
		 * @since 2016/04/09
		 */
		abstract void compute(CPComputeMachine<? extends Object> __cm,
			Object __a, CPOp __op);
		
		/**
		 * Casts down the compute machine to object.
		 *
		 * @param __cm The compute machine to cast.
		 * @return The compute machine with its CAPs removed.
		 * @since 2016/04/09
		 */
		@SuppressWarnings({"unchecked"})
		final CPComputeMachine<Object> __castCM(
			CPComputeMachine<? extends Object> __cm)
		{
			return (CPComputeMachine<Object>)((Object)__cm);
		}
	}
}

