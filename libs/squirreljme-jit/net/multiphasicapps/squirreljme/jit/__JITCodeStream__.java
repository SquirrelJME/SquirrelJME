// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.squirreljme.classformat.CodeDescriptionStream;
import net.multiphasicapps.squirreljme.classformat.CodeVariable;
import net.multiphasicapps.squirreljme.classformat.ExceptionHandlerTable;
import net.multiphasicapps.squirreljme.classformat.StackMapType;
import net.multiphasicapps.squirreljme.linkage.Linkage;
import net.multiphasicapps.squirreljme.linkage.MethodLinkage;
import net.multiphasicapps.util.datadeque.ByteDeque;

/**
 * This is used to handle input Java byte code and translate it into native
 * machine via the JIT.
 *
 * @since 2017/02/07
 */
class __JITCodeStream__
	implements CodeDescriptionStream, JITStateAccessor
{
	/** The owning class stream. */
	final __JITClassStream__ _classstream;
	
	/** The buffer which contains the native machine code. */
	final ByteDeque _codebuffer =
		new ByteDeque();
	
	/** The instance of the translation engine. */
	final TranslationEngine _engine;
	
	/** The input state at the start of every instruction. */
	private volatile ActiveCacheState _instate;
	
	/** The output state at the end of every instruction. */
	private volatile ActiveCacheState _outstate;
	
	/** The state of stack and locals for most instruction addresses. */
	private volatile SnapshotCacheStates _states;
	
	/** Jump targets in the code, where state transfers occur. */
	private volatile int[] _jumptargets;
	
	/** The exception handler table. */
	private volatile ExceptionHandlerTable _exceptions;
	
	/**
	 * Initializes the code stream.
	 *
	 * @param __c The owning class.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/07
	 */
	__JITCodeStream__(__JITClassStream__ __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._classstream = __c;
		
		// Setup engine
		TranslationEngine engine = __c.__jit().engineProvider().
			createEngine(this);
		this._engine = engine;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/07
	 */
	@Override
	public void atInstruction(int __code, int __pos)
	{
		ActiveCacheState instate = this._instate;
		ActiveCacheState outstate = this._outstate;
		
		// Debug
		System.err.printf("DEBUG -- At %d (pos %d)%n", __code, __pos);
		
		// Setup input state if there is a pre-existing state available
		SnapshotCacheStates states = this._states;
		SnapshotCacheState state = states.get(__pos);
		if (state != null)
			instate.switchFrom(state);
		
		// The output state is always a copy of the input state
		outstate.switchFrom(instate);
		
		// Debug
		System.err.printf("DEBUG -- Enter state: %s%n", instate);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/16
	 */
	@Override
	public SnapshotCacheStates cacheStates()
	{
		return this._states;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/07
	 */
	@Override
	public void codeLength(int __n)
	{
		// Not used
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/07
	 */
	@Override
	public void copy(StackMapType __type, CodeVariable __from,
		CodeVariable __to)
		throws NullPointerException
	{
		// Check
		if (__type == null || __from == null || __to == null)
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("DEBUG -- Copy %s %s -> %s%n", __type, __from, __to);
		
		// If the destination is a local variable then make it so the value is
		// actually copied (because locals persist in exception handlers). As
		// such this makes the JIT design a bit simpler at the cost of some
		// copies.
		ActiveCacheState activestate = this._instate;
		if (__to.isLocal())
		{
			// Since locals are always written to, instructions must be
			// generated
			throw new todo.TODO();
		}
	
		// Otherwise for stack targets, just alias them to local variables
		// because these will for the most part be temporaries
		else
		{
			// There might be an alias between stack entries
			ActiveCacheState.Slot to = activestate.getSlot(__to);
			__deAliasStack(to);
			
			// Generate alias
			to.setAlias(__from);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public void endInstruction(int __code, int __pos)
	{
		ActiveCacheState instate = this._instate;
		ActiveCacheState outstate = this._outstate;
		
		// Debug
		System.err.printf("DEBUG -- End %d (pos %d)%n", __code, __pos);
		System.err.printf("DEBUG -- Exit state: %s%n", outstate);
		
		// Handle exceptional jump targets, check their state
		ExceptionHandlerTable exceptions = this._exceptions;
		if (exceptions != null)
			throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 201702/09
	 */
	@Override
	public void exceptionTable(ExceptionHandlerTable __eht)
		throws NullPointerException
	{
		// Check
		if (__eht == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._exceptions = __eht;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/08
	 */
	@Override
	public void initialArguments(CodeVariable[] __cv,
		StackMapType[] __st, int __sh)
		throws NullPointerException
	{
		// Check
		if (__cv == null || __st == null)
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("DEBUG -- initArgs: %s %s %d%n", Arrays.asList(__cv),
			Arrays.asList(__st), __sh);
		
		// Get active state
		ActiveCacheState outstate = this._outstate;
		
		// Load variables into the state
		for (int i = 0, n = __cv.length; i < n; i++)
		{
			CodeVariable v = __cv[i];
			int id = v.id();
			
			// {@squirreljme.error ED08 Initial method arguments placed on the
			// stack is not supported, the initial state must only have local
			// variables used.}
			if (!v.isLocal())
				throw new JITException("ED08");
			
			// Get slot for the entry
			ActiveCacheState.Slot slot = outstate.getSlot(v);
			
			// Set slot type
			slot.setType(__st[i], false);
		}
		
		// Setup native bindings
		this._engine.bindStateForEntry(outstate);
		
		// Set entry point state
		this._states.set(0, outstate);
		
		// Debug
		System.err.printf("DEBUG -- initArgs: %s%n", outstate);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/07
	 */
	@Override
	public void invokeMethod(MethodLinkage __link, int __d,
		CodeVariable __rv, StackMapType __rvt, CodeVariable[] __cargs,
		StackMapType[] __targs)
		throws NullPointerException
	{
		// Check
		if (__link == null || __cargs == null || __targs == null ||
			((__rv == null) != (__rvt == null)))
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("DEBUG -- Invoke %s rv=%s/%s args=%s/%s%n", __link,
			__rv, __rvt, Arrays.asList(__cargs), Arrays.asList(__targs));
		
		// Get active state
		ActiveCacheState instate = this._instate;
		ActiveCacheState outstate = this._outstate;
		
		throw new todo.TODO();
		
		/*
		// Get the actual slots which arguments point to, since everything is
		// cached there are likely to not be values on the stack.
		int n = __cargs.length;
		ActiveCacheState activestate = this._activestate;
		ActiveCacheState.Slot[] stackslots = new ActiveCacheState.Slot[n];
		ActiveCacheState.Slot[] slots = new ActiveCacheState.Slot[n];
		for (int i = 0; i < n; i++)
		{
			// However not all slots will be cached, so those must not use
			// null aliases
			ActiveCacheState.Slot origin = activestate.getSlot(__cargs[i]);
			ActiveCacheState.Slot cached = origin.alias();
			slots[i] = (cached != null ? cached : origin);
			
			// The stack slots need to be destroyed later
			stackslots[i] = origin;
		}
		
		// Return value slots are not stack cached and are truly stored on
		// the stack
		ActiveCacheState.Slot rvs = (__rv == null ? null :
			activestate.getSlot(__rv));
		
		// Debug
		System.err.printf("DEBUG -- Actual slots: %s %s%n",
			rvs, Arrays.asList(slots));
		
		// De-alias stack slots
		__deAliasStack(slots);
		
		// Invoke
		this._engine.invokeMethod(__link, rvs, slots);
		
		// If there is a return value set up the type
		if (rvs != null)
		{
			// This will set the state for the next instruction
			rvs.setType(__rvt);
			
			// Return values are never aliased
			rvs.clearAlias();
		}
		
		// Destroy the stack so their bindings are not valid
		__destroyStack((__rv != null ? 1 : 0), stackslots);
		*/
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/07
	 */
	@Override
	public void jumpTargets(int[] __t)
		throws NullPointerException
	{
		// Set
		this._jumptargets = __t.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/02
	 */
	@Override
	public int link(Linkage __l)
		throws NullPointerException
	{
		return this._classstream.__link(__l);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/07
	 */
	@Override
	public void variableCounts(int __ms, int __ml)
	{
		// Initilaize cache states, this is needed for stack caching to work
		// properly along with restoring or merging into state of another
		// instruction
		TranslationEngine engine = this._engine;
		this._states = new SnapshotCacheStates(engine);
		
		// Also input and output states
		this._instate = new ActiveCacheState(engine, __ms, __ml);
		this._outstate = new ActiveCacheState(engine, __ms, __ml);
	}
	
	/**
	 * This de-aliases the specified stack value slots so that if any values
	 * are aliased to stack entries they will be unsplit and moved.
	 *
	 * @param __s Slots to de-alias.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/03
	 */
	private void __deAliasStack(ActiveCacheState.Slot... __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * For any slot which exists on the stack, the associated bindings will
	 * be destroyed.
	 *
	 * @param __o The starting offset into the array.
	 * @param __s The slots to destroy on the stack.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/03
	 */
	private void __destroyStack(int __o, ActiveCacheState.Slot... __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

