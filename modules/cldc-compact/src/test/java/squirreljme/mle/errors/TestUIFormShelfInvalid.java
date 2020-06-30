// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.constants.UIMetricType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import net.multiphasicapps.tac.UntestableException;

/**
 * Tests whether {@link UIFormShelf} fails as it should with invalid values.
 *
 * @since 2020/06/30
 */
public class TestUIFormShelfInvalid
	extends __BaseMleErrorTest__
{
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	public boolean test(int __index)
		throws MLECallError
	{
		// Check to see if forms are actually supported, if not then we cannot
		// check if it is invalid
		if (0 == UIFormShelf.metric(UIMetricType.UIFORMS_SUPPORTED))
			throw new UntestableException("UIForms Not Supported!");
		
		switch (__index)
		{
			case 0:
				UIFormShelf.metric(-1);
				break;
			
			case 1:
				UIFormShelf.metric(UIMetricType.NUM_METRICS);
				break;
			
			default:
				return true;
		}
		
		return false;
	}
}
