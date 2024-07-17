package jp.hibeiko.yarnshef.dummy

import jp.hibeiko.yarnshelf.repository.MLKitRepository

class MLKitDummyRepository : MLKitRepository {
    override fun getJanCode(searchItem: (String, String) -> Unit) {
        searchItem("","1234567890123")
    }
}

class MLKitDummyErrorRepository : MLKitRepository {
    override fun getJanCode(searchItem: (String, String) -> Unit) {
        throw Exception()
    }
}