// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.khronos.opengles;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Api
@SuppressWarnings("FieldNamingConvention")
public interface GL11
	extends GL10
{
	@Api
	int GL_ACTIVE_TEXTURE =
		34016;
	
	@Api
	int GL_ADD_SIGNED =
		34164;
	
	@Api
	int GL_ALPHA_SCALE =
		3356;
	
	@Api
	int GL_ALPHA_TEST_FUNC =
		3009;
	
	@Api
	int GL_ALPHA_TEST_REF =
		3010;
	
	@Api
	int GL_ARRAY_BUFFER =
		34962;
	
	@Api
	int GL_ARRAY_BUFFER_BINDING =
		34964;
	
	@Api
	int GL_BLEND_DST =
		3040;
	
	@Api
	int GL_BLEND_SRC =
		3041;
	
	@Api
	int GL_BUFFER_ACCESS =
		35003;
	
	@Api
	int GL_BUFFER_SIZE =
		34660;
	
	@Api
	int GL_BUFFER_USAGE =
		34661;
	
	@Api
	int GL_CLIENT_ACTIVE_TEXTURE =
		34017;
	
	@Api
	int GL_CLIP_PLANE0 =
		12288;
	
	@Api
	int GL_CLIP_PLANE1 =
		12289;
	
	@Api
	int GL_CLIP_PLANE2 =
		12290;
	
	@Api
	int GL_CLIP_PLANE3 =
		12291;
	
	@Api
	int GL_CLIP_PLANE4 =
		12292;
	
	@Api
	int GL_CLIP_PLANE5 =
		12293;
	
	@Api
	int GL_COLOR_ARRAY_BUFFER_BINDING =
		34968;
	
	@Api
	int GL_COLOR_ARRAY_POINTER =
		32912;
	
	@Api
	int GL_COLOR_ARRAY_SIZE =
		32897;
	
	@Api
	int GL_COLOR_ARRAY_STRIDE =
		32899;
	
	@Api
	int GL_COLOR_ARRAY_TYPE =
		32898;
	
	@Api
	int GL_COLOR_CLEAR_VALUE =
		3106;
	
	@Api
	int GL_COLOR_WRITEMASK =
		3107;
	
	@Api
	int GL_COMBINE =
		34160;
	
	@Api
	int GL_COMBINE_ALPHA =
		34162;
	
	@Api
	int GL_COMBINE_RGB =
		34161;
	
	@Api
	int GL_CONSTANT =
		34166;
	
	@Api
	int GL_COORD_REPLACE_OES =
		34914;
	
	@Api
	int GL_CULL_FACE_MODE =
		2885;
	
	@Api
	int GL_CURRENT_COLOR =
		2816;
	
	@Api
	int GL_CURRENT_NORMAL =
		2818;
	
	@Api
	int GL_CURRENT_TEXTURE_COORDS =
		2819;
	
	@Api
	int GL_DEPTH_CLEAR_VALUE =
		2931;
	
	@Api
	int GL_DEPTH_FUNC =
		2932;
	
	@Api
	int GL_DEPTH_RANGE =
		2928;
	
	@Api
	int GL_DEPTH_WRITEMASK =
		2930;
	
	@Api
	int GL_DOT3_RGB =
		34478;
	
	@Api
	int GL_DOT3_RGBA =
		34479;
	
	@Api
	int GL_DYNAMIC_DRAW =
		35048;
	
	@Api
	int GL_ELEMENT_ARRAY_BUFFER =
		34963;
	
	@Api
	int GL_ELEMENT_ARRAY_BUFFER_BINDING =
		34965;
	
	@Api
	int GL_FRONT_FACE =
		2886;
	
	@Api
	int GL_GENERATE_MIPMAP =
		33169;
	
	@Api
	int GL_GENERATE_MIPMAP_HINT =
		33170;
	
	@Api
	int GL_INTERPOLATE =
		34165;
	
	@Api
	int GL_LINE_WIDTH =
		2849;
	
	@Api
	int GL_LOGIC_OP_MODE =
		3056;
	
	@Api
	int GL_MATRIX_MODE =
		2976;
	
	@Api
	int GL_MAX_CLIP_PLANES =
		3378;
	
	@Api
	int GL_MODELVIEW_MATRIX =
		2982;
	
	@Api
	int GL_MODELVIEW_MATRIX_FLOAT_AS_INT_BITS_OES =
		35213;
	
	@Api
	int GL_MODELVIEW_STACK_DEPTH =
		2979;
	
	@Api
	int GL_NORMAL_ARRAY_BUFFER_BINDING =
		34967;
	
	@Api
	int GL_NORMAL_ARRAY_POINTER =
		32911;
	
	@Api
	int GL_NORMAL_ARRAY_STRIDE =
		32895;
	
	@Api
	int GL_NORMAL_ARRAY_TYPE =
		32894;
	
	@Api
	int GL_OPERAND0_ALPHA =
		34200;
	
	@Api
	int GL_OPERAND0_RGB =
		34192;
	
	@Api
	int GL_OPERAND1_ALPHA =
		34201;
	
	@Api
	int GL_OPERAND1_RGB =
		34193;
	
	@Api
	int GL_OPERAND2_ALPHA =
		34202;
	
	@Api
	int GL_OPERAND2_RGB =
		34194;
	
	@Api
	int GL_POINT_DISTANCE_ATTENUATION =
		33065;
	
	@Api
	int GL_POINT_FADE_THRESHOLD_SIZE =
		33064;
	
	@Api
	int GL_POINT_SIZE =
		2833;
	
	@Api
	int GL_POINT_SIZE_ARRAY_BUFFER_BINDING_OES =
		35743;
	
	@Api
	int GL_POINT_SIZE_ARRAY_OES =
		35740;
	
	@Api
	int GL_POINT_SIZE_ARRAY_POINTER_OES =
		35212;
	
	@Api
	int GL_POINT_SIZE_ARRAY_STRIDE_OES =
		35211;
	
	@Api
	int GL_POINT_SIZE_ARRAY_TYPE_OES =
		35210;
	
	@Api
	int GL_POINT_SIZE_MAX =
		33063;
	
	@Api
	int GL_POINT_SIZE_MIN =
		33062;
	
	@Api
	int GL_POINT_SPRITE_OES =
		34913;
	
	@Api
	int GL_POLYGON_OFFSET_FACTOR =
		32824;
	
	@Api
	int GL_POLYGON_OFFSET_UNITS =
		10752;
	
	@Api
	int GL_PREVIOUS =
		34168;
	
	@Api
	int GL_PRIMARY_COLOR =
		34167;
	
	@Api
	int GL_PROJECTION_MATRIX =
		2983;
	
	@Api
	int GL_PROJECTION_MATRIX_FLOAT_AS_INT_BITS_OES =
		35214;
	
	@Api
	int GL_PROJECTION_STACK_DEPTH =
		2980;
	
	@Api
	int GL_RGB_SCALE =
		34163;
	
	@Api
	int GL_SAMPLES =
		32937;
	
	@Api
	int GL_SAMPLE_BUFFERS =
		32936;
	
	@Api
	int GL_SAMPLE_COVERAGE_INVERT =
		32939;
	
	@Api
	int GL_SAMPLE_COVERAGE_VALUE =
		32938;
	
	@Api
	int GL_SCISSOR_BOX =
		3088;
	
	@Api
	int GL_SHADE_MODEL =
		2900;
	
	@Api
	int GL_SRC0_ALPHA =
		34184;
	
	@Api
	int GL_SRC0_RGB =
		34176;
	
	@Api
	int GL_SRC1_ALPHA =
		34185;
	
	@Api
	int GL_SRC1_RGB =
		34177;
	
	@Api
	int GL_SRC2_ALPHA =
		34186;
	
	@Api
	int GL_SRC2_RGB =
		34178;
	
	@Api
	int GL_STATIC_DRAW =
		35044;
	
	@Api
	int GL_STENCIL_CLEAR_VALUE =
		2961;
	
	@Api
	int GL_STENCIL_FAIL =
		2964;
	
	@Api
	int GL_STENCIL_FUNC =
		2962;
	
	@Api
	int GL_STENCIL_PASS_DEPTH_FAIL =
		2965;
	
	@Api
	int GL_STENCIL_PASS_DEPTH_PASS =
		2966;
	
	@Api
	int GL_STENCIL_REF =
		2967;
	
	@Api
	int GL_STENCIL_VALUE_MASK =
		2963;
	
	@Api
	int GL_STENCIL_WRITEMASK =
		2968;
	
	@Api
	int GL_SUBTRACT =
		34023;
	
	@Api
	int GL_TEXTURE_BINDING_2D =
		32873;
	
	@Api
	int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING =
		34970;
	
	@Api
	int GL_TEXTURE_COORD_ARRAY_POINTER =
		32914;
	
	@Api
	int GL_TEXTURE_COORD_ARRAY_SIZE =
		32904;
	
	@Api
	int GL_TEXTURE_COORD_ARRAY_STRIDE =
		32906;
	
	@Api
	int GL_TEXTURE_COORD_ARRAY_TYPE =
		32905;
	
	@Api
	int GL_TEXTURE_MATRIX =
		2984;
	
	@Api
	int GL_TEXTURE_MATRIX_FLOAT_AS_INT_BITS_OES =
		35215;
	
	@Api
	int GL_TEXTURE_STACK_DEPTH =
		2981;
	
	@Api
	int GL_VERTEX_ARRAY_BUFFER_BINDING =
		34966;
	
	@Api
	int GL_VERTEX_ARRAY_POINTER =
		32910;
	
	@Api
	int GL_VERTEX_ARRAY_SIZE =
		32890;
	
	@Api
	int GL_VERTEX_ARRAY_STRIDE =
		32892;
	
	@Api
	int GL_VERTEX_ARRAY_TYPE =
		32891;
	
	@Api
	int GL_VIEWPORT =
		2978;
	
	@Api
	int GL_WRITE_ONLY =
		35001;
	
	@Api
	void glBindBuffer(int __a, int __b);
	
	@Api
	void glBufferData(int __a, int __b, Buffer __c, int __d);
	
	@Api
	void glBufferSubData(int __a, int __b, int __c, Buffer __d);
	
	@Api
	void glClipPlanef(int __a, float[] __b, int __c);
	
	@Api
	void glClipPlanef(int __a, FloatBuffer __b);
	
	@Api
	void glClipPlanex(int __a, int[] __b, int __c);
	
	@Api
	void glClipPlanex(int __a, IntBuffer __b);
	
	@Api
	void glColor4ub(byte __a, byte __b, byte __c, byte __d);
	
	@Api
	void glColorPointer(int __a, int __b, int __c, int __d);
	
	@Api
	void glDeleteBuffers(int __a, int[] __b, int __c);
	
	@Api
	void glDeleteBuffers(int __a, IntBuffer __b);
	
	@Api
	void glDrawElements(int __a, int __b, int __c, int __d);
	
	@Api
	void glGenBuffers(int __a, int[] __b, int __c);
	
	@Api
	void glGenBuffers(int __a, IntBuffer __b);
	
	@Api
	void glGetBooleanv(int __a, boolean[] __b, int __c);
	
	@Api
	void glGetBooleanv(int __a, IntBuffer __b);
	
	@Api
	void glGetBufferParameteriv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glGetBufferParameteriv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glGetClipPlanef(int __a, float[] __b, int __c);
	
	@Api
	void glGetClipPlanef(int __a, FloatBuffer __b);
	
	@Api
	void glGetClipPlanex(int __a, int[] __b, int __c);
	
	@Api
	void glGetClipPlanex(int __a, IntBuffer __b);
	
	@Api
	void glGetFixedv(int __a, int[] __b, int __c);
	
	@Api
	void glGetFixedv(int __a, IntBuffer __b);
	
	@Api
	void glGetFloatv(int __a, float[] __b, int __c);
	
	@Api
	void glGetFloatv(int __a, FloatBuffer __b);
	
	@Api
	void glGetLightfv(int __a, int __b, float[] __c, int __d);
	
	@Api
	void glGetLightfv(int __a, int __b, FloatBuffer __c);
	
	@Api
	void glGetLightxv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glGetLightxv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glGetMaterialfv(int __a, int __b, float[] __c, int __d);
	
	@Api
	void glGetMaterialfv(int __a, int __b, FloatBuffer __c);
	
	@Api
	void glGetMaterialxv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glGetMaterialxv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glGetPointerv(int __a, Buffer[] __b);
	
	@Api
	void glGetTexEnvfv(int __a, int __b, float[] __c, int __d);
	
	@Api
	void glGetTexEnvfv(int __a, int __b, FloatBuffer __c);
	
	@Api
	void glGetTexEnviv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glGetTexEnviv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glGetTexEnvxv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glGetTexEnvxv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glGetTexParameterfv(int __a, int __b, float[] __c, int __d);
	
	@Api
	void glGetTexParameterfv(int __a, int __b, FloatBuffer __c);
	
	@Api
	void glGetTexParameteriv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glGetTexParameteriv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glGetTexParameterxv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glGetTexParameterxv(int __a, int __b, IntBuffer __c);
	
	@Api
	boolean glIsBuffer(int __a);
	
	@Api
	boolean glIsEnabled(int __a);
	
	@Api
	boolean glIsTexture(int __a);
	
	@Api
	void glNormalPointer(int __a, int __b, int __c);
	
	@Api
	void glPointParameterf(int __a, float __b);
	
	@Api
	void glPointParameterfv(int __a, float[] __b, int __c);
	
	@Api
	void glPointParameterfv(int __a, FloatBuffer __b);
	
	@Api
	void glPointParameterx(int __a, int __b);
	
	@Api
	void glPointParameterxv(int __a, int[] __b, int __c);
	
	@Api
	void glPointParameterxv(int __a, IntBuffer __b);
	
	@Api
	void glPointSizePointerOES(int __a, int __b, int __c);
	
	@Api
	void glPointSizePointerOES(int __a, int __b, Buffer __c);
	
	@Api
	void glTexCoordPointer(int __a, int __b, int __c, int __d);
	
	@Api
	void glTexEnvi(int __a, int __b, int __c);
	
	@Api
	void glTexEnviv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glTexEnviv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glTexParameterfv(int __a, int __b, float[] __c, int __d);
	
	@Api
	void glTexParameterfv(int __a, int __b, FloatBuffer __c);
	
	@Api
	void glTexParameteri(int __a, int __b, int __c);
	
	@Api
	void glTexParameteriv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glTexParameteriv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glTexParameterxv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glTexParameterxv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glVertexPointer(int __a, int __b, int __c, int __d);
}


