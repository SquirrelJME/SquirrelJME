// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.streamproc;

import java.io.Closeable;
import java.io.IOException;

/**
 * This is the base class for all stream processor which are used to input
 * and output data.
 *
 * This class is not thread safe.
 *
 * @since 2016/12/20
 */
public abstract class StreamProcessor
	implements Closeable
{
}

