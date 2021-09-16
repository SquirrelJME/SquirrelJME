// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.TerminalShelf;
import cc.squirreljme.jvm.mle.constants.StandardPipeType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Tests {@link TerminalShelf}.
 *
 * @since 2020/06/22
 */
public class TestTerminalShelfInvalid
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
		switch (__index)
		{
			case 0:
				TerminalShelf.flush(-1);
				break;
			
			case 1:
				TerminalShelf.write(-1, 0);
				break;
			
			case 2:
				TerminalShelf.write(-1, null, 0, 0);
				break;
			
			case 3:
				TerminalShelf.write(StandardPipeType.STDERR,
					null, 0, 0);
				break;
			
			case 4:
				TerminalShelf.write(StandardPipeType.STDERR,
					new byte[12], -2, 14);
				break;
			
			case 5:
				TerminalShelf.write(StandardPipeType.STDERR,
					new byte[12], 2, 12);
				break;
			
			case 6:
				TerminalShelf.write(StandardPipeType.STDERR,
					new byte[12], 0, 14);
				break;
			
			case 7:
				TerminalShelf.write(StandardPipeType.STDERR,
					new byte[12], 2, -14);
				break;
			
			case 8:
				TerminalShelf.write(-1,
					new byte[12], 0, 12);
				break;
			
			case 9:
				TerminalShelf.close(-1);
				break;
			
			default:
				return true;
		}
		
		return false;
	}
}
