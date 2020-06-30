// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.brackets.VMThreadBracket;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.vm.springcoat.brackets.VMThreadObject;
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
	
	/** {@link ThreadShelf#createVMThread(Thread)}. */
	CREATE_VM_THREAD( "createVMThread:(Ljava/lang/Thread;)Lcc/" +
		"squirreljme/jvm/mle/brackets/VMThreadBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringSimpleObject javaThread = MLEThread.__javaThread(__thread,
				__args[0]);
			
			// Find the thread which the given passed object is bound to, this
			// is the target thread
			SpringThread target = null;
			SpringMachine machine = __thread.machine;
			synchronized (machine)
			{
				// Search through every thread
				SpringThread[] threads = machine.getThreads();
				for (SpringThread thread : threads)
				{
					SpringObject instance;
					try
					{
						instance = thread.threadInstance();
					}
					catch (IllegalStateException ignored)
					{
						continue;
					}
					
					// If this is the thread for this, then use that!
					if (javaThread == instance)
					{
						target = thread;
						break;
					}
				}
				
				// If there is exactly one thread, we can rather get into a bit
				// of a loop where our main thread is created outside of normal
				// means by the VM and not by any other thread.. but only if
				// this initial thread has no actual instance
				if (threads.length == 1 && !threads[0].hasThreadInstance())
					target = threads[0];
				
				// No actual thread exists that the object is bound to, so
				// oops! We need to actually create one here and bind it
				// accordingly!
				if (target == null)
					target = machine.createThread(null, false);
			}
			
			// Create object with this attached thread
			VMThreadObject vmThread = new VMThreadObject(target);
			
			// The thread gets these as well
			target.setThreadInstance(javaThread);
			target.setVMThread(vmThread);
			
			return vmThread;
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
			return __thread.machine._exitcode;
		}
	},
	
	/** {@link ThreadShelf#currentJavaThread()}. */
	CURRENT_JAVA_THREAD("currentJavaThread:()Ljava/lang/Thread;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.thread.threadInstance();
		}
	},
	
	/** {@link ThreadShelf#javaThreadClearInterrupt(Thread)}. */
	JAVA_THREAD_CLEAR_INTERRUPT("javaThreadClearInterrupt:" +
		"(Ljava/lang/Thread;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/28
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringFieldStorage field = MLEThread.__javaThread(__thread,
				__args[0]).fieldByNameAndType(false, 
				"_interrupted", "Z");
			
			// Get and clear the field value
			Object old = field.get();
			field.set(false);
			return old;
		}
	}, 
	
	/** {@link ThreadShelf#javaThreadFlagStarted(Thread)}. */
	JAVA_THREAD_FLAG_STARTED("javaThreadFlagStarted:(Ljava/lang/" +
		"Thread;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Just set the started field to true
			MLEThread.__javaThread(__thread, __args[0]).fieldByNameAndType(
				false, "_started", "Z").set(true);
			return null;
		}
	},
	
	/** {@link ThreadShelf#javaThreadIsStarted(Thread)}. */
	JAVA_THREAD_IS_STARTED("javaThreadIsStarted:(Ljava/lang/Thread;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Just get the state of the given field
			return MLEThread.__javaThread(__thread, __args[0])
				.fieldByNameAndType(false, 
					"_started", "Z").get();
		}
	},
	
	/** {@link ThreadShelf#javaThreadRunnable(Thread)}. */
	JAVA_THREAD_RUNNABLE("javaThreadRunnable:(Ljava/lang/Thread;)" +
		"Ljava/lang/Runnable;")
	{
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Just get the state of the given field
			return MLEThread.__javaThread(__thread, __args[0])
				.fieldByNameAndType(
				false, "_runnable",
				"Ljava/lang/Runnable;").get();
		}
	},
	
	/** {@link ThreadShelf#javaThreadSetAlive(Thread, boolean)}. */
	JAVA_THREAD_SET_ALIVE("javaThreadSetAlive:(Ljava/lang/Thread;Z)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Just set the started field to true
			MLEThread.__javaThread(__thread, __args[0]).fieldByNameAndType(
				false, "_isAlive", "Z")
				.set((int)__args[1] != 0);
			return null;
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
				throw new MLECallError("Out of range time.");
			
			if (ms == 0 && ns == 0)
				Thread.yield();
			else
				try
				{
					Thread.sleep(ms, ns);
				}
				catch (InterruptedException ignored)
				{
					return true;
				}
			
			return false;
		}
	},
	
	/** {@link ThreadShelf#toJavaThread(VMThreadBracket)}. */
	TO_JAVA_THREAD("toJavaThread:(Lcc/squirreljme/jvm/mle/" +
		"brackets/VMThreadBracket;)Ljava/lang/Thread;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/29
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			VMThreadObject vmThread = MLEThread.__vmThread(__args[0]);
			
			return vmThread.getThread().threadInstance();
		}
	}, 
	
	/** {@link ThreadShelf#toVMThread(Thread)}. */
	TO_VM_THREAD("toVMThread:(Ljava/lang/Thread;)Lcc/squirreljme/" +
		"jvm/mle/brackets/VMThreadBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEThread.__javaThread(__thread, __args[0]).fieldByField(
				__thread.resolveClass("java/lang/Thread")
				.lookupField(false, "_vmThread",
				"Lcc/squirreljme/jvm/mle/brackets/VMThreadBracket;"))
				.get();
		}
	},
	
	/** {@link ThreadShelf#vmThreadId(VMThreadBracket)}. */
	VM_THREAD_ID("vmThreadId:(Lcc/squirreljme/jvm/mle/brackets/" +
		"VMThreadBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEThread.__vmThread(__args[0]).getThread().id;
		}
	},
	
	/** {@link ThreadShelf#vmThreadInterrupt(VMThreadBracket)}. */ 
	VM_THREAD_INTERRUPT("vmThreadInterrupt:(Lcc/squirreljme/jvm/mle/" +
		"brackets/VMThreadBracket;)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/22
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			VMThreadObject vmThread = MLEThread.__vmThread(__args[0]);
			
			// Send an interrupt to the thread
			vmThread.getThread().hardInterrupt();
			
			return null;
		}
	},
	
	/** {@link ThreadShelf#vmThreadIsMain(VMThreadBracket)}. */
	VM_THREAD_IS_MAIN("vmThreadIsMain:(Lcc/squirreljme/jvm/mle/" +
		"brackets/VMThreadBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEThread.__vmThread(__args[0]).getThread().isMain();
		}
	},
	
	/** {@link ThreadShelf#vmThreadSetPriority(VMThreadBracket, int)}. */
	VM_THREAD_SET_PRIORITY("vmThreadSetPriority:(Lcc/squirreljme/" +
		"jvm/mle/brackets/VMThreadBracket;I)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/29
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringThread thread = MLEThread.__vmThread(__args[0]).getThread();
			int priority = (int)__args[1];
			
			if (priority < Thread.MIN_PRIORITY ||
				priority > Thread.MAX_PRIORITY)
				throw new MLECallError("Thread priority out of bounds.");
			
			// Try to set the priority
			try
			{
				thread._worker.setPriority(priority);
			}
			catch (IllegalArgumentException|SecurityException e)
			{
				throw new MLECallError("Could not set priority.", e);
			}
			
			return null;
		}
	}, 
	
	/** {@link ThreadShelf#vmThreadStart(VMThreadBracket)}. */
	VM_THREAD_START("vmThreadStart:(Lcc/squirreljme/jvm/mle/brackets/" +
		"VMThreadBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringThread target = MLEThread.__vmThread(__args[0]).getThread();
			
			// Create worker for thread and start it
			SpringThreadWorker worker = new SpringThreadWorker(
				__thread.machine, target, false);
			
			// Enter the base setup frame
			target.enterFrame(worker.loadClass(MLEThread._START_CLASS)
				.lookupMethod(true, MLEThread._BASE_THREAD_METHOD));
			
			// Try to start it
			try
			{
				worker.start();
				return true;
			}
			catch (IllegalThreadStateException ignored)
			{
				return false;
			}
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
				throw new MLECallError("Negative milliseconds");
			
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
	
	/**
	 * Checks if this is a Java thread.
	 * 
	 * @param __thread The context thread.
	 * @param __object The object to check.
	 * @return The verified object.
	 * @throws SpringMLECallError If {@code __object} is {@code null} or is
	 * not an instance of {@link Throwable}.
	 * @since 2020/06/28
	 */
	static SpringSimpleObject __javaThread(SpringThreadWorker __thread,
		Object __object)
		throws SpringMLECallError
	{
		if (__thread == null)
			throw new NullPointerException("NARG");
		
		if (!(__object instanceof SpringSimpleObject))
			throw new SpringMLECallError("Not a Java Thread");
		
		SpringSimpleObject rv = (SpringSimpleObject)__object;
		if (!rv.type().isAssignableFrom(
			__thread.resolveClass("java/lang/Thread")))
			throw new SpringMLECallError("Not instance of Thread.");
		
		return rv;
	}
	
	/**
	 * Ensures that this is a {@link VMThreadObject}.
	 * 
	 * @param __object The object to check.
	 * @return As a {@link VMThreadObject}.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2020/06/27
	 */
	static VMThreadObject __vmThread(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof VMThreadObject))
			throw new SpringMLECallError("Not a VMThreadObject.");
		
		return (VMThreadObject)__object; 
	}
}
