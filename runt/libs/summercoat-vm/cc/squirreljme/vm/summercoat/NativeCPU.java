// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.vm.VMException;
import dev.shadowtail.classfile.nncc.ArgumentFormat;
import dev.shadowtail.classfile.nncc.NativeCode;
import dev.shadowtail.classfile.nncc.NativeInstruction;
import dev.shadowtail.classfile.nncc.NativeInstructionType;
import dev.shadowtail.classfile.xlate.CompareType;
import dev.shadowtail.classfile.xlate.DataType;
import dev.shadowtail.classfile.xlate.MathType;
import dev.shadowtail.classfile.xlate.StackJavaType;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.classfile.InstructionMnemonics;
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
	/**
	 * {@squirreljme.properly cc.squirreljme.summercoat.debug=boolean
	 * Should SummerCoat print lots of debugging information?}*/
	public static final boolean ENABLE_DEBUG =
		Boolean.getBoolean("cc.squirreljme.summercoat.debug");
	
	/** Maximum amount of CPU registers. */
	public static final int MAX_REGISTERS =
		64;
	
	/** The size of the method cache. */
	public static final int METHOD_CACHE =
		2048;
	
	/** Spill over protection for the cache. */
	public static final int METHOD_CACHE_SPILL =
		1024;
	
	/** The memory to read/write from. */
	protected final WritableMemory memory;
	
	/** Stack frames. */
	private final LinkedList<Frame> _frames =
		new LinkedList<>();
	
	/** System call error states for this CPU. */
	private final int[] _syscallerrors =
		new int[SystemCallIndex.NUM_SYSCALLS];
	
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
		// Old frame, to source globals from
		LinkedList<Frame> frames = this._frames;
		Frame lastframe = frames.peekLast();
		
		// Debug
		if (ENABLE_DEBUG)
		{
			System.err.printf(">>>> %08x >>>>>>>>>>>>>>>>>>>>>>%n", __pc);
			System.err.printf(" > ARG %s%n", new IntegerList(__args));
			System.err.printf(" > WAS %s%n", (lastframe == null ? null :
				this.trace(lastframe)));
		}
		
		// Setup new frame
		Frame rv = new Frame();
		rv._pc = __pc;
		rv._entrypc = __pc;
		rv._lastpc = __pc;
		
		// Add to frame list
		frames.addLast(rv);
		
		// Seed initial registers, if valid
		int[] dest = rv._registers;
		if (lastframe != null)
		{
			// Copy globals
			int[] src = lastframe._registers;
			for (int i = 0; i < NativeCode.LOCAL_REGISTER_BASE; i++)
				dest[i] = src[i];
			
			// Set the pool register to the next pool register value
			dest[NativeCode.POOL_REGISTER] =
				src[NativeCode.NEXT_POOL_REGISTER];
		}
		
		// Copy the arguments to the argument slots
		for (int i = 0, o = NativeCode.ARGUMENT_REGISTER_BASE,
			n = __args.length; i < n; i++, o++)
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
		int pc = -1,
			rp = -1;
		
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
				{
					nowframe._pc = pc;
					nowframe._rp = rp;
				}
				
				// Get current frame, stop execution if there is nothing
				// left to execute
				nowframe = frames.peekLast();
				if (nowframe == null)
					return;
				
				// Load stuff needed for execution
				lr = nowframe._registers;
				pc = nowframe._pc;
				rp = nowframe._rp;
				
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
			}
			
			// Calculate last PC base address
			int bpc = pc - lasticache;
			
			// Always set PC address for debugging frames
			nowframe._pc = pc;
			
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
					case VUREG:
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
							if (af[i] == ArgumentFormat.VJUMP)
								args[i] = (short)(base |
									((base & 0x4000) << 1));
							else
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
			
			// Print CPU debug info
			int encoding = NativeInstruction.encoding(op);
			if (ENABLE_DEBUG &&
				encoding != NativeInstructionType.DEBUG_ENTRY &&
				encoding != NativeInstructionType.DEBUG_EXIT &&
				encoding != NativeInstructionType.DEBUG_POINT)
				this.__cpuDebugPrint(nowframe, op, af, args, largs, reglist);
			
			// By default the next instruction is the address after all
			// arguments have been read
			int nextpc = lasticache + rargp;
			
			// Handle the operation
			switch (encoding)
			{
					// CPU Breakpoint
				case NativeInstructionType.BREAKPOINT:
					this.__cpuDebugPrint(nowframe, op, af, args, largs,
						reglist);
					
					// {@squirreljme.error AE04 CPU breakpoint hit.}
					throw new VMException("AE04");
				
					// Debug entry point of method
				case NativeInstructionType.DEBUG_ENTRY:
					this.__debugEntry(nowframe, args[0], args[1], args[2]);
					
					// Trace it!
					if (ENABLE_DEBUG)
						this.__cpuDebugPrint(nowframe, op, af, args, largs,
							reglist);
					break;
					
					// Debug exit of method
				case NativeInstructionType.DEBUG_EXIT:
					break;
					
					// Debug point in method.
				case NativeInstructionType.DEBUG_POINT:
					this.__debugPoint(nowframe, args[0], args[1], args[2]);
					break;
				
					// Atomic decrement and get
				case NativeInstructionType.ATOMIC_INT_DECREMENT_AND_GET:
					synchronized (memory)
					{
						// The address to load from/store to
						int addr = lr[args[1]] + args[2];
						
						// Read, increment, and store
						int newval;
						memory.memWriteInt(addr,
							(newval = memory.memReadInt(addr) - 1));
						
						// Store the value after the decrement
						lr[args[0]] = newval;
					}
					break;
					
					// Atomic increment
				case NativeInstructionType.ATOMIC_INT_INCREMENT:
					synchronized (memory)
					{
						// The address to load from/store to
						int addr = lr[args[0]] + args[1];
						
						// Read, increment, and store
						memory.memWriteInt(addr, memory.memReadInt(addr) + 1);
					}
					break;
				
					// Copy
				case NativeInstructionType.COPY:
					lr[args[1]] = lr[args[0]];
					break;
					
					// Compare integers and possibly jump
				case NativeInstructionType.IF_ICMP:
					{
						// Parts
						int a = lr[args[0]],
							b = lr[args[1]];
						
						// Compare
						boolean branch;
						CompareType ct;
						switch ((ct = CompareType.of(op & 0b111)))
						{
							case EQUALS:
								branch = (a == b); break;
							case NOT_EQUALS:
								branch = (a != b); break;
							case LESS_THAN:
								branch = (a < b); break;
							case LESS_THAN_OR_EQUALS:
								branch = (a <= b); break;
							case GREATER_THAN:
								branch = (a > b); break;
							case GREATER_THAN_OR_EQUALS:
								branch = (a >= b); break;
							case TRUE:
								branch = true; break;
							case FALSE:
								branch = false; break;
							
							default:
								throw new todo.OOPS();
						}
						
						// Branching?
						if (branch)
							nextpc = pc + args[2];
					}
					break;
					
					// If value equal to constant
				case NativeInstructionType.IFEQ_CONST:
					{
						// Branching? Remember that jumps are relative
						if (lr[args[0]] == args[1])
							nextpc = pc + args[2];
					}
					break;
					
					// Invoke a pointer
				case NativeInstructionType.INVOKE:
					{
						// Load values into the register list
						for (int i = 0, n = reglist.length; i < n; i++)
							reglist[i] = lr[reglist[i]];
						
						// Enter the frame
						this.enterFrame(lr[args[0]], reglist);
						
						// Entering some other frame
						reload = true;
					}
					break;
					
					// Load value from int array
				case NativeInstructionType.LOAD_FROM_INTARRAY:
					lr[args[0]] = memory.memReadInt(lr[args[1]] +
						Constants.ARRAY_BASE_SIZE + (lr[args[2]] * 4));
					break;
					
					// Load from constant pool
				case NativeInstructionType.LOAD_POOL:
					{
						lr[args[1]] = memory.memReadInt(
							lr[NativeCode.POOL_REGISTER] + (args[0] * 4));
						
						// Debug
						if (ENABLE_DEBUG)
							todo.DEBUG.note("Read pool: %d", lr[args[1]]);
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
						MathType mt;
						switch ((mt = MathType.of(op & 0xF)))
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
							case SIGNX8:	c = (byte)a; break;
							case SIGNX16:	c = (short)a; break;
							
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
				
					// Read off memory
				case NativeInstructionType.MEMORY_OFF_REG:
				case NativeInstructionType.MEMORY_OFF_REG_JAVA:
				case NativeInstructionType.MEMORY_OFF_ICONST:
				case NativeInstructionType.MEMORY_OFF_ICONST_JAVA:
					{
						// Is this a load operation?
						boolean load = ((op & 0b1000) != 0);
						
						// The address to load from/store to
						int addr = lr[args[1]] +
							(((op & 0x80) != 0) ? args[2] : lr[args[2]]);
						
						// Loads
						DataType dt = DataType.of(op & 0b0111);
						if (load)
						{
							// Load value
							int v;
							switch (dt)
							{
								case BYTE:
									v = (byte)memory.memReadByte(addr);
									break;
									
								case SHORT:
									v = (short)memory.memReadShort(addr);
									break;
									
								case CHARACTER:
									v = memory.memReadShort(addr) & 0xFFFF;
									break;
									
								case OBJECT:
								case INTEGER:
								case FLOAT:
									v = memory.memReadInt(addr);
									break;
									
									// Unknown
								default:
									throw new todo.OOPS(dt.name());
							}
							
							// Set value
							lr[args[0]] = v;
							
							// Debug
							if (ENABLE_DEBUG)
								todo.DEBUG.note("%08x -> %d (%08x)",
									addr, v, v);
						}
						
						// Stores
						else
						{
							// Value to store
							int v = lr[args[0]];
							
							// Store
							switch (dt)
							{
								case BYTE:
									memory.memWriteByte(addr, v);
									break;
									
								case SHORT:
									memory.memWriteShort(addr, v);
									break;
									
								case CHARACTER:
									memory.memWriteShort(addr, v);
									break;
								
								case OBJECT:
								case INTEGER:
								case FLOAT:
									memory.memWriteInt(addr, v);
									break;
									
									// Unknown
								default:
									throw new todo.OOPS(dt.name());
							}
							
							// Debug
							if (ENABLE_DEBUG)
								todo.DEBUG.note("%08x <- %d (%08x)",
									addr, v, v);
						}
					}
					break;
					
					// Return from method call
				case NativeInstructionType.RETURN:
					{
						// Go up frame
						Frame was = frames.removeLast(),
							now = frames.peekLast();
						
						// If we are going back onto a frame then copy all
						// the globals which were set since they are meant to
						// be global!
						if (now != null)
						{
							int[] wr = was._registers,
								nr = now._registers;
							
							// Copy globals
							for (int i = 0; i < NativeCode.LOCAL_REGISTER_BASE;
								i++)
							{
								// Ignore the pool register because if it is
								// replaced then it will just explode and
								// cause issues for the parent method
								if (i == NativeCode.POOL_REGISTER)
									continue;
								
								// Reset the next pool register
								else if (i == NativeCode.NEXT_POOL_REGISTER)
								{
									nr[i] = 0;
									break;
								}
								
								// Copy otherwise
								else
									nr[i] = wr[i];
							}
						}
						
						// A reload is done as the frame has changed
						reload = true;
						
						// Debug
						if (ENABLE_DEBUG)
							System.err.printf(
								"<<<< %08x <<<<<<<<<<<<<<<<<<<<<<%n",
								(now != null ? now._pc : 0));
					}
					break;
				
					// System call
				case NativeInstructionType.SYSTEM_CALL:
					{
						// The arguments to the system calls are in registers
						// so they must be extracted first before they can be
						// known
						int nrl = reglist.length;
						int[] sargs = new int[nrl];
						for (int i = 0; i < nrl; i++)
							sargs[i] = lr[reglist[i]];
						
						// Set the return register to whatever system call
						// was used
						lr[NativeCode.RETURN_REGISTER] = this.__sysCall(
							(short)lr[args[0]], sargs);
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
	 * Returns the trace of the current execution.
	 *
	 * @return The current trace.
	 * @since 2019/04/22
	 */
	public final CallTraceElement[] trace()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the trace for the given frame.
	 *
	 * @param __f The frame to trace.
	 * @return The trace of this frame.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/22
	 */
	public final CallTraceElement trace(Frame __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Build trace
		return new CallTraceElement(
			__f._inclass,
			__f._inmethodname,
			__f._inmethodtype,
			__f._pc, null,
			__f._inline,
			__f._injop,
			__f._injpc);
	}
	
	/**
	 * Performs some nice printing of CPU information.
	 *
	 * @param __nf The current frame.
	 * @param __op The operation.
	 * @param __af The argument format.
	 * @param __args Argument values.
	 * @param __largs Long argument values.
	 * @param __reglist The register list.
	 * @since 2019/04/23
	 */
	private final void __cpuDebugPrint(Frame __nf, int __op,
		ArgumentFormat[] __af, int[] __args, long[] __largs, int[] __reglist)
	{
		PrintStream out = System.err;
		
		// Limit class name
		CallTraceElement trace = this.trace(__nf);
		String cname = "" + trace.className();
		int nl;
		if ((nl = cname.length()) > 20)
			cname = cname.substring(nl - 20, nl);
		
		// Print Header (with location info)
		out.printf("***** @%08x %-19.19s/%10.10s | L%-4d/J%-3d %20.20s::%s %n",
			__nf._pc,
			NativeInstruction.mnemonic(__op),
			InstructionMnemonics.toString(trace.byteCodeInstruction()),
			trace.line(),
			trace.byteCodeAddress(),
			cname,
			trace.methodName() + ":" + trace.methodDescriptor());
		
		// Is this an invoke?
		boolean isinvoke = (__op == NativeInstructionType.INVOKE ||
			__op == NativeInstructionType.SYSTEM_CALL);
		
		// Arguments to print, invocations get 1 (pc) + register list
		int naf = (isinvoke ? 1 + __reglist.length:
			__af.length);
		
		// Used to modify some calls
		int encoding = NativeInstruction.encoding(__op);
		
		// Print out arguments to the call
		out.printf("  A:[");
		for (int i = 0, n = naf; i < n; i++)
		{
			int iv = (isinvoke ? (i == 0 ? __args[i] : __reglist[i - 1]) :
				__args[i]);
			
			// Comma
			if (i > 0)
				out.print(", ");
			
			// Can be special?
			boolean canspec = true;
			if (encoding == NativeInstructionType.DEBUG_ENTRY ||
				encoding == NativeInstructionType.DEBUG_POINT ||
				(encoding == NativeInstructionType.IF_ICMP &&
					i == 2) ||
				(encoding == NativeInstructionType.MATH_CONST_INT &&
					i == 1) ||
				(encoding == NativeInstructionType.IFEQ_CONST &&
					i == 1) ||
				(encoding == NativeInstructionType.ATOMIC_INT_INCREMENT &&
					i == 1) ||
				(encoding == NativeInstructionType.
					ATOMIC_INT_DECREMENT_AND_GET && i == 2) ||
				(encoding == NativeInstructionType.MEMORY_OFF_ICONST &&
					i == 2) ||
				(encoding == NativeInstructionType.LOAD_POOL && i == 0))
				canspec = false;
			
			// Is this a special register?
			String spec = null;
			if (canspec)
				switch (iv)
				{
					case NativeCode.ZERO_REGISTER:
						spec = "zero";
						break;
					
					case NativeCode.RETURN_REGISTER:
						spec = "return1";
						break;
					
					case NativeCode.RETURN_REGISTER + 1:
						spec = "return2";
						break;
					
					case NativeCode.EXCEPTION_REGISTER:
						spec = "exception";
						break;
					
					case NativeCode.STATIC_FIELD_REGISTER:
						spec = "sfieldptr";
						break;
					
					case NativeCode.THREAD_REGISTER:
						spec = "thread";
						break;
					
					case NativeCode.POOL_REGISTER:
						spec = "pool";
						break;
					
					case NativeCode.NEXT_POOL_REGISTER:
						spec = "nextpool";
						break;
					
					case NativeCode.ARGUMENT_REGISTER_BASE:
						spec = "a0/this";
						break;
				}
			
			// Print special register
			if (spec != null)
				out.printf("%10.10s", spec);
			else
				out.printf("%10d", iv);
		}
		out.print("] | ");
		
		// And register value
		out.printf("V:[");
		int[] registers = __nf._registers;
		for (int i = 0, n = naf; i < n; i++)
		{
			int iv = (isinvoke ? (i == 0 ? __args[i] : __reglist[i - 1]) :
				__args[i]);
				
			if (i > 0)
				out.print(", ");
			
			// Load register value
			if (iv < 0 || iv >= registers.length)
				out.print("----------");
			else
				out.printf("%+10d", registers[iv]);
		}
		out.println("]");
	}
	
	/**
	 * Sets the frame information string from the given pool entries.
	 *
	 * @param __f The frame.
	 * @param __pcl The class string from the pool.
	 * @param __pmn The method name from the pool.
	 * @param __pmt The method type from the pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/15
	 */
	private final void __debugEntry(Frame __f, int __pcl, int __pmn, int __pmt)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Get the pool address
		int pooladdr = __f._registers[NativeCode.POOL_REGISTER];
		
		// Load strings
		WritableMemory memory = this.memory;
		__f._inclass = this.__loadUtfString(
			memory.memReadInt(pooladdr + (__pcl * 4)));
		__f._inmethodname = this.__loadUtfString(
			memory.memReadInt(pooladdr + (__pmn * 4)));
		__f._inmethodtype = this.__loadUtfString(
			memory.memReadInt(pooladdr + (__pmt * 4)));
	}
	
	/**
	 * Sets the debug point in the frame.
	 *
	 * @param __f The frame.
	 * @param __sln The source line.
	 * @param __jop The Java operation.
	 * @param __jpc The Java address.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/15
	 */
	private final void __debugPoint(Frame __f, int __sln, int __jop, int __jpc)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		__f._inline = __sln;
		__f._injop = __jop;
		__f._injpc = __jpc;
	}
	
	/**
	 * Loads a UTF string from the given memory address.
	 *
	 * @param __addr The address to read from.
	 * @return The resulting string.
	 * @since 2019/05/15
	 */
	private final String __loadUtfString(int __addr)
	{
		// Read length to figure out how long the string is
		WritableMemory memory = this.memory;
		int strlen = memory.memReadShort(__addr) & 0xFFFF;
		
		// Decode string data
		try (DataInputStream dis = new DataInputStream(
			new ReadableMemoryInputStream(memory, __addr, strlen + 2)))
		{
			return dis.readUTF();
		}
		
		// Could not read string, use some other string form
		catch (IOException e)
		{
			return String.format("??? @%08x (len=%d)", __addr, strlen);
		}
	}
	
	/**
	 * Internal system call handling.
	 *
	 * @param __si System call index.
	 * @param __args Arguments.
	 * @return The result.
	 * @since 2019/05/23
	 */
	private final int __sysCall(short __si, int... __args)
	{
		// Error state for the last call of this type
		int[] errors = this._syscallerrors;
		
		// Return value with error value, to set if any
		int rv,
			err;
		
		// Depends on the system call type
		switch (__si)
		{
				// Check if system call is supported
			case SystemCallIndex.QUERY_INDEX:
				{
					err = 0;
					switch (__args[0])
					{
						case SystemCallIndex.ERROR_GET:
						case SystemCallIndex.ERROR_SET:
						case SystemCallIndex.TIME_LO_MILLI_WALL:
						case SystemCallIndex.TIME_HI_MILLI_WALL:
						case SystemCallIndex.TIME_LO_NANO_MONO:
						case SystemCallIndex.TIME_HI_NANO_MONO:
						case SystemCallIndex.VMI_MEM_FREE:
						case SystemCallIndex.VMI_MEM_USED:
						case SystemCallIndex.VMI_MEM_MAX:
						case SystemCallIndex.PD_OF_STDIN:
						case SystemCallIndex.PD_OF_STDOUT:
						case SystemCallIndex.PD_OF_STDERR:
						case SystemCallIndex.PD_WRITE_BYTE:
							rv = 1;
							break;
						
						default:
							rv = 0;
							break;
					}
				}
				break;
				
				// Get error
			case SystemCallIndex.ERROR_GET:
				{
					// If the ID is valid then a bad array access will be used
					int dx = __args[0];
					if (dx < 0 || dx >= SystemCallIndex.NUM_SYSCALLS)
						dx = SystemCallIndex.QUERY_INDEX;
					
					// Return the stored error code
					synchronized (errors)
					{
						rv = errors[dx];
					}
					
					// Always succeeds
					err = 0;
				}
				break;
				
				// Set error
			case SystemCallIndex.ERROR_SET:
				{
					// If the ID is valid then a bad array access will be used
					int dx = __args[0];
					if (dx < 0 || dx >= SystemCallIndex.NUM_SYSCALLS)
						dx = SystemCallIndex.QUERY_INDEX;
					
					// Return last error code, and set new one
					synchronized (errors)
					{
						rv = errors[dx];
						errors[dx] = __args[0];
					}
					
					// Always succeeds
					err = 0;
				}
				break;
			
				// Current wall clock milliseconds (low).
			case SystemCallIndex.TIME_LO_MILLI_WALL:
				{
					rv = (int)(System.currentTimeMillis());
					err = 0;
				}
				break;

				// Current wall clock milliseconds (high).
			case SystemCallIndex.TIME_HI_MILLI_WALL:
				{
					rv = (int)(System.currentTimeMillis() >>> 32);
					err = 0;
				}
				break;

				// Current monotonic clock nanoseconds (low).
			case SystemCallIndex.TIME_LO_NANO_MONO:
				{
					rv = (int)(System.nanoTime());
					err = 0;
				}
				break;

				// Current monotonic clock nanoseconds (high).
			case SystemCallIndex.TIME_HI_NANO_MONO:
				{
					rv = (int)(System.nanoTime() >>> 32);
					err = 0;
				}
				break;
			
				// VM information: Memory free bytes
			case SystemCallIndex.VMI_MEM_FREE:
				{
					rv = (int)Math.min(Integer.MAX_VALUE,
						Runtime.getRuntime().freeMemory());
					err = 0;
				}
				break;
			
				// VM information: Memory used bytes
			case SystemCallIndex.VMI_MEM_USED:
				{
					rv = (int)Math.min(Integer.MAX_VALUE,
						Runtime.getRuntime().totalMemory());
					err = 0;
				}
				break;
			
				// VM information: Memory max bytes
			case SystemCallIndex.VMI_MEM_MAX:
				{
					rv = (int)Math.min(Integer.MAX_VALUE,
						Runtime.getRuntime().maxMemory());
					err = 0;
				}
				break;
				
				// Pipe descriptor of standard input
			case SystemCallIndex.PD_OF_STDIN:
				{
					rv = 0;
					err = 0;
				}
				break;
				
				// Pipe descriptor of standard output
			case SystemCallIndex.PD_OF_STDOUT:
				{
					rv = 1;
					err = 0;
				}
				break;
				
				// Pipe descriptor of standard error
			case SystemCallIndex.PD_OF_STDERR:
				{
					rv = 2;
					err = 0;
				}
				break;
				
				// Write single byte to PD
			case SystemCallIndex.PD_WRITE_BYTE:
				{
					// Depends on the stream
					int pd = __args[0];
					OutputStream os = (pd == 1 ? System.out :
						(pd == 2 ? System.err : null));
					
					// Write
					if (os != null)
					{
						try
						{
							os.write(__args[1]);
							
							// Okay
							rv = 1;
							err = 0;
						}
						
						// Failed
						catch (IOException e)
						{
							rv = -1;
							err = SystemCallError.PIPE_DESCRIPTOR_BAD_WRITE;
						}
					}
					
					// Failed
					else
					{
						rv = -1;
						err = SystemCallError.PIPE_DESCRIPTOR_INVALID;
					}
				}
				break;
				
			default:
				// Returns no value but sets an error
				rv = -1;
				err = SystemCallError.UNSUPPORTED_SYSTEM_CALL;
				
				// If the ID is valid then a bad array access will be used
				if (__si < 0 || __si >= SystemCallIndex.NUM_SYSCALLS)
					__si = SystemCallIndex.QUERY_INDEX;
				break;
		}
		
		// Set error state as needed
		synchronized (errors)
		{
			errors[__si] = err;
		}
		
		// Use returning value
		return rv;
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
		
		/** The entry PC address. */
		int _entrypc;
		
		/** The PC address for this frame. */
		volatile int _pc;
		
		/** The reference queue positoin. */
		volatile int _rp;
		
		/** Last executed address. */
		int _lastpc;
		
		/** The executing class. */
		String _inclass;
		
		/** The executing method name. */
		String _inmethodname;
		
		/** The executing method type. */
		String _inmethodtype;
		
		/** The current line. */
		int _inline;
		
		/** The current Java operation. */
		int _injop;
		
		/** The current Java address. */
		int _injpc;
	}
}

