// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel;

import cc.squirreljme.kernel.ipc.base.PacketTypes;
import cc.squirreljme.kernel.packets.DatagramIn;
import cc.squirreljme.kernel.packets.DatagramOut;
import cc.squirreljme.kernel.packets.LoopbackDatagramDuplex;
import cc.squirreljme.kernel.packets.Packet;
import cc.squirreljme.kernel.packets.PacketFarm;
import cc.squirreljme.kernel.packets.PacketReader;
import cc.squirreljme.kernel.packets.PacketStream;
import cc.squirreljme.kernel.packets.PacketStreamHandler;
import cc.squirreljme.kernel.packets.PacketWriter;
import cc.squirreljme.kernel.service.ServerInstance;
import cc.squirreljme.kernel.service.ServicePacketStream;
import cc.squirreljme.kernel.service.ServiceProvider;
import cc.squirreljme.runtime.cldc.system.SystemCallImplementation;
import cc.squirreljme.runtime.cldc.system.SystemFunction;
import cc.squirreljme.runtime.cldc.system.VoidType;
import cc.squirreljme.runtime.cldc.SystemTask;
import cc.squirreljme.runtime.cldc.SystemTrustGroup;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * This represents a task which is running within SquirrelJME.
 *
 * @since 2017/12/10
 */
public abstract class KernelTask
	implements SystemCallImplementation, SystemTask
{
	/** Reference to the owning kernel. */
	protected final Reference<Kernel> kernel;
	
	/** The task index. */
	protected final int index;
	
	/** The main class. */
	protected final String mainclass;
	
	/** The trust group this task is within. */
	protected final SystemTrustGroup trustgroup;
	
	/** The packet stream to the child process. */
	@Deprecated
	private final PacketStream _stream;
	
	/** Instances of each service which is mapped to the kernel service set. */
	private final ServerInstance[] _instances;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/** Has the task been initialized? */
	private volatile boolean _isinitialized;
	
	/**
	 * Initializes the kernel task.
	 *
	 * @param __k The owning kernel.
	 * @param __id The index of the task.
	 * @param __l Launch parameters of the task.
	 * @param __in The input from the task.
	 * @param __out The output to the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/02
	 */
	protected KernelTask(Reference<Kernel> __k, int __id, KernelTaskLaunch __l,
		DatagramIn __in, DatagramOut __out)
		throws NullPointerException
	{
		if (__k == null ||
			(__id != 0 && (__l == null || __in == null || __out == null)))
			throw new NullPointerException("NARG");
		
		this.kernel = __k;
		this.index = __id;
		
		Kernel kernel = __k.get();
		
		// The server requires special initialization especially with the
		// streams because of the pipe nature.
		if (__id == 0)
		{
			// Main class represents the kernel, despite it having no main
			// method or MIDlet implementation
			this.mainclass = Kernel.class.getName();
			
			// Set the stream for this side to side A
			LoopbackDatagramDuplex.Side side = kernel.loopback().sideA();
			__in = side.input();
			__out = side.output();
			
			// Use the trust group representing the kernel, which allows
			// anything
			this.trustgroup = kernel.__systemTrustGroup();
		}
		
		// Clients initialize with other means
		else
		{
			// Set main class based on launched class
			if (true)
				throw new todo.TODO();
			
			// Initialize trust group
			this.trustgroup = __l.trustGroup();
		}
		
		// Initialize the packet stream which is used to communicate between
		// the process and the kernel
		this._stream = new PacketStream(__in, __out, new __Handler__(),
			"Kernel-Task-" + __id);
		
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
		
		this.trustgroup.checkPermission(__cl, __n, __a);
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
		// {@squirreljme.error AP06 The kernel was garbage collected.}
		Kernel rv = this.kernel.get();
		if (rv == null)
			throw new RuntimeException("AP06");
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
	 * @since 2018/02/21
	 */
	@Override
	public final Object systemCall(SystemFunction __func, Object... __args)
		throws NullPointerException
	{
		if (__func == null)
			throw new NullPointerException("NARG");
		
		switch (__func)
		{
				// Client has been initialized
			case CLIENT_INITIALIZATION_COMPLETE:
				this._isinitialized = true;
				return new VoidType();
			
				// {@squirreljme.error AP0c Unimplemented system call.
				// (The function)}
			default:
				throw new RuntimeException(String.format("AP0c %s", __func));
		}
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
		return this.trustgroup;
	}
	
	/**
	 * Handler for packets sent from the remote end of this task.
	 *
	 * @since 2018/01/02
	 */
	@Deprecated
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
				
				case PacketTypes.OS_TYPE:
					return this.__osType(__p);
				
					// {@squirreljme.error AP07 Unknown packet. (The packet)}
				default:
					throw new IllegalArgumentException(
						String.format("AP07 %s", __p));
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
			
			// {@squirreljme.error AP08 Could not obtain the class
			// for the client type.}
			catch (ClassNotFoundException e)
			{
				throw new RuntimeException("AP08", e);
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
		 * Returns the operating system type.
		 *
		 * @param __p The incoming packet, which is ignored.
		 * @return The operating system type.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/01/13
		 */
		private final Packet __osType(Packet __p)
			throws NullPointerException
		{
			if (__p == null)
				throw new NullPointerException("NARG");
			
			Packet rv = __p.respond();
			rv.writeString(0,
				KernelTask.this.kernel().operatingSystemType().name());
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
				// {@squirreljme.error AP09 Invalid service index.
				// (The service index)}
				if (svdx < 0 || svdx >= instances.length)
					throw new RuntimeException(String.format("AP09 %d", svdx));
				
				// {@squirreljme.error AP0a The service index is valid however
				// no service has been mapped and initialized yet.}
				i = instances[svdx];
				if (i == null)
					throw new RuntimeException(String.format("AP0a %d", svdx));
			}
			
			// Send to service
			try (Packet q = PacketFarm.createPacket(type, plen))
			{
				__p.readPacketData(8, q);
				
				// The incoming packet data is no longer needed, since
				// services could be sending large packets they might consume
				// extra memory
				__p.close();
				
				return i.handlePacket(q);
			}
		}
	}
}

