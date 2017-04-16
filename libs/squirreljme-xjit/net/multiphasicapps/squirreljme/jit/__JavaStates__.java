// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.linkage.MethodFlags;

/**
 * This parses the stack map table using either the modern Java 6 format or
 * the ancient CLDC 1.0 format.
 *
 * Since both formats are virtually the same (and the modern format being based
 * on the older format), the same code can be used during the parsing stage.
 *
 * @since 2016/03/25
 */
@Deprecated
class __JavaStates__
{
	/** The output verification map. */
	private final Map<Integer, __JavaState__> _map;
	
	/**
	 * Sets up the initial state for the stack based on the method flags and
	 * the method type.
	 *
	 * @param __mf Method flags.
	 * @param __ms The method type.
	 * @param __maxs Max stack entries.
	 * @param __maxl Max local variables.
	 * @return The initial mapping of state.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/12
	 */
	__JavaStates__(MethodFlags __mf, MethodSymbol __ms, int __maxs, int __maxl)
		throws NullPointerException
	{
		this._map = __initialState(__mf, __ms, __maxs, __maxl);
	}
	
	/**
	 * This initializes and performs the parsing.
	 *
	 * @param __m Parse the moderm format?
	 * @param __in The input stream containing the data.
	 * @param __mf The method flags (used to determine if it is static).
	 * @param __ms The method type.
	 * @param __maxs Maximum stack size.
	 * @param __maxl Maximum local size.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/25
	 */
	__JavaStates__(boolean __m, DataInputStream __in, MethodFlags __mf,
		MethodSymbol __ms, int __maxs, int __maxl)
		throws IOException, NullPointerException
	{
		// Check
		if (__in == null || __mf == null || __ms == null)
			throw new NullPointerException("NARG");
		
		// Initialize data needed for parsing
		Map<Integer, __JavaState__> outputmap =
			__initialState(__mf, __ms, __maxs, __maxl);
		this._map = outputmap;
		__Data__ data = new __Data__(__maxs, __maxl, outputmap);
		
		// Parsing the class stack map table
		if (!__m)
		{
			// Read the number of entries in the table
			int ne = __in.readUnsignedShort();
			
			// All entries in the table are full frames
			for (int i = 0; i < ne; i++)
				__oldStyle(__in, data);
		}
		
		// The modern stack map table
		else
		{
			// Read the number of entries in the table
			int ne = __in.readUnsignedShort();
			
			// Read them all
			for (int i = 0; i < ne; i++)
			{
				// Read the frame type
				int type = __in.readUnsignedByte();
				
				// Full frame?
				if (type == 255)
					__fullFrame(__in, data);
				
				// Same frame?
				else if (type >= 0 && type <= 63)
					__sameFrame(__in, data, type);
				
				// Same locals but a single stack item
				else if (type >= 64 && type <= 127)
					__sameLocalsSingleStack(__in, data, type - 64);
				
				// Same locals, single stack item, explicit delta
				else if (type == 247)
					__sameLocalsSingleStackExplicit(__in, data);
				
				// Chopped frame
				else if (type >= 248 && type <= 250)
					__choppedFrame(__in, data, 251 - type);
				
				// Same frame but with a supplied delta
				else if (type == 251)
					__sameFrameDelta(__in, data);
				
				// Appended frame
				else if (type >= 252 && type <= 254)
					__appendFrame(__in, data, type - 251);
				
				// {@squirreljme.error AQ18 Unknown StackMapTable verification
				// type. (The verification type)}
				else
					throw new JITException(String.format("AQ18 %d", type));
			}
		}
	}
	
	/**
	 * Obtains the state for the given address or {@code null} if there is
	 * none.
	 *
	 * @param __addr The address to the get the state for.
	 * @return The state for the address or {@code null}.
	 * @since 2017/04/12
	 */
	public __JavaState__ get(int __addr)
	{
		return this._map.get(__addr);
	}
	
