// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.bytecode;

import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.narf.classinterface.NCIByteBuffer;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

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
	
	/** The program to modify. */
	protected final CPProgram program;
	
	/** The maximum local count. */
	protected final int maxlocals;
	
	/** The maximum stack count. */
	protected final int maxstack;
	
	/** The output verification map. */
	protected final Map<Integer, CPVerifyState> outputmap =
		new HashMap<>();
	
	/** The placement address. */
	private volatile int _placeaddr;
	
	/** The next state to verify for. */
	private volatile CPVerifyState _next;
	
	/**
	 * This initializes and performs the parsing.
	 *
	 * @param __m Parse the moderm format?
	 * @param __in The input stream containing the data.
	 * @param __meth The owning code attribute.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/25
	 */
	__StackMapParser__(boolean __m, NCIByteBuffer __in, NBCByteCode __code)
		throws NullPointerException
	{
		// Check
		if (__in == null || __prg == null || __outmap == null ||
			__meth == null)
			throw new NullPointerException("NARG");
		
		// Set
		modern = __m;
		in = __in;
		program = __prg;
		maxlocals = program.maxLocals();
		maxstack = program.maxStack();
		outputmap = __outmap;
		
		// Setup initial verification state for the given map
		CPVerifyState es = new CPVerifyState(program);
		outputmap.put(0, es);
		
		// Is this method static?
		boolean isstatic = __meth.flags().isStatic();
		
		// Setup state
		int vat = 0;
		try
		{
			// Add object if not static
			if (!isstatic)
				es.set(vat++, CPVariableType.OBJECT);
			
			// Go through all arguments
			MethodSymbol ms = __meth.type();
			int n = ms.argumentCount();
			for (int i = 0; i < n; i++)
			{
				// Convert to type
				CPVariableType vt = CPVariableType.bySymbol(ms.get(i));
				
				// Set
				es.set(vat++, vt);
				
				// If wide, skip one
				if (vt.isWide())
					es.set(vat++, CPVariableType.TOP);
			}
			
			// If the size is exceeded then fail
			if (vat > maxlocals)
				throw new IndexOutOfBoundsException();
		}
		
		// Initialization out of bounds
		catch (IndexOutOfBoundsException e)
		{
			// {@squirreljme.error CP1j There are not enough local variables
			// to store the input method arguments. (The number of input
			// variables; The number of local variables)}
			throw new CPProgramException(String.format("CP1j", vat,
				maxlocals), e);
		}
		
		// Make a copy of the last state for the next state
		_next = new CPVerifyState(es);
		_placeaddr = 0;
		
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
					throw new CPProgramException(
						String.format("WTFX %d", type));
			}
		}
	}
	
	/**
	 * Returns the verification result.
	 *
	 * @return The result of verification.
	 * @since 2016/05/12
	 */
	public Map<Integer, NBCStateVerification> result()
	{
		return UnmodifiableMap.<Integer, NBCStateVerification>unmodifiable(
			outputmap);
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
		CPVerifyState next = __calculateNext(das.readUnsignedShort(),
			false);
		
		// Stack is cleared
		next.setStackTop(maxlocals);
		
		// Read in local variables
		int n = maxlocals;
		for (int i = 0; __addlocs > 0 && i < n; i++)
		{
			// Get slot here
			CPVariableType s = next.get(i);
			
			// If it is not empty, ignore it
			if (s != CPVariableType.NOTHING)
				continue;
			
			// Set it
			CPVariableType aa;
			next.set(i, (aa = __loadInfo()));
			__addlocs--;
			
			// If a wide element was added, then the next one becomes TOP
			if (aa.isWide())
				next.set(++i, CPVariableType.TOP);
		}
		
		// Error if added stuff remains
		// {@squirreljme.error CP0i Appending local variables to the frame
		// however there is no room to place them. (The remaining local count)}
		if (__addlocs != 0)
			throw new CPProgramException(String.format("CP0i %d", __addlocs));
	}
	
	/**
	 * Calculates the next state to use.
	 *
	 * @param __au The address offset.
	 * @param __abs Absolute position?
	 * @return The state for the next address.
	 * @since 2016/03/26
	 */
	private CPVerifyState __calculateNext(int __au, boolean __abs)
	{
		// The current placement
		CPVerifyState now = _next;
		
		// Setup new next
		CPVerifyState next = new CPVerifyState(now);
		_next = next;
		
		// Set new placement address
		int naddr = _placeaddr;
		_placeaddr = (__abs ? __au :
			naddr + (__au + (naddr == 0 ? 0 : 1)));
		
		// Set the state
		outputmap.put(program.physicalToLogical(_placeaddr), now);
		
		// The next state
		return next;
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
		CPVerifyState next = __calculateNext(das.readUnsignedShort(),
			false);
		
		// No stack
		next.setStackTop(maxlocals);
		
		// Chop off some locals
		int i, n = maxlocals;
		for (i = n - 1; __chops > 0 && i >= 0; i--)
		{
			// Get slot here
			CPVariableType s = next.get(i);
			
			// If it is empty, ignore it
			if (s == CPVariableType.NOTHING)
				continue;
			
			// Clear it
			next.set(i, CPVariableType.NOTHING);
			__chops--;
		}
		
		// Still chops left?
		// {@squirreljme.error CP0j Could not chop off all local variables
		// because there are no variables remaining to be chopped. (The
		// remaining variables to remove)}
		if (__chops != 0)
			throw new CPProgramException(String.format("CP0j %d", __chops));
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
		CPVerifyState next = __calculateNext(das.readUnsignedShort(),
			false);
		
		// Read in local variables
		int nl = das.readUnsignedShort();
		
		// {@squirreljme.error CP0k The number of specified local variables in
		// the full frame exceeds the maximum permitted local variable
		// count. (The read local variable count; The number of locals the
		// method uses)}
		if (nl > maxlocals)
			throw new CPProgramException(String.format("CP0k %d %d", nl,
				maxlocals));
		int i;
		for (i = 0; i < nl; i++)
			next.set(i, __loadInfo());
		for (;i < maxlocals; i++)
			next.set(i, CPVariableType.NOTHING);
		
		// Read in stack variables
		int ns = maxlocals + das.readUnsignedShort();
		for (i = maxlocals; i < ns; i++)
			next.set(i, __loadInfo());
		next.setStackTop(ns);
	}
	
	/**
	 * Loads type information for the stack.
	 *
	 * @return The type which was parsed.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private CPVariableType __loadInfo()
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
				return CPVariableType.TOP;
				
				// Integer
			case 1:
				return CPVariableType.INTEGER;
				
				// Float
			case 2:
				return CPVariableType.FLOAT;
				
				// Double
			case 3:
				return CPVariableType.DOUBLE;
				
				// Long
			case 4:
				return CPVariableType.LONG;
				
				// Nothing
			case 5:
				return CPVariableType.NOTHING;
				
				// Uninitialized object
			case 6:
				return CPVariableType.OBJECT;
				
				// Initialized object or a new object which has yet to be
				// invokespecialed
			case 7:
			case 8:
				das.readUnsignedShort();
				return CPVariableType.OBJECT;
				
				// Unknown
			default:
				// {@squirreljme.error CP0l The verification tag in the
				// StackMap/STackMapTable attribute is not valid. (The tag)}
				throw new CPProgramException(String.format("CP0l %d", tag));
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
		CPVerifyState next = __calculateNext(das.readUnsignedShort(),
			true);
		
		// Read in local variables
		int nl = das.readUnsignedShort();
		if (nl > maxlocals)
			throw new CPProgramException(String.format("CP0k %d %d", nl,
				maxlocals));
		int i = 0;
		for (i = 0; i < nl; i++)
			next.set(i, __loadInfo());
		for (;i < maxlocals; i++)
			next.set(i, CPVariableType.NOTHING);
		
		// Read in stack variables
		int ns = maxlocals + das.readUnsignedShort();
		for (i = maxlocals; i < ns; i++)
			next.set(i, __loadInfo());
		next.setStackTop(ns);
	}
	
	/**
	 * The same frame is used with no changes.
	 *
	 * @param __delta The offset from the earlier offset.
	 * @since 2016/03/26
	 */
	private void __sameFrame(int __delta)
	{
		CPVerifyState next = __calculateNext(__delta, false);
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
		CPVerifyState next = __calculateNext(in.readUnsignedShort(),
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
		CPVerifyState next = __calculateNext(__delta, false);
		
		// Set the single stack
		next.setStackTop(maxlocals + 1);
		next.set(maxlocals, __loadInfo());
	}
	
	/**
	 * Same locals but the stack has only a single entry, the delta offset
	 * is specified.
	 *
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __sameLocalsSingleStackExplicit()
		throws IOException
	{
		DataInputStream das = in;
		__sameLocalsSingleStack(das.readUnsignedShort());
	}
}

