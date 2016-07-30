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

/**
 * This interface provides access to a volume which is used for accessing
 * files.
 *
 * All files in the volume are named similarly to ZIP files.
 *
 * @since 2016/07/30
 */
public interface Volume
	extends Closeable
{
	/** The name of the contributing binary paths. */
	public static final String CONTRIB_BINARIES =
		"contrib";
}

