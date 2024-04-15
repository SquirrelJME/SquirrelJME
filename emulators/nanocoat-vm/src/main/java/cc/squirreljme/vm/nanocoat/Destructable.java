// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.vm.VMException;

/**
 * Represents an object that may be destructed.
 *
 * @since 2023/12/14
 */
public interface Destructable
	extends AutoCloseable
{
	/**
	 * {@inheritDoc}
	 * @since 2023/12/14
	 */
	@Override
	void close()
		throws VMException;
}
