const val MASTERCARD = "Mastercard"
const val MAESTRO = "Maestro"
const val VISA = "Visa"
const val WORLD = "Мир"
const val VK_PAY = "VK Pay"
const val COINS = 100
const val COMMISSION_MASTERCARD_MAESTRO = 0.6
const val COMMISSION_VISA_WORLD = 0.75
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
var sumVisaOfMonth = 0
var sumVisaOfDay = 0
var sumWorldOfMonth = 0
var sumWorldOfDay = 0
var sumVKPayOfMonth = 0
var sumVKPayOfDay = 0

fun main(args: Array<String>) {
    println(transfer(MASTERCARD, 35_356_21))
    println(transfer(MASTERCARD, 85_694_36))
    println(transfer(MASTERCARD, 65_356_85))
    sumMastercardOfDay = 0
    println(transfer(MASTERCARD, 10_000_00))
    println(transfer(MASTERCARD, 655_356_85))
    println(transfer(MAESTRO, 35_356_21))
    println(transfer(MAESTRO, 85_694_36))
    println(transfer(MAESTRO, 65_356_85))
    sumMaestroOfDay = 0
    println(transfer(MAESTRO, 10_000_00))
    println(transfer(MAESTRO, 655_356_85))
    println(transfer(VISA, 25_21))
    println(transfer(VISA, 35_356_21))
    println(transfer(VISA, 85_694_36))
    println(transfer(VISA, 65_356_85))
    sumVisaOfDay = 0
    println(transfer(VISA, 10_000_00))
    println(transfer(VISA, 655_356_85))
    println(transfer(WORLD, 25_21))
    println(transfer(WORLD, 35_356_21))
    println(transfer(WORLD, 85_694_36))
    println(transfer(WORLD, 65_356_85))
    sumWorldOfDay = 0
    println(transfer(WORLD, 10_000_00))
    println(transfer(WORLD, 655_356_85))
    println(transfer(transfer = 10_335_00))
    println(transfer(transfer = 5_335_00))
    sumVKPayOfDay = 0
    println(transfer(transfer = 5_335_00))
    println(transfer(transfer = 36_335_00))
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
        VISA -> //Проверяем лимит переводов в месяц
            if (transfer + sumVisaOfMonth < LIMIT_ON_MONTH) {
                transferVisa(card, transfer)
            } else {
                println("\nПревышен лимит переводов по карте $card в месяц")
                0
            }
        WORLD -> //Проверяем лимит переводов в месяц
            if (transfer + sumVisaOfMonth < LIMIT_ON_MONTH) {
                transferWorld(card, transfer)
            } else {
                println("\nПревышен лимит переводов по карте $card в месяц")
                0
            }
        VK_PAY -> //Проверяем лимит переводов в месяц
            if (transfer + sumVKPayOfMonth < LIMIT_ON_MONTH_VK) {
                transferVKPay(card, transfer)
            } else {
                println("\nПревышен лимит переводов по карте $card в месяц")
                -1
            }
        else -> {
            0
        }
    }
    return sum  //Возврат суммы перевода
}


