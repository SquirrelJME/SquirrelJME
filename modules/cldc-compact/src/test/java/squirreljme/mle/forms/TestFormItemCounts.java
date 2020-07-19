// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.forms;

import cc.squirreljme.jvm.mle.UIFormShelf;
import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;
import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.constants.UIItemType;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Tests that item counts are correct in forms.
 *
 * @since 2020/07/19
 */
public class TestFormItemCounts
	extends __BaseFormTest__
{
	/** The number of items to add. */
	public static final int COUNT =
		16;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/07/19
	 */
	@Override
	protected void uiTest(UIDisplayBracket __display, UIFormBracket __form)
	{
		// Before adding everything
		this.secondary("beforeall", UIFormShelf.formItemCount(__form));
		
		// Adding to the special items should not have any effect on size
		UIItemBracket left = UIFormShelf.itemNew(UIItemType.BUTTON);
		UIFormShelf.formItemPosition(__form, left,
			UIItemPosition.LEFT_COMMAND);
		this.secondary("leftcmd", UIFormShelf.formItemCount(__form));
		
		// Adding items should increase the item count
		UIItemBracket[] items = new UIItemBracket[TestFormItemCounts.COUNT];
		for (int i = 0; i < TestFormItemCounts.COUNT; i++)
		{
			items[i] = UIFormShelf.itemNew(UIItemType.BUTTON);
			
			UIFormShelf.formItemPosition(__form, items[i], i);
			
			this.secondary("afteradd-" + i,
				UIFormShelf.formItemCount(__form));
		}
		
		// Should be COUNT
		this.secondary("afteradds", UIFormShelf.formItemCount(__form));
		
		// Remove everything
		for (int i = TestFormItemCounts.COUNT - 1; i >= 0; i--)
		{
			this.secondary("removed-same-" + i, UIFormShelf.equals(
				UIFormShelf.formItemRemove(__form, i), items[i]));
			
			this.secondary("remove-" + i,
				UIFormShelf.formItemCount(__form));
		}
		
		// There should be nothing left
		this.secondary("afterall", UIFormShelf.formItemCount(__form));
	}
}
