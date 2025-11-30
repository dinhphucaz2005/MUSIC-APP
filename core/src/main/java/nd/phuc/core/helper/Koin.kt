package nd.phuc.core.helper

import android.content.ComponentCallbacks
import org.koin.android.ext.android.get
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

@OptIn(KoinInternalApi::class)
inline fun <reified T : Any> ComponentCallbacks.getFromKoin(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T = get(qualifier, parameters)
