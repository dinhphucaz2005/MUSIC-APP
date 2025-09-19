package nd.phuc.core.extension

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.composable

abstract class Route {
    abstract val route: String
}


fun NavGraphBuilder.routeComposable(
    route: Route,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(
        route = route.route,
        arguments = arguments,
        deepLinks = deepLinks,
        content = content,
    )
}

fun NavController.navigateRoute(
    route: Route,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    this.navigate(route.route, navOptions, navigatorExtras)
}
