// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.link.FieldSymbol;
import net.multiphasicapps.squirreljme.jit.link.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.link.ClassExport;
import net.multiphasicapps.squirreljme.jit.link.MethodFlags;

/**
 * This class is used to parse the stack map and initialize the initial
 * snapshot states for jump targets within the method.
 *
 * @since 2017/04/16
 */
class __StackMapParser__
{
	/** The stream to decode from. */
	protected final DataInputStream in;
	
	/** The number of stack entries. */
	protected final int maxstack;
	
	/** The number of local entries. */
	protected final int maxlocals;
	
	/** The method byte code. */
	protected final ByteCode code;
	
	/** Verification targets. */
	private final Map<Integer, BasicVerificationTarget> _targets;
	
	/** The next stack state. */
	private final JavaType[] _nextstack;
	
	/** The next local variable state. */
	private final JavaType[] _nextlocals;
	
	/** The placement address. */
	private volatile int _placeaddr;
	
	/** The top of the stack. */
	private volatile int _stacktop;
	
	/**
	 * Initializes the stack map parser.
	 *
	 * @param __c The owning code parser.
	 * @param __modern Does this represent the modern stack map?
	 * @param __in The stack map table information,
	 * @param __m The current method being exported.
	 * @param __ms The maximum number of stack entries.
	 * @param __ml The maximum number of local entries.
	 * @throws JITException If the table is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	__StackMapParser__(ByteCode __code, byte[] __smtdata, boolean __smtmodern,
		ExportedMethod __m)
		throws JITException, NullPointerException
	{
		// Check
		if (__code == null || __m == null || __smtdata == null)
			throw new NullPointerException("NARG");
		
		// Set
		DataInputStream xin;
		this.in = (xin = new DataInputStream(
			new ByteArrayInputStream(__smtdata)));
		int maxstack = __code.maxStack(),
			maxlocals = __code.maxLocals();
		this.maxstack = maxstack;
		this.maxlocals = maxlocals;
		this.code = __code;
		
		// This is used to set which variables appear next before a state is
		// constructed with them
		JavaType[] nextstack, nextlocals;
		this._nextstack = (nextstack = new JavaType[maxstack]);
		this._nextlocals = (nextlocals = new JavaType[maxlocals]);
		
		// Non-static methods always have an implicit instance argument
		int at = 0;
		if (!__m.methodFlags().isStatic())
			nextlocals[at] = JavaType.of(__m.exportedClass().name().asField());
		
		// Handle each argument
		for (FieldSymbol f : __m.methodType().arguments())
		{
			// Map type
			JavaType j;
			nextlocals[at] = (j = JavaType.of(f));
			
			// Skip space for wide
			if (j.isWide())
				nextlocals[at++] = JavaType.TOP;
		}
		
		// Where states go
		Map<Integer, BasicVerificationTarget> targets = new LinkedHashMap<>();
		this._targets = targets;
		
		// Record state
		__next(0, true);
		
		// Parse the stack map table
		try (DataInputStream in = xin)
		{
			// Parsing the class stack map table
			if (!__smtmodern)
			{
				// Read the number of entries in the table
				int ne = xin.readUnsignedShort();
			
				// All entries in the table are full frames
				for (int i = 0; i < ne; i++)
					__next(__oldStyle(), true);
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
						addr = __fullFrame();
				
					// Same frame?
					else if (type >= 0 && type <= 63)
						addr = __sameFrame(type);
				
					// Same locals but a single stack item
					else if (type >= 64 && type <= 127)
						addr = __sameLocalsSingleStack(type - 64);
				
					// Same locals, single stack item, explicit delta
					else if (type == 247)
						addr = __sameLocalsSingleStackExplicit();
				
					// Chopped frame
					else if (type >= 248 && type <= 250)
						addr = __choppedFrame(251 - type);
				
					// Same frame but with a supplied delta
					else if (type == 251)
						addr = __sameFrameDelta();
				
					// Appended frame
					else if (type >= 252 && type <= 254)
						addr = __appendFrame(type - 251);
				
					// {@squirreljme.error AQ1j Unknown StackMapTable
					// verification type. (The verification type)}
					else
						throw new JITException(String.format("AQ1j %d", type));
					
					// Setup next
					__next(addr, true);
				}
			}
		}
		
		// {@squirreljme.error AQ1b Failed to parse the stack map table.}
		catch (IOException e)
		{
			throw new JITException("AQ1b", e);
		}
	}
	
	/**
	 * Returns the basic verification state for the specified address.
	 *
	 * @param __a The address to get.
	 * @return The verification target for the specified address or
	 * {@code null} if none is available.
	 * @since 2017/05/20
	 */
	public BasicVerificationTarget get(int __a)
	{
		return this._targets.get(__a);
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
		JavaType[] nextlocals = this._nextlocals;
		int n = this.maxlocals;
		for (int i = 0; __addlocs > 0 && i < n; i++)
		{
			// Get slot here
			JavaType s = nextlocals[i];
			
			// If it is not empty, ignore it
			if (!s.equals(JavaType.NOTHING))
				continue;
			
			// Set it
			JavaType aa;
			nextlocals[i] = (aa = __loadInfo());
			__addlocs--;
			
			// If a wide element was added, then the next one becomes TOP
			if (aa.isWide())
				nextlocals[++i] = JavaType.TOP;
		}
		
		// Error if added stuff remains
		// {@squirreljme.error AQ1m Appending local variables to the frame
		// however there is no room to place them. (The remaining local count)}
		if (__addlocs != 0)
			throw new JITException(String.format("AQ1m %d", __addlocs));
		
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
		JavaType[] nextlocals = this._nextlocals;
		int i, n = this.maxlocals;
		for (i = n - 1; __chops > 0 && i >= 0; i--)
		{
			// Get slot here
			JavaType s = nextlocals[i];
			
			// If it is empty, ignore it
			if (s.equals(JavaType.NOTHING))
				continue;
			
			// Clear it
			nextlocals[i] = JavaType.NOTHING;
			__chops--;
		}
		
		// Still chops left?
		// {@squirreljme.error AQ1l Could not chop off all local variables
		// because there are no variables remaining to be chopped. (The
		// remaining variables to remove)}
		if (__chops != 0)
			throw new JITException(String.format("AQ1l %d", __chops));
		
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
		
		// {@squirreljme.error AQ1k The number of specified local variables in
		// the full frame exceeds the maximum permitted local variable
		// count. (The read local variable count; The number of locals the
		// method uses)}
		int maxlocals = this.maxlocals,
			maxstack = this.maxstack;
		if (nl > maxlocals)
			throw new JITException(String.format("AQ1k %d %d", nl,
				maxlocals));
		int i;
		JavaType[] nextlocals = this._nextlocals;
		for (i = 0; i < nl; i++)
			nextlocals[i] = __loadInfo();
		for (;i < maxlocals; i++)
			nextlocals[i] = JavaType.NOTHING;
		
		// Read in stack variables
		JavaType[] nextstack = this._nextstack;
		int ns = in.readUnsignedShort();
		for (i = 0; i < ns; i++)
			nextstack[i] = __loadInfo();
		this._stacktop = ns;
		
		return rv;
	}
	
