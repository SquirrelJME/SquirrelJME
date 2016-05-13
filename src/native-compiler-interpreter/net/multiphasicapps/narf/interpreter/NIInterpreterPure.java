// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.interpreter;

import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.narf.bytecode.NBCByteCode;
import net.multiphasicapps.narf.bytecode.NBCInstructionID;
import net.multiphasicapps.narf.bytecode.NBCOperation;
import net.multiphasicapps.narf.classinterface.NCICodeAttribute;
import net.multiphasicapps.narf.classinterface.NCIClassReference;

/**
 * This is the pure interpreter which takes byte code directly for execution.
 *
 * @since 2016/05/12
 */
public class NIInterpreterPure
	extends NIInterpreter
{
	/** The code to be interpreted. */
	protected final NBCByteCode program;
	
	/** The code attribute used. */
	protected final NCICodeAttribute attribute;
	
	/** Local variable data. */
	private final Object[] _locals;
	
	/** Stack variable data. */
	private final Object[] _stack;
	
	/** The current instruction address. */
	private volatile int _pcaddr;
	
	/** The top of the stack. */
	private volatile int _top;
	
	/**
	 * Initializes the interpreter which uses the direct byte code.
	 *
	 * @param __t The thread of execution.
	 * @param __p The byte code to interpret.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	public NIInterpreterPure(NIThread __t, NBCByteCode __p)
		throws NullPointerException
	{
		super(__t);
		
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.program = __p;
		NCICodeAttribute attr;
		this.attribute = (attr = __p.attribute());
		
		// Setup storage
		this._top = 0;
		this._locals = new Object[attr.maxLocals()];
		this._stack = new Object[attr.maxStack()];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public Object interpret(Object... __args)
	{
		// Setup local variables
		int n = __args.length;
		for (int i = 0; i < n; i++)
			throw new Error("TODO");
		
		// Execution loop
		NBCByteCode program = this.program;
		for (int pcaddr = 0;;)
		{
			// Get the current operation
			NBCOperation op = program.get(pcaddr);
			
			// Debug
			System.err.printf("DEBUG -- Exec: %s%n", op);
			
			// Depends on the instruction code
			int code = op.instructionId();
			switch (code)
			{
					// Allocate new object
				case NBCInstructionID.NEW:
					pcaddr = __new(op);
					break;
					
					// Shuffle entries on the stack
				case NBCInstructionID.SYNTHETIC_STACK_SHUFFLE:
					pcaddr = __stackShuffle(op);
					break;
				
					// {@squirreljme.error AN0q The current operation is not
					// known. (The instruction address; The instruction ID; The
					// operation itself)}
				default:
					throw new NIException(this.core, NIException.Issue.
						ILLEGAL_OPCODE, String.format("AN0q %d %d %s",
						pcaddr, code, op));
			}
			
			// Set the new address
			this._pcaddr = pcaddr;
		}
	}
	
	/**
	 * Allocates a new object.
	 *
	 * @param __op The operation.
	 * @return The next instruction pointer address.
	 * @since 2016/05/13
	 */
	private int __new(NBCOperation __op)
	{
		// Lookup the class
		ClassNameSymbol csn = ((NCIClassReference)__op.arguments().get(0)).
			get();
		NICore core = this.core;
		NIClass cl = core.initClass(csn);
		
		// Allocate object (arrays are allocated to zero length using new)
		NIObject obj = new NIObject(core, cl, 0);
		
		// Set the topmost stack entry to the new object
		int top = _top;
		_stack[top] = obj;
		_top = top + 1;
		
		// Next address
		return __op.address() + 1;
	}
	
	/**
	 * Shuffles items on the stack.
	 *
	 * @param __op The operation.
	 * @return The next address.
	 * @since 2016/05/13
	 */
	private int __stackShuffle(NBCOperation __op)
	{
		if (true)
			throw new Error("TODO");
		
		// Next address
		return __op.address() + 1;
	}
}

