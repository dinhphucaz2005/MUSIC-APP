@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package nd.phuc.musicapp.core.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.TonalElevation
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.internal.Strings
import androidx.compose.material3.internal.getString
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import nd.phuc.musicapp.R
import nd.phuc.musicapp.constants.TopBarHeight
import nd.phuc.musicapp.ui.theme.MyMusicAppTheme
import kotlin.math.max
import kotlin.math.roundToInt

@ExperimentalMaterial3Api
@Preview
@Composable
private fun SearchBarPreview() {
    MyMusicAppTheme {
        val searchBarFocusRequester = remember { FocusRequester() }

        val (query, onQueryChange) = rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }
        var active by rememberSaveable {
            mutableStateOf(false)
        }
        val focusManager = LocalFocusManager.current

        val onActiveChange: (Boolean) -> Unit = { newActive ->
            active = newActive
            if (!newActive) {
                focusManager.clearFocus()
//                if (navigationItems.fastAny { it.route == navBackStackEntry?.destination?.route }) {
//                    onQueryChange(TextFieldValue())
//                }
            }
        }
        val onSearch: (String) -> Unit = {
            if (it.isNotEmpty()) {
                onActiveChange(false)
//                navController.navigate("search/${it.urlEncode()}")
            }
        }
        val searchBarScrollBehavior = appBarScrollBehavior(
            canScroll = {
                true
//                navBackStackEntry?.destination?.route?.startsWith("search/") == false &&
//                        (playerBottomSheetState.isCollapsed || playerBottomSheetState.isDismissed)
            }
        )
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SearchBar(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                active = active,
                onActiveChange = onActiveChange,
                scrollBehavior = searchBarScrollBehavior,
                placeholder = { Text(text = "Search") },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            when {
                                active -> onActiveChange(false)
//                                !navigationItems.fastAny { it.route == navBackStackEntry?.destination?.route } -> {
//                                    navController.navigateUp()
//                                }

                                else -> onActiveChange(true)
                            }
                        },
                        onLongClick = {
                            when {
                                active -> {}
//                                !navigationItems.fastAny { it.route == navBackStackEntry?.destination?.route } -> {
//                                    navController.backToMain()
//                                }

                                else -> {}
                            }
                        }
                    ) {
                        Icon(
                            painterResource(
                                R.drawable.ic_search
//                                if (active || !navigationItems.fastAny { it.route == navBackStackEntry?.destination?.route }) {
//                                    R.drawable.arrow_back
//                                } else {
//                                    R.drawable.search
//                                }
                            ),
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = {
                    if (active) {
                        if (query.text.isNotEmpty()) {
                            IconButton(
                                onClick = { onQueryChange(TextFieldValue("")) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        }
                        IconButton(
                            onClick = {
//                                searchSource = searchSource.toggle()
                            }
                        ) {
                            Icon(
                                painter = painterResource(
                                    R.drawable.ic_mic
//                                    when (searchSource) {
//                                        SearchSource.LOCAL -> R.drawable.library_music
//                                        SearchSource.ONLINE -> R.drawable.language
//                                    }
                                ),
                                contentDescription = null
                            )
                        }
                    }
//                    else if (navBackStackEntry?.destination?.route in topLevelScreens) {
//                        Box(
//                            contentAlignment = Alignment.Center,
//                            modifier = Modifier
//                                .size(48.dp)
//                                .clip(CircleShape)
//                                .clickable {
//                                    navController.navigate("settings")
//                                }
//                        ) {
//                            BadgedBox(
//                                badge = {
//                                    if (latestVersionName != BuildConfig.VERSION_NAME) {
//                                        Badge()
//                                    }
//                                }
//                            ) {
//
//                                Icon(
//                                    painter = painterResource(R.drawable.settings),
//                                    contentDescription = null
//                                )
//                            }
//                        }
//                    }
                },
                focusRequester = searchBarFocusRequester,
                modifier = Modifier.align(Alignment.TopCenter),
            ) {
                Crossfade(
                    targetState = "searchSource",
                    label = "",
                    modifier = Modifier
                        .fillMaxSize()
//                        .padding(bottom = if (!playerBottomSheetState.isDismissed) MiniPlayerHeight else 0.dp)
                        .navigationBarsPadding()
                ) { searchSource ->
                    when (searchSource) {
//                        SearchSource.LOCAL -> LocalSearchScreen(
//                            query = query.text,
//                            navController = navController,
//                            onDismiss = { onActiveChange(false) }
//                        )
//
//                        SearchSource.ONLINE -> OnlineSearchScreen(
//                            query = query.text,
//                            onQueryChange = onQueryChange,
//                            navController = navController,
//                            onSearch = {
//                                navController.navigate("search/${it.urlEncode()}")
//                                if (dataStore[PauseSearchHistoryKey] != true) {
//                                    database.query {
//                                        insert(SearchHistory(query = it))
//                                    }
//                                }
//                            },
//                            onDismiss = { onActiveChange(false) }
//                        )
                    }
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = SearchBarDefaults.inputFieldShape,
    colors: SearchBarColors = SearchBarDefaults.colors(),
    tonalElevation: Dp = TonalElevation,
    windowInsets: WindowInsets = WindowInsets.systemBars,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusRequester: FocusRequester = remember { FocusRequester() },
    content: @Composable ColumnScope.() -> Unit,
) {
    val heightOffsetLimit = with(LocalDensity.current) {
        -(TopBarHeight.toPx() + WindowInsets.systemBars.getTop(this))
    }
    SideEffect {
        if (scrollBehavior.state.heightOffsetLimit != heightOffsetLimit) {
            scrollBehavior.state.heightOffsetLimit = heightOffsetLimit
        }
    }

    val animationProgress: Float by animateFloatAsState(
        targetValue = if (active) 1f else 0f,
        animationSpec = tween(
            durationMillis = AnimationDurationMillis,
            easing = androidx.compose.animation.core.LinearEasing
        ),
        label = ""
    )

    val defaultInputFieldShape = SearchBarDefaults.inputFieldShape
    val defaultFullScreenShape = SearchBarDefaults.fullScreenShape
    val animatedShape by remember {
        derivedStateOf {
            when {
                shape == defaultInputFieldShape -> {
                    // The shape can only be animated if it's the default spec value
                    val animatedRadius = SearchBarCornerRadius * (1 - animationProgress)
                    RoundedCornerShape(CornerSize(animatedRadius))
                }

                animationProgress == 1f -> defaultFullScreenShape
                else -> shape
            }
        }
    }

    val topInset = windowInsets.asPaddingValues().calculateTopPadding()
    val startInset =
        windowInsets.asPaddingValues().calculateStartPadding(LocalLayoutDirection.current)
    val endInset = windowInsets.asPaddingValues().calculateEndPadding(LocalLayoutDirection.current)

    val topPadding = SearchBarVerticalPadding + topInset
    val animatedSurfaceTopPadding = lerp(topPadding, 0.dp, animationProgress)
    val animatedInputFieldPadding by remember {
        derivedStateOf {
            PaddingValues(
                start = startInset * animationProgress,
                top = topPadding * animationProgress,
                end = endInset * animationProgress,
                bottom = SearchBarVerticalPadding * animationProgress,
            )
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .offset {
                IntOffset(x = 0, y = scrollBehavior.state.heightOffset.roundToInt())
            },
        propagateMinConstraints = true
    ) {
        val height: Dp
        val width: Dp
        val startPadding: Dp
        val endPadding: Dp
        with(LocalDensity.current) {
            val startWidth = constraints.maxWidth.toFloat()
            val startHeight = max(constraints.minHeight, InputFieldHeight.roundToPx())
                .coerceAtMost(constraints.maxHeight)
                .toFloat()
            val endWidth = constraints.maxWidth.toFloat()
            val endHeight = constraints.maxHeight.toFloat()

            height = lerp(startHeight, endHeight, animationProgress).toDp()
            width = lerp(startWidth, endWidth, animationProgress).toDp()
            startPadding = lerp(
                (SearchBarHorizontalPadding + startInset).roundToPx().toFloat(),
                0f,
                animationProgress
            ).toDp()
            endPadding = lerp(
                (SearchBarHorizontalPadding + endInset).roundToPx().toFloat(),
                0f,
                animationProgress
            ).toDp()
        }

        Surface(
            shape = animatedShape,
            color = colors.containerColor,
            contentColor = contentColorFor(colors.containerColor),
            tonalElevation = tonalElevation,
            modifier = Modifier
                .padding(
                    top = animatedSurfaceTopPadding,
                    start = startPadding,
                    end = endPadding
                )
                .size(width = width, height = height)
        ) {
            Column {
                @Suppress("DEPRECATION")
                (SearchBarInputField(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        modifier = Modifier.padding(animatedInputFieldPadding),
        enabled = enabled,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = colors.inputFieldColors,
        interactionSource = interactionSource,
        focusRequester = focusRequester,
    ))

                if (animationProgress > 0) {
                    Column(Modifier.alpha(animationProgress)) {
                        HorizontalDivider(color = colors.dividerColor)
                        content()
                    }
                }
            }
        }
    }

    BackHandler(enabled = active) {
        onActiveChange(false)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarInputField(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = SearchBarDefaults.inputFieldColors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusRequester: FocusRequester = remember { FocusRequester() },
) {
    val focused = interactionSource.collectIsFocusedAsState().value

    val searchSemantics = getString(Strings.SearchBarSearch)
    val suggestionsAvailableSemantics = getString(Strings.SuggestionsAvailable)
    val textColor = LocalTextStyle.current.color.takeOrElse {
        colors.textColor(enabled, isError = false, focused = focused)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(InputFieldHeight)
    ) {
        if (leadingIcon != null) {
            Spacer(Modifier.width(SearchBarIconOffsetX))
            leadingIcon()
        }

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        // Must be PointerEventPass.Initial to observe events before the text field
                        // consumes them in the Main pass
                        awaitFirstDown(pass = PointerEventPass.Initial)
                        val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                        if (upEvent != null) {
                            onActiveChange(true)
                        }
                    }
                }
                .semantics {
                    contentDescription = searchSemantics
                    if (active) {
                        stateDescription = suggestionsAvailableSemantics
                    }
                }
                .onKeyEvent {
                    if (it.key == Key.Enter) {
                        onSearch(query.text)
                        return@onKeyEvent true
                    }
                    false
                },
            enabled = enabled,
            singleLine = true,
            textStyle = LocalTextStyle.current.merge(TextStyle(color = textColor)),
            cursorBrush = SolidColor(colors.cursorColor(isError = false)),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(query.text) }),
            interactionSource = interactionSource,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = query.text,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = placeholder,
                    shape = SearchBarDefaults.inputFieldShape,
                    colors = colors,
                    contentPadding = PaddingValues(),
                    container = {},
                )
            }
        )

        if (trailingIcon != null) {
            trailingIcon()
            Spacer(Modifier.width(SearchBarIconOffsetX))
        }
    }
}

// Measurement specs
val InputFieldHeight = 48.dp
private val SearchBarCornerRadius: Dp = InputFieldHeight / 2
internal val SearchBarVerticalPadding: Dp = 8.dp
internal val SearchBarHorizontalPadding: Dp = 12.dp

// Search bar has 16dp padding between icons and start/end, while by default text field has 12dp.
val SearchBarIconOffsetX: Dp = 4.dp

// Animation specs
private const val AnimationDurationMillis: Int = 300
