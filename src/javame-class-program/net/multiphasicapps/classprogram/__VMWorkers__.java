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
	/** Handler shift count. */
	private static final int _HANDLER_SHIFT =
		4;
	
	/** Handler shift mask. */
	private static final int _HANDLER_MASK =
		(1 << _HANDLER_SHIFT) - 1;
	
	/** Handlers for operations. */
	private final Reference<__Worker__>[] _HANDLERS =
		__makeByteOpRefArray();
	
	/**
	 * Initializes the worker dispatcher.
	 *
	 * @since 2016/04/08
	 */
	__VMWorkers__()
	{
	}
	
	/**
	 * Obtains from the cache or caches a class which is used for the handling
	 * of byte code operations. This is to prevent this file from being a
	 * massive 5000 line file.
	 *
	 * {@squirreljme.error CP0g No handler exists for this given
	 * instruction. (The opcode)}
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
		// Is this wide?
		int upper = (__code & (~0xFF));
		if (upper != 0)
		{
			// Not wide?
			if (upper != (CPOpcodes.WIDE << 8))
				throw new CPProgramException(String.format("CP0g %d", __code));
			
			// Make it down to 0x100 level
			__code = 0x100 | (__code & 0xFF);
		}
		
		// Major shift
		int major = __code >>> _HANDLER_SHIFT;
		
		// Get the handler array and check bounds
		Reference<__Worker__>[] refs = _HANDLERS;
		
		// Out of range?
		if (major < 0 || major >= refs.length)
			throw new CPProgramException(String.format("CP0g %d", __code));
		
		// Lock on the handlers
		synchronized (refs)
		{
			// Get reference
			Reference<__Worker__> ref = refs[major];
			__Worker__ rv;
			
			// Needs caching?
			if (ref == null || null == (rv = ref.get()))
			{
				// Can fail
				try
				{
					// Find class first
					Class<?> ohcl = Class.forName("net.multiphasicapps." +
						"classprogram.__OpHandler" +
						(__code & ~_HANDLER_MASK) + "To" +
						(__code | _HANDLER_MASK) + "__");
					
					// Create instance of it
					rv = (__Worker__)ohcl.newInstance();
				}
				
				// Could not find, create, or cast.
				catch (InstantiationException|IllegalAccessException|
					ClassNotFoundException|ClassCastException e)
				{
					throw new CPProgramException(
						String.format("CP0g %d", __code), e);
				}
				
				// Cache it
				refs[major] = new WeakReference<>(rv);
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
			((Object)new Reference[512 >>> _HANDLER_SHIFT]);
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
		abstract void compute(
			CPComputeMachine<? extends Object, ? extends Object> __cm,
			Object __a, Object __b, CPOp __op);
		
		/**
		 * Casts down the compute machine to object.
		 *
		 * @param __cm The compute machine to cast.
		 * @return The compute machine with its CAPs removed.
		 * @since 2016/04/09
		 */
		@SuppressWarnings({"unchecked"})
		final CPComputeMachine<Object, Object> __castCM(
			CPComputeMachine<? extends Object, ? extends Object> __cm)
		{
			return (CPComputeMachine<Object, Object>)((Object)__cm);
		}
	}
}

