// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

/**
 * Ahead of time process settings.
 *
 * @since 2023/07/25
 */
public final class AOTSettings
{
	/** The compiler being used. */
	public final String compiler;
	
	/** The name. */
	public final String name;
	
	/** The mode. */
	public final String mode;
	
	/** The source set being used. */
	public final String sourceSet;
	
	/** The clutter level currently used. */
	public final String clutterLevel;
	
	/** The original library hash. */
	public final String originalLibHash;
	
	/** The Fossil commit. */
	public final String commitFossil;
	
	/** The Git commit. */
	public final String commitGit;
	
	/**
	 * Initializes the settings.
	 *
	 * @param __compiler The compiler used.
	 * @param __name The name.
	 * @param __mode The mode.
	 * @param __sourceSet The source set to use.
	 * @param __clutterLevel The current clutter level.
	 * @param __originalLibHash The original library hash.
	 * @param __commitFossil The Fossil commit.
	 * @param __commitGit The git commit.
	 * @since 2023/07/25
	 */
	public AOTSettings(String __compiler, String __name, String __mode,
		String __sourceSet, String __clutterLevel, String __originalLibHash,
		String __commitFossil, String __commitGit)
	{
		this.compiler = __compiler;
		this.name = __name;
		this.mode = __mode;
		this.sourceSet = __sourceSet;
		this.clutterLevel = __clutterLevel;
		this.originalLibHash = __originalLibHash;
		this.commitFossil = __commitFossil;
		this.commitGit = __commitGit;
	}
}
