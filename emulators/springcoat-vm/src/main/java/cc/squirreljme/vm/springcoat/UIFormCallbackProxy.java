// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIDrawableBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.brackets.UIDisplayObject;
import cc.squirreljme.vm.springcoat.brackets.UIFormObject;
import cc.squirreljme.vm.springcoat.brackets.UIItemObject;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This is an injector which can inject into {@link UIFormCallback}.
 *
 * @since 2021/02/25
 */
public class UIFormCallbackProxy
	extends SpringProxyObject
{
	/** The class to act as a proxy for. */
	private static final ClassName _CLASS =
		new ClassName("cc/squirreljme/jvm/mle/callbacks/UIFormCallback");
	
	/** The callback to call into. */
	private final UIFormCallback callback;
	
	/**
	 * Initializes the form callback wrapper.
	 * 
	 * @param __machine The machine to target.
	 * @param __callback The callback to call into.
	 * @since 2021/02/25
	 */
	public UIFormCallbackProxy(SpringMachine __machine,
		UIFormCallback __callback)
		throws NullPointerException
	{
		super(UIFormCallbackProxy._CLASS, __machine);
		
		if (__callback == null)
			throw new NullPointerException("NARG");
		
		this.callback = __callback;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/02/25
	 */
	@Override
	protected Object invokeProxy(SpringThreadWorker __thread,
		MethodNameAndType __method, Object[] __args)
	{
		switch (__method.toString())
		{
			case "eventKey:(Lcc/squirreljme/jvm/mle/brackets/" +
				"UIDrawableBracket;III)V":
				this.callback.eventKey(
					UIFormCallbackProxy.__drawableBracket(__args[0]),
					(int)__args[1],
					(int)__args[2],
					(int)__args[3]);
				return null;
				
			case "eventMouse:(Lcc/squirreljme/jvm/mle/brackets/" +
				"UIDrawableBracket;IIIII)V":
				this.callback.eventMouse(
					UIFormCallbackProxy.__drawableBracket(__args[0]),
					(int)__args[1],
					(int)__args[2],
					(int)__args[3],
					(int)__args[4],
					(int)__args[5]);
				return null;
				
			case "exitRequest:(Lcc/squirreljme/jvm/mle/brackets/" +
				"UIDrawableBracket;)V":
				this.callback.exitRequest(
					UIFormCallbackProxy.__drawableBracket(__args[0]));
				return null;
				
			case "paint:(Lcc/squirreljme/jvm/mle/brackets/" +
				"UIDrawableBracket;" +
				"IIILjava/lang/Object;I[IIIIII)V":
				{
					SpringArrayObjectInteger pal =
						(SpringArrayObjectInteger)__args[6];
					this.callback.paint(
						UIFormCallbackProxy.__drawableBracket(__args[0]),
						(int)__args[1],
						(int)__args[2],
						(int)__args[3],
						((SpringArrayObject)__args[4]).array(),
						(int)__args[5],
						(pal == null ? null : pal.array()),
						(int)__args[7],
						(int)__args[8],
						(int)__args[9],
						(int)__args[10],
						(int)__args[11]);
				}
				return null;
				
			case "propertyChange:(Lcc/squirreljme/jvm/mle/brackets/" +
				"UIFormBracket;Lcc/squirreljme/jvm/mle/brackets/" +
				"UIItemBracket;IIII)V":
				this.callback.propertyChange(
					MLEUIForm.__form(__args[0]).form,
					MLEUIForm.__item(__args[1]).item,
					(int)__args[2],
					(int)__args[3],
					(int)__args[4],
					(int)__args[5]);
				return null;
				
			case "propertyChange:(Lcc/squirreljme/jvm/mle/brackets/" +
				"UIFormBracket;Lcc/squirreljme/jvm/mle/brackets/" +
				"UIItemBracket;IILjava/lang/String;Ljava/lang/String;)V":
				this.callback.propertyChange(
					MLEUIForm.__form(__args[0]).form,
					MLEUIForm.__item(__args[1]).item,
					(int)__args[2],
					(int)__args[3],
					__thread.asNativeObject(String.class, __args[4]),
					__thread.asNativeObject(String.class, __args[5]));
				return null;
				
			default:
				throw Debugging.oops(__method);
		}
	}
	
	/**
	 * Maps the drawable bracket.
	 * 
	 * @param __arg The argument to map.
	 * @return The drawable bracket.
	 * @since 2023/01/13
	 */
	private static UIDrawableBracket __drawableBracket(Object __arg)
	{
		if (__arg instanceof UIFormObject)
			return MLEUIForm.__form(__arg).form;
		else if (__arg instanceof UIItemObject)
			return MLEUIForm.__item(__arg).item;
		else if (__arg instanceof UIDisplayObject)
			return MLEUIForm.__display(__arg).display;
		else
			throw Debugging.todo(__arg.getClass().toString());
	}
}
