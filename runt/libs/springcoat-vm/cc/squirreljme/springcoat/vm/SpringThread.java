// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.classfile.ByteCode;

/**
 * This class contains information about a thread within the virtual machine.
 *
 * @since 2018/09/01
 */
public final class SpringThread
{
	/** The thread ID. */
	protected final int id;
	
	/** The name of this thread. */
	protected final String name;
	
	/** The stack frames. */
	private final List<SpringThread.Frame> _frames =
		new ArrayList<>();
		
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the thread.
	 *
	 * @param __id The thread ID.
	 * @param __n The name of the thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/01
	 */
	SpringThread(int __id, String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.id = __id;
		this.name = __n;
	}
	
	/**
	 * Returns the current frame of execution or {@code null} if there is none.
	 *
	 * @return The current frame of execution or {@code null} if there is none.
	 * @since 2018/09/03
	 */
	public final SpringThread.Frame currentFrame()
	{
		List<SpringThread.Frame> frames = this._frames;
		synchronized (frames)
		{
			if (frames.isEmpty())
				return null;
			return frames.get(frames.size() - 1);
		}
	}
	
	/**
	 * Enters a blank frame to store data.
	 *
	 * @return The
	 * @since 2018/09/20
	 */
	public final SpringThread.Frame enterBlankFrame()
	{
		// Setup blank frame
		SpringThread.Frame rv = new SpringThread.Frame();
		
		// Lock on frames as a new one is added
		List<SpringThread.Frame> frames = this._frames;
		synchronized (frames)
		{
			frames.add(rv);
		}
		
		return rv;
	}
	
	/**
	 * Enters the specified method and sets up a stack frame for it.
	 *
	 * @param __m The method to enter.
	 * @param __args Arguments to the frame entry (method arguments).
	 * @return The used stack frame.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringVirtualMachineException If the method is abstract.
	 * @since 2018/09/03
	 */
	public final SpringThread.Frame enterFrame(SpringMethod __m,
		Object... __args)
		throws NullPointerException, SpringVirtualMachineException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		if (__args == null)
			__args = new Object[0];
		
		// Debug
		/*todo.DEBUG.note("enterFrame(%s::%s, %s)", __m.inClass(),
			__m.nameAndType(), Arrays.<Object>asList(__args));*/
		
		// {@squirreljme.error BK09 Cannot enter the frame for a method which
		// is abstract. (The class the method is in; The method name and type)}
		if (__m.isAbstract())
			throw new SpringVirtualMachineException(String.format("BK09 %s %s",
				__m.inClass(), __m.nameAndType()));
		
		// Create new frame
		Frame rv = new Frame(__m, __args);
		
		// Lock on frames as a new one is added
		List<SpringThread.Frame> frames = this._frames;
		synchronized (frames)
		{
			frames.add(rv);
		}
		
