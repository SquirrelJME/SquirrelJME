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
		
		// If copying from the same
		if (__from.equals(__to))
			return;
		
		// Get active state
		ActiveCacheState instate = this._instate;
		ActiveCacheState outstate = this._outstate;
		
		// Debug
		System.err.printf("DEBUG -- Copy %s %s -> %s%n", __type, __from, __to);
		
		// Forward to primitive copy
		__primitiveCopy(instate, outstate, instate.getSlot(__from),
			outstate.getSlot(__to), true, true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public void endInstruction(int __code, int __pos, int __next)
	{
		ActiveCacheState instate = this._instate;
		ActiveCacheState outstate = this._outstate;
		
		// Debug
		System.err.printf("DEBUG -- End %d (pos %d, next %d)%n", __code,
			__pos, __next);
		System.err.printf("DEBUG -- Exit state: %s%n", outstate);
		
		// Either check or store the exit state to the implicit next target
		if (__next >= 0)
			__checkStoreState(__next, outstate);
		
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
		
		// Get states
		CacheState instate = this._instate;
		ActiveCacheState outstate = this._outstate;
		
		// Go through the arguments and initialize the output state to match
		// what everything should look like once the method invoke has
		// finished
		int n = __cargs.length;
		CacheState.Slot[] args = new CacheState.Slot[n];
		for (int i = 0; i < n; i++)
		{
			// Fill input slot value to pass in the future
			CodeVariable cv = __cargs[i];
			args[i] = instate.getSlot(cv).value();
			
			// Stack elements are destroyed on input
			__removeStackSlot(instate, outstate, cv, __d);
		}
		
		// No return value
		ActiveCacheState.Slot rv;
		if (__rv == null)
			rv = null;
		
		// Setup output return value
		else
		{
			// Set the output type
			rv = outstate.getSlot(__rv);
			rv.setType(__rvt);
		}
		
		// Forward invoke
		this._engine.invokeMethod(instate, outstate, __link, rv, args);
		
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
		
		// Report sizes for potential stack slots
		engine.slotCount(__ms, __ml);
	}
	
	/**
	 * If the given address already has a stored state then it will be checked
	 * to see if it is compatible. If it is not in the states, it is just
	 * stored.
	 *
	 * @param __next The instruction to check and store the state of.
	 * @param __out The state to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/06
	 */
	private void __checkStoreState(int __next, CacheState __out)
		throws NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// If no state has been set, store it
		SnapshotCacheStates states = this._states;
		if (!states.contains(__next))
			states.set(__next, __out);
		
		// Otherwise check that the state is consistent
		else
			throw new todo.TODO();
	}
	
	/**
	 * Performs a primitive copy operation of one value to another.
	 *
	 * @param __instate The input state.
	 * @param __outstate The output state.
	 * @param __srcslot The source slot to copy from.
	 * @param __destslot The destination slot to copy to.
	 * @param __doalias If {@code true} then aliasing is permitted.
	 * @param __dogenop If {@code true} then generating opcodes is permitted.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/04
	 */
	private void __primitiveCopy(CacheState __instate,
		ActiveCacheState __outstate, CacheState.Slot __srcslot,
		ActiveCacheState.Slot __destslot, boolean __doalias, boolean __dogenop)
		throws NullPointerException
	{
		// Check
		if (__instate == null || __outstate == null || __srcslot == null ||
			__destslot == null)
			throw new NullPointerException("NARG");
		
		// Debug
		System.err.printf("DEBUG -- primitiveCopy: %s -> %s%n", __srcslot,
			__destslot);
		
		// If the destination is a local variable then make it so the value is
		// actually copied (because locals persist in exception handlers). As
		// such this makes the JIT design a bit simpler at the cost of some
		// copies. Locals are never aliased.
		if (__destslot.isLocal())
			__doalias = false;
		
		// Aliasing one value to another
		if (__doalias)
		{
			// Set the type, do not modify the binding because the binding of
			// the slot uses the binding of what it is aliased to.
			__destslot.setType(__srcslot.type(), false);
			
			// Alias
			__destslot.setAlias(__srcslot.isStack(), __srcslot.index());
		}
		
		// Copying over a value, replace it
		else
		{
			// Set the type from the source type
			__destslot.clearAlias();
			__destslot.setType(__srcslot.type(), true);
			
			// Generate operations for the copy
			if (__dogenop)
				throw new todo.TODO();
		}
	}
	
	/**
	 * This removes the the specified information in the given stack position.
	 * This clears any state
	 *
	 * @param __is Input state.
	 * @param __os Output state.
	 * @param __cv The variable to be removed, must be on the stack.
	 * @param __d The depth of the stack, used to not copy values that are
	 * going to be removed anyway.
	 * @throws IllegalArgumentException If the input variable is a local
	 * variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/06
	 */
	private void __removeStackSlot(CacheState __is, ActiveCacheState __os,
		CodeVariable __cv, int __d)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__is == null || __os == null || __cv == null)
			throw new IllegalArgumentException("NARG");
		
		// {@squirreljme.error ED0g Cannot remove local variable on the stack.
		// (The variable to remove)}
		if (__cv.isLocal())
			throw new IllegalArgumentException(String.format("ED0g %s", __cv));
		
		// Go through the stack and check for any items which are aliased to
		// this stack entry, if they are perform a non-aliased copy and
		// generate code for it
		ActiveCacheState.Tread stack = __os.stack();
		int n = stack.size(), dest = __cv.id();
		for (int i = 0; i < __d; i++)
		{
			// Ignore empty values
			ActiveCacheState.Slot slot = stack.get(i);
			if (slot.type() == StackMapType.NOTHING)
				continue;
			
			// Copy the value from the other stack entry to this one so that
			// it is no longer aliased (its value is to be destroyed)
			ActiveCacheState.Slot alias = slot.alias();
			if (alias != null && alias.isStack() && alias.index() == dest)
				__primitiveCopy(__is, __os, __is.getSlot(true, i),
					slot, false, true);
		}
		
		// Away it goes!
		ActiveCacheState.Slot gone = __os.getSlot(__cv);
		gone.clearAlias();
		gone.setType(StackMapType.NOTHING);
	}
}

