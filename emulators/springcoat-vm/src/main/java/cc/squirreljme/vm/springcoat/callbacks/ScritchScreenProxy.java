// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.callbacks;

import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.SpringMachine;
import cc.squirreljme.vm.springcoat.SpringProxyObject;
import cc.squirreljme.vm.springcoat.SpringThreadWorker;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Not Described.
 *
 * @since 2024/06/15
 */
public class ScritchScreenProxy
	extends ScritchBaseProxy<ScritchScreenInterface>
{
	/**
	 * Initializes the proxy.
	 *
	 * @param __machine The machine to use.
	 * @param __wrapped The wrapped instance.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/15
	 */
	public ScritchScreenProxy(SpringMachine __machine,
		ScritchScreenInterface __wrapped)
		throws NullPointerException
	{
		super(ScritchScreenInterface.class, __machine, __wrapped);
	}
	
	@Override
	protected Object invokeProxy(SpringThreadWorker __thread,
		MethodNameAndType __method, Object[] __args)
	{
		throw Debugging.todo();
	}
}
