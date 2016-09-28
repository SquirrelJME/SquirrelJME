// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe;

import net.multiphasicapps.squirreljme.jit.base.JITConfig;

/**
 * This interface describes a means for which an executable that contains
 * native code, class definitions, and resources is written. The output class
 * handles linkage, the layout of the resulting binary, and other details.
 *
 * @since 2016/09/28
 */
public interface ExecutableOutput
{
	/**
	 * Returns the configuration currently being used on this output.
	 *
	 * @return The output configuration.
	 * @since 2016/09/28
	 */
	public abstract JITConfig config();
}

