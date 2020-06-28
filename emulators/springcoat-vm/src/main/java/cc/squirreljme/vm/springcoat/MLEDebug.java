// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

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
			return MLEDebug.__trace(__args[0]).getTrace().address();
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
			return __thread.asVMObject(MLEDebug.__trace(__args[0])
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
			return __thread.asVMObject(MLEDebug.__trace(__args[0])
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
			return MLEDebug.__trace(__args[0]).getTrace().byteCodeAddress();
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
			return MLEDebug.__trace(__args[0]).getTrace()
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
			return MLEDebug.__trace(__args[0]).getTrace().line();
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
			return __thread.asVMObject(MLEDebug.__trace(__args[0])
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
			return __thread.asVMObject(MLEDebug.__trace(__args[0])
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
			CallTraceElement[] trace = __thread.thread.getStackTrace();
			
			int n = trace.length;
			TracePointObject[] rv = new TracePointObject[n];
			
			for (int i = 0; i < n; i++)
				rv[i] = new TracePointObject(trace[i]);
			
			return __thread.asVMObjectArray(__thread.loadClass(
				"[Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;"),
				rv);
		}
	}
	
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
	
	/**
	 * Checks if this is a trace point object.
	 * 
	 * @param __object The object to check.
	 * @return As a trace point if this is one.
	 * @throws SpringMLECallError If this is not a trace point object.
	 * @since 2020/06/22
	 */
	static TracePointObject __trace(Object __object)
		throws SpringMLECallError
	{
		if (!(__object instanceof TracePointObject))
			throw new SpringMLECallError("Not a trace point.");
		
		return (TracePointObject)__object; 
	}
}
