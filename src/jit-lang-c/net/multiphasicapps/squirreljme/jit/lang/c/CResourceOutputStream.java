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
		12;
	
	/** The namespace writer. */
	protected final CLangNamespaceWriter nswriter;
	
	/** The name of the resource. */
	protected final String name;
	
	/** The name of the file as it appears in the JAR. */
	protected final String jarname;
	
	/** Column counter. */
	private volatile int _count;
	
	/** Using comma? */
	private volatile boolean _comma;
	
	/** Was this closed? */
	private volatile boolean _closed;
	
	/** Total number of bytes written. */
	private volatile int _bytes;
	
	/**
	 * Initializes a resource which outputs to C code.
	 *
	 * @param __nsw The owning namespace writer.
	 * @param __rname The name of the resource.
	 * @param __jname The name as it appears in the JAR.
	 * @param __ps The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	public CResourceOutputStream(CLangNamespaceWriter __nsw, String __rname,
		String __jname, PrintStream __ps)
		throws NullPointerException
	{
		super(__ps);
		
		// Check
		if (__nsw == null || __rname == null || __jname == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.nswriter = __nsw;
		this.name = __rname;
		this.jarname = __jname;
		
		// Always include global headers
		PrintStream output = this.output;
		__nsw.__globalHeader(output);
		
		// Start of type declaration
		output.print("const uint8_t DATA_");
		output.print(__rname);
		output.print("[");
		output.print("SIZE_");
		output.print(__rname);
		output.println("] =");
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
		if (!this._closed)
		{
			// Prevent double close
			this._closed = true;
			
			// Get output
			PrintStream output = this.output;
		
			// End sequence
			output.println();
			output.println("};");
			output.println();
			
			// Define the size in the header
			String name = this.name;
			String sizedef = "SIZE_" + name;
			CLangNamespaceWriter nsw = this.nswriter;
			PrintStream nsheader = nsw.namespaceHeader();
			nsheader.print("#define ");
			nsheader.print(sizedef);
			nsheader.print(' ');
			nsheader.println(this._bytes);
			
			// And extern the resource definition
			nsheader.print("extern SJME_Resource ");
			nsheader.print(name);
			nsheader.println(';');
			
			// Declare the resource
			output.println();
			output.print("SJME_Resource ");
			output.print(name);
			output.println(" =");
			output.println("{");
			output.println("\tSJME_STRUCTURETYPE_RESOURCE,");
			
			// Print name
			output.print("\t&");
			output.print(nsw.addString(this.jarname));
			output.println(',');
			
			// Data pointer
			output.print("\tDATA_");
			output.print(name);
			output.println(',');
			
			// Size
			output.print('\t');
			output.println(sizedef);
			
			// End
			output.println("};");
			output.println();
		}
		
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
		
		// Increase and potentially newline
		if (count >= WRAP_BYTES)
		{
			// Make a new line
			output.println();
			output.print('\t');
			
			// Back to the start
			count = 0;
		}
		count++;
		
		// Use comma next time
		this._comma = true;
		
		// Print data
		output.print("0x");
		output.print(Character.forDigit(((__b >>> 4) & 0xF), 16));
		output.print(Character.forDigit(((__b) & 0xF), 16));
		
		// Store
		this._count = count;
		
		// Increase byte count
		this._bytes++;
	}
}

