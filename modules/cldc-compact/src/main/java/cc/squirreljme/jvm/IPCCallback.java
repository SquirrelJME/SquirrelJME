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
 * Anything which would like to listen for IPCs must implement and register
 * this callback.
 *
 * @since 2019/12/28
 */
@Deprecated
public interface IPCCallback
{
	/**
	 * Any classes which
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
	long ipcCall(int __tid, int __ipcid, int __a, int __b, int __c, int __d,
		int __e, int __f, int __g, int __h);
}

