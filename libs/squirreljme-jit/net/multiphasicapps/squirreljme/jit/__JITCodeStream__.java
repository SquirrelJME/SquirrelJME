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
	
	/** Stack slot offsets. */
	private volatile StackSlotOffsets _stackoffsets;
	
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
		__checkStoreState(0, outstate);
		
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
			__removeSlot(instate, outstate, outstate.getSlot(cv), true,
				__d);
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
	 * @since 2017/03/11
	 */
	@Override
	public StackSlotOffsets stackSlotOffsets()
	{
		return this._stackoffsets;
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
		this._stackoffsets = new StackSlotOffsets(engine, __ms, __ml);
		
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
		
		// {@squirreljme.error ED0h Cannot copy from a slot which has no
		// value.}
		if (__srcslot.valueType() == StackMapType.NOTHING)
			throw new JITException("ED0h");
		
		// If the destination is a local variable then make it so the value is
		// actually copied (because locals persist in exception handlers). As
		// such this makes the JIT design a bit simpler at the cost of some
		// copies. Locals are never aliased.
		if (__destslot.thisIsLocal())
			__doalias = false;
		
		// The source slot to be copied, the source is always derived from its
		// own alias
		boolean ss = __srcslot.valueIsStack(),
			ds = __destslot.thisIsStack();
		int si = __srcslot.valueIndex(),
			di = __destslot.thisIndex();
		
		// Alias/copy to self, do nothing
		if (ss == ds && si == di)
			return;
		
		// Go through the target stack, any stack slots which alias to the
		// replaced slot
		// This is always done because the destination slot will always be
		// kludged
		__removeSlot(__instate, __outstate, __destslot, __dogenop,
			Integer.MAX_VALUE);
		
		// The destination slot always gets the type associated with it
		__destslot.setType(__srcslot.thisType(), !__doalias);
		
		// Alias this slot to another, note that no value is actually copied
		// and only the type is set in the destination slot (bindings are
		// forwarded)
		// An alias though might point to another alias however.
		if (__doalias)
			__destslot.setAlias(ss, si);
		
		// Copying over a value, replace it
		else
		{
			// The target slot might have an alias, after it is replaced it
			// should never have one
			__destslot.clearAlias();
			
			// Generate operations for the copy
			if (__dogenop)
			{
				// The target may need registers allocated to it if they
				// for example are not of a compatible type
				if (true)
					throw new todo.TODO();
				
				// Generate opcodes
				throw new todo.TODO();
			}
		}
	}
	
	/**
	 * This removes the the specified information in the given stack position.
	 * This clears any state
	 *
	 * @param __instate Input state.
	 * @param __outstate Output state.
	 * @param __slot The target slot to be destroyed.
	 * @param __dogenop If {@code true} then machine code will be generated
	 * when de-aliasing values.
	 * @param __sl The limit of the stack, used to not copy values that are
	 * going to be removed anyway. Any slots which are aliased after this point
	 * will not be re-associated if they alias earlier values.
	 * @throws IllegalArgumentException If the input variable is a local
	 * variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/06
	 */
	private void __removeSlot(CacheState __instate,
		ActiveCacheState __outstate, ActiveCacheState.Slot __slot,
		boolean __dogenop, int __sl)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__instate == null || __outstate == null || __slot == null)
			throw new IllegalArgumentException("NARG");
		
		// If the destination slot has no value then nothing needs to be done
		if (__slot.valueType() == StackMapType.NOTHING)
			return;
		
		// Only refer to the current slot and not any aliased one
		boolean ss = __slot.thisIsStack();
		int si = __slot.thisIndex();
		
		// Messing with the stack is only needed if this value is on the
		// stack
		if (__slot.thisIsStack())
		{
			// Go through the stack and remove any aliases to the target
			ActiveCacheState.Tread stack = __outstate.stack();
			int limit = Math.min(__sl, stack.size());
			for (int i = 0; i < limit; i++)
			{
				ActiveCacheState.Slot slot = stack.get(i);
				boolean xs = slot.valueIsStack();
				int xi = slot.valueIndex();
			
				// Ignore the source slot
				if (xs == ss && xi == si)
					continue;
			
				// If the target slot has no tyoe then it cannot get aliases to
				// it clobbered
				if (slot.valueType() == StackMapType.NOTHING)
					continue;
			
				// This slot aliases the slot to be removed, it must get the
				// value copied to it before this actually happens
				if (true)
					throw new todo.TODO();
		
				// Generate operations to copy the data
				if (__dogenop)
					throw new todo.TODO();
			}
		}
		
		// Clear information in the target slot
		__slot.clearAlias();
		__slot.setType(StackMapType.NOTHING);
		__slot.clearRegisters();
	}
}

