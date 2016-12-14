// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

/**
 * This class represents the base for projects. A project may be an API, a
 * MIDlet, or a LIBlet. However, most projects from the these distinctions are
 * very much the same. A project points to optional source code and binary
 * projects which may or may not exist.
 *
 * @since 2016/12/14
 */
public abstract class Project
{
	/**
	 * This represents a binary compilation of source code.
	 *
	 * Note that the compiled binary might not be derived from the source code.
	 *
	 * @since 2016/12/14
	 */
	public abstract class Binary
	{
	}
	
	/**
	 * This represents source code within a project which may be used to
	 * construct a binary from.
	 *
	 * @since 2016/12/14
	 */
	public abstract class Source
	{
	}
}

