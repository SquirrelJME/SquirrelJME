// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This class is used to manage the IPC interface and allow any service to
 * register IPC messages and such.
 *
 * @since 2019/12/28
 */
public final class IPCManager
{
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
		throw new todo.TODO();
	}
	
	/**
	 * Registers the given ID with the specified callback.
	 *
	 * @param __ipcid The IPC ID to listen on.
	 * @param __cb The callback for the IPC.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/12/28
	 */
	public static final void register(int __ipcod, IPCCallback __cb)
		throws NullPointerException
	{
		if (__cb == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

