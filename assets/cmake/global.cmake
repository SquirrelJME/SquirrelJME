# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Globals

# Used for Fossil and/or CI/CD
define_property(TARGET PROPERTY SQUIRRELJME_GRADLE_BUILD
	BRIEF_DOCS "Is this a Gradle build?"
	FULL_DOCS "Is this a Gradle build?")
define_property(TARGET PROPERTY SQUIRRELJME_OUTPUT_PATH
	BRIEF_DOCS "The resultant output path, used for later upload."
	FULL_DOCS "The resultant output path, used for later upload.")
define_property(TARGET PROPERTY SQUIRRELJME_OUTPUT_TYPE
	BRIEF_DOCS "The resultant output type, used for later upload."
	FULL_DOCS "The resultant output type, used for later upload.")
define_property(TARGET PROPERTY SQUIRRELJME_TEST_RESULTS_DIR
	BRIEF_DOCS "Directory where test results are stored."
	FULL_DOCS "Directory where test results are stored.")

# Directory where the natives are placed
define_property(TARGET PROPERTY SQUIRRELJME_SYSTEM
	BRIEF_DOCS "The system this is for."
	FULL_DOCS "The system this is for.")
define_property(TARGET PROPERTY SQUIRRELJME_ARCH
	BRIEF_DOCS "The architecture this is for."
	FULL_DOCS "The architecture this is for.")
define_property(TARGET PROPERTY SQUIRRELJME_CORE_NATIVE_PATH
	BRIEF_DOCS "Path where core natives are placed."
	FULL_DOCS "Path where core natives are placed.")
define_property(TARGET PROPERTY SQUIRRELJME_EMULATOR_NATIVE_PATH
	BRIEF_DOCS "Path where emulator natives are placed."
	FULL_DOCS "Path where emulator natives are placed.")

# Compilers and generators that are available for the system
define_property(GLOBAL PROPERTY SQUIRRELJME_KNOWN_COMPILERS
	BRIEF_DOCS "The compilers that are known to the system."
	FULL_DOCS "The compilers that are known to the system.")
define_property(GLOBAL PROPERTY SQUIRRELJME_KNOWN_GENERATORS
	BRIEF_DOCS "The generators that are known to the system."
	FULL_DOCS "The generators that are known to the system.")

# Determine the basename of a path
function(squirreljme_basename_path dest src)
	# Get positions of the last slashes
	string(FIND "${src}" "/" fs REVERSE)
	string(FIND "${src}" "\\" bs REVERSE)

	# Bump both up by one, to exclude the slash
	math(EXPR fs "${fs} + 1")
	math(EXPR bs "${bs} + 1")

	# Has forward slash last
	if("${fs}" GREATER "${bs}")
		string(SUBSTRING "${src}" ${fs} -1 result)

	# Has backslash last
	elseif("${bs}" GREATER "${fs}")
		string(SUBSTRING "${src}" ${bs} -1 result)

	# Has neither last, or not found (both -1)
	else()
		set(result "${src}")
	endif()

	# Return the result of it
	set(${dest} "${result}" PARENT_SCOPE)
endfunction()

# Creates a temporary path
function(squirreljme_temp_path result)
	# Make sure the directory exists first
	file(MAKE_DIRECTORY "${CMAKE_BINARY_DIR}/temp/")

	# Create a random seed
	unset(random)
	string(RANDOM LENGTH 8
		ALPHABET "qwertyuiopasdfghjklzxcvbnm"
		random)

	# Make up a file for it
	set(${result} "${CMAKE_BINARY_DIR}/temp/${random}.tmp"
		PARENT_SCOPE)
endfunction()

# Truncate and make a better looking name
function(squirreljme_truncate_name result input)
	# Normalize
	string(MAKE_C_IDENTIFIER "${input}" input)
	string(TOLOWER "${input}" input)

	# Do not make it too long
	string(LENGTH "${input}" inputLen)
	if("${inputLen}" GREATER "4")
		# Remove from the length
		math(EXPR inputLen "${inputLen} - 2")

		# Only keep the beginning and the end of the string
		string(SUBSTRING "${input}" 0 2 pfx)
		string(SUBSTRING "${input}" "${inputLen}" 2 sfx)

		# Recombine
		set(input "${pfx}${sfx}")
	endif()

	# Give the result
	set(${result} "${input}" PARENT_SCOPE)
endfunction()

# Copy properties from one target to another
function(squirreljme_copy_properties fromTarget toTarget)
	# Remove the from and to targets
	list(REMOVE_AT ARGV 0)
	list(REMOVE_AT ARGV 0)

	# Go through each property
	foreach(property IN LISTS ARGV)
		# Get the value
		unset(value)
		get_target_property(value ${fromTarget} ${property})

		# Copy it over, if set
		if(NOT "${value}" STREQUAL "")
			set_target_properties(${toTarget} PROPERTIES
				"${property}" "${value}")
		endif()
	endforeach()
endfunction()

# Determine name for property
function(squirreljme_natives_order_name result systemNormal archNormal)
	# Calculate it
	set(${result} SQUIRRELJME_TARGET_${systemNormal}_${archNormal}
		PARENT_SCOPE)
endfunction()

# Determines rule name for an native
function(squirreljme_natives_rule_name result systemNormal archNormal)
	# Calculate it
	if("${systemNormal}" STREQUAL "standalone")
		set(${result} standalone.${archNormal}
			PARENT_SCOPE)
	else()
		set(${result} natives.${systemNormal}.${archNormal}
			PARENT_SCOPE)
	endif()
endfunction()

# Determines the rule names for configure and build steps for the compiler
function(squirreljme_compiler_rule_names resultConfigure resultBuild
	systemNormal archNormal method)
	# Determine the base native name
	unset(baseRuleName)
	squirreljme_natives_rule_name(baseRuleName ${systemNormal} ${archNormal})

	# Determine the rule names for configure and build
	set(${resultConfigure} "${baseRuleName}.${method}.configure"
		PARENT_SCOPE)
	set(${resultBuild} "${baseRuleName}.${method}.build"
		PARENT_SCOPE)
endfunction()

# Determine paths for the configure and build steps
function(squirreljme_natives_build_path resultPath systemNormal archNormal)
	# Determine resultant path
	set(${resultPath}
		"${CMAKE_BINARY_DIR}/natives.build/${systemNormal}/${archNormal}/"
		PARENT_SCOPE)
endfunction()

# Determines the UV Path
function(squirreljme_uv_path result input)
	# Set the result
	set(${result} "${SQUIRRELJME_UV_DIR}/${input}"
		PARENT_SCOPE)
endfunction()

# Determines the UV Path
function(squirreljme_natives_download_uv_path result systemNormal archNormal)
	# Set the result
	squirreljme_uv_path(uvPath
		"natives-${systemNormal}-${archNormal}.zip")
	set(${result} "${uvPath}" PARENT_SCOPE)
endfunction()

# Define global properties for known natives
foreach(compilerMap IN LISTS SQUIRRELJME_KNOWN_NATIVES)
	# Obtain back the system and architecture
	squirreljme_unmap(systemNormal 0 "${compilerMap}")
	squirreljme_unmap(archNormal 1 "${compilerMap}")

	# Determine the names
	squirreljme_natives_order_name(orderName ${systemNormal} ${archNormal})

	# Define it
	define_property(GLOBAL PROPERTY ${orderName}
		BRIEF_DOCS "Target order for ${systemNormal}/${archNormal}."
		FULL_DOCS "Target order for ${systemNormal}/${archNormal}.")
endforeach()
