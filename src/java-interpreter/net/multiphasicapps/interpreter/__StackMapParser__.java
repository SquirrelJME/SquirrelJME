// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * This parses the stack map table using either the modern Java 6 format or
 * the ancient CLDC 1.0 format.
 *
 * Since both formats are virtually the same (and the modern format being based
 * on the older format), the same code can be used during the parsing stage.
 *
 * @since 2016/03/25
 */
class __StackMapParser__
{
	/** Use the modern StackMapTable parser? */
	protected final boolean modern;
	
	/** The input source. */
	protected final DataInputStream in;
	
	/** The program state to modify. */
	protected final JVMProgramState state;
	
	/** The last frame used. */
	private volatile JVMProgramState.Atom _last;
	
	/**
	 * This initializes and performs the parsing.
	 *
	 * @param __m Parse the moderm format?
	 * @param __in The input stream containing the data.
	 * @param __ps The state of the program to verify for.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/25
	 */
	__StackMapParser__(boolean __m, DataInputStream __in, JVMProgramState __ps)
		throws IOException, NullPointerException
	{
		// Check
		if (__in == null || __ps == null)
			throw new NullPointerException("NARG");
		
		// Set
		modern = __m;
		in = __in;
		state = __ps;
		
		// The last is always the first!
		_last = __ps.get(0);
		
		// Parsing the class stack map table
		if (!modern)
			throw new Error("TODO");
		
		// The modern stack map table
		else
		{
			// Read the number of entries in the table
			int ne = in.readUnsignedShort();
			
			// Read them all
			for (int i = 0; i < ne; i++)
			{
				// Read the frame type
				int type = in.readUnsignedByte();
				
				throw new Error("TODO");
			}
		}
	}
}

