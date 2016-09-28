// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.exe.elf;

import net.multiphasicapps.squirreljme.exe.ExecutableOutputFactory;
import net.multiphasicapps.squirreljme.jit.base.JITConfig;

/**
 * This is a factory which is used to output to ELF formatted files.
 *
 * @since 2016/09/28
 */
public class ELFExecutableFactory
	implements ExecutableOutputFactory
{
	/**
	 * {@inheritDoc}
	 * @since 2016/09/28
	 */
	@Override
	public ELFExecutable createExecutable(JITConfig __conf)
		throws NullPointerException
	{
		return new ELFExecutable(__conf);
	}
}

