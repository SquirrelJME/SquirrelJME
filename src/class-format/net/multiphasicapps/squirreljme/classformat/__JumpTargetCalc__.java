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
			if (code == __OpIndex__.WIDE)
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
			case __OpIndex__.GOTO:
				__add(__rv, __pos + __dis.readShort());
				break;
				
				// Goto (wide)
			case __OpIndex__.GOTO_W:
				__add(__rv, __pos + __dis.readInt());
				break;
				
				// Table switch
			case __OpIndex__.TABLESWITCH:
				throw new Error("TODO");
				
				// Lookup switch
			case __OpIndex__.LOOKUPSWITCH:
				throw new Error("TODO");
				
				// Comparisons
			case __OpIndex__.IF_ACMPEQ:
			case __OpIndex__.IF_ACMPNE:
			case __OpIndex__.IFEQ:
			case __OpIndex__.IFGE:
			case __OpIndex__.IFGT:
			case __OpIndex__.IF_ICMPEQ:
			case __OpIndex__.IF_ICMPGE:
			case __OpIndex__.IF_ICMPGT:
			case __OpIndex__.IF_ICMPLE:
			case __OpIndex__.IF_ICMPLT:
			case __OpIndex__.IF_ICMPNE:
			case __OpIndex__.IFLE:
			case __OpIndex__.IFLT:
			case __OpIndex__.IFNE:
			case __OpIndex__.IFNONNULL:
			case __OpIndex__.IFNULL:
				__add(__rv, __pos + __dis.readShort());
				break;
				
				// 5 byte parameter
			case __OpIndex__.WIDE_IINC:
				__dis.read();
				
				// 4 byte parameter
			case __OpIndex__.INVOKEINTERFACE:
				__dis.read();
			
				// 3 byte parameter:
			case __OpIndex__.MULTIANEWARRAY:
				__dis.read();
				
				// 2 byte parameter
			case __OpIndex__.ANEWARRAY:
			case __OpIndex__.CHECKCAST:
			case __OpIndex__.GETFIELD:
			case __OpIndex__.GETSTATIC:
			case __OpIndex__.IINC:
			case __OpIndex__.INSTANCEOF:
			case __OpIndex__.INVOKESPECIAL:
			case __OpIndex__.INVOKESTATIC:
			case __OpIndex__.INVOKEVIRTUAL:
			case __OpIndex__.LDC2_W:
			case __OpIndex__.LDC_W:
			case __OpIndex__.NEW:
			case __OpIndex__.PUTFIELD:
			case __OpIndex__.PUTSTATIC:
			case __OpIndex__.SIPUSH:
			case __OpIndex__.WIDE_ALOAD:
			case __OpIndex__.WIDE_ASTORE:
			case __OpIndex__.WIDE_DLOAD:
			case __OpIndex__.WIDE_DSTORE:
			case __OpIndex__.WIDE_FLOAD:
			case __OpIndex__.WIDE_FSTORE:
			case __OpIndex__.WIDE_ILOAD:
			case __OpIndex__.WIDE_ISTORE:
			case __OpIndex__.WIDE_LLOAD:
			case __OpIndex__.WIDE_LSTORE:
				__dis.read();
				
				// 1 byte parameter
			case __OpIndex__.ALOAD:
			case __OpIndex__.ASTORE:
			case __OpIndex__.BIPUSH:
			case __OpIndex__.DLOAD:
			case __OpIndex__.DSTORE:
			case __OpIndex__.FLOAD:
			case __OpIndex__.FSTORE:
			case __OpIndex__.ILOAD:
			case __OpIndex__.ISTORE:
			case __OpIndex__.LDC:
			case __OpIndex__.LLOAD:
			case __OpIndex__.LSTORE:
			case __OpIndex__.NEWARRAY:
				__dis.read();
				break;
				
				// No parameters
			case __OpIndex__.AALOAD:
			case __OpIndex__.AASTORE:
			case __OpIndex__.ACONST_NULL:
			case __OpIndex__.ALOAD_0:
			case __OpIndex__.ALOAD_1:
			case __OpIndex__.ALOAD_2:
			case __OpIndex__.ALOAD_3:
			case __OpIndex__.ARETURN:
			case __OpIndex__.ARRAYLENGTH:
			case __OpIndex__.ASTORE_0:
			case __OpIndex__.ASTORE_1:
			case __OpIndex__.ASTORE_2:
			case __OpIndex__.ASTORE_3:
			case __OpIndex__.ATHROW:
			case __OpIndex__.BALOAD:
			case __OpIndex__.BASTORE:
			case __OpIndex__.CALOAD:
			case __OpIndex__.CASTORE:
			case __OpIndex__.D2F:
			case __OpIndex__.D2I:
			case __OpIndex__.D2L:
			case __OpIndex__.DADD:
			case __OpIndex__.DALOAD:
			case __OpIndex__.DASTORE:
			case __OpIndex__.DCMPG:
			case __OpIndex__.DCMPL:
			case __OpIndex__.DCONST_0:
			case __OpIndex__.DCONST_1:
			case __OpIndex__.DDIV:
			case __OpIndex__.DLOAD_0:
			case __OpIndex__.DLOAD_1:
			case __OpIndex__.DLOAD_2:
			case __OpIndex__.DLOAD_3:
			case __OpIndex__.DMUL:
			case __OpIndex__.DNEG:
			case __OpIndex__.DREM:
			case __OpIndex__.DRETURN:
			case __OpIndex__.DSTORE_0:
			case __OpIndex__.DSTORE_1:
			case __OpIndex__.DSTORE_2:
			case __OpIndex__.DSTORE_3:
			case __OpIndex__.DSUB:
			case __OpIndex__.DUP:
			case __OpIndex__.DUP2:
			case __OpIndex__.DUP2_X1:
			case __OpIndex__.DUP2_X2:
			case __OpIndex__.DUP_X1:
			case __OpIndex__.DUP_X2:
			case __OpIndex__.F2D:
			case __OpIndex__.F2I:
			case __OpIndex__.F2L:
			case __OpIndex__.FADD:
			case __OpIndex__.FALOAD:
			case __OpIndex__.FASTORE:
			case __OpIndex__.FCMPG:
			case __OpIndex__.FCMPL:
			case __OpIndex__.FCONST_0:
			case __OpIndex__.FCONST_1:
			case __OpIndex__.FCONST_2:
			case __OpIndex__.FDIV:
			case __OpIndex__.FLOAD_0:
			case __OpIndex__.FLOAD_1:
			case __OpIndex__.FLOAD_2:
			case __OpIndex__.FLOAD_3:
			case __OpIndex__.FMUL:
			case __OpIndex__.FNEG:
			case __OpIndex__.FREM:
			case __OpIndex__.FRETURN:
			case __OpIndex__.FSTORE_0:
			case __OpIndex__.FSTORE_1:
			case __OpIndex__.FSTORE_2:
			case __OpIndex__.FSTORE_3:
			case __OpIndex__.FSUB:
			case __OpIndex__.I2B:
			case __OpIndex__.I2C:
			case __OpIndex__.I2D:
			case __OpIndex__.I2F:
			case __OpIndex__.I2L:
			case __OpIndex__.I2S:
			case __OpIndex__.IADD:
			case __OpIndex__.IALOAD:
			case __OpIndex__.IAND:
			case __OpIndex__.IASTORE:
			case __OpIndex__.ICONST_0:
			case __OpIndex__.ICONST_1:
			case __OpIndex__.ICONST_2:
			case __OpIndex__.ICONST_3:
			case __OpIndex__.ICONST_4:
			case __OpIndex__.ICONST_5:
			case __OpIndex__.ICONST_M1:
			case __OpIndex__.IDIV:
			case __OpIndex__.ILOAD_0:
			case __OpIndex__.ILOAD_1:
			case __OpIndex__.ILOAD_2:
			case __OpIndex__.ILOAD_3:
			case __OpIndex__.IMPDEP1:
			case __OpIndex__.IMPDEP2:
			case __OpIndex__.IMUL:
			case __OpIndex__.INEG:
			case __OpIndex__.IOR:
			case __OpIndex__.IREM:
			case __OpIndex__.IRETURN:
			case __OpIndex__.ISHL:
			case __OpIndex__.ISHR:
			case __OpIndex__.ISTORE_0:
			case __OpIndex__.ISTORE_1:
			case __OpIndex__.ISTORE_2:
			case __OpIndex__.ISTORE_3:
			case __OpIndex__.ISUB:
			case __OpIndex__.IUSHR:
			case __OpIndex__.IXOR:
			case __OpIndex__.L2D:
			case __OpIndex__.L2F:
			case __OpIndex__.L2I:
			case __OpIndex__.LADD:
			case __OpIndex__.LALOAD:
			case __OpIndex__.LAND:
			case __OpIndex__.LASTORE:
			case __OpIndex__.LCMP:
			case __OpIndex__.LCONST_0:
			case __OpIndex__.LCONST_1:
			case __OpIndex__.LDIV:
			case __OpIndex__.LLOAD_0:
			case __OpIndex__.LLOAD_1:
			case __OpIndex__.LLOAD_2:
			case __OpIndex__.LLOAD_3:
			case __OpIndex__.LMUL:
			case __OpIndex__.LNEG:
			case __OpIndex__.LOR:
			case __OpIndex__.LREM:
			case __OpIndex__.LRETURN:
			case __OpIndex__.LSHL:
			case __OpIndex__.LSHR:
			case __OpIndex__.LSTORE_0:
			case __OpIndex__.LSTORE_1:
			case __OpIndex__.LSTORE_2:
			case __OpIndex__.LSTORE_3:
			case __OpIndex__.LSUB:
			case __OpIndex__.LUSHR:
			case __OpIndex__.LXOR:
			case __OpIndex__.MONITORENTER:
			case __OpIndex__.MONITOREXIT:
			case __OpIndex__.NOP:
			case __OpIndex__.POP:
			case __OpIndex__.POP2:
			case __OpIndex__.RET:
			case __OpIndex__.RETURN:
			case __OpIndex__.SALOAD:
			case __OpIndex__.SASTORE:
			case __OpIndex__.SWAP:
				break;
				
				// {@squirreljme.error AY1j Illegal operation in Java byte
				// code. (The operation code; The position of it)}
			default:
				throw new JITException(String.format("AY1j %d %d", __code,
					__pos));
		}
	}
}

