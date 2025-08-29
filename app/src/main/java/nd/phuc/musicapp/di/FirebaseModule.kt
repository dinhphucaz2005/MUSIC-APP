package nd.phuc.musicapp.di

//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

//    @Provides
//    @Singleton
//    fun provideDatabaseReference(): DatabaseReference {
//        return FirebaseDatabase.getInstance().reference
//    }
//
//    @Provides
//    @Singleton
//    fun provideStoreReference(): StorageReference {
//        return FirebaseStorage.getInstance().reference
//    }
}