	/**
	 * Append extra locals to the frame and clear the stack.
	 *
	 * @param __in The input stream.
	 * @param __d The data information.
	 * @param __addlocs The number of local variables to add.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __appendFrame(DataInputStream __in, __Data__ __d,
		int __addlocs)
		throws IOException
	{
		// Get the atom to use
		__JavaState__ next = __d.__next(__in.readUnsignedShort(), false);
		
		// Stack is cleared
		next._stack.setStackTop(0);
		
		// Read in local variables
		__JavaState__.Locals locals = next._locals;
		int n = __d._maxlocals;
		for (int i = 0; __addlocs > 0 && i < n; i++)
		{
			// Get slot here
			JavaType s = locals.get(i);
			
			// If it is not empty, ignore it
			if (s != JavaType.NOTHING)
				continue;
			
			// Set it
			JavaType aa;
			locals.set(i, (aa = __loadInfo(__in)));
			__addlocs--;
			
			// If a wide element was added, then the next one becomes TOP
			if (aa.isWide())
				locals.set(++i, JavaType.TOP);
		}
		
		// Error if added stuff remains
		// {@squirreljme.error AQ19 Appending local variables to the frame
		// however there is no room to place them. (The remaining local count)}
		if (__addlocs != 0)
			throw new JITException(String.format("AQ19 %d", __addlocs));
	}
	
	/**
	 * Similar frame with no stack and the top few locals removed.
	 *
	 * @param __in The input stream.
	 * @param __d The data information.
	 * @param __chops The number of variables which get chopped.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __choppedFrame(DataInputStream __in, __Data__ __d,
		int __chops)
		throws IOException
	{
		// Get the atom to use
		__JavaState__ next = __d.__next(__in.readUnsignedShort(), false);
		
		// No stack
		next._stack.setStackTop(0);
		
		// Chop off some locals
		__JavaState__.Locals locals = next._locals;
		int i, n = __d._maxlocals;
		for (i = n - 1; __chops > 0 && i >= 0; i--)
		{
			// Get slot here
			JavaType s = locals.get(i);
			
			// If it is empty, ignore it
			if (s == JavaType.NOTHING)
				continue;
			
			// Clear it
			locals.set(i, JavaType.NOTHING);
			__chops--;
		}
		
		// Still chops left?
		// {@squirreljme.error AQ1a Could not chop off all local variables
		// because there are no variables remaining to be chopped. (The
		// remaining variables to remove)}
		if (__chops != 0)
			throw new JITException(String.format("AQ1a %d", __chops));
	}
	
	/**
	 * This reads and parses the full stack frame.
	 *
	 * @param __in The input stream.
	 * @param __d The data information.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __fullFrame(DataInputStream __in, __Data__ __d)
		throws IOException
	{
		// Get the atom to use
		__JavaState__ next = __d.__next(__in.readUnsignedShort(), false);
		
		// Read in local variables
		int nl = __in.readUnsignedShort();
		
		// {@squirreljme.error AQ1b The number of specified local variables in
		// the full frame exceeds the maximum permitted local variable
		// count. (The read local variable count; The number of locals the
		// method uses)}
		int maxlocals = __d._maxlocals,
			maxstack = __d._maxstack;
		if (nl > maxlocals)
			throw new JITException(String.format("AQ1b %d %d", nl,
				maxlocals));
		int i;
		__JavaState__.Locals locals = next._locals;
		for (i = 0; i < nl; i++)
			locals.set(i, __loadInfo(__in));
		for (;i < maxlocals; i++)
			locals.set(i, JavaType.NOTHING);
		
		// Read in stack variables
		__JavaState__.Stack stack = next._stack;
		int ns = __in.readUnsignedShort();
		for (i = 0; i < ns; i++)
			stack.set(i, __loadInfo(__in));
		stack.setStackTop(ns);
	}
	
	/**
	 * Loads type information for the stack.
	 *
	 * @param __in The input stream.
	 * @return The type which was parsed.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private JavaType __loadInfo(DataInputStream __in)
		throws IOException
	{
		// Read the tag
		int tag = __in.readUnsignedByte();
		
		// Depends on the tag
		switch (tag)
		{
				// Top
			case 0:
				return JavaType.TOP;
				
				// Integer
			case 1:
				return JavaType.INTEGER;
				
				// Float
			case 2:
				return JavaType.FLOAT;
				
				// Double
			case 3:
				return JavaType.DOUBLE;
				
				// Long
			case 4:
				return JavaType.LONG;
				
				// Nothing
			case 5:
				return JavaType.NOTHING;
				
				// Uninitialized object
			case 6:
				return JavaType.OBJECT;
				
				// Initialized object or a new object which has yet to be
				// invokespecialed
			case 7:
			case 8:
				__in.readUnsignedShort();
				return JavaType.OBJECT;
				
				// Unknown
			default:
				// {@squirreljme.error AQ1c The verification tag in the
				// StackMap/STackMapTable attribute is not valid. (The tag)}
				throw new JITException(String.format("AQ1c %d", tag));
		}
	}
	
	/**
	 * Reads in an old style full frame.
	 *
	 * @param __in The input stream.
	 * @param __d The data information.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __oldStyle(DataInputStream __in, __Data__ __d)
		throws IOException
	{
		// Get the atom to use
		__JavaState__ next = __d.__next(__in.readUnsignedShort(), true);
		
		// Read in local variables
		int nl = __in.readUnsignedShort();
		
		// {@squirreljme.error AQ1d Old-style full frame specified more local
		// variables than there are in the method. (The number of locals; The
		// maximum number of locals)}
		int maxlocals = __d._maxlocals;
		if (nl > maxlocals)
			throw new JITException(String.format("AQ1d %d %d", nl,
				maxlocals));
		__JavaState__.Locals locals = next._locals;
		int i = 0;
		for (i = 0; i < nl; i++)
			locals.set(i, __loadInfo(__in));
		for (;i < maxlocals; i++)
			locals.set(i, JavaType.NOTHING);
		
		// Read in stack variables
		__JavaState__.Stack stack = next._stack;
		int ns = maxlocals + __in.readUnsignedShort();
		for (i = maxlocals; i < ns; i++)
			stack.set(i, __loadInfo(__in));
		stack.setStackTop(ns);
	}
	
	/**
	 * The same frame is used with no changes.
	 *
	 * @param __in The input stream.
	 * @param __d The data information.
	 * @param __delta The offset from the earlier offset.
	 * @since 2016/03/26
	 */
	private void __sameFrame(DataInputStream __in, __Data__ __d, int __delta)
	{
		__JavaState__ next = __d.__next(__delta, false);
	}
	
