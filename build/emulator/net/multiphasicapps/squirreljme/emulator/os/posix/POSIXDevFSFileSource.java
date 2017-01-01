// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator.os.posix;

import net.multiphasicapps.squirreljme.fs.virtual.VirtualFileSource;

/**
 * This is a POSIX compatible file source which implements the POSIX standard
 * for the {@code /dev} filesystem.
 *
 * The POSIX standard requires only 3 special character devices:
 * {@code console}, {@code null}, and {@code tty}.
 *
 * @since 2016/09/05
 */
public class POSIXDevFSFileSource
	extends VirtualFileSource
{
}

