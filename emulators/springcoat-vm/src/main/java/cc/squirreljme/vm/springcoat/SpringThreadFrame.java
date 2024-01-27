// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.emulator.profiler.ProfiledFrame;
import cc.squirreljme.vm.springcoat.exceptions.SpringNullPointerException;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import net.multiphasicapps.classfile.ByteCode;

/**
 * This class represents the stack frame and is used to store local
 * variables and other such things.
 *
 * @since 2018/09/03
 */
public class SpringThreadFrame
{
	/** The frame level. */
	public final int level;
	
	/** The current method in this frame. */
	protected final SpringMethod method;
	
	/** The method code for execution. */
	protected final ByteCode code;
	
	/** The current object of the current frame. */
	protected final SpringObject thisobject;
	
	/** Is this frame blank? */
	protected final boolean isblank;
	
	/** The class this is for. */
	protected final SpringClass springClass;
	
	/** Local variables. */
	private final Object[] _locals;
	
	/** The stack. */
	private final Object[] _stack;
	
	/** Profiled frame. */
	volatile ProfiledFrame _profiler;
	
	/** The top of the stack. */
	private volatile int _stacktop;
	
	/** The current program counter. */
	private volatile int _pc;
	
	/** Last executed PC address. */
	private volatile int _lastexecpc;
	
	/** Frame execution count. */
	private volatile int _execcount;
	
	/** Exception which was tossed into this frame. */
	private SpringObject _tossedexception;
	
	/** Object which has a monitor for quicker unlock. */
	volatile SpringObject _monitor;
	
	/**
	 * Initializes a blank guard frame.
	 *
	 * @param __level The frame depth.
	 * @since 2018/09/20
	 */
	SpringThreadFrame(int __level)
	{
		this.level = __level;
		this.method = null;
		this.springClass = null;
		this.code = null;
		this.thisobject = null;
		this.isblank = true;
		this._locals = new Object[0];
		this._stack = new Object[2];
	}
	
	/**
	 * Initializes the frame.
	 *
	 * @param __level The frame depth.
	 * @param __cl The class used.
	 * @param __m The method used for the frame.
	 * @param __args The frame arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	SpringThreadFrame(int __level, SpringClass __cl, SpringMethod __m,
		Object... __args)
		throws NullPointerException
	{
		if (__cl == null || __m == null)
			throw new NullPointerException("NARG");
		
		__args = (__args == null ? new Object[0] : __args.clone());
		
		this.level = __level;
		this.isblank = false;
		this.method = __m;
		this.springClass = __cl;
		
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
		this.thisobject =
			(__m.flags().isStatic() ? null : (SpringObject)__args[0]);
		
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
	 * Clears the stack.
	 *
	 * @since 2018/10/13
	 */
	public final void clearStack()
	{
		this._stacktop = 0;
	}
	
	/**
	 * Increments the execution counter for this frame.
	 *
	 * @return The original execution count.
	 * @since 2018/10/12
	 */
	public final int incrementExecCount()
	{
		return this._execcount++;
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
		ByteCode code = this.code;
		if (code == null)
			return -1;
		
		return this._lastexecpc;
	}
	
	/**
	 * Returns the last executed PC index.
	 *
	 * @return The last executed PC index.
	 * @since 2024/01/26
	 */
	public final int lastExecutedPcIndex()
	{
		ByteCode code = this.code;
		if (code == null)
			return -1;
		
		// These just use indexes, not true addresses
		return code.addressToIndex(this._lastexecpc);
	}
	
