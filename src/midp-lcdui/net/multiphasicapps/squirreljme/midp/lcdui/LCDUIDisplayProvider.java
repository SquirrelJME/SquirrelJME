// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midp.lcdui;

import javax.microedition.lcdui.Display;
import net.multiphasicapps.squirreljme.lcduilui.CommonDisplayProvider;

/**
 * This interface is used for lookup to LCDUI providers.
 *
 * @since 2016/10/08
 */
public interface LCDUIDisplayProvider
	extends CommonDisplayProvider<Display, LCDUIDisplay>
{
}