fun transfer(card: String = VK_PAY, transfer: Int) : String {
    val sum = calcCommission(card, transfer)
    return if (sum == 0){
        "\nПо карте $card максимальный месячный лимит: " +
                "${LIMIT_ON_MONTH/COINS} Руб. ${LIMIT_ON_MONTH%COINS} коп. " +
                "\nМаксимальный дневной лимит: ${LIMIT_ON_DAY/COINS} Руб. ${LIMIT_ON_DAY%COINS} коп. "
    } else if(sum == -1) {
        "\nПо карте $card максимальный месячный лимит: " +
                "${LIMIT_ON_MONTH_VK/COINS} Руб. ${LIMIT_ON_MONTH_VK%COINS} коп. " +
                "\nМаксимальный дневной лимит: ${LIMIT_ON_DAY_VK/COINS} Руб. ${LIMIT_ON_DAY_VK%COINS} коп. "
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
            return 0 //Возвращает 0 для функции transfer
        } else {  //Проверяем сумму перевода без процентов
            if (sumMastercardOfMonth <= NO_COMMISSION) {
                sumMastercardOfDay += transfer
                sumMastercardOfMonth += transfer
                commission = if (sumMastercardOfMonth - NO_COMMISSION > 0) {//Расчет комиссии с учетом безпроцентной суммы
                    ((sumMastercardOfMonth - NO_COMMISSION) * COMMISSION_MASTERCARD_MAESTRO).toInt() + 20_00
                } else {
                    0
                }
            } else {//Расчет комиссии без учета безпроцентной суммы
                sumMastercardOfDay += transfer
                sumMastercardOfMonth += transfer
                commission = (transfer * COMMISSION_MASTERCARD_MAESTRO).toInt() + 20_00
            }
        }
    return commission + transfer //Возврат суммы перевода
}

fun transferMaestro(card: String, transfer: Int) : Int {
    var commission = 0
    //Проверяем лимит переводов в сутки
    if (sumMaestroOfDay + transfer > LIMIT_ON_DAY) {
        println("\nПревышен лимит переводов по карте $card в сутки")
        return 0 //Возвращает 0 для функции transfer
    } else {//Проверяем сумму перевода без процентов
        if (sumMaestroOfMonth <= NO_COMMISSION) {
            sumMaestroOfDay += transfer
            sumMaestroOfMonth += transfer
            commission = if (sumMaestroOfMonth - NO_COMMISSION > 0) {//Расчет комиссии с учетом безпроцентной суммы
                ((sumMaestroOfMonth - NO_COMMISSION) * COMMISSION_MASTERCARD_MAESTRO).toInt() + 20_00
            } else {
                0
            }
        } else {//Расчет комиссии без учета безпроцентной суммы
            sumMaestroOfDay += transfer
            sumMaestroOfMonth += transfer
            commission = (transfer * COMMISSION_MASTERCARD_MAESTRO).toInt() + 20_00
        }
    }
    return commission + transfer //Возврат суммы перевода
}

fun transferVisa(card: String, transfer: Int) : Int {
    var commission = 0
    //Проверяем лимит переводов в сутки
    if (sumVisaOfDay + transfer > LIMIT_ON_DAY) {
        println("\nПревышен лимит переводов по карте $card в сутки")
        return 0 //Возвращает 0 для функции transfer
    } else {
        sumVisaOfDay += transfer
        sumVisaOfMonth += transfer
        commission = (transfer * COMMISSION_VISA_WORLD).toInt() //Расчет комиссии
    }
    return if (commission > MIN_COMMISSION ) commission + transfer else transfer + MIN_COMMISSION //Возврат суммы перевода
}

fun transferWorld(card: String, transfer: Int) : Int {
    var commission = 0
    //Проверяем лимит переводов в сутки
    if (sumWorldOfDay + transfer > LIMIT_ON_DAY) {
        println("\nПревышен лимит переводов по карте $card в сутки")
        return 0 //Возвращает 0 для функции transfer
    } else {
        sumWorldOfDay += transfer
        sumWorldOfMonth += transfer
        commission = (transfer * COMMISSION_VISA_WORLD).toInt() //Расчет комиссии
    }
    return if (commission > MIN_COMMISSION ) commission + transfer else transfer + MIN_COMMISSION //Возврат суммы перевода
}

fun transferVKPay(card: String, transfer: Int) : Int {
    //Проверяем лимит переводов в сутки
    if (sumVKPayOfDay + transfer > LIMIT_ON_DAY_VK) {
        println("\nПревышен лимит переводов по карте $card в сутки")
        return -1 //Возвращает -1 для функции transfer
    } else {
        sumVKPayOfDay += transfer
        sumVKPayOfMonth += transfer
    }
    return transfer
}
