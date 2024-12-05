package com.example.musicapp

//import android.content.Context
//import android.util.Log
//import com.example.musicapp.other.data.LocalDataSource
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import kotlinx.coroutines.runBlocking
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.junit.runners.JUnit4
//import javax.inject.Inject

//@HiltAndroidTest
//@RunWith(JUnit4::class)
//class LocalDataSourceTest {
//    companion object {
//        private const val TAG = "LocalDataSourceTest"
//    }
//
//    @get:Rule
//    val hiltRule = HiltAndroidRule(this)
//
//    @Inject
//    lateinit var localDataSource: LocalDataSource
//
//    @Inject
//    lateinit var context: Context
//
//    @Before
//    fun setUp(): Unit = runBlocking {
//        hiltRule.inject()
//    }
//
//    @Test
//    fun testGetLocalFile(): Unit = runBlocking {
////        val songs = localDataSource.fetch(context)
////        Log.d(TAG, "test_get_local_file: $songs")
//    }
//
//}