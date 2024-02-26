// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.emulator.profiler.ProfiledThread;
import cc.squirreljme.jdwp.host.JDWPHostController;
import cc.squirreljme.jdwp.host.JDWPHostStepTracker;
import cc.squirreljme.jdwp.host.JDWPHostThreadSuspension;
import cc.squirreljme.jdwp.host.trips.JDWPGlobalTrip;
import cc.squirreljme.jdwp.host.trips.JDWPTripThread;
import cc.squirreljme.jvm.mle.constants.ThreadStatusType;
import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.runtime.cldc.debug.CallTraceUtils;
import cc.squirreljme.vm.springcoat.brackets.VMThreadObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This class contains information about a thread within the virtual machine.
 *
 * @since 2018/09/01
 */
public final class SpringThread
{
	/** Maximum depth of the stack. */
	public static final int MAX_STACK_DEPTH =
		64;
	
	/** The thread ID. */
	protected final int id;
	
	/** Is this a main thread? */
	protected final boolean main;
	
	/** The name of this thread. */
	protected final String name;
	
	/** Profiler information. */
	protected final ProfiledThread profiler;
	
	/** Tracker for debugging suspension. */
	protected final JDWPHostThreadSuspension debuggerSuspension =
		new JDWPHostThreadSuspension();
	
	/** The virtual machine reference. */
	protected final Reference<SpringMachine> machineRef;
	
	/** Unique thread ID. */
	protected final int uniqueId;
	
	/** The stack frames. */
	private final SpringThreadFrames _frames =
		new SpringThreadFrames();
	
	/** Do not allow debug suspension, as in this is a debugger thread. */
	public final boolean noDebugSuspend;
	
	/** Inherited verbose flags to use. */
	int _initVerboseFlags;
	
	/** Initial priority to set. */
	int _initPriority =
		-1;
	
	/** The thread status. */
	int _status;
	
	/** The thread's {@link Thread} instance. */
	private SpringObject _threadInstance;
	
	/** The thread's VM Thread instance. */
	private VMThreadObject _vmThread;
		
	/** String representation. */
	private Reference<String> _string;
	
	/** Ran at least one frame (was started)? */
	private volatile boolean _hadoneframe;
	
	/** Is this a daemon thread? */
	volatile boolean _daemon;
	
	/** Did we signal exit? */
	volatile boolean _signaledexit;
	
	/** The current worker for the thread. */
	volatile SpringThreadWorker _worker;
	
	/** Terminate the thread? */
	private volatile boolean _terminate;
	
	/** Step tracker. */
	volatile JDWPHostStepTracker _stepTracker;
	
