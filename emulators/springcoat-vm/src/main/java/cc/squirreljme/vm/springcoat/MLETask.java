// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.emulator.MLECallWouldFail;
import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.brackets.TaskBracket;
import cc.squirreljme.jvm.mle.constants.TaskStatusType;
import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.vm.springcoat.brackets.TaskObject;
import cc.squirreljme.vm.springcoat.brackets.TracePointObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;

/**
 * This contains the implementation of {@link TaskShelf}.
 *
 * @since 2020/07/08
 */
public enum MLETask
	implements MLEFunction
{
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
			return new TaskObject(__thread.machine);
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
			return MLETask.__task(__args[0]).getMachine() ==
				MLETask.__task(__args[1]).getMachine();
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
			return MLETask.__task(__args[0]).getMachine().getExitCode();
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
			
			SpringMachine machine = MLETask.__task(__args[0]).getMachine();
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
				outTraces[i] = new TracePointObject(inTraces[i]); 
			
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
			
			SpringMachine machine = MLETask.__task(__args[0]).getMachine();
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
			SpringMachine machine = MLETask.__task(__args[0]).getMachine();
			
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
	
	/**
	 * Ensures that this is a {@link TaskObject}.
	 * 
	 * @param __object The object to check.
	 * @return As a {@link TaskObject}.
	 * @throws SpringMLECallError If this is not one.
	 * @since 2020/07/08
	 */
	static TaskObject __task(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof TaskObject))
			throw new SpringMLECallError("Not a TaskObject.");
		
		return (TaskObject)__object; 
	}
}
