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
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.io.region.SizeLimitedInputStream;
import net.multiphasicapps.squirreljme.java.symbols.MethodSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITMethodFlags;

/**
 * This class is used to decode the actual code attribute in the method
 * along with any of its attributes.
 *
 * @since 2016/08/19
 */
final class __CodeDecoder__
{
	/** The maximum number of bytes the code attribute can be. */
	private static final int _CODE_SIZE_LIMIT =
		65535;
	
	/** The owning class decoder. */
	final __ClassDecoder__ _decoder;
	
	/** The logic writer to use. */
	final JITMethodWriter _writer;
	
	/** The method flags. */
	final JITMethodFlags _flags;
	
	/** The method type. */
	final MethodSymbol _type;
	
	/** The input code attribute data. */
	private final DataInputStream _input;
	
	/**
	 * Add base code decoder class.
	 *
	 * @param __cd The class decoder being used.
	 * @param __dis The input source.
	 * @param __f The method flags.
	 * @param __t The method type.
	 * @param __mlw The logic writer to write code to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/18
	 */
	__CodeDecoder__(__ClassDecoder__ __cd, DataInputStream __dis,
		JITMethodFlags __f, MethodSymbol __t, JITMethodWriter __mlw)
		throws NullPointerException
	{
		// Check
		if (__cd == null || __dis == null || __f == null || __t == null ||
			__mlw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._decoder = __cd;
		this._input = __dis;
		this._flags = __f;
		this._type = __t;
		this._writer = __mlw;
	}
	
	/**
	 * Decodes the code attribute and any of its contained data
	 *
	 * @return The program to be given to the writer.
	 * @throws IOException On read/write errors.
	 * @since 2016/08/23
	 */
	JITMethodProgram __decode()
		throws IOException
	{
		DataInputStream input = this._input;
		
		// Read max stack and locals
		int maxstack = input.readUnsignedShort();
		int maxlocals = input.readUnsignedShort();
		
		// {@squirreljme.error ED06 The code for a given method exceeds the
		// code size limit, or the size is zero. (The current code length;
		// The code size limit)}
		int codelen = input.readInt();
		if (codelen <= 0 || codelen > _CODE_SIZE_LIMIT)
			throw new JITException(String.format("ED06 %d %d",
				codelen & 0xFFFF_FFFFL, _CODE_SIZE_LIMIT));
		
		// Setup read for byte code
		try (DataInputStream cdis = new DataInputStream(
			new SizeLimitedInputStream(input, codelen, true)))
		{
			__decodeOps(cdis);
		}
		
		// Read the exception table
		int numex = input.readUnsignedShort();
		for (int i = 0; i < numex; i++)
			throw new Error("TODO");
		
		throw new Error("TODO");
	}
	
	/**
	 * Decodes operations into micro-operations.
	 *
	 * @param __dis The operation source.
	 * @throws IOException On read errors.
	 * @throws JITException On decode errors.
	 * @since 2016/08/24
	 */
	private void __decodeOps(DataInputStream __dis)
		throws IOException, JITException
	{
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		// Decode loop
		JITMethodProgram mp = new JITMethodProgram();
		for (;;)
		{
			// Read
			int code = __dis.read();
			
			// EOF?
			if (code < 0)
				break;
			
			// Wide? Read another
			if (code == __OpIndex__.WIDE)
				code = (code << 8) | __dis.readUnsignedByte();
			
			// Depends
			switch (code)
			{
					// Waste electrons
				case __OpIndex__.NOP:
					throw new Error("TODO");
				
					// Push null constant
				case __OpIndex__.ACONST_NULL:
					throw new Error("TODO");
				
					// Push int constant
				case __OpIndex__.ICONST_M1:
				case __OpIndex__.ICONST_0:
				case __OpIndex__.ICONST_1:
				case __OpIndex__.ICONST_2:
				case __OpIndex__.ICONST_3:
				case __OpIndex__.ICONST_4:
				case __OpIndex__.ICONST_5:
					throw new Error("TODO");
				
					// Push long constant
				case __OpIndex__.LCONST_0:
				case __OpIndex__.LCONST_1:
					throw new Error("TODO");
				
					// Push float constant
				case __OpIndex__.FCONST_0:
				case __OpIndex__.FCONST_1:
				case __OpIndex__.FCONST_2:
					throw new Error("TODO");
				
					// Push double constant
				case __OpIndex__.DCONST_0:
				case __OpIndex__.DCONST_1:
					throw new Error("TODO");
				
				case __OpIndex__.BIPUSH:
				case __OpIndex__.SIPUSH:
				case __OpIndex__.LDC:
				case __OpIndex__.LDC_W:
				case __OpIndex__.LDC2_W:
				case __OpIndex__.ILOAD:
				case __OpIndex__.LLOAD:
				case __OpIndex__.FLOAD:
				case __OpIndex__.DLOAD:
				case __OpIndex__.ALOAD:
					throw new Error("TODO");
				
					// Load int from local
				case __OpIndex__.ILOAD_0:
				case __OpIndex__.ILOAD_1:
				case __OpIndex__.ILOAD_2:
				case __OpIndex__.ILOAD_3:
					throw new Error("TODO");
				
					// Load long from local
				case __OpIndex__.LLOAD_0:
				case __OpIndex__.LLOAD_1:
				case __OpIndex__.LLOAD_2:
				case __OpIndex__.LLOAD_3:
					throw new Error("TODO");
				
					// Load float from local
				case __OpIndex__.FLOAD_0:
				case __OpIndex__.FLOAD_1:
				case __OpIndex__.FLOAD_2:
				case __OpIndex__.FLOAD_3:
					throw new Error("TODO");
				
					// Load double from local
				case __OpIndex__.DLOAD_0:
				case __OpIndex__.DLOAD_1:
				case __OpIndex__.DLOAD_2:
				case __OpIndex__.DLOAD_3:
					throw new Error("TODO");
				
					// Load reference from local
				case __OpIndex__.ALOAD_0:
				case __OpIndex__.ALOAD_1:
				case __OpIndex__.ALOAD_2:
				case __OpIndex__.ALOAD_3:
					throw new Error("TODO");
				
				case __OpIndex__.IALOAD:
				case __OpIndex__.LALOAD:
				case __OpIndex__.FALOAD:
				case __OpIndex__.DALOAD:
				case __OpIndex__.AALOAD:
				case __OpIndex__.BALOAD:
				case __OpIndex__.CALOAD:
				case __OpIndex__.SALOAD:
				case __OpIndex__.ISTORE:
				case __OpIndex__.LSTORE:
				case __OpIndex__.FSTORE:
				case __OpIndex__.DSTORE:
				case __OpIndex__.ASTORE:
					throw new Error("TODO");
				
					// Store int into local
				case __OpIndex__.ISTORE_0:
				case __OpIndex__.ISTORE_1:
				case __OpIndex__.ISTORE_2:
				case __OpIndex__.ISTORE_3:
					throw new Error("TODO");
				
					// Store long into local
				case __OpIndex__.LSTORE_0:
				case __OpIndex__.LSTORE_1:
				case __OpIndex__.LSTORE_2:
				case __OpIndex__.LSTORE_3:
					throw new Error("TODO");
				
					// Store float into local
				case __OpIndex__.FSTORE_0:
				case __OpIndex__.FSTORE_1:
				case __OpIndex__.FSTORE_2:
				case __OpIndex__.FSTORE_3:
					throw new Error("TODO");
				
					// Store double into local
				case __OpIndex__.DSTORE_0:
				case __OpIndex__.DSTORE_1:
				case __OpIndex__.DSTORE_2:
				case __OpIndex__.DSTORE_3:
					throw new Error("TODO");
				
					// Store object into local
				case __OpIndex__.ASTORE_0:
				case __OpIndex__.ASTORE_1:
				case __OpIndex__.ASTORE_2:
				case __OpIndex__.ASTORE_3:
					throw new Error("TODO");
				
				case __OpIndex__.IASTORE:
				case __OpIndex__.LASTORE:
				case __OpIndex__.FASTORE:
				case __OpIndex__.DASTORE:
				case __OpIndex__.AASTORE:
				case __OpIndex__.BASTORE:
				case __OpIndex__.CASTORE:
				case __OpIndex__.SASTORE:
				case __OpIndex__.POP:
				case __OpIndex__.POP2:
				case __OpIndex__.DUP:
				case __OpIndex__.DUP_X1:
				case __OpIndex__.DUP_X2:
				case __OpIndex__.DUP2:
				case __OpIndex__.DUP2_X1:
				case __OpIndex__.DUP2_X2:
				case __OpIndex__.SWAP:
				case __OpIndex__.IADD:
				case __OpIndex__.LADD:
				case __OpIndex__.FADD:
				case __OpIndex__.DADD:
				case __OpIndex__.ISUB:
				case __OpIndex__.LSUB:
				case __OpIndex__.FSUB:
				case __OpIndex__.DSUB:
				case __OpIndex__.IMUL:
				case __OpIndex__.LMUL:
				case __OpIndex__.FMUL:
				case __OpIndex__.DMUL:
				case __OpIndex__.IDIV:
				case __OpIndex__.LDIV:
				case __OpIndex__.FDIV:
				case __OpIndex__.DDIV:
				case __OpIndex__.IREM:
				case __OpIndex__.LREM:
				case __OpIndex__.FREM:
				case __OpIndex__.DREM:
				case __OpIndex__.INEG:
				case __OpIndex__.LNEG:
				case __OpIndex__.FNEG:
				case __OpIndex__.DNEG:
				case __OpIndex__.ISHL:
				case __OpIndex__.LSHL:
				case __OpIndex__.ISHR:
				case __OpIndex__.LSHR:
				case __OpIndex__.IUSHR:
				case __OpIndex__.LUSHR:
				case __OpIndex__.IAND:
				case __OpIndex__.LAND:
				case __OpIndex__.IOR:
				case __OpIndex__.LOR:
				case __OpIndex__.IXOR:
				case __OpIndex__.LXOR:
				case __OpIndex__.IINC:
				case __OpIndex__.I2L:
				case __OpIndex__.I2F:
				case __OpIndex__.I2D:
				case __OpIndex__.L2I:
				case __OpIndex__.L2F:
				case __OpIndex__.L2D:
				case __OpIndex__.F2I:
				case __OpIndex__.F2L:
				case __OpIndex__.F2D:
				case __OpIndex__.D2I:
				case __OpIndex__.D2L:
				case __OpIndex__.D2F:
				case __OpIndex__.I2B:
				case __OpIndex__.I2C:
				case __OpIndex__.I2S:
				case __OpIndex__.LCMP:
				case __OpIndex__.FCMPL:
				case __OpIndex__.FCMPG:
				case __OpIndex__.DCMPL:
				case __OpIndex__.DCMPG:
				case __OpIndex__.IFEQ:
				case __OpIndex__.IFNE:
				case __OpIndex__.IFLT:
				case __OpIndex__.IFGE:
				case __OpIndex__.IFGT:
				case __OpIndex__.IFLE:
				case __OpIndex__.IF_ICMPEQ:
				case __OpIndex__.IF_ICMPNE:
				case __OpIndex__.IF_ICMPLT:
				case __OpIndex__.IF_ICMPGE:
				case __OpIndex__.IF_ICMPGT:
				case __OpIndex__.IF_ICMPLE:
				case __OpIndex__.IF_ACMPEQ:
				case __OpIndex__.IF_ACMPNE:
				case __OpIndex__.GOTO:
				case __OpIndex__.TABLESWITCH:
				case __OpIndex__.LOOKUPSWITCH:
				case __OpIndex__.IRETURN:
				case __OpIndex__.LRETURN:
				case __OpIndex__.FRETURN:
				case __OpIndex__.DRETURN:
				case __OpIndex__.ARETURN:
				case __OpIndex__.RETURN:
				case __OpIndex__.GETSTATIC:
				case __OpIndex__.PUTSTATIC:
				case __OpIndex__.GETFIELD:
				case __OpIndex__.PUTFIELD:
				case __OpIndex__.INVOKEVIRTUAL:
				case __OpIndex__.INVOKESPECIAL:
				case __OpIndex__.INVOKESTATIC:
				case __OpIndex__.INVOKEINTERFACE:
				case __OpIndex__.NEW:
				case __OpIndex__.NEWARRAY:
				case __OpIndex__.ANEWARRAY:
				case __OpIndex__.ARRAYLENGTH:
				case __OpIndex__.ATHROW:
				case __OpIndex__.CHECKCAST:
				case __OpIndex__.INSTANCEOF:
				case __OpIndex__.MONITORENTER:
				case __OpIndex__.MONITOREXIT:
				case __OpIndex__.MULTIANEWARRAY:
				case __OpIndex__.IFNULL:
				case __OpIndex__.IFNONNULL:
				case __OpIndex__.GOTO_W:
					throw new Error("TODO");
				
					// {@squirreljme.error ED08 Defined operation cannot be
					// used in Java ME programs. (The operation)}
				case __OpIndex__.JSR:
				case __OpIndex__.JSR_W:
				case __OpIndex__.RET:
				case __OpIndex__.INVOKEDYNAMIC:
				case __OpIndex__.BREAKPOINT:
				case __OpIndex__.IMPDEP1:
				case __OpIndex__.IMPDEP2:
					throw new JITException(String.format("ED08 %d", code));
				
					// {@squirreljme.error ED07 Unknown byte-code operation.
					// (The operation)}
				default:
					throw new JITException(String.format("ED07 %d", code));
			}
		}
		
		throw new Error("TODO");
	}
}

