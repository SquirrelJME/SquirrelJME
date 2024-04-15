// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Base test for arrays in {@link ObjectShelf} due to the large amounts of
 * duplicated logic!
 *
 * @param <A> The array type.
 * @since 2020/06/22
 */
abstract class __BaseArrayInvalidTest__<A>
	extends __BaseMleErrorTest__
{
	/**
	 * Allocates the given array.
	 * 
	 * @param __len The length of the array.
	 * @return The created array.
	 * @since 2020/06/22
	 */
	protected abstract A arrayNew(int __len);
	
	/**
	 * Performs the array operation.
	 * 
	 * @param __src The source.
	 * @param __srcOff The source offset.
	 * @param __dest The destination.
	 * @param __destOff The destination offset.
	 * @param __len The length.
	 * @since 2020/06/22
	 */
	protected abstract void arrayOperation(A __src, int __srcOff,
		A __dest, int __destOff, int __len);
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	public boolean test(int __index)
		throws MLECallError
	{
		A src = this.arrayNew(4);
		A dest = this.arrayNew(8);
		
		switch (__index)
		{
			case 0:
				this.arrayOperation(null, 0, 
					null, 0, 0);
				break;
			
			case 1:
				this.arrayOperation(src, 0, 
					null, 0, 0);
				break;
			
			case 2:
				this.arrayOperation(null, 0, 
					dest, 0, 0);
				break;
			
			case 3:
				this.arrayOperation(src, 0, 
					dest, 0, -1);
				break;
			
			case 4:
				this.arrayOperation(src, 0, 
					dest, 0, 12);
				break;
			
			case 5:
				this.arrayOperation(src, 0, 
					dest, 6, 4);
				break;
			
			case 6:
				this.arrayOperation(src, 2, 
					dest, 0, 6);
				break;
			
			case 7:
				this.arrayOperation(src, -1, 
					dest, 0, 4);
				break;
			
			case 8:
				this.arrayOperation(src, 0, 
					dest, -1, 4);
				break;
			
			default:
				return true;
		}
		
		return false;
	}
}
