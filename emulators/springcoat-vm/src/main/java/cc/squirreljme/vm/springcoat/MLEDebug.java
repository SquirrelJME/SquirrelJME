// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jdwp.host.JDWPHostController;
import cc.squirreljme.jdwp.host.trips.JDWPGlobalTrip;
import cc.squirreljme.jdwp.host.trips.JDWPTripVmState;
import cc.squirreljme.jvm.mle.DebugShelf;
import cc.squirreljme.jvm.mle.brackets.TracePointBracket;
import cc.squirreljme.runtime.cldc.debug.CallTraceElement;
import cc.squirreljme.vm.springcoat.brackets.TracePointObject;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;

/**
 * Functions for {@link DebugShelf}.
 *
 * @since 2020/06/18
 */
public enum MLEDebug
	implements MLEFunction
{
	/** {@link DebugShelf#breakpoint()}. */
	BREAKPOINT("breakpoint:()V")
	{
		/**
		 * {@inheritDoc
		 * @since 2023/01/30
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// If a debugger is attached, break immediately on everything
			JDWPHostController jdwp =
				__thread.machine.taskManager().jdwpController;
			if (jdwp != null)
				jdwp.<JDWPTripVmState>trip(JDWPTripVmState.class,
					JDWPGlobalTrip.VM_STATE).userDefined(__thread.thread);
			
			return null;
		}
	},
	
	/** {@link DebugShelf#getThrowableTrace(Throwable)}. */
	GET_THROWABLE_TRACE("getThrowableTrace:(Ljava/lang/Throwable;)" +
		"[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;")
	{
		/**
		 * {@inheritDoc
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			SpringObject object = (SpringObject)__args[0];
			
			// Must be throwable type
			if (!(object instanceof SpringSimpleObject) ||
				object.type().isAssignableFrom(
				__thread.resolveClass("java/lang/Throwable")))
				throw new SpringMLECallError("Not a Throwable.");
			
			return ((SpringSimpleObject)object).fieldByNameAndType(
				false, "_stack",
				"[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;")
				.get();
		}
	},
	
	/** {@link DebugShelf#pointAddress(TracePointBracket)}. */
	POINT_ADDRESS("pointAddress:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TracePointBracket;)J")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.debugTrace(__args[0]).getTrace().address();
		}
	},
	
	/** {@link DebugShelf#pointClass(TracePointBracket)} .*/
	POINT_CLASS("pointClass:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TracePointBracket;)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.asVMObject(MLEObjects.debugTrace(__args[0])
				.getTrace().className());
		}
	},
	
	/** {@link DebugShelf#pointFile(TracePointBracket)}. */
	POINT_FILE("pointFile:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TracePointBracket;)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.asVMObject(MLEObjects.debugTrace(__args[0])
				.getTrace().file());
		}
	},
	
	/** {@link DebugShelf#pointJavaAddress(TracePointBracket)}. */
	POINT_JAVA_ADDRESS("pointJavaAddress:(Lcc/squirreljme/jvm/mle/" +
		"brackets/TracePointBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.debugTrace(__args[0]).getTrace().byteCodeAddress();
		}
	},
	
	/** {@link DebugShelf#pointJavaOperation(TracePointBracket)}. */
	POINT_JAVA_OPERATION("pointJavaOperation:(Lcc/squirreljme/jvm/mle/" +
		"brackets/TracePointBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.debugTrace(__args[0]).getTrace()
				.byteCodeInstruction();
		}
	},
	
	/** {@link DebugShelf#pointLine(TracePointBracket)}. */
	POINT_LINE("pointLine:(Lcc/squirreljme/jvm/mle/brackets/" +
		"TracePointBracket;)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return MLEObjects.debugTrace(__args[0]).getTrace().line();
		}
	},
	
	/** {@link DebugShelf#pointMethodName(TracePointBracket)}. */
	POINT_METHOD_NAME("pointMethodName:(Lcc/squirreljme/jvm/mle/" +
		"brackets/TracePointBracket;)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.asVMObject(MLEObjects.debugTrace(__args[0])
				.getTrace().methodName());
		}
	},
	
	/** {@link DebugShelf#pointMethodType(TracePointBracket)}. */
	POINT_METHOD_TYPE( "pointMethodType:(Lcc/squirreljme/jvm/mle/" +
		"brackets/TracePointBracket;)Ljava/lang/String;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.asVMObject(MLEObjects.debugTrace(__args[0])
				.getTrace().methodDescriptor());
		}
	},
	
	/** {@link DebugShelf#traceStack()}. */
	TRACE_STACK("traceStack:()" +
		"[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			if (true)
			{
				System.err.println("*******************************");
				__thread.thread.printStackTrace(System.err);
				System.err.println("*******************************");
				System.err.flush();
			}
			
			CallTraceElement[] trace = __thread.thread.getStackTrace();
			
			int n = trace.length;
			TracePointObject[] rv = new TracePointObject[n];
			
			for (int i = 0; i < n; i++)
				rv[i] = new TracePointObject(__thread.machine, trace[i]);
			
			return __thread.asVMObjectArray(__thread.loadClass(
				"[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;"),
				rv);
		}
	},
	
	/** {@link DebugShelf#verbose(int)}. */
	VERBOSE("verbose:(I)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/11
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return __thread.verbose().add(
				__thread.thread.numFrames() - 1, (int)__args[0]);
		}
	},
	
	/** {@link DebugShelf#verboseInternalThread(int)} (int)}. */
	VERBOSE_INTERNAL_THREAD("verboseInternalThread:(I)I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/11
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			__thread.machine._verboseInternal = (int)__args[0];
			
			return DebugShelf.INTERNAL_THREAD_VERBOSE_ID;
		}
	},
	
	/** {@link DebugShelf#verboseStop(int)}. */
	VERBOSE_STOP("verboseStop:(I)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/07/11
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			int id = (int)__args[0];
			
			if (id == DebugShelf.INTERNAL_THREAD_VERBOSE_ID)
				__thread.machine._verboseInternal = 0;
			else
				__thread.verbose().remove(id);
			
			return null;
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
	 * @since 2020/06/18
	 */
	MLEDebug(String __key)
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
