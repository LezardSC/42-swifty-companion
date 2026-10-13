package com.fortytwo.swifty.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class IntraUser(
    val id: Int,
    val login: String,
    val email: String,
    val phone: String? = null,
    @SerialName("usual_full_name") val fullName: String,
    val image: ProfileImage? = null,
    val location: String? = null,
    val wallet: Int = 0,
    @SerialName("correction_point") val correctionPoint: Int = 0,
    @SerialName("cursus_users") val cursusUsers: List<CursusUser> = emptyList(),
    @SerialName("projects_users") val projectsUsers: List<ProjectUser> = emptyList(),
) {
    val mainCursus: CursusUser?
        get() = cursusUsers.firstOrNull { it.cursus?.slug == MAIN_CURSUS_SLUG } ?: cursusUsers.lastOrNull()

    private companion object {
        const val MAIN_CURSUS_SLUG = "42cursus"
    }
}

@Serializable
data class ProfileImage(val link: String? = null)

@Serializable
data class CursusUser(
    val level: Double = 0.0,
    val skills: List<Skill> = emptyList(),
    val cursus: Cursus? = null
)

@Serializable
data class Cursus(val name: String, val slug: String? = null)

@Serializable
data class Skill(val name: String, val level: Double)

@Serializable
data class ProjectUser(
    val project: Project,
    @SerialName("final_mark") val finalMark: Int? = null,
    val status: String,
    @SerialName("validated?") val validated: Boolean? = null
)

@Serializable
data class Project(val name: String)

@Serializable
data class Coalition(
    val id: Int,
    val name: String,
    val color: String? = null,
    @SerialName("cover_url") val coverUrl: String? = null,
)
