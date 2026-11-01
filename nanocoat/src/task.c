/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/bytecode.h"
#include "sjme/util.h"
#include "sjme/nvm/instance.h"
#include "sjme/stdGone.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/loop.h"
#include "sjme/debug.h"
#include "sjme/nvm/nvm.h"
#include "sjme/nvm/cleanup.h"
#include "sjme/nvm/instanceInspect.h"
#include "sjme/nvm/instanceProxy.h"
#include "sjme/nvm/mleProxies.h"

/** The number of tasks to grow by. */
#define SJME_NVM_TASK_GROW 4

/** The size to grow the Jar package list by. */
#define SJME_NVM_TASK_JAR_GROW 32

/** The number of threads to grow by. */
#define SJME_NVM_THREAD_GROW 8

static sjme_errorCode sjme_nvm_task_preload(
	sjme_attrInNotNull sjme_nvm_task inTask,
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_errorCode error;
	sjme_nvm_task_commonClassId ccId; 
	sjme_jint i;
	sjme_jclass classy;
	
	if (inTask == NULL || inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* Load all common classes, in reverse order! Perform a load of */
	/* everything first, then initialize during a second stage. */
	for (i = 0; i < 2; i++)
		for (ccId = SJME_NVM_TASK_NUM_COMMON_CLASS - 1;
			ccId > SJME_NVM_COMMON_NULL; ccId--)
		{
			/* Skip these specific classes. */
			/* Mostly ScritchUI related classes are skipped because they might */
			/* not actually exist and they will work if loaded later. */
			if (ccId == SJME_NVM_COMMON_SCRITCH_UI_PROXY ||
				(ccId >= SJME_NVM_COMMON_SCRITCH_UI_TYPE_BASE &&
					ccId <= SJME_NVM_COMMON_SCRITCH_UI_TYPE_LAST) ||
				ccId == SJME_NVM_COMMON_VERY_IMPORTANT ||
				ccId == SJME_NVM_COMMON_NULL)
				continue;
			
			/* Load the common class, only initialize on the second run. */
			classy = NULL;
			if (sjme_error_is(error = sjme_nvm_task_commonClass(inThread,
				ccId, &classy, (i != 0))) ||
				classy == NULL)
				return sjme_error_default(error);
		}
	
	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_bracketJarPackage(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_nvm_rom_library inLibrary,
	sjme_attrOutNotNull sjme_jbracketJarPackage* outBracket)
{
	sjme_errorCode error;
	sjme_nvm_task_globals* globals;
	sjme_list(sjme_jbracketJarPackage)* brackets;
	sjme_jbracketJarPackage result;
	sjme_jint i, n;
	
	if (contextThread == NULL || inLibrary == NULL || outBracket == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* We need the task globals. */
	globals = &SJME_T_K(contextThread)->globals;

	/* Grab the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(&globals->lock)))
		return sjme_error_default(error);

	/* Check pre-existing brackets, note that it is possible for there */
	/* to be blank slots for quick ordering. */
	result = NULL;
	brackets = globals->jarBrackets;
	if (brackets != NULL)
		for (i = 0, n = brackets->length; i < n; i++)
			if (brackets->elements[i] != NULL &&
				inLibrary == brackets->elements[i]->library)
			{
				result = brackets->elements[i];
				break;
			}

	/* Need to create a new bracket for this library. */
	if (result == NULL)
	{
		/* Initialize bracket. */
		if (sjme_error_is(error = sjme_nvm_instance_objectNewBracket(
			contextThread,
			SJME_NVM_STRUCT_BRACKET_JAR_PACKAGE_INSTANCE, -1,
			SJME_AS_JOBJECTP(&result))) || result == NULL)
			goto fail_newBracket;

		/* Set details. */
		result->library = sjme_weakUpR(sjme_nvm_rom_library, inLibrary);

		/* Cache it into the list. */
		if (sjme_error_is(error = sjme_list_injectGrow(
			SJME_T_S(contextThread)->allocPool, SJME_NVM_TASK_JAR_GROW,
			&brackets, result,
			sjme_jbracketJarPackage, 0)) || brackets == NULL)
			goto fail_growList;
		
		/* Store new list. */
		globals->jarBrackets = brackets;
	}
	
	/* Release the lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&globals->lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	*outBracket = result;
	return SJME_ERROR_NONE;

fail_newBracket:
fail_countUp:
fail_growList:
	sjme_thread_spinLockRelease(&globals->lock, NULL);
	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_commonClass(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInValue sjme_nvm_task_commonClassId commonId,
	sjme_attrOutNotNull sjme_jclass* outClass,
	sjme_attrInValue sjme_jboolean doInit)
{
	sjme_errorCode error;
	sjme_lpcstr commonName;
	sjme_nvm_instance_proxyHandlerFunc proxyHandler;
	sjme_jclass result, baseClass;
	
	if (contextThread == NULL)
		return SJME_ERROR_NONE;

	if (commonId <= SJME_NVM_COMMON_NULL ||
		commonId == SJME_NVM_COMMON_VERY_IMPORTANT ||
		commonId >= SJME_NVM_TASK_NUM_COMMON_CLASS)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Already cached? */
	result = sjme_atomic_g(sjme_jclass, 
		&sjme_atomic_g(sjme_nvm_task, &contextThread->inTask)
		->globals.commonClasses[commonId]);
	if (result != NULL)
	{
		/* Make sure it is initialized if we are wanting that. */
		if (doInit)
			if (sjme_error_is(error = sjme_nvm_vmClass_checkInit(result,
				contextThread)))
				return sjme_error_default(error);
		
		/* Return the class. */
		*outClass = result;
		return SJME_ERROR_NONE;
	}

	/* Used for ScritchUI. */
#define SJME_NVM_CC_SCRITCHUI(caps, proper) \
	case SJME_TOKEN_PASTE_PP(SJME_NVM_COMMON_SCRITCH_UI_TYPE_, caps): \
		commonName = "Lcc/squirreljme/jvm/mle/scritchui/brackets/Scritch" \
			#proper "Bracket;"; \
		break;
	
	/* What is the name of the common class? */
	commonName = NULL;
	proxyHandler = NULL;
	switch (commonId)
	{
		case SJME_NVM_COMMON_CLASS:
			commonName = "Ljava/lang/Class;";
			break;

		case SJME_NVM_COMMON_EXCEPTION_ARRAY_STORE:
			commonName = "Ljava/lang/ArrayStoreException;";
			break;

		case SJME_NVM_COMMON_EXCEPTION_ARRAY_IOOB:
			commonName = "Ljava/lang/ArrayIndexOutOfBoundsException;";
			break;

		case SJME_NVM_COMMON_EXCEPTION_CLASS_CAST:
			commonName = "Ljava/lang/ClassCastException;";
			break;
			
		case SJME_NVM_COMMON_EXCEPTION_CLASS_NOT_FOUND:
			commonName = "Ljava/lang/ClassNotFoundException;";
			break;

		case SJME_NVM_COMMON_EXCEPTION_LINKAGE_ERROR:
			commonName = "Ljava/lang/LinkageError;";
			break;
			
		case SJME_NVM_COMMON_EXCEPTION_MLE_CALL:
			commonName = "Lcc/squirreljme/jvm/mle/exceptions/MLECallError;";
			break;

		case SJME_NVM_COMMON_EXCEPTION_NULL_POINTER:
			commonName = "Ljava/lang/NullPointerException;";
			break;

		case SJME_NVM_COMMON_JAR_PACKAGE:
			commonName = "Lcc/squirreljme/jvm/mle/brackets/JarPackageBracket;";
			break;
		
		case SJME_NVM_COMMON_OBJECT:
			commonName = "Ljava/lang/Object;";
			break;

		case SJME_NVM_COMMON_PIPE:
			commonName = "Lcc/squirreljme/jvm/mle/brackets/PipeBracket;";
			break;
	
		case SJME_NVM_COMMON_PRIMITIVE_BOOLEAN:
			commonName = "Z";
			break;
	
		case SJME_NVM_COMMON_PRIMITIVE_BYTE:
			commonName = "B";
			break;
	
		case SJME_NVM_COMMON_PRIMITIVE_CHARACTER:
			commonName = "C";
			break;
	
		case SJME_NVM_COMMON_PRIMITIVE_DOUBLE:
			commonName = "D";
			break;
	
		case SJME_NVM_COMMON_PRIMITIVE_FLOAT:
			commonName = "F";
			break;
	
		case SJME_NVM_COMMON_PRIMITIVE_INTEGER:
			commonName = "I";
			break;
	
		case SJME_NVM_COMMON_PRIMITIVE_LONG:
			commonName = "J";
			break;
	
		case SJME_NVM_COMMON_PRIMITIVE_SHORT:
			commonName = "S";
			break;
	
		case SJME_NVM_COMMON_PRIMITIVE_VOID:
			commonName = "F";
			break;
			
		case SJME_NVM_COMMON_BOXED_BOOLEAN:
			commonName = "Ljava/lang/Boolean;";
			break;
			
		case SJME_NVM_COMMON_BOXED_BYTE:
			commonName = "Ljava/lang/Byte;";
			break;
			
		case SJME_NVM_COMMON_BOXED_CHARACTER:
			commonName = "Ljava/lang/Character;";
			break;
			
		case SJME_NVM_COMMON_BOXED_DOUBLE:
			commonName = "Ljava/lang/Double;";
			break;
			
		case SJME_NVM_COMMON_BOXED_FLOAT:
			commonName = "Ljava/lang/Float;";
			break;
			
		case SJME_NVM_COMMON_BOXED_INTEGER:
			commonName = "Ljava/lang/Integer;";
			break;
			
		case SJME_NVM_COMMON_BOXED_LONG:
			commonName = "Ljava/lang/Long;";
			break;
			
		case SJME_NVM_COMMON_BOXED_SHORT:
			commonName = "Ljava/lang/Short;";
			break;

		case SJME_NVM_COMMON_REFERENCE_PHANTOM:
			commonName = "Ljava/lang/ref/PhantomReference;";
			break;

		case SJME_NVM_COMMON_REFERENCE_SOFT:
			commonName = "Ljava/lang/ref/SoftReference;";
			break;

		case SJME_NVM_COMMON_REFERENCE_WEAK:
			commonName = "Ljava/lang/ref/WeakReference;";
			break;

		case SJME_NVM_COMMON_RUNNABLE:
			commonName = "Ljava/lang/Runnable;";
			break;

		case SJME_NVM_COMMON_SCRITCH_UI_PROXY:
			commonName = "Lcc/squirreljme/jvm/mle/scritchui/"
				"ScritchUnifiedInterface;";
			proxyHandler = sjme_nvm_mle_scritchUiProxyHandler;
			break;
		
		case SJME_NVM_COMMON_STRING:
			commonName = "Ljava/lang/String;";
			break;

		case SJME_NVM_COMMON_THREAD:
			commonName = "Ljava/lang/Thread;";
			break;
		
		case SJME_NVM_COMMON_THROWABLE:
			commonName = "Ljava/lang/Throwable;";
			break;

		case SJME_NVM_COMMON_TRACE_POINT:
			commonName = "Lcc/squirreljme/jvm/mle/brackets/TracePointBracket;";
			break;
			
		case SJME_NVM_COMMON_VM_THREAD:
			commonName = "Lcc/squirreljme/jvm/mle/brackets/VMThreadBracket;";
			break;

			/* ScritchUI Types. */
		SJME_NVM_CC_SCRITCHUI(BASE, Base);
		SJME_NVM_CC_SCRITCHUI(BUTTON, Button);
		SJME_NVM_CC_SCRITCHUI(CHOICE, Choice);
		SJME_NVM_CC_SCRITCHUI(COMBOBOX, ComboBox);
		SJME_NVM_CC_SCRITCHUI(COMPONENT, Component);
		SJME_NVM_CC_SCRITCHUI(CONTAINER, Container);
		SJME_NVM_CC_SCRITCHUI(LABEL, Label);
		SJME_NVM_CC_SCRITCHUI(LIST, List);
		SJME_NVM_CC_SCRITCHUI(MENU_BAR, MenuBar);
		SJME_NVM_CC_SCRITCHUI(MENU, Menu);
		SJME_NVM_CC_SCRITCHUI(MENU_HAS_CHILDREN, MenuHasChildren);
		SJME_NVM_CC_SCRITCHUI(MENU_HAS_LABEL, MenuHasLabel);
		SJME_NVM_CC_SCRITCHUI(MENU_HAS_PARENT, MenuHasParent);
		SJME_NVM_CC_SCRITCHUI(MENU_ITEM, MenuItem);
		SJME_NVM_CC_SCRITCHUI(MENU_KIND, MenuKind);
		SJME_NVM_CC_SCRITCHUI(PAINTABLE, Paintable);
		SJME_NVM_CC_SCRITCHUI(PANEL, Panel);
		SJME_NVM_CC_SCRITCHUI(PENCIL, Pencil);
		SJME_NVM_CC_SCRITCHUI(PROGRESSBAR, ProgressBar);
		SJME_NVM_CC_SCRITCHUI(SCREEN, Screen);
		SJME_NVM_CC_SCRITCHUI(SCROLLBAR, ScrollBar);
		SJME_NVM_CC_SCRITCHUI(SCROLL_PANEL, ScrollPanel);
		SJME_NVM_CC_SCRITCHUI(SLIDER, Slider);
		SJME_NVM_CC_SCRITCHUI(STATE, State);
		SJME_NVM_CC_SCRITCHUI(TEXTBOX, TextBox);
		SJME_NVM_CC_SCRITCHUI(TRIGGER, Trigger);
		SJME_NVM_CC_SCRITCHUI(VIEW, View);
		SJME_NVM_CC_SCRITCHUI(WINDOW, Window);
		
		default:
			return SJME_ERROR_INVALID_ARGUMENT;
	}

	/* No longer needed. */
#undef SJME_NVM_CC_SCRITCHUI

	/* Load the common class. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_vmClass_loaderLoadFU(
		SJME_F_CL(contextThread), &result, contextThread,
		commonName, doInit)) || result == NULL)
		return sjme_error_vmError(contextThread, error);

	/* If this is a proxy, we need to set up the proxy with the handler. */
	if (proxyHandler != NULL)
	{
		/* Generate the proxy class. */
		baseClass = result;
		result = NULL;
		if (sjme_error_is(error = sjme_nvm_instance_proxyClassV(
			contextThread, &result, proxyHandler, baseClass, NULL)) ||
			result == NULL || baseClass == result)
			return sjme_error_vmError(contextThread, error);
	}

	/* Count it up once since it is a global class. */
	if (sjme_error_is(error = sjme_nvm_instance_countUp(
		SJME_AS_JOBJECT(result))))
		return sjme_error_vmError(contextThread, error);

	/* Cache for later. */
	sjme_atomic_cs(sjme_jclass, 
		&sjme_atomic_g(sjme_nvm_task,
			&contextThread->inTask)->globals.commonClasses[commonId],
		NULL, sjme_weakUpR(sjme_jclass, result));

	/* Success! */
	*outClass = result;
	return SJME_ERROR_NONE;
}

sjme_jclass sjme_nvm_task_commonClassR(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInRange(0, SJME_NVM_TASK_NUM_COMMON_CLASS)
		sjme_nvm_task_commonClassId commonId)
{
	sjme_jclass result;
	
	if (contextThread == NULL)
		return NULL;

	if (commonId <= SJME_NVM_COMMON_NULL ||
		commonId == SJME_NVM_COMMON_VERY_IMPORTANT || 
		commonId >= SJME_NVM_TASK_NUM_COMMON_CLASS)
		return NULL;

	/* Load the class. */
	result = NULL;
	if (sjme_error_is(sjme_nvm_task_commonClass(contextThread, commonId,
		&result, SJME_JNI_TRUE)) || result == NULL)
		return NULL;

	/* Success! */
	return result;
}

sjme_errorCode sjme_nvm_task_stackTraceStep(
	sjme_attrInNotNull sjme_nvm_task_stackTraceState* traceState,
	sjme_attrInNotNull sjme_jclass atClass,
	sjme_attrInNotNull sjme_nvm_class_codeInfo atCode,
	sjme_attrInValue sjme_jint atPc,
	sjme_attrInValue sjme_byteCode atIv)
{
	if (traceState == NULL || atClass == NULL || atCode == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;
	
	/* The compact SquirrelJME format is in the following form: */
 	/*  | IN java.lang.Class (Class.java) */
  	/*  |- .whatever:(Lboop;)V @0h (:181 INVOKEINTERFACE@15) */
	
	/* Did the class change? */
	/* | IN java.lang.Class (Class.java) */
	traceState->nowClass = atClass;
	if (traceState->nowClass != traceState->lastClass)
		sjme_emitB(" | IN %s (%s)",
			sjme_charSeq_tempUtf(traceState->nowClass->fieldName),
				"<UNKNOWN>");

	/* Print method trace. */
	/*  |- .whatever:(Lboop;)V @0h (:181 INVOKEINTERFACE@15) */
	traceState->nowCode = atCode;
	traceState->nowMethod = (traceState->nowCode != NULL ?
		sjme_atomic_g(sjme_nvm_class_methodInfo, &atCode->inMethod) : NULL);
	traceState->pc = atPc;
	traceState->instructionId = (atIv != 0 ? atIv :
		(traceState->nowCode != NULL && traceState->pc >= 0 &&
			traceState->pc < traceState->nowCode->rawCodeLen ?
			traceState->nowCode->rawCode[traceState->pc] & 0xFF : -1));
	if (traceState->nowCode == NULL || traceState->nowMethod == NULL)
		sjme_emitB(" | PURE VIRTUAL");
	else
		sjme_emitB(" | .%s:%s @%xh (:%d #%s@%d)",
			sjme_charSeq_tempUtf(traceState->nowMethod->name->seq),
			sjme_charSeq_tempUtf(traceState->nowMethod->type->seq),
			traceState->pc,
			-1,
			sjme_nvm_byteCode_names[traceState->instructionId & 0xFF],
			traceState->pc);

	/* Set for next run. */
	traceState->lastClass = traceState->nowClass;

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_stackTraceThread(
	sjme_attrInNotNull sjme_nvm_thread inThread)
{
	sjme_errorCode error;
	sjme_nvm_frame frame;
	sjme_nvm_task_stackTraceState traceState;
	sjme_jint i;
	
	if (inThread == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Empty? Do nothing. */
	if (inThread->numFrames == 0)
		return SJME_ERROR_NONE;
	
	/* Start from the top of the stack. */
	memset(&traceState, 0, sizeof(traceState));
	for (i = inThread->numFrames - 1; i >= 0; i--)
	{
		/* Which frame is this? */
		frame = inThread->frames->elements[i];

		/* Step trace. */
		if (sjme_error_is(error = sjme_nvm_task_stackTraceStep(
			&traceState, frame->inClass, frame->inCode,
			frame->lastPc, frame->lastIv)))
			return sjme_error_default(error);
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_stackTraceThrowable(
	sjme_attrInNotNull sjme_nvm_thread contextThread,
	sjme_attrInNotNull sjme_jthrowable inThrowable)
{
	sjme_errorCode error;
	sjme_jarray pointArray;
	sjme_jbracketTrace point;
	sjme_jclass throwableClass;
	sjme_nvm_task_stackTraceState traceState;
	sjme_jint i;
	sjme_jfieldID messageId, causeId;
	sjme_jstring message;
	sjme_charSeq messageSeq;
	sjme_jboolean printedMessage;
	sjme_jobject cause;
	
	if (inThrowable == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Must be Throwable. */
	throwableClass = sjme_nvm_task_commonClassR(contextThread,
		SJME_NVM_COMMON_THROWABLE);
	if (sjme_error_is(error = sjme_nvm_vmClass_isAssignableFrom(contextThread,
		throwableClass,
		sjme_atomic_g(sjme_jclass, &inThrowable->object.isClass))))
		return sjme_error_mask(error, SJME_ERROR_CLASS_CAST);

	/* Locate the message field, if at all possible... */
	messageId = NULL;
	if (sjme_error_is(sjme_nvm_vmField_idByNameTypeU(throwableClass,
		contextThread,
		SJME_NVM_CLASS_MEMBER_INSTANCE, SJME_JNI_FALSE,
		"_message", "Ljava/lang/String;", &messageId)))
		messageId = NULL;

	/* Locate the cause field as well, if possible... */
	causeId = NULL;
	if (sjme_error_is(sjme_nvm_vmField_idByNameTypeU(throwableClass,
		contextThread,
		SJME_NVM_CLASS_MEMBER_INSTANCE, SJME_JNI_FALSE,
		"_cause", "Ljava/lang/Throwable;", &causeId)))
		causeId = NULL;

	/* Is the message field valid? */
	printedMessage = SJME_JNI_FALSE;
	if (messageId != NULL)
	{
		/* Read the message. */
		message = NULL;
		if (sjme_error_is(error = sjme_nvm_instance_fieldGetO(
			contextThread,
			SJME_AS_JOBJECT(inThrowable), messageId,
			SJME_NVM_STRUCT_STRING_INSTANCE,
			(sjme_pointer*)&message)))
			return sjme_error_default(error);
		
		/* Is this a valid string? */
		messageSeq = NULL;
		if (message != NULL)
			messageSeq = sjme_atomic_g(sjme_charSeq, &message->seq);
		
		/* If the sequence is valid, print it. */
		if (messageSeq != NULL)
		{
			sjme_emitB("EXCEPTION %s: %s",
				sjme_charSeq_tempUtf(
					sjme_atomic_g(sjme_jclass,
						&inThrowable->object.isClass)->fieldName),
				sjme_charSeq_tempUtf(messageSeq));
			printedMessage = SJME_JNI_TRUE;
		}
	}

	/* If we did not print the message, we can at least print the class. */
	if (!printedMessage)
		sjme_emitB("EXCEPTION %s: <NO MESSAGE>",
			sjme_charSeq_tempUtf(
				sjme_atomic_g(sjme_jclass,
					&inThrowable->object.isClass)->fieldName));

	/* There may be trace points in this. */
	pointArray = (sjme_jarray)sjme_atomic_g(sjme_intPointer, 
		&inThrowable->object.special);
	if (pointArray != NULL)
	{
		/* Not an array? */
		if (!sjme_nvm_isAR(pointArray, SJME_NVM_STRUCT_ARRAY_INSTANCE))
			return SJME_ERROR_CLASS_CAST;

		/* Go through and extract points per each. */
		memset(&traceState, 0, sizeof(traceState));
		for (i = 0; i < pointArray->e.length; i++)
		{
			/* Read in. */
			point = NULL;
			if (sjme_error_is(error = sjme_nvm_instance_arrayGetO(
				pointArray, i, (sjme_jobject*)&point)))
				return sjme_error_default(error);
			
			/* Ignore NULLs. */
			if (point == NULL)
				continue;
			
			/* Must be a trace point. */
			if (!sjme_nvm_isAR(point,
				SJME_NVM_STRUCT_BRACKET_TRACE_INSTANCE))
				return SJME_ERROR_CLASS_CAST;

			/* Step trace. */
			if (sjme_error_is(error = sjme_nvm_task_stackTraceStep(
				&traceState, point->capture.inClass, point->capture.inCode,
				point->capture.lastPc, point->capture.lastIv)))
				return sjme_error_default(error);
		}
	}

#if defined(SJME_CONFIG_DEBUG)
	else
		sjme_message("THROWABLE HAS BLANK TRACE!");
#endif

	/* Recurse into cause? */
	if (causeId != NULL)
	{
		/* Read the message. */
		cause = NULL;
		if (sjme_error_is(error = sjme_nvm_instance_fieldGetO(
			contextThread,
			SJME_AS_JOBJECT(inThrowable), causeId,
			SJME_NVM_STRUCT_ANY_OBJECT_INSTANCE,
			(sjme_pointer*)&cause)))
			return sjme_error_default(error);
		
		/* Only if there is an actual cause. */
		if (cause != NULL)
		{
			/* Not a throwable? */
			if (sjme_error_is(error = sjme_nvm_vmClass_isAssignableFrom(
				contextThread, throwableClass,
				sjme_atomic_g(sjme_jclass, &cause->isClass))))
				return sjme_error_default(error);
		
			sjme_emitB("CAUSED BY:");
			return sjme_nvm_task_stackTraceThrowable(contextThread,
				SJME_AS_JTHROWABLE(cause));
		}
	}

	/* Success! */
	return SJME_ERROR_NONE;
}

sjme_errorCode sjme_nvm_task_taskEnterMain(
	sjme_attrInNotNull sjme_nvm_task inTask,
	sjme_attrOutNullable sjme_nvm_thread* outThread)
{
	sjme_errorCode error;
	sjme_nvm inState;
	sjme_cchar adjustMain[SJME_NVM_CLASS_NAME_LIMIT];
	sjme_nvm_thread mainThread, scritchUiThread;
	sjme_jint i, n;
	const sjme_nvm_task_taskNewConfig* initConfigCopy;
	sjme_list(sjme_jstring)* argStrings;
	sjme_jstring argString;
	sjme_scritchui scritchUi;

	/* TODO FIXME: Optimize/shrink for SDCC. */
	
	if (inTask == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	/* Lock task. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inTask->object.common.lock)))
		return sjme_error_default(error);

	/* Main thread already set? */
	if (sjme_atomic_g(sjme_nvm_thread, &inTask->globals.mainThread) != NULL)
		goto fail_mainExists;

	/* Quicker to reference this way. */
	inState = sjme_atomic_g(sjme_nvm, &inTask->inState);

	/* Recover the initial config. */
	initConfigCopy = inTask->initConfig;
	
	/* Setup main thread, all threads start in java.lang.__Start__! */
	mainThread = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadNew(inTask,
		&mainThread, "main", SJME_JNI_TRUE,
		SJME_JNI_FALSE)) ||
		mainThread == NULL)
		goto fail_taskNewThread;

	/* Setup special ScritchUI thread? */
	scritchUiThread = NULL;
	scritchUi = sjme_atomic_g(sjme_pointer, &inState->globals.scritchUi);
	if (scritchUi != NULL)
	{
		/* Create new thread specifically for ScritchUI. */
		if (sjme_error_is(error = sjme_nvm_task_threadNew(inTask,
			&scritchUiThread, "scritchUiThread",
			SJME_JNI_FALSE, SJME_JNI_TRUE)) ||
			scritchUiThread == NULL)
			goto fail_taskNewThread;

		/* If ScritchUI uses an event thread that is independent, then it */
		/* uses explicit handling which is called from the callback. */
		/* This will never be scheduled cooperatively. */
		if (!sjme_thread_equal(scritchUi->loopThread,
			SJME_THREAD_NULL))
			scritchUiThread->flags |= SJME_NVM_THREAD_IS_EXPLICIT;

		/* ScritchUI is in our thread, so we need to inline it accordingly. */
		/* It effectively needs to own the VM execution and run everything */
		/* in it for the most part. */
		else
		{
			sjme_todo("Impl?");
			return sjme_error_notImplemented(0);
		}

		/* Store ScritchUI thread */
		sjme_atomic_s(sjme_nvm_thread, &inTask->globals.scritchUiThread,
			sjme_weakUpR(sjme_nvm_thread, scritchUiThread));
	}

	/* No thread is available. */
	else
		sjme_atomic_s(sjme_nvm_thread, &inTask->globals.scritchUiThread,
			NULL);

	/* Adjust the main class name, turn periods into slashes. */
	memset(adjustMain, 0, sizeof(adjustMain));
	snprintf(adjustMain, SJME_NVM_CLASS_NAME_LIMIT - 1,
		"%s", initConfigCopy->mainClass);
	for (i = 0, n = sjme_util_sizeToInt(strlen(adjustMain)); i < n; i++)
		if (adjustMain[i] == '.')
			adjustMain[i] = '/';

	/* Setup string for main class. */
	/* Counting does not need to be done because these are both */
	/* intern strings and when pushed to the stack they get counted. */
	inTask->globals.mainClassName = NULL;
	if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfUtf(
		mainThread, &inTask->globals.mainClassName, SJME_JNI_TRUE,
		adjustMain)) ||
		inTask->globals.mainClassName == NULL)
		goto fail_mainClassString;

	/* Setup strings for main arguments. */
	argStrings = NULL;
	if (initConfigCopy->mainArgs != NULL &&
		initConfigCopy->mainArgs->length > 0)
	{
		/* Setup string list. */
		n = initConfigCopy->mainArgs->length;
		if (sjme_error_is(error = sjme_list_alloc(inState->allocPool,
			n, &argStrings, sjme_jstring, 0)) || argStrings == NULL)
			goto fail_mainArgsStrings;

		/* Setup strings for each argument. */
		for (i = 0; i < n; i++)
		{
			/* Intern argument string. */
			argString = NULL;
			if (sjme_error_is(error = sjme_nvm_task_threadStringValueOfUtf(
				mainThread, &argString, SJME_JNI_TRUE,
				initConfigCopy->mainArgs->elements[i])) ||
				argString == NULL)
				goto fail_mainArgsString;
			argStrings->elements[i] = sjme_weakUp(argString);
		}
	}
		
	/* Set argument strings. */
	inTask->globals.mainArgs = argStrings;
	
	/* Take this opportunity to do some preloading initialization. */
	if (sjme_error_is(error = sjme_nvm_task_preload(inTask, mainThread)))
		goto fail_preload;
	
	/* The main thread of any task is always implicitly started. */
	if (sjme_error_is(error = sjme_nvm_task_threadStart(mainThread)))
		goto fail_startMain;
	
	/* Unlock task. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inTask->object.common.lock, NULL)))
		return sjme_error_default(error);

	/* Success! */
	if (outThread != NULL)
		*outThread = mainThread;
	return SJME_ERROR_NONE;

fail_startMain:
fail_preload:
fail_countMainArgString:
fail_mainArgsString:
fail_mainArgsStrings:
	if (argStrings != NULL)
	{
		sjme_alloc_free(argStrings);
		argStrings = NULL;
	}
fail_countMainClassString:
fail_mainClassString:
fail_taskNewThread:
fail_mainExists:
	/* Release lock before failing. */
	sjme_thread_spinLockRelease(&inTask->object.common.lock, NULL);

	return sjme_error_default(error);
}

sjme_errorCode sjme_nvm_task_taskNew(
	sjme_attrInNotNull sjme_nvm inState,
	sjme_attrInNotNull const sjme_nvm_task_taskNewConfig* initConfig,
	sjme_attrOutNullable sjme_nvm_task* outTask)
{
	sjme_errorCode error;
	sjme_list(sjme_nvm_task)* tasks;
	sjme_list(sjme_nvm_thread)* threads;
	sjme_jint i, n, freeSlot;
	sjme_nvm_task result;
	sjme_nvm_vmClass_loader classLoader;
	sjme_nvm_taskStrings strings;
	const sjme_nvm_task_taskNewConfig* initConfigCopy;

	if (inState == NULL || initConfig == NULL || outTask == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (initConfig->mainClass == NULL || initConfig->classPath == NULL ||
		initConfig->classPath->length <= 0)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Debug. */
#if defined(SJME_CONFIG_DEBUG)
	sjme_message("Start Main: %s", initConfig->mainClass);

	if (initConfig->mainArgs != NULL)
		for (i = 0; i < initConfig->mainArgs->length; i++)
			sjme_message("Start Arg[%d]: %s",
				i, initConfig->mainArgs->elements[i]);

	if (initConfig->sysProps != NULL)
		for (i = 0; i < initConfig->sysProps->length; i++)
			sjme_message("Start SysProp[%d]: %s",
				i, initConfig->sysProps->elements[i]);
#endif
	
	/* Lock state for task access. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&inState->common.lock)))
		return sjme_error_default(error);

	/* Make a copy of the initialization config. */
	initConfigCopy = NULL;
	if (sjme_error_is(error = sjme_alloc_copy(inState->allocPool,
		sizeof(*initConfigCopy), (sjme_pointer*)&initConfigCopy,
		(sjme_pointer)initConfig)) ||
		initConfigCopy == NULL)
		goto fail_copyInitConfig;
	
	/* Find a free slot to claim for a new task. */
	freeSlot = -1;
	tasks = inState->tasks;
	if (tasks != NULL)
		for (i = 0, n = tasks->length; i < n; i++)
			if (tasks->elements[0] == NULL)
			{
				freeSlot = -1;
				break;
			}
	
	/* Need to allocate or grow the task list? */
	if (freeSlot < 0)
	{
		/* Either allocate or grow. */
		if (tasks == NULL)
		{
			/* Allocate. */
			if (sjme_error_is(error = sjme_list_alloc(inState->allocPool,
				SJME_NVM_TASK_GROW, &tasks, sjme_nvm_task, 0)) ||
				tasks == NULL)
				goto fail_allocTasks;
			
			/* Free slot is always at the start. */
			freeSlot = 0;
		}
		else
		{
			/* Copy everything over. */
			if (sjme_error_is(error = sjme_list_copy(inState->allocPool,
				tasks->length + SJME_NVM_TASK_GROW, tasks, &tasks,
				sjme_nvm_task, 0)))
				goto fail_allocTasks;
			
			/* Free slot is always at the end. */
			freeSlot = tasks->length;
			
			/* Destroy old list. */
			if (sjme_error_is(error = sjme_alloc_free(inState->tasks)))
				goto fail_allocTasks;
			inState->tasks = NULL;
		}
		
		/* Store for later usage. */
		inState->tasks = tasks;
	}
	
	/* Allocate new task to start. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState,
		sizeof(*result), SJME_NVM_STRUCT_TASK,
		SJME_AS_NVM_COMMONP(&result))) || result == NULL)
		goto fail_allocResult;

	/* Allocate strings. */
	strings = NULL;
	if (sjme_error_is(error = sjme_nvm_alloc(inState,
		sizeof(*result), SJME_NVM_STRUCT_TASK_STRINGS,
		SJME_AS_NVM_COMMONP(&strings))) || strings == NULL)
		goto fail_allocStrings;
	
	/* Initialize a new class loader for the current classpath, if one */
	/* has not been passed by the initial task configuration. */
	classLoader = NULL;
	if (initConfig->classLoader == NULL)
	{
		/* Set one up now. */
		if (sjme_error_is(error = sjme_nvm_vmClass_loaderNew(
			inState, &classLoader,
			initConfigCopy->classPath)) || classLoader == NULL)
			goto fail_initClassLoader;
	}

	/* Otherwise, use the specified classloader. */
	else
		classLoader = initConfig->classLoader;
	
	/* Refer to owning state and set identifier. */
	sjme_atomic_s(sjme_nvm, &result->inState, inState);
	result->classLoader = sjme_weakUpR(sjme_nvm_vmClass_loader, classLoader);
	result->id = 1 + sjme_atomic_ga(sjme_jint, 
		&inState->nextTaskId, 1);
	result->strings = sjme_weakUpR(sjme_nvm_taskStrings, strings);
	result->initConfig = initConfigCopy;

	/* Initialize identity hashcode generator. */
	if (sjme_error_is(error = sjme_random_init(&result->globals.idHash,
		INT32_C(0x43757465), INT32_C(0x53716B21))))
		goto fail_initIdHash;

	/* Use the default field accessor for this task by default. */
	result->globals.accessor[SJME_NVM_FIELD_ACCESS_NORMAL_STATIC] = 
		sjme_nvm_instance_fieldAccessorNS;
	result->globals.accessor[SJME_NVM_FIELD_ACCESS_VOLATILE_STATIC] = 
		sjme_nvm_instance_fieldAccessorVS;
	result->globals.accessor[SJME_NVM_FIELD_ACCESS_NORMAL_INSTANCE] = 
		sjme_nvm_instance_fieldAccessorNI;
	result->globals.accessor[SJME_NVM_FIELD_ACCESS_VOLATILE_INSTANCE] = 
		sjme_nvm_instance_fieldAccessorVI;

	/* Do not perform optimizations? */
	result->globals.noOptimize = initConfigCopy->noOptimize;
	
	/* All new tasks are considered alive. */
	result->status = SJME_NVM_TASK_STATUS_ALIVE;
	
	/* Setup thread storage. */
	if (sjme_error_is(error = sjme_list_alloc(inState->allocPool,
		SJME_NVM_THREAD_GROW, &threads, sjme_nvm_thread, 0)) ||
		threads == NULL)
		goto fail_allocThreads;
	result->threads = threads;
	
	/* Task is considered valid now, so store it in. */
	tasks->elements[freeSlot] = result;
	
	/* Lock state on the task. */
	if (sjme_error_is(error = sjme_thread_spinLockGrab(
		&result->object.common.lock)))
		goto fail_preLockBeforeRelease;
	
	/* Unlock state, we no longer need to keep the state locked since we */
	/* are now in the task list and others will really only care if we */
	/* are even alive or not. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&inState->common.lock, NULL)))
		goto fail_stateLockRelease;
	
	/* Add to the running task count. */
	sjme_atomic_ga(sjme_jint, &inState->numRunningTasks, 1);

	/* Not belaying main start? Then start the main thread. */
	if ((initConfigCopy->belay & SJME_NVM_BOOT_BELAY_MAIN) == 0)
		if (sjme_error_is(error = sjme_nvm_task_taskEnterMain(result, NULL)))
			goto fail_enterMain;

	/* Is this the main/first task? */
	if (sjme_atomic_pcs(&inState->phantomMainTask, NULL, result))
		result->isMain = SJME_JNI_TRUE;
	
	/* Release task specific lock. */
	if (sjme_error_is(error = sjme_thread_spinLockRelease(
		&result->object.common.lock, NULL)))
		return sjme_error_default(error);
	
	/* Success! */
	*outTask = result;
	return SJME_ERROR_NONE;
	
	/* In-state locks. */
fail_preLockBeforeRelease:
fail_allocThreads:
fail_initIdHash:
fail_initClassLoader:
fail_allocStrings:
	if (strings != NULL)
	{
		sjme_closeable_close(SJME_AS_CLOSEABLE(strings));
		strings = NULL;
	}
fail_allocResult:
	if (result != NULL)
	{
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
		result = NULL;
	}
fail_allocTasks:
fail_copyInitConfig:
	if (initConfigCopy != NULL)
		sjme_alloc_free((sjme_pointer)initConfigCopy);
	
	/* Unlock before fail. */
	sjme_error_is(sjme_thread_spinLockRelease(
		&inState->common.lock, NULL));
	
	return sjme_error_default(error);

	/* Post state lock, when accessing state is no longer needed. */
fail_countLoader:
fail_enterMain:
fail_stateLockRelease:
	/* Unlock task before fail. */
	sjme_error_is(sjme_thread_spinLockRelease(
		&result->object.common.lock, NULL));
	
fail_other:
	if (result != NULL)
	{
		sjme_closeable_close(SJME_AS_CLOSEABLE(result));
		result = NULL;
	}
	
	return sjme_error_default(error);
}
