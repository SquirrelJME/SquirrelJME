// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class represents the byte code within a method.
 *
 * @since 2017/10/09
 */
public final class ByteCode
	implements Iterable<Instruction>
{
	/** The code is always at this offset. */
	static final int _CODE_OFFSET =
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
	
	/** The input attribute code, used for instruction lookup. */
	private final byte[] _rawattributedata;
	
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
	
	/**
	 * Initializes the byte code.
	 *
	 * @param __mr The owning method reference.
	 * @param __ca The raw code attribute data.
	 * @param __tt The this type.
	 * @throws InvalidClassFormatException If the byte code is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/09
	 */
	ByteCode(Reference<Method> __mr, byte[] __ca, ClassName __tt)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__mr == null || __ca == null || __tt == null)
			throw new NullPointerException("NARG");
		
		// Needed
		Method method = __mr.get();
		
		// Set
		this._methodref = __mr;
		this._rawattributedata = __ca;
		
		// Is this an initializer method?
		this.methodname = method.name();
		
		// If any IOExceptions are generated then the attribute is not valid
		Pool pool = method.pool();
		try (DataInputStream in = new DataInputStream(
			new ByteArrayInputStream(__ca)))
		{
			// The number of variables allocated to the method
			int maxstack = in.readUnsignedShort(),
				maxlocals = in.readUnsignedShort();
				
			// {@squirreljme.error JC06 The specified code length is not valid.
			// (The code length)}
			int codelen = in.readInt();
			if (codelen <= 0 || codelen > _MAX_CODE_LENGTH)
				throw new InvalidClassFormatException(
					String.format("JC06 %d", codelen));
		
			// Ignore that many bytes
			for (int i = 0; i < codelen; i++)
				in.readByte();
			
			// Read exception handler table
			ExceptionHandlerTable eht = ExceptionHandlerTable.decode(in, pool,
				codelen);
			
			// The instruction index is used to lookup using a linear index
			// count rather than the potentially spaced out address lookup
			int[] index = new int[codelen];
			int indexat = 0;
		
			// Set all lengths initially to invalid positions, this used as a
			// quick marker to determine which positions have valid
			// instructions
			int[] lengths = new int[codelen];
			for (int i = 0; i < codelen; i++)
				lengths[i] = -1;
		
			// Determine instruction lengths for each position
			int[] ollastop = new int[]{-1};
			for (int i = 0, li = -1; i < codelen; li = i)
			{
				// Store address of instruction for an index based lookup
				index[indexat++] = i;
			
				// Store length
				int oplen;
				lengths[i] = (oplen = __opLength(__ca, i, ollastop));
			
				// {@squirreljme.error JC07 The operation exceeds the bounds of
				// the method byte code. (The operation pointer; The operation
				// length; The code length; The last operation pointer)}
				if ((i += oplen) > codelen)
					throw new InvalidClassFormatException(
						String.format("JC07 %d %d %d", i, oplen, codelen, li));
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
			short[] lnt = new short[codelen];
			for (int i = 0; i < codelen; i++)
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
						if (pc < 0 || pc >= codelen)
							continue;
						
						// This gets handled later, but if there is more than
						// 65534 lines in a file then what is the programmer
						// even doing
						lnt[pc] = (short)line;
					}
				}
			
			// Can set fields now
			this.maxstack = maxstack;
			this.maxlocals = maxlocals;
			this.codelen = codelen;
			this.exceptions = eht;
			this.pool = pool;
			this.thistype = __tt;
			this._smtdata = smt;
			this._newsmtdata = smtnew;
			this._lengths = lengths;
			this._icache = __newCache(codelen);
			this._linenumbertable = lnt;
			
			// Store addresses for all the indexes
			if (indexat == codelen)
				this._index = index;
			else
				this._index = Arrays.copyOf(index, indexat);
		}
		
		// {@squirreljme.error JC08 Failed to read from the code attribute.}
		catch (IOException e)
		{
			throw new InvalidClassFormatException("JC08", e);
		}
	}
	
	/**
	 * Returns the address of the instruction following the specified one.
	 *
	 * @param __a The following address.
	 * @return The instruction address following the instruction at the
	 * specified address.
	 * @throws JITExcepInvalidClassFormatExceptiontion If the specified address
	 * is not valid.
	 * @since 2017/05/20
	 */
	public int addressFollowing(int __a)
		throws InvalidClassFormatException
	{
		// {@squirreljme.error JC09 The instruction at the specified address is
		// not valid. (The address)}
		if (!isValidAddress(__a))
			throw new InvalidClassFormatException(
				String.format("JC09 %d", __a));
		
		return __a + this._lengths[__a];
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
		// {@squirreljme.error JC0a The instruction at the specified address is
		// not valid. (The address)}
		if (!isValidAddress(__a))
			throw new InvalidClassFormatException(
				String.format("JC0a %d", __a));
		
		Reference<Instruction>[] icache = this._icache;
		Reference<Instruction> ref = icache[__a];
		Instruction rv;
		
		if (ref == null || null == (rv = ref.get()))
			icache[__a] = new WeakReference<>((rv = new Instruction(
				this._rawattributedata, this.pool, __a, this.exceptions,
				stackMapTable())));
		
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
		
		return getByAddress(index[__i]);
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
	 * Returns the number of instructions which are within this method.
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
		// {@squirreljme.error JC0b Out of bounds read of unsigned short from
		// raw byte code. (The address)}
		if (__addr < 0 || __addr >= this.codelen - 1)
			throw new IndexOutOfBoundsException(
				String.format("JC0b %d", __addr));
		
		byte[] rad = this._rawattributedata;
		int d = __addr + ByteCode._CODE_OFFSET;
		return ((rad[d] & 0xFF) << 8) |
			(rad[d + 1] & 0xFF);
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
			this._smt = new WeakReference<>(rv = new __StackMapParser__(
				this.pool, __method(), this._newsmtdata, this._smtdata,
				this, new JavaType(this.thistype)).get());
		
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
			for (Iterator<Instruction> it = instructionIterator();
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
	 * Returns the method which owns this byte code.
	 *
	 * @return The owning method.
	 * @since 2017/10/15
	 */
	private final Method __method()
	{
		// {@squirreljme.error JC0c The method owning this byte code has been
		// garbage collected.}
		Method rv = this._methodref.get();
		if (rv == null)
			throw new IllegalStateException("JC0c");
		return rv;
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
		return (Reference<Instruction>[])((Object)new Reference[__l]);
	}
	
	/**
	 * Returns the length of the operation at the given address.
	 *
	 * @param __code The method byte code.
	 * @param __a The address of the instruction to get the length of.
	 * @throws InvalidClassFormatException If the instruction is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/17
	 */
	private static int __opLength(byte[] __code, int __a, int[] __last)
		throws InvalidClassFormatException, NullPointerException
	{
		// Check
		if (__code == null)
			throw new NullPointerException("NARG");
		
		// Base instruction length is always 1
		int rv = 1;
		
		// Real offset, since the code attribute is offset
		int aa = __a + _CODE_OFFSET;
		
		// Read operation
		int op = (__code[aa] & 0xFF);
		if (op == InstructionIndex.WIDE)
		{
			// {@squirreljme.error JC0d The wide instruction cannot be the
			// last instruction in a method. (The address)}
			if (aa + 1 >= __code.length)
				throw new InvalidClassFormatException(
					String.format("JC0d %d", __a));
			
			op = (op << 8) | (__code[aa + 1] & 0xFF);
			rv = 2;
		}
		
		// Depends on the operation
		switch (op)
		{
				// {@squirreljme.error JC0e Unsupported instruction specified
				// in the method byte code. (The operation; The address)}
			case InstructionIndex.BREAKPOINT:
			case InstructionIndex.IMPDEP1:
			case InstructionIndex.IMPDEP2:
			case InstructionIndex.JSR:
			case InstructionIndex.JSR_W:
			case InstructionIndex.RET:
			case InstructionIndex.WIDE:
				throw new InvalidClassFormatException(
					String.format("JC0e %d %d", op, __a));
			
				// {@squirreljme.error JC0f Invokedynamic is not supported in
				// this virtual machine. (The address)}
			case InstructionIndex.INVOKEDYNAMIC:
				throw new InvalidClassFormatException(
					String.format("JC0f %d", __a));
				
				// Operands with no arguments
			case InstructionIndex.AALOAD:
			case InstructionIndex.AASTORE:
			case InstructionIndex.ACONST_NULL:
			case InstructionIndex.ALOAD_0:
			case InstructionIndex.ALOAD_1:
			case InstructionIndex.ALOAD_2:
			case InstructionIndex.ALOAD_3:
			case InstructionIndex.ARETURN:
			case InstructionIndex.ARRAYLENGTH:
			case InstructionIndex.ASTORE_0:
			case InstructionIndex.ASTORE_1:
			case InstructionIndex.ASTORE_2:
			case InstructionIndex.ASTORE_3:
			case InstructionIndex.ATHROW:
			case InstructionIndex.BALOAD:
			case InstructionIndex.BASTORE:
			case InstructionIndex.CALOAD:
			case InstructionIndex.CASTORE:
			case InstructionIndex.D2F:
			case InstructionIndex.D2I:
			case InstructionIndex.D2L:
			case InstructionIndex.DADD:
			case InstructionIndex.DALOAD:
			case InstructionIndex.DASTORE:
			case InstructionIndex.DCMPG:
			case InstructionIndex.DCMPL:
			case InstructionIndex.DCONST_0:
			case InstructionIndex.DCONST_1:
			case InstructionIndex.DDIV:
			case InstructionIndex.DLOAD_0:
			case InstructionIndex.DLOAD_1:
			case InstructionIndex.DLOAD_2:
			case InstructionIndex.DLOAD_3:
			case InstructionIndex.DMUL:
			case InstructionIndex.DNEG:
			case InstructionIndex.DREM:
			case InstructionIndex.DRETURN:
			case InstructionIndex.DSTORE_0:
			case InstructionIndex.DSTORE_1:
			case InstructionIndex.DSTORE_2:
			case InstructionIndex.DSTORE_3:
			case InstructionIndex.DSUB:
			case InstructionIndex.DUP:
			case InstructionIndex.DUP2:
			case InstructionIndex.DUP2_X1:
			case InstructionIndex.DUP2_X2:
			case InstructionIndex.DUP_X1:
			case InstructionIndex.DUP_X2:
			case InstructionIndex.F2D:
			case InstructionIndex.F2I:
			case InstructionIndex.F2L:
			case InstructionIndex.FADD:
			case InstructionIndex.FALOAD:
			case InstructionIndex.FASTORE:
			case InstructionIndex.FCMPG:
			case InstructionIndex.FCMPL:
			case InstructionIndex.FCONST_0:
			case InstructionIndex.FCONST_1:
			case InstructionIndex.FCONST_2:
			case InstructionIndex.FDIV:
			case InstructionIndex.FLOAD_0:
			case InstructionIndex.FLOAD_1:
			case InstructionIndex.FLOAD_2:
			case InstructionIndex.FLOAD_3:
			case InstructionIndex.FMUL:
			case InstructionIndex.FNEG:
			case InstructionIndex.FREM:
			case InstructionIndex.FRETURN:
			case InstructionIndex.FSTORE_0:
			case InstructionIndex.FSTORE_1:
			case InstructionIndex.FSTORE_2:
			case InstructionIndex.FSTORE_3:
			case InstructionIndex.FSUB:
			case InstructionIndex.I2B:
			case InstructionIndex.I2C:
			case InstructionIndex.I2D:
			case InstructionIndex.I2F:
			case InstructionIndex.I2L:
			case InstructionIndex.I2S:
			case InstructionIndex.IADD:
			case InstructionIndex.IALOAD:
			case InstructionIndex.IAND:
			case InstructionIndex.IASTORE:
			case InstructionIndex.ICONST_0:
			case InstructionIndex.ICONST_1:
			case InstructionIndex.ICONST_2:
			case InstructionIndex.ICONST_3:
			case InstructionIndex.ICONST_4:
			case InstructionIndex.ICONST_5:
			case InstructionIndex.ICONST_M1:
			case InstructionIndex.IDIV:
			case InstructionIndex.ILOAD_0:
			case InstructionIndex.ILOAD_1:
			case InstructionIndex.ILOAD_2:
			case InstructionIndex.ILOAD_3:
			case InstructionIndex.IMUL:
			case InstructionIndex.INEG:
			case InstructionIndex.IOR:
			case InstructionIndex.IREM:
			case InstructionIndex.IRETURN:
			case InstructionIndex.ISHL:
			case InstructionIndex.ISHR:
			case InstructionIndex.ISTORE_0:
			case InstructionIndex.ISTORE_1:
			case InstructionIndex.ISTORE_2:
			case InstructionIndex.ISTORE_3:
			case InstructionIndex.ISUB:
			case InstructionIndex.IUSHR:
			case InstructionIndex.IXOR:
			case InstructionIndex.L2D:
			case InstructionIndex.L2F:
			case InstructionIndex.L2I:
			case InstructionIndex.LADD:
			case InstructionIndex.LALOAD:
			case InstructionIndex.LAND:
			case InstructionIndex.LASTORE:
			case InstructionIndex.LCMP:
			case InstructionIndex.LCONST_0:
			case InstructionIndex.LCONST_1:
			case InstructionIndex.LDIV:
			case InstructionIndex.LLOAD_0:
			case InstructionIndex.LLOAD_1:
			case InstructionIndex.LLOAD_2:
			case InstructionIndex.LLOAD_3:
			case InstructionIndex.LMUL:
			case InstructionIndex.LNEG:
			case InstructionIndex.LOR:
			case InstructionIndex.LREM:
			case InstructionIndex.LRETURN:
			case InstructionIndex.LSHL:
			case InstructionIndex.LSHR:
			case InstructionIndex.LSTORE_0:
			case InstructionIndex.LSTORE_1:
			case InstructionIndex.LSTORE_2:
			case InstructionIndex.LSTORE_3:
			case InstructionIndex.LSUB:
			case InstructionIndex.LUSHR:
			case InstructionIndex.LXOR:
			case InstructionIndex.MONITORENTER:
			case InstructionIndex.MONITOREXIT:
			case InstructionIndex.NOP:
			case InstructionIndex.POP:
			case InstructionIndex.POP2:
			case InstructionIndex.RETURN:
			case InstructionIndex.SALOAD:
			case InstructionIndex.SASTORE:
			case InstructionIndex.SWAP:
				break;
				
				// An additional byte
			case InstructionIndex.ALOAD:
			case InstructionIndex.ASTORE:
			case InstructionIndex.BIPUSH:
			case InstructionIndex.DLOAD:
			case InstructionIndex.DSTORE:
			case InstructionIndex.FLOAD:
			case InstructionIndex.FSTORE:
			case InstructionIndex.ILOAD:
			case InstructionIndex.ISTORE:
			case InstructionIndex.LDC:
			case InstructionIndex.LLOAD:
			case InstructionIndex.LSTORE:
			case InstructionIndex.NEWARRAY:
				rv++;
				break;
				
				// Operations with two bytes following
			case InstructionIndex.ANEWARRAY:
			case InstructionIndex.CHECKCAST:
			case InstructionIndex.GETFIELD:
			case InstructionIndex.GETSTATIC:
			case InstructionIndex.GOTO:
			case InstructionIndex.IF_ACMPEQ:
			case InstructionIndex.IF_ACMPNE:
			case InstructionIndex.IFEQ:
			case InstructionIndex.IFGE:
			case InstructionIndex.IFGT:
			case InstructionIndex.IF_ICMPEQ:
			case InstructionIndex.IF_ICMPGE:
			case InstructionIndex.IF_ICMPGT:
			case InstructionIndex.IF_ICMPLE:
			case InstructionIndex.IF_ICMPLT:
			case InstructionIndex.IF_ICMPNE:
			case InstructionIndex.IFLE:
			case InstructionIndex.IFLT:
			case InstructionIndex.IFNE:
			case InstructionIndex.IFNONNULL:
			case InstructionIndex.IFNULL:
			case InstructionIndex.IINC:
			case InstructionIndex.INSTANCEOF:
			case InstructionIndex.INVOKESPECIAL:
			case InstructionIndex.INVOKESTATIC:
			case InstructionIndex.INVOKEVIRTUAL:
			case InstructionIndex.LDC2_W:
			case InstructionIndex.LDC_W:
			case InstructionIndex.NEW:
			case InstructionIndex.PUTFIELD:
			case InstructionIndex.PUTSTATIC:
			case InstructionIndex.SIPUSH:
			case InstructionIndex.WIDE_ALOAD:
			case InstructionIndex.WIDE_ASTORE:
			case InstructionIndex.WIDE_DLOAD:
			case InstructionIndex.WIDE_DSTORE:
			case InstructionIndex.WIDE_FLOAD:
			case InstructionIndex.WIDE_FSTORE:
			case InstructionIndex.WIDE_ILOAD:
			case InstructionIndex.WIDE_ISTORE:
			case InstructionIndex.WIDE_LLOAD:
			case InstructionIndex.WIDE_LSTORE:
				rv += 2;
				break;
				
				// Three bytes
			case InstructionIndex.INVOKEINTERFACE:
			case InstructionIndex.MULTIANEWARRAY:
				rv += 3;
				break;
				
				// Four bytes
			case InstructionIndex.GOTO_W:
			case InstructionIndex.WIDE_IINC:
				rv += 4;
				break;
			
				// Table switch, the length of this instruction varies due to
				// alignment and the count contained within
			case InstructionIndex.TABLESWITCH:
				//             tusaddr +4    +8
			    // +0          +4      +8    +12    [+16x4         ]...
				// op  x  x  x default lowdx highdx [(highdx-lowdx)]...
				//    op  x  x default lowdx highdx [(highdx-lowdx)]...
				//       op  x default lowdx highdx [(highdx-lowdx)]...
				//          op default lowdx highdx [(highdx-lowdx)]...
				// tuspadlen includes the opcode
				int tusaddr = ((aa + 4) & (~3)),
					tuspadlen = tusaddr - aa;
				rv = tuspadlen + 12 + (4 *
					(Instruction.__readInt(__code, tusaddr + 8) -
					Instruction.__readInt(__code, tusaddr + 4) + 1));
				break;
				
				// Lookup switch, the length of this instruction varies due to
				// alignment and the number of contained entries.
			case InstructionIndex.LOOKUPSWITCH:
				// The instruction is in this format:
				//             lusaddr +4    +8
				// +0          +4      +8    [+12x8       ]...
				// op  x  x  x default count [match offset]...
				//    op  x  x default count [match offset]...
				//       op  x default count [match offset]...
				//          op default count [match offset]...
				// luspadlen includes the opcode
				int lusaddr = ((aa + 4) & (~3)),
					luspadlen = lusaddr - aa;
				rv = luspadlen + 8 + (8 * Instruction.__readInt(__code,
					lusaddr + 4));
				break;
			
				// {@squirreljme.error JC0g Cannot get the length of the
				// specified operation because it is not valid. (The operation;
				// The address; The operation before this one)}
			default:
				throw new InvalidClassFormatException(
					String.format("JC0g %d %d %d", op, __a,
					((__last != null && __last.length > 0) ? __last[0] : -1)));
		}
		
		// Set last
		if (__last != null && __last.length > 0)
			__last[0] = op;
		
		return rv;
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
		private int _at =
			0;
		
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
			if (!hasNext())
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

