// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Instruction;
import net.multiphasicapps.classfile.InstructionIndex;

/**
 * A worker which runs the actual thread code in single-step fashion.
 *
 * @since 2018/09/03
 */
public final class SpringThreadWorker
	implements Runnable
{
	/** The owning machine. */
	protected final SpringMachine machine;
	
	/** The thread being run. */
	protected final SpringThread thread;
	
	/**
	 * Initialize the worker.
	 *
	 * @param __m The executing machine.
	 * @param __t The running thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	public SpringThreadWorker(SpringMachine __m, SpringThread __t)
		throws NullPointerException
	{
		if (__m == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.machine = __m;
		this.thread = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/03
	 */
	@Override
	public final void run()
	{
		// The thread is alive as long as there are still frames of
		// execution
		SpringThread thread = this.thread;
		while (thread.numFrames() > 0)
		{
			// Single step executing the top frame
			this.__singleStep();
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * Single step through handling a single instruction.
	 *
	 * @since 2018/09/03
	 */
	private final void __singleStep()
	{
		// Need the current frame and its byte code
		SpringThread thread = this.thread;
		SpringThread.Frame frame = thread.currentFrame();
		ByteCode code = frame.byteCode();
		
		// Determine the current instruction of execution
		int pc = frame.pc();
		Instruction inst = code.getByAddress(pc);
		
		// Debug
		todo.DEBUG.note("step(%s) -> %s", thread.name(), inst);
		
		// Used to give a more detailed idea of anything that happens since
		// sub-calls will lose any and all exception types
		int nextpc = code.addressFollowing(pc),
			opid;
		try
		{
			// Handle it
			switch ((opid = inst.operation()))
			{
					// Do absolutely nothing!
				case InstructionIndex.NOP:
					break;
					
					// Load from local variables
				case InstructionIndex.ALOAD_0:
				case InstructionIndex.ALOAD_1:
				case InstructionIndex.ALOAD_2:
				case InstructionIndex.ALOAD_3:
					frame.loadToStack(SpringObject.class,
						opid - InstructionIndex.ALOAD_0);
					break;
					
					// {@squirreljme.error BK0a Unimplemented operation.
					// (The instruction)}
				default:
					throw new SpringVirtualMachineException(String.format(
						"BK0a %s", inst));
			}
		}
		
		// Use the original exception, just add a suppression note on it since
		// that is the simplest action
		catch (SpringException e)
		{
			// Where is this located?
			SpringMethod inmethod = frame.method();
			ClassName inclassname = inmethod.inClass();
			SpringClass inclass = machine.classLoader().loadClass(
				inclassname);
			
			// Location information if debugging is used, this makes it easier
			// to see exactly where failed code happened
			String onfile = inclass.file().sourceFile();
			int online = code.lineOfAddress(pc);
			
			// {@squirreljme.error BK0d An exception was thrown in the virtual
			// machine while executing the specified location. (The class;
			// The method; The program counter; The file in source code,
			// null means it is unknown; The line in source code, negative
			// values are unknown; The instruction)}
			e.addSuppressed(new SpringVirtualMachineException(
				String.format("BK0d %s %s %d %s %d %s", inclassname,
				inmethod.nameAndType(), pc, onfile, online, inst)));
			throw e;
		}
		
		// Set next PC address
		frame.setPc(nextpc);
	}
}

