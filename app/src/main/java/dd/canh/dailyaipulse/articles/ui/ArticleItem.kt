package dd.canh.dailyaipulse.articles.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dd.canh.dailyaipulse.articles.presentation.Article
import dd.canh.dailyaipulse.ui.theme.DailyAIPulseTheme

@Composable
fun ArticleItem(
    article: Article,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
        // NewsAPI sometimes sends an empty string instead of null.
        if (!article.imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = article.imageUrl,
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                error = ColorPainter(Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(
            text = article.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        article.description?.let { description ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = article.date,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArticleItemPreview() {
    DailyAIPulseTheme {
        ArticleItem(
            article = Article(
                title = "Former Prince Andrew moves out of Windsor's Royal Lodge",
                description = "A short description of the article.",
                imageUrl = null,
                date = "2 days ago",
                sourceName = "CNN",
            ),
        )
    }
}
