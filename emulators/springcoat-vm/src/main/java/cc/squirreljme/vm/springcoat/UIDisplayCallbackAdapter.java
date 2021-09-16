// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.callbacks.UIDisplayCallback;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Calls into SpringCoat and performs the callback.
 *
 * @since 2020/10/03
 */
public class UIDisplayCallbackAdapter
	implements UIDisplayCallback
{
	/** The object to call into. */
	private final SpringObject callback;
	
	/** The machine to call for when callbacks occur. */
	private final SpringMachine machine;
	
	/**
	 * Initializes the callback.
	 * 
	 * @param __machine The machine to refer to.
	 * @param __cb The callback to forward to.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/03
	 */
	public UIDisplayCallbackAdapter(SpringMachine __machine,
		SpringObject __cb)
		throws NullPointerException
	{
		if (__machine == null || __cb == null)
			throw new NullPointerException("NARG");
		
		this.machine = __machine;
		this.callback = __cb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void later(int __displayId, int __serialId)
	{
		UIFormCallbackAdapter.__callbackInvoke(this.machine, this.callback,
			MethodNameAndType.ofArguments("later", null,
				"I", "I"),
			__displayId, __serialId);
	}
}
