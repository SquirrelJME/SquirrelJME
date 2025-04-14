// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import cc.squirreljme.jdwp.host.JDWPHostFactory;
import java.util.ServiceLoader;

/**
 * Used with {@link ServiceLoader} to initialize a local debugger.
 *
 * @since 2024/01/19
 */
public interface VMDebuggerService
{
	/**
	 * Initializes a JDWP factory.
	 *
	 * @return The resultant factory.
	 * @since 2024/01/19
	 */
	JDWPHostFactory jdwpFactory();
}
