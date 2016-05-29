// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This represents a process within the kernel which where
 * {@link KernelThread}s are assigned to for performing work.
 *
 * @since 2016/05/16
 */
public final class KernelProcess
	implements __Identifiable__
{
	/** The owning kernel. */
	protected final Kernel kernel;
	
	/** The process ID. */
	protected final int id;
	
	/**
	 * Initializes the kernel process.
	 *
	 * @param __k The owning kernel.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/29
	 */
	KernelProcess(Kernel __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
		this.id = __k.__nextProcessId();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/29
	 */
	@Override
	public final int id()
	{
		return this.id;
	}
}

