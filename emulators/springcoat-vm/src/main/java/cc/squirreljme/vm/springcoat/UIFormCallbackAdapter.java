// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

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
	implements UIFormCallback
{
	/** The callback class. */
	private static final ClassName CALLBACK_CLASS =
		new ClassName("cc/squirreljme/jvm/mle/callbacks/UIFormCallback");
	
	/** The object to call into. */
	private final SpringObject callback;
	
	/** The machine to call for when callbacks occur. */
	private final SpringMachine machine;
	
	/**
	 * Initializes the callback adapter.
	 * 
	 * @param __machine The machine executing under.
	 * @param __cb The SpringCoat object to call.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/13
	 */
	public UIFormCallbackAdapter(SpringMachine __machine, SpringObject __cb)
		throws NullPointerException
	{
		if (__machine == null || __cb == null)
			throw new NullPointerException("NARG");
		
		this.machine = __machine;
		this.callback = __cb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void eventKey(UIFormBracket __form, UIItemBracket __item,
		int __event, int __keyCode, int __modifiers)
	{
		UIFormCallbackAdapter.__callbackInvoke(this.machine, this.callback,
			MethodNameAndType.ofArguments("eventKey", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;",
				"Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;",
				"I", "I", "I"),
			new UIFormObject(__form), new UIItemObject(__item),
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
		UIFormCallbackAdapter.__callbackInvoke(this.machine, this.callback,
			MethodNameAndType.ofArguments("eventMouse", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;",
				"Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;",
				"I", "I", "I", "I", "I"),
			new UIFormObject(__form), new UIItemObject(__item),
			__event, __button, __x, __y, __modifiers);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void exitRequest(UIFormBracket __form)
	{
		UIFormCallbackAdapter.__callbackInvoke(this.machine, this.callback,
			MethodNameAndType.ofArguments("exitRequest", null,
			"Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;"),
			new UIFormObject(__form));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void paint(UIFormBracket __form, UIItemBracket __item, int __pf,
		int __bw, int __bh, Object __buf, int __offset, int[] __pal, int __sx,
		int __sy, int __sw, int __sh)
	{
		UIFormCallbackAdapter.__callbackInvoke(this.machine, this.callback,
			MethodNameAndType.ofArguments("paint", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;",
				"Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;",
				"I", "I", "I", "Ljava/lang/Object;", "I", "[I",
				"I", "I", "I", "I"),
			new UIFormObject(__form), new UIItemObject(__item),
			__pf, __bw, __bh, __buf, __offset, __pal, __sx, __sy, __sw, __sh);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __old, int __new)
	{
		UIFormCallbackAdapter.__callbackInvoke(this.machine, this.callback,
			MethodNameAndType.ofArguments("propertyChange", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;",
				"Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;",
				"I", "I", "I"),
			new UIFormObject(__form), new UIItemObject(__item),
			__intProp, __old, __new);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/13
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, String __old, String __new)
	{
		UIFormCallbackAdapter.__callbackInvoke(this.machine, this.callback,
			MethodNameAndType.ofArguments("propertyChange", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIFormBracket;",
				"Lcc/squirreljme/jvm/mle/brackets/UIItemBracket;",
				"I", "Ljava/lang/String;", "Ljava/lang/String;"),
			new UIFormObject(__form), new UIItemObject(__item),
			__strProp, __old, __new);
	}
	
	/**
	 * Invokes the callback.
	 * 
	 * @param __machine The target machine.
	 * @param __target The target to call.
	 * @param __nat The name and type.
	 * @param __args The arguments to the call.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/15
	 */
	static void __callbackInvoke(SpringMachine __machine,
		SpringObject __target, MethodNameAndType __nat, Object... __args)
		throws NullPointerException
	{
		if (__nat == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Inject our object into the call
		int argLen = __args.length;
		Object[] callArgs = new Object[argLen + 1];
		System.arraycopy(__args, 0, callArgs, 1, argLen);
		callArgs[0] = __target;
		
		// Setup callback thread for handling
		try (CallbackThread cb = __machine.obtainCallbackThread())
		{
			// Invoke the given method
			Object fail = cb.thread().invokeMethod(false,
				UIFormCallbackAdapter.CALLBACK_CLASS, __nat, callArgs);
			
			// Request failed, do not fail but eat the exception
			if (fail != null)
				Debugging.debugNote("Callback exception: %s", fail);
		}
	}
}
