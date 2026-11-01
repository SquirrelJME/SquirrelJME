/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/classyVm.h"
#include "sjme/listUtil.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/instance.h"
#include "sjme/nvm/task.h"
#include "sjme/util.h"

static sjme_errorCode sjme_nvm_vmClass_isClassesAdd(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrOutNotNull sjme_list(sjme_phantom(sjme_jclass))** inOutClasses,
	sjme_attrInNotNull sjme_jclass addClass)
{
#define IS_CLASSES_GROW 8
	sjme_errorCode error;
	sjme_jint i, n, freeSlot;
	sjme_jclass checkClass;
	
	if (contextThread == NULL || inOutClasses == NULL || addClass == NULL)
		return SJME_ERROR_NONE;
	
	/* If this class was already added, do not add again. */
	n = 0;
	freeSlot = -1;
	if (*inOutClasses != NULL)
	{
		n = (*inOutClasses)->length;
		for (i = 0; i < n; i++)
		{
			checkClass = sjme_atomic_g(sjme_jclass,
				&(*inOutClasses)->elements[i]);
			if (checkClass == addClass)
				return SJME_ERROR_NONE;
			
			/* Quickly determined free slot. */
			if (freeSlot < 0 && checkClass == NULL)
				freeSlot = i;
		}
	}

	/* Is there a known free slot? */
	if (freeSlot >= 0)
	{
		sjme_atomic_s(sjme_jclass, &(*inOutClasses)->elements[freeSlot],
			addClass);
		return SJME_ERROR_NONE;
	}

	/* Grow the list. */
	if (sjme_error_is(error = sjme_list_replace(
		sjme_atomic_g(sjme_nvm, &contextThread->inState)->allocPool,
		n + IS_CLASSES_GROW, inOutClasses, sjme_jclass, 0)) ||
		(*inOutClasses) == NULL)
		return sjme_error_default(error);

	/* End of list is the first free. */
	freeSlot = n;

	/* Store into this slot. */
	sjme_atomic_s(sjme_jclass, &(*inOutClasses)->elements[freeSlot], addClass);
	return SJME_ERROR_NONE;
#undef IS_CLASSES_GROW
}

