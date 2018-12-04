// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.asm.NativeDisplayEventCallback;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This is the callback to use which calls the given spring object, this is
 * needed by the native display.
 *
 * @since 2018/12/03
 */
public final class SpringDisplayEventCallback
	implements NativeDisplayEventCallback
{
	/** The class used for callback. */
	private static final ClassName _CALLBACK_CLASS =
		new ClassName("cc/squirreljme/runtime/cldc/asm/" +
			"NativeDisplayEventCallback");
	
	/** The object to call into. */
	protected final SpringObject object;
	
	/** The thread to use. */
	protected final SpringThread thread;
	
	/** The worker for threads. */
	protected final SpringThreadWorker worker;
	
	/**
	 * Initializes the callback to call the given object.
	 *
	 * @param __m The machine owning this.
	 * @param __o The object to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/03
	 */
	public SpringDisplayEventCallback(SpringMachine __m, SpringObject __o)
		throws NullPointerException
	{
		if (__m == null || __o == null)
			throw new NullPointerException("NARG");
		
		// Set object to call
		this.object = __o;
		
		// Setup thread
		SpringThread thread = __m.createThread("SpringCoat-LCDUIThread");
		SpringThreadWorker worker = new SpringThreadWorker(__m, thread, false);
		
		// Need to initialize a thread object because this thread has to be
		// registered with the runtime in order for programs to operate
		// correctly on it, even though it is a weird thread
		// This is always registered until it is explicitely not registered
		SpringObject fakethread = worker.newInstance(
			new ClassName("java/lang/Thread"), new MethodDescriptor(
				"(ILjava/lang/String;)V"), thread.id,
				worker.asVMObject(thread.name));
		
		// Store
		this.thread = thread;
		this.worker = worker;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void command(int __d, int __c)
	{
		this.__exec("command", "(II)V", __d, __c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void exitRequest(int __d)
	{
		this.__exec("exitRequest", "(I)V", __d);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void keyEvent(int __d, int __ty, int __kc, int __ch,
		int __time)
	{
		this.__exec("keyEvent", "(IIIII)V", __d, __ty, __kc, __ch, __time);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void paintDisplay(int __d, int __x, int __y,
		int __w, int __h)
	{
		this.__exec("paintDisplay", "(IIIII)V", __d, __x, __y, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void pointerEvent(int __d, int __ty, int __x, int __y,
		int __time)
	{
		this.__exec("pointerEvent", "(IIIII)V", __d, __ty, __x, __y, __time);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void shown(int __d, int __shown)
	{
		this.__exec("shown", "(II)V", __d, __shown);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/03
	 */
	@Override
	public final void sizeChanged(int __d, int __w, int __h)
	{
		this.__exec("sizeChanged", "(III)V", __d, __w, __h);
	}
	
	/**
	 * Executes the given function in the callback.
	 *
	 * @param __func The function to call.
	 * @param __desc The descriptor.
	 * @param __args The arguments to call.
	 * @return The object value.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/03
	 */
	private final Object __exec(String __func, String __desc, Object... __args)
		throws NullPointerException
	{
		if (__func == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Need object and our class
		SpringObject object = this.object;
		
		// Copy arguments to seed our object
		int n = __args.length;
		Object[] args = new Object[n + 1];
		args[0] = object;
		for (int i = 0, o = 1; i < n; i++, o++)
			args[o] = __args[i];
		
		// Call method
		SpringThreadWorker worker = this.worker;
		Object rv;
		try
		{
			rv = worker.invokeMethod(false,
				_CALLBACK_CLASS, new MethodNameAndType(__func, __desc), args);
		}
		
		// The VM is exiting from the method, we cannot propogate the method
		// up we could just do nothing, just cancel what has happened.
		catch (SpringMachineExitException e)
		{
			// Debug it
			todo.DEBUG.note("VM in exit state, canceling display function.");
			
			return null;
		}
		
		// Handle return value or keep it as void
		if (rv != null)
			return worker.asNativeObject(rv);
		return null;
	}
}

