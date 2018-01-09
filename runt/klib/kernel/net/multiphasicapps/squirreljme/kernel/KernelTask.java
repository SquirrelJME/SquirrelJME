// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.kernel.ipc.base.PacketTypes;
import net.multiphasicapps.squirreljme.kernel.packets.Packet;
import net.multiphasicapps.squirreljme.kernel.packets.PacketReader;
import net.multiphasicapps.squirreljme.kernel.packets.PacketStream;
import net.multiphasicapps.squirreljme.kernel.packets.PacketStreamHandler;
import net.multiphasicapps.squirreljme.kernel.packets.PacketWriter;
import net.multiphasicapps.squirreljme.kernel.service.ServerInstance;
import net.multiphasicapps.squirreljme.kernel.service.ServicePacketStream;
import net.multiphasicapps.squirreljme.kernel.service.ServiceProvider;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;

/**
 * This represents a task which is running within SquirrelJME.
 *
 * @since 2017/12/10
 */
public abstract class KernelTask
	implements SystemTask
{
	/** Reference to the owning kernel. */
	protected final Reference<Kernel> kernel;
	
	/** The task index. */
	protected final int index;
	
	/** The main class. */
	protected final String mainclass;
	
	/** Permissions for this task. */
	protected final KernelPermissions permissions;
	
	/** The packet stream to the child process. */
	private final PacketStream _stream;
	
	/** Instances of each service which is mapped to the kernel service set. */
	private final ServerInstance[] _instances;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the kernel task.
	 *
	 * @param __k The owning kernel.
	 * @param __id The index of the task.
	 * @param __l Launch parameters of the task.
	 * @param __in The input stream from the task.
	 * @param __out The output stream to the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/02
	 */
	protected KernelTask(Reference<Kernel> __k, int __id, KernelTaskLaunch __l,
		InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__k == null ||
			(__id != 0 && (__l == null || __in == null || __out == null)))
			throw new NullPointerException("NARG");
		
		this.kernel = __k;
		this.index = __id;
		
		Kernel kernel = __k.get();
		
		// Permissions will be setup accordingly
		KernelPermissions permissions = new KernelPermissions();
		
		// The server requires special initialization especially with the
		// streams because of the pipe nature.
		if (__id == 0)
		{
			// Main class represents the kernel, despite it having no main
			// method or MIDlet implementation
			this.mainclass = Kernel.class.getName();
			
			// Set the stream for this side to side A
			LoopbackStreams.Side side = kernel.loopback().sideA();
			__in = side.input();
			__out = side.output();
			
			// By default, grant all permissions to the kernel so that it can
			// do whatever it wants
		}
		
		// Clients initialize with other means
		else
		{
			// Set main class based on launched class
			if (true)
				throw new todo.TODO();
			
			// Initialize permissions
			if (true)
				throw new todo.TODO();
		}
		
		// Set permissions
		this.permissions = permissions;
		
		// Initialize the packet stream which is used to communicate between
		// the process and the kernel
		this._stream = new PacketStream(__in, __out, new __Handler__());
		
		// Initialize a base array for instances of services when they are
		// initialized when they are needed to
		this._instances = new ServerInstance[kernel.serviceCount()];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/09
	 */
	@Override
	public final void checkPermission(String __cl, String __n, String __a)
		throws NullPointerException, SecurityException
	{
		if (__cl == null || __n == null || __a == null)
			throw new NullPointerException("NARG");
		
		this.permissions.checkPermission(__cl, __n, __a);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public final int hashCode()
	{
		return this.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/27
	 */
	@Override
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * Returns the kernel which owns this task.
	 *
	 * @return The owning kernel.
	 * @throws RuntimeException If the kernel was garbage collected.
	 * @since 2018/01/02
	 */
	protected final Kernel kernel()
		throws RuntimeException
	{
		// {@squirreljme.error AP0h The kernel was garbage collected.}
		Kernel rv = this.kernel.get();
		if (rv == null)
			throw new RuntimeException("AP0h");
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public final String mainClass()
	{
		return this.mainclass;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/02
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = "Task#" + this.index));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/09
	 */
	@Override
	public final SystemTrustGroup trustGroup()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Handler for packets sent from the remote end of this task.
	 *
	 * @since 2018/01/02
	 */
	private final class __Handler__
		implements PacketStreamHandler
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/01/02
		 */
		@Override
		public void end()
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2018/01/01
		 */
		@Override
		public Packet handle(Packet __p)
			throws NullPointerException
		{
			if (__p == null)
				throw new NullPointerException("NARG");
			
			switch (__p.type())
			{
				case PacketTypes.SERVICE_COUNT:
					return this.__serviceCount(__p);
				
				case PacketTypes.MAP_SERVICE:
					return this.__mapService(__p);
				
				case PacketTypes.SERVICE_WITH_RESULT:
				case PacketTypes.SERVICE_NO_RESULT:
					return this.__servicePacket(__p);
				
					// {@squirreljme.error AP03 Unknown packet. (The packet)}
				default:
					throw new IllegalArgumentException(
						String.format("AP03 %s", __p));
			}
		}
		
		/**
		 * Maps the service.
		 *
		 * @param __p The packet with the service request.
		 * @return The service result.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/05
		 */
		private final Packet __mapService(Packet __p)
			throws NullPointerException
		{
			if (__p == null)
				throw new NullPointerException("NARG");
				
			Kernel kernel = KernelTask.this.kernel();
			PacketReader r = __p.createReader();
			
			// Locate class which was requested to be mapped
			Class<?> clclass;
			try
			{
				clclass = Class.forName(r.readString());
			}
			
			// {@squirreljme.error AP04 Could not obtain the class
			// for the client type.}
			catch (ClassNotFoundException e)
			{
				throw new RuntimeException("AP04", e);
			}
			
			Packet rv = __p.respond();
			PacketWriter w = rv.createWriter();
			
			// Get the index for this service
			int svdx = kernel.serviceIndex(clclass);
			if (svdx <= 0)
			{
				w.writeInteger(0);
				return rv;
			}
			
			// Always respond with the service server
			ServiceProvider sv = kernel.serviceGet(svdx);
			
			// See if a new server instance needs to be initialized
			ServerInstance[] instances = KernelTask.this._instances;
			synchronized (instances)
			{
				ServerInstance i = instances[svdx];
				if (i == null)
					instances[svdx] = sv.createInstance(KernelTask.this,
						new ServicePacketStream(KernelTask.this._stream,
							svdx));
			}
			
			// Record the response
			w.writeInteger(svdx);
			w.writeString(sv.clientFactoryClass().getName());
			return rv;
		}
		
		/**
		 * Returns the number of available services.
		 *
		 * @param __p The incoming packet.
		 * @return The number of available services.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/05
		 */
		private final Packet __serviceCount(Packet __p)
			throws NullPointerException
		{
			if (__p == null)
				throw new NullPointerException("NARG");
			
			Packet rv = __p.respond(4);
			rv.writeInteger(0, KernelTask.this._instances.length);
			return rv;
		}
		
		/**
		 * Handles a service packet.
		 *
		 * @param __p The packet from the service.
		 * @return The result of the service call, if one is expected.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/06
		 */
		private final Packet __servicePacket(Packet __p)
			throws NullPointerException
		{
			if (__p == null)
				throw new NullPointerException("NARG");
			
			// Read service details
			int svdx = __p.readUnsignedShort(0),
				type = __p.readUnsignedShort(2),
				plen = __p.readInteger(4);
			
			// Obtain the instance of the service.
			ServerInstance i;
			ServerInstance[] instances = KernelTask.this._instances;
			synchronized (instances)
			{
				// {@squirreljme.error AP07 Invalid service index.
				// (The service index)}
				if (svdx < 0 || svdx >= instances.length)
					throw new RuntimeException(String.format("AP07 %d", svdx));
				
				// {@squirreljme.error AP08 The service index is valid however
				// no service has been mapped and initialized yet.}
				i = instances[svdx];
				if (i == null)
					throw new RuntimeException(String.format("AP08 %d", svdx));
			}
			
			// Send to service
			try (Packet q = __p.farm().create(type, plen))
			{
				__p.readPacketData(8, q);
				
				return i.handlePacket(q);
			}
		}
	}
}