	/**
	 * Loads type information for the stack.
	 *
	 * @return The type which was parsed.
	 * @throws IOException On read errors.
	 * @since 2016/03/26
	 */
	private JavaType __loadInfo()
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
				
				// Uninitialized this
			case 6:
				throw new todo.TODO();
				
				// Initialized object
			case 7:
				int id = in.readUnsignedShort();
				throw new todo.TODO();
				
				// Uninitialized variable for a new instruction, the pc points
				// to the new instruction so the class must be read from
				// that instruction to determine the type of that actual
				// object
			case 8:
				int pc = in.readUnsignedShort();
				throw new todo.TODO();
				
				// Unknown
			default:
				// {@squirreljme.error AQ1i The verification tag in the
				// StackMap/StackMapTable attribute is not valid. (The tag)}
				throw new JITException(String.format("AQ1i %d", tag));
		}
	}
	
	/**
	 * Initializes the next state.
	 *
	 * @param __au The address offset.
	 * @param __abs Absolute position?
	 * @return The state for the next address.
	 * @since 2016/05/20
	 */
	BasicVerificationTarget __next(int __au, boolean __abs)
	{
		// Generate it
		BasicVerificationTarget rv = new BasicVerificationTarget(
			this._nextstack, this._stacktop, this._nextlocals);
		
		// Set new placement address
		int naddr = this._placeaddr;
		int pp = (__abs ? __au :
			naddr + (__au + (naddr == 0 ? 0 : 1)));
		this._placeaddr = pp;
	
		// Set the state
		this._targets.put(pp, rv);
		
		// Debug
		System.err.printf("DEBUG -- Read state @%d: %s%n", pp, rv);
	
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
		
		// {@squirreljme.error AQ1d Old-style full frame specified more local
		// variables than there are in the method. (The number of locals; The
		// maximum number of locals)}
		int maxlocals = this.maxlocals;
		if (nl > maxlocals)
			throw new JITException(String.format("AQ1d %d %d", nl,
				maxlocals));
		JavaType[] nextlocals = this._nextlocals;
		int i = 0;
		for (i = 0; i < nl; i++)
			nextlocals[i] = __loadInfo();
		for (;i < maxlocals; i++)
			nextlocals[i] = JavaType.NOTHING;
		
		// Read in stack variables
		JavaType[] nextstack = this._nextstack;
		int ns = in.readUnsignedShort();
		for (i = 0; i < ns; i++)
			nextstack[i] = __loadInfo();
		this._stacktop = ns;
		
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
		// Set the single stack
		this._stacktop = 1;
		this._nextstack[0] = __loadInfo();
		
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
		return __sameLocalsSingleStack(this.in.readUnsignedShort());
	}
}

