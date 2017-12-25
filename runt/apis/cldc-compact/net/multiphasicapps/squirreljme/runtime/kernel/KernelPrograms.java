// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to manage the programs which are available for usage.
 *
 * Each program has an identifier which identitifies, that identifier is mapped
 * to a slot within the program list so that the same program will always have
 * the same identifier.
 *
 * @since 2017/12/14
 */
public final class KernelPrograms
{
	/** Internal lock. */
	protected final Object lock =
		new Object();
	
	/** Native program mananger. */
	private final NativePrograms _natives;
	
	/** Programs which are available for usage. */
	private final List<KernelProgram> _programs =
		new ArrayList<>();
	
	/**
	 * Initializes the program manager.
	 *
	 * @param __nps Native program manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/14
	 */
	protected KernelPrograms(NativePrograms __nps)
		throws NullPointerException
	{
		if (__nps == null)
			throw new NullPointerException("NARG");
		
		this._natives = __nps;
		
		// Register all initial native programs so that they are known to
		// the kernel
		for (NativeProgram np : __nps.list())
			__register(np);
	}
	
	/**
	 * Returns the list of programs which are available.
	 *
	 * @param __by The task requesting the program list.
	 * @param __typemask A mask which is used to filter programs of a given
	 * type.
	 * @return An array containing the programs under the specified mask.
	 * @throws NullPointerException On null arguments.
	 * @throws SecurityException If the operation is not permitted.
	 * @since 2017/12/11
	 */
	public final KernelProgram[] listPrograms(KernelTask __by, int __typemask)
		throws NullPointerException, SecurityException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ0f The specified task is not permitted to
		// list available programs. (The task requesting the program list)}
		if (!__by.hasSimplePermissions(__by,
			KernelSimplePermission.LIST_PROGRAMS))
			throw new SecurityException(
				String.format("ZZ0f %s", __by));
		
		// Go through registered programs and find matches
		List<KernelProgram> programs = this._programs;
		synchronized (this.lock)
		{
			throw new todo.TODO();
		}
	}
	
	/**
	 * Registers the specified program with the kernel manager.
	 *
	 * @param __np The program to register.
	 * @return The wrapped program representation.
	 * @throws IllegalStateException If the index has already been registered
	 * or is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/25
	 */
	private final KernelProgram __register(NativeProgram __np)
		throws IllegalStateException, NullPointerException
	{
		if (__np == null)
			throw new NullPointerException("NARG");
		
		int dx = __np.index();
		
		// Progams may only be registered once
		List<KernelProgram> programs = this._programs;
		synchronized (this.lock)
		{
			// {@squirreljme.error ZZ0h Program with the given index has
			// already been registred. (The program index)}
			int n = programs.size();
			for (int i = 0; i < n; i++)
			{
				int pdx = programs.get(i).index();
				if (dx == pdx)
					throw new IllegalStateException(
						String.format("ZZ0h %d", dx));
			}
			
			// Insert new program
			KernelProgram rv = new KernelProgram(__np, dx);
			programs.add(rv);
			return rv;
		}
	}
}

