// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.driver.nio.java.shelf.JavaPathBracket;
import cc.squirreljme.jvm.mle.brackets.PencilBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;
import java.nio.file.Path;
import javax.microedition.lcdui.Graphics;

/**
 * Object representation for {@link JavaPathBracket}.
 *
 * @since 2023/08/20
 */
public class JavaPathObject
	extends AbstractGhostObject
{
	/** The path this represents. */
	public final Path path;
	
	/**
	 * Initializes the path wrapper.
	 *
	 * @param __machine The machine used.
	 * @param __path The path to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/20
	 */
	public JavaPathObject(SpringMachine __machine, Path __path)
		throws NullPointerException
	{
		super(__machine, JavaPathBracket.class);
		
		if (__path == null)
			throw new NullPointerException("NARG");
		
		this.path = __path;
	}
}
