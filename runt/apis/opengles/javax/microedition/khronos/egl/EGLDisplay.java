// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.egl;

import cc.squirreljme.compat.AccessChange;
import cc.squirreljme.compat.AccessType;

/**
 * This represents an OpenGL ES Display.
 *
 * For compatibility with the standard API this class should not be extended,
 * unless the class extending is internal to SquirrelJME.
 *
 * @since 2016/10/10
 */
@AccessChange(from=AccessType.PRIVATE, to=AccessType.PUBLIC,
	value="Constructor changed for package encapsulation.")
public abstract class EGLDisplay
{
}


