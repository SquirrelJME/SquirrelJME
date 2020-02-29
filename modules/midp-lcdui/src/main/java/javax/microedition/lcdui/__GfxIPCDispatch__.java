// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.IPCCallback;

/**
 * This is the graphics IPC handler which dispatches any events to the
 * appropriate widgets and such.
 *
 * @since 2019/12/28
 */
@Deprecated
class __GfxIPCDispatch__
	implements IPCCallback
{
	/** The instance of this. */
	private static __GfxIPCDispatch__ _INSTANCE;
	
	/**
	 * {@inheritDoc}
	 * @since 2019/12/28
	 */
	@Override
	public final long ipcCall(int __tid, int __ipcid, int __a, int __b,
		int __c, int __d, int __e, int __f, int __g, int __h)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Creates an instance of this class.
	 *
	 * @return The class instance.
	 * @since 2019/12/28
	 */
	static final __GfxIPCDispatch__ __instance()
	{
		__GfxIPCDispatch__ rv = __GfxIPCDispatch__._INSTANCE;
		if (rv == null)
			__GfxIPCDispatch__._INSTANCE = (rv = new __GfxIPCDispatch__());
		return rv;
	}
}

