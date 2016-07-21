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
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.JITClassFlag;
import net.multiphasicapps.squirreljme.jit.JITClassFlags;
import net.multiphasicapps.squirreljme.jit.JITCompilerOrder;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.lang.LangClassWriter;

/**
 * This handles JIT compilation of input classes and generates C source code
 * from them.
 *
 * @since 2016/07/17
 */
public class CLangClassWriter
	extends LangClassWriter
{
	/** The owning namespace writer. */
	protected final CLangNamespaceWriter namespacewriter;
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the base writer support for classes.
	 *
	 * @param __n The basic name of class being written.
	 * @param __cn The symbol name of the class being written.
	 * @param __ps The output file stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/17
	 */
	public CLangClassWriter(CLangNamespaceWriter __nsw,
		String __n, ClassNameSymbol __cn, PrintStream __ps)
		throws NullPointerException
	{
		super(__n, __cn, __ps);
		
		// Check
		if (__nsw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.namespacewriter = __nsw;
		
		// Include global headers
		PrintStream output = this.output;
		__nsw.__globalHeader(output);
		
		// Write basic class storage information
		String idprefix = __n;
		output.print("const SJME_Class ");
		output.print(idprefix);
		output.println(" =");
		output.println("{");
		
		// Extern this structure in the header
		PrintStream headerout = __nsw.namespaceHeader();
		headerout.print("extern const SJME_Class ");
		headerout.print(idprefix);
		headerout.println(';');
		
		// Structure type is class
		output.println("\tSJME_STRUCTURETYPE_CLASS,");
		
		// Write the class name
		output.print("\t&");
		output.print(__nsw.addString(__cn.toString()));
		output.println(',');
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/18
	 */
	@Override
	public void classFlags(JITClassFlags __cf)
		throws JITException, NullPointerException
	{
		// {@squirreljme.error BA02 Did not expect class flags to be written.
		// (The current order)}
		JITCompilerOrder order = this.order;
		if (order != JITCompilerOrder.CLASS_FLAGS)
			throw new JITException(String.format("BA02 %s", order));
		this.order = order.next();
		
		// Print all flags
		PrintStream output = this.output;
		boolean spl = false;
		output.print('\t');
		if (__cf.isEmpty())
			output.print('0');
		else
			for (JITClassFlag f : __cf)
			{
				// Split?
				if (spl)
					output.print(" | ");
				spl = true;
			
				// Just use the enumeration
				output.print("SJME_CLASSFLAG_");
				output.print(f.name());
			}
		
		// End the line
		output.println(',');
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/18
	 */
	@Override
	public void close()
	{
		if (!this._closed)
		{
			// Close
			this._closed = true;
			
			// End structure
			PrintStream output = this.output;
			
			// Last element is zero
			output.println("\t0");
			output.println("};");
			output.println();
		}
		
		// Close
		super.close();
	}
}

