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
	private volatile JVMProgramAtom _last;
	
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
				
				// Full frame?
				if (type == 255)
					__fullFrame();
				
				// Same frame?
				else if (type >= 0 && type <= 63)
					__sameFrame(type);
				
				// Same locals but a single stack item
				else if (type >= 64 && type <= 127)
					__sameLocalsSingleStack(type - 64);
				
				// Same locals, single stack item, explicit delta
				else if (type == 247)
					__sameLocalsSingleStackExplicit();
				
				// Chopped frame
				else if (type >= 248 && type <= 250)
					__choppedFrame(251 - type);
				
				// Same frame but with a supplied delta
				else if (type == 251)
					__sameFrameDelta();
				
				// Appended frame
				else if (type >= 252 && type <= 254)
					__appendFrame(type - 251);
				
				else
					throw new JVMClassFormatError(
						String.format("WTFX %d", type));
			}
		}
	}
	
	/**
	 * Append extra locals to the frame and clear the stack.
	 *
	 * @param __addlocs The number of local variables to add.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __appendFrame(int __addlocs)
		throws IOException
	{
		// Get the atom to use
		DataInputStream das = in;
		JVMProgramAtom atom = __calculateAtom(das.readUnsignedShort(),
			false);
		
		// Stack is cleared
		atom.stack().setStackTop(0);
		
		// Read in local variables
		JVMProgramVars locals = atom.locals();
		int n = locals.size();
		for (int i = 0; __addlocs > 0 && i < n; i++)
		{
			// Get slot here
			JVMProgramSlot s = locals.get(i);
			
			// If it is not empty, ignore it
			if (s.getType() != JVMVariableType.NOTHING)
				continue;
			
			// Set it
			s.setType(__loadInfo());
			__addlocs--;
		}
		
		// Error if added stuff remains
		if (__addlocs != 0)
			throw new JVMClassFormatError(String.format("IN1v %d", __addlocs));
	}
	
	/**
	 * Calculates the next atom to use.
	 *
	 * @param __au The address offset.
	 * @param __abs Absolute position?
	 * @return The atom for the next address.
	 * @since 2016/03/26
	 */
	private JVMProgramAtom __calculateAtom(int __au, boolean __abs)
	{
		// Get the current atom
		JVMProgramAtom now = _last, was = now;
		int naddr = now.getAddress();
		
		// Get the next atom to use
		now = state.get((__abs ? __au :
			naddr + (__au + (naddr == 0 ? 0 : 1))), true);
		_last = now;
		
		// Copy local states
		JVMProgramVars al = now.locals(),
			bl = was.locals();
		int n = al.size();
		for (int i = 0; i < n; i ++)
			al.get(i).setType(bl.get(i).getType());
		
		// Copy stack states
		JVMProgramVars as = now.stack(),
			bs = was.stack();
		n = as.size();
		for (int i = 0; i < n; i ++)
			as.get(i).setType(bs.get(i).getType());
		as.setStackTop(bs.getStackTop());
		
		// Return it
		return now;
	}
	
	/**
	 * Similar frame with no stack and the top few locals removed.
	 *
	 * @param __chops The number of variables which get chopped.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __choppedFrame(int __chops)
		throws IOException
	{
		// Get the atom to use
		DataInputStream das = in;
		JVMProgramAtom atom = __calculateAtom(das.readUnsignedShort(),
			false);
		
		// No stack
		atom.stack().setStackTop(0);
		
		// Chop off some locals
		JVMProgramVars locals = atom.locals();
		int i, n = locals.size();
		for (i = n - 1; __chops > 0 && i >= 0; i--)
		{
			// Get slot here
			JVMProgramSlot s = locals.get(i);
			
			// If it is empty, ignore it
			if (s.getType() == JVMVariableType.NOTHING)
				continue;
			
			// Clear it
			s.setType(JVMVariableType.NOTHING);
			__chops--;
		}
		
		// Still chops left?
		if (__chops != 0)
			throw new JVMClassFormatError(String.format("IN1u %d", __chops));
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
		JVMProgramAtom atom = __calculateAtom(das.readUnsignedShort(),
			false);
		
		// Read in local variables
		int nl = das.readUnsignedShort();
		JVMProgramVars locals = atom.locals();
		for (int i = 0; i < nl; i++)
			locals.get(i).setType(__loadInfo());
		
		// Read in stack variables
		int ns = das.readUnsignedShort();
		JVMProgramVars stack = atom.stack();
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
		JVMProgramAtom atom = __calculateAtom(das.readUnsignedShort(),
			true);
		
		// Read in local variables
		int nl = das.readUnsignedShort();
		JVMProgramVars locals = atom.locals();
		for (int i = 0; i < nl; i++)
			locals.get(i).setType(__loadInfo());
		
		// Read in stack variables
		int ns = das.readUnsignedShort();
		JVMProgramVars stack = atom.stack();
		for (int i = 0; i < ns; i++)
			stack.get(i).setType(__loadInfo());
		stack.setStackTop(ns);
	}
	
	/**
	 * The same frame is used with no changes.
	 *
	 * @param __delta The offset from the earlier offset.
	 * @since 2016/03/26
	 */
	private void __sameFrame(int __delta)
	{
		JVMProgramAtom atom = __calculateAtom(__delta, false);
	}
	
	/**
	 * Same frame but with a supplied delta rather than using it with the type.
	 *
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __sameFrameDelta()
		throws IOException
	{
		JVMProgramAtom atom = __calculateAtom(in.readUnsignedShort(),
			false);
	}
	
	/**
	 * Same locals but the stack has only a single entry.
	 *
	 * @param __delta The delta offset.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __sameLocalsSingleStack(int __delta)
		throws IOException
	{
		// Get the atom to use
		DataInputStream das = in;
		JVMProgramAtom atom = __calculateAtom(__delta, false);
		
		// Set the single stack
		JVMProgramVars stack = atom.stack();
		stack.get(0).setType(__loadInfo());
		stack.setStackTop(1);
	}
	
	/**
	 * Same locals but the stack has only a single entry.
	 *
	 * @param __delta The delta offset.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __sameLocalsSingleStackExplicit()
		throws IOException
	{
		// Get the atom to use
		DataInputStream das = in;
		JVMProgramAtom atom = __calculateAtom(das.readUnsignedShort(),
			false);
		
		// Set the single stack
		JVMProgramVars stack = atom.stack();
		stack.get(0).setType(__loadInfo());
		stack.setStackTop(1);
	}
}

