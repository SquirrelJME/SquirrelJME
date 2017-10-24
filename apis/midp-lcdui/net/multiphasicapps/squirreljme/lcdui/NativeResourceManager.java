// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.unsafe.SystemProcess;

/**
 * This is a class which manages native resources, it is used to manage
 * native resources which may be associated with objects and memory areas
 * which cannot be garbage collected.
 *
 * @since 2017/10/24
 */
public final class NativeResourceManager
	implements Runnable
{
	/** The thread which manages resources. */
	protected final Thread thread;
	
	/**
	 * Initializes the native resource manager.
	 *
	 * @since 2017/10/24
	 */
	public NativeResourceManager()
	{
		// Initialize and run the thread last
		Thread thread = SystemProcess.createDaemonThread(this,
			"NativeResourceManager");
		this.thread = thread;
		thread.start();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/24
	 */
	@Override
	public void run()
	{
		throw new todo.TODO();
	}
}

