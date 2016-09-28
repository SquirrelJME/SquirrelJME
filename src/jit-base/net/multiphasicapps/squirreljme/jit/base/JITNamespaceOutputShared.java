// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.base;

import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This interface describes an output which creates a shared executable that
 * writes multiple namespaces within a single binary.
 *
 * @since 2016/09/28
 */
public interface JITNamespaceOutputShared
	extends JITNamespaceOutput
{
	/**
	 * This creates and returns an output stream which writes to a shared
	 * binary source.
	 *
	 * @throws IOException If it could not be created due to an error
	 * related to I/O.
	 * @throws JITException If it could not be created for other reasons.
	 * @since 2016/09/28
	 */
	public abstract OutputStream outputShared()
		throws IOException, JITException;
}

