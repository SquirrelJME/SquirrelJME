// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import net.multiphasicapps.classfile.ByteCode;

/**
 * This class goes through the byte code for a method and then performs stack
 * and instruction work on it.
 *
 * @since 2019/04/06
 */
public final class ByteCodeProcessor
{
	/** The input byte code to be read. */
	protected final ByteCode bytecode;
	
	/** Handle for byte-code operations. */
	protected final ByteCodeHandler handler;
	
	/** The state of the byte code. */
	protected final ByteCodeState state;
	
	/**
	 * Initializes the byte code processor.
	 *
	 * @param __bc The target byte code.
	 * @param __h Handler for the byte code operations.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	public ByteCodeProcessor(ByteCode __bc, ByteCodeHandler __h)
		throws NullPointerException
	{
		if (__bc == null || __h == null)
			throw new NullPointerException("NARG");
		
		this.bytecode = __bc;
		this.handler = __h;
		
		// The state is used to share between the processor and the handler
		ByteCodeState state = __h.state();
		this.state = state;
		
		// Load initial Java stack state from the initial stack map
		JavaStackState s;
		state.stack = (s = JavaStackState.of(__bc.stackMapTable().get(0),
			__bc.writtenLocals()));
		state.stacks.put(0, s);
	}
	
	/**
	 * Processes the byte code and calls the destination handler.
	 *
	 * @since 2019/04/06
	 */
	public final void process()
	{
		ByteCode bytecode = this.bytecode;
		ByteCodeState state = this.state;
		Map<Integer, JavaStackState> stacks = this._stacks;
		
		throw new todo.TODO();
	}
}