	/**
	 * Same frame but with a supplied delta rather than using it with the type.
	 *
	 * @param __in The input stream.
	 * @param __d The data information.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __sameFrameDelta(DataInputStream __in, __Data__ __d)
		throws IOException
	{
		__JavaState__ next = __d.__next(__in.readUnsignedShort(), false);
	}
	
	/**
	 * Same locals but the stack has only a single entry.
	 *
	 * @param __in The input stream.
	 * @param __d The data information.
	 * @param __delta The delta offset.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __sameLocalsSingleStack(DataInputStream __in, __Data__ __d,
		int __delta)
		throws IOException
	{
		// Get the atom to use
		__JavaState__ next = __d.__next(__delta, false);
		
		// Set the single stack
		__JavaState__.Stack stack = next._stack;
		stack.setStackTop(1);
		stack.set(0, __loadInfo(__in));
	}
	
	/**
	 * Same locals but the stack has only a single entry, the delta offset
	 * is specified.
	 *
	 * @param __in The input stream.
	 * @param __d The data information.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private void __sameLocalsSingleStackExplicit(DataInputStream __in,
		__Data__ __d)
		throws IOException
	{
		__sameLocalsSingleStack(__in, __d, __in.readUnsignedShort());
	}
	
	/**
	 * Sets up the initial state for the stack based on the method flags and
	 * the method type.
	 *
	 * @param __mf Method flags.
	 * @param __ms The method type.
	 * @param __maxs Max stack entries.
	 * @param __maxl Max local variables.
	 * @return The initial mapping of state.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/29
	 */
	static private Map<Integer, __JavaState__> __initialState(MethodFlags __mf,
		MethodSymbol __ms, int __maxs, int __maxl)
		throws NullPointerException
	{
		// Check
		if (__mf == null || __ms == null)
			throw new NullPointerException("NARG");
		
		// Setup base
		Map<Integer, __JavaState__> rv = new LinkedHashMap<>();
		
		// Setup initial verification state for the given map
		__JavaState__ es = new __JavaState__(__maxs, __maxl);
		rv.put(0, es);
		
		// Is this method static?
		boolean isstatic = __mf.isStatic();
		
		// Setup state
		__JavaState__.Locals loc = es._locals;
		int vat = 0;
		try
		{
			// Add object if not static
			if (!isstatic)
				loc.set(vat++, JavaType.OBJECT);
			
			// Go through all arguments
			int n = __ms.argumentCount();
			for (int i = 0; i < n; i++)
			{
				// Convert to type
				JavaType vt = JavaType.bySymbol(__ms.get(i));
				
				// Set
				loc.set(vat++, vt);
				
				// If wide, skip one
				if (vt.isWide())
					loc.set(vat++, JavaType.TOP);
			}
			
			// {@squirreljme.error AQ16 There are too many local variables.}
			if (vat > __maxl)
				throw new IndexOutOfBoundsException("AQ16");
		}
		
		// Initialization out of bounds
		catch (IndexOutOfBoundsException e)
		{
			// {@squirreljme.error AQ17 There are not enough local variables
			// to store the input method arguments. (The number of input
			// variables; The number of local variables)}
			throw new JITException(String.format("AQ17", vat,
				__maxl), e);
		}
		
		// Use that
		return rv;
	}
	
