// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigSerializer;

/**
 * This is used for serialization of the MIPS config.
 *
 * @since 2017/02/02
 */
public class MIPSConfigSerializer
	extends JITConfigSerializer<MIPSConfig>
{
	/** The cached instance. */
	private static volatile Reference<MIPSConfigSerializer> _INSTANCE;
	
	/**
	 * Returns the instance of the serializer.
	 *
	 * @return The serializer instance.
	 * @since 2017/02/02
	 */
	public static MIPSConfigSerializer instance()
	{
		Reference<MIPSConfigSerializer> ref = _INSTANCE;
		MIPSConfigSerializer rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			_INSTANCE = new WeakReference<>((rv = new MIPSConfigSerializer()));
		
		return rv;
	}
}

