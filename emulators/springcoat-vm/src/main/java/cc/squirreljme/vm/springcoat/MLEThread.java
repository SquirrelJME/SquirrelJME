// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.emulator.profiler.ProfiledFrame;
import cc.squirreljme.jdwp.host.JDWPHostController;
import cc.squirreljme.jdwp.host.trips.JDWPGlobalTrip;
import cc.squirreljme.jdwp.host.trips.JDWPTripThread;
import cc.squirreljme.jdwp.host.trips.JDWPTripVmState;
import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.brackets.TracePointBracket;
import cc.squirreljme.jvm.mle.constants.ThreadModelType;
import cc.squirreljme.jvm.mle.constants.ThreadStatusType;
import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Functions for {@link ThreadShelf}.
 *
 * @since 2020/06/18
 */
public enum MLEThread
	implements MLEFunction
{
	/** {@link ThreadShelf#aliveThreadCount(boolean, boolean)}. */
	ALIVE_THREAD_COUNT("aliveThreadCount:(ZZ)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			boolean includeMain = ((int)__args[0] != 0);
			boolean includeDaemon = ((int)__args[1] != 0);
			
			// Count every thread
			int count = 0;
			SpringMachine machine = __thread.machine;
			synchronized (machine)
			{
				for (SpringThread thread : machine.getThreads())
				{
					// Ignore any threads that are marked terminated or has not
					// been started as it has no frames
					if (thread.isTerminated() || thread.numFrames() == 0)
						continue;
					
					boolean isMain = thread.isMain();
					boolean isDaemon = thread.isDaemon();
					
					if ((includeMain && isMain) ||
						(includeDaemon && isDaemon) ||
						(!isMain && !isDaemon))
						count++;
				}
			}
			
			return count;
		}
	},
	
	/** {@link ThreadShelf#vmThreadInit(Thread, Runnable)}. */
	VM_THREAD_INIT( "vmThreadInit:(Ljava/lang/Thread;" +
		"Ljava/lang/Runnable;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringThread target = MLEObjects.thread(__args[0]);
			SpringObject runnable = MLEObjects.object(__args[1]);
			
			// The second must be a runnable
			if (runnable != null && runnable != SpringNullObject.NULL)
				if (runnable.type().isAssignableFrom(
					__thread.resolveClass("java/lang/Runnable")))
					throw new SpringMLECallError("Not a Runnable!.");
			
			// Set target runnable, once!
			synchronized (target)
			{
				target.initRunnable(runnable);
			}
			
			return null;
		}
	},
	
	/** {@link ThreadShelf#currentExitCode()}. */
	CURRENT_EXIT_CODE("currentExitCode:()I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.machine.getExitCode();
		}
	},
	
	/** {@link ThreadShelf#currentThread()}. */
	CURRENT_THREAD("currentThread:()Ljava/lang/Thread;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.thread;
		}
	},
	
	
	/** {@link ThreadShelf#equals(Thread, Thread)}. */
	EQUALS("equals:(Ljava/lang/Thread;" +
		"Ljava/lang/Thread;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/05/08
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.thread(__args[0]) == MLEObjects.thread(
				__args[1]);
		}
	}, 
	
	/** {@link ThreadShelf#vmThreadInterruptClear(Thread)}. */
	JAVA_THREAD_CLEAR_INTERRUPT("vmThreadInterruptClear:" +
		"(Ljava/lang/Thread;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/28
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			throw Debugging.todo();
		}
	}, 
	
	/** {@link ThreadShelf#vmThreadRunnable(Thread)}. */
	JAVA_THREAD_RUNNABLE("vmThreadRunnable:(Ljava/lang/Thread;)" +
		"Ljava/lang/Runnable;")
	{
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.thread(__args[0])._runnable;
		}
	},
	
	/** {@link ThreadShelf#vmThreadSetDaemon(Thread)}. */ 
	JAVA_THREAD_SET_DAEMON("vmThreadSetDaemon:(Ljava/lang/Thread;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/09/12
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringThread vmThread = MLEObjects.thread(__args[0]);
			
			synchronized (vmThread)
			{
				// Cannot be changed once started
				if (vmThread.isTerminated() || vmThread.numFrames() > 0)
					throw new SpringMLECallError("Thread is started.");
				
				// Set as a daemon thread
				vmThread.setDaemon();
			}
			
			// No value is returned
			return null;
		}
	},
	
	/** {@link ThreadShelf#model()}. */ 
	MODEL("model:()I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/05/07
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// SpringCoat is always multi-threaded
			return ThreadModelType.MULTI_THREAD;
		}
	},
	
	/** {@link ThreadShelf#runProcessMain()}. */
	RUN_PROCESS_MAIN("runProcessMain:()V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			__thread.runProcessMain();
			return null;
		}
	},
	
	/** {@link ThreadShelf#setCurrentExitCode(int)}. */
	SET_CURRENT_EXIT_CODE("setCurrentExitCode:(I)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/27
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			int exitCode = (int)__args[0];
			
			__thread.machine.setExitCode(exitCode);
			
			return null;
		}
	},
	
	/** {@link ThreadShelf#setTrace(String, TracePointBracket[])}. */ 
	SET_TRACE("setTrace:(Ljava/lang/String;[Lcc/squirreljme/" +
		"jvm/mle/brackets/TracePointBracket;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/06
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			if (!(__args[1] instanceof SpringArrayObjectGeneric))
				throw new SpringMLECallError("Wrong trace array type.");
			SpringObject[] gen = ((SpringArrayObjectGeneric)__args[1]).array();
			
			// Get the message used
			String message = __thread.<String>asNativeObject(String.class,
				__args[0]);
			if (message == null)
				throw new SpringMLECallError("No message set.");
			
			// Map trace points to the call trace for future get
			int n = gen.length;
			CallTraceElement[] trace = new CallTraceElement[n];
			for (int i = 0; i < n; i++)
				trace[i] = MLEObjects.debugTrace(gen[i]).getTrace();
			
			// Store the call trace for other tasks to get
			__thread.machine.storeTrace(message, trace);
			return null;
		}
	},
	
	/** {@link ThreadShelf#sleep(int, int)}. */
	SLEEP("sleep:(II)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			int ms = (int)__args[0];
			int ns = (int)__args[1];
			
			if (ms < 0 || ns < 0 || ns > 1000000000)
				throw new SpringMLECallError("Out of range time.");
			
			// Get the profiler information
			SpringThreadFrame currentFrame = __thread.thread.currentFrame();
			ProfiledFrame profiler = (currentFrame == null ? null :
				currentFrame._profiler);
			
			// We need to restore profiler states
			boolean interrupted = false;
			try
			{
				// Indicate that we are in sleep mode
				__thread.thread.setStatus(ThreadStatusType.SLEEPING);
				
				// Stop counting CPU time for this
				if (profiler != null)
					profiler.sleep(true, System.nanoTime());
				
				// Just giving up CPU time?
				if (ms == 0 && ns == 0)
					Thread.yield();
				
				// Normal sleep
				else
					try
					{
						Thread.sleep(ms, ns);
					}
					catch (InterruptedException ignored)
					{
						interrupted = true;
					}
			}
			finally
			{
				// We have left sleep mode
				__thread.thread.setStatus(ThreadStatusType.RUNNING);
				
				// Continue counting CPU time
				if (profiler != null)
					profiler.sleep(false, System.nanoTime());
			}
			
			return interrupted;
		}
	},
	
	/** {@link ThreadShelf#vmThreadId(Thread)}. */
	VM_THREAD_ID("vmThreadId:(Ljava/lang/Thread;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.thread(__args[0]).id;
		}
	},
	
	/** {@link ThreadShelf#vmThreadInterrupt(Thread)}. */ 
	VM_THREAD_INTERRUPT("vmThreadInterrupt:(Ljava/lang/Thread;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/22
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringThread vmThread = MLEObjects.thread(__args[0]);
			
			// Send an interrupt to the thread
			vmThread.hardInterrupt();
			
			return null;
		}
	},
	
	/** {@link ThreadShelf#vmThreadIsAlive(Thread)}. */
	VM_THREAD_IS_ALIVE("vmThreadIsAlive:(Ljava/lang/Thread;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringThread thread = MLEObjects.thread(__args[0]);
			return thread._worker != null && !thread.isTerminated();
		}
	},
	
	/** {@link ThreadShelf#vmThreadIsMain(Thread)}. */
	VM_THREAD_IS_MAIN("vmThreadIsMain:(Ljava/lang/Thread;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.thread(__args[0]).isMain();
		}
	},
	
	/** {@link ThreadShelf#vmThreadIsStarted(Thread)}. */
	VM_THREAD_IS_STARTED("vmThreadIsStarted:(Ljava/lang/Thread;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringThread thread = MLEObjects.thread(__args[0]);
			return thread._worker != null;
		}
	},
	
	/** {@link ThreadShelf#vmThreadName(Thread)}. */
	VM_THREAD_NAME(MLEDispatcher.methodKey("vmThreadName",
		String.class, Thread.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2026/03/01
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringThread thread = MLEObjects.thread(__args[0]);
			
			// Return the current name
			return __thread.asVMObject(thread.name());
		}
	},
	
	/** {@link ThreadShelf#vmThreadName(Thread, String)}. */
	VM_THREAD_NAME_SET(MLEDispatcher.methodKey("vmThreadName",
		void.class, Thread.class, String.class))
	{
		/**
		 * {@inheritDoc}
		 * @since 2026/03/01
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringThread thread = MLEObjects.thread(__args[0]);
			String name = MLEObjects.string(__args[1]);
			
			// Set new name
			thread.name(name);
			return null;
		}
	},
	
	/** {@link ThreadShelf#vmThreadPriority(Thread, int)}. */
	VM_THREAD_SET_PRIORITY("vmThreadPriority:(Ljava/lang/Thread;I)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/29
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringThread thread = MLEObjects.thread(__args[0]);
			int priority = (int)__args[1];
			
			if (priority < Thread.MIN_PRIORITY ||
				priority > Thread.MAX_PRIORITY)
				throw new SpringMLECallError(
					"Thread priority out of range.");
			
			// Try to set the priority
			try
			{
				if (thread._worker == null)
					thread._initPriority = priority;
				else
					thread._worker.setPriority(priority);
			}
			catch (IllegalArgumentException|SecurityException e)
			{
				throw new SpringMLECallError(
					"Could not set priority.", e);
			}
			
			return null;
		}
	}, 
	
	/** {@link ThreadShelf#vmThreadStart(Thread)}. */
	VM_THREAD_START("vmThreadStart:(Ljava/lang/Thread;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringThread target = MLEObjects.thread(__args[0]);
			
			// Create worker for thread and start it
			SpringThreadWorker worker = new SpringThreadWorker(
				__thread.machine, target, false);
			
			// Inherited verbose flags for this new thread?
			if (target._initVerboseFlags != 0)
				worker.verbose().add(0, target._initVerboseFlags);
			
			// Enter the base setup frame
			target.enterFrame(worker.loadClass(MLEThread._START_CLASS)
				.lookupMethod(true, MLEThread._BASE_THREAD_METHOD));
			
			// Try to start it
			try
			{
				// Start it
				worker.start();
				
				// If we are debugging, we are going to need to tell the
				// debugger some important details
				JDWPHostController jdwp = target.machine()
					.taskManager().jdwpController;
				if (jdwp != null)
				{
					// If we are debugging, we need to tell the debugger that
					// the virtual machine actually started
					if (target.machine().rootVm && target.isMain())
						jdwp.<JDWPTripVmState>trip(JDWPTripVmState.class,
							JDWPGlobalTrip.VM_STATE).alive(target, true);
					
					// If we are debugging, signal that this thread is in the
					// start state. We need the instance to have been set for 
					// this to even properly work!
					jdwp.<JDWPTripThread>trip(JDWPTripThread.class,
						JDWPGlobalTrip.THREAD).alive(target, true);
				}
				
				return true;
			}
			catch (IllegalThreadStateException ignored)
			{
				return false;
			}
		}
	},
	
	/** {@link ThreadShelf#vmThreadTask(Thread)}. */
	VM_THREAD_TASK("vmThreadTask:(Ljava/lang/Thread;)Lcc/squirreljme/jvm/mle/brackets/TaskBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2021/05/08
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.thread(__args[0])
				.machine().taskObject(__thread.machine);
		}
	}, 
	
	/** {@link ThreadShelf#waitForUpdate(int)}. */
	WAIT_FOR_UPDATE("waitForUpdate:(I)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/29
		 */
		@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			int ms = (int)__args[0];
			
			if (ms < 0)
				throw new SpringMLECallError("Negative milliseconds");
			
			// Waiting for nothing? just give up our slice
			if (ms == 0)
			{
				Thread.yield();
				return false;
			}
			
			// Wait until the monitor is hit
			SpringMachine machine = __thread.machine;
			synchronized (machine)
			{
				try
				{
					machine.wait(ms);
				}
				catch (InterruptedException e)
				{
					return true;
				}
			}
			
			// Assume not interrupted
			return false;
		}
	}, 
	
	/* End. */
	;
	
	/** The class which contains the thread starting point. */
	static final ClassName _START_CLASS =
		new ClassName("java/lang/__Start__");
	
	/** The method to enter for main threads. */
	static final MethodNameAndType _BASE_THREAD_METHOD =
		new MethodNameAndType("__base", "()V");
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/18
	 */
	MLEThread(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/18
	 */
	@Override
	public String key()
	{
		return this.key;
	}
	
}
