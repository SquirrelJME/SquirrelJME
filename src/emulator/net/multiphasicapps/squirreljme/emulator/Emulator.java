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
 * This class acts as the base for a given system and acts in a way as the
 * process in a way address space. The emulator only understands memory and
 * state as it sees it, behavior that is dependent on operating systems is
 * implemented by a {@link HypoVisor}.
 *
 * The emulator is not thread safe.
 *
 * @since 2016/07/30
 */
public abstract class Emulator
	implements Runnable
{
	/** The hypovisor used for external access. */
	protected final HypoVisor hypovisor;
	
	/** Has the emulator been booted yet? */
	private volatile boolean _booted;
	
	/**
	 * Initializes the emulator.
	 *
	 * @param __hv The hypovisor used to implement undefined behavior.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public Emulator(HypoVisor __hv)
		throws NullPointerException
	{
		// Check
		if (__hv == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.hypovisor = __hv;
	}
	
	/**
	 * Since CPU states are specific to the system being emulated, this
	 * method is used to create CPU states that may be used by code to setup
	 * new processes and potentially debug a given system.
	 *
	 * @return A new CPU state which may be set in the future.
	 * @since 2016/08/06
	 */
	public abstract CPUState createCPUState();
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/30
	 */
	@Override
	public final void run()
	{
		// Boot the emulator?
		HypoVisor hypovisor = this.hypovisor;
		if (!this._booted)
		{
			// Set booted
			this._booted = true;
			
			// Call hypervisor init
			hypovisor.init(this);
		}
		
		throw new Error("TODO");
	}
}

