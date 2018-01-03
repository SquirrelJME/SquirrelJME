// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemCaller;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgram;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemProgramInstallReport;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;
import net.multiphasicapps.squirreljme.runtime.packets.Packet;
import net.multiphasicapps.squirreljme.runtime.packets.PacketStream;
import net.multiphasicapps.squirreljme.runtime.packets.PacketWriter;

/**
 * This is a system caller which uses a basic input stream and a basic output
 * stream to communicate with the kernel to provide system call support.
 *
 * Not all functionality is supported and as such still requires a native
 * implementation.
 *
 * @since 2017/12/31
 */
public abstract class ClientCaller
	extends SystemCaller
{
	/** The packet stream used to communicate with the kernel. */
	protected final PacketStream stream;
	
	/** Mapped service cache. */
	private final Map<String, String> _svcache =
		new HashMap<>();
	
	/** The kernel sent a hello message. */
	volatile boolean _gothello;
	
	/**
	 * Initializes the client caller.
	 *
	 * @param __in The input stream.
	 * @param __out The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public ClientCaller(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		PacketStream stream = new PacketStream(__in, __out,
			new __ResponseHandler__(new WeakReference<>(this)));
		this.stream = stream;
		
		// Send hello packet to the other end
		try (Packet p = stream.farm().create(PacketTypes.HELLO, 0))
		{
			stream.send(p);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final SystemProgramInstallReport installProgram(
		byte[] __b, int __o, int __l)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final SystemTask launchTask(SystemProgram __program,
		String __mainclass, int __perms, String... __props)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final SystemProgram[] listPrograms(int __typemask)
		throws SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final SystemTask[] listTasks(boolean __incsys)
		throws SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/12/31
	 */
	@Override
	public final String mapService(String __sv)
		throws NullPointerException
	{
		if (__sv == null)
			throw new NullPointerException("NARG");
		
		PacketStream stream = this.stream;
			
		// Cache the service so IPC calls are reduced
		Map<String, String> svcache = this._svcache;
		synchronized (svcache)
		{
			// This allows null to be cached as needed
			if (svcache.containsKey(__sv))
				return svcache.get(__sv);
			
			try (Packet p = stream.farm().create(PacketTypes.MAP_SERVICE))
			{
				// Write the service to map
				PacketWriter w = p.createWriter();
				w.writeString(__sv);
				
				// Send packet to the server, await response
				try (Packet r = stream.send(p))
				{
					// Treat empty strings as null
					String rv = r.createReader().readString();
					if (rv == null || rv.length() <= 0)
						rv = null;
					
					// Cache string so the server does not need to be
					// queried again
					svcache.put(__sv, rv);
					return rv;
				}
			}
		}
	}
	
	/**
	 * Tells the remote end that the initialization has been complete.
	 *
	 * @since 2018/01/01
	 */
	public final void sendInitializationComplete()
	{
		PacketStream stream = this.stream;
		try (Packet p = stream.farm().create(PacketTypes.INITIALIZED, 0))
		{
			stream.send(p);
		}
	}
}

