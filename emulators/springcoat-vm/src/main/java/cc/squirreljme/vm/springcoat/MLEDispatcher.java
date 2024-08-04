// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.AtomicShelf;
import cc.squirreljme.jvm.mle.DebugShelf;
import cc.squirreljme.jvm.mle.JarPackageShelf;
import cc.squirreljme.jvm.mle.MathShelf;
import cc.squirreljme.jvm.mle.MidiShelf;
import cc.squirreljme.jvm.mle.NativeArchiveShelf;
import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.jvm.mle.PencilFontShelf;
import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.jvm.mle.ReferenceShelf;
import cc.squirreljme.jvm.mle.ReflectionShelf;
import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.TaskShelf;
import cc.squirreljme.jvm.mle.TerminalShelf;
import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.jvm.mle.scritchui.NativeScritchInterface;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.util.Map;
import java.util.TreeMap;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Not Described.
 *
 * @since 2020/06/18
 */
public enum MLEDispatcher
	implements MLEDispatcherKey
{
	/** {@link AtomicShelf}. */
	ATOMIC("cc/squirreljme/jvm/mle/AtomicShelf",
		MLEAtomic.values()),
	
	/** {@link DebugShelf}. */
	DEBUG("cc/squirreljme/jvm/mle/DebugShelf",
		MLEDebug.values()),
	
	/** {@link JarPackageShelf}. */
	JAR_PACKAGE("cc/squirreljme/jvm/mle/JarPackageShelf",
		MLEJarPackage.values()),
	
	/** {@link MathShelf}. */
	MATH("cc/squirreljme/jvm/mle/MathShelf",
		MLEMath.values()),
	
	/** {@link MidiShelf}. */
	MIDI("cc/squirreljme/jvm/mle/MidiShelf",
		MLEMidi.values()),
	
	/** {@link NativeArchiveShelf}. */
	NATIVE_ARCHIVE("cc/squirreljme/jvm/mle/NativeArchiveShelf",
		MLENativeArchive.values()),
	
	/** {@link ObjectShelf}. */
	OBJECT("cc/squirreljme/jvm/mle/ObjectShelf",
		MLEObject.values()),
	
	/** {@link PencilShelf}. */
	PENCIL("cc/squirreljme/jvm/mle/PencilShelf",
		MLEPencil.values()),
	
	/** {@link PencilFontShelf}. */
	PENCIL_FONT("cc/squirreljme/jvm/mle/PencilFontShelf",
		MLEPencilFontShelf.values()),
	
	/** {@link ReferenceShelf}. */
	REFERENCE("cc/squirreljme/jvm/mle/ReferenceShelf",
		MLEReference.values()),
	
	/** {@link ReflectionShelf}. */
	REFLECTION("cc/squirreljme/jvm/mle/ReflectionShelf",
		MLEReflection.values()),
	
	/** {@link RuntimeShelf}. */
	RUNTIME("cc/squirreljme/jvm/mle/RuntimeShelf",
		MLERuntime.values()),
	
	/** {@link NativeScritchInterface}. */
	SCRITCH_UI("cc/squirreljme/jvm/mle/scritchui/NativeScritchInterface",
		MLEScritchUI.values()),
	
	/** {@link TaskShelf}. */
	TASK("cc/squirreljme/jvm/mle/TaskShelf",
		MLETask.values()),
	
	/** {@link TerminalShelf}. */
	TERMINAL("cc/squirreljme/jvm/mle/TerminalShelf",
		MLETerminal.values()),
	
	/** {@link ThreadShelf}. */
	THREAD("cc/squirreljme/jvm/mle/ThreadShelf",
		MLEThread.values()),
	
	/** {@link TypeShelf}. */
	TYPE("cc/squirreljme/jvm/mle/TypeShelf",
		MLEType.values()),
	
	/* End. */
	;
	
	/** The function tree. */
	private static Map<String, Map<String, MLEDispatcherTarget>> _fnTree;
	
	/** The dispatcher key. */
	protected final String key;
	
	/** The functions. */
	private final MLEFunction[] _functions;
	
	static
	{
		Map<String, Map<String, MLEDispatcherTarget>> functionTree =
			new TreeMap<>();
		
		// Build the function tree
		for (MLEDispatcher dispatch : MLEDispatcher.values())
		{
			Map<String, MLEDispatcherTarget> subTree = new TreeMap<>();
			
			for (MLEFunction function : dispatch._functions)
				subTree.put(function.key(), function);
			
			functionTree.put(dispatch.key(), subTree);
		}
		
		MLEDispatcher._fnTree = functionTree;
	}
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @param __functions Functions for the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/18
	 */
	MLEDispatcher(String __key, MLEFunction[] __functions)
		throws NullPointerException
	{
		if (__key == null || __functions == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
		this._functions = __functions;
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
	 * Handles the dispatching of the native method.
	 *
	 * @param __thread The current thread this is acting under.
	 * @param __class The native class being called.
	 * @param __func The method being called.
	 * @param __args The arguments to the call.
	 * @return The resulting object returned by the dispatcher.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/05/30
	 */
	public static Object dispatch(SpringThreadWorker __thread,
		ClassName __class, MethodNameAndType __func, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __class == null)
			throw new NullPointerException("NARG");
		
		// Find the sub-tree
		Map<String, MLEDispatcherTarget> subTree = MLEDispatcher._fnTree.get(
			__class.toString());
		if (subTree == null)
			throw new SpringVirtualMachineException(String.format(
				"Unknown MLE Shelf: %s", __class));
		
		// Find the target function
		MLEDispatcherTarget target = subTree.get(__func.toString());
		if (target == null)
			throw new SpringVirtualMachineException(String.format(
				"Unknown MLE Shelf Function: %s::%s", __class, __func));
		
		// Call it
		try
		{
			return target.handle(__thread, __args);
		}
		catch (MLECallError e)
		{
			throw new SpringMLECallError(
				String.format("Unwrapped MLECallError calling %s:%s",
					__class, __func), e);
		}
	}
}
