package jp.hibeiko.yarnshelf.data

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import jp.hibeiko.yarnshelf.R
import java.util.Date

@Entity
data class YarnData(
    @PrimaryKey(autoGenerate = true)
    val yarnId: Int = 0,
    val janCode: String = "",
    val yarnName: String = "",
    val yarnDescription: String = "",
    val lastUpdateDate: Date = Date(),
    val imageUrl: String = "",
    @DrawableRes val drawableResourceId: Int = R.drawable.not_found
)
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
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
//            YarnData(8,"Night Hawk","1020 Night Hawk", formatter.parse("2024/04/08"), R.drawable.spin_101_crpd_1625194841231_400),
//        )
//    }
//}
//insert into yarndata(yarnId,janCode,yarnName,yarnDescription,lastUpdateDate,imageUrl,drawableResourceId)
//values
//(0, '10001','Seabright','1010 Seabright',1718605302247,'','2130968608'),
//(1, '10002','Shaela','102 Shaela',1718605302247,'','2130968602'),
//(2, '10003','Night Hawk','1020 Night Hawk',1718605302247,'','2130968601'),
//(3, '10004','Sholmit','103 Sholmit',1718605302247,'','2130968603'),
//(4, '10005','Natural White','104 Natural White',1718605302247,'','2130968604'),
//(5, '10006','Eesit','105 Eesit',1718605302247,'','2130968605'),
//(6, '10007','Mooskit','106 Mooskit',1718605302247,'','2130968606'),
//(7, '10008','Mogit','107 Mogit',1718605302247,'','2130968607'),
//(8, '10009','Natural Black','101 Natural Black(Shetland Black)',1718605302247,'','2130968600'),
