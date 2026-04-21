package org.garis.pam.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import org.garis.pam.GlassTheme

@Composable
fun MarkdownText(text: String) {
    val annotatedString = remember(text) {
        androidx.compose.ui.text.buildAnnotatedString {
            val lines = text.split("\n")
            lines.forEachIndexed { index, line ->
                var processedLine = line
                
                // Handle List
                if (processedLine.trimStart().startsWith("- ")) {
                    append("  • ")
                    processedLine = processedLine.trimStart().substring(2)
                }

                // Handle Bold **text**
                val boldRegex = "\\*\\*(.*?)\\*\\*".toRegex()
                var lastIndex = 0
                boldRegex.findAll(processedLine).forEach { match ->
                    append(processedLine.substring(lastIndex, match.range.first))
                    withStyle(androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(match.groupValues[1])
                    }
                    lastIndex = match.range.last + 1
                }
                append(processedLine.substring(lastIndex))
                
                if (index < lines.size - 1) append("\n")
            }
        }
    }
    
    Text(
        text = annotatedString,
        color = GlassTheme.colors.TextSecond,
        fontSize = 15.sp,
        lineHeight = 24.sp
    )
}
