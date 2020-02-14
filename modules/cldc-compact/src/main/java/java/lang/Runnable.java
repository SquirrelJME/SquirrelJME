// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This interface should be used by any class which is intended and capable
 * of being run in its own thread (via {@link Thread(Runnable)}.
 *
 * When used with {@link Thread}, the method {@link #run()} will be executed
 * when the thread is started.
 *
 * @since 2018/09/19
 */
public interface Runnable
{
	/**
	 * Performs any action that is required as needed.
	 *
	 * @since 2018/09/19
	 */
	public abstract void run();
}

