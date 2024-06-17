package jp.hibeiko.yarnshelf.data

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity
data class YarnData(
    @PrimaryKey(autoGenerate = true)
    val yarnId: Int = 0,
    val janCode: String,
    val yarnName: String,
    val yarnDescription: String,
    val lastUpdateDate: Date,
    val imageUrl: String,
    @DrawableRes val drawableResourceId: Int
)
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}

//class DataSource(){
//    fun loadData(): List<YarnData>{
//        val formatter = SimpleDateFormat("yyyy/MM/dd")
//        return listOf<YarnData>(
//            YarnData(0,"Seabright","1010 Seabright", formatter.parse("2024/04/01"), R.drawable.spin_1010_crpd_1625196651766_400),
//            YarnData(1,"Shaela","102 Shaela", formatter.parse("2024/04/02"), R.drawable.spin_102_crpd_1625194839510_400),
//            YarnData(2,"Night Hawk","1020 Night Hawk", formatter.parse("2024/04/03"), R.drawable.spin_1020_crpd_1625196650659_400),
//            YarnData(3,"Sholmit","103 Sholmit", formatter.parse("2024/04/04"), R.drawable.spin_103_crpd_1625194837544_400),
//            YarnData(4,"Natural White","104 Natural White", formatter.parse("2024/04/04"), R.drawable.spin_104_crpd_1625194838195_400),
//            YarnData(5,"Eesit","105 Eesit", formatter.parse("2024/04/05"), R.drawable.spin_105_crpd_1625194836934_400),
//            YarnData(6,"Mooskit","106 Mooskit", formatter.parse("2024/04/06"), R.drawable.spin_106_crpd_1625194835979_400),
//            YarnData(7,"Mogit","107 Mogit", formatter.parse("2024/04/07"), R.drawable.spin_107_crpd_1625194834874_400),
//            YarnData(8,"Night Hawk","1020 Night Hawk", formatter.parse("2024/04/08"), R.drawable.spin_1020_crpd_1625196650659_400),
//        )
//    }
//}
//insert into yarndata(yarnId,yarnName,yarnDescription,lastUpdateDate,drawableResourceId)
//values
//(0, 'Seabright','1010 Seabright','Mon Apr 01 00:00:00 GMT 2024','2130968599'),
//(1, 'Shaela','102 Shaela','Tue Apr 02 00:00:00 GMT 2024','2130968602'),
//(2, 'Night Hawk','1020 Night Hawk','Wed Apr 03 00:00:00 GMT 2024','2130968601'),
//(3, 'Sholmit','103 Sholmit','Thu Apr 04 00:00:00 GMT 2024','2130968603'),
//(4, 'Natural White','104 Natural White','Thu Apr 04 00:00:00 GMT 2024','2130968604'),
//(5, 'Eesit','105 Eesit','Fri Apr 05 00:00:00 GMT 2024','2130968605'),
//(6, 'Mooskit','106 Mooskit','Sat Apr 06 00:00:00 GMT 2024','2130968606'),
//(7, 'Mogit','107 Mogit','Sun Apr 07 00:00:00 GMT 2024','2130968607'),
//(8, 'Night Hawk','1020 Night Hawk','Mon Apr 08 00:00:00 GMT 2024','2130968601');
