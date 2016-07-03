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
import java.util.ServiceLoader;

/**
 * This factory is used to create instances of the JIT compiler which reads an
 * input class and produces output from them.
 *
 * This is used with the service loader.
 *
 * @since 2016/07/02
 */
public abstract class JITFactory
{
	/** Service for JIT factory lookup. */
	private static final ServiceLoader<JITFactory> _SERVICES =
		ServiceLoader.<JITFactory>load(JITFactory.class);
	
	/**
	 * Returns the name of the architecture this compiles for.
	 *
	 * @return The name of the architecture.
	 * @since 2016/07/02
	 */
	public abstract String architectureName();
	
	/**
	 * Creates a producer which is capable of creating the requested JIT to
	 * be used during class compilation.
	 *
	 * @param __arch The architecture to compile for.
	 * @param __archvar The variant of the architecture to use.
	 * @param __os The operating system to target.
	 * @return The producer for the given target, or {@code null} if it was not
	 * found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/03
	 */
	public static JITFactory.Producer createProducer(String __arch,
		String __archvar, String __os)
		throws NullPointerException
	{
		// Check
		if (__arch == null || __archvar == null || __os == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * A producer for JIT instances (since there is only a single JIT per
	 * class).
	 *
	 * @since 2016/07/03
	 */
	public static final class Producer
	{
		/**
		 * Initializes the producer.
		 *
		 * @since 2016/07/03
		 */
		private Producer()
		{
		}
		
		/**
		 * Creates a JIT which reads the given input class
		 *
		 * @param __ns The namespace where the class resides, this should be
		 * the name of a JAR file.
		 * @param __is The class to produce code for.
		 * @return The JIT for this given class.
		 * @throws IOException On read/write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/07/03
		 */
		public final JIT produce(String __ns, InputStream __is)
			throws IOException, NullPointerException
		{
			// Check
			if (__ns == null || __is == null)
				throw new NullPointerException("NARG");
			
			throw new Error("TODO");
		}
	}
}

