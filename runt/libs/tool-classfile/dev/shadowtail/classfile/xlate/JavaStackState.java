// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import dev.shadowtail.classfile.nncc.NativeCode;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.classfile.StackMapTableEntry;
import net.multiphasicapps.classfile.StackMapTableState;

/**
 * This class contains the state of the Java stack, it is mostly used in
 * the generation of the register code as it handles caching as well.
 *
 * This class is immutable.
 *
 * @since 2019/03/30
 */
public final class JavaStackState
{
	/** The base register where arguments start. */
	public static final int BASE_REGISTER =
		NativeCode.ARGUMENT_REGISTER_BASE;
	
	/** The top of the stack. */
	public final int stacktop;
	
	/** Number of used registers. */
	public final int usedregisters;
	
	/** The local variables defined. */
	private final Info[] _locals;
	
	/** The stack variables. */
	private final Info[] _stack;
	
	/** String representation. */
	private Reference<String> _string;
	
	/** Hash code. */
	private int _hash;
	
	/**
	 * Initializes the stack state, the state will be modified to ensure that
	 * it is correct for normalization purposes.
	 *
	 * @param __l The locals.
	 * @param __s The stack.
	 * @param __ss The top of the stack.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/31
	 */
	public JavaStackState(Info[] __l, Info[] __s, int __ss)
		throws NullPointerException
	{
		if (__l == null || __s == null)
			throw new NullPointerException("NARG");
		
		for (Info i : (__l = __l.clone()))
			if (i == null)
				throw new NullPointerException("NARG");
		
		for (Info i : (__s = __s.clone()))
			if (i == null)
				throw new NullPointerException("NARG");
		
		// Make sure locals are correct
		for (int i = 0, n = __l.length; i < n; i++)
		{
			Info x = __l[i];
			
			// Checks if there is something here
			if (!x.type.isNothing())
			{
				// {@squirreljme.error JC2z Local variables cannot be an alias
				// of another variable. (The local)}
				if (x.value != x.register)
					throw new InvalidClassFormatException("JC2z " + x);
			}
		}
		
		// Correct pre-stack entries?
		for (int i = 0; i < __ss; i++)
		{
			Info x = __s[i];
			if (x.readonly)
				__s[i] = (x = new Info(x.register, x.type, x.value, false,
					x.nocounting));
			
			// Checks if there is something here
			if (!x.type.isNothing())
			{
				// {@squirreljme.error JC30 Stack variables cannot alias
				// variables at higher indexes. (The stack variable)}
				if (x.value > x.register)
					throw new InvalidClassFormatException("JC30 " + x);
			}
		}
		
		// Correct post-stack entries
		for (int i = __ss, n = __s.length; i < n; i++)
		{
			Info x = __s[i];
			if (!x.type.isNothing() || x.value != -1 || x.readonly ||
				x.nocounting)
				__s[i] = (x = new Info(x.register, JavaType.NOTHING, -1, false,
					false));
		}
		
		// Set
		this._locals = __l;
		this._stack = __s;
		this.stacktop = __ss;
		
		// Determine used registers
		int usedregisters = 0;
		for (Info i : __l)
			usedregisters = Math.max(usedregisters, i.register + 1);
		for (Info i : __s)
			usedregisters = Math.max(usedregisters, i.register + 1);
		this.usedregisters = usedregisters + 1;
	}
	
	/**
	 * Compare two stacks and returns a list of registers which have a
	 * compatible or transferable type but collide in their cached value. This
	 * does not check local variables, only stack entries.
	 *
	 * @param __ts The target stack.
	 * @return A list of registers which collide.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/13
	 */
	public final JavaStackEnqueueList cacheCollision(JavaStackState __ts)
		throws NullPointerException
	{
		if (__ts == null)
			throw new NullPointerException("NARG");
		
		// If the two stacks are e
		if (this.equals(__ts))
			return new JavaStackEnqueueList(0);
			
		// {@squirreljme.error JC3k A collision cannot be made where the
		// length of the stack differs. (The length of the source stack; The
		// length of the target stack)}
		int atop = this.stacktop,
			btop = __ts.stacktop;
		if (atop != btop)
			throw new InvalidClassFormatException("JC3k " + atop + " " + btop);
		
		// Registers which collide
		List<Integer> collides = new ArrayList<>();
		
		// Go through and determine which stack entries collide
		Info[] astk = this._stack,
			bstk = __ts._stack;
		for (int i = 0; i < atop; i++)
		{
			Info a = astk[i],
				b = bstk[i];
			
			// Entries which are the same would never collide
			if (a.equals(b))
				continue;
			
			// If the target type is nothing then there is no collision
			// since the source can be dropped
			JavaType at = a.type,
				bt = b.type;
			if (bt.isNothing())
				continue;
				
			// {@squirreljme.error JC3l A collision cannot be found
			// to the target type because the types are not compatible.
			// (The source; The target)}
			if (at.isObject() != bt.isObject() ||
				(!at.isObject() && !at.equals(bt)))
				throw new InvalidClassFormatException(
					"JC3l " + a + " " + b);
			
			// The target entry is cached, but it has a value which does not
			// map to this register
			if (b.value != b.register && a.register != b.value)
				collides.add(b.register);
		}
		
		return new JavaStackEnqueueList(0, collides);
	}
	
