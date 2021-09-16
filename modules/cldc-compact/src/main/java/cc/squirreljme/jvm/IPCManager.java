// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
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
@Deprecated
public final class IPCManager
{
	/** Services that are available. */
	@Deprecated
	private static final Map<Integer, IPCCallback> _IPC_MAP =
		new HashMap<>();
	
	/**
	 * No instances of this class.
	 *
	 * @since 2019/12/28
	 */
	@Deprecated
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
	@Deprecated
	public static final long ipcCall(int __tid, int __ipcid, int __a, int __b,
		int __c, int __d, int __e, int __f, int __g, int __h)
	{
		IPCCallback handler = null;
		
		// Find the IPC Callback handler
		Map<Integer, IPCCallback> ipcmap = IPCManager._IPC_MAP;
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
	 * This is the handler for IPC messages, which performs unfolding
	 * accordingly.
	 *
	 * @param __tid The origin task ID.
	 * @param __v Input values.
	 * @return The result of the IPC call.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/28
	 */
	@Deprecated
	public static final long ipcCall(int __tid, int... __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		int n = __v.length;
		return IPCManager.ipcCall(__tid,
			(0 < n ? __v[0] : 0),
			(1 < n ? __v[1] : 0),
			(2 < n ? __v[2] : 0),
			(3 < n ? __v[3] : 0),
			(4 < n ? __v[4] : 0),
			(5 < n ? __v[5] : 0),
			(6 < n ? __v[6] : 0),
			(7 < n ? __v[7] : 0),
			(8 < n ? __v[8] : 0));
	}
	
	/**
	 * This is the handler for IPC messages, which performs unfolding
	 * accordingly.
	 *
	 * @param __tid The origin task ID.
	 * @param __ipcid The ID number of the IPC interface.
	 * @param __v Input values.
	 * @return The result of the IPC call.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/28
	 */
	@Deprecated
	public static final long ipcCall(int __tid, int __ipcid, int... __v)
		throws NullPointerException
	{
		if (__v == null)
			throw new NullPointerException("NARG");
		
		int n = __v.length;
		return IPCManager.ipcCall(__tid,
			__ipcid,
			(0 < n ? __v[0] : 0),
			(1 < n ? __v[1] : 0),
			(2 < n ? __v[2] : 0),
			(3 < n ? __v[3] : 0),
			(4 < n ? __v[4] : 0),
			(5 < n ? __v[5] : 0),
			(6 < n ? __v[6] : 0),
			(7 < n ? __v[7] : 0));
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
	@Deprecated
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
		Map<Integer, IPCCallback> ipcmap = IPCManager._IPC_MAP;
		synchronized (ipcmap)
		{
			ipcmap.put(__ipcid, __cb);
		}
	}
}

