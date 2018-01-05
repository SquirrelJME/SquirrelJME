// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.callerbase;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.kernel.ipc.base.PacketTypes;
import net.multiphasicapps.squirreljme.kernel.packets.Packet;
import net.multiphasicapps.squirreljme.kernel.packets.PacketStream;
import net.multiphasicapps.squirreljme.kernel.packets.PacketStreamHandler;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemCaller;

/**
 * This represents the base for the client and server system callers which
 * provides a basis for common code between the two classes.
 *
 * @since 2018/01/04
 */
public abstract class BaseCaller
	extends SystemCaller
{
	/** The packet stream which links to the kernel. */
	protected final PacketStream stream;
	
	/** Service map cache, since they always have single instances. */
	private final Map<Class<?>, Object> _services =
		new HashMap<>();
	
	/**
	 * Initializes the base caller.
	 *
	 * @param __in The input stream.
	 * @param __out The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/04
	 */
	protected BaseCaller(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		this.stream = new PacketStream(__in, __out, new __Handler__());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public final <C> C service(Class<C> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		PacketStream stream = this.stream;
		Map<Class<?>, Object> services = this._services;
		synchronized (services)
		{
			// If the service already exists then use it
			Object rv = services.get(__cl);
			if (rv != null)
				return __cl.cast(rv);
			
			// Ask the kernel to map the service to a class which can create
			// client instances
			int index;
			String mapped;
			try (Packet p = stream.farm().create(PacketTypes.MAP_SERVICE))
			{
				// Just a single class is sent to the server
				p.writeString(0, __cl.getName());
				
				// The server responds with the index the server is located at
				// along with the class it maps to. The index is needed because
				// communication on the channels is done by index only
				try (Packet r = stream.send(p))
				{
					index = r.readInt(0);
					mapped = r.readString(4);
					
					// No service exists for this class
					if (index <= 0 || mapped.length() <= 0)
						return null;
				}
			}
			
			throw new todo.TODO();
		}
	}
	
	/**
	 * Handler interface.
	 *
	 * @since 2018/01/04
	 */
	private final class __Handler__
		implements PacketStreamHandler
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/01/04
		 */
		@Override
		public void end()
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/04
		 */
		@Override
		public Packet handle(Packet __p)
			throws NullPointerException
		{
			if (__p == null)
				throw new NullPointerException("NARG");
			
			throw new todo.TODO();
		}
	}
}

