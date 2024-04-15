// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle.errors;

import cc.squirreljme.jvm.mle.TypeShelf;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Tests that invalid inputs to {@link TypeShelf} fail.
 *
 * @since 2020/06/22
 */
public class TestTypeShelfInvalid
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
		switch (__index)
		{
			case 0:
				TypeShelf.binaryName(null);
				break;
			
			case 1:
				TypeShelf.binaryPackageName(null);
				break;
			
			case 2:
				TypeShelf.typeToClass(null);
				break;
			
			case 3:
				TypeShelf.isArray(null);
				break;
			
			case 4:
				TypeShelf.isPrimitive(null);
				break;
			
			case 5:
				TypeShelf.inJar(null);
				break;
			
			case 6:
				TypeShelf.classToType(null);
				break;
			
			case 7:
				TypeShelf.component(null);
				break;
			
			case 8:
				TypeShelf.component(TypeShelf.typeOfByte());
				break;
			
			case 9:
				TypeShelf.componentRoot(TypeShelf.typeOfByte());
				break;
			
			case 10:
				TypeShelf.findType(null);
				break;
			
			case 11:
				TypeShelf.interfaces(null);
				break;
			
			case 12:
				TypeShelf.isInterface(null);
				break;
			
			case 13:
				TypeShelf.objectType(null);
				break;
			
			case 14:
				TypeShelf.runtimeName(null);
				break;
			
			case 15:
				TypeShelf.superClass(null);
				break;
			
			case 16:
				TypeShelf.isEnum(null);
				break;
			
			case 17:
				TypeShelf.enumValues(null);
				break;
			
			case 18:
				TypeShelf.equals(null, null);
				break;
			
			case 19:
				TypeShelf.equals(TypeShelf.typeOfByte(), null);
				break;
			
			case 20:
				TypeShelf.equals(null, TypeShelf.typeOfByte());
				break;
			
			default:
				return true;
		}
		
		return false;
	}
}
