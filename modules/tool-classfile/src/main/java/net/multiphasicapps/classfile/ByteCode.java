// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.debug.ErrorCode;
import cc.squirreljme.runtime.cldc.util.IntegerArrayList;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This class represents the byte code within a method.
 *
 * @since 2017/10/09
 */
public final class ByteCode
	implements Contexual, Iterable<Instruction>
{
	/** The code is always at this offset. */
	public static final int CODE_OFFSET =
		8;
	
	/** The maximum number of bytes the byte code may be. */
	private static final int _MAX_CODE_LENGTH =
		65535;
	
	/** The maximum number of stack entries. */
	protected final int maxstack;
	
	/** The maximum number of local entries. */
	protected final int maxlocals;
	
	/** The length of the method code in bytes. */
	protected final int codelen;
	
	/** The constant pool. */
	protected final Pool pool;
	
	/** The exceptions within this method. */
	protected final ExceptionHandlerTable exceptions;
	
	/** This type. */
	protected final ClassName thistype;
	
	/** The name of this method. */
	protected final MethodName methodname;
	
	/** Method type. */
	protected final MethodDescriptor methodtype;
	
	/** Is this method synchronized? */
	protected final boolean issynchronized;
	
	/** Is this an instance method? */
	protected final boolean isinstance;
	
	/** Local variable table. */
	protected final LocalVariableTable localVariables;
	
	/** The input attribute code, used for instruction lookup. */
	private final byte[] _rawByteCode;
	
	/** The stack map table data. */
	private final byte[] _smtdata;
	
	/** Is the stack map table data new? */
	private final boolean _newsmtdata;
	
	/** The owning method reference. */
	private final Reference<Method> _methodref;
	
	/** Instruction lengths at each position. */
	private final int[] _lengths;
	
	/** The addresses of every instruction by index. */
	private final int[] _index;
	
	/** The line number table. */
	private final short[] _linenumbertable;
	
	/** The cache of instructions in the byte code. */
	private final Reference<Instruction>[] _icache;
	
	/** String representation of this byte code */
	private Reference<String> _string;
	
	/** The stack map table cache. */
	private Reference<StackMapTable> _smt;
	
	/** Stack map at runtime. */
	private Reference<StackMapTablePairs> _stackMapRunTime;
	
	/**
	 * Initializes the byte code.
	 *
	 * @param __mr The owning method reference.
	 * @param __ca The raw code attribute data.
	 * @param __tt The current {@code this} type.
	 * @param __mf Method flags.
	 * @throws InvalidClassFormatException If the byte code is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/09
	 */
	ByteCode(Reference<Method> __mr, byte[] __ca, ClassName __tt,
		MethodFlags __mf)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__mr == null || __ca == null || __tt == null || __mf == null)
			throw new NullPointerException("NARG");
		
		// Needed
		Method method = __mr.get();
		
		// Set
		this._methodref = __mr;
		this._rawByteCode = __ca;
		this.issynchronized = __mf.isSynchronized();
		this.isinstance = !__mf.isStatic();
		
		// Is this an initializer method?
		this.methodname = method.name();
		this.methodtype = method.type();
		
		// If any IOExceptions are generated then the attribute is not valid
		Pool pool = method.pool();
		try (DataInputStream in = new DataInputStream(
			new ByteArrayInputStream(__ca)))
		{
			// The number of variables allocated to the method
			int maxStack = in.readUnsignedShort();
			int maxLocals = in.readUnsignedShort();
				
			/* {@squirreljme.error JC1y The specified code length is not valid.
			(The code length)} */
			int codeLen = in.readInt();
			if (codeLen <= 0 || codeLen > ByteCode._MAX_CODE_LENGTH)
				throw new InvalidClassFormatException(
					String.format("JC1y %d", codeLen), this);
		
			// Ignore that many bytes
			for (int i = 0; i < codeLen; i++)
				in.readByte();
			
			// Read exception handler table
			ExceptionHandlerTable eht = ExceptionHandlerTable.decode(in, pool,
				codeLen);
			
			// The instruction index is used to lookup using a linear index
			// count rather than the potentially spaced out address lookup
			int[] index = new int[codeLen];
			int indexat = 0;
		
			// Set all lengths initially to invalid positions, this used as a
			// quick marker to determine which positions have valid
			// instructions
			int[] lengths = new int[codeLen];
			for (int i = 0; i < codeLen; i++)
				lengths[i] = -1;
		
			// Determine instruction lengths for each position
			for (int i = 0, li = -1; i < codeLen; li = i)
			{
				// Store address of instruction for an index based lookup
				index[indexat++] = i;
			
				// Store length
				int opLen = ByteCodeUtils.instructionLength(__ca,
					ByteCode.CODE_OFFSET, i, null);
				lengths[i] = opLen;
			
				/* {@squirreljme.error JC1z The operation exceeds the bounds of
				the method byte code. (The operation pointer; The operation
				length; The code length; The last operation pointer)} */
				if ((i += opLen) > codeLen)
					throw new InvalidClassFormatException(
						String.format("JC1z %d %d %d %d",
							i, opLen, codeLen, li), this);
			}
			
			// The stack map table is used for verification
			byte[] smt = null;
			boolean smtnew = false;
			
			// Parse the attribute table
			AttributeTable attrs = AttributeTable.parse(pool, in);
			
			// Try using the newer stack map table, if that does not exist
			// then fallback to the old one
			Attribute attr = attrs.get("StackMapTable");
			if (attr != null)
			{
				smt = attr.bytes();
				smtnew = true;
			}
			else
			{
				attr = attrs.get("StackMap");
				if (attr != null)
				{
					smt = attr.bytes();
					smtnew = false;
				}
			}
			
			// If there is no stack map, then use a default one (which has
			// just no entries) which has an assumed state
			if (smt == null)
			{
				smt = new byte[2];
				smtnew = true;
			}
			
			// Initialize a blank line number table
			short[] lnt = new short[codeLen];
			for (int i = 0; i < codeLen; i++)
				lnt[i] = -1;
			
			// Parse the line number table for debug purposes
			attr = attrs.get("LineNumberTable");
			if (attr != null)
				try (DataInputStream ai = attr.open())
				{
					// Read entry count
					int n = ai.readUnsignedShort();
					
					// Read all the individual entries
					for (int i = 0; i < n; i++)
					{
						int pc = ai.readUnsignedShort(),
							line = ai.readUnsignedShort();
						
						// Failed to read the program address, this could be
						// a failure but instead just ignore it and continue
						// on
						if (pc < 0 || pc >= codeLen)
							continue;
						
						// This gets handled later, but if there is more than
						// 65534 lines in a file then what is the programmer
						// even doing
						lnt[pc] = (short)line;
					}
				}
			
			// Parse local variables
			LocalVariableTable localVariables =
				LocalVariableTable.parse(pool, attrs);
			
			// Can set fields now
			this.maxstack = maxStack;
			this.maxlocals = maxLocals;
			this.codelen = codeLen;
			this.exceptions = eht;
			this.pool = pool;
			this.thistype = __tt;
			this._smtdata = smt;
			this._newsmtdata = smtnew;
			this._lengths = lengths;
			this._icache = ByteCode.__newCache(codeLen);
			this._linenumbertable = lnt;
			this.localVariables = localVariables;
			
			// Store addresses for all the indexes
			if (indexat == codeLen)
				this._index = index;
			else
				this._index = Arrays.copyOf(index, indexat);
		}
		
		/* {@squirreljme.error JC20 Failed to read from the code attribute.} */
		catch (IOException e)
		{
			throw new InvalidClassFormatException("JC20", e, this);
		}
	}
	
	/**
	 * Returns the address of the instruction following the specified one.
	 *
	 * @param __a The following address.
	 * @return The instruction address following the instruction at the
	 * specified address, if the next address is at the end then this will
	 * return an address after the last operation.
	 * @throws InvalidClassFormatException If the specified address
	 * is not valid.
	 * @since 2017/05/20
	 */
	public int addressFollowing(int __a)
		throws InvalidClassFormatException
	{
		/* {@squirreljme.error JC21 The instruction at the specified address is
		not valid. (The address)} */
		if (!this.isValidAddress(__a))
			throw new InvalidClassFormatException(
				String.format("JC21 %d", __a), this);
		
		int result = __a + this._lengths[__a];
		if (result >= this._lengths.length)
			return this._lengths.length;
		
		return result;
	}
	
	/**
	 * Translates an address to an index.
	 *
	 * @param __a The address to translate.
	 * @return The index of the instruction or {@code -1} if it is not valid,
	 * if the address is the byte code length then the number of indexes is
	 * returned.
	 * @since 2017/08/02
	 */
	public int addressToIndex(int __a)
	{
		// Byte right at the end converts to the last index
		int[] index = this._index;
		if (__a == this.codelen)
			return index.length;
		
		// Not the end
		int rv = Arrays.binarySearch(index, __a);
		if (rv < 0)
			return -1;
		return rv;
	}
	
	/**
	 * Returns the exception handler table.
	 *
	 * @return The exception handler table.
	 * @since 2018/10/13
	 */
	public final ExceptionHandlerTable exceptions()
	{
		return this.exceptions;
	}
	
	/**
	 * Returns the instruction at the specified address.
	 *
	 * @param __a The address to get the instruction for.
	 * @return The represented instruction for the given address.
	 * @throws InvalidClassFormatException If the address is not valid.
	 * @since 2017/05/18
	 */
	public Instruction getByAddress(int __a)
		throws InvalidClassFormatException
	{
		/* {@squirreljme.error JC22 The instruction at the specified address is
		not valid. (The address)} */
		if (!this.isValidAddress(__a))
		{
			Reference<Instruction>[] iCache = this._icache;
			int numCache = iCache.length;
			Instruction[] cache = new Instruction[numCache];
			for (int i = 0; i < numCache; i++)
				if (i == __a || iCache[i] != null ||
					!this.isValidAddress(i))
					cache[i] = (iCache[i] != null ? iCache[i].get() : null);
				else
					try
					{
						cache[i] = this.getByAddress(i);
					}
					catch (Throwable __ignored)
					{
						// Ignored
					}
			
			throw new InvalidClassFormatException(
				ErrorCode.__error__("JC22", __a,
					this.__method().inClass(),
					this.methodname, this.methodtype,
					new IntegerArrayList(this._lengths),
					new IntegerArrayList(this._index),
					Arrays.asList(cache)), this);
		}
		
		Reference<Instruction>[] icache = this._icache;
		Reference<Instruction> ref = icache[__a];
		Instruction rv;
		
		if (ref == null || null == (rv = ref.get()))
			icache[__a] = new SoftReference<>((rv = new Instruction(
				this._rawByteCode, this.pool, __a, this.exceptions,
				this.stackMapTable(), this.addressFollowing(__a),
				this.addressToIndex(__a))));
		
		return rv;
	}
	
	/**
	 * Returns the instruction based on an index in the order it appears within
	 * the byte code rather than by address.
	 *
	 * @param __i The instruction index to get.
	 * @return The instruction at this index.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws InvalidClassFormatException If the instruction is not valid.
	 * @since 2017/08/01
	 */
	public Instruction getByIndex(int __i)
		throws IndexOutOfBoundsException
	{
		// Check
		int[] index = this._index;
		if (__i < 0 || __i >= index.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		return this.getByAddress(index[__i]);
	}
	
	/**
	 * Translates an index to an address.
	 *
	 * @param __i The index to translate.
	 * @return The address of the index or {@code -1} if it is not valid, if
	 * the index is the number of indexes then the length of the byte code in
	 * addresses is returned.
	 * @since 2017/08/02
	 */
	public int indexToAddress(int __i)
	{
		// Last index translates to the length of the byte code
		int[] index = this._index;
		int n = index.length;
		if (__i == n)
			return this.codelen;
		
		// Normal position
		if (__i < 0 || __i >= n)
			return -1;
		return index[__i];
	}
	
	/**
	 * Returns the number of instructions (valid addresses) which are within
	 * this method. This will not match the byte code length unless all
	 * byte codes were to be single-byte in length.
	 *
	 * @return The number of instructions which are in the method.
	 * @since 2017/08/01
	 */
	public int instructionCount()
	{
		return this._index.length;
	}
	
	/**
	 * This returns an iterator over the instructions which are defined within
	 * this method.
	 *
	 * @return The iterator over byte code instructions.
	 * @since 2017/05/20
	 */
	public Iterator<Instruction> instructionIterator()
	{
		return new __InstructionIterator__();
	}
	
	/**
	 * Returns if this is an instance or not.
	 *
	 * @return If this is an instance.
	 * @since 2019/04/26
	 */
	public final boolean isInstance()
	{
		return this.isinstance;
	}
	
	/**
	 * Returns whether this is a constructor or not.
	 *
	 * @return Whether this is a constructor or not.
	 * @since 2019/03/24
	 */
	public final boolean isInstanceInitializer()
	{
		return this.methodname.isInstanceInitializer();
	}
	
	/**
	 * Returns whether this is a static initializer or not.
	 *
	 * @return Whether this is a static initializer or not.
	 * @since 2019/03/24
	 */
	public final boolean isStaticInitializer()
	{
		return this.methodname.isStaticInitializer();
	}
	
	/**
	 * Is this method synchronized?
	 *
	 * @return If this is synchronized.
	 * @since 2019/04/26
	 */
	public final boolean isSynchronized()
	{
		return this.issynchronized;
	}
	
	/**
	 * Checks whether the given address is a valid instruction address.
	 *
	 * @param __a The address to check.
	 * @return {@code true} if the address is valid.
	 * @since 2017/05/18
	 */
	public boolean isValidAddress(int __a)
	{
		// Out of range
		if (__a < 0 || __a >= this.codelen)
			return false;
		
		// Has to have a positive non-zero length
		return (this._lengths[__a] > 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/17
	 */
	@Override
	public final Iterator<Instruction> iterator()
	{
		return this.instructionIterator();
	}
	
	/**
	 * Returns the jump targets for this byte code.
	 *
	 * @return The jump targets.
	 * @since 2019/03/30
	 */
	public final Map<Integer, InstructionJumpTargets> jumpTargets()
	{
		Map<Integer, InstructionJumpTargets> rv = new LinkedHashMap<>();
		
		// Just fill addresses with instruction info
		for (Instruction i : this)
			rv.put(i.address(), i.jumpTargets());
		
		return rv;
	}
	
	/**
	 * Returns the length of the byte code.
	 *
	 * @return The byte code length.
	 * @since 2017/05/20
	 */
	public int length()
	{
		return this.codelen;
	}
	
	/**
	 * Returns the line that the address is on, assuming the address is valid
	 * and there is line number information available.
	 *
	 * @param __a The address to lookup.
	 * @return The line of the address or a negative value if it is not valid
	 * or it is unknown.
	 * @since 2018/09/08
	 */
	public final int lineOfAddress(int __a)
	{
		// Scan through the table and try to find the line number for the given
		// address, the table is negative padded for unknown locations
		int codelen = this.codelen;
		short[] linenumbertable = this._linenumbertable;
		int negscandx = __a;
		for (int pc = __a; pc >= 0 && pc < codelen; pc--)
		{
			// Do not use this value if the line is not valid, scan backwards
			short clip = linenumbertable[pc];
			if (clip == -1)
				continue;
			
			// If the address we started at is not valid then a bunch of source
			// code is probably on the same line for a bunch of area so to
			// prevent any extra backscanning when this information is needed
			// cache it back into the table so it is used
			// Fill the entire table to our PC address so that way the entire
			// section is filled
			while (negscandx > pc)
				linenumbertable[negscandx--] = clip;
			
			// Sign extends to int, then removes the negative part so we can
			// recover the full 0-65534 range of the line table
			return (clip & 0xFFFF);
		}
		
		// Not known
		return -1;
	}
	
	/**
	 * Returns the local variable table.
	 *
	 * @return The local variable table.
	 * @since 2022/09/21
	 */
	public LocalVariableTable localVariables()
	{
		return this.localVariables;
	}
	
	/**
	 * Returns the maximum number of locals.
	 *
	 * @return The maximum number of locals.
	 * @since 2017/05/20
	 */
	public int maxLocals()
	{
		return this.maxlocals;
	}
	
	/**
	 * Returns the maximum size of the stack.
	 *
	 * @return The maximum stack size.
	 * @since 2017/05/20
	 */
	public int maxStack()
	{
		return this.maxstack;
	}
	
	/**
	 * Returns the name of this method.
	 *
	 * @return The method name.
	 * @since 2019/04/22
	 */
	public final MethodName name()
	{
		return this.methodname;
	}
	
	/**
	 * Returns the constant pool being used.
	 *
	 * @return The constant pool the method uses.
	 * @since 2017/05/20
	 */
	public Pool pool()
	{
		return this.pool;
	}
	
	/**
	 * Returns the raw byte code array.
	 * 
	 * @return The byte code as an array.
	 * @since 2021/03/21
	 */
	public byte[] rawByteCode()
	{
		byte[] rawCode = this._rawByteCode;
		int rawLen = rawCode.length;
		
		// It is the actual code attribute, so it needs to be stripped
		int len = Math.min(rawLen - ByteCode.CODE_OFFSET,
			this.codelen);
		byte[] result = new byte[len];
		System.arraycopy(rawCode, ByteCode.CODE_OFFSET,
			result, 0, len);
		
		return result;
	}
	
	/**
	 * Reads an unsigned short from the raw byte code at the given address.
	 *
	 * @param __addr The address to read from.
	 * @return The read unsigned short.
	 * @throws IndexOutOfBoundsException If the address is out of bounds.
	 * @since 2018/09/28
	 */
	public final int readRawCodeUnsignedShort(int __addr)
		throws IndexOutOfBoundsException
	{
		/* {@squirreljme.error JC23 Out of bounds read of unsigned short from
		raw byte code. (The address)} */
		if (__addr < 0 || __addr >= this.codelen - 1)
			throw new IndexOutOfBoundsException(
				String.format("JC23 %d", __addr));
		
		byte[] rad = this._rawByteCode;
		int d = __addr + ByteCode.CODE_OFFSET;
		return ((rad[d] & 0xFF) << 8) |
			(rad[d + 1] & 0xFF);
	}
	
	/**
	 * Returns the reverse jump targets, this essentially specifies the
	 * instructions and exception handlers at given points jump to the
	 * key addresses.
	 *
	 * @return The reverse jump targets.
	 * @since 2019/03/30
	 */
	public final Map<Integer, InstructionJumpTargets> reverseJumpTargets()
	{
		// Get the original jump table
		Map<Integer, InstructionJumpTargets> jumpmap = this.jumpTargets();
		
		// The target jump table has both normals and exceptions, so it must
		// remember that state accordingly
		class Working
		{
			Set<InstructionJumpTarget> normal =
				new LinkedHashSet<>();
			
			Set<InstructionJumpTarget> exception =
				new LinkedHashSet<>();
		}
		Map<Integer, Working> works = new LinkedHashMap<>();
		
		// Go through all the original jump targets and add them
		for (Map.Entry<Integer, InstructionJumpTargets> e : jumpmap.entrySet())
		{
			InstructionJumpTarget addr = new InstructionJumpTarget(e.getKey());
			InstructionJumpTargets jumps = e.getValue();
			
			for (int i = 0, n = jumps.size(); i < n; i++)
			{
				int targ = jumps.get(i).target();
				boolean isex = jumps.isException(i);
				
				// Create work if missing
				Working work = works.get(targ);
				if (work == null)
					works.put(targ, (work = new Working()));
				
				// Add
				if (isex)
					work.exception.add(addr);
				else
					work.normal.add(addr);
			}
		}
		
		// Finalize for returning
		Map<Integer, InstructionJumpTargets> rv = new LinkedHashMap<>();
		for (Map.Entry<Integer, Working> e : works.entrySet())
		{
			Working work = e.getValue();
			Set<InstructionJumpTarget> nrm = work.normal,
				exe = work.exception;
			
			// Convert
			rv.put(e.getKey(), new InstructionJumpTargets(
				nrm.<InstructionJumpTarget>toArray(
					new InstructionJumpTarget[nrm.size()]),
				exe.<InstructionJumpTarget>toArray(
					new InstructionJumpTarget[exe.size()])));
		}
		
		return rv;
	}
	
	/**
	 * Returns the stack map table.
	 *
	 * @return The stack map table.
	 * @since 2017/10/15
	 */
	public StackMapTable stackMapTable()
	{
		Reference<StackMapTable> ref = this._smt;
		StackMapTable rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._smt = new SoftReference<>(rv = new __StackMapParser__(
				this.pool, this.__method(), this._newsmtdata, this._smtdata,
				this, new JavaType(this.thistype)).get());
		
		return rv;
	}
	
	/**
	 * Returns a fully filled in {@link StackMapTable} for each instruction
	 * as it would occur at runtime.
	 * 
	 * @return The stack map table at runtime.
	 * @since 2023/07/03
	 */
	public StackMapTablePairs stackMapTableFull()
	{
		Reference<StackMapTablePairs> ref = this._stackMapRunTime;
		StackMapTablePairs rv;
		
		if (ref == null || (rv = ref.get()) == null)
			try
			{
				rv = this.__calcStackMapTableFull();
				this._stackMapRunTime = new WeakReference<>(rv);
			}
			catch (InvalidClassFormatException __e)
			{
				/* {@squirreljme.error JC9a Could not calculate the full stack
				map in method. (The class; The method)} */
				Method method = this.__method();
				throw new InvalidClassFormatException(
					String.format("JC9a %s %s",
						method.inClass(),
						method.nameAndType()), __e, this);
			}
		
		return rv;
	}
	
	/**
	 * Returns the name of the current class.
	 *
	 * @return The current class name.
	 * @since 2019/03/24
	 */
	public final ClassName thisType()
	{
		return this.thistype;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder("[");
			
			// Fill in instructions
			boolean comma = false;
			for (Iterator<Instruction> it = this.instructionIterator();
				 it.hasNext();)
			{
				if (comma)
					sb.append(", ");
				else
					comma = true;
				
				sb.append(it.next());
			}
			
			sb.append(']');
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
	
	/**
	 * Returns the method type.
	 *
	 * @return The method type.
	 * @since 2019/04/22
	 */
	public final MethodDescriptor type()
	{
		return this.methodtype;
	}
	
	/**
	 * Returns all of the valid addresses within this code.
	 * 
	 * @return The list of valid addresses.
	 * @since 2021/03/14
	 */
	public final int[] validAddresses()
	{
		return this._index.clone();
	}
	
	/**
	 * Returns all the local variables which are written to.
	 *
	 * @return The local variables which are written to.
	 * @since 2019/03/30
	 */
	public final int[] writtenLocals()
	{
		Set<Integer> written = new LinkedHashSet<>();
		
		// Go through all instructions and count anything which is written to
		for (Instruction inst : this)
		{
			// Anything which is wide hits the adjacent local as well
			boolean wide = false;
			
			// Only specific instructions will do so
			int hit, op;
			switch ((op = inst.operation()))
			{
				case InstructionIndex.ASTORE:
				case InstructionIndex.WIDE_ASTORE:
				case InstructionIndex.FSTORE:
				case InstructionIndex.WIDE_FSTORE:
				case InstructionIndex.IINC:
				case InstructionIndex.WIDE_IINC:
				case InstructionIndex.ISTORE:
				case InstructionIndex.WIDE_ISTORE:
					hit = inst.intArgument(0);
					break;
				
				case InstructionIndex.ASTORE_0:
				case InstructionIndex.ASTORE_1:
				case InstructionIndex.ASTORE_2:
				case InstructionIndex.ASTORE_3:
					hit = op - InstructionIndex.ASTORE_0;
					break;
				
				case InstructionIndex.DSTORE:
				case InstructionIndex.WIDE_DSTORE:
				case InstructionIndex.LSTORE:
				case InstructionIndex.WIDE_LSTORE:
					hit = inst.intArgument(0);
					wide = true;
					break;
				
				case InstructionIndex.DSTORE_0:
				case InstructionIndex.DSTORE_1:
				case InstructionIndex.DSTORE_2:
				case InstructionIndex.DSTORE_3:
					hit = op - InstructionIndex.DSTORE_0;
					wide = true;
					break;
				
				case InstructionIndex.FSTORE_0:
				case InstructionIndex.FSTORE_1:
				case InstructionIndex.FSTORE_2:
				case InstructionIndex.FSTORE_3:
					hit = op - InstructionIndex.FSTORE_0;
					break;
				
				case InstructionIndex.ISTORE_0:
				case InstructionIndex.ISTORE_1:
				case InstructionIndex.ISTORE_2:
				case InstructionIndex.ISTORE_3:
					hit = op - InstructionIndex.ISTORE_0;
					break;
				
				case InstructionIndex.LSTORE_0:
				case InstructionIndex.LSTORE_1:
				case InstructionIndex.LSTORE_2:
				case InstructionIndex.LSTORE_3:
					hit = op - InstructionIndex.LSTORE_0;
					wide = true;
					break;
				
				default:
					continue;
			}
			
			// Set local as being written to, handle wides as well
			written.add(hit);
			if (wide)
				written.add(hit + 1);
		}
		
		// Convert to array
		Integer[] from = written.<Integer>toArray(new Integer[written.size()]);
		int n = from.length;
		int[] rv = new int[n];
		for (int i = 0; i < n; i++)
			rv[i] = from[i];
		return rv;
	}
	
	/**
	 * Calculates the full stack map.
	 * 
	 * @return The full stack map.
	 * @since 2023/07/16
	 */
	private StackMapTablePairs __calcStackMapTableFull()
	{
		// We need to get the base table
		StackMapTable base = this.stackMapTable();
		
		// Resultant tables, will get big!
		Map<Integer, StackMapTableState> inputs = new SortedTreeMap<>();
		Map<Integer, StackMapTableState> outputs = new SortedTreeMap<>();
		
		// Working pop set
		List<StackMapTableEntry> popped = new ArrayList<>();
		
		// Go through each instruction and build the mapping
		StackMapTableState current = null;
		for (int logicalAddr = 0, numAddrs = this.instructionCount();
			 logicalAddr < numAddrs; logicalAddr++)
		{
			int actualAddr = this.indexToAddress(logicalAddr);
			Instruction instruction = this.getByIndex(logicalAddr)
				.normalize();
			
			// Wipe popped state
			popped.clear();
			
			// Use pre-existing table? At entry point that is
			if (inputs.containsKey(actualAddr))
				current = inputs.get(actualAddr);
			else
			{
				StackMapTableState exist = instruction.stackMapTableState();
				if (exist != null)
				{
					current = exist;
					
					// Overwrite input with the one from the actual stack map
					// table
					inputs.put(actualAddr, current);
				}
			}
			
			// Debug
			/*Debugging.debugNote("I### %s: %s -> ...",
				instruction, current);*/
			
			// Should not occur
			StackMapTableState input = current;
			if (current == null)
				throw Debugging.oops();
			
			// At an input state currently, if not set already
			if (!inputs.containsKey(actualAddr))
				inputs.put(actualAddr, current);
			
			// Depends on the instruction what happens
			try
			{
				int op = instruction.op;
				switch (op)
				{
						// No changes to the stack
					case InstructionIndex.NOP:
					case InstructionIndex.GOTO:
					case InstructionIndex.GOTO_W:
					case InstructionIndex.WIDE_IINC:
					case InstructionIndex.RETURN:
						break;
						
						// Push null object
					case InstructionIndex.ACONST_NULL:
						current = current.deriveStackPush(
							StackMapTableEntry.INITIALIZED_OBJECT);
						break;
						
						// Load local variables
					case InstructionIndex.WIDE_ALOAD:
					case InstructionIndex.WIDE_ILOAD:
					case InstructionIndex.WIDE_FLOAD:
					case InstructionIndex.WIDE_LLOAD:
					case InstructionIndex.WIDE_DLOAD:
						current = current.deriveLocalLoad(
							instruction.intArgument(0));
						break;
					
						// Store local variable
					case InstructionIndex.WIDE_ASTORE:
					case InstructionIndex.WIDE_ISTORE:
					case InstructionIndex.WIDE_FSTORE:
					case InstructionIndex.WIDE_LSTORE:
					case InstructionIndex.WIDE_DSTORE:
						current = current.deriveLocalStore(
							instruction.intArgument(0));
						break;
						
						// Load constant value
					case InstructionIndex.LDC:
					case InstructionIndex.LDC_W:
					case InstructionIndex.LDC2_W:
						current = current.deriveStackPush(
							ByteCode.__deriveLdc(instruction));
						break;
						
						// Load from reference array
					case InstructionIndex.AALOAD:
						current = current.deriveStackPop(popped, 2);
						current = current.deriveStackPush(
							ByteCode.__deriveComponentType(popped.get(0)));
						break;
						
						// Load from array, note that the input could be
						// Object, so we have to assume it is the right array
					case InstructionIndex.IALOAD:
					case InstructionIndex.LALOAD:
					case InstructionIndex.FALOAD:
					case InstructionIndex.DALOAD:
					case InstructionIndex.BALOAD:
					case InstructionIndex.CALOAD:
					case InstructionIndex.SALOAD:
						current = current.deriveStackPop(popped, 2);
						current = current.deriveStackPush(
							ByteCode.__deriveComponentTypeViaOp(op));
						break;
						
						// Store into array
					case InstructionIndex.IASTORE:
					case InstructionIndex.LASTORE:
					case InstructionIndex.FASTORE:
					case InstructionIndex.DASTORE:
					case InstructionIndex.AASTORE:
					case InstructionIndex.BASTORE:
					case InstructionIndex.CASTORE:
					case InstructionIndex.SASTORE:
						current = current.deriveStackPop(null,
							3);
						break;
						
						// Push/pop operations
					case InstructionIndex.POP:
					case InstructionIndex.POP2:
					case InstructionIndex.DUP:
					case InstructionIndex.DUP_X1:
					case InstructionIndex.DUP_X2:
					case InstructionIndex.DUP2:
					case InstructionIndex.DUP2_X1:
					case InstructionIndex.DUP2_X2:
					case InstructionIndex.SWAP:
						current = current.deriveStackShuffle(
							JavaStackShuffleType.ofOperation(op));
						break;
						
						// Pop two, push first type
					case InstructionIndex.IADD:
					case InstructionIndex.LADD:
					case InstructionIndex.FADD:
					case InstructionIndex.DADD:
					case InstructionIndex.ISUB:
					case InstructionIndex.LSUB:
					case InstructionIndex.FSUB:
					case InstructionIndex.DSUB:
					case InstructionIndex.IMUL:
					case InstructionIndex.LMUL:
					case InstructionIndex.FMUL:
					case InstructionIndex.DMUL:
					case InstructionIndex.IDIV:
					case InstructionIndex.LDIV:
					case InstructionIndex.FDIV:
					case InstructionIndex.DDIV:
					case InstructionIndex.IREM:
					case InstructionIndex.LREM:
					case InstructionIndex.FREM:
					case InstructionIndex.DREM:
					case InstructionIndex.IAND:
					case InstructionIndex.LAND:
					case InstructionIndex.IOR:
					case InstructionIndex.LOR:
					case InstructionIndex.IXOR:
					case InstructionIndex.LXOR:
					case InstructionIndex.ISHL:
					case InstructionIndex.LSHL:
					case InstructionIndex.ISHR:
					case InstructionIndex.LSHR:
					case InstructionIndex.IUSHR:
					case InstructionIndex.LUSHR:
						current = current.deriveStackPop(popped, 2);
						current = current.deriveStackPush(popped.get(0));
						break;
						
						// Pop then push same type
					case InstructionIndex.INEG:
					case InstructionIndex.LNEG:
					case InstructionIndex.FNEG:
					case InstructionIndex.DNEG:
					case InstructionIndex.CHECKCAST:
						current = current.deriveStackPop(popped, 1);
						current = current.deriveStackPush(popped.get(0));
						break;
						
						// Pop one then push integer
					case InstructionIndex.INSTANCEOF:
					case InstructionIndex.L2I:
					case InstructionIndex.F2I:
					case InstructionIndex.D2I:
					case InstructionIndex.I2B:
					case InstructionIndex.I2C:
					case InstructionIndex.I2S:
						current = current.deriveStackPop(popped, 1);
						current = current.deriveStackPush(
							StackMapTableEntry.INTEGER);
						break;
						
						// Pop two then push integer
					case InstructionIndex.LCMP:
					case InstructionIndex.FCMPL:
					case InstructionIndex.FCMPG:
					case InstructionIndex.DCMPL:
					case InstructionIndex.DCMPG:
						current = current.deriveStackPop(popped, 2);
						current = current.deriveStackPush(
							StackMapTableEntry.INTEGER);
						break;
					
					// Pop single value, push nothing
					case InstructionIndex.IFEQ:
					case InstructionIndex.IFNE:
					case InstructionIndex.IFLT:
					case InstructionIndex.IFGE:
					case InstructionIndex.IFGT:
					case InstructionIndex.IFLE:
					case InstructionIndex.TABLESWITCH:
					case InstructionIndex.LOOKUPSWITCH:
					case InstructionIndex.ATHROW:
					case InstructionIndex.IRETURN:
					case InstructionIndex.LRETURN:
					case InstructionIndex.FRETURN:
					case InstructionIndex.DRETURN:
					case InstructionIndex.ARETURN:
					case InstructionIndex.MONITORENTER:
					case InstructionIndex.MONITOREXIT:
					case InstructionIndex.IFNULL:
					case InstructionIndex.IFNONNULL:
					case InstructionIndex.PUTSTATIC:
						current = current.deriveStackPop(popped, 1);
						break;
						
						// Pop value, push integer
					case InstructionIndex.ARRAYLENGTH:
						current = current.deriveStackPopThenPush(popped,
							1, StackMapTableEntry.INTEGER);
						break;
						
						// Pop two values, push nothing
					case InstructionIndex.IF_ICMPEQ:
					case InstructionIndex.IF_ICMPNE:
					case InstructionIndex.IF_ICMPLT:
					case InstructionIndex.IF_ICMPGE:
					case InstructionIndex.IF_ICMPGT:
					case InstructionIndex.IF_ICMPLE:
					case InstructionIndex.IF_ACMPEQ:
					case InstructionIndex.IF_ACMPNE:
					case InstructionIndex.PUTFIELD:
						current = current.deriveStackPop(popped, 2);
						break;
						
						// Method invocation
					case InstructionIndex.INVOKEVIRTUAL:
					case InstructionIndex.INVOKESPECIAL:
					case InstructionIndex.INVOKESTATIC:
					case InstructionIndex.INVOKEINTERFACE:
						current = current.deriveMethodCall(
							op == InstructionIndex.INVOKESTATIC,
							instruction.argument(0,
								MethodReference.class));
						break;
						
						// Pop one then push long
					case InstructionIndex.I2L:
					case InstructionIndex.F2L:
					case InstructionIndex.D2L:
						current = current.deriveStackPop(popped, 1);
						current = current.deriveStackPush(
							StackMapTableEntry.LONG);
						break;
						
						// Pop one then push float
					case InstructionIndex.I2F:
					case InstructionIndex.L2F:
					case InstructionIndex.D2F:
						current = current.deriveStackPop(popped, 1);
						current = current.deriveStackPush(
							StackMapTableEntry.FLOAT);
						break;
						
						// Pop one then push double
					case InstructionIndex.I2D:
					case InstructionIndex.L2D:
					case InstructionIndex.F2D:
						current = current.deriveStackPop(popped, 1);
						current = current.deriveStackPush(
							StackMapTableEntry.DOUBLE);
						break;
						
						// Push new object of given type
					case InstructionIndex.NEW:
						current = current.deriveStackPush(
							instruction.argument(0, ClassName.class)
								.field());
						break;
						
						// Pop one, push specified array
					case InstructionIndex.ANEWARRAY:
						current = current.deriveStackPop(null,
							1);
						current = current.deriveStackPush(
							instruction.argument(0, ClassName.class)
								.field().addDimensions(1));
						break;
						
						// Pop one, push specified array as primitive
					case InstructionIndex.NEWARRAY:
						current = current.deriveStackPop(null,
							1);
						current = current.deriveStackPush(
							instruction.argument(0, PrimitiveType.class)
								.field().addDimensions(1));
						break;
						
						// Pop count in arg 1, push an array
					case InstructionIndex.MULTIANEWARRAY:
						current = current.deriveStackPop(null,
							instruction.intArgument(1));
						current = current.deriveStackPush(
							instruction.argument(0, ClassName.class)
								.field().addDimensions(
									instruction.intArgument(1)));
						break;
						
						// Push referred reference type
					case InstructionIndex.GETSTATIC:
						current = current.deriveStackPush(
							instruction.argument(0,
								FieldReference.class).memberType());
						break;
						
						// Pop one, push referred type
					case InstructionIndex.GETFIELD:
						current = current.deriveStackPop(null,
							1);
						current = current.deriveStackPush(
							instruction.argument(0,
								FieldReference.class).memberType());
						break;
						
						// Unhandled?
					default:
						throw Debugging.todo(
							InstructionMnemonics.toString(op));
				}
			}
			catch (InvalidClassFormatException|IllegalArgumentException|
				IllegalStateException __e)
			{
				/* {@squirreljme.error JC9b Could not process for instruction.
				(The instruction; The input; The output (may be partial)} */
				throw new InvalidClassFormatException(
					String.format("JC9b %s L#%d %s ?(%s)?",
						instruction,
						this.lineOfAddress(instruction.address()),
						input,
						current), __e, this);
			}
			
			// Debug
			/*Debugging.debugNote("... -> %s",
				current);*/
			
			// Store output of the instruction
			if (!outputs.containsKey(actualAddr))
				outputs.put(actualAddr, current);
			
			// Set inputs for all jump targets
			InstructionJumpTargets jumps = instruction.jumpTargets();
			for (int i = 0, n = jumps.size(); i < n; i++)
			{
				// Get the designated jump target
				InstructionJumpTarget jump = jumps.get(i);
				boolean isException = jumps.isException(i);
				
				// Set input if it does not exist
				if (!inputs.containsKey(jump.target))
				{
					if (!isException)
						inputs.put(jump.target, current);
				}
			}
		}
		
		// Setup table pair
		return new StackMapTablePairs(inputs, outputs);
	}
	
	/**
	 * Returns the method which owns this byte code.
	 *
	 * @return The owning method.
	 * @since 2017/10/15
	 */
	private Method __method()
	{
		/* {@squirreljme.error JC24 The method owning this byte code has been
		garbage collected.} */
		Method rv = this._methodref.get();
		if (rv == null)
			throw new IllegalStateException("JC24");
		return rv;
	}
	
	/**
	 * Derives the component type.
	 * 
	 * @param __entry The entry to derive.
	 * @return The derived type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	private static StackMapTableEntry __deriveComponentType(
		StackMapTableEntry __entry)
		throws NullPointerException
	{
		if (__entry == null)
			throw new NullPointerException("NARG");
		
		// Get the component type, if it is not found, assume object
		FieldDescriptor type = __entry.type().type().componentType();
		if (type == null)
			return StackMapTableEntry.INITIALIZED_OBJECT;
		
		// Promote smaller than int primitives
		if (type.isPrimitive())
			switch (type.primitiveType())
			{
				case BOOLEAN:
				case BYTE:
				case SHORT:
				case CHARACTER:
					type = FieldDescriptor.INTEGER;
					break;
			}
		
		return new StackMapTableEntry(type, true);
	}
	
	/**
	 * Derives the component type for primitive arrays.
	 *
	 * @param __op The operation to check.
	 * @return The entry for the type.
	 * @throws InvalidClassFormatException If the operation is not valid.
	 * @since 2023/07/17
	 */
	private static StackMapTableEntry __deriveComponentTypeViaOp(int __op)
		throws InvalidClassFormatException
	{
		switch (__op)
		{
			case InstructionIndex.BALOAD:
			case InstructionIndex.CALOAD:
			case InstructionIndex.SALOAD:
			case InstructionIndex.IALOAD:
				return StackMapTableEntry.INTEGER;
			
			case InstructionIndex.LALOAD:
				return StackMapTableEntry.LONG;
				
			case InstructionIndex.FALOAD:
				return StackMapTableEntry.FLOAT;
				
			case InstructionIndex.DALOAD:
				return StackMapTableEntry.DOUBLE;
		}
			
		/* {@squirreljme.error JCl1 Could not derive primitive type of the
		array instruction. (The instruction)} */
		throw new InvalidClassFormatException(
			String.format("JCl1 %s", InstructionMnemonics.toString(__op)));
	}
	
	/**
	 * Derives the LDC instruction.
	 *
	 * @param __instruction The instruction.
	 * @return The derived entry.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	private static FieldDescriptor __deriveLdc(Instruction __instruction)
		throws NullPointerException
	{
		if (__instruction == null)
			throw new NullPointerException("NARG");
		
		return __instruction.argument(0, ConstantValue.class)
			.type.javaType().type;
	}
	
	/**
	 * Initializes a new cache array.
	 *
	 * @param __l The length of the array.
	 * @return The cache array.
	 * @since 2017/05/18
	 */
	@SuppressWarnings({"unchecked"})
	private static Reference<Instruction>[] __newCache(int __l)
	{
		return (Reference<Instruction>[])new Reference[__l];
	}
	
	/**
	 * This iterates over each byte code instruction.
	 *
	 * @since 2017/05/20
	 */
	private final class __InstructionIterator__
		implements Iterator<Instruction>
	{
		/** The code length. */
		protected final int codelen =
			ByteCode.this.codelen;
		
		/** The read address. */
		private int _at;
		
		/**
		 * {@inheritDoc}
		 * @since 2017/05/20
		 */
		@Override
		public boolean hasNext()
		{
			return this._at < this.codelen;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/05/20
		 */
		@Override
		public Instruction next()
			throws NoSuchElementException
		{
			// No more?
			if (!this.hasNext())
				throw new NoSuchElementException("NSEE");
			
			// Instruction at current pointer
			int at = this._at;
			Instruction rv = ByteCode.this.getByAddress(at);
			
			// Skip length of instruction
			this._at += ByteCode.this._lengths[at];
			
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/05/20
		 */
		@Override
		public void remove()
			throws UnsupportedOperationException
		{
			throw new UnsupportedOperationException("RORO");
		}
	}
}

