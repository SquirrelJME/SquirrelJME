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
import net.multiphasicapps.squirreljme.linkage.ClassLinkage;
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
	/** The JIT configuration. */
	protected final JITConfig config;
	
	/** The owning class stream. */
	final __JITClassStream__ _classstream;
	
	/** The buffer which contains the native machine code. */
	final ByteDeque _codebuffer =
		new ByteDeque();
	
	/** The instance of the translation engine. */
	final TranslationEngine _engine;
	
	/** Fragment of machine code. */
	final CodeFragmentOutput _fragment;
	
	/** The input state at the start of every instruction. */
	private volatile CacheState _instate;
	
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
	
	/** Has the input been transited? */
	private volatile boolean _transition;
	
	/**
	 * Initializes the code stream.
	 *
	 * @param __c The owning class.
	 * @param __f The fragment where machine code is placed.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/07
	 */
	__JITCodeStream__(__JITClassStream__ __c, CodeFragmentOutput __f)
		throws NullPointerException
	{
		// Check
		if (__c == null || __f == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._classstream = __c;
		this._fragment = __f;
		
		// Setup engine
		TranslationEngine engine = __c.__jit().engineProvider().
			createEngine(this);
		this._engine = engine;
		this.config = engine.<JITConfig>config(JITConfig.class);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/07
	 */
	@Override
	public void atInstruction(int __code, int __pos)
	{
		CacheState instate = this._instate;
		ActiveCacheState outstate = this._outstate;
		
		// Debug
		System.err.printf("DEBUG -- At %d (pos %d)%n", __code, __pos);
		
		// Setup input state if there is a pre-existing state available
		SnapshotCacheStates states = this._states;
		SnapshotCacheState state = states.get(__pos);
		if (state != null)
			((ActiveCacheState)instate).switchFrom(state);
		
		// The output state is always a copy of the input state
		outstate.switchFrom(instate);
		
		// Clear the transition flag
		this._transition = false;
		
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
	 * @since 2017/03/18
	 */
	@Override
	public <F extends CodeFragmentOutput> F codeFragment(
		Class<F> __cl)
		throws ClassCastException, NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		return __cl.cast(_fragment);
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
		CacheState instate = __transitedInput();
		ActiveCacheState outstate = this._outstate;
		
		// Debug
		System.err.printf("DEBUG -- Copy %s %s -> %s%n", __type, __from, __to);
		
		// Forward to primitive copy
		__primitiveCopy(instate, outstate, instate.getSlot(__from),
			outstate.getSlot(__to), true);
		
		// Transit input
		__transitInput();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/23
	 */
	@Override
	public void endInstruction(int __code, int __pos, int __next)
	{
		CacheState instate = this._instate;
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
	 * @since 2017/03/23
	 */
	@Override
	public void endMethod()
	{
		System.err.println("TODO -- Any endMethod() generation.");
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
			slot.setType(__st[i]);
		}
		
		// Assign registers and stack entries 
		ArgumentAllocation[] allocs = this._engine.allocationForEntry(__st);
		StackSlotOffsets stackoffsets = this._stackoffsets;
		for (int i = 0, n = allocs.length; i < n; i++)
		{
			CodeVariable cv = __cv[i];
			ActiveCacheState.Slot slot = outstate.getSlot(cv);
			ArgumentAllocation alloc = allocs[i];
			
			// Assigning registers?
			if (alloc.hasRegisters())
				slot.setRegisters(alloc.registers());
			
			// Set stack position
			else
				stackoffsets.set(cv, alloc.type(), alloc.stackOffset());
		}
		
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
		CacheState instate = __transitedInput();
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
			outstate.getSlot(cv).remove();
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
		
		// Need to determine the allocations for calling methods along with
		// returning any potential values
		TranslationEngine engine = this._engine;
		ArgumentAllocation[] allocs = engine.allocationForEntry(__targs);
		ArgumentAllocation rvalloc = (rv == null ? null :
			engine.allocationForReturn(__rvt));
		
		// Debug
		System.err.printf("DEBUG -- Allocate: %s %s%n", Arrays.asList(allocs),
			rvalloc);
		
		// Save all registers (including argument registers)
		System.err.printf("DEBUG -- Before savetemp: %s%n", outstate);
		__saveTempRegisters(instate, outstate, __d);
		System.err.printf("DEBUG -- After savetemp: %s%n", outstate);
		
		// Shuffle registers and stack elements around so that the values are
		// in the right position for the method call. Also store stack values
		// too.
		boolean[] stacked = new boolean[n];
		for (int i = 0; i < n; i++)
		{
			// Need allocations
			ArgumentAllocation source = instate.getSlot(__cargs[i]).
				valueAllocation();
			ArgumentAllocation target = allocs[i];
			
			// {@squirreljme.error ED0p Inconsistent input state, the slot has
			// no allocation but was specified as an argument to a method.
			// (The slot; The input state)}
			if (source == null)
				throw new IllegalStateException(String.format("ED0p %s %s",
					__cargs[i], instate));
			
			// If the registers are the same, nothing has to be done
			if (source.isRegisterCompatible(target))
				continue;
			
			// Check if the value would be on the stack
			boolean onstack = !source.hasRegisters();
			if (!onstack)
				for (Register r : source.registers())
					if (engine.isRegisterTemporary(r) ||
						engine.isRegisterArgument(r))
					{
						onstack = true;
						break;
					}
			
			// Target is in registers
			if (target.hasRegisters())
			{
				// If the value is on the stack, do a read from it
				if (onstack)
					engine.loadRegister(target.type(), target.registers(),
						source.stackOffset(), engine.stackPointerRegister());
			
				// Otherwise copy the registers
				else
					engine.moveRegister(target.type(), source.registers(),
						target.registers());
			}
			
			// Target is on the stack
			else
				throw new todo.TODO();
		}
		
		// Need registers for usage
		JITConfig config = this.config;
		DataType pointertype = config.pointerDataType();
		int pointerbytes = pointertype.length();
		__JITClassStream__ classstream = this._classstream;
		Register at = engine.assemblerTemporaryRegister(),
			got = engine.globalTableRegister(),
			sp = engine.stackPointerRegister(),
			fp = engine.framePointerRegister();

		// Store old stack things (old GOT, SP, etc.)
		System.err.println("TODO -- Store GOT/SP/etc. state.");
		
		// Load the address of the remote method (where to jump to)
		engine.loadRegister(pointertype, at,
			classstream.__link(__link) * pointerbytes, got);
		
		// The method's class GOT must be loaded
		engine.loadRegister(pointertype, got,
			classstream.__link(__link.tableLink()) * pointerbytes, got);
		
		// Generate invoke of method, the target methods pointer and the class
		// link table must be obtained also
		engine.invokeRegister(at);
			
		// Restore special registers after the call
		System.err.println("TODO -- Restore GOT/SP/etc. state.");
		
		// If a return value is used then handle moving it
		if (__rv != null)
		{
			throw new todo.TODO();
		}
		
		// Because the values are stack cached they do not need to be read from
		// the stack again until they are needed. So if a bunch of local
		// variables were stored but they are not used at all, then there is
		// no need to restore them at all.
		
		// Transit input
		__transitInput();
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
	 * @since 2017/03/24
	 */
	@Override
	public void loadConstant(ClassLinkage __cl, CodeVariable __cv)
		throws NullPointerException
	{
		// Check
		if (__cl == null || __cv == null)
			throw new NullPointerException("NARG");
			
		ActiveCacheState outstate = this._outstate;
		JITConfig config = this.config;
		TranslationEngine engine = this._engine;
		DataType pointertype = config.pointerDataType();
		
		// Need to allocate the target slot for the given type
		ActiveCacheState.Slot outslot = outstate.getSlot(__cv);
		outslot.setType(StackMapType.OBJECT);
		
		// Load value to registers, if the variable is not in registers the
		// value must be temporarily placed in a temporary register
		boolean tr;
		List<Register> target = ((tr = !outslot.thisRegisters().isEmpty()) ?
			outslot.thisRegisters() :
			Arrays.<Register>asList(engine.assemblerTemporaryRegister()));
		
		// Load from the global table
		engine.loadRegister(pointertype, target, pointertype.length() *
			this._classstream.__link(__cl), engine.globalTableRegister());
		
		// Copy that to the stack for the slot
		if (!tr)
			engine.storeRegister(pointertype, target,
				outslot.thisStackOffset(), engine.stackPointerRegister());
		
		// Transit input
		__transitInput();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/23
	 */
	@Override
	public void returnValue(StackMapType __t, CodeVariable __cv)
		throws NullPointerException
	{
		// Check
		if ((__t == null) != (__cv == null))
			throw new NullPointerException("NARG");
		
		// Returning a value, load it to the return register
		TranslationEngine engine = this._engine;
		if (__t != null)
			throw new todo.TODO();
		
		// Restore callee saved values
		System.err.println("TODO -- Restore callee saved values.");
		
		// Generate return instruction
		engine.methodReturn();
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
		this._states = new SnapshotCacheStates(this);
		this._stackoffsets = new StackSlotOffsets(this, __ms, __ml);
		
		// Also input and output states
		Register[] sv = engine.allocationRegisters(true),
			tm = engine.allocationRegisters(false);
		this._instate = new ActiveCacheState(this, __ms, __ml, sv, tm, false);
		this._outstate = new ActiveCacheState(this, __ms, __ml, sv, tm, true);
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
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/04
	 */
	private void __primitiveCopy(CacheState __instate,
		ActiveCacheState __outstate, CacheState.Slot __srcslot,
		ActiveCacheState.Slot __destslot, boolean __doalias)
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
		__destslot.remove();
		
		// Alias this slot to another, note that no value is actually copied
		// and only the type is set in the destination slot (bindings are
		// forwarded)
		// An alias though might point to another alias however.
		if (__doalias)
			__destslot.setAlias(ss, si);
		
		// Copying over a value, replace it
		else
		{
			// Set the type
			__destslot.setType(__srcslot.thisType());
			
			// The target may need registers allocated to it if they
			// for example are not of a compatible type
			if (true)
				throw new todo.TODO();
			
			// Generate opcodes
			throw new todo.TODO();
		}
	}
	
	/**
	 * Saves all registers that are in argument and tempory registers onto
	 * their associated stack positions.
	 *
	 * @param __instate The input state.
	 * @param __outstate The output state.
	 * @param __d The depth of the stack.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/16
	 */
	private void __saveTempRegisters(CacheState __instate,
		ActiveCacheState __outstate, int __d)
		throws NullPointerException
	{
		// Check
		if (__instate == null || __outstate == null)
			throw new NullPointerException("NARG");
		
		// Go through both treads and store them
		TranslationEngine engine = this._engine;
		StackSlotOffsets stackoffsets = this._stackoffsets;
		for (int z = 0; z < 2; z++)
		{
			// Local variables
			ActiveCacheState.Tread tread;
			int limit;
			if (z == 0)
			{
				tread = __outstate.locals();
				limit = tread.size();
			}
			
			// Stack, only go up to the stack depth because everything after
			// that point is going to be destroyed anyway
			else
			{
				tread = __outstate.stack();
				limit = Math.min(tread.size(), __d);
			}
			
			// Check for slots that need saving
			for (int i = 0; i < limit; i++)
			{
				ActiveCacheState.Slot slot = tread.get(i);
				
				// Ignore aliased slots and slots with no value
				StackMapType type = slot.valueType();
				if (slot.value() != slot ||
					type == StackMapType.NOTHING)
					continue;
				
				List<Register> regs = slot.valueRegisters();
				boolean needsave = false;
				for (Register r : regs)
					if (engine.isRegisterTemporary(r) ||
						engine.isRegisterArgument(r))
					{
						needsave = true;
						break;
					}
				
				// Needs to save
				if (needsave)
				{
					// Assign stack slot
					boolean onstack = slot.valueIsStack();
					int index = slot.valueIndex(),
						stackoffset = slot.valueStackOffset();
					
					// Assign the offset?
					if (stackoffset == Integer.MIN_VALUE)
						stackoffset = stackoffsets.assign(onstack, index,
							engine.toDataType(type));
					
					// Debug
					System.err.printf("DEBUG -- Need to save: %s%n", slot);
					
					// Store the slot
					__storeSlot(slot);
					
					// The values are placed on the stack, so their output
					// register set is actually invalid until they are used
					// again. If a stored temporary values
					__outstate.getSlot(onstack, index).clearRegisters();
				}
			}
		}
	}
	
	/**
	 * Stores the specified slot.
	 *
	 * @param __s The slot to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/21
	 */
	private final void __storeSlot(CacheState.Slot __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Store the registers used in the slot
		TranslationEngine engine = this._engine;
		engine.storeRegister(engine.toDataType(__s.valueType()),
			__s.valueRegisters(), __s.valueStackOffset(),
			engine.stackPointerRegister());
	}
	
	/**
	 * Returns the transited input state.
	 *
	 * @return The input cache state.
	 * @since 2017/03/30
	 */
	private final CacheState __transitedInput()
	{
		// Get input and output
		ActiveCacheState input = (ActiveCacheState)this._instate,
			output = this._outstate;
		
		// If transiting set the input from the output
		if (this._transition)
		{
			input.switchFrom(output);
			
			// Clear transition
			this._transition = false;
		}
		
		// Use the input state
		return input;
	}
	
	/**
	 * Marks the flag so that the output state is placed into the input state
	 * for consecutive calls.
	 *
	 * @since 2017/03/30
	 */
	private final void __transitInput()
	{
		this._transition = true;
	}
}

