package jp.hibeiko.yarnshelf.data

import androidx.annotation.DrawableRes
import jp.hibeiko.yarnshelf.R
import java.text.SimpleDateFormat
import java.util.Date

data class YarnData(
    val yarnName: String,
    val yarnDescription: String,
    val lastUpdateDate: Date,
    @DrawableRes val drawableResourceId: Int
)

class DataSource(){
    fun loadData(): List<YarnData>{
        val formatter = SimpleDateFormat("yyyy/MM/dd")
        return listOf<YarnData>(
            YarnData("Seabright","1010 Seabright", formatter.parse("2024/04/01"), R.drawable.spin_1010_crpd_1625196651766_400),
            YarnData("Shaela","102 Shaela", formatter.parse("2024/04/02"), R.drawable.spin_102_crpd_1625194839510_400),
            YarnData("Night Hawk","1020 Night Hawk", formatter.parse("2024/04/03"), R.drawable.spin_1020_crpd_1625196650659_400),
            YarnData("Sholmit","103 Sholmit", formatter.parse("2024/04/04"), R.drawable.spin_103_crpd_1625194837544_400),
            YarnData("Natural White","104 Natural White", formatter.parse("2024/04/04"), R.drawable.spin_104_crpd_1625194838195_400),
            YarnData("Eesit","105 Eesit", formatter.parse("2024/04/05"), R.drawable.spin_105_crpd_1625194836934_400),
            YarnData("Mooskit","106 Mooskit", formatter.parse("2024/04/06"), R.drawable.spin_106_crpd_1625194835979_400),
            YarnData("Mogit","107 Mogit", formatter.parse("2024/04/07"), R.drawable.spin_107_crpd_1625194834874_400),
            YarnData("Night Hawk","1020 Night Hawk", formatter.parse("2024/04/08"), R.drawable.spin_1020_crpd_1625196650659_400),
        )
    }
}