// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.emulator.MLECallWouldFail;
import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.brackets.JarPackageBracket;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.jvm.mle.constants.TaskPipeRedirectType;
import cc.squirreljme.jvm.mle.constants.TaskStatusType;
import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.springcoat.brackets.TracePointObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This contains the implementation of {@link TaskShelf}.
 *
 * @since 2020/07/08
 */
public enum MLETask
	implements MLEFunction
{
	/** {@link TaskShelf#active()}. */
	ACTIVE("active:()[Lcc/squirreljme/jvm/mle/brackets/TaskBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/09
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringMachine[] tasks = __thread.machine.taskManager().tasks();
			
			// Map to array
			int n = tasks.length;
			SpringObject[] result = new SpringObject[n];
			for (int i = 0; i < n; i++)
				result[i] = tasks[i].taskObject(__thread.machine);
			
			return __thread.asVMObjectArray(__thread.resolveClass(
				"[Lcc/squirreljme/jvm/mle/brackets/TaskBracket;"), result);
		}
	},
	
	/** {@link TaskShelf#current()}. */
	CURRENT("current:()Lcc/squirreljme/jvm/mle/brackets/TaskBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/08
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.machine.taskObject(__thread.machine);
		}
	},
	
	/** {@link TaskShelf#equals(TaskBracket, TaskBracket)}. */
	EQUALS("equals:(Lcc/squirreljme/jvm/mle/brackets/TaskBracket;" +
		"Lcc/squirreljme/jvm/mle/brackets/TaskBracket;)Z")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/08
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.task(__args[0]).getMachine() ==
				MLEObjects.task(__args[1]).getMachine();
		}
	},
	
	/** {@link TaskShelf#exitCode(TaskBracket)}. */
	EXIT_CODE("exitCode:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TaskBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/08
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.task(__args[0]).getMachine().getExitCode();
		}
	},
	
	/** {@link TaskShelf#getTrace(TaskBracket, String[])}. */
	GET_TRACE("getTrace:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TaskBracket;[Ljava/lang/String;)[Lcc/squirreljme/jvm/mle/" +
		"brackets/TracePointBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/08
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			if (!(__args[1] instanceof SpringArrayObjectGeneric))
				throw new SpringMLECallError("Not an array.");
			
			SpringMachine machine = MLEObjects.task(__args[0]).getMachine();
			SpringArrayObjectGeneric array =
				(SpringArrayObjectGeneric)__args[1];
			if (array.length() <= 0)
				throw new SpringMLECallError("Empty array.");
			
			// Only return if we did have actual storage
			CallTraceStorage storage = machine.getTrace();
			if (storage == null)
				return null;
			
			// Set the output message
			array.set(0, __thread.asVMObject(storage.message));
			
			// The traces to be mapped
			CallTraceElement[] inTraces = storage.trace();
			int len = inTraces.length;
			
			// The resultant traces
			SpringObject[] outTraces = new SpringObject[len];
			for (int i = 0; i < len; i++)
				outTraces[i] = new TracePointObject(__thread.machine,
					inTraces[i]); 
			
			return __thread.asVMObjectArray(__thread.loadClass(
				"[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;"),
				outTraces);
		}
	},
	
	/** {@link TaskShelf#read(TaskBracket, int, byte[], int, int)}. */
	READ("read:(Lcc/squirreljme/jvm/mle/brackets/TaskBracket;I[BII)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/08
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			if (!(__args[2] instanceof SpringArrayObjectByte))
				throw new SpringMLECallError("Not a byte array.");
			
			SpringMachine machine = MLEObjects.task(__args[0]).getMachine();
			int fd = (int)__args[1];
			SpringArrayObjectByte buf = (SpringArrayObjectByte)__args[2];
			int off = (int)__args[3];
			int len = (int)__args[4];
			
			try
			{
				return machine.terminalPipes
					.mleRead(fd, buf.array(), off, len);
			}
			catch (MLECallWouldFail e)
			{
				throw new SpringMLECallError(e.getMessage(), e);
			}
		}
	},
	
	/**
	 * {@link TaskShelf#start(JarPackageBracket[], String, String[],
	 * String[], int, int)}.
	 */
	START("start:([Lcc/squirreljme/jvm/mle/brackets/" +
		"JarPackageBracket;Ljava/lang/String;[Ljava/lang/String;" +
		"[Ljava/lang/String;II)Lcc/squirreljme/jvm/mle/brackets/TaskBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/09
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Some initial 
			if (!(__args[0] instanceof SpringArrayObjectGeneric))
				throw new SpringMLECallError("Classpath not array.");
			
			if (!(__args[2] instanceof SpringArrayObjectGeneric))
				throw new SpringMLECallError("Main Args not array.");
			
			if (!(__args[3] instanceof SpringArrayObjectGeneric))
				throw new SpringMLECallError("SysProps not array.");
			
			// Get all the parameters
			SpringArrayObjectGeneric rawClasspath =
				(SpringArrayObjectGeneric)__args[0];
			String mainClass =
				__thread.<String>asNativeObject(String.class, __args[1]);
			SpringArrayObjectGeneric rawMainArgs =
				(SpringArrayObjectGeneric)__args[2];
			SpringArrayObjectGeneric rawSysProps =
				(SpringArrayObjectGeneric)__args[3];
			int stdOutMode = (int)__args[4];
			int stdErrMode = (int)__args[5];
			
			// Perform more checks
			if (mainClass == null)
				throw new SpringMLECallError("No main class.");
			
			if (rawClasspath.length() < 1)
				throw new SpringMLECallError("Classpath is empty.");
			
			if ((rawSysProps.length() % 2) != 0)
				throw new SpringMLECallError("SysProps not in pairs.");
			
			if (stdOutMode < 0 || stdErrMode < 0 ||
				stdOutMode >= TaskPipeRedirectType.NUM_REDIRECT_TYPES ||
				stdErrMode >= TaskPipeRedirectType.NUM_REDIRECT_TYPES)
				throw new SpringMLECallError("Invalid pipe mode.");
			
			// Extract the classpath
			int numClasspath = rawClasspath.length();
			VMClassLibrary[] classpath = new VMClassLibrary[numClasspath];
			for (int i = 0; i < numClasspath; i++)
				classpath[i] = MLEObjects.jarPackage(
					rawClasspath.get(SpringObject.class, i)).library();
			
			// The first entry needs to be the same, so CLDC cannot just get
			// replaced
			if (classpath[0] != __thread.machine.classLoader().rootLibrary())
				throw new SpringMLECallError("RootLib is not the same.");
			
			// Extract the main args
			int numMainArgs = rawMainArgs.length();
			String[] mainArgs = new String[numMainArgs];
			for (int i = 0; i < numMainArgs; i++)
			{
				String s = __thread.<String>asNativeObject(String.class,
					rawMainArgs.get(SpringObject.class, i));
				
				if (s == null)
					throw new SpringMLECallError("Null mainArg: " + i);
				
				mainArgs[i] = s;
			}
			
			// Extract the system properties
			int numSysProps = rawSysProps.length();
			String[] sysProps = new String[numSysProps];
			for (int i = 0; i < numSysProps; i++)
			{
				String s = __thread.<String>asNativeObject(String.class,
					rawSysProps.get(SpringObject.class, i));
				
				if (s == null)
					throw new SpringMLECallError("Null sysProp: " + i);
				
				sysProps[i] = s;
			}
			
			// Map key/value pairs into an actual map
			Map<String, String> sysPropsMap = new LinkedHashMap<>();
			for (int i = 0; i < numSysProps; i += 2)
				sysPropsMap.put(sysProps[i], sysProps[i + 1]);
			
			// Spawn the task
			SpringMachine newMachine =
				__thread.machine.taskManager().startTask(classpath, mainClass,
					mainArgs, sysPropsMap, stdOutMode, stdErrMode,
					true, false);
			return newMachine.taskObject(__thread.machine);
		}
	},
	
	/** {@link TaskShelf#status(TaskBracket)}. */
	STATUS("status:(Lcc/squirreljme/jvm/mle/brackets/TaskBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/08
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringMachine machine = MLEObjects.task(__args[0]).getMachine();
			
			if (machine.isExiting())
				return TaskStatusType.EXITED;
			return TaskStatusType.ALIVE;
		}
	}, 
	
	/* End. */
	;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/08
	 */
	MLETask(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/08
	 */
	@Override
	public String key()
	{
		return this.key;
	}
	
}
