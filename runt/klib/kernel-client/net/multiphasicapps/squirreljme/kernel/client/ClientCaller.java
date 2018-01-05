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
import net.multiphasicapps.squirreljme.kernel.callerbase.BaseCaller;
import net.multiphasicapps.squirreljme.kernel.ipc.base.PacketTypes;
import net.multiphasicapps.squirreljme.kernel.packets.Packet;
import net.multiphasicapps.squirreljme.kernel.packets.PacketStream;
import net.multiphasicapps.squirreljme.kernel.packets.PacketWriter;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemCaller;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;

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
	extends BaseCaller
{
	/**
	 * Initializes the client caller with a stream to the kernel IPC.
	 *
	 * @param __in The input stream from the kernel.
	 * @param __out The output stream to the kernel.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/03
	 */
	protected ClientCaller(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		super(__in, __out);
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public final SystemTask[] listTasks(boolean __incsys)
		throws SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Tell the kernel that the initialization of the task has been completed.
	 *
	 * @since 2018/01/03
	 */
	public final void sendInitializationComplete()
	{
		throw new todo.TODO();
	}
}

