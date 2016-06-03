// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import net.multiphasicapps.squirreljme.classpath.ClassPath;

/**
 * This is a single process that exists within the interpreter, it has its own
 * object state.
 *
 * @since 2016/06/03
 */
public abstract class InterpreterProcess
{
	/** The owning interpreter. */
	protected final Interpreter interpreter;
	
	/** The used classpath. */
	protected final ClassPath classpath;
	
	/**
	 * This initailizes the interpreter process.
	 *
	 * @param __terp The interpreter owning this.
	 * @param __cp The classpath which is used for class lookup.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/03
	 */
	public InterpreterProcess(Interpreter __terp, ClassPath __cp)
		throws NullPointerException
	{
		// Check
		if (__terp == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.interpreter = __terp;
		this.classpath = __cp;
	}
}

