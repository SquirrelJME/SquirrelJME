// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.test;

import cc.squirreljme.jvm.aot.Backend;
import cc.squirreljme.jvm.aot.CompileSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Parameters for the test situation.
 *
 * @since 2022/09/05
 */
public final class SituationParameters
{
	/** The backend used. */
	public final Backend backend;
	
	/** The link glob to use. */
	public final LinkGlob linkGlob;
	
	/** The settings used for compilation. */
	public final CompileSettings compileSettings;
	
	/** The output stream where the link glob writes to. */
	public final ByteArrayOutputStream globOutput;
	
	/**
	 * Initializes the test situation parameters.
	 * 
	 * @param __backend The backend used.
	 * @param __linkGlob The link glob.
	 * @param __compileSettings The compilation settings.
	 * @param __globOutput The output where the data goes.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/05
	 */
	public SituationParameters(Backend __backend, LinkGlob __linkGlob,
		CompileSettings __compileSettings, ByteArrayOutputStream __globOutput)
		throws NullPointerException
	{
		if (__backend == null || __linkGlob == null ||
			__compileSettings == null || __globOutput == null)
			throw new NullPointerException("NARG");
		
		this.backend = __backend;
		this.linkGlob = __linkGlob;
		this.compileSettings = __compileSettings;
		this.globOutput = __globOutput;
	}
}
