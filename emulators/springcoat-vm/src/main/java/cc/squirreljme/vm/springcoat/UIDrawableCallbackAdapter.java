// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIDrawableCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.UIDisplayObject;
import cc.squirreljme.vm.springcoat.brackets.UIFormObject;
import cc.squirreljme.vm.springcoat.brackets.UIItemObject;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Adapter for drawables.
 *
 * @since 2023/01/13
 */
public class UIDrawableCallbackAdapter
	extends SpringCallbackAdapter
	implements UIDrawableCallback
{
	/**
	 * Initializes the callback adapter.
	 * 
	 * @param __callbackClass The callback class.
	 * @param __machine The machine executing under.
	 * @param __callback The SpringCoat object to call.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/01/13
	 */
	public UIDrawableCallbackAdapter(ClassName __callbackClass,
		SpringMachine __machine, SpringObject __callback)
		throws NullPointerException
	{
		super(__callbackClass, __machine, __callback);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void eventKey(UIDrawableBracket __drawable,
		int __event, int __keyCode, int __modifiers)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("eventKey", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIDrawableBracket;",
				"I", "I", "I"),
			this.mapDrawable(__drawable),
			__event, __keyCode, __modifiers);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void eventMouse(UIDrawableBracket __drawable,
		int __event, int __button, int __x, int __y, int __modifiers)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("eventMouse", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIDrawableBracket;",
				"I", "I", "I", "I", "I"),
			this.mapDrawable(__drawable),
			__event, __button, __x, __y, __modifiers);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void exitRequest(UIDrawableBracket __drawable)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("exitRequest", null,
			"Lcc/squirreljme/jvm/mle/brackets/UIDrawableBracket;"),
			this.mapDrawable(__drawable));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void paint(UIDrawableBracket __drawable, int __pf,
		int __bw, int __bh, Object __buf, int __offset, int[] __pal, int __sx,
		int __sy, int __sw, int __sh, int __special)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("paint", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIDrawableBracket;",
				"I", "I", "I", "Ljava/lang/Object;", "I", "[I",
				"I", "I", "I", "I", "I"),
			this.mapDrawable(__drawable),
			__pf, __bw, __bh, __buf, __offset, __pal, __sx, __sy, __sw, __sh,
			__special);
	}
	
	/**
	 * Maps the drawable.
	 * 
	 * @param __drawable The drawable to map.
	 * @return The mapped object.
	 * @since 2023/01/13
	 */
	protected final Object mapDrawable(UIDrawableBracket __drawable)
	{
		if (__drawable instanceof UIFormBracket)
			return new UIFormObject(this.machine, (UIFormBracket)__drawable);
		else if (__drawable instanceof UIItemBracket)
			return new UIItemObject(this.machine, (UIItemBracket)__drawable);
		else if (__drawable instanceof UIDisplayBracket)
			return new UIDisplayObject(this.machine,
				(UIDisplayBracket)__drawable);
		else
			throw Debugging.todo(__drawable.getClass().toString());
	}
}