	/**
	 * Working data when parsing.
	 *
	 * @since 2017/04/12
	 */
	private static final class __Data__
	{
		/** The maximum local count. */
		final int _maxlocals;
	
		/** The maximum stack count. */
		final int _maxstack;
		
		/** The output verification map. */
		final Map<Integer, __JavaState__> _out;
		
		/** The placement address. */
		volatile int _placeaddr;
	
		/** The next state to verify for. */
		volatile __JavaState__ _next;
		
		/**
		 * Initializes the output data.
		 *
		 * @param __ms The stack limit.
		 * @param __ml The local limit.
		 * @param __out The output PC to state mappings.
		 * @since 2017/04/12
		 */
		private __Data__(int __ms, int __ml, Map<Integer, __JavaState__> __out)
		{
			this._maxstack = __ms;
			this._maxlocals = __ml;
			this._out = __out;
			
			// Initial next state is the first state
			this._next = new __JavaState__(__out.get(0));
		}
		
		/**
		 * Calculates the next state to use.
		 *
		 * @param __d The data information.
		 * @param __au The address offset.
		 * @param __abs Absolute position?
		 * @return The state for the next address.
		 * @since 2016/03/26
		 */
		__JavaState__ __next(int __au, boolean __abs)
		{
			// The current placement
			__JavaState__ now = this._next;
		
			// Setup new next
			__JavaState__ next = new __JavaState__(now);
			this._next = next;
		
			// Set new placement address
			int naddr = this._placeaddr;
			int pp = (__abs ? __au :
				naddr + (__au + (naddr == 0 ? 0 : 1)));
			this._placeaddr = pp;
		
			// Set the state
			this._out.put(pp, now);
		
			// The next state
			return next;
		}
	}
}

