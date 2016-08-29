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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITMethodFlags;

/**
 * This parses the stack map table using either the modern Java 6 format or
 * the ancient CLDC 1.0 format.
 *
 * Since both formats are virtually the same (and the modern format being based
 * on the older format), the same code can be used during the parsing stage.
 *
 * @since 2016/03/25
 */
class __SMTParser__
{
	/** Use the modern StackMapTable parser? */
	protected final boolean modern;
	
	/** The input source. */
	protected final DataInputStream in;
	
	/** The maximum local count. */
	protected final int maxlocals;
	
	/** The maximum stack count. */
	protected final int maxstack;
	
	/** The output verification map. */
	protected final Map<Integer, __SMTState__> outputmap =
		new HashMap<>();
	
	/** The placement address. */
	private volatile int _placeaddr;
	
	/** The next state to verify for. */
	private volatile __SMTState__ _next;
	
	/**
	 * This initializes and performs the parsing.
	 *
	 * @param __m Parse the moderm format?
	 * @param __in The input stream containing the data.
	 * @param __mf The method flags (used to determine if it is static).
	 * @param __ms The method type.
	 * @param __maxs Maximum stack size.
	 * @param __maxl Maximum local size.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/25
	 */
	__SMTParser__(boolean __m, DataInputStream __in, JITMethodFlags __mf,
		MethodSymbol __ms, int __maxs, int __maxl)
		throws NullPointerException
	{
		// Check
		if (__in == null || __mf == null || __ms == null)
			throw new NullPointerException("NARG");
		
		// Set
		modern = __m;
		in = __in;
		program = __prg;
		maxlocals = __maxl;
		maxstack = __maxs;
		
		// Setup initial verification state for the given map
		__SMTState__ es = new __SMTState__(__maxs, __maxl);
		outputmap.put(0, es);
		
		// Is this method static?
		boolean isstatic = __mf.isStatic();
		
		// Setup state
		__SMTLocals__ loc = es._locals;
		int vat = 0;
		try
		{
			// Add object if not static
			if (!isstatic)
				loc.set(vat++, __SMTType__.OBJECT);
			
			// Go through all arguments
			int n = __ms.argumentCount();
			for (int i = 0; i < n; i++)
			{
				// Convert to type
				__SMTType__ vt = __SMTType__.bySymbol(__ms.get(i));
				
				// Set
				loc.set(vat++, vt);
				
				// If wide, skip one
				if (vt.isWide())
					loc.set(vat++, __SMTType__.TOP);
			}
			
			// {@squirreljme.error ED0s There are too many local variables.}
			if (vat > maxlocals)
				throw new IndexOutOfBoundsException("ED0s");
		}
		
		// Initialization out of bounds
		catch (IndexOutOfBoundsException e)
		{
			// {@squirreljme.error ED0c There are not enough local variables
			// to store the input method arguments. (The number of input
			// variables; The number of local variables)}
			throw new JITException(String.format("ED0c", vat,
				maxlocals), e);
		}
		
		// Make a copy of the last state for the next state
		_next = new __SMTState__(es);
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
				
				// {@squirreljme.error ED0j Unknown StackMapTable verification
				// type. (The verification type)}
				else
					throw new JITException(JITException.Issue.
						UNKNOWN_STACK_TYPE, String.format("ED0j %d", type));
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
		__SMTState__ next = __calculateNext(das.readUnsignedShort(),
			false);
		
		// Stack is cleared
		next.setStackTop(maxlocals);
		
		// Read in local variables
		int n = maxlocals;
		for (int i = 0; __addlocs > 0 && i < n; i++)
		{
			// Get slot here
			__SMTType__ s = next.get(i);
			
			// If it is not empty, ignore it
			if (s != __SMTType__.NOTHING)
				continue;
			
			// Set it
			__SMTType__ aa;
			next.set(i, (aa = __loadInfo()));
			__addlocs--;
			
			// If a wide element was added, then the next one becomes TOP
			if (aa.isWide())
				next.set(++i, __SMTType__.TOP);
		}
		
		// Error if added stuff remains
		// {@squirreljme.error ED0k Appending local variables to the frame
		// however there is no room to place them. (The remaining local count)}
		if (__addlocs != 0)
			throw new JITException(String.format("ED0k %d", __addlocs));
	}
	
