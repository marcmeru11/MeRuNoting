package com.marcmeru.merunoting.ui.view.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(timestamp: Long): String {
    return if (timestamp <= 0L) "Fecha desconocida"
    else SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(timestamp))
}