static sjme_errorCode sjme_nvm_vmClass_isClassesSub(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jclass rootClass,
	sjme_attrInNotNull sjme_jclass pivotClass,
	sjme_attrOutNotNull sjme_list(sjme_phantom(sjme_jclass))** inOutClasses)
{
	sjme_errorCode error;
	sjme_jint i, n;
	sjme_jclass subClass;
	
	if (contextThread == NULL || rootClass == NULL || pivotClass == NULL ||
		inOutClasses == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Ensure the root class is fully loaded. */
	if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(rootClass,
		contextThread)))
		return sjme_error_default(error);
	
	/* And the pivot class as well. */
	if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(pivotClass,
		contextThread)))
		return sjme_error_default(error);

	/* If this class was already added to the target, then do not process. */
	if (*inOutClasses != NULL)
		for (i = 0, n = (*inOutClasses)->length; i < n; i++)
			if (sjme_atomic_g(sjme_jclass, &(*inOutClasses)->elements[i]) ==
				pivotClass)
				return SJME_ERROR_NONE;
	
	/* Handle super class. */
	subClass = SJME_C_SU(pivotClass);
	if (subClass != NULL)
		if (sjme_error_is(error = sjme_nvm_vmClass_isClassesSub(
			contextThread, rootClass, subClass, inOutClasses)))
			return sjme_error_default(error);

	/* Handle interface class. */
	if (pivotClass->interfaceClasses != NULL)
		for (i = 0, n = pivotClass->interfaceClasses->length; i < n; i++)
		{
			subClass = pivotClass->interfaceClasses->elements[i];
			if (subClass != NULL)
				if (sjme_error_is(error = sjme_nvm_vmClass_isClassesSub(
					contextThread, rootClass, subClass,
					inOutClasses)))
					return sjme_error_default(error);
		}

	/* Add self. */
	if (sjme_error_is(error = sjme_nvm_vmClass_isClassesAdd(
		contextThread, inOutClasses, pivotClass)))
		return sjme_error_default(error);

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_vmClass_isAssignableFrom(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jclass canAssignTo,
	sjme_attrInNotNull sjme_jclass fromClass)
{
	sjme_errorCode error;
	sjme_list(sjme_phantom(sjme_jclass))* fromClasses;
	sjme_jint i, n;
	sjme_jint canDims, fromDims;

	if (contextThread == NULL || canAssignTo == NULL || fromClass == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Same exact class is simple. */
	if (canAssignTo == fromClass)
		return SJME_ERROR_NONE;

	/* Everything can be assigned to object! */
	if (canAssignTo == sjme_nvm_task_commonClassR(contextThread,
		SJME_NVM_COMMON_OBJECT))
		return SJME_ERROR_NONE;

	/* We need to compare if we can put an array in another array. */
	canDims = sjme_atomic_g(sjme_jint, &canAssignTo->numDimensions);
	fromDims = sjme_atomic_g(sjme_jint, &fromClass->numDimensions);
	if (canDims > 0 || fromDims > 0)
	{
		/* One side has reached zero, while the other side has not, */
		/* so there is no possible assignment here. */
		/* Note we want to do this only when zero is hit because it is */
		/* possible to store Float[][] into Object[], so we need to handle */
		/* situations like this. */
		if ((canDims == 0) != (fromDims == 0))
			return SJME_ERROR_CLASS_CAST;
		
		/* Recurse into the component type for the classes, since any array */
		/* of one component can fit in an array of another component. */
		return sjme_nvm_vmClass_isAssignableFrom(contextThread,
			sjme_atomic_g(sjme_jclass, 
				&canAssignTo->componentType),
			sjme_atomic_g(sjme_jclass, 
				&fromClass->componentType));
	}

	/* Get the list of classes the source class is. */
	/* b.getClass().isAssignableFrom(a.getClass()) == (a instanceof b). */
	fromClasses = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_isClasses(
		contextThread, fromClass, &fromClasses)) || fromClasses == NULL)
		return sjme_error_default(error);

	/* Can any of these classes be assigned to this? */
	for (i = 0, n = fromClasses->length; i < n; i++)
		if (canAssignTo == sjme_atomic_g(sjme_jclass,
			&fromClasses->elements[i]))
			return SJME_ERROR_NONE;

#if 0
	/* If the target is an interface, we need to check further as a direct */
	/* class match failed. */
	if (SJME_NVM_ACC_IS(SJME_CL_I(canAssignTo)->flags, INTERFACE))
	{
		sjme_todo("Impl?");
		return sjme_error_notImplemented(0);
	}
#endif

	/* Failed to find a match. */
	return SJME_ERROR_CLASS_CAST;
}

