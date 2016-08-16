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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.io.data.DataEndianess;
import net.multiphasicapps.io.data.ExtendedDataOutputStream;
import net.multiphasicapps.squirreljme.jit.base.JITCPUEndian;
import net.multiphasicapps.squirreljme.exe.ExecutableOutput;

/**
 * This is used to output ELF binaries.
 *
 * @since 2016/08/07
 */
public class ELFOutput
	implements ExecutableOutput
{
	/** The size of the entry table. */
	private static final int _ENTRY_SIZE =
		12;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** System properties to put for initial execution. */
	protected final Map<String, String> properties =
		new LinkedHashMap<>();
	
	/**
	 * Adds a system property to be included in the target binary.
	 *
	 * @param __k The the key.
	 * @param __v The value.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/24
	 */
	public void addSystemProperty(String __k, String __v)
		throws IOException, NullPointerException
	{
		// Check
		if (__k == null || __v == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			Map<String, String> properties = this.properties;
			properties.put(__k, __v);
		}
	}
	
	/**
	 * Generates the actual binary.
	 *
	 * @param __os The stream to write to.
	 * @throws IllegalStateException If the output stream does not match an
	 * optionally previously specified primed stream.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	public void generate(OutputStream __os)
		throws IllegalStateException, IOException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Inserts the namespace into the specified output.
	 *
	 * @param __name The name of the namespace.
	 * @param __data The input namespace data.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	public void insertNamespace(String __name, InputStream __data)
		throws IOException, NullPointerException
	{
		// Check
		if (__name == null || __data == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * This primes the given stream for output for the given binary, generally
	 * this should always be called first with the specified output stream in
	 * the event the writer is capable of streamed output. If the writer does
	 * not need to write to the stream as soon as possible then this method
	 * can do nothing.
	 *
	 * @param __os The stream to prime.
	 * @throws IllegalStateException If the output has already been primed.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/15
	 */
	public void primeOutput(OutputStream __os)
		throws IllegalStateException, IOException, NullPointerException
	{
		// Check
		if (__os == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
}

