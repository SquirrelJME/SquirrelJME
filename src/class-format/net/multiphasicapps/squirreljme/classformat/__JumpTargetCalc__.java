// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

import java.io.IOException;
import java.util.Arrays;
import net.multiphasicapps.io.data.ExtendedDataInputStream;

/**
 * This is used to determine which instruction addresses in the byte code are
 * jumped to, the benefit of this is that the target JIT does not have to
 * remember state for instructions that will never be jumped to.
 *
 * @since 2016/09/03
 */
class __JumpTargetCalc__
{
	/** The returning target array. */
	protected final int[] targets;
	
	/** The current write index. */
	private volatile int _index;
	
	/**
	 * Calculates the jump targets.
	 *
	 * @param __dis The source byte codes.
	 * @param __cl The code length.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	__JumpTargetCalc__(ExtendedDataInputStream __dis, int __cl)
		throws IOException, NullPointerException
	{
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Setup return value
		int[] rv = new int[__cl];
		
		// Go through all instructions, since it is marked a read must not
		// cause EOF to occur
		for (int nowpos = (int)__dis.size(), endpos = nowpos + __cl;
			nowpos < endpos; nowpos = (int)__dis.size())
		{
			// Read
			int code = __dis.read();
			
			// Wide? Read another
			if (code == ClassByteCodeIndex.WIDE)
				code = (code << 8) | __dis.readUnsignedByte();
			
			// Handle the code
			__handle(__dis, code, nowpos, rv);
		}
		
		// Either use the same array or allocate a new one
		int used = this._index;
		if (used == __cl)
			this.targets = rv;
		
		else
			this.targets = Arrays.copyOf(rv, used);
	}
	
	/**
	 * Returns the calculated jump targets.
	 *
	 * @return The jump targets in the method.
	 * @since 2016/09/03
	 */
	public int[] targets()
	{
		return this.targets;
	}
	
	/**
	 * Adds a single jump target to the list.
	 *
	 * @param __rv The resulting list.
	 * @param __p The position to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/04
	 */
	private void __add(int[] __rv, int __p)
		throws NullPointerException
	{
		// Check
		if (__rv == null)
			throw new NullPointerException("NARG");
		
		// Get the end of the list
		int end = this._index;
		
		// Keep the target list sorted, so essentially insertion sort is used
		// here
		for (int i = 0; i < end; i++)
		{
			// Get value
			int val = __rv[i];
			
			// If it is the same, it is already in this list
			if (val == __p)
				return;
			
			// Never insert higher positions before lower ones
			if (__p > val)
				continue;
			
			// Move all values over
			for (int j = end, k = j - 1; j > i; j--, k--)
				__rv[j] = __rv[k];
			
			// Insert
			__rv[i] = __p;
			
			// Would have been inserted
			return;
		}
		
		// Add to the end otherwise
		__rv[end] = __p;
		this._index = end + 1;
	}
	
