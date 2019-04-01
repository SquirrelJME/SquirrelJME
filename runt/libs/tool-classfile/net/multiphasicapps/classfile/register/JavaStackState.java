// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

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
	/** The top of the stack. */
	protected final int stacktop;
	
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
		if (__rv)
			throw new todo.TODO();
		
		// Enqueue stack items, they do not need clearing out because setting
		// a limiting top will auto-clear
		int eqss = enqueue.size();
		for (int i = 0; i < stacktop; i++)
		{
			inf = stack[i];
			
			if (inf.canEnqueue())
				enqueue.add(inf.value);
		}
		
		// Create result
		return new JavaStackResult(this,
			new JavaStackState(newlocals, stack, 0),
			new JavaStackEnqueueList(eqss, enqueue),
			io.<JavaStackResult.InputOutput>toArray(
				new JavaStackResult.InputOutput[io.size()]));
	}
	
	/**
	 * Removes all stack variables and places a single entry on the stack
	 * for exception handling.
	 *
	 * The input is filled with the entire stack. The output is a single
	 * entry and contains the stack entry to place the value at.
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
		
		throw new todo.TODO();
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
			// {@squirreljme.error JC2y Stack overflow.}
			if (stacktop >= stacklimit)
				throw new IllegalArgumentException("JC2y");
			
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
		
		// Working pop list when match is found
		List<Info> pops = new ArrayList<>();
		int basetop = -1,
			maxpop = -1;
		
		// Search for the matching function to use for this state
		JavaStackShuffleType.Function func = null;
		for (JavaStackShuffleType.Function tryf : __t._functions)
		{
			// Clear for run
			pops.clear();
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
				
				// Add this to the input to be popped
				pops.add(i);
			}
			
			// If this index was reached then everything was valid
			if (at == stacktop)
			{
				func = tryf;
				break;
			}
		}
		
		// {@squirreljme.error JC32 Could not find a match for performing
		// shuffled stack operations.}
		if (func == null)
			throw new InvalidClassFormatException("JC32");
		
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
		todo.DEBUG.note("Source map: %s", source);
		
		// Number of entries to push
		int pushcount = sout.max;
		
		// Initialize new stack
		Info[] newstack = stack.clone();
		int newstacktop = basetop + pushcount;
		
		// Any enqueues
		List<Integer> enqs = new ArrayList<>();
		
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
			
			// If the value was never used before, try to use the original
			// register for it
			int useval = storedat.get(vardx);
			if (useval < 0)
				useval = ssl.value;
			
			// Using the value position would violate the strict no-aliasing
			// of future registers
			if (useval > ods.register)
				throw new todo.TODO();
			
			// Set value as being stored here
			storedat.put(vardx, useval);
			
			// Setup slot
			newstack[at] = newstack[at].newTypeValue(ssl.type, useval,
				ssl.nocounting);
		}
		
		// Build
		return new JavaStackResult(this,
			new JavaStackState(this._locals, newstack, newstacktop),
			new JavaStackEnqueueList(enqs.size(), enqs));
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
		
		// Setup output infos
		Info[] locals = new Info[maxlocals],
			stack = new Info[maxstack];
		
		// Register position for the slot
		int rpos = 0;
		
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
			this.nocounting = (__nc || __rp != __rv || __rv < 0);
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
		 * @param __nc Is counting to be used?
		 * @return The new information.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/03/31
		 */
		public final Info newTypeValue(JavaType __t, int __v, boolean __nc)
			throws NullPointerException
		{
			return new Info(this.register, __t, __v, false, __nc);
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
				this._string = new WeakReference<>((rv = String.format(
					"{V=r%d (r%d), T=%s, F=%s%s}", this.value, this.register,
					this.type, (this.readonly ? "RO" : ""),
					(this.nocounting ? "NC" : ""))));
			
			return rv;
		}
	}
}

