// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.egl;

import net.multiphasicapps.squirreljme.compat.AccessChange;
import net.multiphasicapps.squirreljme.compat.AccessType;

/**
 * This stores an OpenGL ES configuration.
 *
 * For compatibility with the standard API this class should not be extended,
 * unless the class extending is internal to SquirrelJME.
 *
 * @since 2016/10/10
 */
@AccessChange(from=AccessType.PRIVATE, to=AccessType.PUBLIC,
	reason="Package encapsulation.")
public abstract class EGLConfig
{
}


