package jp.hibeiko.yarnshelf.repository

import android.util.Log
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner

interface MLKitRepository {
    fun getJanCode(searchItem: (String,String) -> Unit)
}

class MLKitRepositoryImpl(
    private val mlKitService: GmsBarcodeScanner,
) : MLKitRepository {
    override fun getJanCode(searchItem: (String,String) -> Unit) {
        mlKitService.startScan()
            .addOnSuccessListener { barcode ->
                // Task completed successfully
                searchItem("",barcode.rawValue ?: "")
                Log.i("MLKitRepositoryImpl","Task Success!!${barcode.rawValue}")
            }
            .addOnCanceledListener {
                // Task canceled
                Log.i("MLKitRepositoryImpl","Task Canceled")
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                if( e is MlKitException)
                    Log.i("MLKitRepositoryImpl","MLKitException has occured. ErrorCode:${e.errorCode}")
                else
                    Log.i("MLKitRepositoryImpl","Task failed")
                e.printStackTrace()
            }
    }
}