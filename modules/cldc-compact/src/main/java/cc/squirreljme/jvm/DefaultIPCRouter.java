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
 * This is a router which just forward IPC requests to the standard manager.
 *
 * @since 2019/12/28
 */
@Deprecated
public final class DefaultIPCRouter
	implements IPCCallback
{
	/**
	 * {@inheritDoc}
	 * @since 2019/12/28
	 */
	@Override
	@Deprecated
	public final long ipcCall(int __tid, int __ipcid, int __a, int __b,
		int __c, int __d, int __e, int __f, int __g, int __h)
	{
		return IPCManager.ipcCall(__tid, __ipcid, __a, __b, __c, __d, __e,
			__f, __g, __h);
	}
}

