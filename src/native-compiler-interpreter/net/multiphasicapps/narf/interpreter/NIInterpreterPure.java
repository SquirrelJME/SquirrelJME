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
		_locals = new Object[attr.maxLocals()];
		_stack = new Object[attr.maxStack()];
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
		for (;;)
		{
			// Get the current operation
			int pcaddr = this._pcaddr;
			NBCOperation op = program.get(pcaddr);
			
			// Depends on the instruction code
			int code = op.instructionId();
			switch (code)
			{
					// Allocate new object
				case NBCInstructionID.NEW:
					__new(op);
					break;
				
					// {@squirreljme.error AN0q The current operation is not
					// known. (The instruction address; The instruction ID; The
					// operation itself)}
				default:
					throw new NIException(this.core, NIException.Issue.
						ILLEGAL_OPCODE, String.format("AN0q %d %d %s",
						pcaddr, code, op));
			}
		}
	}
	
	/**
	 * Allocates a new object.
	 *
	 * @param __op The operation.
	 * @since 2016/05/13
	 */
	private void __new(NBCOperation __op)
	{
		// Lookup the class
		ClassNameSymbol csn = ((NCIClassReference)__op.arguments().get(0)).
			get();
		NICore core = this.core;
		NIClass cl = core.initClass(csn);
		
		throw new Error("TODO");
	}
}

