package Methodo_test


class Cypher {

    fun cypher(s: Char, key : Int): Char {
        if (s.isLowerCase() || key < 0){
           throw IllegalArgumentException()
        }

        var keyy = key

        if (keyy > 26) {
            keyy = keyy % 26
        }

        if ((s.code + keyy) > 90) {
            return (s.code + keyy-26).toChar()
        } else {
            return (s.code + keyy).toChar()

        }
    }
}

