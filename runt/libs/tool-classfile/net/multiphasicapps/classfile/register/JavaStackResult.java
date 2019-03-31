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
import java.util.List;
import net.multiphasicapps.classfile.JavaType;

/**
 * This represents the result of operations performed on the Java stack.
 *
 * This class is immutable.
 *
 * @since 2019/03/30
 */
public final class JavaStackResult
{
	/** The stack state before. */
	protected final JavaStackState before;
	
	/** The stack state after. */
	protected final JavaStackState after;
	
	/** Enqueue list. */
	protected final JavaStackEnqueueList enqueue;
	
	/** Input. */
	private final JavaStackResult.Input[] _in;
	
	/** Output. */
	private final JavaStackResult.Output[] _out;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the result of the operation
	 *
	 * @param __bs The previous stack state.
	 * @param __as The after (the new) stack state.
	 * @param __eq Enqueue list, may be {@code null}.
	 * @param __io Input/output.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/31
	 */
	public JavaStackResult(JavaStackState __bs, JavaStackState __as,
		JavaStackEnqueueList __eq, InputOutput... __io)
		throws NullPointerException
	{
		if (__bs == null || __as == null)
			throw new NullPointerException("NARG");
		
		// Sort through input/output and put into their own pile
		List<Input> in = new ArrayList<>();
		List<Output> out = new ArrayList<>();
		for (InputOutput x : (__io = (__io == null ?
			new InputOutput[0] : __io.clone())))
			if (x == null)
				throw new NullPointerException("NARG");
			else if (x instanceof Input)
				in.add((Input)x);
			else
				out.add((Output)x);
		
		this.before = __bs;
		this.after = __as;
		this.enqueue = (__eq == null ? new JavaStackEnqueueList(0) : __eq);
		this._in = in.<Input>toArray(new Input[in.size()]);
		this._out = out.<Output>toArray(new Output[out.size()]);
		
		// Debug
		todo.DEBUG.note("%s", this);
	}
	
	/**
	 * Represents the new state after the operation was performed.
	 *
	 * @return The state that is the result of the operation.
	 * @since 2019/03/30
	 */
	public final JavaStackState after()
	{
		return this.after;
	}
	
	/**
	 * Represents the previous state which this was based off.
	 *
	 * @return The previous state this originated from.
	 * @since 2019/03/30
	 */
	public final JavaStackState before()
	{
		return this.before;
	}
	
	/**
	 * Returns the enqueue list which represents everything that is to be
	 * uncounted after the operation completes.
	 *
	 * @return The enqueue list, will be empty if there is nothing to
	 * enqueue.
	 * @since 2019/03/30
	 */
	public final JavaStackEnqueueList enqueue()
	{
		return this.enqueue;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/30
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the information on the input.
	 *
	 * @param __i The input to get.
	 * @return The information on the input.
	 * @since 2019/03/30
	 */
	public final JavaStackResult.Input in(int __i)
	{
		return this._in[__i];
	}
	
	/**
	 * Returns the number of generated inputs.
	 *
	 * @return The input count.
	 * @since 2019/03/30
	 */
	public final int inCount()
	{
		return this._in.length;
	}
	
	/**
	 * Returns the information on the output.
	 *
	 * @param __i The output to get.
	 * @return The information on the output.
	 * @since 2019/03/30
	 */
	public final JavaStackResult.Output out(int __i)
	{
		return this._out[__i];
	}
	
	/**
	 * Returns the number of generated outputs.
	 *
	 * @return The output count.
	 * @since 2019/03/30
	 */
	public final int outCount()
	{
		return this._out.length;
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
				"Result {bef=%s, aft=%s, enq=%s, in=%s, out=%s}",
				this.before, this.after, this.enqueue,
				Arrays.asList(this._in), Arrays.asList(this._out))));
		
		return rv;
	}
	
	/**
	 * Makes an input.
	 *
	 * @param __i The info to base from.
	 * @return The input.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/31
	 */
	public static final Input makeInput(JavaStackState.Info __i)
		throws NullPointerException
	{
		return new Input(__i);
	}
	
	/**
	 * Makes an output.
	 *
	 * @param __i The info to base from.
	 * @return The output.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/31
	 */
	public static final Output makeOutput(JavaStackState.Info __i)
		throws NullPointerException
	{
		return new Output(__i);
	}
	
	/**
	 * Input information.
	 *
	 * @since 2019/03/30
	 */
	public static final class Input
		implements InputOutput
	{
		/** The register used for input. */
		public final int register;
		
		/** The type which was read. */
		public final JavaType type;
		
		/** Not counting? */
		public final boolean nocounting;
	
		/** String representation. */
		private Reference<String> _string;
		
		/**
		 * Initializes the input.
		 *
		 * @param __i The info to base off.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/03/31
		 */
		public Input(JavaStackState.Info __i)
			throws NullPointerException
		{
			if (__i == null)
				throw new NullPointerException("NARG");
			
			this.register = __i.value;
			this.type = __i.type;
			this.nocounting = __i.nocounting;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/31
		 */
		@Override
		public final boolean equals(Object __o)
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/31
		 */
		@Override
		public final int hashCode()
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/31
		 */
		@Override
		public final String toString()
		{
			Reference<String> ref = this._string;
			String rv;
			
			if (ref == null || null == (rv = ref.get()))
				this._string = new WeakReference<>((rv = String.format(
					"In:{r=r%d, type=%s, flags=%s}",
					this.register, this.type, (this.nocounting ? "NC" : ""))));
			
			return rv;
		}
	}
	
	/**
	 * Used to flag input and output.
	 *
	 * @since 2019/03/31
	 */
	public static interface InputOutput
	{
	}
	
	/**
	 * Output information.
	 *
	 * @since 2019/03/30
	 */
	public static final class Output
		implements InputOutput
	{
		/** The register used for output. */
		public final int register;
		
		/** The output type. */
		public final JavaType type;
		
		/** Not counting? */
		public final boolean nocounting;
	
		/** String representation. */
		private Reference<String> _string;
		
		/**
		 * Initializes the output.
		 *
		 * @param __i The info to base off.
		 * @throws NullPointerException On null arguments.
		 * @since 2019/03/31
		 */
		public Output(JavaStackState.Info __i)
			throws NullPointerException
		{
			if (__i == null)
				throw new NullPointerException("NARG");
			
			this.register = __i.value;
			this.type = __i.type;
			this.nocounting = __i.nocounting;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/31
		 */
		@Override
		public final boolean equals(Object __o)
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/31
		 */
		@Override
		public final int hashCode()
		{
			throw new todo.TODO();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2019/03/31
		 */
		@Override
		public final String toString()
		{
			Reference<String> ref = this._string;
			String rv;
			
			if (ref == null || null == (rv = ref.get()))
				this._string = new WeakReference<>((rv = String.format(
					"Out:{r=r%d, type=%s, flags=%s}",
					this.register, this.type, (this.nocounting ? "NC" : ""))));
			
			return rv;
		}
	}
}

