// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.UIFormObject;
import cc.squirreljme.vm.springcoat.brackets.UIItemObject;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This adapter is responsible for when it is called, to call into SpringCoat
 * whenever any events happen.
 *
 * @since 2020/09/13
 */
public class UIFormCallbackAdapter
	extends SpringCallbackAdapter
	implements UIFormCallback
{
	/** The callback class. */
	private static final ClassName CALLBACK_CLASS =
		new ClassName("cc/squirreljme/jvm/mle/callbacks/UIFormCallback");
	
	/**
	 * Initializes the callback adapter.
	 * 
	 * @param __machine The machine executing under.
	 * @param __callback The SpringCoat object to call.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/13
	 */
	public UIFormCallbackAdapter(SpringMachine __machine,
		SpringObject __callback)
		throws NullPointerException
	{
		super(UIFormCallbackAdapter.CALLBACK_CLASS, __machine, __callback);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void eventKey(UIFormBracket __form, UIItemBracket __item,
		int __event, int __keyCode, int __modifiers)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("eventKey", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;",
				"Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;",
				"I", "I", "I"),
			new UIFormObject(this.machine, __form), new UIItemObject(machine,
				__item),
			__event, __keyCode, __modifiers);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void eventMouse(UIFormBracket __form, UIItemBracket __item,
		int __event, int __button, int __x, int __y, int __modifiers)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("eventMouse", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;",
				"Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;",
				"I", "I", "I", "I", "I"),
			new UIFormObject(this.machine, __form), new UIItemObject(
				this.machine, __item),
			__event, __button, __x, __y, __modifiers);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void exitRequest(UIFormBracket __form)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("exitRequest", null,
			"Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;"),
			new UIFormObject(this.machine, __form));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/20
	 */
	@Override
	public void formRefresh(UIFormBracket __form, int __sx, int __sy,
		int __sw, int __sh)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("formRefresh", null,
			"Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;",
				"I", "I", "I", "I"),
			new UIFormObject(this.machine, __form), __sx, __sy, __sw, __sh);
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
		Object fakeDrawable;
		if (__drawable instanceof UIFormBracket)
			fakeDrawable = new UIFormObject(this.machine,
				(UIFormBracket)__drawable);
		else if (__drawable instanceof UIItemBracket)
			fakeDrawable = new UIItemObject(this.machine,
				(UIItemBracket)__drawable);
		else
			throw Debugging.todo(__drawable.getClass().toString());
		
		this.invokeCallback(
			MethodNameAndType.ofArguments("paint", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIDrawableBracket;",
				"I", "I", "I", "Ljava/lang/Object;", "I", "[I",
				"I", "I", "I", "I", "I"),
			fakeDrawable,
			__pf, __bw, __bh, __buf, __offset, __pal, __sx, __sy, __sw, __sh,
			__special);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __sub, int __old, int __new)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("propertyChange", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;",
				"Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;",
				"I", "I", "I", "I"),
			new UIFormObject(this.machine, __form), new UIItemObject(
				this.machine, __item),
			__intProp, __sub, __old, __new);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, int __sub, String __old, String __new)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("propertyChange", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;",
				"Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;",
				"I", "I", "Ljava/lang/String;", "Ljava/lang/String;"),
			new UIFormObject(this.machine, __form),
			new UIItemObject(this.machine, __item),
			__strProp, __sub, __old, __new);
	}
}
