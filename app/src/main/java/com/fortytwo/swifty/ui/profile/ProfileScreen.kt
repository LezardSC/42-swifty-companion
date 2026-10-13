package com.fortytwo.swifty.ui.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fortytwo.swifty.data.model.Coalition
import com.fortytwo.swifty.data.model.IntraUser
import com.fortytwo.swifty.data.model.ProjectUser
import com.fortytwo.swifty.data.model.Skill
import com.fortytwo.swifty.ui.common.UiState
import com.fortytwo.swifty.ui.theme.Failed
import com.fortytwo.swifty.ui.theme.Validated

private const val DISPLAY_MAX = 20.0
private val CoverHeight = 140.dp
private val AvatarSize = 112.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    login: String,
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val coalition by viewModel.coalition.collectAsStateWithLifecycle()
    LaunchedEffect(login) { viewModel.load(login) }

    // Insets are already applied by the activity's Scaffold
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = { Text(login) },
                windowInsets = WindowInsets(0),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (val s = state) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Error -> ErrorMessage(s, Modifier.align(Alignment.Center))
                is UiState.Success -> ProfileContent(s.data, coalition)
                is UiState.Idle -> Unit
            }
        }
    }
}

@Composable
private fun ErrorMessage(state: UiState.Error, modifier: Modifier = Modifier) {
    Text(
        text = state.message,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier.padding(24.dp)
    )
}

@Composable
private fun ProfileContent(user: IntraUser, coalition: Coalition?) {
    val accent by animateColorAsState(
        targetValue = coalition?.color?.toColorOrNull() ?: MaterialTheme.colorScheme.primary,
        label = "accent"
    )
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item { ProfileHeader(user, coalition, accent) }
        item { SkillsSection(user, accent) }
        item { ProjectsSection(user) }
    }
}

@Composable
private fun ProfileHeader(user: IntraUser, coalition: Coalition?, accent: Color) {
    val cursusUser = user.mainCursus
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(Modifier.fillMaxWidth().height(CoverHeight + AvatarSize / 2)) {
            Box(Modifier.fillMaxWidth().height(CoverHeight).background(accent)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(coalition?.coverUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            AsyncImage(
                model = user.image?.link,
                contentDescription = "${user.login}\'s profile photo",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(AvatarSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .border(4.dp, MaterialTheme.colorScheme.background, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(user.fullName, style = MaterialTheme.typography.titleLarge)
        Text("@${user.login}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (coalition != null) {
            Text(coalition.name, style = MaterialTheme.typography.labelLarge, color = accent)
        }
        Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(20.dp))
            LevelBar(cursusUser?.level ?: 0.0, cursusUser?.cursus?.name, accent)
            Spacer(Modifier.height(16.dp))
            ProfileInfoRow("Email", user.email)
            ProfileInfoRow("Phone", user.phone ?: "hidden")
            ProfileInfoRow("Location", user.location ?: "not on campus")
            ProfileInfoRow("Wallet", "${user.wallet}₳")
            ProfileInfoRow("Evaluation points", "${user.correctionPoint}")
        }
    }
}

@Composable
private fun LevelBar(level: Double, cursusName: String?, accent: Color) {
    val whole = level.toInt()
    val progress = (level - whole).toFloat()
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text("Level $whole", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                if (cursusName != null) {
                    Spacer(Modifier.width(8.dp))
                    Text(cursusName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text("${(progress * 100).toInt()}%", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(6.dp))
        AnimatedBar(progress, accent, height = 10.dp)
    }
}

@Composable
private fun SkillsSection(user: IntraUser, accent: Color) {
    val skills = user.mainCursus?.skills.orEmpty().sortedByDescending { it.level }
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text("Skills", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        if (skills.isEmpty()) {
            Text("No skills recorded yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        skills.forEachIndexed { index, skill -> SkillRow(skill, accent, delayMillis = index * 60) }
    }
}

@Composable
private fun ProjectsSection(user: IntraUser) {
    val projects = user.projectsUsers.sortedByDescending { it.finalMark ?: -1 }
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text("Projects", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        if (projects.isEmpty()) {
            Text("No projects attempted yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        projects.forEach { p -> ProjectRow(p) }
    }
}

@Composable
private fun ProfileInfoRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun SkillRow(skill: Skill, accent: Color, delayMillis: Int) {
    val fraction = (skill.level / DISPLAY_MAX).coerceIn(0.0, 1.0).toFloat()
    Column(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(skill.name, style = MaterialTheme.typography.bodyMedium)
            Text(
                "lvl ${"%.2f".format(skill.level)} - ${(fraction * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(4.dp))
        AnimatedBar(fraction, accent, height = 6.dp, delayMillis = delayMillis)
    }
}

@Composable
private fun AnimatedBar(target: Float, color: Color, height: Dp, delayMillis: Int = 0) {
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }
    val fraction by animateFloatAsState(
        targetValue = if (started) target else 0f,
        animationSpec = tween(durationMillis = 900, delayMillis = delayMillis, easing = FastOutSlowInEasing),
        label = "bar"
    )
    Box(
        Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
    ) {
        Box(
            Modifier
                .fillMaxWidth(fraction)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(color)
        )
    }
}

@Composable
private fun ProjectRow(p: ProjectUser) {
    val passed = p.validated == true
    Row(
        Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(p.project.name, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f, fill = false))
        Spacer(Modifier.width(12.dp))
        when {
            p.status != "finished" -> StatusChip("in progress", MaterialTheme.colorScheme.onSurfaceVariant)
            passed -> StatusChip("${p.finalMark ?: 0}", Validated, icon = { Icon(Icons.Filled.Check, contentDescription = "validated", tint = Validated, modifier = Modifier.size(14.dp)) })
            else -> StatusChip("${p.finalMark ?: 0}", Failed, icon = { Icon(Icons.Filled.Close, contentDescription = "failed", tint = Failed, modifier = Modifier.size(14.dp)) })
        }
    }
}

@Composable
private fun StatusChip(text: String, color: Color, icon: (@Composable () -> Unit)? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        if (icon != null) {
            icon()
            Spacer(Modifier.width(4.dp))
        }
        Text(text, color = if (icon != null) MaterialTheme.colorScheme.onSurface else color, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
    }
}

private fun String.toColorOrNull(): Color? =
    runCatching { Color(android.graphics.Color.parseColor(this)) }.getOrNull()