	/**
	 * Returns the state which would be used if the specified registers were
	 * to have their caches cleared. Only the stack is considered as locals
	 * are never cached.
	 *
	 * @param __enq The registers to clear.
	 * @return The resulting stack.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/13
	 */
	public final JavaStackState cacheClearState(JavaStackEnqueueList __enq)
		throws NullPointerException
	{
		if (__enq == null)
			throw new NullPointerException("NARG");
		
		// If there is nothing to clear 
		if (__enq.isEmpty())
			return this;
		
		// Clear cache from these
		Info[] stack = this._stack.clone();
		int stacktop = this.stacktop;
		
		// Remove any cached slots
		for (int i = 0; i < stacktop; i++)
		{
			Info inf = stack[i];
			
			// This is to be uncached
			if (inf.value != inf.register && __enq.contains(inf.register))
				stack[i] = new Info(inf.register, inf.type, inf.register,
					false, inf.nocounting);
		}
		
		// Build it
		return new JavaStackState(this._locals, stack, stacktop);
	}
	
	/**
	 * Checks if transition can be made to the other state.
	 *
	 * @param __ts The state to check transition to.
	 * @return If it can transition.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/13
	 */
	public final boolean canTransition(JavaStackState __ts)
		throws NullPointerException
	{
		if (__ts == null)
			throw new NullPointerException("NARG");
		
		// Transition is possible if there are no collisions
		return this.cacheCollision(__ts).isEmpty();
	}
	
	/**
	 * Performs a flush of the entire state removing all cached values.
	 *
	 * @return The result of the cache flush.
	 * @since 2019/04/11
	 */
	public final JavaStackResult doCacheFlush()
	{
		// This is just a transition to the non-cached state
		return this.doTransition(this.nonCached());
	}
	
	/**
	 * Destroys all local variables and stack variables returning the process
	 * that is needed to clear out the entire state.
	 *
	 * Any references that need to be cleared when the code completes will be
	 * placed in the enqueue list.
	 *
	 * @param __rv If true then a return value will be popped before everything
	 * is destroyed, this will be the single input available.
	 * @since 2019/03/30
	 */
	public final JavaStackResult doDestroy(boolean __rv)
	{
		Info inf;
		Info[] locals = this._locals,
			stack = this._stack;
		int stacktop = this.stacktop;
		
		// Setup new locals
		Info[] newlocals = locals.clone();
		
		// Find locals to enqueue
		List<Integer> enqueue = new ArrayList<>();
		List<StateOperation> sops = new ArrayList<>();
		for (int i = 0, n = locals.length; i < n; i++)
		{
			inf = locals[i];
			
			// Enqueue?
			if (inf.canEnqueue())
				enqueue.add(inf.value);
			
			// Clear out
			newlocals[i] = inf.newTypeValue(JavaType.NOTHING, -1, false);
		}
		
		// Return value?
		List<JavaStackResult.InputOutput> io = new ArrayList<>();
		int newstacktop = stacktop;
		if (__rv)
		{
			// Pop top item
			Info popped = stack[--newstacktop];
			if (popped.type.isWide())
				popped = stack[--newstacktop];
			
			// Add to input
			io.add(JavaStackResult.makeInput(popped));
		}
		
		// Enqueue stack items, they do not need clearing out because setting
		// a limiting top will auto-clear
		// If returning a value do not enqueue what is being returned
		// otherwise it might end up being garbage collected and returned
		int eqss = enqueue.size();
		for (int i = 0; i < newstacktop; i++)
		{
			inf = stack[i];
			
			if (inf.canEnqueue())
				enqueue.add(inf.value);
		}
		
		// Create result
		return new JavaStackResult(this,
			new JavaStackState(newlocals, stack, 0),
			new JavaStackEnqueueList(eqss, enqueue),
			new StateOperations(sops),
			io.<JavaStackResult.InputOutput>toArray(
				new JavaStackResult.InputOutput[io.size()]));
	}
	
	/**
	 * Removes all stack variables and places a single entry on the stack
	 * for exception handling.
	 *
	 * The input is filled with the entire stack. The output is a single
	 * entry and contains the stack entry to place the value at. The single
	 * stack entry for the exception will never be cached to the exception
	 * register.
	 *
	 * @param __t The type to push.
	 * @since 2019/04/13
	 */
	public final JavaStackResult doExceptionHandler()
	{
		return this.doExceptionHandler(JavaType.THROWABLE);
	}
	
