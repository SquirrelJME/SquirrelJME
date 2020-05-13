// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.springcoat.objects.SimpleObjectViewer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;

/**
 * Stores the context for a thread.
 *
 * @since 2020/05/12
 */
public final class ThreadContext
{
	/** The current class path for this context. */
	private final List<VMClassLibrary> _classPath =
		new ArrayList<>();
	
	/** The class information pointers which exist currently. */
	private final Map<ClassName, SimpleObjectViewer> _classInfos =
		new HashMap<>();
	
	/**
	 * Obtains or loads a new class info context for the current thread.
	 *
	 * @param __worker The worker to operate under, for any method calls.
	 * @param __class The class to load.
	 * @return The object viewer for the class info.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/13
	 */
	public SimpleObjectViewer loadClassInfo(SpringThreadWorker __worker,
		ClassName __class)
		throws NullPointerException
	{
		if (__worker == null || __class == null)
			throw new NullPointerException("NARG");
		
		// Lock to prevent multiple threads from loading at once
		Map<ClassName, SimpleObjectViewer> classInfos = this._classInfos;
		synchronized (this)
		{
			// Check if already already to not load twice
			SimpleObjectViewer rv = classInfos.get(__class);
			if (rv != null)
				return rv;
			
			// Debug
			Debugging.debugNote("TC.loadClassInfos(%s)", __class);
			
			throw Debugging.todo();
		}
	}
	
	/**
	 * Pushes a library to the class path for this context.
	 *
	 * @param __lib The library to push.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/13
	 */
	public void pushClassPath(VMClassLibrary __lib)
		throws NullPointerException
	{
		if (__lib == null)
			throw new NullPointerException("NARG");
		
		// Debug
		Debugging.debugNote("TC.pushClassPath(%s)", __lib.name());
		
		// Do not allow multiple threads to push out of order
		synchronized (this)
		{
			this._classPath.add(__lib);
		}
	}
}
