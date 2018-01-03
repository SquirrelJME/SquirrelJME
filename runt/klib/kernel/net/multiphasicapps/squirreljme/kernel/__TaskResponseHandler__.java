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
import net.multiphasicapps.squirreljme.runtime.clsyscall.PacketTypes;
import net.multiphasicapps.squirreljme.runtime.packets.Packet;
import net.multiphasicapps.squirreljme.runtime.packets.PacketReader;
import net.multiphasicapps.squirreljme.runtime.packets.PacketStreamHandler;
import net.multiphasicapps.squirreljme.runtime.packets.PacketWriter;

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
	public Packet handle(Packet __p)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		KernelTask task = this.__task();
		
		// Depends on the type
		switch (__p.type())
		{
				// Client said hello
			case PacketTypes.HELLO:
				task._gothello = true;
				return null;
				
				// Initialization complete
			case PacketTypes.INITIALIZED:
				task._gotinitcomplete = true;
				return null;
				
				// Map service
			case PacketTypes.MAP_SERVICE:
				{
					PacketReader r = __p.createReader();
					String service = r.readString();
					
					// Map service
					String mapped = task.__tasks().__kernel().
						mapService(service);
					if (mapped == null)
						mapped = "";
					
					// Respond
					Packet rv = __p.respond();
					rv.createWriter().writeString(mapped);
					return rv;
				}
				
				// {@squirreljme.error AP0e Unknown packet. (The packet)}
			default:
				throw new RuntimeException(String.format("AP0e %s", __p));
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

