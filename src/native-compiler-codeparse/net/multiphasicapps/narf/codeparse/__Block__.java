// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.codeparse;

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.narf.program.NRJumpTarget;
import net.multiphasicapps.narf.program.NROp;

/**
 * This contains operations that are to be placed within a basic block.
 *
 * @since 2016/05/09
 */
class __Block__
{
	/** The jump target that may be bound to the basic block. */
	protected final NRJumpTarget jumptarget =
		new NRJumpTarget();
	
	/** The output list of operations. */
	protected final List<NROp> ops =
		new ArrayList<>();
	
	/**
	 * Initializes the basic block.
	 *
	 * @since 2016/05/09
	 */
	__Block__()
	{
	}
}

