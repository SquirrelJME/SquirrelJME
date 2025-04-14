// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is used to parse the stack map and initialize the initial
 * snapshot states for jump targets within the method.
 *
 * @since 2017/04/16
 */
final class __StackMapParser__
	implements Contexual
{
	/** The stream to decode from. */
	protected final DataInputStream in;
	
	/** The number of stack entries. */
	protected final int maxstack;
	
	/** The number of local entries. */
	protected final int maxlocals;
	
	/** The method byte code. */
	protected final ByteCode code;
	
	/** Constant pool. */
	protected final Pool pool;
	
	/** This type. */
	protected final JavaType thistype;
		
	/** Verification targets. */
	private final Map<Integer, StackMapTableState> _targets;
	
	/** The next stack state. */
	private final StackMapTableEntry[] _nextstack;
	
	/** The next local variable state. */
	private final StackMapTableEntry[] _nextlocals;
	
	/** The placement address. */
	private int _placeaddr;
	
	/** The top of the stack. */
	private int _stacktop;
	
	/**
	 * Initializes the stack map parser.
	 *
	 * @param __p The constant pool.
	 * @param __m The method this code exists within.
	 * @param __new Should the new stack map table format be used?
	 * @param __in The data for the stack map table.
	 * @param __bc The owning byte code.
	 * @param __tt This type.
	 * @throws InvalidClassFormatException If the stack map table is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	__StackMapParser__(Pool __p, Method __m, boolean __new, byte[] __in,
		ByteCode __bc, JavaType __tt)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__p == null || __m == null || __in == null || __bc == null ||
			__tt == null)
			throw new NullPointerException("NARG");
		
		// Set
		DataInputStream xin;
		this.in = (xin = new DataInputStream(
			new ByteArrayInputStream(__in)));
		int maxstack = __bc.maxStack(),
			maxlocals = __bc.maxLocals();
		this.maxstack = maxstack;
		this.maxlocals = maxlocals;
		this.code = __bc;
		this.pool = __p;
		this.thistype = __tt;
		
		// This is used to set which variables appear next before a state is
		// constructed with them
		StackMapTableEntry[] nextstack, nextlocals;
		this._nextstack = (nextstack = new StackMapTableEntry[maxstack]);
		this._nextlocals = (nextlocals = new StackMapTableEntry[maxlocals]);
		
		// Setup initial state
		/* {@squirreljme.error JC43 The arguments that are required for the
		given method exceeds the maximum number of permitted local
		variables. (The method in question; The required number of local
		variables; The maximum number of local variables)} */
		MethodHandle handle = __m.handle();
		boolean isinstance = !__m.flags().isStatic();
		JavaType[] jis = handle.javaStack(isinstance);
		int jn = jis.length;
		if (jn > maxlocals)
			throw new InvalidClassFormatException(
				String.format("JC43 %s %d %d", handle, jn, maxlocals), this);
		
		// Setup entries
		// If this is an instance initializer method then only the first
		// argument is not initialized
		boolean isiinit = isinstance && __m.name().isInstanceInitializer();
		for (int i = 0; i < jn; i++)
			nextlocals[i] = new StackMapTableEntry(jis[i],
				(!isiinit || (i != 0)));
		
		// Initialize entries with nothing
		for (int i = 0, n = nextstack.length; i < n; i++)
			if (nextstack[i] == null)
				nextstack[i] = StackMapTableEntry.NOTHING;
		for (int i = 0, n = nextlocals.length; i < n; i++)
			if (nextlocals[i] == null)
				nextlocals[i] = StackMapTableEntry.NOTHING;
		
		// Where states go
		Map<Integer, StackMapTableState> targets = new LinkedHashMap<>();
		this._targets = targets;
		
		// Record state
		this.__next(0, true, -1, -1);
		
		// Parse the stack map table
		try (DataInputStream in = xin)
		{
			// Parsing the class stack map table
			if (!__new)
			{
				// Read the number of entries in the table
				int ne = xin.readUnsignedShort();
			
				// All entries in the table are full frames
				for (int i = 0; i < ne; i++)
					this.__next(this.__oldStyle(), true, -1, i);
			}
		
			// The modern stack map table
			else
			{
				// Read the number of entries in the table
				int ne = xin.readUnsignedShort();
			
				// Read them all
				for (int i = 0; i < ne; i++)
				{
					// Read the frame type
					int type = xin.readUnsignedByte();
					int addr;
				
					// Full frame?
					if (type == 255)
						addr = this.__fullFrame();
				
					// Same frame?
					else if (type >= 0 && type <= 63)
						addr = this.__sameFrame(type);
				
					// Same locals but a single stack item
					else if (type >= 64 && type <= 127)
						addr = this.__sameLocalsSingleStack(type - 64);
				
					// Same locals, single stack item, explicit delta
					else if (type == 247)
						addr = this.__sameLocalsSingleStackExplicit();
				
					// Chopped frame
					else if (type >= 248 && type <= 250)
						addr = this.__choppedFrame(251 - type);
				
					// Same frame but with a supplied delta
					else if (type == 251)
						addr = this.__sameFrameDelta();
				
					// Appended frame
					else if (type >= 252 && type <= 254)
						addr = this.__appendFrame(type - 251);
				
					/* {@squirreljme.error JC44 Unknown StackMapTable
					verification type. (The verification type)} */
					else
						throw new InvalidClassFormatException(
							String.format("JC44 %d", type), this);
					
					// Setup next
					this.__next(addr, false, type, i);
				}
			}
		}
		
		/* {@squirreljme.error JC45 Failed to parse the stack map table.} */
		catch (IOException e)
		{
			throw new InvalidClassFormatException("JC45", e, this);
		}
	}
	
	/**
	 * Returns the stack map table.
	 *
	 * @return The parsed stack map table.
	 * @since 2017/10/16
	 */
	public StackMapTable get()
	{
		return new StackMapTable(this._targets);
	}
	
	/**
	 * Append extra locals to the frame and clear the stack.
	 *
	 * @param __addlocs The number of local variables to add.
	 * @return The address offset.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __appendFrame(int __addlocs)
		throws IOException
	{
		// Get the atom to use
		DataInputStream in = this.in;
		int rv = in.readUnsignedShort();
		
		// Stack is cleared
		this._stacktop = 0;
		
		// Read in local variables
		StackMapTableEntry[] nextlocals = this._nextlocals;
		int n = this.maxlocals;
		for (int i = 0; __addlocs > 0 && i < n; i++)
		{
			// Get slot here
			StackMapTableEntry s = nextlocals[i];
			
			// If it is not empty, ignore it
			if (!s.equals(StackMapTableEntry.NOTHING))
				continue;
			
			// Set it
			StackMapTableEntry aa;
			nextlocals[i] = (aa = this.__loadInfo());
			__addlocs--;
			
			// If a wide element was added, then the next one becomes TOP
			if (aa.isWide())
				nextlocals[++i] = aa.topType();
		}
		
		// Error if added stuff remains
		/* {@squirreljme.error JC46 Appending local variables to the frame
		however there is no room to place them. (The remaining local count)} */
		if (__addlocs != 0)
			throw new InvalidClassFormatException(
				String.format("JC46 %d", __addlocs), this);
		
		return rv;
	}
	
	/**
	 * Similar frame with no stack and the top few locals removed.
	 *
	 * @param __chops The number of variables which get chopped.
	 * @return The address offset.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __choppedFrame(int __chops)
		throws IOException
	{
		// Get the atom to use
		DataInputStream in = this.in;
		int rv = in.readUnsignedShort();
		
		// No stack
		this._stacktop = 0;
		
		// Chop off some locals
		StackMapTableEntry[] nextlocals = this._nextlocals;
		int i, n = this.maxlocals;
		for (i = n - 1; __chops > 0 && i >= 0; i--)
		{
			// Get slot here
			StackMapTableEntry s = nextlocals[i];
			
			// If it is empty, ignore it
			if (s.equals(StackMapTableEntry.NOTHING))
				continue;
			
			// Clear top off, but only if it is not an undefined top
			if (s.isTop() && !s.equals(StackMapTableEntry.TOP_UNDEFINED))
				nextlocals[i--] = StackMapTableEntry.NOTHING;
			
			// Clear it
			nextlocals[i] = StackMapTableEntry.NOTHING;
			__chops--;
		}
		
		// Still chops left?
		/* {@squirreljme.error JC47 Could not chop off all local variables
		because there are no variables remaining to be chopped. (The
		remaining variables to remove)} */
		if (__chops != 0)
			throw new InvalidClassFormatException(
				String.format("JC47 %d", __chops), this);
		
		return rv;
	}
	
	/**
	 * This reads and parses the full stack frame.
	 *
	 * @return The address offset.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __fullFrame()
		throws IOException
	{
		// Get the atom to use
		DataInputStream in = this.in;
		int rv = in.readUnsignedShort();
		
		// Read in local variables
		int nl = in.readUnsignedShort();
		
		/* {@squirreljme.error JC48 The number of specified local variables in
		the full frame exceeds the maximum permitted local variable
		count. (The read local variable count; The number of locals the
		method uses)} */
		int maxlocals = this.maxlocals,
			maxstack = this.maxstack;
		if (nl > maxlocals)
			throw new InvalidClassFormatException(
				String.format("JC48 %d %d", nl, maxlocals), this);
		int i, o;
		StackMapTableEntry[] nextlocals = this._nextlocals;
		for (i = 0, o = 0; i < nl; i++)
		{
			StackMapTableEntry e;
			nextlocals[o++] = (e = this.__loadInfo());
			
			// Add top?
			if (e.isWide())
				nextlocals[o++] = e.topType();
		}
		for (;o < maxlocals; o++)
			nextlocals[o] = StackMapTableEntry.NOTHING;
		
		// Read in stack variables
		StackMapTableEntry[] nextstack = this._nextstack;
		int ns = in.readUnsignedShort();
		for (i = 0, o = 0; i < ns; i++)
		{
			StackMapTableEntry e;
			nextstack[o++] = (e = this.__loadInfo());
			
			// Add top?
			if (e.isWide())
				nextstack[o++] = e.topType();
		}
		this._stacktop = o;
		
		return rv;
	}
	
	/**
	 * Loads type information for the stack.
	 *
	 * @return The type which was parsed.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private StackMapTableEntry __loadInfo()
		throws IOException
	{
		// Read the tag
		DataInputStream in = this.in;
		int tag = in.readUnsignedByte();
		
		// Depends on the tag
		switch (tag)
		{
				// Top
			case 0:
				return StackMapTableEntry.TOP_UNDEFINED;
				
				// Integer
			case 1:
				return StackMapTableEntry.INTEGER;
				
				// Float
			case 2:
				return StackMapTableEntry.FLOAT;
				
				// Double
			case 3:
				return StackMapTableEntry.DOUBLE;
				
				// Long
			case 4:
				return StackMapTableEntry.LONG;
				
				// Nothing
			case 5:
				return StackMapTableEntry.NOTHING;
				
				// Uninitialized this
			case 6:
				return new StackMapTableEntry(this.thistype, false);
				
				// Initialized object
			case 7:
				return new StackMapTableEntry(new JavaType(
					this.pool.<ClassName>get(ClassName.class,
					in.readUnsignedShort()).field()), true);
				
				// Uninitialized variable for a new instruction, the pc points
				// to the new instruction so the class must be read from
				// that instruction to determine the type of that actual
				// object
			case 8:
				return new StackMapTableEntry(new JavaType(this.pool.
					<ClassName>get(ClassName.class, this.code.
					readRawCodeUnsignedShort(in.readUnsignedShort() + 1))),
					false);
				
				// Unknown
			default:
				/* {@squirreljme.error JC49 The verification tag in the
				StackMap/StackMapTable attribute is not valid. (The tag)} */
				throw new InvalidClassFormatException(
					String.format("JC49 %d", tag), this);
		}
	}
	
	/**
	 * Initializes the next state.
	 *
	 * @param __au The address offset.
	 * @param __abs Absolute position?
	 * @param __type The type of entry that was just handled, this is for
	 * debug purposes.
	 * @param __ne The entry number of this index.
	 * @return The state for the next address.
	 * @since 2016/05/20
	 */
	StackMapTableState __next(int __au, boolean __abs, int __type, int __ne)
	{
		// Where are we?
		int naddr = this._placeaddr;
		
		// Generate it
		StackMapTableState rv;
		try
		{
			rv = new StackMapTableState(this._nextlocals,
				this._nextstack, this._stacktop);
		}
		catch (InvalidClassFormatException e)
		{
			/* {@squirreljme.error JC4a Invalid stack map table at the
			specified address. (The address offset; Is the address offset
			absolute?; The placement address; The type of entry which
			was just handled, -1 means it was old-style or initial state.)} */
			throw new InvalidClassFormatException(String.format(
				"JC4a %d %b %d %d", __au, __abs, naddr, __type), e, this);
		}
		
		// Set new placement address, the first is always absolute
		int pp = (__abs ? __au :
			naddr + (__au + (__ne == 0 ? 0 : 1)));
		this._placeaddr = pp;
	
		/* {@squirreljme.error JC4b A duplicate stack map information for the
		specified address has already been loaded. (The address; The
		already existing information; The information to be placed there;
		Absolute address?; Current address of parse; The address offset;
		The parsed type)} */
		
		// Note that the first instruction if it is a jump target may have an
		// explicit state even if it one is always defined implicitly, so
		// just ignore it
		Map<Integer, StackMapTableState> targets = this._targets;
		if (pp != 0 && targets.containsKey(pp))
			throw new IllegalStateException(String.format(
				"JC4b %d %s %s %b %d %d %d",
				pp, targets.get(pp), rv, __abs, naddr, __au, __type));
		targets.put(pp, rv);
		
		// Debug
		/*Debugging.debugNote("Read state @%d: %s%n", pp, rv);*/
	
		// The stored state
		return rv;
	}
	
	/**
	 * Reads in an old style full frame.
	 *
	 * @return The address information.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __oldStyle()
		throws IOException
	{
		// Get the atom to use
		DataInputStream in = this.in;
		int rv = in.readUnsignedShort();
		
		// Read in local variables
		int nl = in.readUnsignedShort();
		StackMapTableEntry[] inlocals = new StackMapTableEntry[nl];
		for (int i = 0; i < nl; i++)
			inlocals[i] = this.__loadInfo();
		
		// Read in stack variables
		int ns = in.readUnsignedShort();
		StackMapTableEntry[] instack = new StackMapTableEntry[ns];
		for (int i = 0; i < ns; i++)
			instack[i] = this.__loadInfo();
		
		// Assign read local variables
		int lat = 0;
		StackMapTableEntry[] nextlocals = this._nextlocals;
		for (int i = 0; i < nl; i++)
		{
			// Copy in
			StackMapTableEntry e = inlocals[i];
			nextlocals[lat++] = e;
			
			// Handling wide type?
			if (e.isWide())
			{
				// Set top
				nextlocals[lat++] = e.topType();
				
				// If the top is explicit, then skip it
				if (i + 1 < nl && inlocals[i + 1].isTop())
					i++;
			}
		}
		
		// Assign read stack variables
		int sat = 0;
		StackMapTableEntry[] nextstack = this._nextstack;
		for (int i = 0; i < ns; i++)
		{
			// Copy in
			StackMapTableEntry e = instack[i];
			nextstack[sat++] = e;
			
			// Handling wide type?
			if (e.isWide())
			{
				// Set top
				nextstack[sat++] = e.topType();
				
				// If the top is explicit, then skip it
				if (i + 1 < ns && instack[i + 1].isTop())
					i++;
			}
		}
		
		// Stack depth is where the next stack would have been placed
		this._stacktop = sat;
		
		return rv;
	}
	
	/**
	 * The same frame is used with no changes.
	 *
	 * @param __delta The offset from the earlier offset.
	 * @return The address information.
	 * @since 2016/03/26
	 */
	private int __sameFrame(int __delta)
	{
		return __delta;
	}
	
	/**
	 * Same frame but with a supplied delta rather than using it with the type.
	 *
	 * @return The address information.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __sameFrameDelta()
		throws IOException
	{
		return this.in.readUnsignedShort();
	}
	
	/**
	 * Same locals but the stack has only a single entry.
	 *
	 * @param __delta The delta offset.
	 * @return The address information.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __sameLocalsSingleStack(int __delta)
		throws IOException
	{
		// Load single entry
		StackMapTableEntry ent;
		this._nextstack[0] = (ent = this.__loadInfo());
		
		// If the entry is wide then the top type will not be specified as it
		// will be implicit, so we need to set the according type
		if (ent.isWide())
		{
			this._nextstack[1] = ent.topType();
			this._stacktop = 2;
		}
		
		// Only a single entry exists
		else
			this._stacktop = 1;
		
		return __delta;
	}
	
	/**
	 * Same locals but the stack has only a single entry, the delta offset
	 * is specified.
	 *
	 * @return The address information.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private int __sameLocalsSingleStackExplicit()
		throws IOException
	{
		return this.__sameLocalsSingleStack(this.in.readUnsignedShort());
	}
}

