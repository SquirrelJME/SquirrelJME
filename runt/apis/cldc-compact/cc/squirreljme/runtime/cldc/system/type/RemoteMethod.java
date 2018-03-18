// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.type;

import cc.squirreljme.runtime.cldc.system.SystemCall;
import cc.squirreljme.runtime.cldc.system.SystemCallError;
import cc.squirreljme.runtime.cldc.system.SystemCallException;

/**
 * This represents a method which allows another process such as the kernel or
 * a user process to call into a method on the same stack in another address
 * space.
 *
 * @since 2018/03/18
 */
public abstract class RemoteMethod
{
	/**
	 * This is called on the outer end and actually performs the invocation.
	 *
	 * @param __args The arguments to the method call.
	 * @return The method call result.
	 * @since 2018/03/18
	 */
	protected abstract Object internalInvoke(Object[] __args);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final boolean equals(Object __o)
	{
		return this == __o;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int hashCode()
	{
		return super.hashCode();
	}
	
	/**
	 * Invokes the specified method.
	 *
	 * @param <R> The return type.
	 * @param __cl The return type.
	 * @param __args Arguments to the call.
	 * @return The result of the call.
	 * @throws ClassCastException If the return type is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public final <R> R invoke(Class<R> __cl, Object... __args)
		throws ClassCastException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Validate the arguments
		__args = SystemCall.validateArguments(__args);
		
		// Handle the call
		try
		{
			return __cl.cast(SystemCall.validateArgument(
				this.internalInvoke(__args)));
		}
		
		// Wrap exceptions so that local interfaces are consistent
		catch (RuntimeException|Error t)
		{
			// Already excpetions of the desired type
			if (t instanceof SystemCallException ||
				t instanceof SystemCallError)
				throw t;
			
			// Recursively initialize new exceptions accordingly
			if (t instanceof Error)
				throw SystemCall.<Error>wrapException(t);
			throw SystemCall.<RuntimeException>wrapException(t);
		}
	}
}

