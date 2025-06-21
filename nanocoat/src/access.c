/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/access.h"

sjme_errorCode sjme_nvm_access_checkEToE(
	sjme_attrInNotNull sjme_jmemberID from,
	sjme_attrInNotNull sjme_jmemberID to,
	sjme_attrInNotNull sjme_nvm_class_memberFlags* toFlags)
{
	sjme_jclass fromClass;
	sjme_jclass toClass;
	sjme_jclass rover;
	sjme_nvm_class_accessFlags* flags;
	sjme_jboolean checkPP;
	
	if (from == NULL || to == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Refers to the same exact method? */
	if (from == to)
		return SJME_ERROR_NONE;

	/* In the same class? */
	fromClass = from->inClass;
	toClass = to->inClass;
	if (fromClass == toClass)
		return SJME_ERROR_NONE;

	/* Target is public? */
	flags = &toFlags->access;
	if (flags->public)
		return SJME_ERROR_NONE;

	/* Target is protected? */
	checkPP = SJME_JNI_FALSE;
	if (flags->protected)
	{
		/* Must be a superclass of this one. */
		for (rover = fromClass; rover != NULL; rover = SJME_C_SU(rover))
			if (rover == toClass)
				return SJME_ERROR_NONE;
		
		/* Check failed, so also check package private as well. */
		checkPP = SJME_JNI_TRUE;
	}

	/* Target is packing private? */
	else if (!flags->private && !flags->protected && !flags->public)
		checkPP = SJME_JNI_TRUE;

	/* Must be in the same package? */
	if (checkPP)
	{
		/* The target class is nowhere? */
		if (toClass == NULL)
			return SJME_ERROR_MEMBER_ACCESS_DENIED;
		
		/* Must be in the same package. */
		if (sjme_charSeq_equalsR(fromClass->info->inPackage->seq,
			toClass->info->inPackage->seq))
			return SJME_ERROR_NONE;
	}

	/* Access denied. */
	return SJME_ERROR_MEMBER_ACCESS_DENIED;
}

sjme_errorCode sjme_nvm_access_checkFToF(
	sjme_attrInNotNull sjme_nvm_frame from,
	sjme_attrInNotNull sjme_jfieldID to)
{
	if (from == NULL || to == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Unwrap to other call. */
	return sjme_nvm_access_checkEToE(
		(sjme_jmemberID)from->inMethod,
		(sjme_jmemberID)to,
		&to->flags.member);
}

sjme_errorCode sjme_nvm_access_checkFToM(
	sjme_attrInNotNull sjme_nvm_frame from,
	sjme_attrInNotNull sjme_jmethodID to)
{
	if (from == NULL || to == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Unwrap to other call. */
	return sjme_nvm_access_checkEToE(
		(sjme_jmemberID)from->inMethod,
		(sjme_jmemberID)to,
		&to->flags.member);
}

sjme_errorCode sjme_nvm_access_checkMToM(
	sjme_attrInNotNull sjme_jmethodID from,
	sjme_attrInNotNull sjme_jmethodID to)
{
	/* Cast to other call. */
	return sjme_nvm_access_checkEToE(
		(sjme_jmemberID)from,
		(sjme_jmemberID)to,
		&to->flags.member);
}
