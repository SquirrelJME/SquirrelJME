// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.profiler;

/**
 * This stores information along with the thread information along with the
 * call stack.
 *
 * @since 2018/11/10
 */
public final class ProfiledThread
{
	/** The name of this thread. */
	protected final String name;
	
	/**
	 * Initializes the thread information.
	 *
	 * @param __n The name of the thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/10
	 */
	public ProfiledThread(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
	}
	
	/**
	 * Enters the given frame.
	 *
	 * @param __cl The name of the class.
	 * @param __mn The name of the method.
	 * @param __md The type of the method.
	 * @param __ns The The starting time in nanoseconds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/10
	 */
	public ProfiledFrame enterFrame(String __cl, String __mn, String __md,
		long __ns)
		throws NullPointerException
	{
		if (__cl == null || __mn == null || __md == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Exits the frame which at the top of the stack.
	 *
	 * @param __ns The nanoseconds when the frame exited.
	 * @return The exited frame.
	 * @throws IllegalStateException If there is no frame to exit.
	 * @since 2018/11/10
	 */
	public ProfiledFrame exitFrame(long __ns)
		throws IllegalStateException
	{
		throw new todo.TODO();
	}
}

