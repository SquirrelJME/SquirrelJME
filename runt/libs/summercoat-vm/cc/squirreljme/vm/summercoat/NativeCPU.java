// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import dev.shadowtail.classfile.nncc.ArgumentFormat;
import dev.shadowtail.classfile.nncc.NativeCode;
import dev.shadowtail.classfile.nncc.NativeInstruction;
import dev.shadowtail.classfile.nncc.NativeInstructionType;
import dev.shadowtail.classfile.xlate.CompareType;
import dev.shadowtail.classfile.xlate.DataType;
import dev.shadowtail.classfile.xlate.InvokeType;
import dev.shadowtail.classfile.xlate.MathType;
import dev.shadowtail.classfile.xlate.StackJavaType;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.collections.IntegerList;
import net.multiphasicapps.io.HexDumpOutputStream;

/**
 * This represents a native CPU which may run within its own thread to
 * execute code that is running from within the virtual machine.
 *
 * @since 2019/04/21
 */
public final class NativeCPU
	implements Runnable
{
	/** Maximum amount of CPU registers. */
	public static final int MAX_REGISTERS =
		64;
	
	/** The size of the method cache. */
	public static final int METHOD_CACHE =
		1024;
	
	/** Spill over protection for the cache. */
	public static final int METHOD_CACHE_SPILL =
		512;
	
	/** The memory to read/write from. */
	protected final WritableMemory memory;
	
	/** Stack frames. */
	private final LinkedList<Frame> _frames =
		new LinkedList<>();
	
	/**
	 * Initializes the native CPU.
	 *
	 * @param __mem The memory space.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/21
	 */
	public NativeCPU(WritableMemory __mem)
		throws NullPointerException
	{
		if (__mem == null)
			throw new NullPointerException("NARG");
		
		this.memory = __mem;
	}
	
	/**
	 * Enters the given frame for the given address.
	 *
	 * @param __pc The address of the frame.
	 * @param __args Arguments to the frame
	 * @return The newly created frame.
	 * @since 2019/04/21
	 */
	public final Frame enterFrame(int __pc, int... __args)
	{
		// Setup new frame
		Frame rv = new Frame();
		rv._pc = __pc;
		rv._lastpc = __pc;
		
		// Old frame, to source globals from
		LinkedList<Frame> frames = this._frames;
		Frame lastframe = frames.peekLast();
		
		// Add to frame list
		frames.addLast(rv);
		
		// Seed initial globals, if valid
		int[] dest = rv._registers;
		if (lastframe != null)
		{
			int[] src = lastframe._registers;
			for (int i = 0; i < NativeCode.LOCAL_REGISTER_BASE; i++)
				dest[i] = src[i];
		}
		
		// Copy registers to the locals
		for (int i = 0, o = NativeCode.LOCAL_REGISTER_BASE, n = __args.length;
			i < n; i++, o++)
			dest[o] = __args[i];
		
		// Clear zero
		dest[0] = 0;
		
		// Use this frame
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/21
	 */
	@Override
	public final void run()
	{
		this.run(0);
	}
	
	/**
	 * Runs anything after this frame.
	 *
	 * @param __fl The frame limit.
	 * @since 2019/04/21
	 */
	public final void run(int __fl)
	{
		// Read the CPU stuff
		final WritableMemory memory = this.memory;
		boolean reload = true;
		
		// Frame specific info
		Frame nowframe = null;
		int[] lr = null;
		int pc = -1;
		
		// Per operation handling
		final int[] args = new int[6];
		final long[] largs = new long[6];
		
		// Method cache to reduce tons of method reads
		final byte[] icache = new byte[METHOD_CACHE];
		int lasticache = -(METHOD_CACHE_SPILL + 1);
		
		// Execution is effectively an infinite loop
		LinkedList<Frame> frames = this._frames;
		for (int frameat = frames.size(), lastframe = -1; frameat >= __fl;
			frameat = frames.size())
		{
			// Reload parameters?
			if ((reload |= (lastframe != frameat)))
			{
				// Before dumping this frame, store old info
				if (nowframe != null)
					nowframe._pc = pc;
				
				// Get current frame, stop execution if there is nothing
				// left to execute
				nowframe = frames.peekLast();
				if (nowframe == null)
					return;
				
				// Load stuff needed for execution
				lr = nowframe._registers;
				pc = nowframe._pc;
				
				// Used to auto-detect frame change
				lastframe = frameat;
				
				// No longer reload information
				reload = false;
			}
			
			// For a bit faster execution of the method, cache a bunch of
			// the code that is being executed in memory. Constantly performing
			// the method calls to read single bytes of memory is a bit so, so
			// this should hopefully improve performance slightly.
			int pcdiff = pc - lasticache;
			if (pcdiff < 0 || pcdiff >= METHOD_CACHE_SPILL)
			{
				memory.memReadBytes(pc, icache, 0, METHOD_CACHE);
				lasticache = pc;
				
				// Dump the cache
				HexDumpOutputStream.dump(System.err, icache);
			}
			
			// Calculate last PC base address
			int bpc = pc - lasticache;
			
			// Read operation
			nowframe._lastpc = pc;
			int op = icache[bpc] & 0xFF;
			
			// Reset all input arguments
			for (int i = 0, n = args.length; i < n; i++)
			{
				args[i] = 0;
				largs[i] = 0;
			}
			
			// Register list, just one is used everywhere
			int[] reglist = null;
			
			// Load arguments for this instruction
			ArgumentFormat[] af = NativeInstruction.argumentFormat(op);
			int rargp = bpc + 1;
			for (int i = 0, n = af.length; i < n; i++)
				switch (af[i])
				{
					// Variable sized entries, may be pool values
					case VUINT:
					case VPOOL:
					case VJUMP:
						{
							// Long value?
							int base = (icache[rargp++] & 0xFF);
							if ((base & 0x80) != 0)
							{
								base = ((base & 0x7F) << 8);
								base |= (icache[rargp++] & 0xFF);
							}
							
							// Set
							args[i] = base;
						}
						break;
					
					// Register list.
					case REGLIST:
						{
							// Wide
							int count = (icache[rargp++] & 0xFF);
							if ((count & 0x80) != 0)
							{
								count = ((count & 0x7F) << 8) |
									(icache[rargp++] & 0xFF);
								
								// Read values
								reglist = new int[count];
								for (int r = 0; r < count; r++)
									reglist[r] =
										((icache[rargp++] & 0xFF) << 8) |
										(icache[rargp++] & 0xFF);
							}
							// Narrow
							else
							{
								reglist = new int[count];
								
								// Read values
								for (int r = 0; r < count; r++)
									reglist[r] = (icache[rargp++] & 0xFF);
							}
						}
						break;
					
					// 32-bit integer/float
					case INT32:
					case FLOAT32:
						args[i] = ((icache[rargp++] & 0xFF) << 24) |
							((icache[rargp++] & 0xFF) << 16) |
							((icache[rargp++] & 0xFF) << 8) |
							((icache[rargp++] & 0xFF));
						break;
					
					// 64-bit long/double
					case INT64:
					case FLOAT64:
						largs[i] = ((icache[rargp++] & 0xFFL) << 56L) |
							((icache[rargp++] & 0xFFL) << 48L) |
							((icache[rargp++] & 0xFFL) << 40L) |
							((icache[rargp++] & 0xFFL) << 32L) |
							((icache[rargp++] & 0xFFL) << 24L) |
							((icache[rargp++] & 0xFFL) << 16L) |
							((icache[rargp++] & 0xFFL) << 8L) |
							((icache[rargp++] & 0xFFL));
						break;
					
					default:
						throw new todo.OOPS(af[i].name());
				}
			
			// Debug
			todo.DEBUG.note("@%08x/%d -> (%02x) %s %s", pc, bpc,
				op, NativeInstruction.mnemonic(op), new IntegerList(args));
			
			// By default the next instruction is the address after all
			// arguments have been read
			int nextpc = lasticache + rargp;
			
			// Handle the operation
			switch (NativeInstruction.encoding(op))
			{
					// Entry marker used for debug
				case NativeInstructionType.ENTRY_MARKER:
					break;
					
					// Conversion (Narrow)
				case NativeInstructionType.CONVERSION:
					{
						StackJavaType a = StackJavaType.of((op >> 2) & 0x3),
							b = StackJavaType.of(op & 0x03);
						
						// The value to convert
						int va = lr[args[0]];
						
						if (a == StackJavaType.INTEGER)
						{
							if (b == StackJavaType.FLOAT)
								va = Float.floatToRawIntBits((float)va);
						}
						else
						{
							if (b == StackJavaType.INTEGER)
								va = (int)Float.intBitsToFloat(va);
						}
						
						// Set destination
						lr[args[1]] = va;
					}
					break;
					
					// Integer math
				case NativeInstructionType.MATH_CONST_INT:
				case NativeInstructionType.MATH_REG_INT:
					{
						// Parts
						int a = lr[args[0]],
							b = (((op & 0x80) != 0) ? args[1] : lr[args[1]]),
							c;
						
						// Operation to execute
						switch (MathType.of(op & 0xF))
						{
							case ADD:		c = a + b; break;
							case SUB:		c = a - b; break;
							case MUL:		c = a * b; break;
							case DIV:		c = a / b; break;
							case REM:		c = a % b; break;
							case NEG:		c = -a; break;
							case SHL:		c = a << b; break;
							case SHR:		c = a >> b; break;
							case USHR:		c = a >>> b; break;
							case AND:		c = a & b; break;
							case OR:		c = a | b; break;
							case XOR:		c = a ^ b; break;
							case SIGN_X8:	c = (byte)a; break;
							case SIGN_HALF:	c = (short)a; break;
							
							case CMPL:
							case CMPG:
								c = (a < b ? -1 : (a == b ? 0 : 1));
								break;
							
							default:
								throw new todo.OOPS();
						}
						
						// Set result
						lr[args[2]] = c;
					}
					break;
				
				default:
					throw new todo.OOPS(NativeInstruction.mnemonic(op));
			}
			
			// Set next PC address
			pc = nextpc;
		}
	}
	
	/**
	 * This represents a single frame in the execution stack.
	 *
	 * @since 2019/04/21
	 */
	public static final class Frame
	{
		/** Registers for this frame. */
		final int[] _registers =
			new int[MAX_REGISTERS];
		
		/** The PC address for this frame. */
		volatile int _pc;
		
		/** Last executed address. */
		int _lastpc;
	}
}

