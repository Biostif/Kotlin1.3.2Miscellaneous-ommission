const val MASTERCARD = "Mastercard"
const val MAESTRO = "Maestro"
const val VISA = "Visa"
const val WORLD = "Мир"
const val VK_PAY = "VK Pay"
const val COINS = 100
const val COMMISSION_MASTERCARD_MAESTRO = 0.6
const val LIMIT_ON_DAY = 150_000_00
const val LIMIT_ON_MONTH = 600_000_00
const val NO_COMMISSION = 75_000_00
const val MIN_COMMISSION = 35_00
const val LIMIT_ON_DAY_VK = 15_000_00
const val LIMIT_ON_MONTH_VK = 40_000_00
var sumMastercardOfMonth = 0
var sumMaestroOfMonth = 0
var sumMastercardOfDay = 0
var sumMaestroOfDay = 0

fun main(args: Array<String>) {
    println(transfer(MASTERCARD, 85_267_35))
}

fun calcCommission(card: String, transfer: Int) : Int{
    val sum = when (card) {
        MASTERCARD -> //Проверяем лимит переводов в месяц
            if (transfer + sumMastercardOfMonth < LIMIT_ON_MONTH) {
            transferMastercard(card, transfer)
        } else {
            println("\nПревышен лимит переводов по карте $card в месяц")
            0
        }
        MAESTRO -> //Проверяем лимит переводов в месяц
            if (transfer + sumMaestroOfMonth < LIMIT_ON_MONTH) {
            transferMaestro(card, transfer)
        } else {
            println("\nПревышен лимит переводов по карте $card в месяц")
            0
        }
        else -> {
            0
        }
    }
    return sum
}


fun transfer(card: String = VK_PAY, transfer: Int) : String {
    val sum = calcCommission(card, transfer)
    return if (sum == 0){
        "\nПо карте $card максимальный месячный лимит: " +
                "${LIMIT_ON_MONTH/COINS} Руб. ${LIMIT_ON_MONTH%COINS} коп. " +
                "\nМаксимальный дневной лимит: ${LIMIT_ON_DAY/COINS} Руб. ${LIMIT_ON_DAY%COINS} коп. "
    } else {
        "\nПеревод по карте $card" +
                "\nСумма перевода составит: ${sum / COINS} Руб. " +
                "${sum % COINS} коп." +
                "\nКоммиссия: ${(sum - transfer) / COINS} Руб. " +
                "${(sum - transfer) % COINS} коп."
    }
}

fun transferMastercard(card: String, transfer: Int) : Int {
    var commission = 0
        //Проверяем лимит переводов в сутки
        if (sumMastercardOfDay + transfer > LIMIT_ON_DAY) {
            println("\nПревышен лимит переводов по карте $card в сутки")
            return 0
        } else {  //Проверяем сумму перевода без процентов
            if (sumMastercardOfMonth <= NO_COMMISSION) {
                sumMastercardOfDay += transfer
                sumMastercardOfMonth += transfer
                commission = if (sumMastercardOfMonth - NO_COMMISSION > 0) {//Расчет коммиссии с учетом безпроцентной суммы
                    ((sumMastercardOfMonth - NO_COMMISSION) * COMMISSION_MASTERCARD_MAESTRO).toInt() + 20_00
                } else {
                    0
                }
            } else {//Расчет коммиссии без учета безпроцентной суммы
                sumMastercardOfDay += transfer
                sumMastercardOfMonth += transfer
                commission = (transfer * COMMISSION_MASTERCARD_MAESTRO).toInt() + 20_00
            }
        }
    return commission + transfer
}

fun transferMaestro(card: String, transfer: Int) : Int {
    var commission = 0
    //Проверяем лимит переводов в сутки
    if (sumMaestroOfDay + transfer > LIMIT_ON_DAY) {
        println("\nПревышен лимит переводов по карте $card в сутки")
        return 0
    } else {//Проверяем сумму перевода без процентов
        if (sumMaestroOfMonth <= NO_COMMISSION) {
            sumMaestroOfDay += transfer
            sumMaestroOfMonth += transfer
            commission = if (sumMaestroOfMonth - NO_COMMISSION > 0) {//Расчет коммиссии с учетом безпроцентной суммы
                ((sumMaestroOfMonth - NO_COMMISSION) * COMMISSION_MASTERCARD_MAESTRO).toInt() + 20_00
            } else {
                0
            }
        } else {//Расчет коммиссии без учета безпроцентной суммы
            sumMaestroOfDay += transfer
            sumMaestroOfMonth += transfer
            commission = (transfer * COMMISSION_MASTERCARD_MAESTRO).toInt() + 20_00
        }
    }
    return commission + transfer
}
