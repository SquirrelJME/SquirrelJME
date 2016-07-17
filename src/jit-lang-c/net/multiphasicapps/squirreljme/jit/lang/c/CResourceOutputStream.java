// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.lang.c;

import java.io.IOException;
import java.io.PrintStream;
import net.multiphasicapps.squirreljme.jit.lang.ResourceOutputStream;

/**
 * This outputs C source code for resources.
 *
 * @since 2016/07/17
 */
public class CResourceOutputStream
	extends ResourceOutputStream
{
	/** When to wrap bytes. */
	public static final int WRAP_BYTES =
		15;
	
	/** The namespace writer. */
	protected final CLangNamespaceWriter nswriter;
	
	/** The name of the resource. */
	protected final String name;
	
	/** The number of bytes written. */
	private volatile int _count;
	
	/** Using comma? */
	private volatile boolean _comma;
	
	/**
	 * Initializes a resource which outputs to C code.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __rname The name of the resource.
	 * @param __ps The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	public CResourceOutputStream(CLangNamespaceWriter __nsw, String __rname,
		PrintStream __ps)
		throws NullPointerException
	{
		super(__ps);
		
		// Check
		if (__nsw == null || __rname == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.nswriter = __nsw;
		this.name = __rname;
		
		System.err.printf("DEBUG -- Identifier: %s%n", __rname);
		
		// Always include global header
		PrintStream output = this.output;
		output.println("#include \"squirrel.h\"");
		output.println();
		
		// Start of type declaration
		output.print("const int8_t ");
		output.print(__rname);
		output.println("[] =");
		output.println('{');
		output.print('\t');
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	public void close()
		throws IOException
	{
		PrintStream output = this.output;
		
		// End sequence
		output.println();
		output.println("};");
		output.println();
		
		// Close super stream
		super.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/17
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		// Get count
		int count = this._count;
		
		// Add comma?
		PrintStream output = this.output;
		if (this._comma)
			output.print(", ");
		
		// Use comma next time
		this._comma = true;
		
		// Print data
		output.print("0x");
		output.print(Character.forDigit(((__b >>> 4) & 0xF), 16));
		output.print(Character.forDigit(((__b) & 0xF), 16));
		
		// Increase and potentially newline
		count++;
		if (count >= WRAP_BYTES)
		{
			// Make a new line
			output.println();
			output.print('\t');
			
			// Back to the start
			count = 0;
		}
		
		// Store
		this._count = count;
	}
}

