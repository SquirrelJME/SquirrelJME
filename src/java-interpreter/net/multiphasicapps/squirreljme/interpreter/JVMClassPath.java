// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import java.io.InputStream;
import java.util.LinkedHashMap;

/**
 * This represents a class path which is visible to the interpreter so that
 * classes may be grouped together.
 *
 * @since 2016/03/05
 */
public abstract class InterpreterClassPath
{
	/** The owning interpreter engine. */
	protected final InterpreterEngine engine;	
	
	/**
	 * Initializes the classpath which is part of an interpreter.
	 *
	 * @param __eng The owning engine.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/05
	 */
	public InterpreterClassPath(InterpreterEngine __eng)
		throws NullPointerException
	{
		// Check
		if (__eng == null)
			throw new NullPointerException();
		
		// Set
		engine = __eng;
	}
	
	/**
	 * Returns the engine which owns this class path element.
	 *
	 * @return The owning engine.
	 * @since 2016/03/05
	 */
	public final InterpreterEngine engine()
	{
		return engine;
	}
	
	/**
	 * Finds a resource using the given name as if it were requested from a
	 * {@link ClassLoader}.
	 *
	 * @param __res The resource to find.
	 * @return The input stream of the given resource or {@code null} if not
	 * found.
	 * @since 2016/03/02
	 */
	public abstract InputStream getResourceAsStream(String __res);
}

