// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.util.SerializedPath;
import java.io.Serializable;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

/**
 * Parameters for a suite run, everything within must be serializable.
 *
 * @since 2022/09/11
 */
@Data
@Builder(toBuilder = true)
public final class SuiteRunParameters
	implements Serializable
{
	/** System properties to use. */
	Map<String, String> sysProps;
	
	/** Do not use parallel tests? */
	boolean noParallelTests;
	
	/** The emulator binary path. */
	SerializedPath emuLib;
	
	/** The class path. */
	SerializedPath[] classPath;
	
	/** Unique ID for the test run. */
	String uniqueId;
}
