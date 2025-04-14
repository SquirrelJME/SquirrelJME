// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sun.javadoc;


public interface ExecutableMemberDoc
	extends MemberDoc
{

	public abstract String flatSignature();
	

	public abstract boolean isNative();
	

	public abstract boolean isSynchronized();
	

	public abstract boolean isVarArgs();
	

	public abstract ParamTag[] paramTags();
	

	public abstract Parameter[] parameters();
	

	public abstract Type receiverType();
	

	public abstract String signature();
	

	public abstract Type[] thrownExceptionTypes();
	

	public abstract ClassDoc[] thrownExceptions();
	

	public abstract ThrowsTag[] throwsTags();
	

	public abstract ParamTag[] typeParamTags();
	

	public abstract TypeVariable[] typeParameters();
}


