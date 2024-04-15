// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.nio.file.Path;

/**
 * Parameters for building ROMs.
 * 
 * This class is mutable.
 *
 * @since 2021/08/22
 */
public final class RomBuildParameters
{
	/** Main class for the launcher. */
	public String launcherMainClass;
	
	/** Launch boot arguments. */
	public String[] launcherArgs;
	
	/** Path that must be loaded to start the launcher. */
	public Path[] launcherClassPath;
	
	/** The main class for the boot loader. */
	public String bootLoaderMainClass;
	
	/** The class path for the boot loader. */
	public Path[] bootLoaderClassPath;
}
