package jp.hibeiko.yarnshelf.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

// @Database アノテーションには、Room がデータベースを構築できるように、複数の引数が必要です。
// entities のリストを持つ唯一のクラスとして YarnData を指定します。
// version を 1 に設定します。データベース テーブルのスキーマを変更するたびに、バージョン番号を増やす必要があります。
// スキーマのバージョン履歴のバックアップを保持しないように、exportSchema を false に設定します。
@Database(entities = [YarnData::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class YarnShelfDatabase : RoomDatabase() {
    abstract fun yarnDataDAO(): YarnDataDAO

    // アプリ全体で必要な RoomDatabase のインスタンスは 1 つのみであるため、RoomDatabase をシングルトンにします。
    companion object {
        // データベースに対する参照を保持します
        // volatile 変数の値はキャッシュに保存されません。
        @Volatile
       private var Instance:YarnShelfDatabase? = null
        fun getDatabase(context: Context): YarnShelfDatabase {
            // Instance が null の場合は synchronized{} ブロック内で初期化します。
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, YarnShelfDatabase::class.java, "yarnshelf_database")
                    .fallbackToDestructiveMigration() // 移行戦略
                    .build() // データベースインスタンス作成
                    .also { Instance = it } // 最近作成された db インスタンスへの参照を保持します。
            }
        }
    }
}