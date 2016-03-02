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
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;

/**
 * This represents a single class loaded by the interpreter.
 *
 * @since 2016/03/01
 */
public class InterpreterClass
{
	/** The interpreter engine which owns this class. */
	protected final InterpreterEngine engine;
	
	/** Super classes and interfaces implemented in this class. */
	protected final Set<InterpreterClass> superclasses;
	
	/**
	 * Initializes the class data.
	 *
	 * @param __owner The owning interpreter engine.
	 * @param __cdata Class data for parsing.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	InterpreterClass(InterpreterEngine __owner, InputStream __cdata)
		throws IOException, NullPointerException
	{
		// Check
		if (__owner == null || __cdata == null)
			throw new NullPointerException();
		
		// Set
		engine = __owner;
		
		throw new Error("TODO");
	}
	
	/**
	 * Locates a method in the class by the given identifier name and method
	 * descriptor.
	 *
	 * @param __name The name of the method.
	 * @param __desc The method descriptor.
	 * @return The method matching the name and descriptor.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/01
	 */
	public InterpreterMethod getMethod(String __name, String __desc)
		throws NullPointerException
	{
		// Check
		if (__name == null || __desc == null)
			throw new NullPointerException();
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the name of this class as returned by {@link Class#getName()}.
	 *
	 * @return The name of this class.
	 * @since 2016/03/02
	 */
	public String getName()
	{
		throw new Error("TODO");
	}
}