	/**
	 * Removes all stack variables and places a single entry on the stack
	 * for exception handling.
	 *
	 * The input is filled with the entire stack. The output is a single
	 * entry and contains the stack entry to place the value at. The single
	 * stack entry for the exception will never be cached to the exception
	 * register.
	 *
	 * @param __t The type to push.
	 * @return The result of the operation.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	public final JavaStackResult doExceptionHandler(JavaType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Enqueues and operations
		List<Integer> enq = new ArrayList<>();
		List<StateOperation> ops = new ArrayList<>();
		
		// Setup new stack
		int oldstacktop = this.stacktop;
		Info[] newstack = this._stack.clone();
		
		// Cleanup everything on the stack!
		for (int i = 0; i < oldstacktop; i++)
		{
			Info sit = newstack[i];
			
			// If there are objects here, then uncount them
			if (sit.canEnqueue())
			{
				enq.add(sit.value);
				ops.add(StateOperation.uncount(sit.value));
			}
		}
		
		// Setup new stack entry
		Info olddest = newstack[0],
			dest = newstack[0].newTypeValue(__t, olddest.register, false);
		newstack[0] = dest;
		
		// Build result, only stack items were enqueued so all entries are
		// stack entries
		return new JavaStackResult(this,
			new JavaStackState(this._locals, newstack, 1),
			new JavaStackEnqueueList(0, enq),
			new StateOperations(ops),
			JavaStackResult.makeOutput(dest));
	}
	
	/**
	 * Loads the specified local variable onto the stack.
	 *
	 * @param __i The local to load from.
	 * @return The result of the operation.
	 * @throws InvalidClassFormatException If the local is not valid or
	 * the stack overflows.
	 * @since 2019/03/30
	 */
	public final JavaStackResult doLocalLoad(int __i)
		throws InvalidClassFormatException
	{
		// {@squirreljme.error JC2t Load of local with no value.}
		Info from = this._locals[__i];
		if (from.type.isNothing())
			throw new InvalidClassFormatException("JC2t");
		
		// Space needed to be used on the stack
		JavaType type = from.type;
		boolean iswide = type.isWide();
		int space = (iswide ? 2 : 1);
		
		// {@squirreljme.error JC2u Stack would overflow loading local value.}
		Info[] stack = this._stack;
		int stacktop = this.stacktop;
		if (stacktop + space > stack.length)
			throw new InvalidClassFormatException("JC2u");
		
		// Setup new stack
		Info[] newstack = stack.clone();
		Info dest;
		newstack[stacktop] = (dest = stack[stacktop].newTypeValue(type,
			from.value, true));
		
		// Add top entry as well
		if (iswide)
			newstack[stacktop + 1] = stack[stacktop + 1].newTypeValue(
				type.topType(), from.value + 1, true);
		
		// Create resulting state
 		return new JavaStackResult(this,
			new JavaStackState(this._locals, newstack, stacktop + space),
			null,
			JavaStackResult.makeInput(from),
			JavaStackResult.makeOutput(dest));
	}
	
	/**
	 * Sets a local variable.
	 *
	 * @param __jt The type of entry to set.
	 * @param __l The local to set.
	 * @return Return the result of the set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/06
	 */
	public final JavaStackResult doLocalSet(JavaType __jt, int __l)
		throws NullPointerException
	{
		if (__jt == null)
			throw new NullPointerException("NARG");
			
		Info[] locals = this._locals;
		
		// {@squirreljme.error JC38 Cannot write over a local variable which
		// is read-only. (The local)}
		Info olddest = locals[__l];
		if (olddest.readonly)
			throw new InvalidClassFormatException("JC38 " + olddest);
		
		// If the target local is an object it could be enqueued and it has
		// to be uncounted
		List<Integer> enq = new ArrayList<>();
		List<StateOperation> ops = new ArrayList<>();
		if (olddest.canEnqueue())
			enq.add(olddest.value);
		
		// If we are going to be writing over two locals we need to check
		// the other as well
		if (__jt.isWide())
		{
			// {@squirreljme.error JC34 Cannot write over a local variable
			// which is read-only. (The local)}
			Info wolddest = locals[__l + 1];
			if (wolddest.readonly)
				throw new InvalidClassFormatException("JC34 " + wolddest);
			
			// If the target local is an object it could be enqueued
			if (wolddest.canEnqueue())
				enq.add(wolddest.value);
		}
		
		// Go through the stack and uncache anything which refers to the
		// old destination by value
		Info[] newstack = this._stack.clone();
		int bumpreg = olddest.register,
			stacktop = this.stacktop;
		for (int i = 0; i < stacktop; i++)
		{
			Info ss = newstack[i];
			
			// If the value points to the local register then it is going to
			// be destroyed, so make sure the value is correctly restored and
			// the cached state of the stack is removed
			if (ss.value == bumpreg)
			{
				int ssreg = ss.register;
				JavaType sst = ss.type;
				
				// Copy the value from the local to the stack entry's true
				// register
				ops.add(new StateOperation((sst.isWide() ?
					StateOperation.Type.WIDE_COPY : StateOperation.Type.COPY),
					bumpreg, ssreg));
				
				// If the local is counted, then the destination spot on the
				// stack needs to be counted
				if (sst.isObject() && !olddest.nocounting)
					ops.add(new StateOperation(StateOperation.Type.COUNT,
						ssreg));
				
				// Then this slot on the stack becomes just a non-cached direct
				// value
				newstack[i] = new Info(ssreg, sst, ssreg, false,
					olddest.nocounting);
				
				// Also un-cache wide values, remember that longs and doubles
				// are never counted
				if (sst.isWide())
					newstack[i + 1] = new Info(ssreg + 1, sst.topType(),
						ssreg + 1, false, false);
			}
		}
		
		// Setup new base local, remember that locals are never aliased but
		// they might use no counting
		Info[] newlocals = locals.clone();
		Info pushed;
		newlocals[__l] = (pushed = olddest.newTypeValue(__jt,
			olddest.register, false));
		
		// Additionally push top type as well
		if (__jt.isWide())
			newlocals[__l + 1] = newlocals[__l + 1].newTypeValue(
				__jt.topType(), pushed.register + 1, false);
		
		// Create resulting state
 		return new JavaStackResult(this,
			new JavaStackState(newlocals, newstack, stacktop),
			new JavaStackEnqueueList(enq.size(), enq),
			new StateOperations(ops),
			JavaStackResult.makeOutput(pushed));
	}
	