sjme_errorCode sjme_nvm_vmClass_isClasses(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jclass inClass,
	sjme_attrOutNotNull sjme_list(sjme_phantom(sjme_jclass))** outIsClasses)
{
	sjme_errorCode error;
	sjme_nvm_isClasses isClasses;
	sjme_list(sjme_phantom(sjme_jclass))* result;
	sjme_jint i, n, numInterfaces, at;
	sjme_jclass checkClass;
	sjme_list(sjme_jinterfaceID)* interfaceBinds;
	sjme_jinterfaceID interfaceBind;
	
	if (contextThread == NULL || inClass == NULL || outIsClasses == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Get pre-allocated is-classes. */
	isClasses = inClass->isClasses;
	if (isClasses == NULL)
		return SJME_ERROR_ILLEGAL_STATE;

	/* Result already set? */
	result = isClasses->classes;
	if (result != NULL)
	{
		*outIsClasses = result;
		return SJME_ERROR_NONE;
	}

	/* Lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&isClasses->common.lock)))
		return sjme_error_default(error);

	/* Double check get. */
	interfaceBinds = NULL;
	result = isClasses->classes;
	if (result == NULL)
	{
		/* Start at this current class. */
		if (sjme_error_is(error = sjme_nvm_vmClass_isClassesSub(
			contextThread, inClass, inClass, &result)))
			goto fail_scanSelf;

		/* Store result. */
		isClasses->classes = result;

		/* Normalize list count, so there are no NULLs at the end. */
		/* Also count the number of every interface. */
		n = result->length;
		numInterfaces = 0;
		for (i = 0; i < n; i++)
		{
			/* End of the is-classes list. */
			checkClass = sjme_atomic_g(sjme_jclass, &result->elements[i]);
			if (checkClass == NULL)
				break;

			/* Is this an interface? */
			if (SJME_NVM_ACC_IS(SJME_CL_I(checkClass)->flags, INTERFACE))
				numInterfaces++;
		}

		/* Set new length? */
		if (i < result->length)
		{
			result->length = i;
			n = i;
		}
		
		/* Setup interface binds, if any. */
		if (numInterfaces > 0)
		{
			/* Allocate interface binds. */
			if (sjme_error_is(error = sjme_list_alloc(
				sjme_atomic_g(sjme_nvm, &contextThread->inState)->allocPool,
				numInterfaces, &interfaceBinds, sjme_jinterfaceID, 0)) ||
				interfaceBinds == NULL)
				goto fail_allocBinds;

			/* Store binds. */
			inClass->interfaceBinds = interfaceBinds;
			
			/* Individually setup each interface bind in an initial state. */
			for (i = 0, at = 0; i < n; i++)
			{
				/* Not an interface? */
				checkClass = sjme_atomic_g(sjme_jclass, &result->elements[i]);
				if (checkClass == NULL ||
					!SJME_NVM_ACC_IS(SJME_CL_I(checkClass)->flags, INTERFACE))
					continue;

				/* Allocate interface bind. */
				interfaceBind = NULL;
				if (sjme_error_is(error = sjme_nvm_alloc(
					sjme_atomic_g(sjme_nvm, &contextThread->inState),
					sizeof(*interfaceBind),
					SJME_NVM_STRUCT_INTERFACE_ID,
					SJME_AS_NVM_COMMONP(&interfaceBind))) ||
					interfaceBind == NULL)
					goto fail_allocBind;

				/* Initialize data for it. */
				interfaceBind->isInterface = checkClass;
				interfaceBind->descriptorHash =
					sjme_charSeq_hashR(SJME_CL_I(checkClass)->name->seq);
				
				/* Store at the next interface position. */
				interfaceBinds->elements[at++] = interfaceBind;
			}
		}
	}

	/* Release lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&isClasses->common.lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	*outIsClasses = result;
	return SJME_ERROR_NONE;

fail_allocBind:
fail_allocBinds:
fail_scanSelf:
fail_badAlloc:
	/* Release before failing. */
	sjme_thread_spinLockRelease(&isClasses->common.lock, NULL);

	return sjme_error_default(error);
}

sjme_jboolean sjme_nvm_vmClass_isSuperClass(
	sjme_attrInNotNull sjme_jclass thisClass,
	sjme_attrInNotNull sjme_jclass otherClass)
{
	sjme_jclass rover;
	
	if (thisClass == NULL || otherClass == NULL)
		return SJME_JNI_FALSE;

	/* If these are the same class, this cannot be true. */
	if (thisClass == otherClass)
		return SJME_JNI_FALSE;

	/* Try to find the other class. */
	for (rover = SJME_C_SU(thisClass); rover != NULL; rover = SJME_C_SU(rover))
		if (rover == otherClass)
			return SJME_JNI_TRUE;

	/* Not found, so is not one. */
	return SJME_JNI_FALSE;
}
