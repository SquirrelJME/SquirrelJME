// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * Viewer for methods.
 *
 * @since 2024/01/21
 */
public interface MethodViewer
{
	/**
	 * Which class is this in?
	 *
	 * @return The class this is in.
	 * @since 2024/01/21
	 */
	ClassName inClass();
	
	/**
	 * Returns all the instructions for this method.
	 *
	 * @return The instructions for this method, may return {@code null} if
	 * not yet known.
	 * @since 2024/01/21
	 */
	InstructionViewer[] instructions();
	
	/**
	 * Is this an abstract method?
	 *
	 * @return If the method is abstract.
	 * @since 2024/01/21
	 */
	boolean isAbstract();
	
	/**
	 * Is this a native method?
	 *
	 * @return If the method is native.
	 * @since 2024/01/21
	 */
	boolean isNative();
	
	/**
	 * Returns the method name and type this represents.
	 *
	 * @return The method's name and type.
	 * @since 2024/01/21
	 */
	MethodNameAndType methodNameAndType();
}