	/**
	 * Calculates the next state to use.
	 *
	 * @param __au The address offset.
	 * @param __abs Absolute position?
	 * @return The state for the next address.
	 * @since 2016/03/26
	 */
	private __SMTState__ __calculateNext(int __au, boolean __abs)
	{
		// The current placement
		__SMTState__ now = _next;
		
		// Setup new next
		__SMTState__ next = new __SMTState__(now);
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
		__SMTState__ next = __calculateNext(das.readUnsignedShort(),
			false);
		
		// No stack
		next.setStackTop(maxlocals);
		
		// Chop off some locals
		int i, n = maxlocals;
		for (i = n - 1; __chops > 0 && i >= 0; i--)
		{
			// Get slot here
			__SMTType__ s = next.get(i);
			
			// If it is empty, ignore it
			if (s == __SMTType__.NOTHING)
				continue;
			
			// Clear it
			next.set(i, __SMTType__.NOTHING);
			__chops--;
		}
		
		// Still chops left?
		// {@squirreljme.error ED0l Could not chop off all local variables
		// because there are no variables remaining to be chopped. (The
		// remaining variables to remove)}
		if (__chops != 0)
			throw new JITException(String.format("ED0l %d", __chops));
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
		__SMTState__ next = __calculateNext(das.readUnsignedShort(),
			false);
		
		// Read in local variables
		int nl = das.readUnsignedShort();
		
		// {@squirreljme.error ED0o The number of specified local variables in
		// the full frame exceeds the maximum permitted local variable
		// count. (The read local variable count; The number of locals the
		// method uses)}
		if (nl > maxlocals)
			throw new JITException(String.format("ED00 %d %d", nl,
				maxlocals));
		int i;
		for (i = 0; i < nl; i++)
			next.set(i, __loadInfo());
		for (;i < maxlocals; i++)
			next.set(i, __SMTType__.NOTHING);
		
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
	private __SMTType__ __loadInfo()
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
				return __SMTType__.TOP;
				
				// Integer
			case 1:
				return __SMTType__.INTEGER;
				
				// Float
			case 2:
				return __SMTType__.FLOAT;
				
				// Double
			case 3:
				return __SMTType__.DOUBLE;
				
				// Long
			case 4:
				return __SMTType__.LONG;
				
				// Nothing
			case 5:
				return __SMTType__.NOTHING;
				
				// Uninitialized object
			case 6:
				return __SMTType__.OBJECT;
				
				// Initialized object or a new object which has yet to be
				// invokespecialed
			case 7:
			case 8:
				das.readUnsignedShort();
				return __SMTType__.OBJECT;
				
				// Unknown
			default:
				// {@squirreljme.error ED0p The verification tag in the
				// StackMap/STackMapTable attribute is not valid. (The tag)}
				throw new JITException(String.format("ED0p %d", tag));
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
		__SMTState__ next = __calculateNext(das.readUnsignedShort(),
			true);
		
		// Read in local variables
		int nl = das.readUnsignedShort();
		
		// {@squirreljme.error ED0q Old-style full frame specified more local
		// variables than there are in the method. (The number of locals; The
		// maximum number of locals)}
		if (nl > maxlocals)
			throw new JITException(String.format("ED0q %d %d", nl,
				maxlocals));
		int i = 0;
		for (i = 0; i < nl; i++)
			next.set(i, __loadInfo());
		for (;i < maxlocals; i++)
			next.set(i, __SMTType__.NOTHING);
		
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
		__SMTState__ next = __calculateNext(__delta, false);
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
		__SMTState__ next = __calculateNext(in.readUnsignedShort(),
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
		__SMTState__ next = __calculateNext(__delta, false);
		
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

