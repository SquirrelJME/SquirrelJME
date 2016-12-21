// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import net.multiphasicapps.squirreljme.build.base.SourceCompiler;
import net.multiphasicapps.squirreljme.build.base.SourceCompilerProvider;

/**
 * This provides access to the standard Java compiler available since Java 6.
 *
 * @since 2016/12/21
 */
public class StandardCompilerProvider
	extends SourceCompilerProvider
{
	/**
	 * {@inheritDoc}
	 * @since 2016/12/21
	 */
	@Override
	public SourceCompiler newCompilerInstance()
	{
		return new StandardCompiler();
	}
}

