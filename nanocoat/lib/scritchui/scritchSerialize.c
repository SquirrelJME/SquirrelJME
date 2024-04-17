/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "lib/scritchui/core/core.h"
#include "lib/scritchui/core/coreSerial.h"
#include "lib/scritchui/scritchuiTypes.h"
#include "sjme/alloc.h"
#include "sjme/debug.h"

/** Serial variables. */
#define SJME_SCRITCHUI_SERIAL_VARS \
	sjme_errorCode error; \
	sjme_jboolean direct

/** Pre-check call to make. */
#define SJME_SCRITCHUI_SERIAL_PRE_CHECK \
	do { if (inState == NULL) \
    { \
		return SJME_ERROR_NULL_ARGUMENTS; \
	} } while(0)

/** Check for being in the loop. */
#define SJME_SCRITCHUI_SERIAL_LOOP_CHECK(what) \
	do { \
		error = SJME_ERROR_NOT_IMPLEMENTED; \
		direct = SJME_JNI_FALSE; \
		 \
		if (inState->api->loopIsInThread == NULL || \
            inState->api->what == NULL || \
            inState->apiInThread->what == NULL || \
			sjme_error_is(error = inState->api->loopIsInThread(inState, \
				&direct))) \
			return sjme_error_default(error); \
	} while (0)

sjme_errorCode sjme_scritchui_coreSerial_componentSetPaintListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiComponent inComponent,
	sjme_attrInNullable sjme_scritchui_paintListenerFunc inListener,
	sjme_frontEnd* copyFrontEnd)
{
	SJME_SCRITCHUI_SERIAL_VARS;
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(componentSetPaintListener);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->componentSetPaintListener(inState,
			inComponent, inListener, copyFrontEnd);
	
	sjme_todo("Impl");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_coreSerial_panelEnableFocus(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_uiPanel inPanel,
	sjme_attrInValue sjme_jboolean enableFocus)
{
	SJME_SCRITCHUI_SERIAL_VARS;
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(panelEnableFocus);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->panelEnableFocus(inState,
			inPanel, enableFocus);
	
	sjme_todo("Impl");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_coreSerial_panelNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiPanel* outPanel)
{
	SJME_SCRITCHUI_SERIAL_VARS;
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(panelNew);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->panelNew(inState,
			outPanel);
	
	sjme_todo("Impl");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_coreSerial_screenSetListener(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInNotNull sjme_scritchui_screenListenerFunc callback)
{
	SJME_SCRITCHUI_SERIAL_VARS;
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(screenSetListener);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->screenSetListener(inState,
			callback);
	
	sjme_todo("Impl");
	return SJME_ERROR_NOT_IMPLEMENTED;
}

sjme_errorCode sjme_scritchui_coreSerial_screens(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrOutNotNull sjme_scritchui_uiScreen* outScreens,
	sjme_attrInOutNotNull sjme_jint* inOutNumScreens)
{
	SJME_SCRITCHUI_SERIAL_VARS;
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(screens);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->screens(inState,
			outScreens, inOutNumScreens);
	
	sjme_todo("Impl");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
	
sjme_errorCode sjme_scritchui_coreSerial_windowNew(
	sjme_attrInNotNull sjme_scritchui inState,
	sjme_attrInOutNotNull sjme_scritchui_uiWindow* outWindow)
{
	SJME_SCRITCHUI_SERIAL_VARS;
	
	SJME_SCRITCHUI_SERIAL_PRE_CHECK;
	SJME_SCRITCHUI_SERIAL_LOOP_CHECK(windowNew);
	
	/* Direct call? */
	if (direct)
		return inState->apiInThread->windowNew(inState,
			outWindow);
	
	sjme_todo("Impl");
	return SJME_ERROR_NOT_IMPLEMENTED;
}
