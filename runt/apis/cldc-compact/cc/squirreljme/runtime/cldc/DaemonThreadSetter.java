// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

/**
 * This interface is used to set a daemon thread before the system call
 * interface has been initialized, this is needed by the packet interface.
 *
 * @since 2018/01/05
 */
@Deprecated
public interface DaemonThreadSetter
{
	/**
	 * Sets the specified thread to be a daemon thread.
	 *
	 * @param __t The thread to set as a daemon.
	 * @throws IllegalThreadStateException If it could not be set.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/05
	 */
	public abstract void setDaemonThread(Thread __t)
		throws IllegalThreadStateException, NullPointerException;
}

