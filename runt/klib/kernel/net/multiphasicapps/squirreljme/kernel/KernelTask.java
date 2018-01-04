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
import net.multiphasicapps.squirreljme.kernel.packets.PacketStream;
import net.multiphasicapps.squirreljme.kernel.packets.PacketStreamHandler;
import net.multiphasicapps.squirreljme.kernel.service.ServiceInstance;
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
	
	/** The packet stream to the child process. */
	private final PacketStream _stream;
	
	/** Instances of each service which is mapped to the kernel service set. */
	private final ServiceInstance[] _instances;
	
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
		
		// The server requires special initialization especially with the
		// streams because of the pipe nature.
		if (__id == 0)
		{
			// Main class represents the kernel, despite it having no main
			// method or MIDlet implementation
			this.mainclass = Kernel.class.getName();
			
			throw new todo.TODO();
		}
		
		// Clients initialize with other means
		else
		{
			throw new todo.TODO();
		}
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
			
			throw new todo.TODO();
		}
	}
}

