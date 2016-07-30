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

/**
 * This represents a file that is currently open so that its data may be
 * accessed as required.
 *
 * This interface does not know about file positioning, that is up to the
 * emulation implementation to handle.
 *
 * All instances of this class must be reference counted for opening and
 * closing. On initial construction of this interface, the reference count
 * is to be {@code 1}.
 *
 * @since 2016/07/30
 */
public interface OpenFile
	extends Closeable
{
}

