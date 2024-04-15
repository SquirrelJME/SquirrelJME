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

/**
 * Viewer for classes.
 *
 * @since 2024/01/22
 */
public interface ClassViewer
{
	/**
	 * Returns all the methods which are available.
	 *
	 * @return The class methods.
	 * @since 2024/01/22
	 */
	MethodViewer[] methods();
	
	/**
	 * Returns the name of this class.
	 *
	 * @return The class name.
	 * @since 2024/01/22
	 */
	ClassName thisName();
}
