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
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;
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
	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Override
	public boolean test(int __index)
		throws MLECallError
	{
		// Check to see if forms are actually supported, if not then we cannot
		// check if it is invalid
		if (0 == UIFormShelf.metric(UIMetricType.UIFORMS_SUPPORTED))
			throw new UntestableException("UIForms Not Supported!");
		
		UIItemBracket fake;
		
		switch (__index)
		{
			case 0:
				UIFormShelf.metric(-1);
				break;
			
			case 1:
				UIFormShelf.metric(UIMetricType.NUM_METRICS);
				break;
			
			case 2:
				UIFormShelf.equals((UIDisplayBracket)null, null);
				break;
			
			case 3:
				UIFormShelf.equals((UIFormBracket)null, null);
				break;
			
			case 4:
				UIFormShelf.equals((UIItemBracket)null, null);
				break;
			
			case 5:
				UIFormShelf.displayCurrent(null);
				break;
			
			case 6:
				UIFormShelf.displayShow(null, null);
				break;
			
			case 7:
				UIFormShelf.displayShow(UIFormShelf.displays()[0],
					null);
				break;
			
			case 8:
				UIFormShelf.formDelete(null);
				break;
			
			case 9:
				UIFormShelf.callback(null, null);
				break;
			
			case 10:
				UIFormShelf.itemNew(-1);
				break;
			
			case 11:
				UIFormShelf.itemNew(UIItemType.NUM_TYPES);
				break;
			
			case 12:
				UIFormShelf.itemDelete(null);
				break;
			
			case 13:
				fake = UIFormShelf.itemNew(UIItemType.BUTTON);
				UIFormShelf.itemDelete(fake);
				UIFormShelf.itemDelete(fake);
				break;
			
			case 14:
				UIFormShelf.formItemCount(null);
				break;
			
			case 15:
				UIFormShelf.formItemRemove(null, 0);
				break;
			
			case 16:
				UIFormShelf.formItemRemove(UIFormShelf.formNew(), 0);
				break;
			
			case 17:
				UIFormShelf.formItemRemove(UIFormShelf.formNew(),
					UIItemPosition.MIN_VALUE - 1);
				break;
			
			case 18:
				UIFormShelf.formItemAtPosition(UIFormShelf.formNew(),
					UIItemPosition.MIN_VALUE - 1);
				break;
			
			case 19:
				UIFormShelf.formItemAtPosition(UIFormShelf.formNew(),
					0);
				break;
			
			case 20:
				UIFormShelf.formItemPosition(UIFormShelf.formNew(),
					null, -1);
				break;
			
			case 21:
				UIFormShelf.formItemPosition(UIFormShelf.formNew(),
					UIFormShelf.itemNew(UIItemType.BUTTON), 0);
				break;
			
			case 22:
				UIFormShelf.formItemPosition(UIFormShelf.formNew(),
					UIFormShelf.itemNew(UIItemType.BUTTON),
					UIItemPosition.MIN_VALUE - 1);
				break;
			
			case 23:
				UIFormShelf.formItemRemove(UIFormShelf.formNew(),
					UIItemPosition.BODY);
				break;
			
			default:
				return true;
		}
		
		return false;
	}
}
