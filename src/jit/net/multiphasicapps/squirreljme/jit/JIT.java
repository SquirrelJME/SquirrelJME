// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import java.io.IOException;

/**
 * This contains the just in time compiler.
 *
 * A JIT may only be used once for any input class, however the output of the
 * JIT may be used multiple times so that multiple JARs may be combined into
 * a single binary.
 *
 * @since 2016/07/02
 */
public abstract class JIT
{
	/** One time lock. */
	private final Object _oncelock =
		new Object();
	
	/** One time only. */
	private volatile boolean _once;
}

