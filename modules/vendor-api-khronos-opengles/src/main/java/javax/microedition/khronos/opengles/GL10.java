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
@SuppressWarnings({"FieldNamingConvention", "InterfaceWithOnlyOneDirectInheritor"})
public interface GL10
	extends GL
{
	@Api
	int GL_ADD =
		260;
	
	@Api
	int GL_ALIASED_LINE_WIDTH_RANGE =
		33902;
	
	@Api
	int GL_ALIASED_POINT_SIZE_RANGE =
		33901;
	
	@Api
	int GL_ALPHA =
		6406;
	
	@Api
	int GL_ALPHA_BITS =
		3413;
	
	@Api
	int GL_ALPHA_TEST =
		3008;
	
	@Api
	int GL_ALWAYS =
		519;
	
	@Api
	int GL_AMBIENT =
		4608;
	
	@Api
	int GL_AMBIENT_AND_DIFFUSE =
		5634;
	
	@Api
	int GL_AND =
		5377;
	
	@Api
	int GL_AND_INVERTED =
		5380;
	
	@Api
	int GL_AND_REVERSE =
		5378;
	
	@Api
	int GL_BACK =
		1029;
	
	@Api
	int GL_BLEND =
		3042;
	
	@Api
	int GL_BLUE_BITS =
		3412;
	
	@Api
	int GL_BYTE =
		5120;
	
	@Api
	int GL_CCW =
		2305;
	
	@Api
	int GL_CLAMP_TO_EDGE =
		33071;
	
	@Api
	int GL_CLEAR =
		5376;
	
	@Api
	int GL_COLOR_ARRAY =
		32886;
	
	@Api
	int GL_COLOR_BUFFER_BIT =
		16384;
	
	@Api
	int GL_COLOR_LOGIC_OP =
		3058;
	
	@Api
	int GL_COLOR_MATERIAL =
		2903;
	
	@Api
	int GL_COMPRESSED_TEXTURE_FORMATS =
		34467;
	
	@Api
	int GL_CONSTANT_ATTENUATION =
		4615;
	
	@Api
	int GL_COPY =
		5379;
	
	@Api
	int GL_COPY_INVERTED =
		5388;
	
	@Api
	int GL_CULL_FACE =
		2884;
	
	@Api
	int GL_CW =
		2304;
	
	@Api
	int GL_DECAL =
		8449;
	
	@Api
	int GL_DECR =
		7683;
	
	@Api
	int GL_DEPTH_BITS =
		3414;
	
	@Api
	int GL_DEPTH_BUFFER_BIT =
		256;
	
	@Api
	int GL_DEPTH_TEST =
		2929;
	
	@Api
	int GL_DIFFUSE =
		4609;
	
	@Api
	int GL_DITHER =
		3024;
	
	@Api
	int GL_DONT_CARE =
		4352;
	
	@Api
	int GL_DST_ALPHA =
		772;
	
	@Api
	int GL_DST_COLOR =
		774;
	
	@Api
	int GL_EMISSION =
		5632;
	
	@Api
	int GL_EQUAL =
		514;
	
	@Api
	int GL_EQUIV =
		5385;
	
	@Api
	int GL_EXP =
		2048;
	
	@Api
	int GL_EXP2 =
		2049;
	
	@Api
	int GL_EXTENSIONS =
		7939;
	
	@Api
	int GL_FALSE =
		0;
	
	@Api
	int GL_FASTEST =
		4353;
	
	@Api
	int GL_FIXED =
		5132;
	
	@Api
	int GL_FLAT =
		7424;
	
	@Api
	int GL_FLOAT =
		5126;
	
	@Api
	int GL_FOG =
		2912;
	
	@Api
	int GL_FOG_COLOR =
		2918;
	
	@Api
	int GL_FOG_DENSITY =
		2914;
	
	@Api
	int GL_FOG_END =
		2916;
	
	@Api
	int GL_FOG_HINT =
		3156;
	
	@Api
	int GL_FOG_MODE =
		2917;
	
	@Api
	int GL_FOG_START =
		2915;
	
	@Api
	int GL_FRONT =
		1028;
	
	@Api
	int GL_FRONT_AND_BACK =
		1032;
	
	@Api
	int GL_GEQUAL =
		518;
	
	@Api
	int GL_GREATER =
		516;
	
	@Api
	int GL_GREEN_BITS =
		3411;
	
	@Api
	int GL_IMPLEMENTATION_COLOR_READ_FORMAT_OES =
		35739;
	
	@Api
	int GL_IMPLEMENTATION_COLOR_READ_TYPE_OES =
		35738;
	
	@Api
	int GL_INCR =
		7682;
	
	@Api
	int GL_INVALID_ENUM =
		1280;
	
	@Api
	int GL_INVALID_OPERATION =
		1282;
	
	@Api
	int GL_INVALID_VALUE =
		1281;
	
	@Api
	int GL_INVERT =
		5386;
	
	@Api
	int GL_KEEP =
		7680;
	
	@Api
	int GL_LEQUAL =
		515;
	
	@Api
	int GL_LESS =
		513;
	
	@Api
	int GL_LIGHT0 =
		16384;
	
	@Api
	int GL_LIGHT1 =
		16385;
	
	@Api
	int GL_LIGHT2 =
		16386;
	
	@Api
	int GL_LIGHT3 =
		16387;
	
	@Api
	int GL_LIGHT4 =
		16388;
	
	@Api
	int GL_LIGHT5 =
		16389;
	
	@Api
	int GL_LIGHT6 =
		16390;
	
	@Api
	int GL_LIGHT7 =
		16391;
	
	@Api
	int GL_LIGHTING =
		2896;
	
	@Api
	int GL_LIGHT_MODEL_AMBIENT =
		2899;
	
	@Api
	int GL_LIGHT_MODEL_TWO_SIDE =
		2898;
	
	@Api
	int GL_LINEAR =
		9729;
	
	@Api
	int GL_LINEAR_ATTENUATION =
		4616;
	
	@Api
	int GL_LINEAR_MIPMAP_LINEAR =
		9987;
	
	@Api
	int GL_LINEAR_MIPMAP_NEAREST =
		9985;
	
	@Api
	int GL_LINES =
		1;
	
	@Api
	int GL_LINE_LOOP =
		2;
	
	@Api
	int GL_LINE_SMOOTH =
		2848;
	
	@Api
	int GL_LINE_SMOOTH_HINT =
		3154;
	
	@Api
	int GL_LINE_STRIP =
		3;
	
	@Api
	int GL_LUMINANCE =
		6409;
	
	@Api
	int GL_LUMINANCE_ALPHA =
		6410;
	
	@Api
	int GL_MAX_ELEMENTS_INDICES =
		33001;
	
	@Api
	int GL_MAX_ELEMENTS_VERTICES =
		33000;
	
	@Api
	int GL_MAX_LIGHTS =
		3377;
	
	@Api
	int GL_MAX_MODELVIEW_STACK_DEPTH =
		3382;
	
	@Api
	int GL_MAX_PROJECTION_STACK_DEPTH =
		3384;
	
	@Api
	int GL_MAX_TEXTURE_SIZE =
		3379;
	
	@Api
	int GL_MAX_TEXTURE_STACK_DEPTH =
		3385;
	
	@Api
	int GL_MAX_TEXTURE_UNITS =
		34018;
	
	@Api
	int GL_MAX_VIEWPORT_DIMS =
		3386;
	
	@Api
	int GL_MODELVIEW =
		5888;
	
	@Api
	int GL_MODULATE =
		8448;
	
	@Api
	int GL_MULTISAMPLE =
		32925;
	
	@Api
	int GL_NAND =
		5390;
	
	@Api
	int GL_NEAREST =
		9728;
	
	@Api
	int GL_NEAREST_MIPMAP_LINEAR =
		9986;
	
	@Api
	int GL_NEAREST_MIPMAP_NEAREST =
		9984;
	
	@Api
	int GL_NEVER =
		512;
	
	@Api
	int GL_NICEST =
		4354;
	
	@Api
	int GL_NOOP =
		5381;
	
	@Api
	int GL_NOR =
		5384;
	
	@Api
	int GL_NORMALIZE =
		2977;
	
	@Api
	int GL_NORMAL_ARRAY =
		32885;
	
	@Api
	int GL_NOTEQUAL =
		517;
	
	@Api
	int GL_NO_ERROR =
		0;
	
	@Api
	int GL_NUM_COMPRESSED_TEXTURE_FORMATS =
		34466;
	
	@Api
	int GL_ONE =
		1;
	
	@Api
	int GL_ONE_MINUS_DST_ALPHA =
		773;
	
	@Api
	int GL_ONE_MINUS_DST_COLOR =
		775;
	
	@Api
	int GL_ONE_MINUS_SRC_ALPHA =
		771;
	
	@Api
	int GL_ONE_MINUS_SRC_COLOR =
		769;
	
	@Api
	int GL_OR =
		5383;
	
	@Api
	int GL_OR_INVERTED =
		5389;
	
	@Api
	int GL_OR_REVERSE =
		5387;
	
	@Api
	int GL_OUT_OF_MEMORY =
		1285;
	
	@Api
	int GL_PACK_ALIGNMENT =
		3333;
	
	@Api
	int GL_PALETTE4_R5_G6_B5_OES =
		35730;
	
	@Api
	int GL_PALETTE4_RGB5_A1_OES =
		35732;
	
	@Api
	int GL_PALETTE4_RGB8_OES =
		35728;
	
	@Api
	int GL_PALETTE4_RGBA4_OES =
		35731;
	
	@Api
	int GL_PALETTE4_RGBA8_OES =
		35729;
	
	@Api
	int GL_PALETTE8_R5_G6_B5_OES =
		35735;
	
	@Api
	int GL_PALETTE8_RGB5_A1_OES =
		35737;
	
	@Api
	int GL_PALETTE8_RGB8_OES =
		35733;
	
	@Api
	int GL_PALETTE8_RGBA4_OES =
		35736;
	
	@Api
	int GL_PALETTE8_RGBA8_OES =
		35734;
	
	@Api
	int GL_PERSPECTIVE_CORRECTION_HINT =
		3152;
	
	@Api
	int GL_POINTS =
		0;
	
	@Api
	int GL_POINT_SMOOTH =
		2832;
	
	@Api
	int GL_POINT_SMOOTH_HINT =
		3153;
	
	@Api
	int GL_POLYGON_OFFSET_FILL =
		32823;
	
	@Api
	int GL_POLYGON_SMOOTH_HINT =
		3155;
	
	@Api
	int GL_POSITION =
		4611;
	
	@Api
	int GL_PROJECTION =
		5889;
	
	@Api
	int GL_QUADRATIC_ATTENUATION =
		4617;
	
	@Api
	int GL_RED_BITS =
		3410;
	
	@Api
	int GL_RENDERER =
		7937;
	
	@Api
	int GL_REPEAT =
		10497;
	
	@Api
	int GL_REPLACE =
		7681;
	
	@Api
	int GL_RESCALE_NORMAL =
		32826;
	
	@Api
	int GL_RGB =
		6407;
	
	@Api
	int GL_RGBA =
		6408;
	
	@Api
	int GL_SAMPLE_ALPHA_TO_COVERAGE =
		32926;
	
	@Api
	int GL_SAMPLE_ALPHA_TO_ONE =
		32927;
	
	@Api
	int GL_SAMPLE_COVERAGE =
		32928;
	
	@Api
	int GL_SCISSOR_TEST =
		3089;
	
	@Api
	int GL_SET =
		5391;
	
	@Api
	int GL_SHININESS =
		5633;
	
	@Api
	int GL_SHORT =
		5122;
	
	@Api
	int GL_SMOOTH =
		7425;
	
	@Api
	int GL_SMOOTH_LINE_WIDTH_RANGE =
		2850;
	
	@Api
	int GL_SMOOTH_POINT_SIZE_RANGE =
		2834;
	
	@Api
	int GL_SPECULAR =
		4610;
	
	@Api
	int GL_SPOT_CUTOFF =
		4614;
	
	@Api
	int GL_SPOT_DIRECTION =
		4612;
	
	@Api
	int GL_SPOT_EXPONENT =
		4613;
	
	@Api
	int GL_SRC_ALPHA =
		770;
	
	@Api
	int GL_SRC_ALPHA_SATURATE =
		776;
	
	@Api
	int GL_SRC_COLOR =
		768;
	
	@Api
	int GL_STACK_OVERFLOW =
		1283;
	
	@Api
	int GL_STACK_UNDERFLOW =
		1284;
	
	@Api
	int GL_STENCIL_BITS =
		3415;
	
	@Api
	int GL_STENCIL_BUFFER_BIT =
		1024;
	
	@Api
	int GL_STENCIL_TEST =
		2960;
	
	@Api
	int GL_SUBPIXEL_BITS =
		3408;
	
	@Api
	int GL_TEXTURE =
		5890;
	
	@Api
	int GL_TEXTURE0 =
		33984;
	
	@Api
	int GL_TEXTURE1 =
		33985;
	
	@Api
	int GL_TEXTURE10 =
		33994;
	
	@Api
	int GL_TEXTURE11 =
		33995;
	
	@Api
	int GL_TEXTURE12 =
		33996;
	
	@Api
	int GL_TEXTURE13 =
		33997;
	
	@Api
	int GL_TEXTURE14 =
		33998;
	
	@Api
	int GL_TEXTURE15 =
		33999;
	
	@Api
	int GL_TEXTURE16 =
		34000;
	
	@Api
	int GL_TEXTURE17 =
		34001;
	
	@Api
	int GL_TEXTURE18 =
		34002;
	
	@Api
	int GL_TEXTURE19 =
		34003;
	
	@Api
	int GL_TEXTURE2 =
		33986;
	
	@Api
	int GL_TEXTURE20 =
		34004;
	
	@Api
	int GL_TEXTURE21 =
		34005;
	
	@Api
	int GL_TEXTURE22 =
		34006;
	
	@Api
	int GL_TEXTURE23 =
		34007;
	
	@Api
	int GL_TEXTURE24 =
		34008;
	
	@Api
	int GL_TEXTURE25 =
		34009;
	
	@Api
	int GL_TEXTURE26 =
		34010;
	
	@Api
	int GL_TEXTURE27 =
		34011;
	
	@Api
	int GL_TEXTURE28 =
		34012;
	
	@Api
	int GL_TEXTURE29 =
		34013;
	
	@Api
	int GL_TEXTURE3 =
		33987;
	
	@Api
	int GL_TEXTURE30 =
		34014;
	
	@Api
	int GL_TEXTURE31 =
		34015;
	
	@Api
	int GL_TEXTURE4 =
		33988;
	
	@Api
	int GL_TEXTURE5 =
		33989;
	
	@Api
	int GL_TEXTURE6 =
		33990;
	
	@Api
	int GL_TEXTURE7 =
		33991;
	
	@Api
	int GL_TEXTURE8 =
		33992;
	
	@Api
	int GL_TEXTURE9 =
		33993;
	
	@Api
	int GL_TEXTURE_2D =
		3553;
	
	@Api
	int GL_TEXTURE_COORD_ARRAY =
		32888;
	
	@Api
	int GL_TEXTURE_ENV =
		8960;
	
	@Api
	int GL_TEXTURE_ENV_COLOR =
		8705;
	
	@Api
	int GL_TEXTURE_ENV_MODE =
		8704;
	
	@Api
	int GL_TEXTURE_MAG_FILTER =
		10240;
	
	@Api
	int GL_TEXTURE_MIN_FILTER =
		10241;
	
	@Api
	int GL_TEXTURE_WRAP_S =
		10242;
	
	@Api
	int GL_TEXTURE_WRAP_T =
		10243;
	
	@Api
	int GL_TRIANGLES =
		4;
	
	@Api
	int GL_TRIANGLE_FAN =
		6;
	
	@Api
	int GL_TRIANGLE_STRIP =
		5;
	
	@Api
	int GL_TRUE =
		1;
	
	@Api
	int GL_UNPACK_ALIGNMENT =
		3317;
	
	@Api
	int GL_UNSIGNED_BYTE =
		5121;
	
	@Api
	int GL_UNSIGNED_SHORT =
		5123;
	
	@Api
	int GL_UNSIGNED_SHORT_4_4_4_4 =
		32819;
	
	@Api
	int GL_UNSIGNED_SHORT_5_5_5_1 =
		32820;
	
	@Api
	int GL_UNSIGNED_SHORT_5_6_5 =
		33635;
	
	@Api
	int GL_VENDOR =
		7936;
	
	@Api
	int GL_VERSION =
		7938;
	
	@Api
	int GL_VERTEX_ARRAY =
		32884;
	
	@Api
	int GL_XOR =
		5382;
	
	@Api
	int GL_ZERO =
		0;
	
	@Api
	void glActiveTexture(int __a);
	
	@Api
	void glAlphaFunc(int __a, float __b);
	
	@Api
	void glAlphaFuncx(int __a, int __b);
	
	@Api
	void glBindTexture(int __a, int __b);
	
	@Api
	void glBlendFunc(int __a, int __b);
	
	@Api
	void glClear(int __a);
	
	@Api
	void glClearColor(float __a, float __b, float __c, float __d);
	
	@Api
	void glClearColorx(int __a, int __b, int __c, int __d);
	
	@Api
	void glClearDepthf(float __a);
	
	@Api
	void glClearDepthx(int __a);
	
	@Api
	void glClearStencil(int __a);
	
	@Api
	void glClientActiveTexture(int __a);
	
	@Api
	void glColor4f(float __a, float __b, float __c, float __d);
	
	@Api
	void glColor4x(int __a, int __b, int __c, int __d);
	
	@Api
	void glColorMask(boolean __a, boolean __b, boolean __c, boolean __d);
	
	@Api
	void glColorPointer(int __a, int __b, int __c, Buffer __d);
	
	@Api
	void glCompressedTexImage2D(int __a, int __b, int __c, int __d, int __e,
		int __f, int __g, Buffer __h);
	
	@Api
	void glCompressedTexSubImage2D(int __a, int __b, int __c, int __d, int __e,
		int __f, int __g, int __h, Buffer __i);
	
	@Api
	void glCopyTexImage2D(int __a, int __b, int __c, int __d, int __e, int __f,
		int __g, int __h);
	
	@Api
	void glCopyTexSubImage2D(int __a, int __b, int __c, int __d, int __e,
		int __f, int __g, int __h);
	
	@Api
	void glCullFace(int __a);
	
	@Api
	void glDeleteTextures(int __a, int[] __b, int __c);
	
	@Api
	void glDeleteTextures(int __a, IntBuffer __b);
	
	@Api
	void glDepthFunc(int __a);
	
	@Api
	void glDepthMask(boolean __a);
	
	@Api
	void glDepthRangef(float __a, float __b);
	
	@Api
	void glDepthRangex(int __a, int __b);
	
	@Api
	void glDisable(int __a);
	
	@Api
	void glDisableClientState(int __a);
	
	@Api
	void glDrawArrays(int __a, int __b, int __c);
	
	@Api
	void glDrawElements(int __a, int __b, int __c, Buffer __d);
	
	@Api
	void glEnable(int __a);
	
	@Api
	void glEnableClientState(int __a);
	
	@Api
	void glFinish();
	
	@Api
	void glFlush();
	
	@Api
	void glFogf(int __a, float __b);
	
	@Api
	void glFogfv(int __a, float[] __b, int __c);
	
	@Api
	void glFogfv(int __a, FloatBuffer __b);
	
	@Api
	void glFogx(int __a, int __b);
	
	@Api
	void glFogxv(int __a, int[] __b, int __c);
	
	@Api
	void glFogxv(int __a, IntBuffer __b);
	
	@Api
	void glFrontFace(int __a);
	
	@Api
	void glFrustumf(float __a, float __b, float __c, float __d, float __e,
		float __f);
	
	@Api
	void glFrustumx(int __a, int __b, int __c, int __d, int __e, int __f);
	
	@Api
	void glGenTextures(int __a, int[] __b, int __c);
	
	@Api
	void glGenTextures(int __a, IntBuffer __b);
	
	@Api
	int glGetError();
	
	@Api
	void glGetIntegerv(int __a, int[] __b, int __c);
	
	@Api
	void glGetIntegerv(int __a, IntBuffer __b);
	
	@Api
	String glGetString(int __a);
	
	@Api
	void glHint(int __a, int __b);
	
	@Api
	void glLightModelf(int __a, float __b);
	
	@Api
	void glLightModelfv(int __a, float[] __b, int __c);
	
	@Api
	void glLightModelfv(int __a, FloatBuffer __b);
	
	@Api
	void glLightModelx(int __a, int __b);
	
	@Api
	void glLightModelxv(int __a, int[] __b, int __c);
	
	@Api
	void glLightModelxv(int __a, IntBuffer __b);
	
	@Api
	void glLightf(int __a, int __b, float __c);
	
	@Api
	void glLightfv(int __a, int __b, float[] __c, int __d);
	
	@Api
	void glLightfv(int __a, int __b, FloatBuffer __c);
	
	@Api
	void glLightx(int __a, int __b, int __c);
	
	@Api
	void glLightxv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glLightxv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glLineWidth(float __a);
	
	@Api
	void glLineWidthx(int __a);
	
	@Api
	void glLoadIdentity();
	
	@Api
	void glLoadMatrixf(float[] __a, int __b);
	
	@Api
	void glLoadMatrixf(FloatBuffer __a);
	
	@Api
	void glLoadMatrixx(int[] __a, int __b);
	
	@Api
	void glLoadMatrixx(IntBuffer __a);
	
	@Api
	void glLogicOp(int __a);
	
	@Api
	void glMaterialf(int __a, int __b, float __c);
	
	@Api
	void glMaterialfv(int __a, int __b, float[] __c, int __d);
	
	@Api
	void glMaterialfv(int __a, int __b, FloatBuffer __c);
	
	@Api
	void glMaterialx(int __a, int __b, int __c);
	
	@Api
	void glMaterialxv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glMaterialxv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glMatrixMode(int __a);
	
	@Api
	void glMultMatrixf(float[] __a, int __b);
	
	@Api
	void glMultMatrixf(FloatBuffer __a);
	
	@Api
	void glMultMatrixx(int[] __a, int __b);
	
	@Api
	void glMultMatrixx(IntBuffer __a);
	
	@Api
	void glMultiTexCoord4f(int __a, float __b, float __c, float __d, float __e);
	
	@Api
	void glMultiTexCoord4x(int __a, int __b, int __c, int __d, int __e);
	
	@Api
	void glNormal3f(float __a, float __b, float __c);
	
	@Api
	void glNormal3x(int __a, int __b, int __c);
	
	@Api
	void glNormalPointer(int __a, int __b, Buffer __c);
	
	@Api
	void glOrthof(float __a, float __b, float __c, float __d, float __e,
		float __f);
	
	@Api
	void glOrthox(int __a, int __b, int __c, int __d, int __e, int __f);
	
	@Api
	void glPixelStorei(int __a, int __b);
	
	@Api
	void glPointSize(float __a);
	
	@Api
	void glPointSizex(int __a);
	
	@Api
	void glPolygonOffset(float __a, float __b);
	
	@Api
	void glPolygonOffsetx(int __a, int __b);
	
	@Api
	void glPopMatrix();
	
	@Api
	void glPushMatrix();
	
	@Api
	void glReadPixels(int __a, int __b, int __c, int __d, int __e, int __f,
		Buffer __g);
	
	@Api
	void glRotatef(float __a, float __b, float __c, float __d);
	
	@Api
	void glRotatex(int __a, int __b, int __c, int __d);
	
	@Api
	void glSampleCoverage(float __a, boolean __b);
	
	@Api
	void glSampleCoveragex(int __a, boolean __b);
	
	@Api
	void glScalef(float __a, float __b, float __c);
	
	@Api
	void glScalex(int __a, int __b, int __c);
	
	@Api
	void glScissor(int __a, int __b, int __c, int __d);
	
	@Api
	void glShadeModel(int __a);
	
	@Api
	void glStencilFunc(int __a, int __b, int __c);
	
	@Api
	void glStencilMask(int __a);
	
	@Api
	void glStencilOp(int __a, int __b, int __c);
	
	@Api
	void glTexCoordPointer(int __a, int __b, int __c, Buffer __d);
	
	@Api
	void glTexEnvf(int __a, int __b, float __c);
	
	@Api
	void glTexEnvfv(int __a, int __b, float[] __c, int __d);
	
	@Api
	void glTexEnvfv(int __a, int __b, FloatBuffer __c);
	
	@Api
	void glTexEnvx(int __a, int __b, int __c);
	
	@Api
	void glTexEnvxv(int __a, int __b, int[] __c, int __d);
	
	@Api
	void glTexEnvxv(int __a, int __b, IntBuffer __c);
	
	@Api
	void glTexImage2D(int __a, int __b, int __c, int __d, int __e, int __f,
		int __g, int __h, Buffer __i);
	
	@Api
	void glTexParameterf(int __a, int __b, float __c);
	
	@Api
	void glTexParameterx(int __a, int __b, int __c);
	
	@Api
	void glTexSubImage2D(int __a, int __b, int __c, int __d, int __e, int __f,
		int __g, int __h, Buffer __i);
	
	@Api
	void glTranslatef(float __a, float __b, float __c);
	
	@Api
	void glTranslatex(int __a, int __b, int __c);
	
	@Api
	void glVertexPointer(int __a, int __b, int __c, Buffer __d);
	
	@Api
	void glViewport(int __a, int __b, int __c, int __d);
}


