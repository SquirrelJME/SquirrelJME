// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import java.net.URI;

/**
 * Provider for the creation of tasks.
 *
 * @since 2024/07/28
 */
@FunctionalInterface
public interface MakeRunTaskProvider
{
	/**
	 * Makes the given task.
	 *
	 * @param __name The task name.
	 * @param __classifier The classifier user.
	 * @param __mainClass The main class.
	 * @param __midlet The MIDlet to use.
	 * @param __debugServer The GDB debug server.
	 * @since 2024/07/28
	 */
	void makeTask(String __name, SourceTargetClassifier __classifier,
		String __mainClass, JavaMEMidlet __midlet, URI __debugServer);
}
