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
import cc.squirreljme.jvm.mle.callbacks.UIDisplayCallback;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Calls into SpringCoat and performs the callback.
 *
 * @since 2020/10/03
 */
public class UIDisplayCallbackAdapter
	extends SpringCallbackAdapter
	implements UIDisplayCallback
{
	/** The class used to call back. */
	private static final ClassName CALLBACK_CLASS =
		new ClassName(
			"cc/squirreljme/jvm/mle/callbacks/UIDisplayCallback");
	
	/**
	 * Initializes the callback.
	 * 
	 * @param __machine The machine to refer to.
	 * @param __callback The callback to forward to.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/03
	 */
	public UIDisplayCallbackAdapter(SpringMachine __machine,
		SpringObject __callback)
		throws NullPointerException
	{
		super(UIDisplayCallbackAdapter.CALLBACK_CLASS, __machine, __callback);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void later(int __displayId, int __serialId)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("later", null,
				"I", "I"),
			__displayId, __serialId);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/01/05
	 */
	@Override
	public void paintDisplay(UIDisplayBracket __display, int __pf, int __bw,
		int __bh, Object __buf, int __offset, int[] __pal, int __sx, int __sy,
		int __sw, int __sh, int __special)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("paintDisplay", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;",
				"I", "I",
				"I", "Ljava/lang/Object;", "I", "[I", "I", "I",
				"I", "I", "I"),
			__display, __pf, __bw, __bh, __buf, __offset, __pal,
			__sx, __sy, __sw, __sh, __special);
	}
}
