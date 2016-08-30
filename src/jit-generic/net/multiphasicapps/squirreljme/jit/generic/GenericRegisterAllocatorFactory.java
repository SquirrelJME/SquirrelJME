// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import net.multiphasicapps.squirreljme.jit.JITObjectProperties;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;

/**
 * This is a factory which is used for creating generic register allocators
 * which is needed by the generic JIT to determine where values are to be
 * placed during execution.
 *
 * @since 2016/08/30
 */
public interface GenericRegisterAllocatorFactory
	extends JITObjectProperties
{
	/**
	 * Creates a new register allocator.
	 *
	 * @return The newly created register allocator.
	 * @since 2016/08/30
	 */
	public abstract GenericRegisterAllocator create();
}

