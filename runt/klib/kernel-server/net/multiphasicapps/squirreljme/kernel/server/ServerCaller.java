// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.server;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.multiphasicapps.squirreljme.kernel.callerbase.BaseCaller;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.LoopbackStreams;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemCaller;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemTask;

/**
 * This class provides the system call interfaces to the kernel by directly
 * accessing the kernel, this is intended to be used for the kernel task.
 *
 * @since 2017/12/10
 */
public abstract class ServerCaller
	extends BaseCaller
{
	/** The kernel to call into. */
	protected final Kernel kernel;
	
	/**
	 * Initializes the system caller with the given kernel.
	 *
	 * @param __k The kernel to call into.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/03
	 */
	protected ServerCaller(Kernel __k)
		throws NullPointerException
	{
		super(__k.loopback().sideB().input(),
			__k.loopback().sideB().output());
		
		if (__k == null)
			throw new NullPointerException("NARG");
		
		this.kernel = __k;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public SystemTask[] listTasks(boolean __incsys)
		throws SecurityException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public final <C> C service(Class<C> __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/01/03
	 */
	@Override
	public void setDaemonThread(Thread __t)
		throws IllegalThreadStateException, NullPointerException
	{
		throw new todo.TODO();
	}
}

