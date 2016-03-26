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
		_last = state.get(0);
		
		// Parsing the class stack map table
		if (!modern)
		{
			// Read the number of entries in the table
			int ne = in.readUnsignedShort();
			
			// All entries in the table are full frames
			for (int i = 0; i < ne; i++)
				__oldStyle();
		}
		
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
				
				System.err.printf("DEBUG -- %d%n", type);
				
				// Full frame?
				if (type == 255)
					__fullFrame();
				
				else
					throw new Error("TODO");
				
				System.err.printf("DEBUG -- %s%n", state);
			}
		}
	}
	
	/**
	 * Calculates the next atom to use.
	 *
	 * @param __au The address offset.
	 * @return The atom for the next address.
	 * @since 2016/03/26
	 */
	private JVMProgramState.Atom __calculateAtom(int __au)
	{
		// Get the current atom
		JVMProgramState.Atom now = _last;
		int naddr = now.getAddress();
		
		// Get the next atom to use
		now = state.get(naddr + (__au + (naddr == 0 ? 0 : 1)), true);
		_last = now;
		return now;
	}
	
	/**
	 * This reads and parses the full stack frame.
	 *
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __fullFrame()
		throws IOException
	{
		// Get the atom to use
		DataInputStream das = in;
		JVMProgramState.Atom atom = __calculateAtom(das.readUnsignedShort());
		
		// Read in local variables
		int nl = das.readUnsignedShort();
		JVMProgramState.Variables locals = atom.locals();
		for (int i = 0; i < nl; i++)
			locals.get(i).setType(__loadInfo());
		
		// Read in stack variables
		int ns = das.readUnsignedShort();
		JVMProgramState.Variables stack = atom.stack();
		for (int i = 0; i < ns; i++)
			stack.get(i).setType(__loadInfo());
		stack.setStackTop(ns);
	}
	
	/**
	 * Loads type information for the stack.
	 *
	 * @return The type which was parsed.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private JVMVariableType __loadInfo()
		throws IOException
	{
		// Read the tag
		DataInputStream das = in;
		int tag = das.readUnsignedByte();
		
		// Depends on the tag
		switch (tag)
		{
				// Top
			case 0:
				return JVMVariableType.TOP;
				
				// Integer
			case 1:
				return JVMVariableType.INTEGER;
				
				// Float
			case 2:
				return JVMVariableType.FLOAT;
				
				// Double
			case 3:
				return JVMVariableType.DOUBLE;
				
				// Long
			case 4:
				return JVMVariableType.LONG;
				
				// Nothing
			case 5:
				return JVMVariableType.NOTHING;
				
				// Uninitialized object
			case 6:
				return JVMVariableType.OBJECT;
				
				// Initialized object or a new object which has yet to be
				// invokespecialed
			case 7:
			case 8:
				das.readUnsignedShort();
				return JVMVariableType.OBJECT;
				
				// Unknown
			default:
				throw new JVMClassFormatError(String.format("IN1t %d", tag));
		}
	}
	
	/**
	 * Reads in an old style full frame.
	 *
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __oldStyle()
		throws IOException
	{
		// Get the atom to use
		DataInputStream das = in;
		JVMProgramState.Atom atom = state.get(das.readUnsignedShort(), true);
		_last = atom;
		
		// Read in local variables
		int nl = das.readUnsignedShort();
		JVMProgramState.Variables locals = atom.locals();
		for (int i = 0; i < nl; i++)
			locals.get(i).setType(__loadInfo());
		
		// Read in stack variables
		int ns = das.readUnsignedShort();
		JVMProgramState.Variables stack = atom.stack();
		for (int i = 0; i < ns; i++)
			stack.get(i).setType(__loadInfo());
		stack.setStackTop(ns);
	}
}

