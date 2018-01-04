// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.callerbase;

import java.io.InputStream;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.runtime.cldc.SystemCaller;

/**
 * This represents the base for the client and server system callers which
 * provides a basis for common code between the two classes.
 *
 * @since 2018/01/04
 */
public abstract class BaseCaller
	extends SystemCaller
{
	protected BaseCaller(InputStream __in, OutputStream __out)
		throws NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

