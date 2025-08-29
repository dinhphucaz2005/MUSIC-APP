package nd.phuc.musicapp

//import android.content.Context
//import nd.phuc.musicapp.other.data.LocalDataSource
//import nd.phuc.musicapp.other.data.RoomDataSource
//import nd.phuc.musicapp.other.data.database.AppDAO
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import kotlinx.coroutines.runBlocking
//import org.junit.Before
//import org.junit.Rule
//import org.junit.runner.RunWith
//import org.junit.runners.JUnit4
//import javax.inject.Inject
//
//@HiltAndroidTest
//@RunWith(JUnit4::class)
//class RoomDataSourceTest {
//    companion object {
//        private const val TAG = "RoomDataSourceTest"
//    }
//
//    @get:Rule
//    val hiltRule = HiltAndroidRule(this)
//
//    @Inject
//    lateinit var dao: AppDAO
//
//    @Inject
//    lateinit var localDataSource: LocalDataSource
//
//    @Inject
//    lateinit var roomDataSource: RoomDataSource
//
//    @Inject
//    lateinit var context: Context
//
//    @Before
//    fun setUp(): Unit = runBlocking {
//        hiltRule.inject()
//    }
//}