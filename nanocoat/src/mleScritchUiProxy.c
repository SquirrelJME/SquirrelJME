/* -*- Mode: C; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// -------------------------------------------------------------------------*/

#include "sjme/nvm/instanceProxy.h"
#include "sjme/nvm/task.h"
#include "sjme/nvm/mle.h"
#include "sjme/nvm/mleShelves.h"
#include "sjme/nvm/instance.h"

#pragma region(mleInfo)
	#define SJME_NVM_MLE_SHELF ScritchUiProxy
#pragma endregion(mleInfo)

#pragma region(returnThis)

#define SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(name) \
	SJME_NVM_MLE_FUNCTION_DECL(name) \
	{ \
		/* Always map the same type, since this could do bad things. */ \
		argR->t = argV[0].t; \
		argR->v.l = argV[0].v.l; \
		return SJME_ERROR_NONE; \
	}

/** Define a self returning interface method. */
#define SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(name, type) \
	SJME_NVM_MLE_DEFINE(name, \
		/* Note the descriptor type differs from the actual type as this */ \
		/* is an instance method and not static. */ \
		SJME_MD(type, \
			SJME_MDMP___NO_ARGS__), \
		SJME_MP(SJME_MP_L, \
			SJME_MP_L)) \

SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(choice)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(component)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(container)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(environment)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(eventLoop)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(label)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(list)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(menu)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(paintable)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(panel)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(screen)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(scrollPanel)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(view)
SJME_NVM_MLE_SCRITCH_UI_RETURN_THIS(window)

#pragma endregion(returnThis)
#pragma region(general)

