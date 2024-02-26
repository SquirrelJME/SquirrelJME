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
	
	/** Unspecified signedness char. */
	CHAR("char"),
	
	/** Signed Char. */
	SIGNED_CHAR("signed char"),
	
	/** Unsigned char. */
	UNSIGNED_CHAR("unsigned char"),
	
	/** Signed short. */
	SIGNED_SHORT("short"),
	
	/** Unsigned short. */
	UNSIGNED_SHORT("unsigned short"),
	
	/** Signed integer. */
	SIGNED_INTEGER("int"),
	
	/** Unsigned integer. */
	UNSIGNED_INTEGER("unsigned int"),
	
	/** Long. */
	SIGNED_LONG("long"),
	
	/** Unsigned long. */
	UNSIGNED_LONG("unsigned long"),
	
	/** Signed long-long. */
	SIGNED_LONG_LONG("long long int"),
	
	/** Unsigned long-long. */
	UNSIGNED_LONG_LONG("unsigned long long"),
	
	/* End. */
	;
	
	/** Character string. */
	public static final CType CHAR_STAR =
		CPrimitiveType.CHAR.pointerType();
	
	/** Constant character string. */
	public static final CType CONST_CHAR_STAR =
		CPrimitiveType.CHAR.constType().pointerType();
	
	/** The single token used. */
	protected final String token;
	
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
		
		this.token = __token;
	}
	
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
	 * @since 2023/06/24
	 */
	@Override
	public List<String> declareTokens(CIdentifier __name)
		throws NullPointerException
	{
		if (__name == null)
			return Arrays.asList(this.token);
		return Arrays.asList(this.token, __name.identifier);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CType dereferenceType()
		throws IllegalArgumentException
	{
		/* {@squirreljme.error CW03 Cannot dereference a basic type.} */
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
	 * @since 2023/06/24
	 */
	@Override
	public CType pointerType(CPointerCloseness __closeness)
		throws IllegalArgumentException
	{
		if (__closeness == CPointerCloseness.NEAR)
			return this.pointerType();
		
		return CPointerType.of(this, __closeness);
	}
}
