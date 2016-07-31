// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;

/**
 * This class is used as the core of the emulator so emulate other operating
 * systems.
 *
 * @since 2016/07/30
 */
public final class Emulator
	implements Runnable
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The hypervisor controlling this emulator. */
	protected final HyperVisor hypervisor;
	
	/**
	 * Initializes the emulator.
	 *
	 * @param __hv The hypervisor used to access the system.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public Emulator(HyperVisor __hv)
		throws NullPointerException
	{
		// Check
		if (__hv == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.hypervisor = __hv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public void run()
	{
		throw new Error("TODO");
	}
}