static sjme_errorCode sjme_nvm_mle_scritchUiNew(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNotNull sjme_scritchui scritchUi,
	sjme_attrInNotNull sjme_scritchui_uiCommon uiCommon,
	sjme_attrInValue sjme_scritchui_uiType subType,
	sjme_attrOutNotNull sjme_jbracketScritchUi* outBracket)
{
	sjme_errorCode error;
	sjme_jbracketScritchUi result;

	if (inFrame == NULL || scritchUi == NULL || uiCommon == NULL ||
		outBracket == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

	if (subType <= SJME_SCRITCHUI_TYPE_RESERVED ||
		subType >= SJME_SCRITCHUI_NUM_UI_TYPES)
		return SJME_ERROR_INVALID_ARGUMENT;

	/* Initialize the new bracket. */
	result = NULL;
	if (sjme_error_is(error = sjme_nvm_instance_objectNewBracket(
		SJME_F_T(inFrame), SJME_NVM_STRUCT_BRACKET_SCRITCH_UI,
		subType, SJME_AS_JOBJECTP(&result))) || result == NULL)
		return sjme_error_default(error);

	/* Fill in the bracket details. */
	result->type = subType;
	sjme_atomic_s(sjme_pointer, &result->ref,
		sjme_weakUpR(sjme_jbracketScritchUi, uiCommon));

	/* Success! */
	*outBracket = result;
	return SJME_ERROR_NONE;
}

#pragma endregion(general)
#pragma region(panel)

SJME_NVM_MLE_FUNCTION_DECL(panelNew)
{
	sjme_errorCode error;
	sjme_scritchui scritchUi;
	sjme_scritchui_uiPanel ui;
	sjme_jbracketScritchUi bracket;

	/* Recover ScritchUI instance. */
	scritchUi = sjme_atomic_g(sjme_pointer,
		&SJME_F_S(inFrame)->globals.scritchUi);
	if (scritchUi == NULL)
		return sjme_error_mask(SJME_ERROR_HEADLESS_DISPLAY,
			SJME_ERROR_MLE_CALL);

	/* Create new panel. */
	ui = NULL;
	if (sjme_error_is(error = scritchUi->api->panelNew(scritchUi,
		&ui)) || ui == NULL)
	{
		error = sjme_error_mask(error, SJME_ERROR_MLE_CALL);
		goto fail_scritchNew;
	}

	/* Create wrapper object. */
	bracket = NULL;
	if (sjme_error_is(sjme_nvm_mle_scritchUiNew(inFrame, scritchUi,
		SJME_SUI_CAST_COMMON(ui), SJME_SCRITCHUI_TYPE_PANEL,
		&bracket)))
	{
		error = sjme_error_mask(error, SJME_ERROR_MLE_CALL);
		goto fail_bracketNew;
	}

	/* Return the created object. */
	argR->t = SJME_JAVA_TYPE_ID_OBJECT;
	argR->v.l = (sjme_jobject)bracket;
	return SJME_ERROR_NONE;

fail_bracketNew:
	if (bracket != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(bracket));
fail_scritchNew:
	if (ui != NULL)
		sjme_closeable_close(SJME_AS_CLOSEABLE(ui));
	return sjme_error_default(error);
}

#pragma endregion(panel)
#pragma region(proxyHandler)

SJME_NVM_MLE_SHELF_DECLARE(ScritchUiProxy) =
{
	/* Self returning interfaces. */
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(choice,
		SJME_MD_SCRITCH_UI_INTERFACE(Choice)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(component,
		SJME_MD_SCRITCH_UI_INTERFACE(Component)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(container,
		SJME_MD_SCRITCH_UI_INTERFACE(Container)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(environment,
		SJME_MD_SCRITCH_UI_INTERFACE(Environment)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(eventLoop,
		SJME_MD_SCRITCH_UI_INTERFACE(EventLoop)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(label,
		SJME_MD_SCRITCH_UI_INTERFACE(Label)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(list,
		SJME_MD_SCRITCH_UI_INTERFACE(List)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(menu,
		SJME_MD_SCRITCH_UI_INTERFACE(Menu)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(paintable,
		SJME_MD_SCRITCH_UI_INTERFACE(Paintable)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(panel,
		SJME_MD_SCRITCH_UI_INTERFACE(Panel)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(screen,
		SJME_MD_SCRITCH_UI_INTERFACE(Screen)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(scrollPanel,
		SJME_MD_SCRITCH_UI_INTERFACE(ScrollPanel)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(view,
		SJME_MD_SCRITCH_UI_INTERFACE(View)),
	SJME_NVM_MLE_SCRITCH_UI_DEFINE_THIS(window,
		SJME_MD_SCRITCH_UI_INTERFACE(Window)),

	/* Panel interface. */
	SJME_NVM_MLE_DEFINE(panelNew,
		SJME_MD(SJME_MD_SCRITCH_UI_BRACKET(Panel),
			SJME_MDMP___NO_ARGS__),
		SJME_MP(SJME_MP_L,
			SJME_MP_L)),

	SJME_NVM_MLE_STOP()
};

sjme_errorCode sjme_nvm_mle_scritchUiProxyHandler(
	sjme_attrInNotNull sjme_nvm_frame inFrame,
	sjme_attrInNullable sjme_nvm_frame_gcCommit* commit,
	sjme_attrInNotNull sjme_jobject proxyInstance,
	sjme_attrInNotNull sjme_jmethodID proxyMethod,
	sjme_attrInNotNull sjme_jvalueTyped* argR,
	sjme_attrInNotNull sjme_jint argC,
	sjme_attrInNotNull sjme_jvalueTyped* argV)
{
	sjme_errorCode error;

	if (inFrame == NULL || proxyInstance == NULL || proxyMethod == NULL)
		return SJME_ERROR_NULL_ARGUMENTS;

#if defined(SJME_CONFIG_DEBUG)
	/* Debug. */
	sjme_message("ScritchUI MLE: %s.%s()",
		sjme_charSeq_tempUtf(proxyMethod->member.name->seq),
		sjme_charSeq_tempUtf(proxyMethod->member.type->seq));
#endif

	/* If ScritchUI is not initialized, then this always fails. */
	if (sjme_atomic_g(sjme_pointer,
		&SJME_F_S(inFrame)->globals.scritchUi) == NULL)
		return sjme_error_mask(SJME_ERROR_HEADLESS_DISPLAY,
			SJME_ERROR_MLE_CALL);

	/* Forward to minor shelf handling, this already exists and thus we do */
	/* not need to duplicate this functionality. */
	if (sjme_error_is(error = sjme_mle_mleCallShelfM(inFrame,
		&SJME_NVM_MLE_SHELF_NAME(ScritchUiProxy)[0],
		proxyMethod->member.name->seq,
		proxyMethod->member.type->seq,
		argR, argC, argV)))
		return sjme_error_vmError(inFrame, error);

	/* Success! */
	return SJME_ERROR_NONE;
}

#pragma endregion(proxyHandler)