	/**
	 * Initializes the thread.
	 *
	 * @param __machRef The machine reference.
	 * @param __id The thread ID.
	 * @param __uniqueId Unique ID for debugging.
	 * @param __main Is this a main thread.
	 * @param __n The name of the thread.
	 * @param __profiler Profiled storage.
	 * @param __noDebugSuspend Do not allow the debugger to suspend this
	 * thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/01
	 */
	SpringThread(Reference<SpringMachine> __machRef, int __id, int __uniqueId,
		boolean __main, String __n, ProfiledThread __profiler,
		boolean __noDebugSuspend)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.machineRef = __machRef;
		this.id = __id;
		this.uniqueId = __uniqueId;
		this.main = __main;
		this.name = __n;
		this.profiler = __profiler;
		this.noDebugSuspend = __noDebugSuspend;
	}
	
	/**
	 * Returns the current frame of execution or {@code null} if there is none.
	 *
	 * @return The current frame of execution or {@code null} if there is none.
	 * @since 2018/09/03
	 */
	public final SpringThreadFrame currentFrame()
	{
		return this._frames.current();
	}
	
	/**
	 * Enters a blank frame to store data.
	 *
	 * @return The newly created frame.
	 * @since 2018/09/20
	 */
	public final SpringThreadFrame enterBlankFrame()
	{
		// Cannot enter frames when terminated
		if (this.isTerminated())
			throw new SpringVirtualMachineException(
				"Cannot enter frame on terminated thread.");
		
		// Enter blank frame
		SpringThreadFrame frame = this._frames.enterBlank();
		
		// Had one frame (started)
		this._hadoneframe = true;
		
		return frame;
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
	public final SpringThreadFrame enterFrame(SpringMethod __m,
		Object... __args)
		throws NullPointerException, SpringVirtualMachineException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Cannot enter frames when terminated
		if (this.isTerminated())
			throw new SpringVirtualMachineException(
				"Cannot enter frame on terminated thread.");
		
		if (__args == null)
			__args = new Object[0];
		
		/* {@squirreljme.error BK1k Cannot enter the frame for a method which
		is abstract. (The class the method is in; The method name and type)} */
		if (__m.isAbstract())
			throw new SpringVirtualMachineException(String.format("BK1k %s %s",
				__m.inClass(), __m.nameAndType()));
				
		SpringThreadWorker worker = this._worker;
		
		// Convert all the object to virtual machine objects if they are
		// not already
		Object[] vmArgs = Arrays.copyOf(__args, __args.length);
		if (worker != null)
			for (int i = 0, n = __args.length; i < n; i++)
				vmArgs[i] = worker.asVMObject(vmArgs[i], true);
		
		// Create new frame
		SpringThreadFrame rv = this._frames.enter(
			this._worker.loadClass(__m.inClass()), __m, vmArgs);
		
		// Profile for this frame
		rv._profiler = this.profiler.enterFrame(__m.inClass().toString(),
			__m.nameAndType().name().toString(),
			__m.nameAndType().type().toString(), System.nanoTime());
		
		// Had one frame (started)
		this._hadoneframe = true;
		
		// Handle synchronized method
		if (__m.flags().isSynchronized())
		{
			SpringObject monitor;
			
			// Monitor on the class object, needs the worker since we need to
			// load a class
			if (__m.flags().isStatic())
			{
				/* {@squirreljme.error BK1l Cannot enter a synchronized static
				method without a thread working, since we need to load
				the class object.} */
				if (worker == null)
					throw new SpringVirtualMachineException("BK1l");
				
				// Use the class object
				monitor = (SpringObject)worker.asVMObject(
					__m.inClass(), true);
			}
			
			// On this object
			else
			{
				/* {@squirreljme.error BK1m Cannot enter a synchronized
				instance method with no arguments passed.} */
				if (__args.length <= 0)
					throw new SpringVirtualMachineException("BK1m");
				
				/* {@squirreljme.error BK1n Cannot enter a monitor of nothing
				or a non-object.} */
				Object argzero = __args[0];
				if (!(argzero instanceof SpringObject))
					throw new SpringVirtualMachineException("BK1n");
				
				// Use this as the monitor
				monitor = (SpringObject)argzero;
			}
			
			// Set to unlock later on
			rv._monitor = monitor;
			
			// Enter the monitor and just wait around
			monitor.monitor().enter(this);
		}
		
		return rv;
	}
	
	/**
	 * Exits all frames in the stack.
	 *
	 * @since 2018/11/17
	 */
	public final void exitAllFrames()
	{
		this._frames.exitAll();
	}
	
	/**
	 * Returns all the frames which are available.
	 *
	 * @return All of the available stack frames.
	 * @since 2018/09/16
	 */
	public final SpringThreadFrame[] frames()
	{
		return this._frames.all();
	}
	
	/**
	 * Returns the stack trace for this thread.
	 * 
	 * @return The stack trace for this thread.
	 * @since 2020/06/13
	 */
	public final CallTraceElement[] getStackTrace()
	{
		// Gather all frames
		SpringThreadFrame[] frames = this._frames.all();
		
		// Setup target array
		int n = frames.length;
		CallTraceElement[] rv = new CallTraceElement[n];
		
		// The frames at the end are at the top
		for (int i = n - 1, write = 0; i >= 0; i--, write++)
		{
			SpringThreadFrame frame = frames[i];
			CallTraceElement trace;
			
			// Blanks are purely virtual standing points so they are
			// regarded as such
			if (frame.isBlank())
			{
				trace = new CallTraceElement(
					"<guard>", "<guard>", null,
					0L, null, -1);
			}
			
			// Print other parts
			else
			{
				SpringMethod inMethod = frame.method();
				int pc = frame.lastExecutedPc();
				
				trace = new CallTraceElement(
					inMethod.inClass().toString(),
					inMethod.name().toString(),
					inMethod.nameAndType().type().toString(),
					0,
					inMethod.infile,
					inMethod.byteCode().lineOfAddress(pc),
					inMethod.byteCode().getByAddress(pc).operation(),
					pc);
			}
			
			// Store trace in top-most order
			rv[write] = trace;
		}
		
		return rv;
	}
	
	/**
	 * Interrupts this thread.
	 * 
	 * @since 2020/06/22
	 */
	public final void hardInterrupt()
	{
		SpringThreadWorker worker = this._worker;
		if (worker == null)
			throw new IllegalStateException(
				"Cannot interrupt thread with no worker.");
		
		// Signal the other thread or something else?
		Thread signal = this._worker.signalinstead;
		if (signal != null)
			signal.interrupt();
		else
			worker.interrupt();
	}
	
	/**
	 * Returns whether or not this thread has an instance.
	 *
	 * @return If this thread has an instance.
	 * @since 2020/06/18
	 */
	public final boolean hasThreadInstance()
	{
		synchronized (this)
		{
			return this._threadInstance != null;
		}
	}
	
	/**
	 * Invokes the given method, this forwards to
	 * {@link SpringThreadWorker#invokeMethod(boolean, ClassName,
	 * MethodNameAndType, Object...)}.
	 * 
	 * @param __static Is the method static?
	 * @param __cl The class to execute from within.
	 * @param __nat The method to be invoked.
	 * @param __args The arguments to the call.
	 * @return The return value from the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/15
	 */
	public final Object invokeMethod(boolean __static, ClassName __cl,
		MethodNameAndType __nat, Object... __args)
		throws NullPointerException
	{
		if (__cl == null || __nat == null || __args == null)
			throw new NullPointerException("NARG");
		
		return this._worker.invokeMethod(__static, __cl, __nat, __args);
	}
	
	/**
	 * Returns the machine that created this.
	 * 
	 * @return The machine that created this.
	 * @since 2021/03/16
	 */
	public SpringMachine machine()
	{
		SpringThreadWorker worker = this._worker;
		return (worker == null ? this.machineRef.get() : worker.machine);
	}
	
	/**
	 * Is this a daemon thread?
	 *
	 * @return If this is a daemon thread.
	 * @since 2020/06/17
	 */
	public final boolean isDaemon()
	{
		return this._daemon;
	}
	
	/**
	 * Is exiting the virtual machine okay?
	 *
	 * @return If it is okay to exit.
	 * @since 2018/11/17
	 */
	public final boolean isExitOkay()
	{
		return this._daemon || this._terminate;
	}
	
	/**
	 * If this is a main thread or not.
	 *
	 * @return If this is a main thread.
	 * @since 2020/06/17
	 */
	public final boolean isMain()
	{
		return this.main;
	}
	
	/**
	 * Returns if this thread has terminated.
	 * 
	 * @return If this thread has terminated.
	 * @since 2020/06/29
	 */
	public final boolean isTerminated()
	{
		synchronized (this)
		{
			return this._terminate;
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
		return this._frames.count();
	}
	
	/**
	 * Pops a frame from the thread stack.
	 *
	 * @return The frame which was popped.
	 * @throws SpringVirtualMachineException If there are no stack frames.
	 * @since 2018/09/09
	 */
	public final SpringThreadFrame popFrame()
		throws SpringVirtualMachineException
	{
		// Pop from the stack
		SpringThreadFrame rv = this._frames.pop();
		
		// Exit the frame, if not blank
		if (!rv.isblank)
			this.profiler.exitFrame(System.nanoTime());
		
		// If there is a monitor associated with this then leave it
		SpringObject monitor = rv._monitor;
		if (monitor != null)
			monitor.monitor().exit(this, true);
		
		return rv;
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
		
		// Use standard SquirrelJME trace printing here
		CallTraceUtils.printStackTrace(__ps,
			String.format("SpringThread #%d: %s", this.id, this.name),
			this.getStackTrace(), null, null,
			0);
	}
	
	/**
	 * Sets this thread as a daemon thread.
	 * 
	 * @since 2020/09/12
	 */
	public void setDaemon()
	{
		synchronized (this)
		{
			this._daemon = true;
		}
	}
	
	/**
	 * Sets the thread status.
	 * 
	 * @param __status The {@link ThreadStatusType} to set.
	 * @since 2021/03/15
	 */
	public void setStatus(int __status)
	{
		synchronized (this)
		{
			this._status = __status;
		}
	}
	
	/**
	 * Sets the {@link Thread} instance.
	 *
	 * @param __object The object to set the instance to.
	 * @throws IllegalStateException If this thread already has an instance.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public final void setThreadInstance(SpringObject __object)
		throws IllegalStateException, NullPointerException
	{
		if (__object == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Only a single instance is permitted
			if (this._threadInstance != null)
				throw new IllegalStateException("Thread has an instance.");
			
			this._threadInstance = __object;
		}
	}
	
	/**
	 * Sets the virtual machine thread of this thread.
	 *
	 * @param __vmThread The thread to set.
	 * @throws IllegalStateException If this thread already has one.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/17
	 */
	public final void setVMThread(VMThreadObject __vmThread)
		throws IllegalStateException, NullPointerException
	{
		if (__vmThread == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			if (this._vmThread != null)
				throw new IllegalStateException("Thread has VM Thread.");
			
			this._vmThread = __vmThread;
		}
	}
	
	/**
	 * Terminates this thread.
	 * 
	 * @since 2020/06/29
	 */
	public final void terminate()
	{
		// Terminates this thread
		synchronized (this)
		{
			// Set as terminated
			this._terminate = true;
		}
		
		// Signal to the machine that this thread terminated
		SpringThreadWorker worker = this._worker;
		if (worker != null)
			worker.machine.signalThreadTerminate(this);
		
		// If debugging, signal that the thread is no longer alive
		JDWPHostController jdwp = this.machine().taskManager().jdwpController;
		if (jdwp != null)
			jdwp.<JDWPTripThread>trip(JDWPTripThread.class,
				JDWPGlobalTrip.THREAD).alive(this, false);
	}
	
	/**
	 * Returns the instance of the {@link Thread} object for this thread.
	 *
	 * @return The instance of {@link Thread}.
	 * @throws IllegalStateException If the thread has no instance.
	 * @since 2020/06/17
	 */
	public final SpringObject threadInstance()
		throws IllegalStateException
	{
		SpringObject rv;
		synchronized (this)
		{
			rv = this._threadInstance;
		}
		
		if (rv == null)
			throw new IllegalStateException("Thread has no instance.");
		
		return rv;
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
}