	/**
	 * Returns the source line of the last executed address.
	 *
	 * @return The source line for the last executed address.
	 * @since 2018/09/24
	 */
	public final int lastExecutedPcSourceLine()
	{
		ByteCode code = this.code;
		if (code == null)
			return -1;
		
		return code.lineOfAddress(this._lastexecpc);
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
		
		/* {@squirreljme.error BK1p Cannot push local variable to the stack
		because it of the incorrect type. (The varible to push; The
		index to load from; The expected class; The value to push;
		The type of value to push)} */
		Object pushy = locals[__dx];
		if (!__cl.isInstance(pushy))
			throw new SpringVirtualMachineException(
				String.format("BK1p %s %d %s %s %s", pushy, __dx, __cl, pushy,
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
	 * Returns the number of locals.
	 *
	 * @return The number of locals.
	 * @since 2021/04/14
	 */
	public final int numLocals()
	{
		return this._locals.length;
	}
	
	/**
	 * Returns the instruction counter.
	 *
	 * @return The instruction counter.
	 * @since 2018/09/03
	 */
	public final int pc()
	{
		ByteCode code = this.code;
		if (code == null)
			return -1;
		
		return this._pc;
	}
	
	/**
	 * Returns the PC index.
	 *
	 * @return The PC index.
	 * @since 2021/04/11
	 */
	public final int pcIndex()
	{
		ByteCode code = this.code;
		if (code == null)
			return -1;
		
		// These just use indexes, not true addresses
		return code.addressToIndex(this._pc);
	}
	
	/**
	 * Returns the line of code the program counter is on.
	 *
	 * @return The program counter line of code.
	 * @since 2018/09/16
	 */
	public final int pcSourceLine()
	{
		if (this.code == null)
			return -1;
		
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
		
		/* {@squirreljme.error BK1q Stack underflow. (The current top of
		the stack; The stack limit)} */
		if (stacktop <= 0)
			throw new SpringVirtualMachineException(
				String.format("BK1q %d %d", stacktop, stack.length));
		
		// Read value and clear the value that was there
		Object rv = stack[--stacktop];
		stack[stacktop] = null;
		this._stacktop = stacktop;
		
		/* {@squirreljme.error BK1r Popped a null value of the stack, which
		should not occur.} */
		if (rv == null)
			throw new SpringVirtualMachineException("BK1r");
		
		// Is top, so pop again to read the actual desired value
		if (rv == SpringStackTop.TOP)
		{
			rv = this.popFromStack();
			
			/* {@squirreljme.error BK1s Expected long or double below
			top entry in stack. (The current top of the stack; The
			stack limit)} */
			if (!(rv instanceof Long || rv instanceof Double))
				throw new SpringVirtualMachineException(
					String.format("BK1s %d %d", stacktop, stack.length));
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
		
		/* {@squirreljme.error BK1t Popped the wrong kind of value from the
		stack. (The popped type; The expected type)} */
		Object rv = this.popFromStack();
		if (!__cl.isInstance(rv))
			throw new SpringVirtualMachineException(String.format("BK1t %s " +
					"%s",
				(rv == null ? null : rv.getClass()), __cl));
		
		return __cl.cast(rv);
	}
	
	/**
	 * Pops the given class from the stack and throws an exception if it
	 * is null.
	 *
	 * @param <C> The type to pop.
	 * @param __cl The type to pop.
	 * @return The popped data.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringNullPointerException If the popped item is null.
	 * @throws SpringVirtualMachineException If the type does not match or
	 * the stack underflows.
	 * @since 2018/12/11
	 */
	public final <C> C popFromStackNotNull(Class<C> __cl)
		throws NullPointerException, SpringNullPointerException,
		SpringVirtualMachineException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Pop from the stack
		Object rv = this.popFromStack();
		
		/* {@squirreljme.error BK1u Did not expect a null value to be
		popped from the stack.} */
		if (rv == SpringNullObject.NULL || (rv instanceof SpringNullObject))
			throw new SpringNullPointerException("BK1u");
		
		/* {@squirreljme.error BK1v Popped the wrong kind of value from the
		stack. (The popped type; The expected type)} */
		if (!__cl.isInstance(rv))
			throw new SpringVirtualMachineException(String.format("BK1v %s " +
					"%s",
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
		
		/* {@squirreljme.error BK1w Stack overflow pushing value. (The
		value; The current top of the stack; The stack limit)} */
		if (stacktop >= stack.length)
			throw new SpringVirtualMachineException(
				String.format("BK1w %s %d %d", __v, stacktop, stack.length));
		
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
	
	/**
	 * Returns the tossed exception.
	 *
	 * @return The exception being tossed.
	 * @since 2018/10/13
	 */
	public final SpringObject tossedException()
	{
		return this._tossedexception;
	}
	
	/**
	 * Sets the tossed exception for this frame.
	 *
	 * @param __o The exception tossed for this frame.
	 * @since 2018/10/13
	 */
	public final void tossException(SpringObject __o)
	{
		this._tossedexception = __o;
	}
}
