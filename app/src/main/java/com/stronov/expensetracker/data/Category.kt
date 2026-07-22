package com.stronov.expensetracker.data

/**
 * Predefined expense categories. Stored in the database by [Category.name].
 */
enum class Category(val label: String) {
    FOOD("Food"),
    TRANSPORT("Transport"),
    HOUSING("Housing"),
    UTILITIES("Utilities"),
    ENTERTAINMENT("Entertainment"),
    HEALTH("Health"),
    SHOPPING("Shopping"),
    OTHER("Other");

    companion object {
        /** Safe lookup that falls back to [OTHER] for unknown values. */
        fun fromName(value: String?): Category =
            entries.firstOrNull { it.name == value } ?: OTHER
    }
}
