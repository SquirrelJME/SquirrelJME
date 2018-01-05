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
import net.multiphasicapps.squirreljme.kernel.service.ClientInstance;
import net.multiphasicapps.squirreljme.kernel.service.ClientInstanceFactory;
import net.multiphasicapps.squirreljme.kernel.service.ServicePacketStream;
import net.multiphasicapps.squirreljme.kernel.service.ServiceServer;
import net.multiphasicapps.squirreljme.kernel.service.ServiceServerFactory;
import net.multiphasicapps.squirreljme.runtime.cldc.NoSuchServiceException;
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
	
	/** Mapping of client classes to instances. */
	private final Map<Class<?>, Integer> _instancemap =
		new HashMap<>();
	
	/** Services which have been initialized for clients. */
	private final ClientInstance[] _instances;
	
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
		
		PacketStream stream = new PacketStream(__in, __out, new __Handler__());
		this.stream = stream;
		
		// Initializes the client instance set with the fixed number of
		// services which are available
		try (Packet p = stream.farm().create(PacketTypes.SERVICE_COUNT, 0))
		{
			try (Packet r = stream.send(p))
			{
				this._instances = new ClientInstance[r.readInteger(0)];
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public final <C> C service(Class<C> __cl)
		throws NoSuchServiceException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		int svdx;
		String rawcif;
		
		PacketStream stream = this.stream;
		
		// First try to map the service to an index
		Map<Class<?>, Integer> instancemap = this._instancemap;
		synchronized (instancemap)
		{
			Integer idxi = instancemap.get(__cl);
			if (idxi != null)
			{
				svdx = idxi;
				rawcif = null;
			}
			
			// Request it from the server
			else
				try (Packet p = stream.farm().create(PacketTypes.MAP_SERVICE))
				{
					p.writeString(0, __cl.getName());
					
					// Tell server to map it
					try (Packet r = stream.send(p))
					{
						svdx = r.readInteger(0);
						
						// The class will only be set if the service is valid
						if (svdx > 0)
							rawcif = r.readString(4);
						else
							rawcif = null;
					}
				}
		}
		
		// {@squirreljme.error BG02 No such service for the given class exists.
		// (The class to provide a service for)}
		if (svdx <= 0)
			throw new NoSuchServiceException(String.format("BG03 %s", __cl));
		
		// Use a pre-initialized instance or setup a new one
		ClientInstance[] instances = this._instances;
		synchronized (instances)
		{
			// Has it already been initialized?
			ClientInstance rv = instances[svdx];
			if (rv != null)
				return __cl.cast(rv);
			
			// The factory creates client instances
			ClientInstanceFactory ssf;
			try
			{
				ssf = (ClientInstanceFactory)
					(Class.forName(rawcif).newInstance());
			}
			
			// {@squirreljme.error BG04 Failed to initialize the factory for
			// creating the client instance for a service. (The requested
			// service)}
			catch (ClassNotFoundException|IllegalAccessException|
				InstantiationException e)
			{
				throw new RuntimeException(String.format("BG04 %s", __cl), e);
			}
			
			// Create instance of the client service
			rv = ssf.createClient(new ServicePacketStream(stream, svdx));
			instances[svdx] = rv;
			return __cl.cast(rv);
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
			
			switch (__p.type())
			{
					// {@squirreljme.error BG01 Unknown packet. (The packet)}
				default:
					throw new IllegalArgumentException(
						String.format("BG01 %s", __p));
			}
		}
	}
}

