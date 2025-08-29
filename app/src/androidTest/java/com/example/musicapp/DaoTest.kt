package nd.phuc.musicapp

//import android.util.Log
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import nd.phuc.musicapp.other.data.database.AppDAO
//import nd.phuc.musicapp.other.data.database.entity.PlayListEntity
//import nd.phuc.musicapp.other.data.database.entity.SongEntity
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import kotlinx.coroutines.runBlocking
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import javax.inject.Inject
//
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class DaoTest {
//
//    companion object {
//        private const val TAG = "DaoTest"
//    }
//
//    @get:Rule
//    val hiltRule = HiltAndroidRule(this)
//
//
//    @Inject
//    lateinit var dao: AppDAO
//
//    @Before
//    fun setUp(): Unit = runBlocking {
//        hiltRule.inject()
//    }
//
//    @Test
//    fun testGetPlayLists(): Unit = runBlocking {
//        val playLists = dao.getPlayLists()
//        Log.d(TAG, "testGetPlayLists: $playLists")
//    }
//
//}