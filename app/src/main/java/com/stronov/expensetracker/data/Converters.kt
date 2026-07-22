package com.stronov.expensetracker.data

import androidx.room.TypeConverter

/**
 * Room type converters for non-primitive columns.
 */
class Converters {
    @TypeConverter
    fun fromCategory(category: Category): String = category.name

    @TypeConverter
    fun toCategory(value: String): Category = Category.fromName(value)
}
