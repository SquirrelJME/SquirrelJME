// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.classformat.ClassDecoder;
import net.multiphasicapps.squirreljme.classformat.ClassDecoderOption;
import net.multiphasicapps.squirreljme.classformat.ClassFormatException;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This contains the just in time compiler.
 *
 * A JIT may only be used once for any input class, however the output of the
 * JIT may be used multiple times so that multiple JARs may be combined into
 * a single binary.
 *
 * When the JIT processes a class, the information is always performed in the
 * same order so that implementations of the JIT do not need to store as much
 * information in extra memory before writing it into the cache or binary.
 *
 * @since 2016/07/02
 */
public final class JIT
	implements Runnable
{
	/** The input source. */
	protected final InputStream input;
	
	/** The output of the JIT. */
	protected final JITOutput output;
	
	/** The namespace to write to. */
	protected final JITNamespaceWriter namespace;
	
	/** One time lock. */
	private final Object _oncelock =
		new Object();
	
	/** One time only. */
	private volatile boolean _once;
	
	/**
	 * Initializes the JIT.
	 *
	 * This performs basic sanity checks before the actual compilation of a JIT
	 * is performed.
	 *
	 * @param __jo The output of the JIT.
	 * @apram __ns The namespace writer to place classes into.
	 * @param __ic The input stream of the class data.
	 * @throws JITException If the class is not correctly formed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/03
	 */
	public JIT(JITOutput __jo, JITNamespaceWriter __ns, InputStream __ic)
		throws JITException, NullPointerException
	{
		// Check
		if (__jo == null || __ns == null || __ic == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.output = __jo;
		this.input = __ic;
		this.namespace = __ns;
	}
	
	/**
	 * Returns the JIT output configuration.
	 *
	 * @return The JIT output configuration.
	 * @since 2016/08/13
	 */
	public final JITConfig config()
	{
		return this.output.config();
	}
	
	/**
	 * Runs the JIT compilation.
	 *
	 * @since 2016/07/03
	 */
	@Override
	public final void run()
	{
		// Perform the JIT by running and parsing the class file.
		try (__ClassWriter__ cw = new __ClassWriter__(this, this.namespace))
		{
			// Setup data stream
			DataInputStream dis = new DataInputStream(this.input);
			
			// Start decoding the class
			ClassDecoder cd = new ClassDecoder(dis, cw,
				ClassDecoderOption.CALCULATE_ALL_VARIABLE_TYPES);
			cd.decode();
		}
		
		// {@squirreljme.error ED02 Failed to read the class file.}
		catch (IOException|ClassFormatException e)
		{
			throw new JITException("ED02", e);
		}
	}
}