	/**
	 * Writes into the specified local variable from the top-most stack entry.
	 *
	 * Locals which are written to are never cached and are exempt from
	 * any kind caching.
	 *
	 * @param __l The local to store.
	 * @return The result of the store.
	 * @throws InvalidClassFormatException If the local cannot be written to.
	 * @since 2019/04/02
	 */
	public final JavaStackResult doLocalStore(int __l)
		throws InvalidClassFormatException
	{
		// Pop the value to store from the stack
		JavaStackResult stackpop = this.doStack(1);
		JavaStackResult.Input popped = stackpop.in(0);
		
		// Then perform the store from this previous result
		JavaStackResult stacksto = stackpop.after().doLocalSet(
			popped.type, __l);
		
		// Then combine these two states into a new result, make sure the
		// enqueues are merged
		return new JavaStackResult(this,
			stacksto.after,
			JavaStackEnqueueList.merge(stackpop.enqueue, stacksto.enqueue),
			stacksto.operations(),
			popped, stacksto.out(0));
	}
	
	/**
	 * Does nothing, keeping the state exactly the same but producing a
	 * result.
	 *
	 * @return The result of doing nothing.
	 * @since 2019/04/07
	 */
	public final JavaStackResult doNothing()
	{
		return new JavaStackResult(this, this, null);
	}
	
	/**
	 * Pops a certain number of variables and then pushes the given types
	 * to the stack. Note that all results of this operation will treat
	 * all of the target stack operations as new freshly obtained values
	 * with no caching performed on them.
	 *
	 * @param __n The number of locals to pop.
	 * @param __t The types to push.
	 * @return The result of the operation.
	 * @since 2019/03/30
	 */
	public final JavaStackResult doStack(int __n, JavaType... __t)
	{
		return this.doStack(__n, false, __t);
	}
	
