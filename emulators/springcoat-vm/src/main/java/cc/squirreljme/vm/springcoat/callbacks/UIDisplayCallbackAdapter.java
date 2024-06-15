// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.callbacks;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.callbacks.UIDisplayCallback;
import cc.squirreljme.vm.springcoat.SpringMachine;
import cc.squirreljme.vm.springcoat.SpringObject;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Calls into SpringCoat and performs the callback.
 *
 * @since 2020/10/03
 */
public class UIDisplayCallbackAdapter
	extends UIDrawableCallbackAdapter
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
	public void later(UIDisplayBracket __display, int __serialId)
	{
		this.invokeCallback(
			MethodNameAndType.ofArguments("later", null,
				"Lcc/squirreljme/jvm/mle/brackets/UIDisplayBracket;",
				"I"), this.mapDrawable(__display), __serialId);
	}
}