	/**
	 * Handles a single operation.
	 *
	 * @param __dis The input data source.
	 * @param __code The operation.
	 * @param __pos The current position for relative addressing.
	 * @param __rv The returning resultant array.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/04
	 */
	private void __handle(ExtendedDataInputStream __dis, int __code, int __pos,
		int[] __rv)
		throws IOException, NullPointerException
	{
		// Check
		if (__dis == null || __rv == null)
			throw new NullPointerException("NARG");
		
		// Depends on the code
		switch (__code)
		{
				// Goto
			case ClassByteCodeIndex.GOTO:
				__add(__rv, __pos + __dis.readShort());
				break;
				
				// Goto (wide)
			case ClassByteCodeIndex.GOTO_W:
				__add(__rv, __pos + __dis.readInt());
				break;
				
				// Table switch
			case ClassByteCodeIndex.TABLESWITCH:
				throw new Error("TODO");
				
				// Lookup switch
			case ClassByteCodeIndex.LOOKUPSWITCH:
				throw new Error("TODO");
				
				// Comparisons
			case ClassByteCodeIndex.IF_ACMPEQ:
			case ClassByteCodeIndex.IF_ACMPNE:
			case ClassByteCodeIndex.IFEQ:
			case ClassByteCodeIndex.IFGE:
			case ClassByteCodeIndex.IFGT:
			case ClassByteCodeIndex.IF_ICMPEQ:
			case ClassByteCodeIndex.IF_ICMPGE:
			case ClassByteCodeIndex.IF_ICMPGT:
			case ClassByteCodeIndex.IF_ICMPLE:
			case ClassByteCodeIndex.IF_ICMPLT:
			case ClassByteCodeIndex.IF_ICMPNE:
			case ClassByteCodeIndex.IFLE:
			case ClassByteCodeIndex.IFLT:
			case ClassByteCodeIndex.IFNE:
			case ClassByteCodeIndex.IFNONNULL:
			case ClassByteCodeIndex.IFNULL:
				__add(__rv, __pos + __dis.readShort());
				break;
				
				// 5 byte parameter
			case ClassByteCodeIndex.WIDE_IINC:
				__dis.read();
				
				// 4 byte parameter
			case ClassByteCodeIndex.INVOKEINTERFACE:
				__dis.read();
			
				// 3 byte parameter:
			case ClassByteCodeIndex.MULTIANEWARRAY:
				__dis.read();
				
				// 2 byte parameter
			case ClassByteCodeIndex.ANEWARRAY:
			case ClassByteCodeIndex.CHECKCAST:
			case ClassByteCodeIndex.GETFIELD:
			case ClassByteCodeIndex.GETSTATIC:
			case ClassByteCodeIndex.IINC:
			case ClassByteCodeIndex.INSTANCEOF:
			case ClassByteCodeIndex.INVOKESPECIAL:
			case ClassByteCodeIndex.INVOKESTATIC:
			case ClassByteCodeIndex.INVOKEVIRTUAL:
			case ClassByteCodeIndex.LDC2_W:
			case ClassByteCodeIndex.LDC_W:
			case ClassByteCodeIndex.NEW:
			case ClassByteCodeIndex.PUTFIELD:
			case ClassByteCodeIndex.PUTSTATIC:
			case ClassByteCodeIndex.SIPUSH:
			case ClassByteCodeIndex.WIDE_ALOAD:
			case ClassByteCodeIndex.WIDE_ASTORE:
			case ClassByteCodeIndex.WIDE_DLOAD:
			case ClassByteCodeIndex.WIDE_DSTORE:
			case ClassByteCodeIndex.WIDE_FLOAD:
			case ClassByteCodeIndex.WIDE_FSTORE:
			case ClassByteCodeIndex.WIDE_ILOAD:
			case ClassByteCodeIndex.WIDE_ISTORE:
			case ClassByteCodeIndex.WIDE_LLOAD:
			case ClassByteCodeIndex.WIDE_LSTORE:
				__dis.read();
				
				// 1 byte parameter
			case ClassByteCodeIndex.ALOAD:
			case ClassByteCodeIndex.ASTORE:
			case ClassByteCodeIndex.BIPUSH:
			case ClassByteCodeIndex.DLOAD:
			case ClassByteCodeIndex.DSTORE:
			case ClassByteCodeIndex.FLOAD:
			case ClassByteCodeIndex.FSTORE:
			case ClassByteCodeIndex.ILOAD:
			case ClassByteCodeIndex.ISTORE:
			case ClassByteCodeIndex.LDC:
			case ClassByteCodeIndex.LLOAD:
			case ClassByteCodeIndex.LSTORE:
			case ClassByteCodeIndex.NEWARRAY:
				__dis.read();
				break;
				
				// No parameters
			case ClassByteCodeIndex.AALOAD:
			case ClassByteCodeIndex.AASTORE:
			case ClassByteCodeIndex.ACONST_NULL:
			case ClassByteCodeIndex.ALOAD_0:
			case ClassByteCodeIndex.ALOAD_1:
			case ClassByteCodeIndex.ALOAD_2:
			case ClassByteCodeIndex.ALOAD_3:
			case ClassByteCodeIndex.ARETURN:
			case ClassByteCodeIndex.ARRAYLENGTH:
			case ClassByteCodeIndex.ASTORE_0:
			case ClassByteCodeIndex.ASTORE_1:
			case ClassByteCodeIndex.ASTORE_2:
			case ClassByteCodeIndex.ASTORE_3:
			case ClassByteCodeIndex.ATHROW:
			case ClassByteCodeIndex.BALOAD:
			case ClassByteCodeIndex.BASTORE:
			case ClassByteCodeIndex.CALOAD:
			case ClassByteCodeIndex.CASTORE:
			case ClassByteCodeIndex.D2F:
			case ClassByteCodeIndex.D2I:
			case ClassByteCodeIndex.D2L:
			case ClassByteCodeIndex.DADD:
			case ClassByteCodeIndex.DALOAD:
			case ClassByteCodeIndex.DASTORE:
			case ClassByteCodeIndex.DCMPG:
			case ClassByteCodeIndex.DCMPL:
			case ClassByteCodeIndex.DCONST_0:
			case ClassByteCodeIndex.DCONST_1:
			case ClassByteCodeIndex.DDIV:
			case ClassByteCodeIndex.DLOAD_0:
			case ClassByteCodeIndex.DLOAD_1:
			case ClassByteCodeIndex.DLOAD_2:
			case ClassByteCodeIndex.DLOAD_3:
			case ClassByteCodeIndex.DMUL:
			case ClassByteCodeIndex.DNEG:
			case ClassByteCodeIndex.DREM:
			case ClassByteCodeIndex.DRETURN:
			case ClassByteCodeIndex.DSTORE_0:
			case ClassByteCodeIndex.DSTORE_1:
			case ClassByteCodeIndex.DSTORE_2:
			case ClassByteCodeIndex.DSTORE_3:
			case ClassByteCodeIndex.DSUB:
			case ClassByteCodeIndex.DUP:
			case ClassByteCodeIndex.DUP2:
			case ClassByteCodeIndex.DUP2_X1:
			case ClassByteCodeIndex.DUP2_X2:
			case ClassByteCodeIndex.DUP_X1:
			case ClassByteCodeIndex.DUP_X2:
			case ClassByteCodeIndex.F2D:
			case ClassByteCodeIndex.F2I:
			case ClassByteCodeIndex.F2L:
			case ClassByteCodeIndex.FADD:
			case ClassByteCodeIndex.FALOAD:
			case ClassByteCodeIndex.FASTORE:
			case ClassByteCodeIndex.FCMPG:
			case ClassByteCodeIndex.FCMPL:
			case ClassByteCodeIndex.FCONST_0:
			case ClassByteCodeIndex.FCONST_1:
			case ClassByteCodeIndex.FCONST_2:
			case ClassByteCodeIndex.FDIV:
			case ClassByteCodeIndex.FLOAD_0:
			case ClassByteCodeIndex.FLOAD_1:
			case ClassByteCodeIndex.FLOAD_2:
			case ClassByteCodeIndex.FLOAD_3:
			case ClassByteCodeIndex.FMUL:
			case ClassByteCodeIndex.FNEG:
			case ClassByteCodeIndex.FREM:
			case ClassByteCodeIndex.FRETURN:
			case ClassByteCodeIndex.FSTORE_0:
			case ClassByteCodeIndex.FSTORE_1:
			case ClassByteCodeIndex.FSTORE_2:
			case ClassByteCodeIndex.FSTORE_3:
			case ClassByteCodeIndex.FSUB:
			case ClassByteCodeIndex.I2B:
			case ClassByteCodeIndex.I2C:
			case ClassByteCodeIndex.I2D:
			case ClassByteCodeIndex.I2F:
			case ClassByteCodeIndex.I2L:
			case ClassByteCodeIndex.I2S:
			case ClassByteCodeIndex.IADD:
			case ClassByteCodeIndex.IALOAD:
			case ClassByteCodeIndex.IAND:
			case ClassByteCodeIndex.IASTORE:
			case ClassByteCodeIndex.ICONST_0:
			case ClassByteCodeIndex.ICONST_1:
			case ClassByteCodeIndex.ICONST_2:
			case ClassByteCodeIndex.ICONST_3:
			case ClassByteCodeIndex.ICONST_4:
			case ClassByteCodeIndex.ICONST_5:
			case ClassByteCodeIndex.ICONST_M1:
			case ClassByteCodeIndex.IDIV:
			case ClassByteCodeIndex.ILOAD_0:
			case ClassByteCodeIndex.ILOAD_1:
			case ClassByteCodeIndex.ILOAD_2:
			case ClassByteCodeIndex.ILOAD_3:
			case ClassByteCodeIndex.IMPDEP1:
			case ClassByteCodeIndex.IMPDEP2:
			case ClassByteCodeIndex.IMUL:
			case ClassByteCodeIndex.INEG:
			case ClassByteCodeIndex.IOR:
			case ClassByteCodeIndex.IREM:
			case ClassByteCodeIndex.IRETURN:
			case ClassByteCodeIndex.ISHL:
			case ClassByteCodeIndex.ISHR:
			case ClassByteCodeIndex.ISTORE_0:
			case ClassByteCodeIndex.ISTORE_1:
			case ClassByteCodeIndex.ISTORE_2:
			case ClassByteCodeIndex.ISTORE_3:
			case ClassByteCodeIndex.ISUB:
			case ClassByteCodeIndex.IUSHR:
			case ClassByteCodeIndex.IXOR:
			case ClassByteCodeIndex.L2D:
			case ClassByteCodeIndex.L2F:
			case ClassByteCodeIndex.L2I:
			case ClassByteCodeIndex.LADD:
			case ClassByteCodeIndex.LALOAD:
			case ClassByteCodeIndex.LAND:
			case ClassByteCodeIndex.LASTORE:
			case ClassByteCodeIndex.LCMP:
			case ClassByteCodeIndex.LCONST_0:
			case ClassByteCodeIndex.LCONST_1:
			case ClassByteCodeIndex.LDIV:
			case ClassByteCodeIndex.LLOAD_0:
			case ClassByteCodeIndex.LLOAD_1:
			case ClassByteCodeIndex.LLOAD_2:
			case ClassByteCodeIndex.LLOAD_3:
			case ClassByteCodeIndex.LMUL:
			case ClassByteCodeIndex.LNEG:
			case ClassByteCodeIndex.LOR:
			case ClassByteCodeIndex.LREM:
			case ClassByteCodeIndex.LRETURN:
			case ClassByteCodeIndex.LSHL:
			case ClassByteCodeIndex.LSHR:
			case ClassByteCodeIndex.LSTORE_0:
			case ClassByteCodeIndex.LSTORE_1:
			case ClassByteCodeIndex.LSTORE_2:
			case ClassByteCodeIndex.LSTORE_3:
			case ClassByteCodeIndex.LSUB:
			case ClassByteCodeIndex.LUSHR:
			case ClassByteCodeIndex.LXOR:
			case ClassByteCodeIndex.MONITORENTER:
			case ClassByteCodeIndex.MONITOREXIT:
			case ClassByteCodeIndex.NOP:
			case ClassByteCodeIndex.POP:
			case ClassByteCodeIndex.POP2:
			case ClassByteCodeIndex.RET:
			case ClassByteCodeIndex.RETURN:
			case ClassByteCodeIndex.SALOAD:
			case ClassByteCodeIndex.SASTORE:
			case ClassByteCodeIndex.SWAP:
				break;
				
				// {@squirreljme.error AY1j Illegal operation in Java byte
				// code. (The operation code; The position of it)}
			default:
				throw new ClassFormatException(String.format("AY1j %d %d", __code,
					__pos));
		}
	}
}

