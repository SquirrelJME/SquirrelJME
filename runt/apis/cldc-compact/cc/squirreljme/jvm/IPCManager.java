// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to manage the IPC interface and allow any service to
 * register IPC messages and such.
 *
 * @since 2019/12/28
 */
public final class IPCManager
{
	/** Services that are available. */
	private static final Map<Integer, IPCCallback> _IPC_MAP =
		new HashMap<>();
	
	/**
	 * No instances of this class.
	 *
	 * @since 2019/12/28
	 */
	private IPCManager()
	{
	}
	
	/**
	 * This is the handler for IPC messages.
	 *
	 * @param __tid The origin task ID.
	 * @param __ipcid The ID number of the IPC interface.
	 * @param __a Argument A.
	 * @param __b Argument B.
	 * @param __c Argument C.
	 * @param __d Argument D.
	 * @param __e Argument E.
	 * @param __f Argument F.
	 * @param __g Argument G.
	 * @param __h Argument H.
	 * @return The result of the IPC call.
	 * @since 2019/12/28
	 */
	public static final long ipcCall(int __tid, int __ipcid, int __a, int __b,
		int __c, int __d, int __e, int __f, int __g, int __h)
	{
		IPCCallback handler = null;
		
		// Find the IPC Callback handler
		Map<Integer, IPCCallback> ipcmap = _IPC_MAP;
		synchronized (ipcmap)
		{
			handler = ipcmap.get(__ipcid);
		}
		
		// Drop the call if there is no handler
		if (handler == null)
			return 0;
		
		// Perform the call
		return handler.ipcCall(__tid, __ipcid, __a, __b, __c, __d, __e, __f,
			__g, __h);
	}
	
	/**
	 * Registers the given ID with the specified callback.
	 *
	 * @param __ipcid The IPC ID to listen on.
	 * @param __cb The callback for the IPC.
	 * @throws IllegalArgumentException If IPC ID is zero.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/28
	 */
	public static final void register(int __ipcid, IPCCallback __cb)
		throws IllegalArgumentException, NullPointerException
	{
		if (__cb == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ3u It is not valid to register the zero
		// IPC ID.}
		if (__ipcid == 0)
			throw new IllegalArgumentException("ZZ3u");
		
		// Lock and register
		Map<Integer, IPCCallback> ipcmap = _IPC_MAP;
		synchronized (ipcmap)
		{
			ipcmap.put(__ipcid, __cb);
		}
	}
}

