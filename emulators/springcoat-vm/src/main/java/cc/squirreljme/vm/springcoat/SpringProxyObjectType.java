// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Base interface for any type of proxy.
 *
 * @since 2024/08/02
 */
public interface SpringProxyObjectType
	extends SpringObject
{
	/**
	 * Invokes the given proxy method.
	 * 
	 * @param __thread The thread is invoke on.
	 * @param __method The method to invoke.
	 * @param __args The arguments of the method.
	 * @return The resultant value, if any.
	 * @since 2021/02/25
	 */
	Object invokeProxy(SpringThreadWorker __thread,
		MethodNameAndType __method, Object[] __args);
}
