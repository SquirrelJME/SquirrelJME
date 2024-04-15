// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Base class for non-basic C types.
 *
 * @since 2023/06/06
 */
public abstract class __CAbstractType__
	implements CType
{
	/** Constant type cache. */
	private volatile Reference<CType> _constType;
	
	/** Pointer type cache. */
	private volatile Reference<CType> _pointerType;
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CType arrayType(int __size)
		throws IllegalArgumentException
	{
		return CArrayType.of(this, __size);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/25
	 */
	@Override
	public <T extends CType> T cast(Class<T> __as)
		throws NullPointerException
	{
		return __as.cast(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType constType()
		throws IllegalArgumentException
	{
		Reference<CType> ref = this._constType;
		CType rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = CModifiedType.of(CConstModifier.CONST, this);
			this._constType = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/06
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		return this.pointerType(CPointerCloseness.NEAR);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CType pointerType(CPointerCloseness __closeness)
		throws IllegalArgumentException
	{
		// Near is the most common pointer
		if (__closeness == null || __closeness == CPointerCloseness.NEAR)
		{
			Reference<CType> ref = this._pointerType;
			CType rv;
			
			if (ref == null || (rv = ref.get()) == null)
			{
				rv = CPointerType.of(this, CPointerCloseness.NEAR);
				this._pointerType = new WeakReference<>(rv);
			}
			
			return rv;
		}
		
		// Otherwise, whatever pointer of this type
		return CPointerType.of(this, __closeness);
	}
}
