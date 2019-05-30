# qr-emvco-decoder-kotlin

# How to use?

```
let qrEmvData = 00020101021143520016com.mymerchantar0128http://localhost:3344/p/931850150011203611100015204970053030325802AR5921SERGIO MATIAS GUALINO6004CABA63044edf
decodeEMVQR(qr: qrEmvData)

// This function returns 
/*
 MERCHANT_ID_DATA 00 = 20361110001
 COUNTRY_CODE = AR
 MERCHANT_NAME = SERGIO MATIAS GUALINO
 MERCHANT_CITY = CABA
 MERCHANT_URL_DATA 00 = com.mymerchantar
 MERCHANT_URL_DATA 01 = http://localhost:3344/p/9318
 MERCHANT_CATEGORY_CODE = 9700
 CURRENCY_CODE = 032
*/
```


## Define keys 

```
val PAYLOAD_FORMAT_INDICATOR = "00"
val POINT_INITIATION_METHOD = "01"
val MERCHANT_URL_DATA = "43"
val MERCHANT_ID_DATA = "50"
val MERCHANT_CATEGORY_CODE = "52"
val CURRENCY_CODE = "53"
val TRANSACTIONAL_AMOUNT = "54"
val COUNTRY_CODE = "58"
val MERCHANT_NAME = "59"
val MERCHANT_CITY = "60"
val CRC = "63"

val keys = mapOf(
    MERCHANT_URL_DATA to "MERCHANT_URL_DATA",
    MERCHANT_ID_DATA to "MERCHANT_ID_DATA",
    MERCHANT_CATEGORY_CODE to "MERCHANT_CATEGORY_CODE",
    CURRENCY_CODE to "CURRENCY_CODE",
    COUNTRY_CODE to "COUNTRY_CODE",
    MERCHANT_NAME to "MERCHANT_NAME",
    MERCHANT_CITY to "MERCHANT_CITY"
)

val emvKeys = arrayListOf(MERCHANT_URL_DATA, MERCHANT_ID_DATA)

data class TLVParsed(val key: String, val value: String, val remain: String)
```

## Functions

`parseData` recibes TLV (Tag, Length, Value) string and returns the key, the value and the rest of the string.

```
fun parseData(data: String): TLVParsed? {
    val key = data.substring(0, 2)

    val temp = data.substring(2)
    val lengthString = temp.substring(0, 2)
    lengthString.toIntOrNull()?.let { length ->
        val value = temp.substring(2, 2 + length)
        val remain = temp.substring(2 + length)

        return TLVParsed(key, value, remain)
    }

    return null
}
```

`decodeEMV` recibes a valid QR EMV data (TLV) and returns dictionary formed with the keys and values included in the TLV data.

```
fun decodeEMV(qrData: String): Map<String, String>? {
    var data = qrData
    var dic = mutableMapOf<String, String>()

    while (data.isNotEmpty()) {
        parseData(data)?.let {
            data = it.remain

            if (it.key.isNotEmpty() && it.value.isNotEmpty()) {
                dic[it.key] = it.value
            }
        }
    }

    return dic
}
```

`decodeEMVQR` prints all parsed data transforming the key to a readable string and parsing a value when is a TLV data.

```
fun decodeEMVQR(qr: String): Map<String, String>? {
    decodeEMV(qr)?.let { mapDecode ->
        var map = mutableMapOf<String, String>()
        keys.forEach {
            val key = it.key

            mapDecode[key]?.let { value ->
                if (emvKeys.contains(key)) {
                    decodeEMV(value)?.let { emvdic ->
                        emvdic.forEach { dic ->
                            keys[key]?.let {
                                map[it] = dic.value
                            }
                        }
                    }
                } else {
                    keys[key]?.let {
                        map[it] = value
                    }
                }
            }
        }

        return map
    } ?: run {
        return null
    }
}
```