	/**
	 * Pops a certain number of variables and then pushes the given types
	 * to the stack, this may also force caching on pushed values. Note that
	 * all results of this operation will treat
	 * all of the target stack operations as new freshly obtained values
	 * with no caching performed on them.
	 *
	 * @param __n The number of locals to pop.
	 * @param __nc If true then all the values being pushed will not be
	 * reference countable.
	 * @param __pts The types to push.
	 * @return The result of the operation.
	 * @throws IllegalArgumentException If the local count is negative or an
	 * attempt is made to push a top or nothing type.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	public final JavaStackResult doStack(int __n, boolean __nc,
		JavaType... __pts)
		throws IllegalArgumentException, NullPointerException
	{
		// {@squirreljme.error JC2v Cannot pop a negative number of entries.}
		if (__n < 0)
			throw new IllegalArgumentException("JC2v");
		
		// Force blank types for pushing
		__pts = (__pts == null ? new JavaType[0] : __pts.clone());
		for (JavaType pt : __pts)
			if (pt == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error JC2x Cannot push nothing or top type.}
			else if (pt.isNothing() || pt.isTop())
				throw new IllegalArgumentException("JC2x");
		
		// Only the stack is operated on!
		Info[] stack = this._stack;
		int stacktop = this.stacktop,
			stacklimit = stack.length;
		
		// Enqueues to clear popped entries
		List<Integer> enqs = new ArrayList<>();
		List<StateOperation> ops = new ArrayList<>();
		
		// Pop entries off the stack first
		List<Info> popped = new ArrayList<>();
		for (int i = 0; i < __n; i++)
		{
			// {@squirreljme.error JC2w Stack underflow.}
			if (stacktop <= 0)
				throw new IllegalArgumentException("JC2w");
			
			// Read top most entry, handle tops accordingly
			Info inf = stack[--stacktop];
			if (inf.type.isTop())
				inf = stack[--stacktop];
			
			// Only enqueue objects which are counting and which do not have
			// values of another register
			if (inf.canEnqueue())
				enqs.add(inf.value);
			
			// Was popped, so add to to the pop list
			popped.add(0, inf);
		}
		
		// Setup new stack for pushing
		Info[] newstack = stack.clone();
		
		// Push new entries to the stack
		List<Info> pushed = new ArrayList<>();
		for (JavaType pt : __pts)
		{
			// {@squirreljme.error JC2y Stack overflow. (Top; Limit)}
			if (stacktop >= stacklimit)
				throw new IllegalArgumentException("JC2y " + stacktop + " " +
					stacklimit);
			
			// Setup entry
			Info inf = newstack[stacktop];
			newstack[stacktop] = (inf = inf.newTypeValue(
				pt, inf.register, __nc));
			stacktop++;
			if (pt.isWide())
			{
				newstack[stacktop] = newstack[stacktop].newTypeValue(
					pt.topType(), inf.value + 1, __nc);
				stacktop++;
			}
			
			// Add to pushed set
			pushed.add(inf);
		}
		
		// Convert infos to I/O
		List<JavaStackResult.InputOutput> ios = new ArrayList<>();
		for (Info i : popped)
			ios.add(JavaStackResult.makeInput(i));
		for (Info o : pushed)
			ios.add(JavaStackResult.makeOutput(o));
		
		// Build result
		return new JavaStackResult(this,
			new JavaStackState(this._locals, newstack, stacktop),
			new JavaStackEnqueueList(enqs.size(), enqs),
			new StateOperations(ops),
			ios.<JavaStackResult.InputOutput>toArray(
				new JavaStackResult.InputOutput[ios.size()]));
	}
	
	/**
	 * Performs the specified stack shuffling, which may be duplication or
	 * otherwise.
	 *
	 * @param __t The type of shuffle to perform.
	 * @return The result of the shuffle.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	public final JavaStackResult doStackShuffle(JavaStackShuffleType __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Input stack properties
		Info[] stack = this._stack;
		int stacktop = this.stacktop;
		
		// Find function
		JavaStackShuffleType.Function func = this.findShuffleFunction(__t);
		
		// Determine stack properties of the pop
		int maxpop = func.in.max,
			basetop = stacktop - maxpop;
		
		// Load section of stack to be popped
		List<Info> pops = new ArrayList<>(maxpop);
		for (int i = basetop; i < stacktop; i++)
			pops.add(stack[i]);
		
		// Input and output slots
		JavaStackShuffleType.Slots sin = func.in,
			sout = func.out;
		
		// Map virtual variables to entries on the input so we know what is
		// what. Also include the register values are stored at for caching.
		Map<Integer, Info> source = new LinkedHashMap<>();
		Map<Integer, Integer> storedat = new LinkedHashMap<>();
		for (int ldx = 0; ldx < maxpop; ldx++)
		{
			int var = sin._var[ldx];
			if (var >= 0)
			{
				source.put(var, pops.get(ldx));
				storedat.put(var, -1);
			}
		}
		
		// Debug
		if (__Debug__.ENABLED)
			todo.DEBUG.note("Source map: %s", source);
		
		// Number of entries to push
		int pushcount = sout.max;
		
		// Initialize new stack
		Info[] newstack = stack.clone();
		int newstacktop = basetop + pushcount;
		
		// Any enqueues and operations to perform
		List<Integer> enqs = new ArrayList<>();
		List<StateOperation> sops = new ArrayList<>();
		
		// For registers which have a value collision, they must be
		// pre-copied to temporary space
		int tempbase = this.usedregisters;
		Map<Integer, Integer> precopy = new LinkedHashMap<>();
		
		// Setup the new stack by pushing around
		for (int at = basetop, ldx = 0; ldx < pushcount; at++, ldx++)
		{
			// Pushing a top type?
			int vardx = sout._var[ldx];
			if (vardx < 0)
			{
				// Set the current to the appropriate top type of the entry
				// before this one
				Info prev = newstack[at - 1];
				newstack[at] = newstack[at].newTypeValue(prev.type.topType(),
					prev.value + 1, false);
				
				continue;
			}
			
			// Get the source info to use for this slot
			// Also the original destination
			Info ssl = source.get(vardx),
				ods = newstack[at];
				
			// Is this type wide?
			boolean iswide = ssl.type.isWide();
			
			// If the value was never used before, try to use the original
			// register for it
			int useval = storedat.get(vardx);
			if (useval < 0)
				useval = ssl.value;
			
			// Using the value position would violate the strict no-aliasing
			// of future registers
			if (useval > ods.register)
			{
				// Try to use an already copied value, if it has not yet had
				// a pre-copy then map it to the copied source instead
				Integer pre = precopy.get(useval);
				if (pre == null)
				{
					precopy.put(useval,
						(pre = (iswide ? -tempbase : tempbase)));
					tempbase += (iswide ? 2 : 1);
				}
				
				// The value to use is the destination register because it
				// will be copied
				useval = ods.register;
				sops.add(StateOperation.copy(iswide, Math.abs(pre), useval));
				
				// Debug
				if (__Debug__.ENABLED)
					todo.DEBUG.note("Pre %d -> %d", pre, useval);
			}
			
			// Set value as being stored here
			storedat.put(vardx, useval);
			
			// Setup slot
			newstack[at] = newstack[at].newTypeValue(ssl.type, useval,
				ssl.nocounting);
		}
		
		// Pre-copies which are needed, but make sure that the original
		// link order is maintained, negative premaps are treated as
		// being wide
		int vdat = 0;
		for (Map.Entry<Integer, Integer> e : precopy.entrySet())
			sops.add(vdat++, StateOperation.copy(
				e.getValue() < 0, e.getKey(), Math.abs(e.getValue())));
		
		// Build
		return new JavaStackResult(this,
			new JavaStackState(this._locals, newstack, newstacktop),
			new JavaStackEnqueueList(enqs.size(), enqs),
			new StateOperations(sops));
	}
	
	/**
	 * Transitions to the given stack state.
	 *
	 * @param __ts The target to transition.
	 * @return The result of the transition and the operations used.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/11
	 */
	public final JavaStackResult doTransition(JavaStackState __ts)
		throws NullPointerException
	{
		if (__ts == null)
			throw new NullPointerException("NARG");
		
		// If this state is exactly the same as this one then no actual
		// transition work needs to be done, so no result is generated
		if (this.equals(__ts))
			return new JavaStackResult(this, this, null);
		
		// Debug
		if (__Debug__.ENABLED)
		{
			todo.DEBUG.note("Will transition the stack!");
			todo.DEBUG.note("From: %s", this);
			todo.DEBUG.note("To  : %s", __ts);
		}
		
		// {@squirreljme.error JC3f A transition cannot be made where the
		// length of the stack differs. (The length of the source stack; The
		// length of the target stack)}
		int atop = this.stacktop,
			btop = __ts.stacktop;
		if (atop != btop)
			throw new InvalidClassFormatException("JC3f " + atop + " " + btop);
		
		// Used to store operations and enqueues
		List<Integer> stackenq = new ArrayList<>(),
			localenq = new ArrayList<>();
		List<StateOperation> ops = new ArrayList<>();
		
		// Go through and transition the stack
		Info[] asta = this._stack,
			bsta = __ts._stack;
		for (int i = 0; i < atop; i++)
		{
			Info a = asta[i],
				b = bsta[i];
			
			// No need to transition the same values
			if (a.equals(b))
				continue;
				
			JavaType at = a.type,
				bt = b.type;
			
			// {@squirreljme.error JC3m A transition cannot be made
			// to the target type because the types are not compatible.
			// (The source; The target)}
			if (at.isObject() != bt.isObject() ||
				(!at.isObject() && !at.equals(bt)))
				throw new InvalidClassFormatException(
					"JC3m " + a + " " + b);
			
			// Copy to destination, if the values differ
			if (a.value != b.value)
				ops.add(StateOperation.copy(
					at.isWide(), a.value, b.value));
			
			// Transitioning from no-counting to counting means that A
			// was never counted
			if (!a.canEnqueue() && b.canEnqueue())
				ops.add(StateOperation.count(b.value));
			
			// Going from counting to no counting means we probably have
			// an extra count somewhere
			else if (a.canEnqueue() && !b.canEnqueue())
				ops.add(StateOperation.uncount(b.value));
		}
		
		// Go through and transition the locals
		Info[] aloc = this._locals,
			bloc = __ts._locals;
		for (int i = 0, n = aloc.length; i < n; i++)
		{
			Info a = aloc[i],
				b = bloc[i];
			
			// They are exactly the same, so nothing needs to be done
			if (a.equals(b))
				continue;
			
			// The source is nothing
			JavaType at = a.type,
				bt = b.type;
			if (at.isNothing())
			{
				// Transition to a non-nothing type, just copy zero to it
				if (!bt.isNothing())
					ops.add(StateOperation.copy(
						bt.isWide(), 0, b.value));
			}
			
			// If the target is transitioning to nothing, then it will be
			// removed
			else if (bt.isNothing())
			{
				// If the A local is an object that is countable, then just
				// uncount it
				if (a.canEnqueue())
				{
					localenq.add(a.value);
					ops.add(new StateOperation(StateOperation.Type.UNCOUNT,
						a.value));
				}
			}
			
			// Check if the types and values are compatible
			else
			{
				// {@squirreljme.error JC3g A transition cannot be made
				// to the target type because the types are not compatible.
				// (The source; The target)}
				if (at.isObject() != bt.isObject() ||
					(!at.isObject() && !at.equals(bt)))
					throw new InvalidClassFormatException(
						"JC3g " + a + " " + b);
				
				// Copy to destination, if the values differ
				if (a.value != b.value)
					ops.add(StateOperation.copy(
						at.isWide(), a.value, b.value));
				
				// Transitioning from no-counting to counting means that A
				// was never counted
				if (!a.canEnqueue() && b.canEnqueue())
					ops.add(StateOperation.count(b.value));
				
				// Going from counting to no counting means we probably have
				// an extra count somewhere
				else if (a.canEnqueue() && !b.canEnqueue())
					ops.add(StateOperation.uncount(b.value));
			}
		}
		
		// Merge the enqueues for locals and the stack
		int eqsat = localenq.size();
		localenq.addAll(stackenq);
		
		// The transition results in the target stack so we do not need to
		// initialize it, however we do need enqueues and operations
		return new JavaStackResult(this, __ts,
			new JavaStackEnqueueList(eqsat, localenq),
			new StateOperations(ops));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof JavaStackState))
			return false;
		
		// Faster to compare hashcodes first since there are lots of values
		JavaStackState o = (JavaStackState)__o;
		if (this.hashCode() != o.hashCode())
			return false;
		
		return this.stacktop == o.stacktop &&
			Arrays.equals(this._locals, o._locals) &&
			Arrays.equals(this._stack, o._stack);
	}
	
	/**
	 * Goes through the stack map and filters the locals and types so that
	 * they match what is in the stack map table.
	 *
	 * @param __smts The source stack map.
	 * @return The resulting stack which has been filtered.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/13
	 */
	public final JavaStackState filterByStackMap(StackMapTableState __smts)
		throws NullPointerException
	{
		if (__smts == null)
			throw new NullPointerException("NARG");
		
		// Debug
		if (__Debug__.ENABLED)
			todo.DEBUG.note("SMTS: %s", __smts);
			
		// Input stack properties
		Info[] locals = this._locals.clone(),
			stack = this._stack.clone();
		int stacktop = this.stacktop;
		
		// Used to just drop any changes if nothing changed
		boolean changed = false;
		
		// Filter local variables
		for (int i = 0, n = locals.length; i < n; i++)
		{
			// Get infos and state
			Info inf = locals[i];
			StackMapTableEntry sme = __smts.getLocal(i);
			
			// Type changed?
			if (!inf.type.equals(sme.type()))
			{
				// The local is being wiped away
				if (sme.type().isNothing())
					inf = new Info(inf.register, JavaType.NOTHING,
						inf.register, false, false);
				
				// Use this type instead
				else if (inf.type.isObject())
					inf = new Info(inf.register, sme.type(),
						inf.value, inf.readonly, inf.nocounting);
			}
			
			// Set if changed
			if (changed |= (locals[i] != inf))
				locals[i] = inf;
		}
		
		// Filter stack variables
		for (int i = 0; i < stacktop; i++)
		{
			// Get infos and state
			Info inf = stack[i];
			StackMapTableEntry sme = __smts.getStack(i);
			
			// Use type if the object is different
			if (!inf.type.equals(sme.type()) && inf.type.isObject())
				inf = new Info(inf.register, sme.type(),
					inf.value, inf.readonly, inf.nocounting);
			
			// Set if changed
			if (changed |= (stack[i] != inf))
				stack[i] = inf;
		}
		
		// Only return a new state if it actually changed
		if (changed)
			return new JavaStackState(locals, stack, stacktop);
		return this;
	}
	
	/**
	 * Locates the shuffle function that is used to pop from the stack
	 * accordingly to this stack state.
	 *
	 * @param __t The type of shuffle to perform.
	 * @return The matching shuffle function.
	 * @throws InvalidClassFormatException If the shuffle function was not
	 * found.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/04
	 */
	public final JavaStackShuffleType.Function findShuffleFunction(
		JavaStackShuffleType __t)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Input stack properties
		Info[] stack = this._stack;
		int stacktop = this.stacktop;
		
		// Working pop list when match is found
		int basetop = -1,
			maxpop = -1;
		
		// Search for the matching function to use for this state
		for (JavaStackShuffleType.Function tryf : __t._functions)
		{
			// Clear for run
			basetop = -1;
			
			// Input slots are used
			JavaStackShuffleType.Slots sls = tryf.in;
			
			// Too little on the stack to pop everything?
			maxpop = sls.max;
			basetop = stacktop - maxpop;
			if (basetop < 0)
				continue;
			
			// Go through slots and see if this is a match or not
			int at = basetop;
			for (int ldx = 0; at < stacktop; ldx++, at++)
			{
				Info i = stack[at];
				JavaType it = i.type;
				
				// Top-ness and wide-ness does not match
				if (it.isTop() != (sls._var[ldx] < 0) ||
					it.isWide() != sls._wide[ldx])
					break;
			}
			
			// If this index was reached then everything was valid
			if (at == stacktop)
				return tryf;
		}
		
		// {@squirreljme.error JC32 Could not find a match for performing
		// shuffled stack operations.}
		throw new InvalidClassFormatException("JC32");
	}
	
	/**
	 * Obtains the given local.
	 *
	 * @param __i The local to obtain.
	 * @return The information for the local.
	 * @since 2019/03/30
	 */
	public final JavaStackState.Info getLocal(int __i)
	{
		return this._locals[__i];
	}
	
	/**
	 * Obtains the given stack entry.
	 *
	 * @param __i The stack entry to obtain.
	 * @return The information for the stack entry.
	 * @since 2019/03/30
	 */
	public final JavaStackState.Info getStack(int __i)
	{
		return this._stack[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final int hashCode()
	{
		int hash = this._hash;
		if (hash == 0)
			this._hash = (hash = this.stacktop -
				Arrays.asList(this._locals).hashCode() ^
				Arrays.asList(this._stack).hashCode());
		return hash;
	}
	
	/**
	 * Returns the number of local used.
	 *
	 * @return The locals used.
	 * @since 2019/04/06
	 */
	public final int maxLocals()
	{
		return this._locals.length;
	}
	
	/**
	 * Returns the maximum stack size.
	 *
	 * @return The maximum stack size.
	 * @since 2019/04/06
	 */
	public final int maxStack()
	{
		return this._stack.length;
	}
	
	/**
	 * Returns the resulting stack state which would be as if nothing were
	 * cached.
	 *
	 * @return The non-cached stack state.
	 * @since 2019/04/11
	 */
	public final JavaStackState nonCached()
	{
		// Create new copies of the state
		int stacktop = this.stacktop;
		Info[] locals = this._locals.clone(),
			stack = this._stack.clone();
		
		// Un-cache locals
		for (int i = 0, n = locals.length; i < n; i++)
		{
			Info info = locals[i];
			
			// Read-only values are never un-cached
			if (info.readonly)
				continue;
			
			// Map value to the register
			locals[i] = info.newValue(info.register, false);
		}
		
		// Un-cache the stack
		for (int i = 0; i < stacktop; i++)
		{
			Info info = stack[i];
			stack[i] = info.newValue(info.register, false);
		}
		
		// Build, do not return the new object if it ends up being the same
		JavaStackState rv = new JavaStackState(locals, stack, stacktop);
		if (this.equals(rv))
			return this;
		return rv;
	}
	
	/**
	 * Returns all of the enqueues which are possible if the entire stack
	 * and locals were to be flushed.
	 *
	 * @return The maximum possible enqueue list.
	 * @since 2019/03/30
	 */
	public final JavaStackEnqueueList possibleEnqueue()
	{
		// This is the same as a destroy
		return this.doDestroy(false).enqueue();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder("State:{L=");
			
			// Add locals
			sb.append(Arrays.asList(this._locals));
			
			// Add stack entries
			Info[] stack = this._stack;
			sb.append(", S=[");
			for (int i = 0, n = this.stacktop; i < n; i++)
			{
				if (i > 0)
					sb.append(", ");
				sb.append(stack[i]);
			}
			sb.append("]}");
			
			// Build
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
	
	/**
	 * Initializes the stack state based off the given stack map table state,
	 * this should only be used for the initial seed of the stack state.
	 *
	 * @param __s The state to base off.
	 * @param __lw Local variables which have been written, this is used to
	 * set flags where locals are cached and can never be written to.
	 * @return The result stack state.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	public static final JavaStackState of(StackMapTableState __s, int... __lw)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Optional, might not be specified, but also sort it for searching
		__lw = (__lw == null ? new int[0] : __lw.clone());
		Arrays.sort(__lw);
		
		// Get size of the entries
		int maxlocals = __s.maxLocals(),
			maxstack = __s.maxStack(),
			stacktop = __s.depth();
		
		// Force the stack to have at least a single entry for exception
		// handlers
		if (maxstack == 0)
			maxstack++;
		
		// Setup output infos
		Info[] locals = new Info[maxlocals],
			stack = new Info[maxstack];
		
		// Register position for the slot
		int rpos = BASE_REGISTER;
		
		// Initialize locals
		for (int i = 0; i < maxlocals; i++)
		{
			StackMapTableEntry from = __s.getLocal(i);
			
			// This local is considered read-only if it is not written to
			boolean ro = !(Arrays.binarySearch(__lw, i) >= 0);
			
			// Is there a type here?
			JavaType t = from.type();
			
			// Setup info here
			locals[i] = new Info(rpos, t, (t.isNothing() ? -1 : rpos), ro, ro);
			rpos++;
		}
		
		// Initialize stack
		for (int i = 0; i < maxstack; i++)
		{
			// Past end of stack?
			if (i >= stacktop)
				stack[i] = new Info(rpos++, JavaType.NOTHING, -1, false,
					false);
			
			// Normal entry
			else
			{
				StackMapTableEntry from = __s.getStack(i);
				
				// Setup info here
				stack[i] = new Info(rpos, from.type(), rpos, false, false);
				rpos++;
			}
		}
		
		// Build it
		return new JavaStackState(locals, stack, stacktop);
	}
	
	/**
	 * Contains information on the individual stack slots.
	 *
	 * @since 2019/03/30
	 */
	public static final class Info
	{
		/** The register position. */
		public final int register;
		
		/** The type. */
		public final JavaType type;
		
		/** The value register. */
		public final int value;
		
		/** Is this read-only? */
		public final boolean readonly;
		
		/** Do not use counting. */
		public final boolean nocounting;
		
		/** String representation. */
		private Reference<String> _string;
		
		/** Hash. */
		private int _hash;
		
		/**
		 * Initializes the information.
		 *
		 * @param __rp The register.
		 * @param __t The type.
		 * @param __rv The value register.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/03/31
		 */
		public Info(int __rp, JavaType __t, int __rv)
			throws NullPointerException
		{
			this(__rp, __t, __rv, false, false);
		}
		
		/**
		 * Initializes the information.
		 *
		 * @param __rp The register.
		 * @param __t The type.
		 * @param __rv The value register.
		 * @param __ro Is this read-only?
		 * @param __nc Is no counting to be used?
		 * @throws NullPointerException On null arguments.
		 * @since 2019/03/31
		 */
		public Info(int __rp, JavaType __t, int __rv, boolean __ro,
			boolean __nc)
			throws NullPointerException
		{
			if (__t == null)
				throw new NullPointerException("NARG");
			
			// If no value was set, just set it to the position
			if (!__t.isNothing() && __rv < 0)
				__rv = __rp;
			
			// Set
			this.register = __rp;
			this.type = __t;
			this.value = (__rv = (__t.isNothing() ? -1 : __rv));
			this.readonly = __ro;
			this.nocounting = __t.isObject() &&
				(__nc || __rp != __rv || __rv < 0);
		}
		
		/**
		 * Can this be enqueued?
		 *
		 * @return If this can be enqueued.
		 * @since 2019/03/31
		 */
		public final boolean canEnqueue()
		{
			if (this.nocounting)
				return false;
			
			return this.type.isObject() &&
				this.register == this.value;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/30
		 */
		@Override
		public final boolean equals(Object __o)
		{
			if (this == __o)
				return true;
			
			if (!(__o instanceof Info))
				return false;
			
			Info o = (Info)__o;
			if (this.hashCode() != o.hashCode())
				return false;
			
			return this.register == o.register &&
				this.type.equals(o.type) &&
				this.value == o.value &&
				this.readonly == o.readonly &&
				this.nocounting == o.nocounting;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/30
		 */
		@Override
		public final int hashCode()
		{
			int rv = this._hash;
			if (rv == 0)
				this._hash = (rv = this.register + this.type.hashCode() +
					this.value + (this.readonly ? 12873 : -18723) +
					(this.nocounting ? 987214 : -2143));
			return rv;
		}
		
		/**
		 * Returns information with a new type and value.
		 *
		 * @param __t The type to use.
		 * @param __v The value to use.
		 * @param __nc Do not count this?
		 * @return The new information.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/03/31
		 */
		public final Info newTypeValue(JavaType __t, int __v, boolean __nc)
			throws NullPointerException
		{
			Info rv = new Info(this.register, __t, __v, false, __nc);
			if (this.equals(rv))
				return this;
			return rv;
		}
		
		/**
		 * Sets up info with a new value, using the same type.
		 *
		 * @param __v The new value.
		 * @param __nc Do not count this?
		 * @return The resulting value.
		 * @since 2019/04/11
		 */
		public final Info newValue(int __v, boolean __nc)
		{
			Info rv = new Info(this.register, this.type, __v, false, __nc);
			if (this.equals(rv))
				return this;
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/30
		 */
		@Override
		public final String toString()
		{
			Reference<String> ref = this._string;
			String rv;
			
			if (ref == null || null == (rv = ref.get()))
			{
				int register = this.register;
				JavaType type = this.type;
				boolean ro = this.readonly,
					nc = this.nocounting;
				
				// Use a compact format for nothing
				if (type.isNothing())
					rv = "--(r" + register + ")";
				
				// Otherwise use a more compact form
				// Previously it was {V=r4 (r4), T=I, F=RONC}, however that
				// takes up too much room and is hard to read
				else
				{
					// If value == register, there is no point in duplicating
					int value = this.value;
					rv = String.format("%s:%s%s%s%s",
						(value != register ?
							("r" + value + "(" + register + ")") :
							("r" + value)),
						this.type, ((ro || nc) ? ":" : ""),
						(ro ? "R" : ""), (nc ? "N" : ""));
				}
				
				this._string = new WeakReference<>(rv);
			}
			
			return rv;
		}
	}
}

