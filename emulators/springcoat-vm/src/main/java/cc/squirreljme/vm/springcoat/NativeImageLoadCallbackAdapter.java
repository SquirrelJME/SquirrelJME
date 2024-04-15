// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.callbacks.NativeImageLoadCallback;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Callback for the loading of native images.
 *
 * @since 2022/06/28
 */
public class NativeImageLoadCallbackAdapter
	extends SpringCallbackAdapter
	implements NativeImageLoadCallback
{
	/** The class used to call back. */
	private static final ClassName CALLBACK_CLASS =
		new ClassName(
			"cc/squirreljme/jvm/mle/callbacks/NativeImageLoadCallback");
	
	/**
	 * Initializes the image loading callback.
	 * 
	 * @param __machine The machine used.
	 * @param __target The target object.
	 * @since 2022/06/28
	 */
	public NativeImageLoadCallbackAdapter(SpringMachine __machine,
		SpringObject __target)
	{
		super(NativeImageLoadCallbackAdapter.CALLBACK_CLASS, __machine,
			__target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/28
	 */
	@Override
	public void addImage(int[] __buf, int __off, int __len, int __frameDelay,
		boolean __hasAlpha)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("addImage", null,
				"[I", "I", "I", "I", "Z"),
			__buf, __off, __len, __frameDelay, __hasAlpha);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	public void cancel()
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("cancel", null));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/28
	 */
	@Override
	public Object finish()
	{
		return this.invokeCallback(
			MethodNameAndType.ofArguments("finish",
				"Ljava/lang/Object;"));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/28
	 */
	@Override
	public void initialize(int __width, int __height, boolean __animated,
		boolean __scalable)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("initialize", null,
				"I", "I", "Z", "Z"),
			__width, __height, __animated, __scalable);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/28
	 */
	@Override
	public void setLoopCount(int __loopCount)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("setLoopCount", null,
				"I"),
			__loopCount);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @return
	 * @since 2024/01/14
	 */
	@Override
	public boolean setPalette(int[] __colors, int __off, int __len,
		boolean __hasAlpha, int __transDx)
	{
		return ((Integer)this.invokeCallback(
			MethodNameAndType.ofArguments("setPalette", "Z",
				"[I", "I", "I", "Z", "I"),
			__colors, __off, __len, __hasAlpha, __transDx)) != 0;
	}
}
