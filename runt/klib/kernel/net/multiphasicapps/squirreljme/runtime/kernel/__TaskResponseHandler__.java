// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

import java.lang.ref.Reference;
import net.multiphasicapps.squirreljme.runtime.clsyscall.PacketStreamHandler;
import net.multiphasicapps.squirreljme.runtime.clsyscall.PacketTypes;
import net.multiphasicapps.squirreljme.runtime.clsyscall.StringConvert;

/**
 * This handles responses for kernel tasks.
 *
 * @since 2018/01/01
 */
final class __TaskResponseHandler__
	implements PacketStreamHandler
{
	/** The task to handle. */
	protected final Reference<KernelTask> task;
	
	/**
	 * Initializes the response handler.
	 *
	 * @param __cr The reference to the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/01
	 */
	__TaskResponseHandler__(Reference<KernelTask> __tr)
		throws NullPointerException
	{
		if (__tr == null)
			throw new NullPointerException("NARG");
		
		this.task = __tr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/01
	 */
	@Override
	public void end()
	{
		// Tell the handler that the task was terminated and that it should
		// go away now
		this.__task().__terminated();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/01
	 */
	@Override
	public byte[] handle(int __t, byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __b.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		KernelTask task = this.__task();
		
		switch (__t)
		{
				// Client said hello
			case PacketTypes.HELLO:
				task._gothello = true;
				return null;
				
				// Initialization complete
			case PacketTypes.INITIALIZATION_COMPLETE:
				task._gotinitcomplete = true;
				return null;
				
				// Map service
			case PacketTypes.MAP_SERVICE:
				String rv = task.__tasks().__kernel().
					mapService(StringConvert.bytesToString(__b, __o, __l));
				if (rv == null)
					return null;
				return StringConvert.stringToBytes(rv);
			
				// {@squirreljme.error AP0e Unknown response type. (The type)}
			default:
				throw new RuntimeException(String.format("AP0e", __t));
		}
	}
	
	/**
	 * Returns the task.
	 *
	 * @return The task.
	 * @throws RuntimeException If it was garbage collected.
	 * @since 2018/01/01
	 */
	final KernelTask __task()
		throws RuntimeException
	{
		// {@squirreljme.error AP0d The task was garbage collected.}
		KernelTask rv = this.task.get();
		if (rv == null)
			throw new RuntimeException("AP0d");
		return rv;
	}
}

