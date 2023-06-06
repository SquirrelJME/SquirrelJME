// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents a basic type within NanoCoat.
 *
 * @since 2023/05/29
 */
public enum CPrimitiveType
	implements CType
{
	/** Void. */
	VOID("void"),
	
	/** Signed Byte. */
	INT8_T("int8_t"),
	
	/** Unsigned byte. */
	UINT8_T("uint8_t"),
	
	/** Signed Short. */
	INT16_T("int16_t"),
	
	/** Unsigned short. */
	UINT16_T("uint16_t"),
	
	/** Signed Integer. */
	INT32_T("int32_t"),
	
	/** Unsigned Integer. */
	UINT32_T("uint32_t"),
	
	/* End. */
	;
	
	/** The single token used. */
	protected final List<String> token;
	
	/** Pointer type. */
	private volatile Reference<CType> _pointerType;
	
	/** Constant type. */
	private volatile Reference<CType> _constType;
	
	/**
	 * Initializes the basic type.
	 * 
	 * @param __token The token for this type.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	CPrimitiveType(String __token)
		throws NullPointerException
	{
		if (__token == null)
			throw new NullPointerException("NARG");
		
		this.token = UnmodifiableList.of(Arrays.asList(__token));
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
		CType rv = null;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = CModifiedType.of(CConstModifier.CONST, this);
			this._constType = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		// {@squirreljme.error CW03 Cannot dereference a basic type.}
		throw new IllegalArgumentException("CW03");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/05
	 */
	@Override
	public boolean isPointer()
	{
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CType pointerType()
		throws IllegalArgumentException
	{
		Reference<CType> ref = this._pointerType;
		CType rv = null;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			rv = CPointerType.of(this);
			this._pointerType = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public List<String> tokens(CTokenSet __set)
	{
		// Primitive types are all the same
		return this.token;
	}
}