		return rv;
	}
	
	/**
	 * Returns all of the frames which are available.
	 *
	 * @return All of the available stack frames.
	 * @since 2018/09/16
	 */
	public final SpringThread.Frame[] frames()
	{
		// Lock on frames
		List<SpringThread.Frame> frames = this._frames;
		synchronized (frames)
		{
			return frames.<SpringThread.Frame>toArray(
				new SpringThread.Frame[frames.size()]);
		}
	}
	
	/**
	 * Returns the name of the thread.
	 *
	 * @return The name of the thread.
	 * @since 2018/09/03
	 */
	public final String name()
	{
		return this.name;
	}
	
	/**
	 * Returns the number of frames that are available in this thread.
	 *
	 * @return The number of available frames.
	 * @since 2018/09/03
	 */
	public final int numFrames()
	{
		List<SpringThread.Frame> frames = this._frames;
		synchronized (frames)
		{
			return frames.size();
		}
	}
	
	/**
	 * Pops a frame from the thread stack.
	 *
	 * @return The frame which was popped.
	 * @throws SpringVirtualMachineException If there are no stack frames.
	 * @since 2018/09/09
	 */
	public final SpringThread.Frame popFrame()
		throws SpringVirtualMachineException
	{
		List<SpringThread.Frame> frames = this._frames;
		synchronized (frames)
		{
			// {@squirreljme.error BK0p No frames to pop.}
			int n;
			if ((n = frames.size()) <= 0)
				throw new SpringVirtualMachineException("BK0p");	
			
			return frames.remove(n - 1);
		}
	}
	
	/**
	 * Prints this thread's stack trace.
	 *
	 * @param __ps The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/20
	 */
	public final void printStackTrace(PrintStream __ps)
		throws NullPointerException
	{
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Note the thread
		__ps.printf("Thread #%d: %s%n", this.id, this.name);
		
		// Lock since the frames may change!
		List<SpringThread.Frame> frames = this._frames;
		synchronized (frames)
		{
			// The frames at the end are at the top
			for (int n = frames.size(), i = n - 1; i >= 0; i--)
			{
				SpringThread.Frame frame = frames.get(i);
				
				// Do not print traces for blank frames
				if (frame.isBlank())
				{
					__ps.printf("    at <guard frame>%n");
					continue;
				}
				
				SpringMethod inmethod = frame.method();
				int pc = frame.lastExecutedPc();
				
				// Print information
				__ps.printf("    at %s.%s @ %d [%s] (%s:%d)%n",
					inmethod.inClass(),
					inmethod.nameAndType(),
					pc,
					inmethod.byteCode().getByAddress(pc).mnemonic(),
					inmethod.inFile(),
					inmethod.byteCode().lineOfAddress(pc));
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/15
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = String.format(
				"Thread-%d: %s", this.id, this.name)));
		
		return rv;
	}
	
	/**
	 * This class represents the stack frame and is used to store local
	 * variables and other such things.
	 *
	 * @since 2018/09/03
	 */
	public static class Frame
	{
		/** The current method in this frame. */
		protected final SpringMethod method;
		
		/** The method code for execution. */
		protected final ByteCode code;
		
		/** The current object of the current frame. */
		protected final SpringObject thisobject;
		
		/** Is this frame blank? */
		protected final boolean isblank;
		
		/** Local variables. */
		private final Object[] _locals;
		
		/** The stack. */
		private final Object[] _stack;
		
		/** The top of the stack. */
		private volatile int _stacktop;
		
		/** The current program counter. */
		private volatile int _pc;
		
		/** Last executed PC address. */
		private volatile int _lastexecpc;
		
		/**
		 * Initializes a blank guard frame.
		 *
		 * @since 2018/09/20
		 */
		private Frame()
		{
			this.method = null;
			this.code = null;
			this.thisobject = null;
			this.isblank = true;
			this._locals = new Object[0];
			this._stack = new Object[2];
		}
		
		/**
		 * Initializes the frame.
		 *
		 * @param __m The method used for the frame.
		 * @param __args The frame arguments.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/09/03
		 */
		private Frame(SpringMethod __m, Object... __args)
			throws NullPointerException
		{
			if (__m == null)
				throw new NullPointerException("NARG");
			
			__args = (__args == null ? new Object[0] : __args.clone());
			
			this.isblank = false;
			this.method = __m;
			
			// We will need to initialize the local and stack data from the
			// information the byte code gives
			ByteCode code;
			this.code = (code = __m.byteCode());
			
			// Initialize variable storage
			Object[] locals;
			this._locals = (locals = new Object[code.maxLocals()]);
			this._stack = new Object[code.maxStack()];
			
			// Copy arguments passed to the method
			for (int i = 0, n = __args.length, o = 0; i < n; i++)
			{
				Object av;
				locals[o++] = (av = __args[i]);
				
				// Add additional top for long/double
				if (av instanceof Long || av instanceof Double)
					locals[o++] = SpringStackTop.TOP;
			}
			
			// Store the this object, if needed
			this.thisobject = (__m.flags().isStatic() ? null :
				(SpringObject)__args[0]);
			
			// Debug
			/*todo.DEBUG.note("Frame has %d locals, %d stack", locals.length,
				this._stack.length);*/
		}
		
		/**
		 * Returns the byte code to execute.
		 *
		 * @return The byte code to execute.
		 * @since 2018/09/03
		 */
		public final ByteCode byteCode()
		{
			return this.code;
		}
		
		/**
		 * Is this a blank frame?
		 *
		 * @return If this is a blank frame.
		 * @since 2018/09/20
		 */
		public final boolean isBlank()
		{
			return this.isblank;
		}
		
		/**
		 * Returns the last executed PC address.
		 *
		 * @return The last executed PC address.
		 * @since 2018/09/20
		 */
		public final int lastExecutedPc()
		{
			return this._lastexecpc;
		}
		
		/**
		 * Loads a value from local variables.
		 *
		 * @param <C> The type to return.
		 * @param __cl The type to return.
		 * @param __dx The index to load from.
		 * @return The read value.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/09/16
		 */
		public final <C> C loadLocal(Class<C> __cl, int __dx)
			throws NullPointerException
		{
			return __cl.cast(this._locals[__dx]);
		}
		
		/**
		 * Loads the specified value from a local variable and pushes it to
		 * the stack.
		 *
		 * @param __cl The expected class type.
		 * @param __dx The index to load from.
		 * @since 2018/09/08
		 */
		public final void loadToStack(Class<?> __cl, int __dx)
		{
			Object[] locals = this._locals;
			
			// {@squirreljme.error BK0b Cannot push local variable to the stack
			// because it of the incorrect type. (The varible to push; The
			// index to load from; The expected class; The value to push;
			// The type of value to push)}
			Object pushy = locals[__dx];
			if (!__cl.isInstance(pushy))
				throw new SpringVirtualMachineException(String.format(
					"BK0b %s %d %s %s %s", pushy, __dx, __cl, pushy,
					(pushy == null ? "null" : pushy.getClass())));
			
			// Just copy to the stack
			this.pushToStack(pushy);
		}
		
		/**
		 * Returns the method that this frame represents.
		 *
		 * @return The current method the frame is in.
		 * @since 2018/09/08
		 */
		public final SpringMethod method()
		{
			return this.method;
		}
		
		/**
		 * Returns the instruction counter.
		 *
		 * @return The instruction counter.
		 * @since 2018/09/03
		 */
		public final int pc()
		{
			return this._pc;
		}
		
		/**
		 * Returns the line of code the program counter is on.
		 *
		 * @return The program counter line of code.
		 * @since 2018/09/16
		 */
		public final int pcSourceLine()
		{
			return this.code.lineOfAddress(this._pc);
		}
		
		/**
		 * Pops from the stack.
		 *
		 * @return The popped value.
		 * @throws SpringVirtualMachineException If the stack underflows.
		 * @since 2018/09/09
		 */
		public final Object popFromStack()
			throws SpringVirtualMachineException
		{
			Object[] stack = this._stack;
			int stacktop = this._stacktop;
			
			// {@squirreljme.error BK0m Stack underflow. (The current top of
			// the stack; The stack limit)}
			if (stacktop <= 0)
				throw new SpringVirtualMachineException(String.format(
					"BK0m %d %d", stacktop, stack.length));
			
			// Read value and clear the value that was there
			Object rv = stack[--stacktop];
			stack[stacktop] = null;
			this._stacktop = stacktop;
			
			// {@squirreljme.error BK11 Popped a null value of the stack, which
			// should not occur.}
			if (rv == null)
				throw new SpringVirtualMachineException("BK11");
			
			// Is top, so pop again to read the actual desired value
			if (rv == SpringStackTop.TOP)
			{
				rv = this.popFromStack();
				
				// {@squirreljme.error BK0n Expected long or double below
				// top entry in stack. (The current top of the stack; The
				// stack limit)}
				if (!(rv instanceof Long || rv instanceof Double))
					throw new SpringVirtualMachineException(String.format(
						"BK0n %d %d", stacktop, stack.length));
			}
			
			// Debug
			/*todo.DEBUG.note("popped(%s) <- %d", rv, stacktop);*/
			
			return rv;
		}
		
		/**
		 * Pops from the stack, checking the type.
		 *
		 * @param <C> The type to pop.
		 * @param __cl The type to pop.
		 * @return The popped data.
		 * @throws NullPointerException On null arguments.
		 * @throws SpringVirtualMachineException If the type does not match or
		 * the stack underflows.
		 * @since 2018/09/15
		 */
		public final <C> C popFromStack(Class<C> __cl)
			throws NullPointerException, SpringVirtualMachineException
		{
			if (__cl == null)
				throw new NullPointerException("NARG");
			
			// {@squirreljme.error BK0z Popped the wrong kind of value from the
			// stack. (The popped type; The expected type)}
			Object rv = this.popFromStack();
			if (!__cl.isInstance(rv))
				throw new SpringVirtualMachineException(
					String.format("BK0z %s %s",
						(rv == null ? null : rv.getClass()), __cl));
			
			return __cl.cast(rv);
		}
		
		/**
		 * Pushes the specified value to the stack.
		 *
		 * @param __v The value to push.
		 * @throws NullPointerException On null arguments.
		 * @throws SpringVirtualMachineException If the stack overflows or is
		 * not valid.
		 * @since 2018/09/08
		 */
		public final void pushToStack(Object __v)
			throws NullPointerException, SpringVirtualMachineException
		{
			if (__v == null)
				throw new NullPointerException("NARG");
			
			Object[] stack = this._stack;
			int stacktop = this._stacktop;
			
			// Debug
			/*todo.DEBUG.note("push(%s) -> %d", __v, stacktop);*/
			
			// {@squirreljme.error BK0c Stack overflow pushing value. (The
			// value; The current top of the stack; The stack limit)}
			if (stacktop >= stack.length)
				throw new SpringVirtualMachineException(String.format(
					"BK0c %s %d %d", __v, stacktop, stack.length));
			
			// Store
			stack[stacktop++] = __v;
			this._stacktop = stacktop;
			
			// Push an extra top for long and double
			if (__v instanceof Long || __v instanceof Double)
				this.pushToStack(SpringStackTop.TOP);
		}
		
		/**
		 * Sets the last executed PC address.
		 *
		 * @param __pc The last executed PC address.
		 * @since 2018/09/20
		 */
		public final void setLastExecutedPc(int __pc)
		{
			this._lastexecpc = __pc;
		}
		
		/**
		 * Sets the program counter.
		 *
		 * @param __pc The program counter to set.
		 * @since 2018/09/03
		 */
		public final void setPc(int __pc)
		{
			this._pc = __pc;
		}
		
		/**
		 * Stores the specified value at the given local variable index.
		 *
		 * @param __dx The index to store into.
		 * @param __v The value to store.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/09/15
		 */
		public final void storeLocal(int __dx, Object __v)
			throws NullPointerException
		{
			if (__v == null)
				throw new NullPointerException("NARG");
			
			this._locals[__dx] = __v;
		}
		
		/**
		 * Returns the {@code this} of the current frame.
		 *
		 * @return The current this,
		 * @since 2018/09/09
		 */
		public final SpringObject thisObject()
		{
			return this.thisobject;
		}
	}
}

